package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;



public class ILiveSendMsg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8946654035012790141L;
	private Long id;
	/**
	 * roomid
	 */
	private Long roomId;
	/**
	 * 手机号 
	 */
	private String mobile;
	/**
	 * 备注
	 * @return
	 */
	private String remark;
	/**
	 * 创建时间
	 * @return
	 */
	private Timestamp creatTime;
	/**
	 * 导入人
	 * @return
	 */
	private String loadManager;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Timestamp getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = creatTime;
	}
	public String getLoadManager() {
		return loadManager;
	}
	public void setLoadManager(String loadManager) {
		this.loadManager = loadManager;
	}
	
	
	
}
