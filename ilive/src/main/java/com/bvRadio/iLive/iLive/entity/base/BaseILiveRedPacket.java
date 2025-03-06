package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.Date;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

/**
 * 红包
 * @author Wei
 *
 */
public abstract  class BaseILiveRedPacket {


	/**
	 * 主键id
	 */
	private Long packetId;
	
	/**
	 * 活动名称
	 */
	private String name;
	
	/**
	 * 创建人
	 */
	private String userName;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	
	/**
	 * 是否开启红包  1开启  0不开启
	 */
	private Integer isAllow;
	
	/**
	 * 上传人userId
	 */
	private Long userId;
	
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 红包口令
	 */
	private String command;
	
	private ILiveLiveRoom room;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getPacketId() {
		return packetId;
	}


	public void setPacketId(Long packetId) {
		this.packetId = packetId;
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


	public Integer getIsAllow() {
		return isAllow;
	}


	public void setIsAllow(Integer isAllow) {
		this.isAllow = isAllow;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public ILiveLiveRoom getRoom() {
		return room;
	}


	public void setRoom(ILiveLiveRoom room) {
		this.room = room;
	}


	public BaseILiveRedPacket() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
