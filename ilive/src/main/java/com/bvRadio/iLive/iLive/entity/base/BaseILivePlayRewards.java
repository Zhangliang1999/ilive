package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
/**
 * 打赏
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class BaseILivePlayRewards implements Serializable{
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	/**
	 * 打赏功能开关 0 关闭  1 开启
	 */
	private Integer rewardsSwitch;
	/**
	 * 打赏宣传标题 类型  0 宣传语    1 宣传图
	 */
	private Integer rewardsTitleType;
	/**
	 * 宣传语
	 */
	private String promotionalLanguage;
	/**
	 * 宣传图
	 */
	private String promotionalImage;
	/**
	 * 最少打赏金额
	 */
	private String minRewardsAmount;
	/**
	 * 最大打赏金额
	 */
	private String maxRewardsAmount;
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Integer getRewardsSwitch() {
		return rewardsSwitch;
	}
	public void setRewardsSwitch(Integer rewardsSwitch) {
		this.rewardsSwitch = rewardsSwitch;
	}
	public Integer getRewardsTitleType() {
		return rewardsTitleType;
	}
	public void setRewardsTitleType(Integer rewardsTitleType) {
		this.rewardsTitleType = rewardsTitleType;
	}
	public String getMinRewardsAmount() {
		return minRewardsAmount;
	}
	public void setMinRewardsAmount(String minRewardsAmount) {
		this.minRewardsAmount = minRewardsAmount;
	}
	public String getMaxRewardsAmount() {
		return maxRewardsAmount;
	}
	public void setMaxRewardsAmount(String maxRewardsAmount) {
		this.maxRewardsAmount = maxRewardsAmount;
	}
	public String getPromotionalLanguage() {
		return promotionalLanguage;
	}
	public void setPromotionalLanguage(String promotionalLanguage) {
		this.promotionalLanguage = promotionalLanguage;
	}
	public String getPromotionalImage() {
		return promotionalImage;
	}
	public void setPromotionalImage(String promotionalImage) {
		this.promotionalImage = promotionalImage;
	}
	public BaseILivePlayRewards() {
		super();
	}
	
}
