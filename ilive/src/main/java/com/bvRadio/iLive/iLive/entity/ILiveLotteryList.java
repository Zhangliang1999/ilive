package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 抽奖白名单、黑名单
 * @author Wei
 *
 */
public class ILiveLotteryList implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5040883475024024090L;
	
	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 抽奖活动id
	 */
	private Long lotteryId;

	/**
	 * 名单类型    1白名单   2黑名单
	 */
	private Integer listType;
	
	/**
	 * 联系方式
	 */
	private String phone;

	/**
	 * 奖品id
	 */
	private Long lotteryPrizeId;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	public Long getLotteryPrizeId() {
		return lotteryPrizeId;
	}

	public void setLotteryPrizeId(Long lotteryPrizeId) {
		this.lotteryPrizeId = lotteryPrizeId;
	}

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

	public Integer getListType() {
		return listType;
	}

	public void setListType(Integer listType) {
		this.listType = listType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
