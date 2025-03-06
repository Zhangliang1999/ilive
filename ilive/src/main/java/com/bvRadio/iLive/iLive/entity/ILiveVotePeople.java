package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class ILiveVotePeople implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 2433380508659767358L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 投票活动id
	 */
	private Long voteId;
	
	/**
	 * 投票问题id
	 */
	private Long voteProblemId;
	
	/**
	 * 投票选项id
	 */
	private Long voteOptionId;
	
	/**
	 * userId
	 */
	private Long userId;
	
	/**
	 * 手机号
	 */
	private String mobile;

	private Timestamp createTime;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getVoteId() {
		return voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	public Long getVoteProblemId() {
		return voteProblemId;
	}

	public void setVoteProblemId(Long voteProblemId) {
		this.voteProblemId = voteProblemId;
	}

	public Long getVoteOptionId() {
		return voteOptionId;
	}

	public void setVoteOptionId(Long voteOptionId) {
		this.voteOptionId = voteOptionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
