package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.base.BaseTotalStatisticResult;

@SuppressWarnings("serial")
public class TotalStatisticResult extends BaseTotalStatisticResult {

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatisticTime()) {
			setStatisticTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public TotalStatisticResult() {
		super();
	}

}
