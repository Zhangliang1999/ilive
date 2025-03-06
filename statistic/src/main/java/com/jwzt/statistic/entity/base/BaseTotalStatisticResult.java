package com.jwzt.statistic.entity.base;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author ysf
 */

@SuppressWarnings("serial")
public class BaseTotalStatisticResult implements java.io.Serializable {

	private String id;
	/**
	 * 直播总场次
	 */
	private Long liveEventTotalNum;
	/**
	 * 企业数
	 */
	private Long liveEnterpriseTotalNum;
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
	 * 累计用户总数
	 */
	private Long userTotalNum;
	/**
	 * 用户观看总时长
	 */
	private Long viewTotalDuration;
	/**
	 * 用户观看人次
	 */
	private Long viewTotalNum;
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
	 * 直播间新用户数
	 */
	private Long newRegisterUserNumAboutLive;
	/**
	 * 直播间老用户数
	 */
	private Long oldRegisterUserNumAboutLive;
	/**
	 * 每天新增用户数
	 */
	private Long newRegisterUserNumAboutDay;
	/**
	 * 每天老用户数
	 */
	private Long oldRegisterUserNumAboutDay;
	/**
	 * 新企业数
	 */
	private Long newRegisterEnterpriseNum;
	/**
	 * 老企业数
	 */
	private Long oldRegisterEnterpriseNum;
	/**
	 * 累计直播时长0到10分钟的直播间数
	 */
	private Long durationLiveNum0To10;
	/**
	 * 累计直播时长10到100分钟的直播间数
	 */
	private Long durationLiveNum10To100;
	/**
	 * 累计直播时长100到200分钟的直播间数
	 */
	private Long durationLiveNum100To200;
	/**
	 * 累计直播时长200到300分钟的直播间数
	 */
	private Long durationLiveNum200To300;
	/**
	 * 累计直播时长300分钟以上的直播间数
	 */
	private Long durationLiveNum300To;
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
	 * 最高注册峰值
	 */
	private Long numOfMaxNewRegisterUserNumAboutLive;
	/**
	 * 最高注册峰值 发生时间
	 */
	private Timestamp timeOfMaxNewRegisterUserNumAboutLive;
	/**
	 * 最高注册峰值 直播名称
	 */
	private String liveTitleOfMaxNewRegisterUserNumAboutLive;
	/**
	 * 最高注册峰值 企业名称
	 */
	private String enterpriseNameOfMaxNewRegisterUserNumAboutLive;
	/**
	 * 用户注册端口来源 安卓
	 */
	private Long androidRegisterUserNum;
	/**
	 * 用户注册端口来源 IOS
	 */
	private Long iosRegisterUserNum;
	/**
	 * 用户注册端口来源 PC
	 */
	private Long pcRegisterUserNum;
	/**
	 * 用户注册端口来源 WAP
	 */
	private Long wapRegisterUserNum;
	/**
	 * 用户注册端口来源 其他
	 */
	private Long otherRegisterUserNum;
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
	 * 统计时间
	 */
	private Timestamp statisticTime;
	/**
	 * 创建时间
	 */
	@JsonIgnore
	private Timestamp createTime;

	public BaseTotalStatisticResult() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getLiveEventTotalNum() {
		return liveEventTotalNum;
	}

	public void setLiveEventTotalNum(Long liveEventTotalNum) {
		this.liveEventTotalNum = liveEventTotalNum;
	}

	public Long getLiveEnterpriseTotalNum() {
		return liveEnterpriseTotalNum;
	}

