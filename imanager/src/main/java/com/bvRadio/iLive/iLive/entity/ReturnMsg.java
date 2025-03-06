package com.bvRadio.iLive.iLive.entity;


/**
 * 接口响应结果
 * @author YanXL
 *
 */
public class ReturnMsg {
	/**
	 * 结果标示
	 */
	private String msg;
	/**
	 * 结果原因
	 */
	private String value;
	/**
	 * 信息
	 */
	private Object object;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ReturnMsg() {
		super();
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
}
