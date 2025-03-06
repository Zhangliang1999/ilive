package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

/**
 * 评论上墙活动
 * @author Wei
 *
 */
public abstract  class BaseILiveComment {


	/**
	 * 主键id
	 */
	private String commentId;
	
	
	/**
	 * 创建人
	 */
	private String userName;
	
	/**
	 * 创建时间
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
	 * 是否开启评论活动  1开启  0不开启
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
	 * 签到墙图片
	 */
	private String imgUrl;
	/**
	 * 评论地址
	 */
	private String commentUrl;
	
	
	public String getCommentUrl() {
		return commentUrl;
	}
	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}
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
	
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
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
	public BaseILiveComment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
