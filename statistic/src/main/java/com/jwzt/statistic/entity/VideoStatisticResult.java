package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.base.BaseVideoStatisticResult;

@SuppressWarnings("serial")
public class VideoStatisticResult extends BaseVideoStatisticResult {

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatisticTime()) {
			setStatisticTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public void fillBlank() {
		if (null == getUserTotalNum()) {
			setUserTotalNum(0L);
		}
		if (null == getViewTotalNum()) {
			setViewTotalNum(0L);
		}
		if (null == getShareTotalNum()) {
			setShareTotalNum(0L);
		}
		if (null == getLikeTotalNum()) {
			setLikeTotalNum(0L);
		}
		if (null == getCommentTotalNum()) {
			setCommentTotalNum(0L);
		}
		if (null == getAndroidUserNum()) {
			setAndroidUserNum(0L);
		}
		if (null == getIosUserNum()) {
			setIosUserNum(0L);
		}
		if (null == getPcUserNum()) {
			setPcUserNum(0L);
		}
		if (null == getWapUserNum()) {
			setWapUserNum(0L);
		}
		if (null == getOtherUserNum()) {
			setOtherUserNum(0L);
		}
		if (null == getNewRegisterUserNum()) {
			setNewRegisterUserNum(0L);
		}
		if (null == getOldRegisterUserNum()) {
			setOldRegisterUserNum(0L);
		}
		if (null == getOtherRegisterUserNum()) {
			setOtherRegisterUserNum(0L);
		}
	}

	public VideoStatisticResult() {
		super();
	}

}
