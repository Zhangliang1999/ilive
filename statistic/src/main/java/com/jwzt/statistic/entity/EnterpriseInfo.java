package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.jwzt.statistic.entity.base.BaseEnterpriseInfo;

@SuppressWarnings("serial")
public class EnterpriseInfo extends BaseEnterpriseInfo {
	private EnterpriseStatisticResult statisticResult;
	private String certStatusName;// 0未提交认证 1认证中  4认证通过 5认证失败
	private String entypeName;//1、外部测试  2、内部测试  3、签约用户
	private String stampName;// 类型   0个人  1企业
	
	public void transformMeaning(){
		if(null != this.getStamp() && this.getStamp() == 1){
			this.stampName = "企业";
		}else{
			this.stampName = "个人";
		}
		if(null != this.getCertStatus() && this.getCertStatus() == 4){
			this.certStatusName = "已认证";
		}else{
			this.certStatusName = "试用";
		}
		if(null != this.getEntype()){
			switch(this.getEntype()){
			case 1:
				this.entypeName = "外部测试";
				break;
			case 2:
				this.entypeName = "外部测试";
				break;
			case 3:
				this.entypeName = "签约用户";
				break;
			default:
				this.entypeName = "其他";
				break;
			}
		}else{
			this.entypeName = "其他";
		}
		
	}
	
	public String getStampName() {
		return stampName;
	}

	public String getCertStatusName() {
		return certStatusName;
	}

	public void setCertStatusName(String certStatusName) {
		this.certStatusName = certStatusName;
	}

	public String getEntypeName() {
		return entypeName;
	}

	public void setEntypeName(String entypeName) {
		this.entypeName = entypeName;
	}

	public void setStampName(String stampName) {
		this.stampName = stampName;
	}

	public EnterpriseStatisticResult getStatisticResult() {
		return statisticResult;
	}

	public void setStatisticResult(EnterpriseStatisticResult statisticResult) {
		this.statisticResult = statisticResult;
	}

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public EnterpriseInfo() {
		super();
	}

}
