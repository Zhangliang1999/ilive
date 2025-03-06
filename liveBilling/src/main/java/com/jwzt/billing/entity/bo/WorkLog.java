package com.jwzt.billing.entity.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ysf
 */
public class WorkLog {
	/**
	 * 当前系统systemId
	 */
	public final static Integer CURRENT_SYSTEM_ID = 500;
	/**
	 * 模块id：套餐
	 */
	public final static Integer MODEL_ID_PACKAGE = 501;
	/**
	 * 模块id：订单
	 */
	public final static Integer MODEL_ID_PAYMENT = 502;
	/**
	 * 模块id：套餐查询/试用
	 */
	public final static Integer MODEL_ID_SPAYMENT = 503;
	public Map<String, String> toParamsMap() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("systemId", String.valueOf(systemId));
		paramsMap.put("modelId", String.valueOf(modelId));
		paramsMap.put("contentId", contentId);
		paramsMap.put("content", content);
		paramsMap.put("contentName", contentName);
		paramsMap.put("userId", String.valueOf(userId));
		paramsMap.put("userName", userName);
		paramsMap.put("terminal", terminal);
		return paramsMap;
	}

	public WorkLog(Integer modelId, String contentId, String content, String contentName, Long userId, String userName,
			String terminal) {
		super();
		this.systemId = CURRENT_SYSTEM_ID;
		this.modelId = modelId;
		this.contentId = contentId;
		this.content = content;
		this.contentName = contentName;
		this.userId = userId;
		this.userName = userName;
		this.terminal = terminal;
	}

	/**
	 * 系统ID
	 */
	private Integer systemId;
	/**
	 * 模块ID
	 */
	private Integer modelId;
	/**
	 * 内容ID
	 */
	private String contentId;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 内容名称
	 */
	private String contentName;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 终端
	 */
	private String terminal;

	private String createTime;

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

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
