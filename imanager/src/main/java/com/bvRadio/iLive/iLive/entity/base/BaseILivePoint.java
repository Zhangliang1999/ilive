package com.bvRadio.iLive.iLive.entity.base;

@SuppressWarnings("serial")
public abstract class BaseILivePoint implements java.io.Serializable {

	private Integer pointId;
	private String pointUrl;
	private Integer pointPort;
	private String  vod;
	private String pointRTMP;
	private boolean isDefault;

	public BaseILivePoint() {
	}

	public BaseILivePoint(Integer pointId) {
		this.pointId = pointId;
	}

	

	public Integer getPointId() {
		return pointId;
	}

	public void setPointId(Integer pointId) {
		this.pointId = pointId;
	}

	public String getPointUrl() {
		return pointUrl;
	}

	public void setPointUrl(String pointUrl) {
		this.pointUrl = pointUrl;
	}

	public Integer getPointPort() {
		return pointPort;
	}

	public void setPointPort(Integer pointPort) {
		this.pointPort = pointPort;
	}

	public String getVod() {
		return vod;
	}

	public void setVod(String vod) {
		this.vod = vod;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getPointRTMP() {
		return pointRTMP;
	}

	public void setPointRTMP(String pointRTMP) {
		this.pointRTMP = pointRTMP;
	}

	
	
}