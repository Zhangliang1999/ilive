package com.bvRadio.iLive.iLive.util;

public class FaceVo{
	/**
	 * 标示
	 */
	private String key;
	/**
	 * 地址
	 */
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public FaceVo() {
		super();
	}
	public FaceVo(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
}
