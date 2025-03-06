package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 禁言实体
 * 
 * @author YanXL
 * 
 */
public abstract class BaseILiveEstoppel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户IP
	 * 
	 * @return
	 */
	private String userIp;

	/**
	 * 禁言时间
	 * 
	 * @return
	 */
	private Timestamp createTime;

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BaseILiveEstoppel() {
		super();
	}

	public BaseILiveEstoppel(Integer roomId, Long userId) {
		super();
		this.roomId = roomId;
		this.userId = userId;
	}

	public BaseILiveEstoppel(Long userId, String userIp, Timestamp createTime) {
		super();
		this.userIp = userIp;
		this.userId = userId;
		this.createTime = createTime;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}