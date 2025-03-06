package com.jwzt.statistic.action.api;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.utils.IpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.web.RequestUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.VideoInfo;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.UserInfoMng;
import com.jwzt.statistic.manager.VideoInfoMng;
import com.jwzt.statistic.pool.IpAddressPool;

@Controller
public class UserAct {
	private static final Logger log = LogManager.getLogger();

	/**
	 * 用户动作通知
	 * @param action
	 * @param userRequestListJson
	 * @param mp
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/user/{action}")
	public String request(@PathVariable(value = "action") String action, @RequestBody String userRequestListJson,
			ModelMap mp, HttpServletRequest request) {
		try {
			log.debug("UserAct.request, action:{}, userRequestListJson:{}", action, userRequestListJson);
			if (StringUtils.isBlank(action)) {
				throw new Exception("无效的请求");
			}
			if (null == userRequestListJson) {
				throw new Exception("request is blank.");
			}
			List<Map<String, Object>> userRequestList = JsonUtils.jsonToObject(userRequestListJson, List.class);
			if (null != userRequestList) {
				for (Map<String, Object> userRequestMap : userRequestList) {
					if (null != userRequestMap) {
						Long time = (Long) userRequestMap.get("time");
						String userId = (String) userRequestMap.get("userId");
						Integer roomId = (Integer) userRequestMap.get("roomId");
						Object liveEventIdObj = userRequestMap.get("liveEventId");
						Long liveEventId;
						if (null != liveEventIdObj && Long.class.isInstance(liveEventIdObj)) {
							liveEventId = (Long) liveEventIdObj;
						} else {
							liveEventId = ((Integer) liveEventIdObj).longValue();
						}
						Integer type = (Integer) userRequestMap.get("type");
						String ip = (String) userRequestMap.get("ip");
						if (StringUtils.isBlank(ip)) {
							continue;
						}
						if (null == time) {
							continue;
						}

						// userId格式为 ： {x}_{sessionId} x为0是游客，x大于0为注册用户
						String userIdValue;
						Integer userType;
						String[] split = userId.split("_");
						if ("0".equals(split[0])) {
							// 游客则以sessionId为用户名
							userIdValue = split[1];
							userType = RequestLog.USER_TYPE_UNREGISTERED_USER;
						} else {
							userType = RequestLog.USER_TYPE_REGISTERED_USER;
							userIdValue = split[0];
						}
						
						//判断是否是IPV6
						long ipCode = 1;
						if(ip.indexOf(":")!=-1){
							
						}else{
							IpAddressPool.execute(ip);
							ipCode = IpUtils.ipToLong(ip);
						}
						
						
						RequestLog requestLog = new RequestLog(roomId, liveEventId, userIdValue, userType, type,
								ipCode);

						LiveInfo liveInfo = null;
						if (null != liveEventId) {
							liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
							if (null != liveInfo) {
								Integer enterpriseId = liveInfo.getEnterpriseId();
								requestLog.setEnterpriseId(enterpriseId);
							}
						}

						Object videoIdObj = userRequestMap.get("fileId");
						Long videoId = null;
						if (null != videoIdObj) {
							if (Long.class.isInstance(videoIdObj)) {
								videoId = (Long) videoIdObj;
							} else {
								videoId = ((Integer) videoIdObj).longValue();
							}
						}
						requestLog.setVideoId(videoId);
						if (null != videoId) {
							VideoInfo videoInfo = videoInfoMng.getBeanById(videoId);
							if (null != videoInfo) {
								Integer enterpriseId = videoInfo.getEnterpriseId();
								requestLog.setEnterpriseId(enterpriseId);
							}
						}

						if ("enter".equals(action)) {
							if (null == liveInfo) {
								continue;
							}
							Timestamp liveBeginTime = liveInfo.getLiveBeginTime();
							Timestamp liveEndTime = liveInfo.getLiveEndTime();
							if (liveBeginTime.getTime() > time
									|| (null != liveEndTime && liveEndTime.getTime() < time)) {
								// 不在直播场次开始和结束时间之间，不处理
								continue;
							}
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_ENTER);
						} else if ("leave".equals(action)) {
							if (null == liveInfo) {
								continue;
							}
							Timestamp liveBeginTime = liveInfo.getLiveBeginTime();
							Timestamp liveEndTime = liveInfo.getLiveEndTime();
							if (liveBeginTime.getTime() > time
									|| (null != liveEndTime && liveEndTime.getTime() < time)) {
								// 不在直播场次开始和结束时间之间，不处理
								continue;
							}
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_LEAVE);
						} else if ("like".equals(action)) {//点赞
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_LIKE);
							if (null == requestLog.getVideoId()) {
								requestLog.setProp1("1");
							} else {
								requestLog.setProp1("2");
							}
						} else if ("comment".equals(action)) {//评论
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_COMMENT);
							if (null == requestLog.getVideoId()) {
								requestLog.setProp1("1");
							} else {
								requestLog.setProp1("2");
							}
						} else if ("view".equals(action)) {//观看
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_VIEW);
						} else if ("concern".equals(action)) {//关注
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_CONCERN);
							Integer enterpriseId = (Integer) userRequestMap.get("enterpriseId");
							requestLog.setEnterpriseId(enterpriseId);
						} else if ("unconcern".equals(action)) {//取消关注
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_UNCONCERN);
							Integer enterpriseId = (Integer) userRequestMap.get("enterpriseId");
							requestLog.setEnterpriseId(enterpriseId);
						} else if ("sign".equals(action)) {//签到
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_SIGN);
						} else if ("sendGift".equals(action)) {//送礼物
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_GIFT);
							String money = (String) userRequestMap.get("money");
							requestLog.setProp1(money);
							String number = (String) userRequestMap.get("number");
							requestLog.setProp2(number);
						} else if ("reward".equals(action)) {//打赏
							requestLog.setLogType(RequestLog.LOG_TYPE_USER_REWARD);
							String money = (String) userRequestMap.get("money");
							requestLog.setProp1(money);
						} else {
							throw new Exception("无效的请求");
						}
						requestLog.setCreateTime(new Timestamp(time));
						requestLogMng.save(requestLog);
					}
				}
			}
		} catch (Exception e) {
			log.debug("UserAct.request error.", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}

		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	/**
	 * 分享
	 * @param type
	 * @param liveEventId
	 * @param roomId
	 * @param userId
	 * @param mp
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/share")
	public String share(Integer type, Long liveEventId, Integer roomId, String userId, ModelMap mp,
			HttpServletRequest request) {
		try {
			log.debug("UserAct.share, liveEventId:{}, userId:{}, type:{}, roomId:{}", liveEventId, userId, type,
					roomId);
			Integer logType = RequestLog.LOG_TYPE_USER_SHARE;
			String ip = RequestUtils.getIpAddr(request);
			Long time = System.currentTimeMillis();
			// userId格式为 ： {x}_{sessionId} x为0是游客，x大于0为注册用户
			String userIdValue;
			Integer userType;
			String[] split = userId.split("_");
			if ("0".equals(split[0])) {
				// 游客则以sessionId为用户名
				userIdValue = split[1];
				userType = RequestLog.USER_TYPE_UNREGISTERED_USER;
			} else {
				userType = RequestLog.USER_TYPE_REGISTERED_USER;
				userIdValue = split[0];
			}
			IpAddressPool.execute(ip);
			long ipCode = IpUtils.ipToLong(ip);
			RequestLog requestLog = new RequestLog(roomId, liveEventId, userIdValue, userType, type, logType, ipCode);
			requestLog.setCreateTime(new Timestamp(time));
			requestLogMng.save(requestLog);
		} catch (Exception e) {
			log.debug("UserAct.request error.", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}

		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}
	
	/**
	 * 新增用户
	 * @param userRequestListJson
	 * @param mp
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/user/save")
	public String save(@RequestBody String userRequestListJson, ModelMap mp, HttpServletRequest request) {
		try {
			log.debug("UserAct.save, userRequestListJson:{} ", userRequestListJson);
			if (null == userRequestListJson) {
				throw new Exception("request is blank.");
			}
			List<Map<String, Object>> userRequestList = JsonUtils.jsonToObject(userRequestListJson, List.class);
			if (null != userRequestList) {
				for (Map<String, Object> userRequestMap : userRequestList) {
					if (null != userRequestMap) {
						Integer code = (Integer) userRequestMap.get("code");
						if (null != code && code == 1) {
							List<Map<?, ?>> dataList = (List<Map<?, ?>>) userRequestMap.get("data");
							if (null != dataList) {
								for (Map<?, ?> dataMap : dataList) {
									try {
										userInfoMng.saveOrUpdateFromDataMap(dataMap);
									} catch (Exception e) {
										log.warn("saveUserInfo error.", e);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.debug("UserAct.save error.", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}

		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	/**
	 * 更新用户信息
	 * @param userRequestListJson
	 * @param mp
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/user/update")
	public String update(@RequestBody String userRequestListJson, ModelMap mp, HttpServletRequest request) {
		try {
			log.debug("UserAct.save, userRequestListJson:{} ", userRequestListJson);
			if (null == userRequestListJson) {
				throw new Exception("request is blank.");
			}
			List<Map<String, Object>> userRequestList = JsonUtils.jsonToObject(userRequestListJson, List.class);
			if (null != userRequestList) {
				for (Map<String, Object> userRequestMap : userRequestList) {
					if (null != userRequestMap) {
						Integer code = (Integer) userRequestMap.get("code");
						if (null != code && code == 1) {
							List<Map<?, ?>> dataList = (List<Map<?, ?>>) userRequestMap.get("data");
							if (null != dataList) {
								for (Map<?, ?> dataMap : dataList) {
									try {
										userInfoMng.saveOrUpdateFromDataMap(dataMap);
									} catch (Exception e) {
										log.warn("saveUserInfo error.", e);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.debug("UserAct.save error.", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	@Autowired
	private RequestLogMng requestLogMng;
	@Autowired
	private LiveInfoMng liveInfoMng;
	@Autowired
	private UserInfoMng userInfoMng;
	@Autowired
	private VideoInfoMng videoInfoMng;
}
