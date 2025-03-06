package com.bvRadio.iLive.iLive.action.front.vo;

public class AppDiyformReceiver {
	
	// 顺序
	private Integer dataOrder;
	
	// 值
	private String dataValue;
	
	// 辅助值
	private String subValue;
	
	// 标题
	private String dataTitle;
	
	// 问题Id
	private Integer dataModelId;
	
	// 短信验证
	private Integer needMsgValid;
	
	
	public Integer getDataOrder() {
		return dataOrder;
	}

	public void setDataOrder(Integer dataOrder) {
		this.dataOrder = dataOrder;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataTitle() {
		return dataTitle;
	}

	public void setDataTitle(String dataTitle) {
		this.dataTitle = dataTitle;
	}

	public Integer getDataModelId() {
		return dataModelId;
	}

	public void setDataModelId(Integer dataModelId) {
		this.dataModelId = dataModelId;
	}

	public Integer getNeedMsgValid() {
		return needMsgValid;
	}

	public void setNeedMsgValid(Integer needMsgValid) {
		this.needMsgValid = needMsgValid;
	}

	public String getSubValue() {
		return subValue;
	}

	public void setSubValue(String subValue) {
		this.subValue = subValue;
	}
	

}
