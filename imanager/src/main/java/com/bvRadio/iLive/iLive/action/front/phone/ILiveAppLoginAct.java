package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppDiyFormVo;
import com.bvRadio.iLive.iLive.action.front.vo.AppDiyformReceiver;
import com.bvRadio.iLive.iLive.action.front.vo.DiyFormModelVo;
import com.bvRadio.iLive.iLive.action.front.vo.DiyFormVo;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.BBSDiyformDataMng;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.sms.ILiveSmgServerCenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping(value = "/appuser")
public class ILiveAppLoginAct {

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILiveLiveRoomMng liveRoomMng;

	@Autowired
	private BBSDiyformMng bbsDiyformMng;

	@Autowired
	private BBSDiymodelMng bbsDiymodelMng;

	@Autowired
	private BBSDiyformDataMng dataMng;

	@Autowired
	private ILiveEnterpriseFansMng enterpriseFansMng;

	/**
	 * 登录接口 1是账号密码登录 2是验证码登录
	 * 
	 * @param username
	 * @param password
	 * @param vpassword
	 * @param jpushId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/login.jspx")
	public void login(String phoneNum, String password, String vpassword, Integer type, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		Cache cacheInfo = null;
		try {
			if (phoneNum == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "手机号必须输入");
				resultJson.put("data", new JSONObject());
			} else if (type == 1 && password == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请输入密码");
				resultJson.put("data", new JSONObject());
			} else if (type == 2 && vpassword == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请输入验证码");
				resultJson.put("data", new JSONObject());
			} else {
				cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "login_" + phoneNum);
				if (type == 2) {
					if (cacheInfo == null) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "验证码不通过");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					} else if (cacheInfo.isExpired()) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "验证码已过期,请重新发送");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					} else if (!vpassword.equals(cacheInfo.getValue())) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "验证码不通过");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					} else {
						ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
						if (iLiveMangerByMobile == null) {
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "用户不存在");
							resultJson.put("data", new JSONObject());
							ResponseUtils.renderJson(request, response, resultJson.toString());
							return;
						}
						ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
						IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise);
						String loginToken = this.buildLoginToken();
						retInfo.setLoginToken(loginToken);
						iLiveMangerByMobile.setLoginToken(loginToken);
						iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "登录成功");
						resultJson.put("data", new JSONObject(retInfo));
						retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
						UserBean userBean = new UserBean();
						userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
						userBean.setLoginToken(loginToken);
						userBean.setNickname(iLiveMangerByMobile.getNailName());
						userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
						CacheManager.cacheExpired(cacheInfo);
						request.getSession().setAttribute("appUser", userBean);
					}
				} else {
					ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveManagerByUserName(phoneNum);
					if (iLiveMangerByMobile == null) {
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "用户不存在");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
					if (type == 1) {
						String encrtypePwd = md5(password, iLiveMangerByMobile.getSalt());
						if (encrtypePwd.equals(iLiveMangerByMobile.getUserPwd())) {
							ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
							IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise);
							String loginToken = this.buildLoginToken();
							retInfo.setLoginToken(loginToken);
							iLiveMangerByMobile.setLoginToken(loginToken);
							iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "登录成功");
							resultJson.put("data", new JSONObject(retInfo));
							retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
							UserBean userBean = new UserBean();
							userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
							userBean.setLoginToken(loginToken);
							userBean.setNickname(iLiveMangerByMobile.getNailName());
							userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
							request.getSession().setAttribute("appUser", userBean);
							CacheManager.cacheExpired(cacheInfo);
						} else {
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "登录失败,密码不正确");
							resultJson.put("data", new JSONObject());
						}
					}
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "登录失败,请确认账户或密码是否正确");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	public static void main(String[] args) {
		String v = "383f22ea367fac93f13c0b6ef2d1c020";
		String o = "123456";
		String s = "1687";
		String md5 = md5(0, s);
		System.out.println(md5);
	}

	private static String md5(int pass, String saltSource) {
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;
		Object result = new SimpleHash("MD5", pass, salt, hashIterations);
		String password = result.toString();
		return password;
	}

	/**
	 * 注册接口 登录接口 1是账号密码登录 2是验证码登录
	 * 
	 * @param username
	 * @param password
	 * @param vpassword
	 * @param jpushId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/reg.jspx")
	public void reg(String phoneNum, String password, String vpassword, Integer type, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (phoneNum == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "手机号必须输入");
			resultJson.put("data", new JSONObject());
		} else if (type == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "类型不存在");
			resultJson.put("data", new JSONObject());
		} else if (type == 1 && password == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "未填写密码");
			resultJson.put("data", new JSONObject());
		} else if (type == 2 && vpassword == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "未填写验证码");
			resultJson.put("data", new JSONObject());
		} else {
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "reg_" + phoneNum);
			if (cacheInfo == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码不通过");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else if (cacheInfo.isExpired()) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码已过期,请重新发送");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else if (!vpassword.equals(cacheInfo.getValue())) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "验证码校验不通过");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
			ILiveManager iLiveManger = iLiveManagerMng.getILiveMangerByMobile(phoneNum);
			if (iLiveManger != null) {
				// resultJson.put("code", ERROR_STATUS);
				// resultJson.put("message", "手机号已注册");
				// resultJson.put("data", new JSONObject());
				ILiveEnterprise enterPrise = iLiveManger.getEnterPrise();
				IliveAppRetInfo retInfo = this.buildAppRet(iLiveManger, enterPrise);
				String loginToken = this.buildLoginToken();
				retInfo.setLoginToken(loginToken);
				iLiveManger.setLoginToken(loginToken);
				iLiveManagerMng.updateLiveManager(iLiveManger);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "登录成功");
				resultJson.put("data", new JSONObject(retInfo));
				retInfo.setJpushId(iLiveManger.getJpushId());
				UserBean userBean = new UserBean();
				userBean.setUserId(String.valueOf(iLiveManger.getUserId()));
				userBean.setLoginToken(loginToken);
				userBean.setNickname(iLiveManger.getNailName());
				userBean.setUserThumbImg(iLiveManger.getUserImg());
				CacheManager.cacheExpired(cacheInfo);
				request.getSession().setAttribute("appUser", userBean);
			} else {
				// 0标识手机号注册
				try {
					//
					long registeredManagerId = iLiveManagerMng.registeredManager(phoneNum, password, 0, type);
					ILiveManager iLiveMangerByMobile = iLiveManagerMng.getILiveManager(registeredManagerId);
					ILiveEnterprise enterPrise = iLiveMangerByMobile.getEnterPrise();
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveMangerByMobile, enterPrise);
					String loginToken = this.buildLoginToken();
					retInfo.setLoginToken(loginToken);
					iLiveMangerByMobile.setLoginToken(loginToken);
					iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "登录成功");
					resultJson.put("data", new JSONObject(retInfo));
					retInfo.setJpushId(iLiveMangerByMobile.getJpushId());
					UserBean userBean = new UserBean();
					userBean.setUserId(String.valueOf(iLiveMangerByMobile.getUserId()));
					userBean.setLoginToken(loginToken);
					userBean.setNickname(iLiveMangerByMobile.getNailName());
					userBean.setUserThumbImg(iLiveMangerByMobile.getUserImg());
					CacheManager.cacheExpired(cacheInfo);
					request.getSession().setAttribute("appUser", userBean);
				} catch (Exception e) {
					e.printStackTrace();
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "注册时发生异常");
					resultJson.put("data", new JSONObject());
					CacheManager.cacheExpired(cacheInfo);
				}
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	/**
	 * 获取报名
	 */
	@RequestMapping(value = "/diyform")
	public void getDiyForm(Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		ILiveLiveRoom iliveRoom = liveRoomMng.getIliveRoom(roomId);
		BBSDiyform diyform = bbsDiyformMng.findById(iliveRoom.getLiveEvent().getFormId());
		int diyformId = diyform.getDiyformId();
		List<BBSDiymodel> listByDiyformId = bbsDiymodelMng.getListByDiyformId(diyformId);
		AppDiyFormVo formVo = this.convert2AppDiyFormVo(iliveRoom, diyform, listByDiyformId);
		Gson gson = new Gson();
		String json = gson.toJson(formVo);
		ResponseUtils.renderJson(request, response, json);
	}

