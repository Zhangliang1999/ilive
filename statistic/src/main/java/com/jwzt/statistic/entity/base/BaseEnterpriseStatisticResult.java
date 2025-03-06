package com.jwzt.statistic.entity.base;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author ysf
 */

@SuppressWarnings("serial")
public class BaseEnterpriseStatisticResult implements java.io.Serializable {
	@JsonIgnore
	private String id;
	@JsonIgnore
	private Integer enterpriseId;
	/**
	 * 直播总场次
	 */
	private Long liveEventTotalNum;
	/**
	 * 直播总时长 单位：秒
	 */
	private Long liveTotalDuration;
	/**
	 * 累计观看总数
	 */
	private Long liveViewTotalNum;
	/**
	 * 累计观看人数
	 */
	private Long liveViewUserTotalNum;
	/**
	 * 总点赞数
	 */
	private Long liveLikeTotalNum;
	/**
	 * 总评论数
	 */
	private Long liveCommentTotalNum;
	/**
	 * 总分享数
	 */
	private Long liveShareTotalNum;
	/**
	 * 用户观看总时长
	 */
	private Long viewTotalDuration;
	/**
	 * 用户观看人次
	 */
	private Long viewTotalNum;
	/**
	 * 用户观看端口来源 安卓
	 */
	private Long androidViewUserNum;
	/**
	 * 用户观看端口来源 IOS
	 */
	private Long iosViewUserNum;
	/**
	 * 用户观看端口来源 PC
	 */
	private Long pcViewUserNum;
	/**
	 * 用户观看端口来源 WAP
	 */
	private Long wapViewUserNum;
	/**
	 * 用户观看端口来源 其他
	 */
	private Long otherViewUserNum;
	/**
	 * 最高收看直播峰值
	 */
	private Long numOfMaxViewUserNumAboutLive;
	/**
	 * 最高收看直播峰值 发生时间
	 */
	private Timestamp timeOfMaxViewUserNumAboutLive;
	/**
	 * 最高收看直播峰值 直播名称
	 */
	private String liveTitleOfMaxViewUserNumAboutLive;
	/**
	 * 最高收看直播峰值 企业名称
	 */
	private String enterpriseNameOfMaxViewUserNumAboutLive;
	/**
	 * 累计用户观看时长0到5分钟以上的直播间数
	 */
	private Long durationViewUserNum0To5;
	/**
	 * 累计用户观看时长5到10分钟以上的直播间数
	 */
	private Long durationViewUserNum5To10;
	/**
	 * 累计用户观看时长10到30分钟以上的直播间数
	 */
	private Long durationViewUserNum10To30;
	/**
	 * 累计用户观看时长30到60分钟以上的直播间数
	 */
	private Long durationViewUserNum30To60;
	/**
	 * 累计用户观看时长60分钟以上的直播间数
	 */
	private Long durationViewUserNum60To;
	/**
	 * 用户观看直播时间点分布 0点3点
	 */
	private Long viewTimeHour0To3;
	/**
	 * 用户观看直播时间点分布 3点6点
	 */
	private Long viewTimeHour3To6;
	/**
	 * 用户观看直播时间点分布 6点9点
	 */
	private Long viewTimeHour6To9;
	/**
	 * 用户观看直播时间点分布 9点12点
	 */
	private Long viewTimeHour9To12;
	/**
	 * 用户观看直播时间点分布 12点15点
	 */
	private Long viewTimeHour12To15;
	/**
	 * 用户观看直播时间点分布 15点18点
	 */
	private Long viewTimeHour15To18;
	/**
	 * 用户观看直播时间点分布 18点21点
	 */
	private Long viewTimeHour18To21;
	/**
	 * 用户观看直播时间点分布 21点24点
	 */
	private Long viewTimeHour21To24;
	/**
	 * 企业直播推流时间点分布 0点3点
	 */
	private Long beginLiveHour0To3;
	/**
	 * 企业直播推流时间点分布 3点6点
	 */
	private Long beginLiveHour3To6;
	/**
	 * 企业直播推流时间点分布 6点9点
	 */
	private Long beginLiveHour6To9;
	/**
	 * 企业直播推流时间点分布 9点12点
	 */
	private Long beginLiveHour9To12;
	/**
	 * 企业直播推流时间点分布 12点15点
	 */
	private Long beginLiveHour12To15;
	/**
	 * 企业直播推流时间点分布 15点18点
	 */
	private Long beginLiveHour15To18;
	/**
	 * 企业直播推流时间点分布 18点21点
	 */
	private Long beginLiveHour18To21;
	/**
	 * 企业直播推流时间点分布 21点24点
	 */
	private Long beginLiveHour21To24;
	/**
	 * 粉丝数
	 */
	private Long fansNum;
	/**
	 * 红包金额
	 */
	private Double redPacketMoney;
	/**
	 * 礼物数
	 */
	private Long giftNum;

	private Timestamp statisticTime;
	@JsonIgnore
	private Timestamp createTime;

	public BaseEnterpriseStatisticResult() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getLiveEventTotalNum() {
		return liveEventTotalNum;
	}

