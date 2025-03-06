package com.jwzt.billing.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JWZT-YSF
 * 
 */
public class TimerUtils {
	private static final Logger log = LoggerFactory.getLogger(TimerUtils.class);

	/**
	 * 启动定时器
	 * 
	 * @param startTime
	 *            启动时间，如：09:00:00 默认 01:00:00
	 * @param intervalTime
	 *            间隔时间，单位：毫秒 默认 24*60*60*1000
	 * @return
	 */
	public static Timer startTimer(TimerTask task, String startTime, String intervalTime) {
		Timer timer = new Timer();
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
			log.info("定时器启动时间格式错误。startTime = {}", startTime);
			calendar.set(Calendar.HOUR_OF_DAY, 1);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		}
		Date nowDateTime = new Date();
		Date startDateTime = calendar.getTime();
		if (startDateTime.before(nowDateTime)) {
			startDateTime = addDay(startDateTime, 1);
		}
		int period = 24 * 60 * 60 * 1000;
		try {
			period = Integer.parseInt(intervalTime);
		} catch (Exception e) {
			log.info("定时器间隔时间格式错误。intervalTime = {}", intervalTime);
		}
		long countdown = Math.abs(startDateTime.getTime() - nowDateTime.getTime());
		timer.schedule(task, countdown, period);
		log.info(">>定时器启动 task = {}，距启动时间（毫秒）>>  {}", task.getClass().getName(), countdown);
		return timer;
	}

	/**
	 * 启动定时器
	 * 
	 * @param intervalTime
	 *            间隔时间，单位：毫秒 默认 24*60*60*1000
	 * @return
	 */
	public static Timer startTimer(TimerTask task, String intervalTime) {
		Timer timer = new Timer();
		int period = 24 * 60 * 60 * 1000;
		try {
			period = Integer.parseInt(intervalTime);
		} catch (Exception e) {
			log.info("定时器间隔时间格式错误。intervalTime = {}", intervalTime);
		}
		timer.schedule(task, 0, period);
		log.info(">>定时器启动 task = {}", task.getClass().getName());
		return timer;
	}

	private static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
}
