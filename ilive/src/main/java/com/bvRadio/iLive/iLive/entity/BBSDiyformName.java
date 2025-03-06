package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

/**
 * 问答每次修改的关联名称
 * @author Wei
 *
 */
public class BBSDiyformName {

	private Integer id;
	
	private Integer diyformId;
	
	private String diyformName;
	
	private Integer updateMark;
	
	private Timestamp createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDiyformId() {
		return diyformId;
	}

	public void setDiyformId(Integer diyformId) {
		this.diyformId = diyformId;
	}

	public String getDiyformName() {
		return diyformName;
	}

	public void setDiyformName(String diyformName) {
		this.diyformName = diyformName;
	}

	public Integer getUpdateMark() {
		return updateMark;
	}

	public void setUpdateMark(Integer updateMark) {
		this.updateMark = updateMark;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