	public void setLiveEventTotalNum(Long liveEventTotalNum) {
		this.liveEventTotalNum = liveEventTotalNum;
	}

	public Long getLiveTotalDuration() {
		return liveTotalDuration;
	}

	public void setLiveTotalDuration(Long liveTotalDuration) {
		this.liveTotalDuration = liveTotalDuration;
	}

	public Long getLiveViewTotalNum() {
		return liveViewTotalNum;
	}

	public void setLiveViewTotalNum(Long liveViewTotalNum) {
		this.liveViewTotalNum = liveViewTotalNum;
	}

	public Long getLiveViewUserTotalNum() {
		return liveViewUserTotalNum;
	}

	public void setLiveViewUserTotalNum(Long liveViewUserTotalNum) {
		this.liveViewUserTotalNum = liveViewUserTotalNum;
	}

	public Long getLiveLikeTotalNum() {
		return liveLikeTotalNum;
	}

	public void setLiveLikeTotalNum(Long liveLikeTotalNum) {
		this.liveLikeTotalNum = liveLikeTotalNum;
	}

	public Long getLiveCommentTotalNum() {
		return liveCommentTotalNum;
	}

	public void setLiveCommentTotalNum(Long liveCommentTotalNum) {
		this.liveCommentTotalNum = liveCommentTotalNum;
	}

	public Long getLiveShareTotalNum() {
		return liveShareTotalNum;
	}

	public void setLiveShareTotalNum(Long liveShareTotalNum) {
		this.liveShareTotalNum = liveShareTotalNum;
	}

	public Long getViewTotalDuration() {
		return viewTotalDuration;
	}

	public void setViewTotalDuration(Long viewTotalDuration) {
		this.viewTotalDuration = viewTotalDuration;
	}

	public Long getViewTotalNum() {
		return viewTotalNum;
	}

	public void setViewTotalNum(Long viewTotalNum) {
		this.viewTotalNum = viewTotalNum;
	}

	public Long getAndroidViewUserNum() {
		return androidViewUserNum;
	}

	public void setAndroidViewUserNum(Long androidViewUserNum) {
		this.androidViewUserNum = androidViewUserNum;
	}

	public Long getIosViewUserNum() {
		return iosViewUserNum;
	}

	public void setIosViewUserNum(Long iosViewUserNum) {
		this.iosViewUserNum = iosViewUserNum;
	}

	public Long getPcViewUserNum() {
		return pcViewUserNum;
	}

	public void setPcViewUserNum(Long pcViewUserNum) {
		this.pcViewUserNum = pcViewUserNum;
	}

	public Long getWapViewUserNum() {
		return wapViewUserNum;
	}

	public void setWapViewUserNum(Long wapViewUserNum) {
		this.wapViewUserNum = wapViewUserNum;
	}

	public Long getOtherViewUserNum() {
		return otherViewUserNum;
	}

	public void setOtherViewUserNum(Long otherViewUserNum) {
		this.otherViewUserNum = otherViewUserNum;
	}

	public Long getNumOfMaxViewUserNumAboutLive() {
		return numOfMaxViewUserNumAboutLive;
	}

	public void setNumOfMaxViewUserNumAboutLive(Long numOfMaxViewUserNumAboutLive) {
		this.numOfMaxViewUserNumAboutLive = numOfMaxViewUserNumAboutLive;
	}

	public Timestamp getTimeOfMaxViewUserNumAboutLive() {
		return timeOfMaxViewUserNumAboutLive;
	}

	public void setTimeOfMaxViewUserNumAboutLive(Timestamp timeOfMaxViewUserNumAboutLive) {
		this.timeOfMaxViewUserNumAboutLive = timeOfMaxViewUserNumAboutLive;
	}

	public String getLiveTitleOfMaxViewUserNumAboutLive() {
		return liveTitleOfMaxViewUserNumAboutLive;
	}

	public void setLiveTitleOfMaxViewUserNumAboutLive(String liveTitleOfMaxViewUserNumAboutLive) {
		this.liveTitleOfMaxViewUserNumAboutLive = liveTitleOfMaxViewUserNumAboutLive;
	}

	public String getEnterpriseNameOfMaxViewUserNumAboutLive() {
		return enterpriseNameOfMaxViewUserNumAboutLive;
	}

	public void setEnterpriseNameOfMaxViewUserNumAboutLive(String enterpriseNameOfMaxViewUserNumAboutLive) {
		this.enterpriseNameOfMaxViewUserNumAboutLive = enterpriseNameOfMaxViewUserNumAboutLive;
	}

	public Long getDurationViewUserNum0To5() {
		return durationViewUserNum0To5;
	}

	public void setDurationViewUserNum0To5(Long durationViewUserNum0To5) {
		this.durationViewUserNum0To5 = durationViewUserNum0To5;
	}

	public Long getDurationViewUserNum5To10() {
		return durationViewUserNum5To10;
	}

	public void setDurationViewUserNum5To10(Long durationViewUserNum5To10) {
		this.durationViewUserNum5To10 = durationViewUserNum5To10;
	}

	public Long getDurationViewUserNum10To30() {
		return durationViewUserNum10To30;
	}

