package com.jwzt.jssdk;

public class JSSDKInfo {
	
	
	public String appId;
	public String timestamp;
	public String nonceStr;
	public String signature;
	public String getAppId() {
		return appId;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public String getSignature() {
		return signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
}
