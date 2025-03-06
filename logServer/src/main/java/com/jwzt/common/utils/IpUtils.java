package com.jwzt.common.utils;

/**
 * 
 * @author ysf
 *
 */
public class IpUtils {
	/**
	 * 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
	 * 
	 * @param strIp
	 * @return
	 */
	public static long ipToLong(String strIp) {
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = strIp.indexOf(".");
		int position2 = strIp.indexOf(".", position1 + 1);
		int position3 = strIp.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(strIp.substring(0, position1));
		ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIp.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	/**
	 * 将十进制整数形式转换成127.0.0.1形式的ip地址
	 * 
	 * @param longIp
	 * @return
	 */
	public static String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	/**
	 * 将ip地址转换为一个字符串
	 */
	public static String ipToString(String ipAddr) {
		StringBuilder retValBuilder = new StringBuilder();
		String temp = "";
		temp = ipAddr;
		String[] ipSegments = null;
		ipSegments = temp.split("\\.");
		for (int i = 0; i < ipSegments.length; i++) {
			{
				if (ipSegments[i].length() == 1) {
					retValBuilder.append("00" + ipSegments[i]);
				}

				if (ipSegments[i].length() == 2) {
					retValBuilder.append("0" + ipSegments[i]);
				}

				if (ipSegments[i].length() == 3) {
					retValBuilder.append(ipSegments[i]);
				}
			}

		}
		return retValBuilder.toString();
	}
	// public static void main(String[] args) {
	// String ipStr = "0.0.111.1";
	// long longIp = IPUtils.ipToLong(ipStr);
	// System.out.println("整数形式为：" + longIp);
	// System.out.println("整数" + longIp + "转化成字符串IP地址：" +
	// IPUtils.longToIP(longIp));
	// // ip地址转化成二进制形式输出
	// System.out.println("二进制形式为：" + Long.toBinaryString(longIp));
	// String a=IPUtils.ipToString(ipStr);
	// String aString=ipStr.replace(".","");
	// System.out.println(ipStr.replace(".","")+"**");
	// System.out.println(aString+"*****");
	// System.out.println(Long.valueOf(aString));
	// System.out.println(Long.valueOf(a));
	// }
}
