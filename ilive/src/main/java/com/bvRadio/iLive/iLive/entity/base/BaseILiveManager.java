package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.Calendar;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

/**
 * @author administrator Ilive管理用户 一般为企业用户
 */
public abstract class BaseILiveManager implements java.io.Serializable{

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户账号
	 */
	private String userName;

	/**
	 * 用户密码
	 */
	private String userPwd;

	/**
	 * 加盐干扰值
	 */
	private String salt;
	
	private String saltPwd;

	/**
	 * 用户是否为主账号 
	 * 	0 为第一级 主账号
	 * 	1为第二级   协同管理子用户
	 * 	2为第三级  助手邀请码用户
	 * ......
	 * @return
	 */
	private Integer level;

	/**
	 * 手机号
	 * 
	 * @return
	 */
	private String mobile;

	/**
	 * 父类ID
	 * 
	 * @return
	 */
	private Long parentId;

	/**
	 * 根ID
	 * 
	 * @return
	 */
	private Long rootId;

	/**
	 * 极光推送ID
	 * 
	 * @return
	 */
	private String jpushId;

	/**
	 * 邮箱
	 * 
	 * @return
	 */
	private String email;

	/**
	 * 用户名
	 * 
	 * @return
	 */
	private String nailName;

	/**
	 * 头像
	 * 
	 * @return
	 */
	private String userImg;

	/**
	 * 登录方式 
	 * @return
	 */
	private String loginType;
	
	/**
	 * 创建时间
	 * @return
	 */
	private Timestamp createTime;
	
	/**
	 * 最后一次登录时间
	 * @return
	 */
	private Timestamp lastLoginTime;
	
	/**
	 * 最后一次登录ip
	 */
	private String lastIP;
	
	/**
	 * 最后一次登录地点
	 */
	private String lastRegion;
	
	/**
	 * 删除标记  0为未删除  1为删除
	 * @return
	 */
	private int isDel;
	
	/**
	 * 账号的企业ID
	 * 
	 * @return
	 */
	private ILiveEnterprise enterPrise;
	/**
	 * 生日
	 * @return
	 */
	private Timestamp birthday;
	
	/**
	 * 星座
	 * @return
	 */
	private String constellatory;
	
	/**
	 * 性别
	 * @return
	 */
	private String sex;
	
	/**
	 * 教育水平
	 * @return
	 */
	private String eduLevel;
	
	/**
	 * 收入水平
	 * @return
	 */
	private String incomeLevel;
	
	/**
	 * 个人职业
	 * @return
	 */
	private String occupation;
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	
	/**
	 * 修改账号标识   只能修改一次   修改过则为1
	 */
	private Integer editUserNameIdentify;
	
	/**
	 * 记录当前登录日期
	 */
	private Calendar calendar;
	
	/**
	 * 记录当天密码输入错误次数  超过五次则禁止登录
	 */
	private Integer errorNum;
	
	/**
	 * 锁定结束日志，用于自动锁定用户到期自动解除锁定
	 */
	private Calendar lockCalendar;
	
	
	/**
	 * 上一次密码修改时间
	 */
	private Timestamp updatePasswordTime;
	/**
	 * 子账户状态
	 */
	private Integer subType;
	/**
	 * 微信绑定手机号
	 */
	private String wxMobile;
	/**
	 * 用户真实姓名
	 */
	private String realName;
	/**
	 * 子账户分配到的直播间id
	 */
	private String subRoomIds;
	/**
	 * 子账户上面的权限
	 */
	private String subTop;
	/**
	 * 子账户左边的权限
	 */
	private String subLeft;
	
	/**
	 * 电信掌上营业厅的userId
	 */
	private String dxzyappId;
	
	/**
	 * 来源平台Id
	 */
	private String platformId;
	/**
	 * 智慧校園appid
	 * @return
	 */
	private String zhxyappId;
	/**
	 * 智慧校園userid
	 * @return
	 */
	private String zhxyuserId;
	/**
	 * 智慧校园获取用户信息链接
	 * @return
	 */
	private String userInfoUrl;
	/**
	 * 智慧校园获取用户是否可以观看链接
	 * @return
	 */
	private String checkIfCanUrl;
	public String getSubTop() {
		return subTop;
	}

	public void setSubTop(String subTop) {
		this.subTop = subTop;
	}

	public String getSubLeft() {
		return subLeft;
	}

	public void setSubLeft(String subLeft) {
		this.subLeft = subLeft;
	}

	public String getSubRoomIds() {
		return subRoomIds;
	}

	public void setSubRoomIds(String subRoomIds) {
		this.subRoomIds = subRoomIds;
	}

	public Timestamp getUpdatePasswordTime() {
		return updatePasswordTime;
	}

	public void setUpdatePasswordTime(Timestamp updatePasswordTime) {
		this.updatePasswordTime = updatePasswordTime;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Integer getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(Integer errorNum) {
		this.errorNum = errorNum;
	}

	public Integer getEditUserNameIdentify() {
		return editUserNameIdentify;
	}

	public void setEditUserNameIdentify(Integer editUserNameIdentify) {
		this.editUserNameIdentify = editUserNameIdentify;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
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

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getRootId() {
		return rootId;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getJpushId() {
		return jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNailName() {
		return nailName;
	}

	public void setNailName(String nailName) {
		this.nailName = nailName;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public ILiveEnterprise getEnterPrise() {
		return enterPrise;
	}

	public void setEnterPrise(ILiveEnterprise enterPrise) {
		this.enterPrise = enterPrise;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public String getConstellatory() {
		return constellatory;
	}

	public void setConstellatory(String constellatory) {
		this.constellatory = constellatory;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEduLevel() {
		return eduLevel;
	}

	public void setEduLevel(String eduLevel) {
		this.eduLevel = eduLevel;
	}

	public String getIncomeLevel() {
		return incomeLevel;
	}

	public void setIncomeLevel(String incomeLevel) {
		this.incomeLevel = incomeLevel;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getSaltPwd() {
		return saltPwd;
	}

	public void setSaltPwd(String saltPwd) {
		this.saltPwd = saltPwd;
	}

	public Calendar getLockCalendar() {
		return lockCalendar;
	}

	public void setLockCalendar(Calendar lockCalendar) {
		this.lockCalendar = lockCalendar;
	}
	

	public String getLastIP() {
		return lastIP;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public Integer getSubType() {
		return subType;
	}

	public void setSubType(Integer subType) {
		this.subType = subType;
	}

	public String getWxMobile() {
		return wxMobile;
	}

	public void setWxMobile(String wxMobile) {
		this.wxMobile = wxMobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDxzyappId() {
		return dxzyappId;
	}

	public void setDxzyappId(String dxzyappId) {
		this.dxzyappId = dxzyappId;
	}

	public String getLastRegion() {
		return lastRegion;
	}

	public void setLastRegion(String lastRegion) {
		this.lastRegion = lastRegion;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getZhxyappId() {
		return zhxyappId;
	}

	public void setZhxyappId(String zhxyappId) {
		this.zhxyappId = zhxyappId;
	}

	public String getZhxyuserId() {
		return zhxyuserId;
	}

	public void setZhxyuserId(String zhxyuserId) {
		this.zhxyuserId = zhxyuserId;
	}

	public String getUserInfoUrl() {
		return userInfoUrl;
	}

	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

	public String getCheckIfCanUrl() {
		return checkIfCanUrl;
	}

	public void setCheckIfCanUrl(String checkIfCanUrl) {
		this.checkIfCanUrl = checkIfCanUrl;
	}
	
}
