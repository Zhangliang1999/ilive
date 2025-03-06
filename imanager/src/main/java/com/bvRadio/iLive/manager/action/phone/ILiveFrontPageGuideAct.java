package com.bvRadio.iLive.manager.action.phone;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.manager.entity.IliveOperationUser;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.manager.manager.ILiveManagerOperatorUserMng;
import com.bvRadio.iLive.manager.vo.AppUserInfo;

@Controller
@RequestMapping(value = "/token")
public class ILiveFrontPageGuideAct {

	@Autowired
	private ILiveManagerOperatorUserMng iLiveManagerOperatorUserMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng; // 获取企业信息
	

	/**
	 * 获取平台总商户数 、正式签约商户数
	 */
	@RequestMapping(value = "/enterpriseSum.jspx")
	public void searchEnterpriseSum(String token, HttpServletRequest requset, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			int contractUserNum = iLiveEnterpriseMng.getContractUserNum();
			int userNum = iLiveEnterpriseMng.getUserNum();
			// 执行认证操作.
			resultJson.put("contractUserNum",contractUserNum);
			resultJson.put("userNum", userNum);
			resultJson.put("status", 1);
			resultJson.put("msg", "成功");
		} catch (Exception e) {
			resultJson.put("status",0);
			resultJson.put("msg", "失败");
		}
		ResponseUtils.renderJson(requset, response, resultJson.toString());
	}

	@RequestMapping(value = "/userinfo.jspx")
	public void getUserInfoByToken(String token, HttpServletRequest requset, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.third_token_ + token);
		if (cacheInfo == null || cacheInfo.isExpired()) {
			resultJson.put("code", 0);
			resultJson.put("message", "token不合法");
			resultJson.put("data", new JSONObject());
		} else {
			String userId = (String) cacheInfo.getValue();
			IliveOperationUser operatorUser = iLiveManagerOperatorUserMng.getUserById(Long.parseLong(userId));
			AppUserInfo userInfo = new AppUserInfo();
			userInfo.setNickName(operatorUser.getNickName() == null ? "" : operatorUser.getNickName());
			userInfo.setUserId(operatorUser.getUserId());
			userInfo.setUserImg(operatorUser.getUserImg() == null ? "" : operatorUser.getUserImg());
			userInfo.setUserName(operatorUser.getUserName());
			JSONObject json = new JSONObject(userInfo);
			resultJson.put("code", 1);
			resultJson.put("message", "token校验通过");
			resultJson.put("data", json);
			cacheInfo.setExpired(true);
		}
		ResponseUtils.renderJson(requset, response, resultJson.toString());

	}

	@RequestMapping(value = "/userlist.jspx")
	public void getUserList(String token, HttpServletRequest requset, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		List<IliveOperationUser> list = iLiveManagerOperatorUserMng.listByParams();
		JSONArray jsonArray = new JSONArray();
		if (null != list) {
			for (IliveOperationUser operationUser : list) {
				if(null!=operationUser) {
					JSONObject jsonObject = new JSONObject();
					String nickName = operationUser.getNickName();
					Long userId = operationUser.getUserId();
					String userImg = operationUser.getUserImg();
					String userName = operationUser.getUserName();
					jsonObject.put("nickName", nickName);
					jsonObject.put("userId", userId);
					jsonObject.put("userImg", userImg);
					jsonObject.put("userName", userName);
					jsonArray.put(jsonObject);
				}
			}
		}
		resultJson.put("code", 1);
		resultJson.put("message", "成功");
		resultJson.put("data", jsonArray);
		ResponseUtils.renderJson(requset, response, resultJson.toString());

	}

}
