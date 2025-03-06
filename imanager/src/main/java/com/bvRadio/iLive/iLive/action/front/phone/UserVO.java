package com.bvRadio.iLive.iLive.action.front.phone;
/**
 * 
 * @author gyf
 * 存放 用户简单信息
 */
public class UserVO {
 private Integer uid;
 private String username;
 private String userImg;
 //为添加禁言功能新加
 private String status;
public Integer getUid() {
	return uid;
}
public void setUid(Integer uid) {
	this.uid = uid;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getUserImg() {
	return userImg;
}
public void setUserImg(String userImg) {
	this.userImg = userImg;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((uid == null) ? 0 : uid.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	UserVO other = (UserVO) obj;
	if (uid == null) {
		if (other.uid != null)
			return false;
	} else if (!uid.equals(other.uid))
		return false;
	return true;
}
 
 
}
