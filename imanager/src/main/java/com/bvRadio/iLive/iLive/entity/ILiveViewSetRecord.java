package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.util.Date;

public class ILiveViewSetRecord {

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 直播间id
	 */
	private Integer roomId;
	
	/**
	 * 观看授权方式 1公开观看 2密码观看 3付费观看 4白名单 5登录观看
	 */
	private Integer viewAuthorized;
	
	/**
	 * 欢迎语
	 */
	private String welcome;
	
	/**
	 * 密码观看时的密码
	 */
	private String viewPassword;
	
	/**
	 * 支付观看时支付金额
	 */
	private Double payAmount;
	
	/**
	 * 设置人
	 */
	private String userName;
	
	/**
	 * 设置时间
	 */
	private Timestamp updateTime;
	
	private String date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getViewAuthorized() {
		return viewAuthorized;
	}

	public void setViewAuthorized(Integer viewAuthorized) {
		this.viewAuthorized = viewAuthorized;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	public String getViewPassword() {
		return viewPassword;
	}

	public void setViewPassword(String viewPassword) {
		this.viewPassword = viewPassword;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}


	public String getDate() {
		return date;
	}


	@Override
	public String toString() {
		return "ILiveViewSetRecord [id=" + id + ", roomId=" + roomId + ", viewAuthorized=" + viewAuthorized
				+ ", welcome=" + welcome + ", viewPassword=" + viewPassword + ", payAmount=" + payAmount + ", userName="
				+ userName + ", updateTime=" + updateTime + "]";
	}

	public void setDate(String format) {
		// TODO Auto-generated method stub
		this.date = format;
	}

}
