package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 保存分享的记录
 * @author Wei
 *
 */
public class ILiveLotteryShare implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8027638829947151801L;
	
	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 抽奖活动id
	 */
	private Long lotteryId;
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 用户联系方式
	 */
	private String phone;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(Long lotteryId) {
		this.lotteryId = lotteryId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
