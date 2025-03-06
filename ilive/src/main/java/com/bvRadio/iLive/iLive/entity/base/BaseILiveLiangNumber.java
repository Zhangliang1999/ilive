package com.bvRadio.iLive.iLive.entity.base;

@SuppressWarnings("serial")
public abstract class BaseILiveLiangNumber implements java.io.Serializable {

	private Integer id;//id
	private Integer liangNumber;//靓号
	private Integer statu;//状态
	
	public BaseILiveLiangNumber(Integer id, Integer liangNumber, Integer statu) {
		super();
		this.id = id;
		this.liangNumber = liangNumber;
		this.statu = statu;
	}
	
	public BaseILiveLiangNumber() {
		super();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLiangNumber() {
		return liangNumber;
	}
	public void setLiangNumber(Integer liangNumber) {
		this.liangNumber = liangNumber;
	}
	public Integer getStatu() {
		return statu;
	}
	public void setStatu(Integer statu) {
		this.statu = statu;
	}
}