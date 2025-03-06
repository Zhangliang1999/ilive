package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * @author administrator 直播企业
 */
public abstract class BaseILiveEnterprise implements java.io.Serializable{

	/**
	 * 企业ID
	 */
	private Integer enterpriseId;

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业注册名称
	 */
	private String enterpriseRegName;

	/**
	 * 企业类型
	 */
	private String enterpriseType;

	/**
	 * 营业执照注册号
	 */
	private String enterpriseRegNo;

	/**
	 * 企业描述
	 */
	private String enterpriseDesc;

	/**
	 * 企业LOGO图片
	 */
	private String enterpriseImg;

	/**
	 * 公司地址
	 */
	private String enterpriseInfo;

	/**
	 * 街道信息
	 */
	private String road;

	/**
	 * 企业电话
	 */
	private String enterprisePhone;

	/**
	 * 企业认证状态 0未提交认证 1认证中 4认证通过 5认证失败
	 * 用户状态 0是普通用户观看端的 1是开通直播填写完简单信息的 2是填完简单信息但是未进行企业认证 3是提交认证信息后认证中的 4是认证成功的
	 * 5是认证失败的
	 */
	private Integer certStatus;

	/**
	 * 联系人手机号码
	 */
	private String contactPhone;

	/**
	 * 联系人姓名
	 */
	private String contactName;

	/**
	 * 申请人身份证号
	 */
	private String idCard;

	/**
	 * 申请人身份证照片
	 */
	private String idCardImg;

	/**
	 * 联系人邮箱
	 */
	private String contactEmail;

	/**
	 * 企业邮箱
	 */
	private String enterpriseEmail;

	/**
	 * 企业认证时间
	 */
	private Timestamp certTime;

	/**
	 * 企业申请时间
	 */
	private Timestamp applyTime;

	/**
	 * 企业认证资料图片
	 * 
	 * @return
	 */
	private String certResource;

	/**
	 * 企业网站
	 * 
	 * @return
	 */
	private String enterpriseSite;

	/**
	 * 营业执照
	 * 
	 * @return
	 */
	private String enterpriseLicence;
	
	/**
	 * 1、外部测试  2、内部测试  3、签约用户
	 */
	private Integer entype;

	/**
	 * 是否删除 1为删除
	 */
	private int isDel;
	
	/**
	 * 操作人
	 */
	private Long userId;
	
	/**
	 * 操作人账号
	 */
	private String userPhone;
	
	private String province;
	private String city;
	private String area;
	
	//关注总数
	private Integer concernTotal;
	
	/**
	 * 直播方名称
	 */
	private String iliveName;
	
	
	/**
	 * 类型   0个人  1企业
	 */
	private Integer stamp;
	
	/**
	 * 关注人数
	 */
	private Integer fansNum;

	/**
	 * 禁用标识  0正常  1禁用
	 */
	private Integer disabled;
	/**
	 * 认证人userId
	 */
	private Long authUserId;
	
	/**
	 * 认证人账号
	 */
	private String autoUserName;
	/**
	 * 审核人
	 * @return
	 */
	private  String checkPerson;
	
	/**
	 * 是否开发者
	 */
	private Integer isDeveloper;
	
	/**
	 * 开发者appId
	 */
	private String appId;
	
	/**
	 * 开发者secret
	 */
	private String secret;

	/**
	 * 领航用户是否自动续购套餐 默认自动 1；0 不自动
	 * @return
	 */
	private Integer autoBuy;

	/**
	 * 是否开通企业云盘 0或null 未开通   1 已开通
	 */
	private Integer openAreaStatus;

	/**
	 * 天翼云盘企业域
	 */
	private String openArea;

	/**
	 * 企业云盘id
	 */
	private Long corpId;

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
	/**
	 * 对接接口调用的ip地址
	 * @return
	 */
	private String IPAddress;
	/**
	 * 是否开启隐私设置 0 关闭；1 开始
	 * @return
	 */
	private Integer privacy;
	public Long getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(Long authUserId) {
		this.authUserId = authUserId;
	}

	public String getAutoUserName() {
		return autoUserName;
	}

	public void setAutoUserName(String autoUserName) {
		this.autoUserName = autoUserName;
	}

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public String getIliveName() {
		return iliveName;
	}

	public void setIliveName(String iliveName) {
		this.iliveName = iliveName;
	}

	public Long getUserId() {
		return userId;
	}

	public Integer getEntype() {
		return entype;
	}

