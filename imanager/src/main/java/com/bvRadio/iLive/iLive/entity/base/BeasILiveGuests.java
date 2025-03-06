package com.bvRadio.iLive.iLive.entity.base;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

/**
 * 直播间嘉宾
 * @author YanXL
 *
 */
public abstract class BeasILiveGuests implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 嘉宾ID
	 */
	private Long guestsId;
	/**
	 * 嘉宾名称
	 */
	private String guestsName;
	/**
	 * 直播间
	 */
	private ILiveLiveRoom iLiveLiveRoom;
	/**
	 * 嘉宾头像
	 */
	private String guestsImage;
	/**
	 * 是否删除
	 */
	private Boolean isDelete;
	public Long getGuestsId() {
		return guestsId;
	}
	public void setGuestsId(Long guestsId) {
		this.guestsId = guestsId;
	}
	public String getGuestsName() {
		return guestsName;
	}
	public void setGuestsName(String guestsName) {
		this.guestsName = guestsName;
	}
	public ILiveLiveRoom getiLiveLiveRoom() {
		return iLiveLiveRoom;
	}
	public void setiLiveLiveRoom(ILiveLiveRoom iLiveLiveRoom) {
		this.iLiveLiveRoom = iLiveLiveRoom;
	}
	public String getGuestsImage() {
		return guestsImage;
	}
	public void setGuestsImage(String guestsImage) {
		this.guestsImage = guestsImage;
	}
	public BeasILiveGuests() {
		super();
	}
	
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	public BeasILiveGuests(Long guestsId, String guestsName,
			ILiveLiveRoom iLiveLiveRoom, String guestsImage, Boolean isDelete) {
		super();
		this.guestsId = guestsId;
		this.guestsName = guestsName;
		this.iLiveLiveRoom = iLiveLiveRoom;
		this.guestsImage = guestsImage;
		this.isDelete = isDelete;
	}
	
}
