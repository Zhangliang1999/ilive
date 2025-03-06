package com.bvRadio.iLive.iLive.entity.vo;

import java.util.List;

public class FileInfo {
	private String  userName;
	private String Url;
	private String content;
	private Integer msgType;
	private Integer liveId; 
	private String ip;
	private List<FileResource> list;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public Integer getLiveId() {
		return liveId;
	}
	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public List<FileResource> getList() {
		return list;
	}
	public void setList(List<FileResource> list) {
		this.list = list;
	}
	

}
