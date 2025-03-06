package com.bvRadio.iLive.iLive.action.util;

import com.bvRadio.iLive.iLive.weiXin.MD5Util;

public class AppSecretUtil {

	/**
	 * 根据md5加密
	 * 
	 * @param product
	 * @return
	 */
	public static String App_screct(String userId) {
		String mw = "jwzt_clouder_user" + userId;
		String app_sign = MD5Util.MD5Encode(mw, "UTF-8").toLowerCase();// 得到以后还要用MD5加密。
		return app_sign;
	}

	// public static void main(String[] args) {
	// AppSecretUtil su = new AppSecretUtil();
	// String app_screct = su.App_screct("1000");
	// System.out.println(app_screct);
	// }

}
