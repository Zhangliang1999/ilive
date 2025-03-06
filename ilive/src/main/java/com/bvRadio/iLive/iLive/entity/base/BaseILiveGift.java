package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
/**
 * 礼物管理
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public abstract class BaseILiveGift implements Serializable{
	/**
	 * 主键
	 */
	private Long giftId;
	/**
	 * 礼物名称
	 */
	private String giftName;
	/**
	 * 礼物图片地址
	 */
	private String giftImage;
	/**
	 * 礼物单价
	 */
	private String giftPrice;
	/**
	 * 礼物类型  0 系统礼物   1 个人礼物
	 */
	private Integer giftType;
	/**
	 * 礼物所属用户 0 系统   其他  个人
	 */
	private Long userId;
	/**
	 * 直播间ID 0 系统礼物   其他 个人直播间礼物
	 */
	private Integer roomId;
	
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
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
	public String getGiftImage() {
		return giftImage;
	}
	public void setGiftImage(String giftImage) {
		this.giftImage = giftImage;
	}
	public String getGiftPrice() {
		return giftPrice;
	}
	public void setGiftPrice(String giftPrice) {
		this.giftPrice = giftPrice;
	}
	public Integer getGiftType() {
		return giftType;
	}
	public void setGiftType(Integer giftType) {
		this.giftType = giftType;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BaseILiveGift() {
		super();
	}
	
}