	private AppDiyFormVo convert2AppDiyFormVo(ILiveLiveRoom iliveRoom, BBSDiyform diyform,
			List<BBSDiymodel> listByDiyformId) {
		AppDiyFormVo formVo = new AppDiyFormVo();
		DiyFormVo diyformVo = new DiyFormVo();
		diyformVo.setFormDesc(iliveRoom.getLiveEvent().getLiveTitle());
		diyformVo.setFormName(iliveRoom.getLiveEvent().getLiveTitle());
		diyformVo.setFormId(diyform.getDiyformId());
		formVo.setDiyformVo(diyformVo);
		List<DiyFormModelVo> modelVoList = new ArrayList<>();
		DiyFormModelVo modelVo = null;
		for (BBSDiymodel diyformModel : listByDiyformId) {
			modelVo = new DiyFormModelVo();
			modelVo.setIsNeed(diyformModel.getNeedAnswer());
			modelVo.setQuestionName(diyformModel.getDiymodelTitle());
			modelVo.setQuestionType((int) diyformModel.getDiymodelType());
			String defValue = diyformModel.getOptValue();
			defValue = defValue == null ? "" : defValue;
			List<String> defValueList = new ArrayList<>();
			if (defValue.indexOf("@;@") > -1) {
				String[] splitOpts = defValue.split("@;@");
				for (String opt : splitOpts) {
					defValueList.add(opt);
				}
			} else {
				defValueList.add(defValue);
			}
			modelVo.setOptionName(defValueList);
			modelVo.setModelId(diyformModel.getDiymodelId());
			modelVo.setQuestionOrder(diyformModel.getDiyOrder());
			modelVoList.add(modelVo);
		}
		formVo.setModelList(modelVoList);
		return formVo;
	}

