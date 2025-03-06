package com.jwzt.ums.api;

import com.jwzt.live4g.mgr.UmsMountInfo;

import java.util.List;

public class umsTest {
	public static void main(String[] args) {
		UmsApi umsApi = new UmsApi("live01.zbt.tv189.net", "1935");
//		boolean ret=umsApi.SetRelayAddrThird("1","live","live5506_tzwj_5000k","rtmp://ps3.live.panda.tv/live_panda/https://www.panda.tv/2324030c" );
		List<UmsMountInfo> umsMountName = umsApi.getUmsMountName();
		for (UmsMountInfo item : umsMountName) {
			System.out.println(item.getMountName());
		}
	}
}
