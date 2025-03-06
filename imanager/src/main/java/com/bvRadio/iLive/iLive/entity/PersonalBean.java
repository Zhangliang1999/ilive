package com.bvRadio.iLive.iLive.entity;

public class PersonalBean {
	private Integer id;
	private String nickname;
	private String origPwd;
	private String realname;
	private String userImg;
	private String password;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getOrigPwd() {
		return origPwd;
	}
	public void setOrigPwd(String origPwd) {
		this.origPwd = origPwd;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public PersonalBean() {
		super();
	}
	public PersonalBean(Integer id, String nickname, String origPwd,
			String realname, String userImg, String password) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.origPwd = origPwd;
		this.realname = realname;
		this.userImg = userImg;
		this.password = password;
	}
	
}
