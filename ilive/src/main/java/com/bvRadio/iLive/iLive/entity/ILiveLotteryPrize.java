package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 活动奖品设置
 * @author Wei
 *
 */
public class ILiveLotteryPrize implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 2200031410065841386L;

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 抽奖活动id
	 */
	private Long lotteryId;
	
	/**
	 * 奖项名称
	 */
	private String prizeTypeName;
	
	/**
	 * 奖品名称
	 */
	private String prizeName; 

	/**
	 * 奖品数量
	 */
	private Integer num;

	/**
	 * 概率
	 */
	private Double probability;

	/**
	 * 奖品图片
	 */
	private String prizeImg;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 中奖白名单
	 */
	private String whiteList;
	
	public String getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(String whiteList) {
		this.whiteList = whiteList;
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

	public String getPrizeTypeName() {
		return prizeTypeName;
	}

	public void setPrizeTypeName(String prizeTypeName) {
		this.prizeTypeName = prizeTypeName;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

	public String getPrizeImg() {
		return prizeImg;
	}

	public void setPrizeImg(String prizeImg) {
		this.prizeImg = prizeImg;
	}
	
}
