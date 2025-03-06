package com.jwzt.billing.action.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.billing.entity.bo.UserBO;
import com.jwzt.billing.task.EnterpriseSyncTask;
import com.jwzt.billing.utils.ConfigUtils;
import com.jwzt.billing.utils.SessionUtils;
import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class WelcomeAct {

	@RequestMapping("/frame/top")
	public String index(String token, HttpServletRequest request, ModelMap mp) {
		UserBO currentUser = SessionUtils.getUser(request);
		if (null != currentUser) {
			String imanagerHomeUrl = ConfigUtils.get("imanager_home_url");
			String statisticHomeUrl = ConfigUtils.get("statistic_home_url");
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("currentUser", currentUser);
			dataMap.put("imanagerHomeUrl", imanagerHomeUrl);
			dataMap.put("statisticHomeUrl", statisticHomeUrl);
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} else {
			RenderJsonUtils.addError(mp, "");
			return "renderJson";
		}
	}

	@RequestMapping("/tool/syncenterprise")
	public String syncenterprise(HttpServletRequest request, ModelMap mp) {
		EnterpriseSyncTask enterpriseSyncTask = new EnterpriseSyncTask();
		enterpriseSyncTask.run();
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}
	public static boolean reCheckUsedPruduct(Integer enterpriseId) {
		boolean ret=true;
		StringBuilder paramsBuilder = new StringBuilder();
		paramsBuilder.append("enterpriseId").append("=").append(enterpriseId).append("&");
		String params = paramsBuilder.toString();
		String recheckUsedPruductUrl = ConfigUtils.get("recheck_usedPruduct_url");
		System.out.println("开始重新计算套餐使用量！！！！！！！！");
		String getEnterpriseListResultJson;
		try {
			getEnterpriseListResultJson = HttpUtils.sendPost(recheckUsedPruductUrl, params, "utf-8");
			Map<?, ?> getEnterpriseListResultMap = JsonUtils.jsonToMap(getEnterpriseListResultJson);
			Integer code = (Integer) getEnterpriseListResultMap.get("code");
			if (null != code && code == 1) {
				ret=false;
			}else {
				ret=true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret=false;
		}
		return ret;
	}
	public static boolean recheckUsedCache(Integer enterpriseId,Integer certStatus) {
		boolean ret=true;
		StringBuilder paramsBuilder = new StringBuilder();
		paramsBuilder.append("enterpriseId").append("=").append(enterpriseId).append("&");
		String params = paramsBuilder.toString();
		String recheckUsedPruductUrl = ConfigUtils.get("recheck_usedCache_url");
		System.out.println("开始重新更新缓存！！！！！！！！");
		String getEnterpriseListResultJson;
		try {
			getEnterpriseListResultJson = HttpUtils.sendPost(recheckUsedPruductUrl, params, "utf-8");
			Map<?, ?> getEnterpriseListResultMap = JsonUtils.jsonToMap(getEnterpriseListResultJson);
			Integer code = (Integer) getEnterpriseListResultMap.get("code");
			if (null != code && code == 1) {
				ret=false;
			}else {
				ret=true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret=false;
		}
		return ret;
	}
}