	@RequestMapping(value = "postformdata")
	public void postDiyformData(String diyFormData, HttpServletRequest request, HttpServletResponse response) {
		String formId = request.getParameter("formId");
		System.out.println("diyFormData:" + diyFormData);
		JSONObject json = new JSONObject();
		UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
		String userId = "100";
		if (userBean != null) {
			userId = userBean.getUserId();
		}
		try {
			Gson gson = new Gson();
			List<AppDiyformReceiver> receiversList = gson.fromJson(diyFormData,
					new TypeToken<List<AppDiyformReceiver>>() {
					}.getType());
			if (receiversList != null) {
				List<BBSDiyformData> diyformDatas = new ArrayList<>();
				BBSDiyformData formData = null;
				BBSDiyform diyform = bbsDiyformMng.findById(Integer.parseInt(formId));
				for (AppDiyformReceiver receiver : receiversList) {
					formData = new BBSDiyformData();
					formData.setCreateTime(new Timestamp(System.currentTimeMillis()));
					formData.setDataOrder(receiver.getDataOrder());
					formData.setDataTitle(receiver.getDataTitle());
					formData.setDataValue(receiver.getDataValue());
					formData.setManagerId(Long.parseLong(userId));
					formData.setModelId(receiver.getDataModelId());
					formData.setBbsDiyform(diyform);
					diyformDatas.add(formData);
				}
				dataMng.saveBBSDiyfromData(diyformDatas);
				json.put("code", "1");
				json.put("message", "提交成功");
				json.put("data", new JSONObject());
			}
		} catch (Exception e) {
			json.put("code", "0");
			json.put("message", "提交失败");
			json.put("data", new JSONObject());
			e.printStackTrace();
		}

		ResponseUtils.renderJson(request, response, json.toString());

	}

