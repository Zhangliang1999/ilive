package com.jwzt.statistic.entity;

import com.jwzt.statistic.entity.base.BaseLiveStatisticResult;

@SuppressWarnings("serial")
public class LiveStatisticResult extends BaseLiveStatisticResult {

	public void initFieldValue() {
		
	}

	/**
	 * 补全空数据
	 */
	public void fillBlank() {
		if (null == getUserNum()) {
			setUserNum(0L);
		}
		if (null == getShareNum()) {
			setShareNum(0L);
		}
		if (null == getLikeNum()) {
			setLikeNum(0L);
		}
		if (null == getCommentNum()) {
			setCommentNum(0L);
		}
		if (null == getMaxMinute()) {
			setMaxMinute(0L);
		}
		if (null == getMaxMinuteUserNum()) {
			setMaxMinuteUserNum(0L);
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
		if (null == getViewDuraionUserNum0To5()) {
			setViewDuraionUserNum0To5(0L);
		}
		if (null == getViewDuraionUserNum5To10()) {
			setViewDuraionUserNum5To10(0L);
		}
		if (null == getViewDuraionUserNum10To30()) {
			setViewDuraionUserNum10To30(0L);
		}
		if (null == getViewDuraionUserNum30To60()) {
			setViewDuraionUserNum30To60(0L);
		}
		if (null == getViewDuraionUserNum60To()) {
			setViewDuraionUserNum60To(0L);
		}
	}

	public LiveStatisticResult() {
		super();
	}

	public LiveStatisticResult(Long id) {
		super(id);
	}

}
