package com.bvRadio.iLive.iLive.entity.base;

import java.util.Date;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveSignin;

/**
 * 签到人员
 * @author Wei
 *
 */
public abstract class BaseILiveSigninUser {


	/**
	 * 主键id
	 */
	private String signUserId;
	
	
	/**
	 * 签到人
	 */
	private Long userId;
	/**
	 * 签到人手机
	 */
	private String userPhone;
	/**
	 * 签到人昵称
	 */
	private String userName;
	/**
	 * 签到人照片
	 */
	private String userPhoto;
	
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 开始时间
	 */
	private Date createTime;
	
	/**
	 * 直播房间
	 */
	private ILiveLiveRoom room;
	
	/**
	 * 签到活动id
	 */
	private ILiveSignin sign;
	
    
    

	public ILiveSignin getSign() {
		return sign;
	}



	public void setSign(ILiveSignin sign) {
		this.sign = sign;
	}



	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public String getUserPhone() {
		return userPhone;
	}



	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getUserPhoto() {
		return userPhoto;
	}



	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}



	public String getSignUserId() {
		return signUserId;
	}



	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}






	public Integer getStatus() {
		return status;
	}



	public void setStatus(Integer status) {
		this.status = status;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public ILiveLiveRoom getRoom() {
		return room;
	}



	public void setRoom(ILiveLiveRoom room) {
		this.room = room;
	}



	public BaseILiveSigninUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
