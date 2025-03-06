package com.jwzt.statistic.entity.vo;

/**
 * 企业使用会议次数统计
 * @author zl
 *
 */
public class MeetingEnterpriseResult {
private String id;
 private Long enterpriseId;
 private String enterpriseName;
 private Long useTime;
 
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public Long getEnterpriseId() {
	return enterpriseId;
}
public void setEnterpriseId(Long enterpriseId) {
	this.enterpriseId = enterpriseId;
}
public String getEnterpriseName() {
	return enterpriseName;
}
public void setEnterpriseName(String enterpriseName) {
	this.enterpriseName = enterpriseName;
}
public Long getUseTime() {
	return useTime;
}
public void setUseTime(Long useTime) {
	this.useTime = useTime;
}
 
}