	/**
	 * 直播间密码校验
	 * 
	 * @param roomId
	 * @param roomPassword
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "checkRoomPassword.jspx")
	public void checkRoomPassword(Integer roomId, String roomPassword, HttpServletRequest request,
			HttpServletResponse response) {
		ILiveLiveRoom room = liveRoomMng.getIliveRoom(roomId);
		JSONObject resultJson = new JSONObject();
		if (room == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "直播间不存在");
			resultJson.put("data", "");
		} else if (roomPassword == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "没有输入房间密码");
			resultJson.put("data", "");
		} else {
			String viewPassword = room.getLiveEvent().getViewPassword();
			if (roomPassword.equals(viewPassword)) {
				UserBean userBean = (UserBean) request.getSession().getAttribute("appUser");
				if (userBean != null) {
					userBean.setPasswdCheckResult(true);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "密码输入正确");
					resultJson.put("data", "");
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "密码不正确");
				resultJson.put("data", "");
			}

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

	/**
	 * 构建登录token
	 * 
	 * @return
	 */
	private String buildLoginToken() {
		UUID uuid = UUID.randomUUID();
		String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
		return token;
	}

	/**
	 * 加密
	 * 
	 * @param salt
	 * @param password
	 * @return
	 */
	public static String md5(String pass, String saltSource) {
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;
		Object result = new SimpleHash("MD5", pass, salt, hashIterations);
		String password = result.toString();
		return password;
	}

	/**
	 * 验证码验证
	 */
	@RequestMapping(value = "/msg.jspx")
	public String validSendMsg(String phoneNum, String code, String msg, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> sendParam = new HashMap<>();
		String biztype_mode_valid = ConfigUtils.get("biztype_mode_valid");
		sendParam.put("biztype", Integer.parseInt(biztype_mode_valid));
		sendParam.put("params", "[{\"Name\":\"{vcode}\",\"Val\":\"" + code + "\"}]");
		if (msg != null) {
			sendParam.put("msg", msg);
		}
		try {
			Map<String, Object> sendMsg = ILiveSmgServerCenter.sendMsg(phoneNum, sendParam);
			JSONObject json = new JSONObject(sendMsg);
			ResponseUtils.renderJson(request, response, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/sendmsg.jspx")
	public void validSendMsg(String phoneNum, String type, HttpServletRequest request, HttpServletResponse response) {
		Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum);
		Map<String, Object> map = new HashMap<>();
		if (cacheInfo == null || cacheInfo.isExpired()) {
			Map<String, Object> sendParam = new HashMap<>();
			// String code = this.generatorCode();
			String code = "1111";
			CacheManager.putCacheInfo(CacheManager.mobile_token_ + type + "_" + phoneNum, code, 2 * 60 * 1000);
			sendParam.put("biztype", 38);
			sendParam.put("params", "[{\"Name\":\"{vcode}\",\"Val\":\"" + code + "\"}]");
			try {
				// Map<String, Object> sendMsg =
				// ILiveSmgServerCenter.sendMsg(phoneNum, sendParam);
				// JSONObject json = new JSONObject(sendMsg);
				// if (json.getInt("code") == 0) {
				if (true) {
					map.put("code", 1);
					map.put("message", "验证码已发送,请注意查收!");
					map.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
				}
			} catch (Exception e) {
				map.put("code", 0);
				map.put("message", "发送异常，请稍后再试!");
				map.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
				e.printStackTrace();
			}
		} else {
			map.put("code", 0);
			map.put("message", "发送时间过于频繁，请稍后再试!");
			map.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
		}

	}

	/**
	 * 企业关注接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "enterprise/concern.jspx")
	public void concernEnterprise(Integer enterpriseId, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		try {
			boolean ret = enterpriseFansMng.addEnterpriseConcern(enterpriseId, userId);
			if (ret) {
				map.put("code", SUCCESS_STATUS);
				map.put("message", "关注企业成功");
				map.put("data", new JSONObject());
			} else {
				map.put("code", ERROR_STATUS);
				map.put("message", "关注企业失败");
				map.put("data", new JSONObject());
			}
		} catch (Exception e) {
			map.put("code", ERROR_STATUS);
			map.put("message", "关注企业失败");
			map.put("data", new JSONObject());
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request, response, new JSONObject(map).toString());
	}

	private String generatorCode() {
		String str[] = new String[] { "5", "1", "2", "4", "7", "3", "6", "0", "9", "8" };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			sb.append(str[random.nextInt(9)]);
		}
		return sb.toString();
	}

}
