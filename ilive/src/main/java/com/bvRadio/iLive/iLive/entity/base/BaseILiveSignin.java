package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.Date;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

/**
 * 签到活动
 * @author Wei
 *
 */
public abstract  class BaseILiveSignin {


	/**
	 * 主键id
	 */
	private Long signId;
	
	/**
	 * 签到活动名称
	 */
	private String name;
	
	
	
	/**
	 * 创建人
	 */
	private String userName;
	
	/**
	 * 上传时间
	 */
	private Timestamp createTime;
	
	
	
	/**
	 * 更新人
	 */
	private String updateUserName;
	
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 是否开启签到活动  1开启  0不开启
	 */
	private Integer isAllow;
	
	/**
	 * 上传人userId
	 */
	private Long userId;
	
	/**
	 * 是否开启签到墙功能 1开启  0不开启
	 */
	private Integer state;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 签到墙图片
	 */
	private String imgUrl;
	
	private Integer enterpriseId;
	
	public String getEwImg() {
		return ewImg;
	}
	public void setEwImg(String ewImg) {
		this.ewImg = ewImg;
	}
	/**
	 * 直播房间
	 */
	private ILiveLiveRoom room;
	/**
	 * 签到二维码
	 */
	private String ewImg;
	
	
	public ILiveLiveRoom getRoom() {
		return room;
	}
	public void setRoom(ILiveLiveRoom room) {
		this.room = room;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Long getSignId() {
		return signId;
	}
	public void setSignId(Long signId) {
		this.signId = signId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
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
	public BaseILiveSignin() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	
	
}
