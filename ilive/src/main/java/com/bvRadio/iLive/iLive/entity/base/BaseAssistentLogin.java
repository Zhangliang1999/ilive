package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 协助助手登录记录
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class BaseAssistentLogin implements Serializable{
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 登录时间
	 */
	private Timestamp loginTime;
	/**
	 * 登录地址IP
	 */
	private String ip;
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	/**
	 * 登录使用邀请码
	 */
	private String invitationCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public BaseAssistentLogin() {
		super();
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
}
