package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 点赞记录
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public  abstract class BaseILiveMessagePraise implements java.io.Serializable{
	/**
	 * 话题ID
	 */
	private Long msgId;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 点赞时间
	 */
	private Timestamp createTime;
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
