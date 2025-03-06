package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UN_LOGIN;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.constants.ILiveCertStatus;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseSetup;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveManagerState;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseSetupMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerStateMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "/enterprise")
public class ILiveEnterpriseAct {

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng iLiveEenteriseMng;

	@Autowired
	private ILiveManagerStateMng iLiveManagerStateMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	@Autowired
	private ILiveEnterpriseSetupMng enterpriseSetupMng;
	//@RequestMapping(value = "/saveEnterprise.jspx", method = RequestMethod.POST)
	@RequestMapping(value = "/saveEnterprise.jspx")
	public void saveEnterprise(HttpServletRequest request, HttpServletResponse response, String userId, Integer stamp,
			String enterpriseName, String contactName, String idCard, String idCardImg, String contactPhone,
			String enterpriseRegNo, String certResource, String iliveName,ILiveEnterprise acceptEnterprise) {
		JSONObject resultJson = new JSONObject();
		System.out.println(acceptEnterprise.toString());
		UserBean user = ILiveUtils.getAppUser(request);
		if (user == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取不到用户");
			resultJson.put("data", new JSONObject());
		} else {
			try {
				userId = user.getUserId();
				ILiveManager manager = iLiveManagerMng.selectILiveManagerById(Long.parseLong(userId));
				Integer certStatus = manager.getCertStatus();
				// 既不是认证过的也不是认证中的可以提交认证
				if (certStatus != 4 && certStatus != 3) {
					ILiveEnterprise iliveEnterprise = new ILiveEnterprise();
					System.out.println("stamp" + stamp);
					if (stamp == 0) {
						// 个人
						user.setUserType(0);
						iliveEnterprise.setStamp(0);
						iliveEnterprise.setIliveName(iliveName);
						iliveEnterprise.setEnterpriseName(iliveName);
						iliveEnterprise.setContactName(contactName);
						iliveEnterprise.setIdCard(idCard);
						iliveEnterprise.setIdCardImg(idCardImg);
						iliveEnterprise.setContactPhone(contactPhone);
						iliveEnterprise.setUserPhone(manager.getMobile());
					} else if (stamp == 1) {
						// 企业
						user.setUserType(1);
						iliveEnterprise.setStamp(1);
						iliveEnterprise.setIliveName(iliveName);
						iliveEnterprise.setEnterpriseName(iliveName);
						iliveEnterprise.setEnterpriseRegName(enterpriseName);
						iliveEnterprise.setContactName(contactName);
						iliveEnterprise.setEnterpriseRegNo(enterpriseRegNo);
						iliveEnterprise.setContactPhone(contactPhone);
						iliveEnterprise.setCertResource(acceptEnterprise.getCertResource());
						iliveEnterprise.setEnterpriseLicence(certResource);
						iliveEnterprise.setUserPhone(manager.getMobile());
					}

					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
					Integer saveEnterpriseForWeb = iLiveEenteriseMng.saveEnterpriseForWeb(iliveEnterprise,
							iLiveManager);
					if (saveEnterpriseForWeb > -1) {
						user.setCertStatus(3);
						user.setEnterpriseId(saveEnterpriseForWeb);
						ILiveManagerState iliveManagerState = iLiveManagerStateMng
								.getIliveManagerState(iLiveManager.getUserId());
						if (iliveManagerState == null) {
							iliveManagerState = new ILiveManagerState();
							iliveManagerState.setCertStatus(ILiveCertStatus.CERT_ING);
							iliveManagerState.setManagerId(iLiveManager.getUserId());
							iLiveManagerStateMng.saveIliveManagerState(iliveManagerState);
						} else {
							iliveManagerState.setCertStatus(ILiveCertStatus.CERT_ING);
							iLiveManagerStateMng.updateIliveManagerState(iliveManagerState);
						}
					}
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "保存成功");
					iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveManager, iLiveManager.getEnterPrise());
					resultJson.put("data", new JSONObject(retInfo));
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "不是可认证状态");
					resultJson.put("data", new JSONObject());
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "保存失败");
				resultJson.put("data", new JSONObject());
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	@RequestMapping(value = "/saveEnterprise2.jspx")
	public void saveEnterprise2(HttpServletRequest request, HttpServletResponse response, String userId, Integer stamp,
			String enterpriseName, String contactName, String idCard, String idCardImg, String contactPhone,
			String enterpriseRegNo, String certResource, String iliveName,ILiveEnterprise acceptEnterprise) {
		JSONObject resultJson = new JSONObject();
		System.out.println(acceptEnterprise.toString());
		//UserBean user = ILiveUtils.getAppUser(request);
		if (true){
			try {
				ILiveManager manager = iLiveManagerMng.selectILiveManagerById(Long.parseLong(userId));
				Integer certStatus = manager.getCertStatus();
				// 既不是认证过的也不是认证中的可以提交认证
				if (certStatus != 4 && certStatus != 3) {
					ILiveEnterprise iliveEnterprise = new ILiveEnterprise();
					System.out.println("stamp" + stamp);
					if (stamp == 0) {
						// 个人
						//user.setUserType(0);
						iliveEnterprise.setStamp(0);
						iliveEnterprise.setIliveName(iliveName);
						iliveEnterprise.setEnterpriseName(iliveName);
						iliveEnterprise.setContactName(contactName);
						iliveEnterprise.setIdCard(idCard);
						iliveEnterprise.setIdCardImg(idCardImg);
						iliveEnterprise.setContactPhone(contactPhone);
						iliveEnterprise.setUserPhone(manager.getMobile());
					} else if (stamp == 1) {
						// 企业
						//user.setUserType(1);
						iliveEnterprise.setStamp(1);
						iliveEnterprise.setIliveName(iliveName);
						iliveEnterprise.setEnterpriseName(iliveName);
						iliveEnterprise.setEnterpriseRegName(enterpriseName);
						iliveEnterprise.setContactName(contactName);
						iliveEnterprise.setEnterpriseRegNo(enterpriseRegNo);
						iliveEnterprise.setContactPhone(contactPhone);
						iliveEnterprise.setCertResource(acceptEnterprise.getCertResource());
						iliveEnterprise.setEnterpriseLicence(certResource);
						iliveEnterprise.setUserPhone(manager.getMobile());
					}
					
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
					Integer saveEnterpriseForWeb = iLiveEenteriseMng.saveEnterpriseForWeb(iliveEnterprise,
							iLiveManager);
					if (saveEnterpriseForWeb > -1) {
						//user.setCertStatus(3);
						//user.setEnterpriseId(saveEnterpriseForWeb);
						ILiveManagerState iliveManagerState = iLiveManagerStateMng
								.getIliveManagerState(iLiveManager.getUserId());
						if (iliveManagerState == null) {
							iliveManagerState = new ILiveManagerState();
							iliveManagerState.setCertStatus(ILiveCertStatus.CERT_ING);
							iliveManagerState.setManagerId(iLiveManager.getUserId());
							iLiveManagerStateMng.saveIliveManagerState(iliveManagerState);
						} else {
							iliveManagerState.setCertStatus(ILiveCertStatus.CERT_ING);
							iLiveManagerStateMng.updateIliveManagerState(iliveManagerState);
						}
					}
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "保存成功");
					iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
					IliveAppRetInfo retInfo = this.buildAppRet(iLiveManager, iLiveManager.getEnterPrise());
					resultJson.put("data", new JSONObject(retInfo));
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "不是可认证状态");
					resultJson.put("data", new JSONObject());
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "保存失败");
				resultJson.put("data", new JSONObject());
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	@RequestMapping(value = "/updateProductInfo.jspx", method = RequestMethod.POST)
	public void updateProductInfo(Integer enterpriseId,HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		Integer certStatus = null;
		if (iLiveEnterPrise != null) {
			certStatus = iLiveEnterPrise.getCertStatus();
		}
		try {
			EnterpriseUtil.selectEnterpriseCache(enterpriseId, certStatus);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "更新失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	@RequestMapping(value = "/updateEnterpriseName.jspx")
	public void updateEnterpriseName(String enterpriseName,HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getAppUser(request);
		System.out.println("开始更新企业名称！+++++++++");
		if (user == null) {
			System.out.println("用户未登录");
			resultJson.put("code", UN_LOGIN);
			resultJson.put("message", "用户未登录");
			resultJson.put("data", new JSONObject());
		}else{
			Integer userId=Integer.parseInt(user.getUserId());
			Integer level=user.getLevel();
			Integer cerStus=user.getCertStatus();
			if(cerStus!=4) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "未认证用户不能修改企业信息！");
			}else if(level==3) {
				//查询子账户是否具有图片查看全部
				boolean per=iLiveSubLevelMng.selectAppIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_enterpriseInformationManagement);
			   if(!per) {
				   resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "该子账号无权修改企业信息！"); 
			   }
			}else {
				ILiveEnterprise iLiveEnterPrise = iLiveManagerMng.getILiveManager(Long.parseLong(userId.toString())).getEnterPrise();
				try {
					iLiveEnterPrise.setEnterpriseName(enterpriseName);
					iLiveEnterpriseMng.update(iLiveEnterPrise);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "更新成功");
				} catch (Exception e) {
					e.printStackTrace();
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "更新失败");
				}
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
	 * 推荐企业
	 */
	@RequestMapping(value = "/remEnterprise.jspx", method = RequestMethod.GET)
	public void remEnterprise(HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			String data= HttpUtils.sendStr(ConfigUtils.get("imanager_url")+"/managercontrol/dataList.do", "POST", null);
			resultJson.put("list", data);
			resultJson.put("code", 1);
			resultJson.put("msg", "数据获取成功");
		} catch (Exception e) {
			resultJson.put("code", 0);
			resultJson.put("msg", "数据获取失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * h5企业设置信息
	 */
	@RequestMapping(value = "/enterpriseSetup.jspx")
	public void enterpriseSetup(Integer enterpriseId,HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveEnterpriseSetup enterpriseSetup= enterpriseSetupMng.getByEnterpriseId(enterpriseId);
			resultJson.put("data", new JSONObject(enterpriseSetup));
			resultJson.put("code", 1);
			resultJson.put("msg", "数据获取成功");
		} catch (Exception e) {
			resultJson.put("code", 0);
			resultJson.put("msg", "数据获取失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
}
