package com.jwzt.statistic.entity.base;

/**
 * @author ysf
 */

@SuppressWarnings("serial")
public class BaseLiveStatisticResult implements java.io.Serializable {

	private Long id;
	private Long userNum;
	private Long viewTotalDuration;
	private Long shareNum;
	private Long likeNum;
	private Long commentNum;
	private Long maxMinute;
	private Long maxMinuteUserNum;
	private Long androidUserNum;
	private Long iosUserNum;
	private Long pcUserNum;
	private Long wapUserNum;
	private Long otherUserNum;

	private Long viewDuraionUserNum0To5;
	private Long viewDuraionUserNum5To10;
	private Long viewDuraionUserNum10To30;
	private Long viewDuraionUserNum30To60;
	private Long viewDuraionUserNum60To;

	private Long newRegisterUserNum;
	private Long oldRegisterUserNum;
	private Long otherRegisterUserNum;

	private Long viewNum;
	private Long signNum;
	private Double redPacketMoney;
	private Long giftNum;

	public BaseLiveStatisticResult() {
		super();
	}

	public BaseLiveStatisticResult(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserNum() {
		return userNum;
	}

	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}

	public Long getViewTotalDuration() {
		return viewTotalDuration;
	}

	public void setViewTotalDuration(Long viewTotalDuration) {
		this.viewTotalDuration = viewTotalDuration;
	}

	public Long getShareNum() {
		return shareNum;
	}

	public void setShareNum(Long shareNum) {
		this.shareNum = shareNum;
	}

	public Long getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(Long likeNum) {
		this.likeNum = likeNum;
	}

	public Long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Long commentNum) {
		this.commentNum = commentNum;
	}

	public Long getMaxMinute() {
		return maxMinute;
	}

	public void setMaxMinute(Long maxMinute) {
		this.maxMinute = maxMinute;
	}

	public Long getMaxMinuteUserNum() {
		return maxMinuteUserNum;
	}

	public void setMaxMinuteUserNum(Long maxMinuteUserNum) {
		this.maxMinuteUserNum = maxMinuteUserNum;
	}

	public Long getAndroidUserNum() {
		return androidUserNum;
	}

	public void setAndroidUserNum(Long androidUserNum) {
		this.androidUserNum = androidUserNum;
	}

	public Long getIosUserNum() {
		return iosUserNum;
	}

	public void setIosUserNum(Long iosUserNum) {
		this.iosUserNum = iosUserNum;
	}

	public Long getPcUserNum() {
		return pcUserNum;
	}

	public void setPcUserNum(Long pcUserNum) {
		this.pcUserNum = pcUserNum;
	}

	public Long getWapUserNum() {
		return wapUserNum;
	}

	public void setWapUserNum(Long wapUserNum) {
		this.wapUserNum = wapUserNum;
	}

	public Long getOtherUserNum() {
		return otherUserNum;
	}

	public void setOtherUserNum(Long otherUserNum) {
		this.otherUserNum = otherUserNum;
	}

	public Long getNewRegisterUserNum() {
		return newRegisterUserNum;
	}

	public void setNewRegisterUserNum(Long newRegisterUserNum) {
		this.newRegisterUserNum = newRegisterUserNum;
	}

	public Long getOldRegisterUserNum() {
		return oldRegisterUserNum;
	}

	public void setOldRegisterUserNum(Long oldRegisterUserNum) {
		this.oldRegisterUserNum = oldRegisterUserNum;
	}

	public Long getOtherRegisterUserNum() {
		return otherRegisterUserNum;
	}

	public void setOtherRegisterUserNum(Long otherRegisterUserNum) {
		this.otherRegisterUserNum = otherRegisterUserNum;
	}

	public Long getViewDuraionUserNum0To5() {
		return viewDuraionUserNum0To5;
	}

	public void setViewDuraionUserNum0To5(Long viewDuraionUserNum0To5) {
		this.viewDuraionUserNum0To5 = viewDuraionUserNum0To5;
	}

	public Long getViewDuraionUserNum5To10() {
		return viewDuraionUserNum5To10;
	}

	public void setViewDuraionUserNum5To10(Long viewDuraionUserNum5To10) {
		this.viewDuraionUserNum5To10 = viewDuraionUserNum5To10;
	}

	public Long getViewDuraionUserNum10To30() {
		return viewDuraionUserNum10To30;
	}

	public void setViewDuraionUserNum10To30(Long viewDuraionUserNum10To30) {
		this.viewDuraionUserNum10To30 = viewDuraionUserNum10To30;
	}

	public Long getViewDuraionUserNum30To60() {
		return viewDuraionUserNum30To60;
	}

	public void setViewDuraionUserNum30To60(Long viewDuraionUserNum30To60) {
		this.viewDuraionUserNum30To60 = viewDuraionUserNum30To60;
	}

	public Long getViewDuraionUserNum60To() {
		return viewDuraionUserNum60To;
	}

	public void setViewDuraionUserNum60To(Long viewDuraionUserNum60To) {
		this.viewDuraionUserNum60To = viewDuraionUserNum60To;
	}

	public Long getViewNum() {
		return viewNum;
	}

	public void setViewNum(Long viewNum) {
		this.viewNum = viewNum;
	}

	public Long getSignNum() {
		return signNum;
	}

	public void setSignNum(Long signNum) {
		this.signNum = signNum;
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

}
