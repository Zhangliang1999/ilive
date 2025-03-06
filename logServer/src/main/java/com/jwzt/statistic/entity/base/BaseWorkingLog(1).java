package com.jwzt.statistic.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 工作日志
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class BaseWorkingLog implements Serializable{
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 系统ID
	 */
	private Integer systemId;
	/**
	 * 模块ID
	 */
	private Integer modelId;
	/**
	 * 操作内容ID
	 */
	private String contentId;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 操作名称
	 */
	private String contentName;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 终端
	 */
	private String terminal;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public Integer getSystemId() {
		return systemId;
	}
	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}
	public Integer getModelId() {
		return modelId;
	}
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public BaseWorkingLog() {
		super();
	}
	public BaseWorkingLog(String id, Integer systemId, Integer modelId,
			String contentId, String content, String contentName,
			Integer userId, String userName, String terminal,
			Timestamp createTime) {
		super();
		this.id = id;
		this.systemId = systemId;
		this.modelId = modelId;
		this.contentId = contentId;
		this.content = content;
		this.contentName = contentName;
		this.userId = userId;
		this.userName = userName;
		this.terminal = terminal;
		this.createTime = createTime;
	}
	
}
