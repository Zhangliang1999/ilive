package com.bvRadio.iLive.iLive.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public enum SafeTyChainPasswdUtil {

	INSTANCE;

	private final static String passEncoder = "tysxtv189";

	// public static void main(String[] args) {
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// try {
	// Date date = sdf.parse("2013-06-23 18:14:26");
	// long time = date.getTime() / 1000L;
	// String hexString = Long.toHexString(time);
	// System.out.println(hexString);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }

	public Map<String, String> generatorEncoderPwd() {
		String generatorSequence = generatorSequence();
		String timestamp = getTimestamp();
		String encodePwd = encode(generatorSequence, timestamp);
		Map<String, String> map = new HashMap<>();
		map.put("sequence", generatorSequence);
		map.put("timestamp", timestamp);
		map.put("encodePwd", encodePwd);
		return map;
	}

	/**
	 * 生成时间戳
	 * 
	 * @return
	 */
	public static String getTimestamp() {
		Date date = new Date();
		long time = (date.getTime() + 5 * 60 * 1000L) / 1000L;
		String hexString = Long.toHexString(time);
		return hexString;
	}

	/**
	 * 生成序列号
	 * 
	 * @return
	 */
	public String generatorSequence() {
		String generateNumber = RandomNumberUtil.generateNumber();
		return generateNumber;
	}

	public String encode(String sequnce, String timestamp) {
		// (时间戳明码+”tysxtv189”+序列号)
		String dest = String.format("%s%s%s", timestamp, passEncoder, sequnce);
		String encode = Md5Util.encode(dest);
		return encode;
	}

}
