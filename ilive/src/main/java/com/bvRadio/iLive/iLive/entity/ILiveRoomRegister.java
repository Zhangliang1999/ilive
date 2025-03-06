package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class ILiveRoomRegister implements Serializable{
	
	/**
	 * 序列化标识
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 用户昵称	不保存数据库
	 */
	private String userNickName;
	
	/**
	 * 用户姓名	不保存数据库
	 */
	private String userName;
	
	/**
	 * 直播间ID		点赞行为
	 */
	private Integer roomId;
	
	/**
	 * 直播场次id		签到行为
	 */
	private Long liveEventId;
	
	/**
	 * 行为状态  0签到   1点赞
	 */
	private Integer state;
	
	/**
	 * 点赞时间
	 */
	private Timestamp createTime;
	
	/**
	 * 删除记录  1为删除  0或null为未删除
	 */
	private Integer isDel;
	
	/**
	 * 标识   0为直播间  1为回看
	 */
	private Integer mark;
	
	/**
	 * 媒资文件ID
	 */
	private Long fileId;
	
	private ILiveManager iLiveManager;
	
	
	public ILiveManager getiLiveManager() {
		return iLiveManager;
	}

	public void setiLiveManager(ILiveManager iLiveManager) {
		this.iLiveManager = iLiveManager;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	
}
