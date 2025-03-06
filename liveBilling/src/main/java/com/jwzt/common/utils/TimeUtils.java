package com.jwzt.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeUtils {
	private static final Logger log = LogManager.getLogger();

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
		if (time <= 0) {
			return "00:00:00";
		} else {
			minute = time / 60;
			hour = minute / 60;
			if (hour > 99) {
				return "99:59:59";
			}
			minute = minute % 60;
			second = time - hour * 3600 - minute * 60;
			timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
		}
		return timeStr;
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
			log.info("开始时间格式错误。startTime = {}", startTime);
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

	/**
	 * 获取到下一个00:00:00的毫秒数
	 * 
	 * @param startTime
	 *            开始时间，默认01:00:00
	 * @return
	 */
	public static long getDayCountdown(Date startTime) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(startTime);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int min = calendar.get(Calendar.HOUR_OF_DAY);
			int sec = calendar.get(Calendar.HOUR_OF_DAY);
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, min);
			calendar.set(Calendar.SECOND, sec);
		} catch (Exception e) {
			log.info("开始时间格式错误。startTime = {}", startTime);
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

	/**
	 * String日期格式化
	 * 
	 * @param time
	 *            string日期
	 * @param format
	 *            格式 例：yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static Date formatString(String time, String format) {
		Date parse = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			parse = dateFormat.parse(time);
		} catch (Exception e) {
			log.debug("日期格式化失败,timeStr={}", time, e);
		}
		return parse;
	}

	public static String formatDate(Date time, String format) {
		String parse = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			parse = dateFormat.format(time);
		} catch (Exception e) {
			log.debug("日期格式化失败,timeStr={}", time, e);
		}
		return parse;
	}

	public static String formatObj(Object time, String format) {
		String parse = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			parse = dateFormat.format(time);
		} catch (Exception e) {
			log.debug("日期格式化失败,timeStr={}", time, e);
		}
		return parse;
	}

	/**
	 * 获取开始时间和结束时间间隔时长
	 * 
	 * @param startTime
	 * @param nowTime
	 * @return
	 */
	public static long getPlayTime(Date startTime, Date endTime) {
		long time = endTime.getTime() - startTime.getTime();
		return time;
	}

	/**
	 * 小时格式转换为Long毫秒数
	 * 
	 * @param time
	 *            例 01:20:01
	 * @return
	 */
	public static long getTimeLong(String time) {
		if (time == "") {
			return 0;
		}
		String[] split = time.split(":");
		long l = 0;
		l += Integer.parseInt(split[0]) * 3600000;
		l += Integer.parseInt(split[1]) * 60000;
		l += Integer.parseInt(split[2]) * 1000;
		return l;
	}

	/**
	 * 毫秒数转化为小时格式 例 01:20:01
	 * 
	 * @param mss
	 * @return
	 */
	public static String formatDuring(long mss) {
		Long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		Long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		Long seconds = (mss % (1000 * 60)) / 1000;
		String hourStr = "";
		String minuteStr = "";
		String secondStr = "";
		if (hours == 0) {
			hourStr = "00";
		} else if (hours < 10) {
			hourStr = "0" + hours;
		} else {
			hourStr = hours.toString();
		}

		if (minutes == 0) {
			minuteStr = "00";
		} else if (minutes < 10) {
			minuteStr = "0" + minutes;
		} else {
			minuteStr = minutes.toString();
		}
		if (seconds == 0) {
			secondStr = "00";
		} else if (seconds < 10) {
			secondStr = "0" + seconds;
		} else {
			secondStr = seconds.toString();
		}
		return hourStr + ":" + minuteStr + ":" + secondStr;
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
	 * * 获取指定日期是星期几
	 * 
	 * 参数为null时表示获取当前日期是星期几
	 * 
	 * @param date
	 * 
	 * @return
	 */

	public static Integer getWeekOfDate(Date date) {

		Calendar calendar = Calendar.getInstance();

		if (date != null) {

			calendar.setTime(date);

		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (w < 0) {

			w = 0;

		}
		return w;

	}

	/**
	 * 获取当天的开始和结束时间
	 * 
	 * @param findTime
	 * @return
	 */
	public static DataDate getDataDate(Date findTime) {
		DataDate dataDate = new DataDate();
		Calendar instance = Calendar.getInstance();
		instance.setTime(findTime);
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		Date startTime = instance.getTime();
		instance.set(Calendar.HOUR_OF_DAY, 23);
		instance.set(Calendar.MINUTE, 59);
		instance.set(Calendar.SECOND, 59);
		Date endTime = instance.getTime();
		dataDate.setStartTime(startTime);
		dataDate.setEndTime(endTime);
		return dataDate;
	}

	/**
	 * 获取两个日期相差天数
	 * 
	 * @param oldDate
	 * @param newDate
	 * @return
	 */
	public static int getIntervalDays(Date oldDate, Date newDate) {
		if (oldDate.after(newDate)) {
			throw new IllegalArgumentException("时间先后顺序不对!");
		}
		// 将转换的两个时间对象转换成Calendard对象
		Calendar can1 = Calendar.getInstance();
		can1.setTime(oldDate);
		Calendar can2 = Calendar.getInstance();
		can2.setTime(newDate);
		// 拿出两个年份
		int year1 = can1.get(Calendar.YEAR);
		int year2 = can2.get(Calendar.YEAR);
		// 天数
		int days = 0;
		Calendar can = null;
		// 减去小的时间在这一年已经过了的天数
		// 加上大的时间已过的天数
		days -= can1.get(Calendar.DAY_OF_YEAR);
		days += can2.get(Calendar.DAY_OF_YEAR);
		can = can1;
		for (int i = 0; i < Math.abs(year2 - year1); i++) {
			// 获取小的时间当前年的总天数
			days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
			// 再计算下一年。
			can.add(Calendar.YEAR, 1);
		}
		return days;
	}

	/**
	 * 获取两个日期所有间隔时间
	 * 
	 * @param startTimeStr
	 * @param endTimeStr
	 * @return
	 */
	public static List<String> getEveryDay(String startTimeStr, String endTimeStr) {
		List<String> list = new ArrayList<String>();
		Date startTime = TimeUtils.formatString(startTimeStr, "yyyy-MM-dd");
		Date endTime = TimeUtils.formatString(endTimeStr, "yyyy-MM-dd");

		int day = getIntervalDays(startTime, endTime);
		for (int i = 0; i <= day; i++) {
			Calendar instance = Calendar.getInstance();
			instance.setTime(startTime);
			instance.add(Calendar.DAY_OF_MONTH, i);
			list.add(TimeUtils.formatDate(instance.getTime(), "yyyy-MM-dd"));
		}
		return list;
	}

	public static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return yesterday;
	}

	public static void main(String[] args) {
		getEveryDay("2016-12-4", "2016-12-10");
	}

	public static class DataDate {
		private Date startTime;

		private Date endTime;

		public Date getStartTime() {
			return startTime;
		}

		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}

		public Date getEndTime() {
			return endTime;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
	}

}
