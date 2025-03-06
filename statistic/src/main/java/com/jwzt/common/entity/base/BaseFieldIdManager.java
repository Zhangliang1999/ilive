package com.jwzt.common.entity.base;

@SuppressWarnings("serial")
public abstract class BaseFieldIdManager implements java.io.Serializable {

	private Integer id;
	private String tableName;
	private String fieldName;
	private Long nextId;

	public BaseFieldIdManager() {
	}

	public BaseFieldIdManager(Integer id, String tableName, String fieldName) {
		this.id = id;
		this.tableName = tableName;
		this.fieldName = fieldName;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Long getNextId() {
		return this.nextId;
	}

	public void setNextId(Long nextId) {
		this.nextId = nextId;
	}

}