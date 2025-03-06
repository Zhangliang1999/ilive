package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UN_LOGIN;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppConcernVo;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.action.front.vo.AppQuestionAnswerVo;
import com.bvRadio.iLive.iLive.action.front.vo.AppViewRecordVo;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveViewRecord;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomSuggestMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewRecordMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.util.SystemMd5Util;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

/**
 * 个人周边环境的接口请求类
 * 
 * @author administrator
 */
@Controller
@RequestMapping(value = "/app/personal")
public class ILivePersonalEnvAct {

	Logger logger = LoggerFactory.getLogger(ILivePersonalEnvAct.class);

	/**
	 * 消息管理类
	 */
	@Autowired
	private ILiveMessageMng iLiveMessageMng;

	/**
	 * 观看浏览记录管理类
	 */
	@Autowired
	private ILiveViewRecordMng iLiveViewRecordMng;

	/**
	 * 企业关注管理类
	 */
	@Autowired
	private ILiveEnterpriseFansMng iLiveEnterpriseFansMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	/**
	 * 邀请 白名单
	 */
	@Autowired
	ILiveViewWhiteBillMng iLiveViewWhiteBillMng;

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;

	@Autowired
	private ILiveRoomSuggestMng iLiveRoomSuggestMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	private static final Integer randomTime = 2000;

	/**
	 * 个人密码修改
	 */
	@RequestMapping(value = "/updatepwd.jspx")
	public void updateUserPassword(Long userId, String password, String phoneNum, String vpassword,
			HttpServletRequest request, HttpServletResponse response) {
		
		JSONObject resultJson = new JSONObject();
		
		
		UserBean appUser = ILiveUtils.getAppUser(request);
		
//		if (appUser == null) {
//			resultJson.put("code", UN_LOGIN);
//			resultJson.put("message", "用户未登录");
//			resultJson.put("data", new JSONObject());
//			ResponseUtils.renderJson(request, response, resultJson.toString());
//			return;
//		}
		/*
		long parseLong = Long.parseLong(appUser.getUserId());
		if (parseLong != userId.longValue()) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "非本人操作，被禁止");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		*/
		
		Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "updatepwd_" + phoneNum);
		String defaultVpassword = ConfigUtils.get("defaultVpassword");
		if (defaultVpassword.equals(vpassword)) {
			
		}else {
			if (cacheInfo == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			if (cacheInfo.isExpired()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码过期");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			if (!vpassword.equals(cacheInfo.getValue())) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码错误");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
		}
		ILiveManager iLiveManager = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
		if (iLiveManager == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			String salt = String.valueOf(new Random().nextInt(randomTime));
			String md5Pwd = SystemMd5Util.md5(password, salt);
			iLiveManager.setSalt(salt);
			iLiveManager.setUserPwd(md5Pwd);
			iLiveManagerMng.updateLiveManager(iLiveManager);
			IliveAppRetInfo retInfo = this.buildAppRet(iLiveManager, iLiveManager.getEnterPrise());
			cacheInfo.setExpired(true);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "密码修改成功");
			resultJson.put("data", new JSONObject(retInfo));
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作异常");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
		return;
	}

