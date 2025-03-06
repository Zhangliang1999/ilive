package com.bvRadio.iLive.common.util;

import com.bvRadio.iLive.aliyunlive.LiveConfigs;
import com.bvRadio.iLive.iLive.util.Md5Util;

public class AuthKeyGenerator {

	/**
	 * 
	 * @param uri
	 *            "/wxqz/房间号"
	 * @param privateKey
	 *            "jwztsa"
	 * @param hour
	 *            有效时长 小时
	 * @return
	 */
	public static String generator(String roomId) {

		Long validTime = System.currentTimeMillis()
				+ Integer.parseInt(LiveConfigs.get(LiveConfigs.HOUR)) * 60 * 60 * 1000;
		Long timestamp = validTime / 1000;
		String sstring = "/" + LiveConfigs.get(LiveConfigs.APPNAME) + "/" + roomId + "-" + timestamp + "-0-0-"
				+ LiveConfigs.get(LiveConfigs.MAINKEY);
		String authkey = Md5Util.encode(sstring);
		return timestamp + "-0-0-" + authkey;

	}

}
