package com.bvRadio.iLive.iLive.entity.base;

import java.io.Serializable;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.BBSVoteLog;

/**
 * This is an object that contains data related to the bbs_vote_item table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="bbs_vote_item"
 */

@SuppressWarnings("serial")
public abstract class BaseBBSVoteItem implements Serializable {

	/**
	 * 条目ID
	 */
	private Integer itemId;

	/**
	 * 条目标题
	 */
	private String title;

	/**
	 * 投票次数
	 */
	private Integer voteCount;

	/**
	 * 投票优先级
	 */
	private Integer priority;

	/**
	 * 媒体地址
	 */
	private String mediaPath;

	/**
	 * 头像地址
	 */
	private String photo;

	// 暂不可知
	private Integer optionNumber;

	// 暂不可知
	private String voteActivitySynopsis;

	// 暂不可知
	private String voteActivityDetail;

	// 暂不可知
	private Integer customNumber;

	/**
	 * 投票结果
	 */
	private Set<BBSVoteLog> bbsVoteLogs;

	public BaseBBSVoteItem() {

	}

	public BaseBBSVoteItem(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getOptionNumber() {
		return optionNumber;
	}

	public void setOptionNumber(Integer optionNumber) {
		this.optionNumber = optionNumber;
	}

	public String getVoteActivitySynopsis() {
		return voteActivitySynopsis;
	}

	public void setVoteActivitySynopsis(String voteActivitySynopsis) {
		this.voteActivitySynopsis = voteActivitySynopsis;
	}

	public String getVoteActivityDetail() {
		return voteActivityDetail;
	}

	public void setVoteActivityDetail(String voteActivityDetail) {
		this.voteActivityDetail = voteActivityDetail;
	}

	public Integer getCustomNumber() {
		return customNumber;
	}

	public void setCustomNumber(Integer customNumber) {
		this.customNumber = customNumber;
	}

	public Set<BBSVoteLog> getBbsVoteLogs() {
		return bbsVoteLogs;
	}

	public void setBbsVoteLogs(Set<BBSVoteLog> bbsVoteLogs) {
		this.bbsVoteLogs = bbsVoteLogs;
	}

}