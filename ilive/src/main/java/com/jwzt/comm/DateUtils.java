package com.jwzt.comm;

public class DateUtils {
	public static String DATETIME_FORMAT ="yyyy-MM-dd HH:mm:ss";
	
	public static String format(java.util.Date date , String format){
		
		String ret = "";
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(format);
		ret = sf.format(date);
		return ret;
		
	}
}
