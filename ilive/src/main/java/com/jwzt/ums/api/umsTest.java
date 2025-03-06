package com.jwzt.ums.api;
public class umsTest {
	public static void main(String[] args) {
		UmsApi umsApi = new UmsApi("source.live01.zb.tv189.com", "1935");
		boolean ret=umsApi.SetRelayAddrThird("1","live","live5506_tzwj_5000k","rtmp://ps3.live.panda.tv/live_panda/https://www.panda.tv/2324030c" );
		System.out.println(ret);
	}
}
