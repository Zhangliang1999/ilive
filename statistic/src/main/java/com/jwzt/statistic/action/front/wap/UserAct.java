package com.jwzt.statistic.action.front.wap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.constants.RenderJsonConstants;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.LiveStatisticResult;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.LiveStatisticResultMng;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.UserViewLogMng;
import com.jwzt.statistic.utils.ConfigUtils;

@Controller
public class UserAct {
	private static final Logger log = LogManager.getLogger();

	@RequestMapping("/user/statistic/viewTime")
	public String viewTimeStatistic(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(id);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null == liveEndTime) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_LIVE_NOT_END, "直播未结束", null);
				return "renderJson";
			}
			Timestamp lastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == lastStatisticTime || lastStatisticTime.getTime() < liveEndTime.getTime()) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_STATISTIC_NOT_END, "统计未结束", null);
				return "renderJson";
			}
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Map<String, Long>> minuteStatisticSeries = ConfigUtils.getMinuteStatisticSeries();
			if (null != minuteStatisticSeries) {
				for (Map<String, Long> minuteStatisticSerieMap : minuteStatisticSeries) {
					Long start = minuteStatisticSerieMap.get("start");
					Long end = minuteStatisticSerieMap.get("end");
					Long userNum;
					if (null != end) {
						userNum = userViewLogMng.countUserNumByParams(id, null, start * 60, end * 60, null, null);
					} else {
						userNum = userViewLogMng.countUserNumByParams(id, null, start * 60, null, null, null);
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("start", start);
					map.put("end", end);
					map.put("userNum", userNum);
					list.add(map);
				}
			}
			RenderJsonUtils.addSuccess(mp, list);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping("/user/statistic/location")
	public String locationStatistic(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(id);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null == liveEndTime) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_LIVE_NOT_END, "直播未结束", null);
				return "renderJson";
			}
			Timestamp lastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == lastStatisticTime || lastStatisticTime.getTime() < liveEndTime.getTime()) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_STATISTIC_NOT_END, "统计未结束", null);
				return "renderJson";
			}
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng.listByFlag(id,
					LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW, null, null);
			if (null != locationStatisticResultList) {
				
				String defaultProvinceName = ConfigUtils.get(ConfigUtils.DEFAULT_PROVINCE_NAME);
				String dName = defaultProvinceName;
				Iterator<LocationStatisticResult> iterator = locationStatisticResultList.iterator();
				while(iterator.hasNext()) {
					LocationStatisticResult next = iterator.next();
					if (next.getProvinceName()!=null) {
						dName = next.getProvinceName();
						break;
					}
				}
				
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						String provinceName = locationStatisticResult.getProvinceName();
						Long totalNum = locationStatisticResult.getTotalNum();
						if (StringUtils.isBlank(provinceName) || "xx".equalsIgnoreCase(provinceName)) {
							provinceName = defaultProvinceName;
							//provinceName = dName;
						}
						boolean isExisted = false;
						for (Map<String, Object> dataMap : list) {
							String provinceNameInMap = (String) dataMap.get("provinceName");
							if (StringUtils.isNotBlank(provinceName) && provinceName.equals(provinceNameInMap)) {
								Long totalNumInMap = (Long) dataMap.get("totalNum");
								totalNum = totalNum + totalNumInMap;
								dataMap.put("totalNum", totalNum);
								isExisted = true;
								break;
							}
						}
						if (!isExisted) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("provinceName", provinceName);
							map.put("totalNum", totalNum);
							list.add(map);
						}
					}
				}
			}
			RenderJsonUtils.addSuccess(mp, list);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping("/user/statistic/source")
	public String sourceStatistic(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(id);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null == liveEndTime) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_LIVE_NOT_END, "直播未结束", null);
				return "renderJson";
			}
			Timestamp lastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == lastStatisticTime || lastStatisticTime.getTime() < liveEndTime.getTime()) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_STATISTIC_NOT_END, "统计未结束", null);
				return "renderJson";
			}
			Map<String, Object> data = new HashMap<String, Object>();
			Long androidUserNum = 0L;
			Long iosUserNum = 0L;
			Long wapUserNum = 0L;
			Long pcUserNum = 0L;
			Long otherUserNum = 0L;
			Long liveId = liveInfo.getId();
			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			if (null != liveStatisticResult) {
				androidUserNum = liveStatisticResult.getAndroidUserNum();
				iosUserNum = liveStatisticResult.getIosUserNum();
				wapUserNum = liveStatisticResult.getWapUserNum();
				pcUserNum = liveStatisticResult.getPcUserNum();
				otherUserNum = liveStatisticResult.getOtherUserNum();
			}
			data.put("androidUserNum", androidUserNum);
			data.put("iosUserNum", iosUserNum);
			data.put("wapUserNum", wapUserNum);
			data.put("pcUserNum", pcUserNum);
			data.put("otherUserNum", otherUserNum);
			RenderJsonUtils.addSuccess(mp, data);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping("/user/statistic/register")
	public String registerStatistic(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(id);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null == liveEndTime) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_LIVE_NOT_END, "直播未结束", null);
				return "renderJson";
			}
			Timestamp lastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == lastStatisticTime || lastStatisticTime.getTime() < liveEndTime.getTime()) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_STATISTIC_NOT_END, "统计未结束", null);
				return "renderJson";
			}
			Map<String, Object> data = new HashMap<String, Object>();
			Long newRegisterUserNum = 0L;
			Long oldRegisterUserNum = 0L;
			Long otherRegisterUserNum = 0L;
			Long liveId = liveInfo.getId();
			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			if (null != liveStatisticResult) {
				newRegisterUserNum = liveStatisticResult.getNewRegisterUserNum();
				oldRegisterUserNum = liveStatisticResult.getOldRegisterUserNum();
				otherRegisterUserNum = liveStatisticResult.getOtherRegisterUserNum();
			}
			data.put("newRegisterUserNum", newRegisterUserNum);
			data.put("oldRegisterUserNum", oldRegisterUserNum);
			data.put("otherRegisterUserNum", otherRegisterUserNum);
			RenderJsonUtils.addSuccess(mp, data);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@Autowired
	private LiveInfoMng liveInfoMng;
	@Autowired
	private UserViewLogMng userViewLogMng;
	@Autowired
	private LocationStatisticResultMng locationStatisticResultMng;
	@Autowired
	private LiveStatisticResultMng liveStatisticResultMng;
}
