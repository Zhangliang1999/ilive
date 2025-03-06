package com.bvRadio.iLive.iLive.config;

public class ApplePayReturnJSON {
	
	private Integer status;
	private String environment;
	private Receipt receipt;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public Receipt getReceipt() {
		return receipt;
	}
	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
	public ApplePayReturnJSON() {
		super();
	}
	@Override
	public String toString() {
		return "ApplePayReturnJSON [status=" + status + ", environment="
				+ environment + ", receipt=" + receipt + "]";
	}
	
}
