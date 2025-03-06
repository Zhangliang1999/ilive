package com.bvRadio.iLive.iLive.util;

import java.util.Random;

public class RandomUtils {
	/**
	 * 产生指定长度的随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String getRadomNum(Integer length) {
		int ans = 0;
		while (Math.log10(ans) + 1 < length)
			ans = (int) (Math.random() * Math.pow(10, length));
		return ans + "";
	}

	/**
	 * 产生指定长度的字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

}