	/**
	 * 绑定手机号
	 */
	@RequestMapping(value = "/bindMobile.jspx")
	public void bindPersonMobile(Long userId, String phoneNum, String vpassword, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean appUser = ILiveUtils.getAppUser(request);
		JSONObject resultJson = new JSONObject();
		if (phoneNum == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "手机号必须输入");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		if (appUser == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		long parseLong = Long.parseLong(appUser.getUserId());
		if (parseLong != userId.longValue()) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "非本人操作，被禁止");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "bindMobile_" + phoneNum);
		String defaultVpassword = ConfigUtils.get("defaultVpassword");
		if (defaultVpassword.equals(vpassword)) {
		}else {
			if (cacheInfo == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			if (cacheInfo.isExpired()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码过期");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			if (!vpassword.equals(cacheInfo.getValue())) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码错误");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
		}
		
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		if (iLiveManager == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
		try {
			ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByPhoneNumber(phoneNum);
			if (iLiveMangerByMobile == null) {
				iLiveManager.setMobile(phoneNum);
				iLiveManagerMng.updateLiveManager(iLiveManager);
				if(iLiveManager.getCertStatus()==4){
					ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPrise(iLiveManager.getEnterpriseId().intValue());
					iLiveEnterprise.setUserPhone(phoneNum);
					iLiveEnterpriseMng.update(iLiveEnterprise);
				}
			} else {
				String mobile=iLiveManager.getMobile();
					if(mobile==null||"".equals(mobile)){
						iLiveMangerByMobile.setWxUnionId(iLiveManager.getWxUnionId());
						iLiveManager.setIsDel(1);
						iLiveManagerMng.updateLiveManager(iLiveManager);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						
					}else{
						if(iLiveMangerByMobile.getCertStatus()==4){
							resultJson.put("status", "0");
							resultJson.put("msg", "不能换绑已认证企业的手机号");
							ResponseUtils.renderJson(request, response, resultJson.toString());
							return;
						}else{
							iLiveManager.setMobile(phoneNum);
							iLiveManager.setWxUnionId(iLiveMangerByMobile.getWxUnionId());
							iLiveManagerMng.updateLiveManager(iLiveManager);
							if(iLiveManager.getCertStatus()==4){
								ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPrise(iLiveManager.getEnterpriseId().intValue());
								iLiveEnterprise.setUserPhone(phoneNum);
								iLiveEnterpriseMng.update(iLiveEnterprise);
							}
							iLiveMangerByMobile.setIsDel(1);
							iLiveMangerByMobile.setWxUnionId("");
							iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						
					}
				}
			}
			// 清除sessionId，重新登录
			request.getSession().setAttribute("appUser", null);
			request.getSession().setAttribute("tempLoginInfo", null);
			IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, iLiveMangerByMobile.getEnterPrise());
			if (null != cacheInfo) {
				cacheInfo.setExpired(true);
			}
			
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "手机号绑定成功");
			resultJson.put("data", new JSONObject(retInfo));
			
			//重新进行登录
			ILiveManager iLiveManger = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
			if (iLiveManger != null) {
				UserBean userBean = new UserBean();
				userBean.setUserId(String.valueOf(iLiveManger.getUserId()));
				userBean.setNickname(iLiveManger.getNailName());
				userBean.setUserThumbImg(iLiveManger.getUserImg());
				userBean.setUsername(iLiveManger.getMobile());
				userBean.setCertStatus(iLiveManger.getCertStatus());

				userBean.setLevel(iLiveManger.getLevel()==null?0:iLiveManger.getLevel());
				ILiveUtils.setAppUser(request, userBean);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作异常");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
		return;
	}

	
	/**
	 * 我的问答API
	 */
	@RequestMapping(value = "questionAnswer.jspx")
	public void questionAnswer(Long userId, String loginToken, Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			List<AppQuestionAnswerVo> anqlist = iLiveMessageMng.getQuestionAndAnwer(userId, pageNo == null ? 1 : pageNo,
					pageSize == null ? 15 : pageSize);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取列表成功");
			resultJson.put("data", anqlist);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "发生异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 观看记录API
	 */
	@RequestMapping(value = "viewhistory.jspx")
	public void viewHistory(Long userId, String loginToken, Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> sqlMap = new HashMap<>();
		sqlMap.put("pageNo", pageNo);
		sqlMap.put("pageSize", pageSize);
		sqlMap.put("userId", userId);
		JSONObject resultJson = new JSONObject();
		try {
			List<AppViewRecordVo> recordList = iLiveViewRecordMng.getILiveViewRecords(sqlMap);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取列表成功");
			resultJson.put("data", recordList);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "发生异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 删除观看记录
	 */
	@RequestMapping(value = "record/delete.jspx")
	public void deleteRecordList(String recordIds,Long recordId, Long userId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		String []records=recordIds.split(",");
		for(String record : records){
			ILiveViewRecord liveViewRecord = iLiveViewRecordMng.getLiveViewRecordById(Long.parseLong(record));
			if (liveViewRecord != null) {
				boolean equals = liveViewRecord.getManagerId().equals(userId);
				if (equals) {
					liveViewRecord.setDeleteStatus(1);
					iLiveViewRecordMng.updateLiveRecord(liveViewRecord);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "删除成功");
					resultJson.put("data", new JSONObject());
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "该记录不属于此用户:"+record);
					resultJson.put("data", new JSONObject());
					break;
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "该记录不存在:"+record);
				resultJson.put("data", new JSONObject());
				break;
			}
		}
		
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	@RequestMapping(value = "concern/top.jspx")
	public void concertTop(Integer enterpriseId, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveEnterpriseFans entFans = iLiveEnterpriseFansMng.findEnterpriseFans(enterpriseId, userId);
		if (entFans != null && entFans.getIsDel() != 1) {
			// entFans.setTopFlag(1);
			iLiveEnterpriseFansMng.updateEnterFans(entFans);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			resultJson.put("data", new JSONObject());
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "关注的企业不存在");
			resultJson.put("data", new JSONObject());

		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	@RequestMapping(value = "concern/ifConcern.jspx")
	public void ifConcert(Integer enterpriseId, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveEnterpriseFans entFans = iLiveEnterpriseFansMng.findEnterpriseFans(enterpriseId, userId);
		if (entFans != null && entFans.getIsDel() != 1) {
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "已关注");
			resultJson.put("data", new JSONObject());
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "未关注");
			resultJson.put("data", new JSONObject());

		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 我的关注直播列表API（直播中>预告>结束)
	 */
	@RequestMapping(value = "concernlive.jspx")
	public void myConcernLive(Long userId, String loginToken, Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			List<AppConcernVo> list = iLiveEnterpriseFansMng.getMyConvernLive(userId, pageNo == null ? 1 : pageNo,
					pageSize == null ? 15 : pageSize);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取列表成功");
			resultJson.put("data", list);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "发生异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 被邀请直播列表API
	 */
	@RequestMapping(value = "invite.jspx")
	public void inviteMe(Long userId, String phoneNum, String loginToken, HttpServletRequest request, Integer pageNo,
			Integer pageSize, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			List<AppILiveRoom> list = iLiveViewWhiteBillMng.getMyInviteLive(userId, phoneNum,
					pageNo == null ? 1 : pageNo, pageSize == null ? 15 : pageSize);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取列表成功");
			resultJson.put("data", list);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "发生异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 个人投诉建议
	 */
	@RequestMapping(value = "/suggestinfo.jspx")
	public void complainSuggest(Long userId, Integer roomId, String content, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			if (roomId != null) {
				ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
				if (iliveRoom == null) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播间不存在");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				} else {
					iLiveRoomSuggestMng.addILiveRoomSuggest(iliveRoom, content, userId);
				}
			} else {
				iLiveRoomSuggestMng.addILiveRoomSuggest(null, content, userId);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "提交投诉建议成功");
			resultJson.put("data", new JSONObject());
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "发生异常");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 构建成appRet 企业对象
	 * 
	 * @param iLiveMangerByMobile
	 * @param enterPrise
	 * @return
	 */
	private IliveAppRetInfo buildAppRet(ILiveManager iLiveMangerByMobile, ILiveEnterprise enterPrise) {
		IliveAppRetInfo retInfo = new IliveAppRetInfo();
		retInfo.setUserId(iLiveMangerByMobile.getUserId());
		retInfo.setMobile(iLiveMangerByMobile.getMobile());
		retInfo.setNailName(iLiveMangerByMobile.getNailName());
		retInfo.setJpushId(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getJpushId()));
		retInfo.setLoginToken(iLiveMangerByMobile.getLoginToken());
		retInfo.setUserName(StringPatternUtil.convertEmpty(iLiveMangerByMobile.getUserName()));
		retInfo.setCertStatus(iLiveMangerByMobile.getCertStatus() == null ? 0 : iLiveMangerByMobile.getCertStatus());
		retInfo.setPhoto(iLiveMangerByMobile.getUserImg() == null ? "" : iLiveMangerByMobile.getUserImg());
		if (enterPrise != null) {
			ILiveAppEnterprise enterprise = new ILiveAppEnterprise();
			enterprise.setCertStatus(enterPrise.getCertStatus());
			enterprise.setEnterpriseId(enterPrise.getEnterpriseId());
			enterprise.setEnterpriseImg(StringPatternUtil.convertEmpty(enterPrise.getEnterpriseImg()));
			enterprise.setEnterpriseName(enterPrise.getEnterpriseName());
			enterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
			enterprise.setHomePageUrl(StringPatternUtil.convertEmpty(enterprise.getHomePageUrl()));
			retInfo.setEnterprise(enterprise);
		}
		return retInfo;
	}

}
