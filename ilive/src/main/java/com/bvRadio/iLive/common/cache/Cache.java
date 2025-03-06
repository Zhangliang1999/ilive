package com.bvRadio.iLive.common.cache;

import java.util.Date;

/**
 * 缓存类
 * 
 * @author JWZT-YSF
 * 
 */
public class Cache {
	private String key;// 缓存ID
	private Object value;// 缓存数据
	private long timeOut;// 更新时间
	private boolean expired; // 是否终止
	private long putTime; //发送时间

	public Cache() {
		super();
		this.putTime = new Date().getTime();
	}

	public Cache(String key, Object value, long timeOut, boolean expired) {
		this.key = key;
		this.value = value;
		this.timeOut = timeOut;
		this.expired = expired;
		this.putTime = new Date().getTime();
	}

	public String getKey() {
		return key;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(String string) {
		key = string;
	}

	public void setTimeOut(long l) {
		timeOut = l;
	}

	public void setValue(Object object) {
		value = object;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean b) {
		expired = b;
	}

	public long getPutTime() {
		return putTime;
	}

	public void setPutTime(long putTime) {
		this.putTime = putTime;
	}
	
	/**
	 * 1分钟后允许重新发送
	 * @return
	 */
	public boolean canRePub(){
		return new Date().getTime() > this.putTime+60*1000;
	}
}