	public void setEntype(Integer entype) {
		this.entype = entype;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getStamp() {
		return stamp;
	}

	public void setStamp(Integer stamp) {
		this.stamp = stamp;
	}

	public String getEnterpriseDesc() {
		return enterpriseDesc;
	}

	public void setEnterpriseDesc(String enterpriseDesc) {
		this.enterpriseDesc = enterpriseDesc;
	}

	public String getEnterpriseImg() {
		return enterpriseImg;
	}

	public void setEnterpriseImg(String enterpriseImg) {
		this.enterpriseImg = enterpriseImg;
	}

	public String getEnterpriseInfo() {
		return enterpriseInfo;
	}

	public void setEnterpriseInfo(String enterpriseInfo) {
		this.enterpriseInfo = enterpriseInfo;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public Timestamp getCertTime() {
		return certTime;
	}

	public void setCertTime(Timestamp certTime) {
		this.certTime = certTime;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getCertResource() {
		return certResource;
	}

	public void setCertResource(String certResource) {
		this.certResource = certResource;
	}

	public String getEnterpriseSite() {
		return enterpriseSite;
	}

	public void setEnterpriseSite(String enterpriseSite) {
		this.enterpriseSite = enterpriseSite;
	}

	public String getEnterpriseLicence() {
		return enterpriseLicence;
	}

	public void setEnterpriseLicence(String enterpriseLicence) {
		this.enterpriseLicence = enterpriseLicence;
	}

	public String getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	public String getEnterpriseRegNo() {
		return enterpriseRegNo;
	}

	public void setEnterpriseRegNo(String enterpriseRegNo) {
		this.enterpriseRegNo = enterpriseRegNo;
	}

	public Timestamp getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getEnterpriseRegName() {
		return enterpriseRegName;
	}

	public void setEnterpriseRegName(String enterpriseRegName) {
		this.enterpriseRegName = enterpriseRegName;
	}

	public String getEnterprisePhone() {
		return enterprisePhone;
	}

	public void setEnterprisePhone(String enterprisePhone) {
		this.enterprisePhone = enterprisePhone;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getEnterpriseEmail() {
		return enterpriseEmail;
	}

	public void setEnterpriseEmail(String enterpriseEmail) {
		this.enterpriseEmail = enterpriseEmail;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getIdCardImg() {
		return idCardImg;
	}

	public void setIdCardImg(String idCardImg) {
		this.idCardImg = idCardImg;
	}

	public Integer getFansNum() {
		return fansNum;
	}

	public void setFansNum(Integer fansNum) {
		this.fansNum = fansNum;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getConcernTotal() {
		return concernTotal;
	}

	public void setConcernTotal(Integer concernTotal) {
		this.concernTotal = concernTotal;
	}

	public Integer getIsDeveloper() {
		return isDeveloper;
	}

	public void setIsDeveloper(Integer isDeveloper) {
		this.isDeveloper = isDeveloper;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getAutoBuy() {
		return autoBuy;
	}

	public void setAutoBuy(Integer autoBuy) {
		this.autoBuy = autoBuy;
	}

	public String getOpenArea() {
		return openArea;
	}

	public void setOpenArea(String openArea) {
		this.openArea = openArea;
	}

	public Integer getOpenAreaStatus() {
		return openAreaStatus;
	}

	public void setOpenAreaStatus(Integer openAreaStatus) {
		this.openAreaStatus = openAreaStatus;
	}

	public Long getCorpId() {
		return corpId;
	}

	public void setCorpId(Long corpId) {
		this.corpId = corpId;
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

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public Integer getPrivacy() {
		return privacy;
	}

	public void setPrivacy(Integer privacy) {
		this.privacy = privacy;
	}

	@Override
	public String toString() {
		return "BaseILiveEnterprise{" +
				"enterpriseId=" + enterpriseId +
				", enterpriseName='" + enterpriseName + '\'' +
				", enterpriseRegName='" + enterpriseRegName + '\'' +
				", enterpriseType='" + enterpriseType + '\'' +
				", enterpriseRegNo='" + enterpriseRegNo + '\'' +
				", enterpriseDesc='" + enterpriseDesc + '\'' +
				", enterpriseImg='" + enterpriseImg + '\'' +
				", enterpriseInfo='" + enterpriseInfo + '\'' +
				", road='" + road + '\'' +
				", enterprisePhone='" + enterprisePhone + '\'' +
				", certStatus=" + certStatus +
				", contactPhone='" + contactPhone + '\'' +
				", contactName='" + contactName + '\'' +
				", idCard='" + idCard + '\'' +
				", idCardImg='" + idCardImg + '\'' +
				", contactEmail='" + contactEmail + '\'' +
				", enterpriseEmail='" + enterpriseEmail + '\'' +
				", certTime=" + certTime +
				", applyTime=" + applyTime +
				", certResource='" + certResource + '\'' +
				", enterpriseSite='" + enterpriseSite + '\'' +
				", enterpriseLicence='" + enterpriseLicence + '\'' +
				", entype=" + entype +
				", isDel=" + isDel +
				", userId=" + userId +
				", userPhone='" + userPhone + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", area='" + area + '\'' +
				", concernTotal=" + concernTotal +
				", iliveName='" + iliveName + '\'' +
				", stamp=" + stamp +
				", fansNum=" + fansNum +
				", disabled=" + disabled +
				", authUserId=" + authUserId +
				", autoUserName='" + autoUserName + '\'' +
				", checkPerson='" + checkPerson + '\'' +
				", isDeveloper=" + isDeveloper +
				", appId='" + appId + '\'' +
				", secret='" + secret + '\'' +
				", autoBuy=" + autoBuy +
				", openAreaStatus=" + openAreaStatus +
				", openArea='" + openArea + '\'' +
				", corpId=" + corpId +
				", userInfoUrl='" + userInfoUrl + '\'' +
				", checkIfCanUrl='" + checkIfCanUrl + '\'' +
				'}';
	}
}
