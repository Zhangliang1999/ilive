package com.bvRadio.iLive.iLive.util;

import java.util.regex.Pattern;

public class StringPatternUtil {

	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 字符串转化为空字符
	 * 
	 * @return
	 */
	public static String convertEmpty(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

}
