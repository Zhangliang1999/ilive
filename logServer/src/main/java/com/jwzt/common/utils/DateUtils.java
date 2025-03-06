package com.jwzt.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ysf
 */
public class DateUtils {
	public static Date initQueryStartTime(final Date startTime) {
		if (null != startTime) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTime);
			calendar.set(Calendar.HOUR_OF_DAY, 00);
			calendar.set(Calendar.MINUTE, 00);
			calendar.set(Calendar.SECOND, 00);
			return calendar.getTime();
		}
		return null;
	}

	public static Date initQueryEndTime(final Date endTime) {
		if (null != endTime) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			return calendar.getTime();
		}
		return null;
	}
}
