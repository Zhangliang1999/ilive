package com.bvRadio.iLive.manager.entity;

import java.util.HashMap;
import java.util.Map;

public class WorkLog {
	/**
	 * 当前系统systemId
	 */
	public final static Integer CURRENT_SYSTEM_ID = 200;
	
	/**
	 * 登录  id与名称
	 */
	public final static Integer MODEL_LOGIN = 128;
	public final static String MODEL_LOGIN_NAME = "登录";
	
	/**
	 * 创建直播间
	 */
	public final static Integer MDEOL_CREATEROOM = 129;
	public final static String MODEL_CREATEROOM_NAME = "创建直播间";
	
	/**
	 * 直播属性
	 */
	public final static Integer MODEL_EDITROOMPROPERTY = 102;
	public final static String MODEL_EDITROOMPROPERTY_NAME = "修改直播属性";
	
	/**
	 * logo设置
	 */
	public final static Integer MODEL_EDITLOGO = 103;
	public final static String MODEL_EDITLOGO_NAME = "logo设置";
	
	/**
	 * 修改报名
	 */
	public final static Integer MODEL_ENLIST = 105;
	public final static String MODEL_ENLIST_NAME = "修改报名";
	
	/**
	 * 观看授权
	 */
	public final static Integer MODEL_EDITPOWER = 104;
	public final static String MODEL_EDITPOWER_NAME = "修改观看授权";
	
	/**
	 * 直播倒计时
	 */
	public final static Integer MODEL_COUNTDOWN = 106;
	public final static String MODEL_COUNTDOWN_NAME = "修改直播倒计时";
	
	/**
	 * 修改推广分享
	 */
	public final static Integer MODEL_SHARE = 107;
	public final static String MODEL_SHARE_NAME = "修改推广分享";
	
	/**
	 * 短信邀请
	 */
	public final static Integer MODEL_INVITEMESSAGE = 130;
	public final static String MODEL_INVITEMESSAGE_NAME = "发送短信邀请";
	
	/**
	 * 邀请卡
	 */
	public final static Integer MODEL_EDITINVITE = 131;
	public final static String MODEL_EDITINVITE_NAME = "修改邀请卡";
	
	/**
	 * 移动端页面
	 */
	public final static Integer MODEL_PAGEDECORATE = 108;
	public final static String MODEL_PAGEDECORATE_NAME = "修改移动端引导页";
	
	/**
	 * 直播引导页
	 */
	public final static Integer MODEL_GUIDE = 110;
	public final static String MODEL_GUIDE_NAME = "修改直播引导页";
	
	/**
	 * 往期回看
	 */
	public final static Integer MODEL_REVIEW = 112;
	public final static String MODEL_REVIEW_NAME_ADD = "增加往期回看";
	public final static String MODEL_REVIEW_NAME_DEL = "删除往期回看";
	
	/**
	 * 结束直播
	 */
	public final static Integer MODEL_ENDLIVE = 133;
	public final static String MODEL_ENDLIVE_NAME = "结束直播";
	
	/**
	 * 开始直播
	 */
	public final static Integer MODEL_STARTLIVE = 132;
	public final static String MODEL_STARTLIVE_NAME = "开始直播";
	
	/**
	 * 媒体库上传视频
	 */
	public final static Integer MODEL_UPLOADVIDEO = 134;
	public final static String MODEL_UPLOADVIDEO_NAME = "媒体库上传视频";
	
	/**
	 * 媒体库删除视频
	 */
	public final static Integer MODEL_DELVIDEO = 137;
	public final static String MODEL_DELVIDEO_NAME = "媒体库删除视频";
	
	/**
	 * 主页修改
	 */
	public final static Integer MODEL_HOMEPAGE = 123;
	public final static String MODEL_HOMEPAGE_NAME = "主页修改";

	/**
	 * 专题创建
	 */
	public final static Integer MODEL_TOPIC = 135;
	public final static String MODEL_TOPIC_NAME = "专题创建";
	
	/**
	 * 专题修改
	 */
	public final static Integer MODEL_EDITTOPIC = 136;
	public final static String MODEL_EDITTOPIC_NAME = "专题修改";
	
	/**
	 * 企业认证
	 */
	public final static Integer MODEL_ENTEXAMINE = 203;
	public final static String MODEL_ENTEXAMINE_NAME_PASS = "企业认证通过";
	public final static String MODEL_ENTEXAMINE_NAME_NOPASS = "企业认证未通过";
	
	/**
	 * 个人认证
	 */
	public final static Integer MODEL_PEREXAMINE = 204;
	public final static String MODEL_PEREXAMINE_NAME_PASS = "个人认证通过";
	public final static String MODEL_PEREXAMINE_NAME_NOPASS = "个人认证未通过";
	
	
	
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

	public WorkLog() {
		
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
