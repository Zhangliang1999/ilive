package com.bvRadio.iLive.iLive.entity.vo;

import java.sql.Timestamp;
import java.util.List;

import com.bvRadio.iLive.iLive.entity.BBSInteractResource;
import com.bvRadio.iLive.iLive.entity.ILiveUserEntity;

public class ILiveComment {

	private Integer id;
	private ILiveUserEntity commentUser;
	private Integer flagId;
	private Integer flagType;
	private String commentOrginContent;
	private String commentContent;
	private Integer duration;
	private Integer commentType;
	private Timestamp createTime;
	private Integer ups;
	private Integer downs;
	private boolean isRecommend;
	private boolean isChecked;
	private boolean isTop;
	private boolean deleted;
	private Timestamp topTime;
	private String sta;
	private String flagName;
	private Integer width;
	private Integer height;
	private List<BBSInteractResource> interactResourceList;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ILiveUserEntity getCommentUser() {
		return commentUser;
	}
	public void setCommentUser(ILiveUserEntity commentUser) {
		this.commentUser = commentUser;
	}
	public Integer getFlagId() {
		return flagId;
	}
	public void setFlagId(Integer flagId) {
		this.flagId = flagId;
	}
	public Integer getFlagType() {
		return flagType;
	}
	public void setFlagType(Integer flagType) {
		this.flagType = flagType;
	}
	public String getCommentOrginContent() {
		return commentOrginContent;
	}
	public void setCommentOrginContent(String commentOrginContent) {
		this.commentOrginContent = commentOrginContent;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getCommentType() {
		return commentType;
	}
	public void setCommentType(Integer commentType) {
		this.commentType = commentType;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Integer getUps() {
		return ups;
	}
	public void setUps(Integer ups) {
		this.ups = ups;
	}
	public Integer getDowns() {
		return downs;
	}
	public void setDowns(Integer downs) {
		this.downs = downs;
	}
	public boolean isRecommend() {
		return isRecommend;
	}
	public void setRecommend(boolean isRecommend) {
		this.isRecommend = isRecommend;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public boolean isTop() {
		return isTop;
	}
	public void setTop(boolean isTop) {
		this.isTop = isTop;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Timestamp getTopTime() {
		return topTime;
	}
	public void setTopTime(Timestamp topTime) {
		this.topTime = topTime;
	}
	public String getSta() {
		return sta;
	}
	public void setSta(String sta) {
		this.sta = sta;
	}
	public String getFlagName() {
		return flagName;
	}
	public void setFlagName(String flagName) {
		this.flagName = flagName;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public List<BBSInteractResource> getInteractResourceList() {
		return interactResourceList;
	}
	public void setInteractResourceList(
			List<BBSInteractResource> interactResourceList) {
		this.interactResourceList = interactResourceList;
	}
	public ILiveComment(Integer id, ILiveUserEntity commentUser,
			Integer flagId, Integer flagType, String commentOrginContent,
			String commentContent, Integer duration, Integer commentType,
			Timestamp createTime, Integer ups, Integer downs,
			boolean isRecommend, boolean isChecked, boolean isTop,
			boolean deleted, Timestamp topTime, String sta, String flagName,
			Integer width, Integer height,
			List<BBSInteractResource> interactResourceList) {
		super();
		this.id = id;
		this.commentUser = commentUser;
		this.flagId = flagId;
		this.flagType = flagType;
		this.commentOrginContent = commentOrginContent;
		this.commentContent = commentContent;
		this.duration = duration;
		this.commentType = commentType;
		this.createTime = createTime;
		this.ups = ups;
		this.downs = downs;
		this.isRecommend = isRecommend;
		this.isChecked = isChecked;
		this.isTop = isTop;
		this.deleted = deleted;
		this.topTime = topTime;
		this.sta = sta;
		this.flagName = flagName;
		this.width = width;
		this.height = height;
		this.interactResourceList = interactResourceList;
	}
	public ILiveComment() {
		super();
	}
	
	
}
