package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.base.BaseUserViewLog;

@SuppressWarnings("serial")
public class UserViewLog extends BaseUserViewLog {
	/**
	 * 状态：进入
	 */
	public static final Integer STATUS_ENTER = 0;
	/**
	 * 状态：离开
	 */
	public static final Integer STATUS_LEAVE = 1;
	
	public void initFieldValue() {
	}

	public UserViewLog() {
		super();
	}

	public UserViewLog(Long liveEventId, String userId, Integer userType, Timestamp lastStatisticTime,
			Long viewTotalTime) {
		super(liveEventId, userId, userType, lastStatisticTime, viewTotalTime);
	}

}
