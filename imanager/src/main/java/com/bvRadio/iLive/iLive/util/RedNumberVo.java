package com.bvRadio.iLive.iLive.util;

import java.util.List;

public class RedNumberVo {
	private Integer redId;
	private Long redAmount;
	private Integer redNumber;
	private List<Long> list;
	public Integer getRedId() {
		return redId;
	}
	public void setRedId(Integer redId) {
		this.redId = redId;
	}
	public List<Long> getList() {
		return list;
	}
	public void setList(List<Long> list) {
		this.list = list;
	}
	public RedNumberVo() {
		super();
	}
	public Long getRedAmount() {
		return redAmount;
	}
	public void setRedAmount(Long redAmount) {
		this.redAmount = redAmount;
	}
	public Integer getRedNumber() {
		return redNumber;
	}
	public void setRedNumber(Integer redNumber) {
		this.redNumber = redNumber;
	}
}
