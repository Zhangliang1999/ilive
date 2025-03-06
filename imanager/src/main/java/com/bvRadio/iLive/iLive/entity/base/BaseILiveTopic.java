package com.bvRadio.iLive.iLive.entity.base;

/**
 * @author administrator 直播互动话题
 */
public abstract class BaseILiveTopic {

	/**
	 * 话题ID
	 */
	private Long topicId;

	/**
	 * 话题标题
	 */
	private String topicTitle;

	/**
	 * 点赞数
	 */
	private Long praiseNum;

	/**
	 * 评论数
	 */
	private Long commentNum;

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public Long getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Long praiseNum) {
		this.praiseNum = praiseNum;
	}

	public Long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Long commentNum) {
		this.commentNum = commentNum;
	}

}
