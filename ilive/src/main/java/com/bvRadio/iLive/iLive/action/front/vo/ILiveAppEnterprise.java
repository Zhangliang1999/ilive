package com.bvRadio.iLive.iLive.action.front.vo;

/**
 * 企业信息
 * 
 * @author administrator
 *
 */
public class ILiveAppEnterprise {

	/**
	 * 企业ID
	 */
	private Integer enterpriseId;

	/**
	 * 企业名称
	 */
	private String enterpriseName;

	/**
	 * 企业简介
	 */
	private String enterpriseDesc;

	/**
	 * 企业图像
	 */
	private String enterpriseImg;

	/**
	 * 认证状态
	 */
	private Integer certStatus;

	/**
	 * 主页地址
	 */
	private String homePageUrl;

	/**
	 * 关注人数
	 * 
	 * @return
	 */
	private Long concernTotal;

	/**
	 * 关注状态
	 * 
	 * @return
	 */
	private Integer concernStatus;

	/**
	 * 注册时间
	 * 
	 * @return
	 */
	private String applyTime;

	/**
	 * 认证时间
	 * 
	 * @return
	 */
	private String certTime;

	/**
	 * 1、外部测试  2、内部测试  3、签约用户
	 */
	private Integer entype;
	
	/**
	 * 绑定手机号
	 */
	private String bindPhone;
	
	/**
	 * 类型   0个人  1企业
	 */
	private Integer stamp;
	private String contactPhone;

	public Integer getEntype() {
		return entype;
	}

	public void setEntype(Integer entype) {
		this.entype = entype;
	}

	public String getBindPhone() {
		return bindPhone;
	}

	public void setBindPhone(String bindPhone) {
		this.bindPhone = bindPhone;
	}

	public Integer getStamp() {
		return stamp;
	}

	public void setStamp(Integer stamp) {
		this.stamp = stamp;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseImg() {
		return enterpriseImg;
	}

	public void setEnterpriseImg(String enterpriseImg) {
		this.enterpriseImg = enterpriseImg;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

	public String getHomePageUrl() {
		return homePageUrl;
	}

	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}

	public String getEnterpriseDesc() {
		return enterpriseDesc;
	}

	public void setEnterpriseDesc(String enterpriseDesc) {
		this.enterpriseDesc = enterpriseDesc;
	}

	public Long getConcernTotal() {
		return concernTotal;
	}

	public void setConcernTotal(Long concernTotal) {
		this.concernTotal = concernTotal;
	}

	public Integer getConcernStatus() {
		return concernStatus;
	}

	public void setConcernStatus(Integer concernStatus) {
		this.concernStatus = concernStatus;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getCertTime() {
		return certTime;
	}

	public void setCertTime(String certTime) {
		this.certTime = certTime;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

}
