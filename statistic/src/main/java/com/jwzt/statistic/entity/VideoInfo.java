package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.base.BaseVideoInfo;

@SuppressWarnings("serial")
public class VideoInfo extends BaseVideoInfo {

	private VideoStatisticResult statisticResult;

	public VideoStatisticResult getStatisticResult() {
		return statisticResult;
	}

	public void setStatisticResult(VideoStatisticResult statisticResult) {
		this.statisticResult = statisticResult;
	}

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public VideoInfo() {
		super();
	}

}
