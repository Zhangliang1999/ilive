package com.jwzt.statistic.task;

import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.entity.IpAddress;
import com.jwzt.statistic.manager.IpAddressMng;

public class IPUpdateTask implements Runnable{

	@Override
	public void run() {
		try {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				return;
			}
			IpAddressMng ipAddressMng = (IpAddressMng) context.getBean("IpAddressMng");
			
			List<IpAddress> notSuccess = ipAddressMng.isNotSuccess();
			if (notSuccess!=null&&notSuccess.size()>0) {
				for(IpAddress ipAddress:notSuccess) {
					IpAddressTask task = new IpAddressTask(ipAddress.getIp());
					task.run();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
