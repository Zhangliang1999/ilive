package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 用户打赏记录
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class BaseILiveUserPlayRewards implements Serializable{
	
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	/**
	 * 场次ID
	 */
	private Long evenId;
	/**
	 * 直播标题
	 */
	private String roomTitle;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 打赏时间
	 */
	private Timestamp playRewardsTime;
	/**
	 * 打赏金额
	 */
	private String playRewardsAmount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Long getEvenId() {
		return evenId;
	}
	public void setEvenId(Long evenId) {
		this.evenId = evenId;
	}
	public String getRoomTitle() {
		return roomTitle;
	}
	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Timestamp getPlayRewardsTime() {
		return playRewardsTime;
	}
	public void setPlayRewardsTime(Timestamp playRewardsTime) {
		this.playRewardsTime = playRewardsTime;
	}
	public String getPlayRewardsAmount() {
		return playRewardsAmount;
	}
	public void setPlayRewardsAmount(String playRewardsAmount) {
		this.playRewardsAmount = playRewardsAmount;
	}
	public BaseILiveUserPlayRewards() {
		super();
	}
	
}
