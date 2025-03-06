package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.BBSDiyform;

/**
 * 问卷收集的数据
 * 
 * @author administrator
 *
 */
@SuppressWarnings("serial")
public abstract class BaseBBSDiyformData implements java.io.Serializable {

	// Fields

	/**
	 * 暂不知道
	 */
	private Integer dataId;

	/**
	 * 主键
	 */
	private Integer id;

	/**
	 * 问卷标题
	 */
	private String dataTitle;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	/**
	 * 问卷回答的答案
	 */
	private String dataValue;

	/**
	 * 选项顺序
	 */
	private Integer dataOrder;

	/**
	 * 关联的表单
	 */
	private BBSDiyform bbsDiyform;

	// Constructors

	/** default constructor */
	public BaseBBSDiyformData() {
	}

	/** full constructor */
	public BaseBBSDiyformData(Integer id, Integer dataId, String dataTitle, Timestamp createTime, String dataValue,
			Integer dataOrder, BBSDiyform bbsDiyform) {
		super();
		this.id = id;
		this.dataId = dataId;
		this.dataTitle = dataTitle;
		this.createTime = createTime;
		this.dataValue = dataValue;
		this.dataOrder = dataOrder;
		this.bbsDiyform = bbsDiyform;
	}

	public Integer getDataId() {
		return dataId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public String getDataTitle() {
		return dataTitle;
	}

	public void setDataTitle(String dataTitle) {
		this.dataTitle = dataTitle;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public Integer getDataOrder() {
		return dataOrder;
	}

	public void setDataOrder(Integer dataOrder) {
		this.dataOrder = dataOrder;
	}

	public BBSDiyform getBbsDiyform() {
		return bbsDiyform;
	}

	public void setBbsDiyform(BBSDiyform bbsDiyform) {
		this.bbsDiyform = bbsDiyform;
	}

}