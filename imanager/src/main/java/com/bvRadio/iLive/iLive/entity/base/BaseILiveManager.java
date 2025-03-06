package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

/**
 * @author administrator Ilive管理用户 一般为企业用户
 */
public abstract class BaseILiveManager {

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

	/**
	 * 用户是否为主账号 0 为第一级 1为第二级 2为第三级......
	 * 
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
	
	/********新增字段********/
	
	
	/**
	 * 是否拉黑    1为拉黑   0或null为正常
	 */
	private Integer isBlack;
	
	/**
	 * 拉黑理由
	 */
	private String blackReason;
	
	/**
	 * 标签
	 */
	private String mark;
	
	/**
	 * 注册来源
	 */
	private String createFrom;
	
	/**
	 * 最后一次登录IP
	 */
	private String loginIp;
	
	/**
	 * 最后一次登录ip  与ilive相同
	 */
	private String lastIP;
	
	/**
	 * 最后一次更新时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 最后一次编辑的编辑人账号
	 */
	private String updateUserName;
	
	/**
	 * 备注
	 */
	private String beizhu;
	
	//用于查询的时间
	private Date startTime;
	private Date endTime;
	/**
	 * 直播间ID
	 */
	private Integer roomId;
	
	/**
	 * 是否被整个系统拉黑  1位拉黑  0或null为默认
	 */
	private Integer isAllBlack;
	
	/**
	 * 全局拉黑理由
	 */
	private String allBlackReason;
	
	/**
	 * 全局拉黑账号
	 */
	private String allBlackUser;
	
	/**
	 * 注册来源
	 */
	private Integer registerSource;
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

	public Integer getRegisterSource() {
		return registerSource;
	}

	public void setRegisterSource(Integer registerSource) {
		this.registerSource = registerSource;
	}

	public Integer getIsAllBlack() {
		return isAllBlack;
	}

	public void setIsAllBlack(Integer isAllBlack) {
		this.isAllBlack = isAllBlack;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getEditUserNameIdentify() {
		return editUserNameIdentify;
	}

	public void setEditUserNameIdentify(Integer editUserNameIdentify) {
		this.editUserNameIdentify = editUserNameIdentify;
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

	public Calendar getLockCalendar() {
		return lockCalendar;
	}

	public void setLockCalendar(Calendar lockCalendar) {
		this.lockCalendar = lockCalendar;
	}

	public Timestamp getUpdatePasswordTime() {
		return updatePasswordTime;
	}

	public void setUpdatePasswordTime(Timestamp updatePasswordTime) {
		this.updatePasswordTime = updatePasswordTime;
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

	public String getSubRoomIds() {
		return subRoomIds;
	}

	public void setSubRoomIds(String subRoomIds) {
		this.subRoomIds = subRoomIds;
	}

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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}


	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getCreateFrom() {
		return createFrom;
	}

	public void setCreateFrom(String createFrom) {
		this.createFrom = createFrom;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getIsBlack() {
		return isBlack;
	}

	public void setIsBlack(Integer isBlack) {
		this.isBlack = isBlack;
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

	public String getBlackReason() {
		return blackReason;
	}

	public void setBlackReason(String blackReason) {
		this.blackReason = blackReason;
	}

	public String getLastIP() {
		return lastIP;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public String getAllBlackReason() {
		return allBlackReason;
	}

	public void setAllBlackReason(String allBlackReason) {
		this.allBlackReason = allBlackReason;
	}

	public String getAllBlackUser() {
		return allBlackUser;
	}

	public void setAllBlackUser(String allBlackUser) {
		this.allBlackUser = allBlackUser;
	}
	
}
