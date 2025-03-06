package com.bvRadio.iLive.iLive.util;

import java.util.Calendar;
import java.util.Date;

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

	public static String secToTime1(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time < 60) {
			return "00:" + time;
		} else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr=  unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr= unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
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
	
	/**
	 * 获取到下一个00:00:00的毫秒数
	 * 
	 * @param startTime
	 *            开始时间，默认01:00:00
	 * @return
	 */
	public static long getDayCountdown(String startTime) {
		Calendar calendar = Calendar.getInstance();
		try {
			String[] startTimeArray = startTime.split(":");
			String parseHour = startTimeArray[0];
			String parseMin = startTimeArray[1];
			String parseSec = startTimeArray[1];
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parseHour));
			calendar.set(Calendar.MINUTE, Integer.parseInt(parseMin));
			calendar.set(Calendar.SECOND, Integer.parseInt(parseSec));
		} catch (Exception e) {
			calendar.set(Calendar.HOUR_OF_DAY, 1);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		}
		Date nowDateTime = new Date();
		Date startDateTime = calendar.getTime();
		if (startDateTime.before(nowDateTime)) {
			startDateTime = addDay(startDateTime, 1);
		}
		long countdown = Math.abs(startDateTime.getTime() - nowDateTime.getTime());
		return countdown;
	}
	public static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
}
