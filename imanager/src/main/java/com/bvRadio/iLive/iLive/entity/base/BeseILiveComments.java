package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 话题品论
 * @author YanXL
 *
 */
public abstract class BeseILiveComments {
	/**
	 * 主键ID
	 */
	private Long commentsId;
	/**
	 * 评论内容
	 */
	private String comments;
	/**
	 * 评论人
	 */
	private String commentsName;
	/**
	 * 评论用户ID
	 */
	private Long userId;
	/**
	 * 评论话题ID
	 */
	private Long msgId;
	/**
	 * 是否删除
	 */
	private Boolean isDelete;
	/**
	 * 是否审核
	 */
	private Boolean isChecked;
	/**
	 * 评论时间
	 */
	private Timestamp createTime;
	public BeseILiveComments() {
		super();
	}
	public Long getCommentsId() {
		return commentsId;
	}
	public void setCommentsId(Long commentsId) {
		this.commentsId = commentsId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCommentsName() {
		return commentsName;
	}
	public void setCommentsName(String commentsName) {
		this.commentsName = commentsName;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	public Boolean getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BeseILiveComments(Long commentsId, String comments,
			String commentsName, Long userId, Long msgId, Boolean isDelete,
			Boolean isChecked, Timestamp createTime) {
		super();
		this.commentsId = commentsId;
		this.comments = comments;
		this.commentsName = commentsName;
		this.userId = userId;
		this.msgId = msgId;
		this.isDelete = isDelete;
		this.isChecked = isChecked;
		this.createTime = createTime;
	}
}
