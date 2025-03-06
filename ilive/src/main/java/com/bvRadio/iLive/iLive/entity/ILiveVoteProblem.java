package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 投票问题
 * @author Wei
 *
 */
public class ILiveVoteProblem implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3778884200560390042L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 投票活动问题id
	 */
	private Long voteId;
	
	/**
	 * 投票问题
	 */
	private String problemName;
	
	/**
	 * 投票样式    1文字    2图片加文字
	 */
	private Integer style;
	
	/**
	 * 最大投票个数
	 */
	private Integer maxVoteNum;
	
	private List<ILiveVoteOption> listOption;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;

	public List<ILiveVoteOption> getListOption() {
		return listOption;
	}

	public void setListOption(List<ILiveVoteOption> listOption) {
		this.listOption = listOption;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVoteId() {
		return voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public Integer getMaxVoteNum() {
		return maxVoteNum;
	}

	public void setMaxVoteNum(Integer maxVoteNum) {
		this.maxVoteNum = maxVoteNum;
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
	
}
