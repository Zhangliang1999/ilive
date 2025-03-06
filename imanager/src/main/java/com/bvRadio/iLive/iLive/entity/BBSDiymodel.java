package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSDiymodel;

@SuppressWarnings("serial")
public class BBSDiymodel extends BaseBBSDiymodel implements java.io.Serializable {

	public BBSDiymodel() {
	}

	public BBSDiymodel(Integer diymodelId) {
		super(diymodelId);
	}

	public BBSDiymodel(Integer diymodelId, String diymodelName, Short diymodelType, String diymodelTitle,
			String diymodelKey, String defValue, String optValue, String optImgUrl, Integer textSize, Integer areaRows,
			Integer areaCols, String helpTxt, Integer helpPosition, boolean isDisplay, BBSDiyform bbsDiyform,
			Integer diyOrder, Timestamp createTime) {
		super(diymodelId, diymodelName, diymodelType, diymodelTitle, diymodelKey, defValue, optValue, optImgUrl,
				textSize, areaRows, areaCols, helpTxt, helpPosition, isDisplay, bbsDiyform, diyOrder, createTime);
	}

	/**
	 * 
	 * @return
	 */
	public JSONObject toJsonObj() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("diymodelId", this.getDiymodelId());
		jsonObject.put("diymodelType",this.getDiymodelType());
		jsonObject.put("diymodelTitle", this.getDiymodelTitle());
		jsonObject.put("optValue", this.getOptValue());
		jsonObject.put("optImgUrl", this.getOptImgUrl());
		jsonObject.put("openAnswer", this.getOpenAnswer());
		
		
		
		
		return null;
	}

}
