package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

public class ILiveFCodeDetail {
	//这个F码的id
	private Long id;
	
	//XX公司F码的ID
	private Long codeId;
	
	//F码
	private String code;
	
	//是否使用 0为未使用  1为已经使用
	private int isUsed;
	
	//使用时间
	private Timestamp usedTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodeId() {
		return codeId;
	}

	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}

	public Timestamp getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Timestamp usedTime) {
		this.usedTime = usedTime;
	}
	
}
