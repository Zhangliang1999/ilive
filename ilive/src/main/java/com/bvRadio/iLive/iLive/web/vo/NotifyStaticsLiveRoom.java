package com.bvRadio.iLive.iLive.web.vo;

import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;

/**
 * 开始直播时通知统计系统直播间信息
 * @author administrator
 */
public class NotifyStaticsLiveRoom {

	/**
	 * 直播间ID
	 */
	private Integer roomId;

	/**
	 * 直播场次ID
	 */
	private Long liveEventId;

	/**
	 * 直播标题
	 */
	private String liveTitle;

	/**
	 * 封面图
	 */
	private String coverAddr;

	/**
	 * 直播开始时间
	 */
	private Long liveBeginTime;

	/**
	 * 企业信息
	 */
	private ILiveAppEnterprise enterprise;
	
	/**
	 * 是否打开数据美化
	 * @return
	 */
	private Integer openDecorate;
	
	/**
	 * 美化基数
	 * @return
	 */
	private Long baseNum;
	
	/**
	 * 美化倍数
	 * @return
	 */
	private Double multipleNum;
	

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public String getCoverAddr() {
		return coverAddr;
	}

	public void setCoverAddr(String coverAddr) {
		this.coverAddr = coverAddr;
	}

	public ILiveAppEnterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(ILiveAppEnterprise enterprise) {
		this.enterprise = enterprise;
	}


	public Integer getOpenDecorate() {
		return openDecorate;
	}

	public void setOpenDecorate(Integer openDecorate) {
		this.openDecorate = openDecorate;
	}

	public Long getBaseNum() {
		return baseNum;
	}

	public void setBaseNum(Long baseNum) {
		this.baseNum = baseNum;
	}

	public Double getMultipleNum() {
		return multipleNum;
	}

	public void setMultipleNum(Double multipleNum) {
		this.multipleNum = multipleNum;
	}

	public Long getLiveBeginTime() {
		return liveBeginTime;
	}

	public void setLiveBeginTime(Long liveBeginTime) {
		this.liveBeginTime = liveBeginTime;
	}
	
	

}
