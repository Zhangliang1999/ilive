package com.jwzt.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LJ-silver
 */
public class Utils {
	/**
	 * 从ip的字符串形式得到字节数组形式
	 * 
	 * @param ip
	 *            字符串形式的ip
	 * @return 字节数组形式的ip
	 */
	public static byte[] getIpByteArrayFromString(String ip) {
		byte[] ret = new byte[4];
		java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
		try {
			ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
 
	public static void main(String args[]) {
		byte[] a = getIpByteArrayFromString(args[0]);
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);
		System.out.println(getIpStringFromBytes(a));
	}
 
	/**
	 * 对原始字符串进行编码转换，如果失败，返回原始的字符串
	 * 
	 * @param s
	 *            原始字符串
	 * @param srcEncoding
	 *            源编码方式
	 * @param destEncoding
	 *            目标编码方式
	 * @return 转换编码后的字符串，失败返回原始字符串
	 */
	public static String getString(String s, String srcEncoding,
			String destEncoding) {
		try {
			return new String(s.getBytes(srcEncoding), destEncoding);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
 
	/**
	 * 根据某种编码方式将字节数组转换成字符串
	 * 
	 * @param b
	 *            字节数组
	 * @param encoding
	 *            编码方式
	 * @return 如果encoding不支持，返回一个缺省编码的字符串
	 */
	public static String getString(byte[] b, String encoding) {
		try {
			return new String(b, encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(b);
		}
	}
 
	/**
	 * 根据某种编码方式将字节数组转换成字符串
	 * 
	 * @param b
	 *            字节数组
	 * @param offset
	 *            要转换的起始位置
	 * @param len
	 *            要转换的长度
	 * @param encoding
	 *            编码方式
	 * @return 如果encoding不支持，返回一个缺省编码的字符串
	 */
	public static String getString(byte[] b, int offset, int len,
			String encoding) {
		try {
			return new String(b, offset, len, encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(b, offset, len);
		}
	}
 
	/**
	 * @param ip
	 *            ip的字节数组形式
	 * @return 字符串形式的ip
	 */
	public static String getIpStringFromBytes(byte[] ip) {
		StringBuffer sb = new StringBuffer();
		sb.append(ip[0] & 0xFF);
		sb.append('.');
		sb.append(ip[1] & 0xFF);
		sb.append('.');
		sb.append(ip[2] & 0xFF);
		sb.append('.');
		sb.append(ip[3] & 0xFF);
		return sb.toString();
	}
	
	public static String[] getArea(String addr){
		
		String[] ret = {"美国","美国"};
		String sp = "(河北省|山西省|辽宁省|吉林省|黑龙江省|江苏省|浙江省|安徽省|福建省|台湾省|江西省|山东省|河南省|湖北省|湖南省|广东省|海南省|四川省|贵州省|云南省|陕西省|甘肃省|青海省|北京市|天津市|上海市|重庆市|内蒙古|广西|西藏|宁夏|新疆|香港|澳门)";
		Pattern pa = Pattern.compile(sp);
		Matcher ma = pa.matcher(addr);
		if (ma.find()) {
			String Sheng = ma.group(1);
			ret[0] = Sheng.replaceAll("省|市", "");
			ret[1] = addr.replaceFirst(Sheng, "");
		}
		return ret;
		
	}

}
