package com.jwzt.statistic.entity.base;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.IpAddress;

public class BaseLoginRecord {

	private Long id;
	
	private Long userId;
	
	private String ip;
	
	private Long ipCode;
	
	private Timestamp createTime;
	
	private IpAddress ipAddress;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getIpCode() {
		return ipCode;
	}

	public void setIpCode(Long ipCode) {
		this.ipCode = ipCode;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public IpAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BaseLoginRecord [id=" + id + ", userId=" + userId + ", ip=" + ip + ", ipCode=" + ipCode
				+ ", createTime=" + createTime + ", ipAddress=" + ipAddress + "]";
	}
	
}
