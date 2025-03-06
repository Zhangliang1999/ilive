package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 抽奖活动内容
 * @author Wei
 *
 */
public class ILiveLottery implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 7019371432364943145L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 抽奖名称
	 */
	private String name;
	
	/**
	 * 抽奖类型  1砸金蛋  2摇一摇   3九宫格
	 */
	private Integer lotteryType;
	
	/**
	 * 同一个人是否可以重复中奖  0不能   1能
	 */
	private Integer isRepeat;
	
	/**
	 * 同一个人可以抽奖多少次
	 */
	private Integer lotteryNum;
	
	/**
	 * 抽奖规则   手机验证    0未选中   1选中  
	 */
	private Integer isLotteryRulePhone;
	
	/**
	 * 抽奖规则   分享完成次数加一，最多增加一次    0未选中   1选中  
	 */
	private Integer isLotteryRuleShare;
	
	/**
	 * 抽奖开始时间
	 */
	private Timestamp startTime;
	
	/**
	 * 抽奖结束时间
	 */
	private Timestamp endTime;
	
	/**
	 * 抽奖创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 抽奖更新时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 房间ID
	 */
	private Integer roomId;
	
	/**
	 * 是否开启该活动  0未开启   1开启
	 */
	private Integer isSwitch = 0;
	
	/**
	 * 是否结束该活动   0未结束   1结束
	 */
	private Integer isEnd;
	
	/**
	 * 参与人数
	 */
	private Integer number;
	
	/**
	 * 曾经是否开启过该活动   0没有    1有    开启过以后不能再修改抽奖活动
	 */
	private Integer isOpen;
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 奖品列表
	 */
	private List<ILiveLotteryPrize> list;

	public List<ILiveLotteryPrize> getList() {
		return list;
	}

	public void setList(List<ILiveLotteryPrize> list) {
		this.list = list;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(Integer lotteryType) {
		this.lotteryType = lotteryType;
	}

	public Integer getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(Integer isRepeat) {
		this.isRepeat = isRepeat;
	}

	public Integer getLotteryNum() {
		return lotteryNum;
	}

	public void setLotteryNum(Integer lotteryNum) {
		this.lotteryNum = lotteryNum;
	}

	public Integer getIsLotteryRulePhone() {
		return isLotteryRulePhone;
	}

	public void setIsLotteryRulePhone(Integer isLotteryRulePhone) {
		this.isLotteryRulePhone = isLotteryRulePhone;
	}

	public Integer getIsLotteryRuleShare() {
		return isLotteryRuleShare;
	}

	public void setIsLotteryRuleShare(Integer isLotteryRuleShare) {
		this.isLotteryRuleShare = isLotteryRuleShare;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getIsSwitch() {
		return isSwitch;
	}

	public void setIsSwitch(Integer isSwitch) {
		this.isSwitch = isSwitch;
	}

	public Integer getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ILiveLottery [id=" + id + ", name=" + name + ", lotteryType=" + lotteryType + ", isRepeat=" + isRepeat
				+ ", lotteryNum=" + lotteryNum + ", isLotteryRulePhone=" + isLotteryRulePhone + ", isLotteryRuleShare="
				+ isLotteryRuleShare + ", startTime=" + startTime + ", endTime=" + endTime + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", roomId=" + roomId + ", isSwitch=" + isSwitch
				+ ", isEnd=" + isEnd + ", number=" + number + "]";
	}
	
}
