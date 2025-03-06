package com.jwzt.comm;

public class StringUtils {
	public static boolean isEmpty(String str){
		boolean ret = true;
		if(str != null && !str.equals(""))ret = false;
		return ret;
	}
}
