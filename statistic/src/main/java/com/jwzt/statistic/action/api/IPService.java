package com.jwzt.statistic.action.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.utils.IpUtils;
import com.jwzt.statistic.entity.IpAddress;
import com.jwzt.statistic.entity.LoginRecord;
import com.jwzt.statistic.manager.IpAddressMng;
import com.jwzt.statistic.manager.LoginRecordMng;
import com.jwzt.statistic.task.IpAddressTask;

@Controller
@RequestMapping(value="ip")
public class IPService {
	private static final String IP_ADDRESS_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=:ip";
	
	@Autowired
	private IpAddressMng ipAddressMng;
	
	@Autowired
	private LoginRecordMng loginRecordMng;
	
	@ResponseBody
	@RequestMapping(value="getAddress")
	public String getAddress(String ip) {
		JSONObject obj = new JSONObject();
		String ipAddressUrl = IP_ADDRESS_URL;
		ipAddressUrl = ipAddressUrl.replace(":ip", ip);
		System.out.println("ipAddressUrl: "+ipAddressUrl);
		long ipCode = IpUtils.ipToLong(ip);
		System.out.println("ipCode: "+ipCode);
		IpAddress ipAddress = ipAddressMng.getBeanByIpCode(ipCode);
		try {
			System.out.println(ipAddress);
			IpAddress ipAddressFromInternet = IpAddressTask.getIpAddressFromInternet(ip);
			if (ipAddress==null) {
				ipAddress = ipAddressFromInternet;
				ipAddressMng.save(ipAddressFromInternet);
			}else {
				ipAddressMng.update(ipAddressFromInternet);
			}
			obj.put("code", "0");
			obj.put("data", ipAddressFromInternet.getRegion()+" "+ipAddressFromInternet.getCity());
		} catch (IOException e) {
			e.printStackTrace();
			if (ipAddress==null) {
				ipAddress = new IpAddress();
				ipAddress.setIp(ip);
				ipAddress.setIpCode(ipCode);
				ipAddress.setIsSuccess(1);
				ipAddressMng.save(ipAddress);
			}
			obj.put("code", "1");
			obj.put("data", "未知");
		}
		return obj.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="saveRecord")
	public String saveRecord(String ip,Long userId) {
		JSONObject obj = new JSONObject();
		try {
			LoginRecord record = new LoginRecord();
			record.setUserId(userId);
			record.setIp(ip);
			record.setIpCode(IpUtils.ipToLong(ip));
			loginRecordMng.save(record);
			getAddress(ip);
			obj.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", 1);
		}
		return obj.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="getByUserId")
	public String getByUserId(Long userId,Integer pageNo,Integer pageSize) {
		JSONObject obj = new JSONObject();
		try {
			Pagination page = loginRecordMng.getPageByUserId(userId, 
					pageNo==null?1:pageNo, pageSize==null?10:pageSize);
			obj.put("code", 0);
			obj.put("data", page);
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", 1);
		}
		return obj.toJSONString();
	}
}
