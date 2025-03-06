package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public abstract class BaseILiveSensitiveWord implements java.io.Serializable {

	/*
	 * 主键id
	 */
	private Integer id;
	/*
	 * 敏感词名称
	 */
	private String sensitiveName;
	/*
	 * 敏感词描述
	 */
	private String sensitiveDesc;
	/*
	 * 敏感词创建时间
	 */
	private Timestamp createTime;
	/*
	 * 敏感词状态
	 */
	private Boolean disabled;
	
	/*
	 * 敏感词类型 1、暴恐词库  2、反动词库  3、民生词库   4、色情词库   5、贪腐词库   6、其他
	 */
	private Integer type;

	public BaseILiveSensitiveWord() {
	}

	public BaseILiveSensitiveWord(Integer id) {
		this.id = id;
	}

	public BaseILiveSensitiveWord(Integer id, String sensitiveName, String sensitiveDesc, Timestamp createTime) {
		super();
		this.id = id;
		this.sensitiveName = sensitiveName;
		this.sensitiveDesc = sensitiveDesc;
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSensitiveName() {
		return sensitiveName;
	}

	public void setSensitiveName(String sensitiveName) {
		this.sensitiveName = sensitiveName;
	}

	public String getSensitiveDesc() {
		return sensitiveDesc;
	}

	public void setSensitiveDesc(String sensitiveDesc) {
		this.sensitiveDesc = sensitiveDesc;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}