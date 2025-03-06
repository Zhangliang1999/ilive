package com.jwzt.soms.web.ums;

import com.jwzt.common.SomsConfigInfo;

/**
 * 启动tomcat时执行线程
 * @author jwzt
 *
 */
public class OpenUmsThread extends Thread{
	@Override
	public void run() {
		try {
			if(!"1".equals(SomsConfigInfo.get("linuxConfig"))) {
				RunUms run=new RunUms();
				//匹配Application.xml
				OperateUms.FindFiletoUms();
				//匹配config.xml
				OperateUms.PanUMSStreamPort();
				//启动UMS程序
				run.RunUmss(SomsConfigInfo.getHomePath()+"/UMS/BVUMServerADV_X64.exe");
			}
		} catch (Exception e) {
			System.out.println("线程执行异常");
			e.printStackTrace();
		}
	}
}
