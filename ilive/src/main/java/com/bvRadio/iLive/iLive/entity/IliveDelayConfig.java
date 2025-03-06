package com.bvRadio.iLive.iLive.entity;

/**
 * 
 * @author administrator
 * 直播延迟设置
 */
public class IliveDelayConfig {
	
	// 直播类型
	private Integer liveId;
	
	// 延迟类型
	private Integer delayType;
	
	//延迟时长
	private Integer delayTime;

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public Integer getDelayType() {
		return delayType;
	}

	public void setDelayType(Integer delayType) {
		this.delayType = delayType;
	}

	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}
	
	
	
	
	
	

}
