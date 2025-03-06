package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSDiyform;


@SuppressWarnings("serial")
public class BBSDiyform extends BaseBBSDiyform implements java.io.Serializable {
	
	/**
	 * 表单描述
	 */
	private String diyformDesc;
	
	/**
	 * 表单类型，0表示报名 1表示投票
	 */
	private String formType;

	// Constructors

	/** default constructor */
	public BBSDiyform() {
	}

	/** minimal constructor */
	public BBSDiyform(Integer diyformId) {
		super(diyformId);
	}

	/** full constructor */
	public BBSDiyform(Integer diyformId,  String diyformName, Timestamp createTime, Set<BBSDiymodel> bbsDiymodels,
			Set<BBSDiyformData> bbsDiyformDatas) {
		super(diyformId, diyformName, createTime, bbsDiymodels, bbsDiyformDatas);
	}

	public String getDiyformDesc() {
		return diyformDesc;
	}

	public void setDiyformDesc(String diyformDesc) {
		this.diyformDesc = diyformDesc;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}
	
}
