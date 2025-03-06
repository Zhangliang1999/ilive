package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseBBSVoteActivity implements Serializable {

	/**
	 * 主键
	 */
	private Long voteId;

	/**
	 * 投票类型
	 */
	private Integer voteType;

	/**
	 * 投票
	 */
	private Integer voteMore;

	/**
	 * 运行最大选项
	 */
	private Integer maxOption;

	/**
	 * 投票开始时间
	 */
	private Timestamp voteStartTime;

	/**
	 * 投票结束时间
	 */
	private Timestamp voteEndTime;

	/**
	 * 投票创建时间
	 */
	private Timestamp createTime;

	private Integer creditspolicy;

	/***
	 * 是否关闭评论
	 */
	private Integer closeComment;

	/**
	 * 是否开启终端限制
	 */
	private Integer enableTerminalRestrictions;

	/**
	 * 是否运行观看结果
	 */
	private Integer allowSeeResult;

	/**
	 * 是否启用IP限制
	 */
	private Integer enableIPControl;

	/**
	 * 背景描述
	 */
	private String backgroundDescription;

	/**
	 * 规则描述
	 */
	private String ruleDescription;

	/**
	 * 投票标题
	 */
	private String voteTitle;

	/**
	 * 投票简介
	 */
	private String voteDescription;

	/**
	 * 投票优先级
	 */
	private Integer priority;

	/**
	 * 允许登录
	 */
	private Integer allowNotLogin;

	/**
	 * 每日次数
	 */
	private Integer everydayCount;
	
	/**
	 * 删除标记  1表示删除  0表示未删除
	 */
	private Integer delFlag;

	
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public BaseBBSVoteActivity() {

	}

	public BaseBBSVoteActivity(Long voteId) {
		this.voteId = voteId;
	}

	public Long getVoteId() {
		return voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	public Integer getVoteType() {
		return voteType;
	}

	public void setVoteType(Integer voteType) {
		this.voteType = voteType;
	}

	public Integer getVoteMore() {
		return voteMore;
	}

	public void setVoteMore(Integer voteMore) {
		this.voteMore = voteMore;
	}

	public Integer getMaxOption() {
		return maxOption;
	}

	public void setMaxOption(Integer maxOption) {
		this.maxOption = maxOption;
	}

	public Timestamp getVoteStartTime() {
		return voteStartTime;
	}

	public void setVoteStartTime(Timestamp voteStartTime) {
		this.voteStartTime = voteStartTime;
	}

	public Timestamp getVoteEndTime() {
		return voteEndTime;
	}

	public void setVoteEndTime(Timestamp voteEndTime) {
		this.voteEndTime = voteEndTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getCreditspolicy() {
		return creditspolicy;
	}

	public void setCreditspolicy(Integer creditspolicy) {
		this.creditspolicy = creditspolicy;
	}

	public Integer getCloseComment() {
		return closeComment;
	}

	public void setCloseComment(Integer closeComment) {
		this.closeComment = closeComment;
	}

	public Integer getEnableTerminalRestrictions() {
		return enableTerminalRestrictions;
	}

	public void setEnableTerminalRestrictions(Integer enableTerminalRestrictions) {
		this.enableTerminalRestrictions = enableTerminalRestrictions;
	}

	public Integer getAllowSeeResult() {
		return allowSeeResult;
	}

	public void setAllowSeeResult(Integer allowSeeResult) {
		this.allowSeeResult = allowSeeResult;
	}

	public Integer getEnableIPControl() {
		return enableIPControl;
	}

	public void setEnableIPControl(Integer enableIPControl) {
		this.enableIPControl = enableIPControl;
	}

	public String getBackgroundDescription() {
		return backgroundDescription;
	}

	public void setBackgroundDescription(String backgroundDescription) {
		this.backgroundDescription = backgroundDescription;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public String getVoteTitle() {
		return voteTitle;
	}

	public void setVoteTitle(String voteTitle) {
		this.voteTitle = voteTitle;
	}

	public String getVoteDescription() {
		return voteDescription;
	}

	public void setVoteDescription(String voteDescription) {
		this.voteDescription = voteDescription;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getAllowNotLogin() {
		return allowNotLogin;
	}

	public void setAllowNotLogin(Integer allowNotLogin) {
		this.allowNotLogin = allowNotLogin;
	}

	public Integer getEverydayCount() {
		return everydayCount;
	}

	public void setEverydayCount(Integer everydayCount) {
		this.everydayCount = everydayCount;
	}

}
