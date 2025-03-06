package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveEnterprise;

/**
 * 直播企业
 * 
 * @author administrator
 */
public class ILiveEnterprise extends BaseILiveEnterprise implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 构建简单企业json对象
	 * 
	 * @return
	 */
	public JSONObject toSimpleJsonObject() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jobj = new JSONObject();
		jobj.put("enterpriseId", this.getEnterpriseId());
		jobj.put("enterpriseName", this.getEnterpriseName());
		jobj.put("enterpriseDesc", this.getEnterpriseDesc());
		jobj.put("enterpriseImg", this.getEnterpriseImg());
		jobj.put("concernStatus", this.getConcernStatus() == null ? 0 : this.getConcernStatus());
		jobj.put("certStatus", this.getCertStatus() == null ? 0 : this.getCertStatus());
		jobj.put("concernTotal", this.getConcernTotal());
		jobj.put("isEnterpriseRem", this.getIsEnterpriseRem());
		Timestamp applyTime = this.getApplyTime();
		if (applyTime == null) {
			jobj.put("applyTime", format.format(new Date()));
		} else {
			jobj.put("applyTime", format.format(applyTime));
		}
		return jobj;
	}

	/**
	 * 关注的状态 0为未关注 1为已关注
	 */
	private Integer concernStatus;
	/**
	 * 是否开启关注 0为未 1为
	 */
	private Integer isEnterpriseRem;


	public Integer getConcernStatus() {
		return concernStatus;
	}
	
	

	public Integer getIsEnterpriseRem() {
		return isEnterpriseRem;
	}



	public void setIsEnterpriseRem(Integer isEnterpriseRem) {
		this.isEnterpriseRem = isEnterpriseRem;
	}



	public void setConcernStatus(Integer concernStatus) {
		this.concernStatus = concernStatus;
	}
}
