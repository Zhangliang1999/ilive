package com.bvRadio.iLive.manager.entity;

import java.sql.Timestamp;
import java.util.List;

import com.google.inject.spi.PrivateElements;

/**
 * 站内信
 * @author Wei
 *
 */
public class InstationMessage {

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 发送者账号
	 */
	private Long userId;
	
	/**
	 * 发送类型
	 */
	private Integer type;
	
	/**
	 * 发送标题
	 */
	private String title;
	
	/**
	 * 发送内容
	 */
	private String content;
	
	/**
	 * 发送时间
	 */
	private Timestamp createTime;
	
	/**
	 * 是否删除 0未删除   1删除
	 */
	private Integer isDel;

	List<InstationMessageUser> list;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<InstationMessageUser> getList() {
		return list;
	}

	public void setList(List<InstationMessageUser> list) {
		this.list = list;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
