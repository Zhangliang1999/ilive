package com.bvRadio.iLive.iLive.action.front.phone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ContentPolicy;
import com.bvRadio.iLive.iLive.entity.ContentSelect;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.manager.ILiveContentSelectMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/homepage")
public class ILiveHomePageAct {

	@Autowired
	private ILiveContentSelectMng contentMng; // 选择显示的内容
	@Autowired
	private ILiveEnterpriseFansMng fansMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng; // 获取企业信息
	

	@RequestMapping(value = "homepage.jspx")
	public void getHomePage(Integer enterpriseId,HttpServletResponse response,HttpServletRequest request) {
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterpriseById(enterpriseId);
		if(iLiveEnterprise == null) {
			return;
		}else {
			Map<String, Object> map = new HashMap<>();
			// 获取标题
			ContentPolicy policy1 = contentMng.getPolicyByShows(1,enterpriseId);
			ContentPolicy policy2 = contentMng.getPolicyByShows(2,enterpriseId);
			ContentPolicy policy4 = contentMng.getPolicyByShows(4,enterpriseId);
			if (policy1 != null) {
				map.put("1", policy1);
			}
			if (policy2 != null) {
				map.put("2", policy2);
			}
			if (policy4 != null) {
				map.put("4", policy4);
			}

			// 获取一行一条数据的条数
			List<ContentSelect> tlist3;
			// 获取一行两条数据的条数
			List<ContentSelect> tlist5;
			// 获取第三行信息
			ContentPolicy policy3 = contentMng.getPolicyByShows(3,enterpriseId);
			if (policy3 != null) {
				tlist3 = contentMng.getNumByShows(3, policy3.getNum(),enterpriseId);
				map.put("3", tlist3);
			} else {
				tlist3 = contentMng.getNumByShows(3, 2,enterpriseId);
				map.put("3", tlist3);
			}
			ContentPolicy policy5 = contentMng.getPolicyByShows(5,enterpriseId);
			if (policy5 != null) {
				tlist5 = contentMng.getNumByShows(5, policy5.getNum(),enterpriseId);
				map.put("5", tlist5);
			} else {
				tlist5 = contentMng.getNumByShows(5, 2,enterpriseId);
				map.put("5", tlist5);
			}
			//获取企业信息
			String logo = iLiveEnterprise.getEnterpriseImg();
			String name = iLiveEnterprise.getEnterpriseName();
			int num = fansMng.getFansNum(enterpriseId);
			HashMap<String, Object> enter = new HashMap<>();
			enter.put("logo", logo);
			enter.put("name",name);
			enter.put("num",num);
			map.put("enterprise", enter);
			JSONArray json = JSONArray.fromObject(map);
			ResponseUtils.renderJson(request,response, json.toString());
		}
		
		
	}
}
