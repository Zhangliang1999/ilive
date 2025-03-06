package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

/**
 * 专题 实体
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public abstract class BaseILiveThematic  implements java.io.Serializable {
	/**
	 * 专题ID
	 */
	private Long thematicId;
	/**
	 * 专题名称
	 */
	private String thematicName;
	/**
	 * 专题简介
	 */
	private String thematicDesc;
	/**
	 * 专题封面
	 */
	private String thematicImage;
	/**
	 * 模板ID
	 */
	private Integer templateId;
	/**
	 * 创建人名称
	 */
	private String createName;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 列表页选择框
	 */
	private Integer isChecked;
	/**
	 * 是否删除
	 */
	private Boolean isDelete;
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	public Long getThematicId() {
		return thematicId;
	}
	public void setThematicId(Long thematicId) {
		this.thematicId = thematicId;
	}
	public String getThematicName() {
		return thematicName;
	}
	public void setThematicName(String thematicName) {
		this.thematicName = thematicName;
	}
	public String getThematicDesc() {
		return thematicDesc;
	}
	public void setThematicDesc(String thematicDesc) {
		this.thematicDesc = thematicDesc;
	}
	public String getThematicImage() {
		return thematicImage;
	}
	public void setThematicImage(String thematicImage) {
		this.thematicImage = thematicImage;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Integer getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Integer isChecked) {
		this.isChecked = isChecked;
	}
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	public Integer getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public BaseILiveThematic() {
		super();
	}
	public BaseILiveThematic(Long thematicId, String thematicName,
			String thematicDesc, String thematicImage, Integer templateId,
			String createName, Timestamp createTime, Integer isChecked,
			Boolean isDelete) {
		super();
		this.thematicId = thematicId;
		this.thematicName = thematicName;
		this.thematicDesc = thematicDesc;
		this.thematicImage = thematicImage;
		this.templateId = templateId;
		this.createName = createName;
		this.createTime = createTime;
		this.isChecked = isChecked;
		this.isDelete = isDelete;
	}
	
}
