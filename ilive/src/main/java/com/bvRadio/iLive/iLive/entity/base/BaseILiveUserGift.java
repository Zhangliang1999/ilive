package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 礼物收取记录
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class BaseILiveUserGift implements Serializable{
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
	 * 赠送时间
	 */
	private Timestamp giveTime;
	/**
	 *礼物ID
	 */
	private Long giftId;
	/**
	 * 礼物名称
	 */
	private String giftName;
	/**
	 * 礼物单价格
	 */
	private String giftPrice;
	/**
	 * 礼物数量
	 */
	private Integer giftNumber;
	/**
	 * 礼物地址
	 */
	private String giftImage;
	
	public Long getGiftId() {
		return giftId;
	}
	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}
	public String getGiftName() {
		return giftName;
	}
	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}
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
	public Timestamp getGiveTime() {
		return giveTime;
	}
	public void setGiveTime(Timestamp giveTime) {
		this.giveTime = giveTime;
	}
	public String getGiftPrice() {
		return giftPrice;
	}
	public void setGiftPrice(String giftPrice) {
		this.giftPrice = giftPrice;
	}
	public Integer getGiftNumber() {
		return giftNumber;
	}
	public void setGiftNumber(Integer giftNumber) {
		this.giftNumber = giftNumber;
	}
	public String getGiftImage() {
		return giftImage;
	}
	public void setGiftImage(String giftImage) {
		this.giftImage = giftImage;
	}
	public BaseILiveUserGift() {
		super();
	}
	
}
