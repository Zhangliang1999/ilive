package com.bvRadio.iLive.iLive.action.admin;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppCertEnterprise;
import com.bvRadio.iLive.iLive.constants.ILiveCertStatus;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveManagerState;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerStateMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "enterprise")
public class ILiveEnterpriseController {

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng ILiveEenteriseMng;

	@Autowired
	private ILiveManagerStateMng iLiveManagerStateMng;

	/**
	 * 新增企业
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save.do")
	public String addEnterprise(ILiveEnterprise enterprise) {
		boolean saveILiveEnterprise = iLiveEnterpriseMng.saveILiveEnterprise(enterprise);
		if (saveILiveEnterprise) {
			return "redirect:/admin/room/list.do";
		}
		return null;
	}

	@RequestMapping(value = "/push/certinfo.do")
	public void certEnterprise(AppCertEnterprise certEnterprise, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			UserBean user = ILiveUtils.getUser(request);
			Integer certStatus = user.getCertStatus();
			// 既不是认证过的也不是认证中的可以提交认证
			if (certStatus != 4 && certStatus != 3) {
				ILiveEnterprise iliveEnterprise = new ILiveEnterprise();
				iliveEnterprise.setEnterpriseName(certEnterprise.getEnterpriseName());
				iliveEnterprise.setEnterpriseType(certEnterprise.getEnterpriseType());
				iliveEnterprise.setEnterpriseInfo(certEnterprise.getEnterpriseInfo());
				iliveEnterprise.setRoad(certEnterprise.getRoad());
				iliveEnterprise.setEnterpriseRegName(certEnterprise.getEnterpriseRegName());
				iliveEnterprise.setEnterpriseRegNo(certEnterprise.getEnterpriseRegNo());
				iliveEnterprise.setEnterpriseLicence(certEnterprise.getEnterpriseLicence());
				iliveEnterprise.setContactPhone(certEnterprise.getContactPhone());
				iliveEnterprise.setContactName(certEnterprise.getContactName());
				iliveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
				iliveEnterprise.setIdCard(certEnterprise.getIdCard());
				iliveEnterprise.setIdCardImg(certEnterprise.getIdCardImg());
				iliveEnterprise.setEnterprisePhone(certEnterprise.getEnterprisePhone());
				iliveEnterprise.setEnterpriseEmail(certEnterprise.getEnterpriseEmail());
				iliveEnterprise.setEnterpriseImg(certEnterprise.getEnterpriseImg());
				iliveEnterprise.setStamp(certEnterprise.getStamp());
				iliveEnterprise.setCertResource(certEnterprise.getEnterpriseLicence());
				iliveEnterprise.setUserId(Long.parseLong(user.getUserId()));
				try {
					String userId = user.getUserId();
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
					iliveEnterprise.setUserPhone(iLiveManager.getMobile());
					Integer saveEnterpriseForWeb = ILiveEenteriseMng.saveEnterpriseForWeb(iliveEnterprise, iLiveManager);
					if (saveEnterpriseForWeb > -1) {
						user.setCertStatus(ILiveCertStatus.CERT_ING);
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
						user.setLevel(user.getLevel()==null?ILiveManager.USER_LEVEL_ADMIN:user.getLevel());
						ILiveUtils.setUser(request, user);
					}
					JSONObject resultJson = new JSONObject();
					resultJson.put("status", "1");
					ResponseUtils.renderJson(request, response, resultJson.toString());
				} catch (Exception e) {
					JSONObject resultJson = new JSONObject();
					resultJson.put("status", "0");
					ResponseUtils.renderJson(request, response, resultJson.toString());
					e.printStackTrace();
				}
			}else {
				JSONObject resultJson = new JSONObject();
				resultJson.put("status", "0");
				ResponseUtils.renderJson(request, response, resultJson.toString());
			}
		}catch (Exception e) {
			e.printStackTrace();
			JSONObject resultJson = new JSONObject();
			resultJson.put("status", "0");
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	}
	
	@RequestMapping(value = "/push/certinfot.do")
	public void certEnterpriset(Long userId,AppCertEnterprise certEnterprise, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ILiveManager user = iLiveManagerMng.getILiveManager(userId);
			
			Integer certStatus = user.getCertStatus();
			// 既不是认证过的也不是认证中的可以提交认证
			if (certStatus != 4 && certStatus != 3) {
				ILiveEnterprise iliveEnterprise = new ILiveEnterprise();
				iliveEnterprise.setEnterpriseName(certEnterprise.getEnterpriseName());
				iliveEnterprise.setEnterpriseType(certEnterprise.getEnterpriseType());
				iliveEnterprise.setEnterpriseInfo(certEnterprise.getEnterpriseInfo());
				iliveEnterprise.setRoad(certEnterprise.getRoad());
				iliveEnterprise.setEnterpriseRegName(certEnterprise.getEnterpriseRegName());
				iliveEnterprise.setEnterpriseRegNo(certEnterprise.getEnterpriseRegNo());
				iliveEnterprise.setEnterpriseLicence(certEnterprise.getEnterpriseLicence());
				iliveEnterprise.setContactPhone(certEnterprise.getContactPhone());
				iliveEnterprise.setContactName(certEnterprise.getContactName());
				iliveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
				iliveEnterprise.setIdCard(certEnterprise.getIdCard());
				iliveEnterprise.setIdCardImg(certEnterprise.getIdCardImg());
				iliveEnterprise.setEnterprisePhone(certEnterprise.getEnterprisePhone());
				iliveEnterprise.setEnterpriseEmail(certEnterprise.getEnterpriseEmail());
				iliveEnterprise.setEnterpriseImg(certEnterprise.getEnterpriseImg());
				iliveEnterprise.setStamp(certEnterprise.getStamp());
				iliveEnterprise.setCertResource(certEnterprise.getEnterpriseLicence());
				iliveEnterprise.setUserId(user.getUserId());
				try {
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
					iliveEnterprise.setUserPhone(iLiveManager.getMobile());
					Integer saveEnterpriseForWeb = ILiveEenteriseMng.saveEnterpriseForWeb(iliveEnterprise, iLiveManager);
					if (saveEnterpriseForWeb > -1) {
						user.setCertStatus(ILiveCertStatus.CERT_ING);
						user.setEnterpriseId(saveEnterpriseForWeb.longValue());
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
						user.setLevel(user.getLevel()==null?ILiveManager.USER_LEVEL_ADMIN:user.getLevel());
					}
					JSONObject resultJson = new JSONObject();
					resultJson.put("status", "1");
					ResponseUtils.renderJson(request, response, resultJson.toString());
				} catch (Exception e) {
					JSONObject resultJson = new JSONObject();
					resultJson.put("status", "0");
					ResponseUtils.renderJson(request, response, resultJson.toString());
					e.printStackTrace();
				}
			}else {
				JSONObject resultJson = new JSONObject();
				resultJson.put("status", "0");
				resultJson.put("msg", "该账号已经通过认证或者正在认证中，请勿重复操作");
				ResponseUtils.renderJson(request, response, resultJson.toString());
			}
		}catch (Exception e) {
			e.printStackTrace();
			JSONObject resultJson = new JSONObject();
			resultJson.put("status", "0");
			resultJson.put("msg", "操作出现问题，请稍后再试");
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	}

}
