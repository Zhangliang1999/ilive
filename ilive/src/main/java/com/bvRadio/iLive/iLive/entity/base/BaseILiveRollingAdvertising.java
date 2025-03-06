package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
/**
 * 直播间滚动广告
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public abstract class BaseILiveRollingAdvertising implements Serializable{
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	/**
	 * 广告内容
	 */
	private String content;
	/**
	 * 是否启动 0 关闭   1 启动
	 */
	private Integer startType;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStartType() {
		return startType;
	}
	public void setStartType(Integer startType) {
		this.startType = startType;
	}
	public BaseILiveRollingAdvertising() {
		super();
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public BaseILiveRollingAdvertising(Integer roomId, String content,
			Integer startType) {
		super();
		this.roomId = roomId;
		this.content = content;
		this.startType = startType;
	}
}
