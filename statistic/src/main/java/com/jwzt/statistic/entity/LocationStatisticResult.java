package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.base.BaseLocationStatisticResult;

@SuppressWarnings("serial")
public class LocationStatisticResult extends BaseLocationStatisticResult {
	/**
	 * 统计类型：总体 观看
	 */
	public static final Integer FLAG_TYPE_TOTAL_VIEW = 0;
	/**
	 * 统计类型：直播场次 观看
	 */
	public static final Integer FLAG_TYPE_LIVE_EVENT_VIEW = 1;
	/**
	 * 统计类型：企业 推流
	 */
	public static final Integer FLAG_TYPE_ENTERPRISE_BEGIN_LIVE = 2;
	/**
	 * 统计类型：点播回看 观看
	 */
	public static final Integer FLAG_TYPE_VIDEO_VIEW = 3;
	/**
	 * 统计类型：总体 推流
	 */
	public static final Integer FLAG_TYPE_TOTAL_BEGIN_LIVE = 4;
	/**
	 * 统计类型：企业 用户观看
	 */
	public static final Integer FLAG_TYPE_ENTERPRISE_LIVE_VIEW = 5;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatisticTime()) {
			setStatisticTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getFlagType()) {
			setFlagType(FLAG_TYPE_LIVE_EVENT_VIEW);
		}
	}

	public LocationStatisticResult() {
		super();
	}

}
