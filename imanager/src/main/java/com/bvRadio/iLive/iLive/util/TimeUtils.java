package com.bvRadio.iLive.iLive.util;

public class TimeUtils {
	/**
	 * 秒转时长，格式：“xx:xx:xx”
	 * 
	 * @param time
	 * @return
	 */
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00:00";
		else {
			minute = time / 60;
			hour = minute / 60;
			if (hour > 99)
				return "99:59:59";
			minute = minute % 60;
			second = time - hour * 3600 - minute * 60;
			timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
		}
		return timeStr;
	}

	private static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10) {
			retStr = "0" + Integer.toString(i);
		} else {
			retStr = "" + i;
		}
		return retStr;
	}
}
