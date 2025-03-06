package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * @author administrator 直播企业
 */
public abstract class BaseILiveEnterprise {

	/**
	 * 企业ID
	 */
	private Integer enterpriseId;

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业注册名称   组织机构名称
	 */
	private String enterpriseRegName;

	/**
	 * 认证类型  1、IT   2、金融     3、教育    4、其他
	 */
	private String enterpriseType;
	
	/**
	 * 企业类型  1、 外部测试  2、内部测试  3、签约用户
	 */
	private Integer entype;
	

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
	 * 企业认证状态 0未提交认证 1认证中  4认证通过 5认证失败
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
	
	private String idCardImgface;
	
	private String idCardImgReverse;

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
	 * 企业认证资料
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
	 * 营业执照扫描件
	 * 
	 * @return
	 */
	private String enterpriseLicence;

	/**
	 * 是否删除 1为删除
	 */
	private int isDel;
	
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String area;
	/**
	 * 直播方名称
	 */
	private String iliveName;
	
	/**
	 * 提交人id
	 */
	private Long userId;
	
	/**
	 * 提交人账号/手机号
	 */
	private String userPhone;
	
	/**
	 * 禁用理由
	 */
	private String forbiddenReason;
	/**
	 * 禁用人
	 */
	private String forbiddenUser;
	
	/*
	 * 类型   0个人  1企业
	 */
	private Integer stamp;
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
	 * 注册起始时间
	 */
	private Timestamp registerStartTime;
	private Timestamp registerEndTime;
	
	/**
	 * 认证起始时间
	 */
	private Timestamp authStartTime;
	private Timestamp authEndTime;
	
	/**
	 * 审核人
	 * @return
	 */
	private  String checkPerson;
	
	/**
	 * 企业备注
	 */
	private String remark;
	
	/**
	 * 最后编辑人
	 */
	private String lastEditPerson;
	
	/**
	 * 最后编辑时间
	 */
	private Timestamp lastEditTime;
	/**
	 * 是不是开发者
	 */
	private Integer isDeveloper;
	/**
	 * 开发者appID
	 */
	private String appId;
	/**
	 * 开发者secret
	 */
	private String secret;
	private Integer autoBuy;
	private Integer privacy;
	public Timestamp getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public String getLastEditPerson() {
		return lastEditPerson;
	}

	public void setLastEditPerson(String lastEditPerson) {
		this.lastEditPerson = lastEditPerson;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getForbiddenUser() {
		return forbiddenUser;
	}

	public void setForbiddenUser(String forbiddenUser) {
		this.forbiddenUser = forbiddenUser;
	}

	public String getForbiddenReason() {
		return forbiddenReason;
	}

	public void setForbiddenReason(String forbiddenReason) {
		this.forbiddenReason = forbiddenReason;
	}

	public Long getUserId() {
		return userId;
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

	public Timestamp getRegisterStartTime() {
		return registerStartTime;
	}

	public void setRegisterStartTime(Timestamp registerStartTime) {
		this.registerStartTime = registerStartTime;
	}

	public Timestamp getRegisterEndTime() {
		return registerEndTime;
	}

	public void setRegisterEndTime(Timestamp registerEndTime) {
		this.registerEndTime = registerEndTime;
	}

	public Timestamp getAuthStartTime() {
		return authStartTime;
	}

	public Integer getEntype() {
		return entype;
	}

	public void setEntype(Integer entype) {
		this.entype = entype;
	}

	public void setAuthStartTime(Timestamp authStartTime) {
		this.authStartTime = authStartTime;
	}

	public Timestamp getAuthEndTime() {
		return authEndTime;
	}

	public void setAuthEndTime(Timestamp authEndTime) {
		this.authEndTime = authEndTime;
	}

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

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public String getIdCardImgface() {
		return idCardImgface;
	}

	public void setIdCardImgface(String idCardImgface) {
		this.idCardImgface = idCardImgface;
	}

	public String getIdCardImgReverse() {
		return idCardImgReverse;
	}

	public void setIdCardImgReverse(String idCardImgReverse) {
		this.idCardImgReverse = idCardImgReverse;
	}

	public String getIliveName() {
		return iliveName;
	}

	public void setIliveName(String iliveName) {
		this.iliveName = iliveName;
	}

	public Integer getStamp() {
		return stamp;
	}

	public void setStamp(Integer stamp) {
		this.stamp = stamp;
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

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
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

	public Integer getIsDeveloper() {
		return isDeveloper;
	}

	public void setIsDeveloper(Integer isDeveloper) {
		this.isDeveloper = isDeveloper;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Integer getAutoBuy() {
		return autoBuy;
	}

	public void setAutoBuy(Integer autoBuy) {
		this.autoBuy = autoBuy;
	}

	public Integer getPrivacy() {
		return privacy;
	}

	public void setPrivacy(Integer privacy) {
		this.privacy = privacy;
	}

	@Override
	public String toString() {
		return "BaseILiveEnterprise [enterpriseName=" + enterpriseName + "]";
	}
	

}
