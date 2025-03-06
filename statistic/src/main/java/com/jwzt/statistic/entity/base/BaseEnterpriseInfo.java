package com.jwzt.statistic.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class BaseEnterpriseInfo implements java.io.Serializable {

	private Integer id;
	private Timestamp createTime;
	private String enterpriseName;
	private String enterpriseDesc;
	private String enterpriseImg;
	private String homePageUrl;
	private Timestamp certTime;
	@JsonIgnore
	private Integer certStatus;// 0未提交认证 1认证中  4认证通过 5认证失败
	private Timestamp applyTime;
	private String contactPhone;
	
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
    

	public Integer getStamp() {
		return stamp;
	}

	public void setStamp(Integer stamp) {
		this.stamp = stamp;
	}

	public String getBindPhone() {
		return bindPhone;
	}

	public void setBindPhone(String bindPhone) {
		this.bindPhone = bindPhone;
	}

	public Integer getEntype() {
		return entype;
	}

	public void setEntype(Integer entype) {
		this.entype = entype;
	}

	public BaseEnterpriseInfo() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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

	public String getHomePageUrl() {
		return homePageUrl;
	}

	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}

	public Timestamp getCertTime() {
		return certTime;
	}

	public void setCertTime(Timestamp certTime) {
		this.certTime = certTime;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

	public Timestamp getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

}
