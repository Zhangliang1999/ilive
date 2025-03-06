package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.base.BaseEnterpriseStatisticResult;

@SuppressWarnings("serial")
public class EnterpriseStatisticResult extends BaseEnterpriseStatisticResult {

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatisticTime()) {
			setStatisticTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	/**
	 * 补全空数据
	 */
	public void fillBlank() {
		if (null == getLiveEventTotalNum()) {
			setLiveEventTotalNum(0L);
		}
		if (null == getLiveTotalDuration()) {
			setLiveTotalDuration(0L);
		}
		if (null == getLiveViewTotalNum()) {
			setLiveViewTotalNum(0L);
		}
		if (null == getLiveViewUserTotalNum()) {
			setLiveViewUserTotalNum(0L);
		}
		if (null == getLiveLikeTotalNum()) {
			setLiveLikeTotalNum(0L);
		}
		if (null == getLiveCommentTotalNum()) {
			setLiveCommentTotalNum(0L);
		}
		if (null == getLiveShareTotalNum()) {
			setLiveShareTotalNum(0L);
		}
		if (null == getAndroidViewUserNum()) {
			setAndroidViewUserNum(0L);
		}
		if (null == getIosViewUserNum()) {
			setIosViewUserNum(0L);
		}
		if (null == getPcViewUserNum()) {
			setPcViewUserNum(0L);
		}
		if (null == getWapViewUserNum()) {
			setWapViewUserNum(0L);
		}
		if (null == getOtherViewUserNum()) {
			setOtherViewUserNum(0L);
		}
		if (null == getDurationViewUserNum0To5()) {
			setDurationViewUserNum0To5(0L);
		}
		if (null == getDurationViewUserNum5To10()) {
			setDurationViewUserNum5To10(0L);
		}
		if (null == getDurationViewUserNum10To30()) {
			setDurationViewUserNum10To30(0L);
		}
		if (null == getDurationViewUserNum30To60()) {
			setDurationViewUserNum30To60(0L);
		}
		if (null == getDurationViewUserNum60To()) {
			setDurationViewUserNum60To(0L);
		}
		if (null == getViewTimeHour0To3()) {
			setViewTimeHour0To3(0L);
		}
		if (null == getViewTimeHour3To6()) {
			setViewTimeHour3To6(0L);
		}
		if (null == getViewTimeHour6To9()) {
			setViewTimeHour6To9(0L);
		}
		if (null == getViewTimeHour9To12()) {
			setViewTimeHour9To12(0L);
		}
		if (null == getViewTimeHour12To15()) {
			setViewTimeHour12To15(0L);
		}
		if (null == getViewTimeHour15To18()) {
			setViewTimeHour15To18(0L);
		}
		if (null == getViewTimeHour18To21()) {
			setViewTimeHour18To21(0L);
		}
		if (null == getViewTimeHour21To24()) {
			setViewTimeHour21To24(0L);
		}
		if (null == getBeginLiveHour0To3()) {
			setBeginLiveHour0To3(0L);
		}
		if (null == getBeginLiveHour3To6()) {
			setBeginLiveHour3To6(0L);
		}
		if (null == getBeginLiveHour6To9()) {
			setBeginLiveHour6To9(0L);
		}
		if (null == getBeginLiveHour9To12()) {
			setBeginLiveHour9To12(0L);
		}
		if (null == getBeginLiveHour12To15()) {
			setBeginLiveHour12To15(0L);
		}
		if (null == getBeginLiveHour15To18()) {
			setBeginLiveHour15To18(0L);
		}
		if (null == getBeginLiveHour18To21()) {
			setBeginLiveHour18To21(0L);
		}
		if (null == getBeginLiveHour21To24()) {
			setBeginLiveHour21To24(0L);
		}
		if (null == getFansNum()) {
			setFansNum(0L);
		}
		if (null == getRedPacketMoney()) {
			setRedPacketMoney(0.0);
		}
		if (null == getGiftNum()) {
			setGiftNum(0L);
		}
	}

	public EnterpriseStatisticResult() {
		super();
	}

}
