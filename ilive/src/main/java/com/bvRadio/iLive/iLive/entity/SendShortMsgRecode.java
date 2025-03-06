package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

/**
 * 用于查询今日发送短信是否达到上限
 * @author zhangliang
 *
 */
public class SendShortMsgRecode {
	private static final long serialVersionUID = 4565708369606512545L;
	private Long id;
	/**
	 * 发送手机号
	 */
	private String mobile;
	/**
	 * 发送时间
	 */
	private Timestamp sendTime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	
	
}
