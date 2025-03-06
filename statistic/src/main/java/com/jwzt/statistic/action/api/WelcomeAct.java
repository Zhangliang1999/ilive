package com.jwzt.statistic.action.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.utils.IpUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.IpAddress;
import com.jwzt.statistic.manager.IpAddressMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.pool.IpAddressPool;

@Controller
public class WelcomeAct {
	@RequestMapping("/ip/null")
	public String index(ModelMap mp) {
		List<Long> ipCodeList = requestLogMng.listNullIpCode();
		for (Long ipCode : ipCodeList) {
			String ip = IpUtils.longToIP(ipCode);
			IpAddressPool.execute(ip);
		}
		List<IpAddress> listNull = ipAddressMng.listNull();
		for (IpAddress ipAddress : listNull) {
			String ip = ipAddress.getIp();
			IpAddressPool.execute(ip);
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}
	@Autowired
	private IpAddressMng ipAddressMng;
	@Autowired
	private RequestLogMng requestLogMng;
}