	public void setLiveEnterpriseTotalNum(Long liveEnterpriseTotalNum) {
		this.liveEnterpriseTotalNum = liveEnterpriseTotalNum;
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

	public Long getUserTotalNum() {
		return userTotalNum;
	}

	public void setUserTotalNum(Long userTotalNum) {
		this.userTotalNum = userTotalNum;
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

	public Long getNewRegisterUserNumAboutLive() {
		return newRegisterUserNumAboutLive;
	}

	public void setNewRegisterUserNumAboutLive(Long newRegisterUserNumAboutLive) {
		this.newRegisterUserNumAboutLive = newRegisterUserNumAboutLive;
	}

	public Long getOldRegisterUserNumAboutLive() {
		return oldRegisterUserNumAboutLive;
	}

	public void setOldRegisterUserNumAboutLive(Long oldRegisterUserNumAboutLive) {
		this.oldRegisterUserNumAboutLive = oldRegisterUserNumAboutLive;
	}

	public Long getNewRegisterUserNumAboutDay() {
		return newRegisterUserNumAboutDay;
	}

	public void setNewRegisterUserNumAboutDay(Long newRegisterUserNumAboutDay) {
		this.newRegisterUserNumAboutDay = newRegisterUserNumAboutDay;
	}

	public Long getOldRegisterUserNumAboutDay() {
		return oldRegisterUserNumAboutDay;
	}

	public void setOldRegisterUserNumAboutDay(Long oldRegisterUserNumAboutDay) {
		this.oldRegisterUserNumAboutDay = oldRegisterUserNumAboutDay;
	}

	public Long getNewRegisterEnterpriseNum() {
		return newRegisterEnterpriseNum;
	}

	public void setNewRegisterEnterpriseNum(Long newRegisterEnterpriseNum) {
		this.newRegisterEnterpriseNum = newRegisterEnterpriseNum;
	}

	public Long getOldRegisterEnterpriseNum() {
		return oldRegisterEnterpriseNum;
	}

	public void setOldRegisterEnterpriseNum(Long oldRegisterEnterpriseNum) {
		this.oldRegisterEnterpriseNum = oldRegisterEnterpriseNum;
	}

	public Long getDurationLiveNum0To10() {
		return durationLiveNum0To10;
	}

	public void setDurationLiveNum0To10(Long durationLiveNum0To10) {
		this.durationLiveNum0To10 = durationLiveNum0To10;
	}

	public Long getDurationLiveNum10To100() {
		return durationLiveNum10To100;
	}

	public void setDurationLiveNum10To100(Long durationLiveNum10To100) {
		this.durationLiveNum10To100 = durationLiveNum10To100;
	}

	public Long getDurationLiveNum100To200() {
		return durationLiveNum100To200;
	}

	public void setDurationLiveNum100To200(Long durationLiveNum100To200) {
		this.durationLiveNum100To200 = durationLiveNum100To200;
	}

	public Long getDurationLiveNum200To300() {
		return durationLiveNum200To300;
	}

	public void setDurationLiveNum200To300(Long durationLiveNum200To300) {
		this.durationLiveNum200To300 = durationLiveNum200To300;
	}

	public Long getDurationLiveNum300To() {
		return durationLiveNum300To;
	}

	public void setDurationLiveNum300To(Long durationLiveNum300To) {
		this.durationLiveNum300To = durationLiveNum300To;
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

	public Long getNumOfMaxNewRegisterUserNumAboutLive() {
		return numOfMaxNewRegisterUserNumAboutLive;
	}

	public void setNumOfMaxNewRegisterUserNumAboutLive(Long numOfMaxNewRegisterUserNumAboutLive) {
		this.numOfMaxNewRegisterUserNumAboutLive = numOfMaxNewRegisterUserNumAboutLive;
	}

	public Timestamp getTimeOfMaxNewRegisterUserNumAboutLive() {
		return timeOfMaxNewRegisterUserNumAboutLive;
	}

	public void setTimeOfMaxNewRegisterUserNumAboutLive(Timestamp timeOfMaxNewRegisterUserNumAboutLive) {
		this.timeOfMaxNewRegisterUserNumAboutLive = timeOfMaxNewRegisterUserNumAboutLive;
	}

	public String getLiveTitleOfMaxNewRegisterUserNumAboutLive() {
		return liveTitleOfMaxNewRegisterUserNumAboutLive;
	}

	public void setLiveTitleOfMaxNewRegisterUserNumAboutLive(String liveTitleOfMaxNewRegisterUserNumAboutLive) {
		this.liveTitleOfMaxNewRegisterUserNumAboutLive = liveTitleOfMaxNewRegisterUserNumAboutLive;
	}

	public String getEnterpriseNameOfMaxNewRegisterUserNumAboutLive() {
		return enterpriseNameOfMaxNewRegisterUserNumAboutLive;
	}

	public void setEnterpriseNameOfMaxNewRegisterUserNumAboutLive(
			String enterpriseNameOfMaxNewRegisterUserNumAboutLive) {
		this.enterpriseNameOfMaxNewRegisterUserNumAboutLive = enterpriseNameOfMaxNewRegisterUserNumAboutLive;
	}

	public Long getAndroidRegisterUserNum() {
		return androidRegisterUserNum;
	}

	public void setAndroidRegisterUserNum(Long androidRegisterUserNum) {
		this.androidRegisterUserNum = androidRegisterUserNum;
	}

	public Long getIosRegisterUserNum() {
		return iosRegisterUserNum;
	}

	public void setIosRegisterUserNum(Long iosRegisterUserNum) {
		this.iosRegisterUserNum = iosRegisterUserNum;
	}

	public Long getPcRegisterUserNum() {
		return pcRegisterUserNum;
	}

	public void setPcRegisterUserNum(Long pcRegisterUserNum) {
		this.pcRegisterUserNum = pcRegisterUserNum;
	}

	public Long getWapRegisterUserNum() {
		return wapRegisterUserNum;
	}

	public void setWapRegisterUserNum(Long wapRegisterUserNum) {
		this.wapRegisterUserNum = wapRegisterUserNum;
	}

	public Long getOtherRegisterUserNum() {
		return otherRegisterUserNum;
	}

	public void setOtherRegisterUserNum(Long otherRegisterUserNum) {
		this.otherRegisterUserNum = otherRegisterUserNum;
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
