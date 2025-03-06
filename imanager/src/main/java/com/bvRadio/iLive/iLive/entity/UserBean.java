package com.bvRadio.iLive.iLive.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.web.socket.WebSocketSession;

public class UserBean {
	private String userId;
	private String username;
	private String userThumbImg;
	private Integer level;
	private IoSession session = null;
	private WebSocketSession webSocketSession = null;
	private String nickname;
	private Integer userType;// 0 个人 1企业
	private List<ILiveMessage> msgList = new ArrayList<ILiveMessage>();
	private Integer sessionType;// session使用：0 IoSession 1 WebSocketSession
	// 企业ID
	private Integer enterpriseId;
	// 认证状态
	private Integer certStatus;

	// 密码校验结果
	private Boolean passwdCheckResult = false;
	private String loginToken;


	public UserBean() {
		super();
	}

	public UserBean(String userId, String username, String userThumbImg, Integer level) {
		super();
		this.userId = userId;
		this.username = username;
		this.userThumbImg = userThumbImg;
		this.level = level;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public List<ILiveMessage> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<ILiveMessage> msgList) {
		this.msgList = msgList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserThumbImg() {
		return userThumbImg;
	}

	public void setUserThumbImg(String userThumbImg) {
		this.userThumbImg = userThumbImg;
	}

	public Integer getLevel() {
		if (null == level) {
			level = 1;
		}
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public WebSocketSession getWebSocketSession() {
		return webSocketSession;
	}

	public void setWebSocketSession(WebSocketSession webSocketSession) {
		this.webSocketSession = webSocketSession;
	}

	public Integer getSessionType() {
		return sessionType;
	}

	public void setSessionType(Integer sessionType) {
		this.sessionType = sessionType;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Boolean getPasswdCheckResult() {
		return passwdCheckResult;
	}

	public void setPasswdCheckResult(Boolean passwdCheckResult) {
		this.passwdCheckResult = passwdCheckResult;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

	
	
	
}
