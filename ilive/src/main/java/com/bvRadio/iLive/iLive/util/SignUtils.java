package com.bvRadio.iLive.iLive.util;

public class SignUtils {

	/**
	 * 获取md5方法
	 * 
	 * @param key
	 * @return
	 */
	public static String getMD5(String key) {
		return Md5Util.encode(key);
	}
}
