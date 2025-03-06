package com.bvRadio.iLive.iLive.entity;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveEnterprise;

/**
 * 直播企业
 * 
 * @author administrator
 */
public class ILiveEnterprise extends BaseILiveEnterprise {

	/**
	 * 构建简单企业json对象
	 * 
	 * @return
	 */
	public JSONObject toSimpleJsonObject() {
		JSONObject jobj = new JSONObject();
		jobj.put("enterpriseId", this.getEnterpriseId());
		jobj.put("enterpriseName", this.getEnterpriseName());
		jobj.put("enterpriseDesc", this.getEnterpriseDesc());
		jobj.put("enterpriseImg", this.getEnterpriseImg());
		jobj.put("concernStatus", this.getConcernStatus() == null ? 0 : this.getConcernStatus());
		return jobj;
	}

	/**
	 * 关注的状态 0为未关注 1为已关注
	 */
	private Integer concernStatus;

	/**
	 * 企业密码
	 */
	private String password;
	
	/**
	 * 最后一次登录IP
	 */
	private String lastIP;
	
	/**
	 * 直播时长
	 */
	private String liveTimeLong;
	
	/**
	 * 直播次数
	 */
	private Integer liveCount;
	
	public String getLiveTimeLong() {
		return liveTimeLong;
	}

	public void setLiveTimeLong(String liveTimeLong) {
		this.liveTimeLong = liveTimeLong;
	}

	public Integer getLiveCount() {
		return liveCount;
	}

	public void setLiveCount(Integer liveCount) {
		this.liveCount = liveCount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getConcernStatus() {
		return concernStatus;
	}

	public void setConcernStatus(Integer concernStatus) {
		this.concernStatus = concernStatus;
	}

	public String getLastIP() {
		return lastIP;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

}
