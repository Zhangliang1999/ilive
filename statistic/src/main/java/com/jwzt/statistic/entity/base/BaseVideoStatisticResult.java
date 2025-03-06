package com.jwzt.statistic.entity.base;

import java.sql.Timestamp;

/**
 * @author ysf
 */

@SuppressWarnings("serial")
public class BaseVideoStatisticResult implements java.io.Serializable {

	private String id;
	private Long videoId;
	private Long userTotalNum;
	private Long viewTotalNum;
	private Long shareTotalNum;
	private Long likeTotalNum;
	private Long commentTotalNum;
	private Long androidUserNum;
	private Long iosUserNum;
	private Long pcUserNum;
	private Long wapUserNum;
	private Long otherUserNum;
	
	private Long curDayNum;
	private Long curDayView;

	private Long newRegisterUserNum;
	private Long oldRegisterUserNum;
	private Long otherRegisterUserNum;
	private Timestamp statisticTime;
	private Timestamp createTime;

	public BaseVideoStatisticResult() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public Long getUserTotalNum() {
		return userTotalNum;
	}

	public void setUserTotalNum(Long userTotalNum) {
		this.userTotalNum = userTotalNum;
	}

	public Long getViewTotalNum() {
		return viewTotalNum;
	}

	public void setViewTotalNum(Long viewTotalNum) {
		this.viewTotalNum = viewTotalNum;
	}

	public Long getShareTotalNum() {
		return shareTotalNum;
	}

	public void setShareTotalNum(Long shareTotalNum) {
		this.shareTotalNum = shareTotalNum;
	}

	public Long getLikeTotalNum() {
		return likeTotalNum;
	}

	public void setLikeTotalNum(Long likeTotalNum) {
		this.likeTotalNum = likeTotalNum;
	}

	public Long getCommentTotalNum() {
		return commentTotalNum;
	}

	public void setCommentTotalNum(Long commentTotalNum) {
		this.commentTotalNum = commentTotalNum;
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

	public Long getCurDayNum() {
		return curDayNum;
	}

	public void setCurDayNum(Long curDayNum) {
		this.curDayNum = curDayNum;
	}

	public Long getCurDayView() {
		return curDayView;
	}

	public void setCurDayView(Long curDayView) {
		this.curDayView = curDayView;
	}

}