	public void setDurationViewUserNum10To30(Long durationViewUserNum10To30) {
		this.durationViewUserNum10To30 = durationViewUserNum10To30;
	}

	public Long getDurationViewUserNum30To60() {
		return durationViewUserNum30To60;
	}

	public void setDurationViewUserNum30To60(Long durationViewUserNum30To60) {
		this.durationViewUserNum30To60 = durationViewUserNum30To60;
	}

	public Long getDurationViewUserNum60To() {
		return durationViewUserNum60To;
	}

	public void setDurationViewUserNum60To(Long durationViewUserNum60To) {
		this.durationViewUserNum60To = durationViewUserNum60To;
	}

	public Long getViewTimeHour0To3() {
		return viewTimeHour0To3;
	}

	public void setViewTimeHour0To3(Long viewTimeHour0To3) {
		this.viewTimeHour0To3 = viewTimeHour0To3;
	}

	public Long getViewTimeHour3To6() {
		return viewTimeHour3To6;
	}

	public void setViewTimeHour3To6(Long viewTimeHour3To6) {
		this.viewTimeHour3To6 = viewTimeHour3To6;
	}

	public Long getViewTimeHour6To9() {
		return viewTimeHour6To9;
	}

	public void setViewTimeHour6To9(Long viewTimeHour6To9) {
		this.viewTimeHour6To9 = viewTimeHour6To9;
	}

	public Long getViewTimeHour9To12() {
		return viewTimeHour9To12;
	}

	public void setViewTimeHour9To12(Long viewTimeHour9To12) {
		this.viewTimeHour9To12 = viewTimeHour9To12;
	}

	public Long getViewTimeHour12To15() {
		return viewTimeHour12To15;
	}

	public void setViewTimeHour12To15(Long viewTimeHour12To15) {
		this.viewTimeHour12To15 = viewTimeHour12To15;
	}

	public Long getViewTimeHour15To18() {
		return viewTimeHour15To18;
	}

	public void setViewTimeHour15To18(Long viewTimeHour15To18) {
		this.viewTimeHour15To18 = viewTimeHour15To18;
	}

	public Long getViewTimeHour18To21() {
		return viewTimeHour18To21;
	}

	public void setViewTimeHour18To21(Long viewTimeHour18To21) {
		this.viewTimeHour18To21 = viewTimeHour18To21;
	}

	public Long getViewTimeHour21To24() {
		return viewTimeHour21To24;
	}

	public void setViewTimeHour21To24(Long viewTimeHour21To24) {
		this.viewTimeHour21To24 = viewTimeHour21To24;
	}

	public Long getBeginLiveHour0To3() {
		return beginLiveHour0To3;
	}

	public void setBeginLiveHour0To3(Long beginLiveHour0To3) {
		this.beginLiveHour0To3 = beginLiveHour0To3;
	}

	public Long getBeginLiveHour3To6() {
		return beginLiveHour3To6;
	}

	public void setBeginLiveHour3To6(Long beginLiveHour3To6) {
		this.beginLiveHour3To6 = beginLiveHour3To6;
	}

	public Long getBeginLiveHour6To9() {
		return beginLiveHour6To9;
	}

	public void setBeginLiveHour6To9(Long beginLiveHour6To9) {
		this.beginLiveHour6To9 = beginLiveHour6To9;
	}

	public Long getBeginLiveHour9To12() {
		return beginLiveHour9To12;
	}

	public void setBeginLiveHour9To12(Long beginLiveHour9To12) {
		this.beginLiveHour9To12 = beginLiveHour9To12;
	}

	public Long getBeginLiveHour12To15() {
		return beginLiveHour12To15;
	}

	public void setBeginLiveHour12To15(Long beginLiveHour12To15) {
		this.beginLiveHour12To15 = beginLiveHour12To15;
	}

	public Long getBeginLiveHour15To18() {
		return beginLiveHour15To18;
	}

	public void setBeginLiveHour15To18(Long beginLiveHour15To18) {
		this.beginLiveHour15To18 = beginLiveHour15To18;
	}

	public Long getBeginLiveHour18To21() {
		return beginLiveHour18To21;
	}

	public void setBeginLiveHour18To21(Long beginLiveHour18To21) {
		this.beginLiveHour18To21 = beginLiveHour18To21;
	}

	public Long getBeginLiveHour21To24() {
		return beginLiveHour21To24;
	}

	public void setBeginLiveHour21To24(Long beginLiveHour21To24) {
		this.beginLiveHour21To24 = beginLiveHour21To24;
	}

	public Long getFansNum() {
		return fansNum;
	}

	public void setFansNum(Long fansNum) {
		this.fansNum = fansNum;
	}

	public Double getRedPacketMoney() {
		return redPacketMoney;
	}

	public void setRedPacketMoney(Double redPacketMoney) {
		this.redPacketMoney = redPacketMoney;
	}

	public Long getGiftNum() {
		return giftNum;
	}

	public void setGiftNum(Long giftNum) {
		this.giftNum = giftNum;
	}

	public Timestamp getStatisticTime() {
		return statisticTime;
	}

	public void setStatisticTime(Timestamp statisticTime) {
		this.statisticTime = statisticTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
