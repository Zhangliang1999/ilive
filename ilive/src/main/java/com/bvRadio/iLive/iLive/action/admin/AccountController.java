package com.bvRadio.iLive.iLive.action.admin;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseSetup;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.AccountMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseSetupMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.util.SystemMd5Util;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "account")
public class AccountController {

	@Autowired
	private AccountMng accountMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Autowired
	private ILiveEnterpriseSetupMng enterpriseSetupMng;
	/**
	 * 概览
	 * 
	 * @return
	 */
	@RequestMapping(value = "overview.do")
	public String overview(Model model, HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		String userId = user.getUserId();
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
		if (iLiveManager != null) {
			model.addAttribute("iLiveManager", iLiveManager);
		}
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "1");
		return "account/overview";
	}

	/**
	 * 企业信息管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "accountmanager.do")
	public String account(Model model, HttpServletRequest request) {
		ILiveEnterprise enterprise = accountMng.getEnterpriseById(ILiveUtils.getUser(request).getEnterpriseId());
		if(enterprise.getPrivacy()==null){
			enterprise.setPrivacy(0);
		}
		Integer serverGroupId = this.selectServerGroup();
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		Integer certStatus = iLiveEnterprise.getCertStatus();
		//检查是否开通子账号管理
		boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_SubAccountManagement,certStatus);
		if(b){
			model.addAttribute("SubAccountManagement", 0);
		}else{
			model.addAttribute("SubAccountManagement", 1);
		}
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("enterprise", enterprise);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "8_1");
		return "account/enterprose_infos";
	}
	
	/**
	 * 企业关注设置
	 */
	@RequestMapping(value="enterpriseSetup.do")
	public String enterpriseSetup(Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		ILiveEnterpriseSetup enterpriseSetup= enterpriseSetupMng.getByEnterpriseId(enterpriseId);
		
		Integer serverGroupId = this.selectServerGroup();
		
		model.addAttribute("serverGroupId", serverGroupId);
		if(enterpriseSetup==null) {
			model.addAttribute("enterpriseSetup", new ILiveEnterpriseSetup());
		}else {
			model.addAttribute("enterpriseSetup", enterpriseSetup);
		}
		model.addAttribute("enterpriseId", enterpriseId);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "8_5");
		return "account/enterprise_setup";
	}

	private Integer selectServerGroup() {
		return 100;
	}

	/**
	 * 账号管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "accountmng.do")
	public String accountmng(Model model, HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		String userId = user.getUserId();
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
		if (iLiveManager != null) {
			String userName = iLiveManager.getUserName();
			if (userName==null || userName.equals("")) {
				iLiveManager.setUserName(iLiveManager.getMobile());
				iLiveManager.setEditUserNameIdentify(0);
			}
			model.addAttribute("iLiveManager", iLiveManager);
			if(iLiveManager.getUpdatePasswordTime()==null) {
				model.addAttribute("showedit", "");
				iLiveManager.setUpdatePasswordTime(iLiveManager.getCreateTime());
				iLiveManagerMng.updateLiveManager(iLiveManager);
			}else {
				Long updatePasswordTime = iLiveManager.getUpdatePasswordTime().getTime()+365*24*60*60*1000;
				Long now = new Date().getTime();
				if(now>updatePasswordTime) {
					model.addAttribute("showedit", "密码已过期，请立即修改");
				}else {
					model.addAttribute("showedit", "");
				}
			}
		}
		
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "5");
		return "account/account_info";
	}

	@RequestMapping(value = "/update/userinfo.do")
	public void updateIliveSimpleInfo(String nailName, String userImg, Long managerId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(managerId);
			iLiveManager.setNailName(nailName);
			iLiveManager.setUserImg(userImg);
			iLiveManagerMng.updateLiveManager(iLiveManager);
			UserBean user = ILiveUtils.getUser(request);
			user.setNickname(nailName);
			user.setUsername(nailName);
			user.setUserThumbImg(userImg);
			resultJson.put("status", "1");
		} catch (Exception e) {
			resultJson.put("status", "0");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 企业信息更新
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveenterprise.do")
	public void saveaccount(ILiveEnterprise iLiveEnterprise) {
		accountMng.saveaccount(iLiveEnterprise);
	}
	/**
	 * 企业信息更新
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateenterprise.do")
	public void updateaccount(Integer allowComment,HttpServletRequest request,
			HttpServletResponse response) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		iLiveEnterprise.setPrivacy(allowComment);
		iLiveEnterpriseMng.update(iLiveEnterprise);
	}
	/**
	 * 企业关注信息更新保存
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveenterpriseSetup.do")
	public void saveaccount(Integer enterpriseId,String slogan,String prompt,Integer isImg,String advertisementImg) {
		ILiveEnterpriseSetup iLiveEnterpriseSetup=enterpriseSetupMng.getByEnterpriseId(enterpriseId);
		if(iLiveEnterpriseSetup==null) {
			ILiveEnterpriseSetup enterpriseSetup =new ILiveEnterpriseSetup();
			if("".endsWith(advertisementImg)||advertisementImg.length()<=0) {
				enterpriseSetup.setAdvertisementImg(null);
			}else {
				enterpriseSetup.setAdvertisementImg(advertisementImg);
			}
			
			enterpriseSetup.setPrompt(prompt);
			enterpriseSetup.setSlogan(slogan);
			enterpriseSetup.setIsImg(isImg);
			enterpriseSetup.setEnterpriseId(enterpriseId);
			enterpriseSetupMng.save(enterpriseSetup);
		}else {
			if("".endsWith(advertisementImg)||advertisementImg.length()<=0) {
				iLiveEnterpriseSetup.setAdvertisementImg(null);
			}else {
				iLiveEnterpriseSetup.setAdvertisementImg(advertisementImg);
			}
			iLiveEnterpriseSetup.setPrompt(prompt);
			iLiveEnterpriseSetup.setSlogan(slogan);
			iLiveEnterpriseSetup.setIsImg(isImg);
			enterpriseSetupMng.update(iLiveEnterpriseSetup);
		}
	}
	
	/**
	 * 修改新账号
	 * @param response
	 * @param id
	 * @param username
	 */
	@ResponseBody
	@RequestMapping(value="update/username")
	public void updateUsername(HttpServletResponse response,Long id,String username) {
		JSONObject result = new JSONObject();
		try {
			ILiveManager iLiveManagerByUserName = iLiveManagerMng.getILiveManagerByUserName(username);
			if(iLiveManagerByUserName==null) {
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(id);
				iLiveManager.setEditUserNameIdentify(1);
				iLiveManager.setUserName(username);
				iLiveManagerMng.updateLiveManager(iLiveManager);
				result.put("status", "1");
			}else {
				result.put("status", "3");
				result.put("message", "该账号已存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "2");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 修改密码
	 * @param response
	 * @param id
	 * @param password
	 */
	@ResponseBody
	@RequestMapping(value="update/password")
	public void updatepassword(HttpServletResponse response,Long id,String password) {
		JSONObject result = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(id);
			iLiveManager.setEditUserNameIdentify(1);
			String md5Pwd = SystemMd5Util.md5(password, iLiveManager.getSalt());
			iLiveManager.setUserPwd(md5Pwd);
			iLiveManager.setUpdatePasswordTime(new Timestamp(new Date().getTime()));
			iLiveManagerMng.updateLiveManager(iLiveManager);
			result.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "2");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 发送短信
	 * @param response
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value="sendmessage")
	public void sendmessage(HttpServletResponse response,Long id) {
		JSONObject result = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(id);
			String mobile = iLiveManager.getMobile();
			result.put("status", "1");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "2");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 验证
	 * @param response
	 * @param id
	 * @param username
	 * @param vPassword
	 */
	@ResponseBody
	@RequestMapping(value="validateold")
	public void validateold(HttpServletResponse response,Long userId,String username,String vPassword,Integer type) {
		JSONObject json = new JSONObject();
		try {
			Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + "reg_" + username);
			String defaultVpassword = ConfigUtils.get("defaultVpassword");
//			System.out.println("缓存中的值为："+cacheInfo.getValue());
//			System.out.println("页面用户填写的验证码为："+vPassword);
			boolean ret = true;
			if (defaultVpassword.equals(vPassword)) {
				ret = true;
			} else if (cacheInfo == null) {
				json.put("status", "0");
				json.put("msg", "请先获取验证码!");
				ret = false;
			} else if (cacheInfo.isExpired()) {
				json.put("status", "0");
				json.put("msg", "输入的验证码已经过期,请重新获取");
				ret = false;
			} else if (!vPassword.equals(cacheInfo.getValue())) {
				json.put("status", "0");
				json.put("msg", "输入的验证码不正确");
				ret = false;
			}
			if (ret) {
				json.put("status", "1");
				json.put("msg", "验证成功");
				if (type!=null&&type==1) {
					ILiveManager Manager = iLiveManagerMng.getILiveMangerByPhoneNumber(username);
					if(Manager==null){
						ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
						iLiveManager.setMobile(username);
						iLiveManagerMng.updateLiveManager(iLiveManager);
						if(iLiveManager.getCertStatus()==4){
							ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPrise(iLiveManager.getEnterpriseId().intValue());
							iLiveEnterprise.setUserPhone(username);
							iLiveEnterpriseMng.update(iLiveEnterprise);
						}
						json.put("msg", "绑定成功");
					}else{
						ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
						
							String mobile=iLiveManager.getMobile();
							if(mobile==null||"".equals(mobile)){
								 if(iLiveManager.getLevel()==3){
									 if(Manager.getCertStatus()==4){
											json.put("status", "0");
											json.put("msg", "不能换绑其他已认证企业绑定的手机号");
											ret = false;
										}else{
											iLiveManager.setMobile(username);
											iLiveManager.setWxUnionId(Manager.getWxUnionId());
											Manager.setIsDel(1);
											iLiveManagerMng.updateLiveManager(iLiveManager);
											iLiveManagerMng.updateLiveManager(Manager);
										}
								 }else{
									 Manager.setWxUnionId(iLiveManager.getWxUnionId());
									 iLiveManager.setIsDel(1);
									 iLiveManagerMng.updateLiveManager(iLiveManager);
									 iLiveManagerMng.updateLiveManager(Manager);
								 }
								}else{
									 if(iLiveManager.getLevel()==3){
										 if(Manager.getCertStatus()==4){
												json.put("status", "0");
												json.put("msg", "不能换绑其他已认证企业绑定的手机号");
												ret = false;
											}else{
												iLiveManager.setMobile(username);
												iLiveManager.setWxUnionId(Manager.getWxUnionId());
												Manager.setIsDel(1);
												iLiveManagerMng.updateLiveManager(iLiveManager);
												iLiveManagerMng.updateLiveManager(Manager);
											}
									 }else{
										 if(Manager.getCertStatus()==4){
											 json.put("status", "0");
											 json.put("msg", "不能换绑其他已认证企业绑定的手机号");
											 ret = false;
										 }else{
											 if(iLiveManager.getUserName().equals(iLiveManager.getMobile())){
												 iLiveManager.setUserName(username);
											 }
											 iLiveManager.setMobile(username);
											 iLiveManager.setWxUnionId(Manager.getWxUnionId());
											 iLiveManagerMng.updateLiveManager(iLiveManager);
											 Manager.setWxUnionId("");
											 Manager.setIsDel(1);
											 iLiveManagerMng.updateLiveManager(Manager);
											 if(iLiveManager.getCertStatus()==4){
												 ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPrise(iLiveManager.getEnterpriseId().intValue());
												 iLiveEnterprise.setUserPhone(username);
												 iLiveEnterpriseMng.update(iLiveEnterprise);
											 }
											 json.put("msg", "绑定成功");
										 }
									 }
						}
					}
					
				}
			}
			
			CacheManager.clearOnly(CacheManager.mobile_token_ + "reg_" + username);
		} catch (Exception e) {
			CacheManager.clearOnly(CacheManager.mobile_token_ + "reg_" + username);
			e.printStackTrace();
			json.put("status", "0");
			json.put("msg", "验证失败");
		}
		ResponseUtils.renderJson(response, json.toString());
	}

}
