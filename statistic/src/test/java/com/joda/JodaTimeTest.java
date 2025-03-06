package com.joda;

import org.joda.time.DateTime;

/**
 * @author ysf
 */
public class JodaTimeTest {
	public static void main(String[] args) {

		DateTime statisticStartTime = new DateTime(2018, 5, 3, 20, 0, 20);
		DateTime statisticEndTime = new DateTime(2018, 5, 3, 20, 5, 00);
		DateTime minuteStatisticStartTime = statisticStartTime;
		DateTime minuteStatisticEndTime = null;
		while (null == minuteStatisticEndTime || minuteStatisticEndTime.getMillis() < statisticEndTime.getMillis()) {
			if (minuteStatisticStartTime.plusMinutes(1).getMillis() < statisticEndTime.getMillis()) {
				minuteStatisticEndTime = minuteStatisticStartTime.plusMinutes(1);
			} else {
				minuteStatisticEndTime = statisticEndTime;
			}
			System.out.println(minuteStatisticStartTime.toString("yyyy-MM-dd HH:mm:ss") + "|"
					+ minuteStatisticEndTime.toString("yyyy-MM-dd HH:mm:ss"));
			minuteStatisticStartTime = minuteStatisticEndTime;
		}
	}
}
