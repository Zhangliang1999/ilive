package com.bvRadio.iLive.iLive.entity;

import java.util.Date;
import java.util.List;

public class RedRecordingVo {
	/**
	 * 主键ID
	 */
	private Integer redId;
	/**
	 * 红包类型！0 发红包   、  1 收红包
	 */
	private Integer redType;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 用户名称
	 */
	private String userNameId;
	/**
	 * 用户头像
	 */
	private String  userImg;
	/**
	 * 发送红包金额
	 */
	private long redAmount;
	/**
	 * 红包个数
	 */
	private Integer redNumber;
	/**
	 *  时间
	 */
	private Date createTime;
	/**
	 * 抢
	 */
	private List<RedRecordingBean> beans;

	public Integer getRedId() {
		return redId;
	}

	public void setRedId(Integer redId) {
		this.redId = redId;
	}

	public Integer getRedType() {
		return redType;
	}

	public void setRedType(Integer redType) {
		this.redType = redType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserNameId() {
		return userNameId;
	}

	public void setUserNameId(String userNameId) {
		this.userNameId = userNameId;
	}

	public long getRedAmount() {
		return redAmount;
	}

	public void setRedAmount(long redAmount) {
		this.redAmount = redAmount;
	}

	public Integer getRedNumber() {
		return redNumber;
	}

	public void setRedNumber(Integer redNumber) {
		this.redNumber = redNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<RedRecordingBean> getBeans() {
		return beans;
	}

	public void setBeans(List<RedRecordingBean> beans) {
		this.beans = beans;
	}

	public RedRecordingVo() {
		super();
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	
}
