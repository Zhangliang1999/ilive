package com.jwzt.common.entity.bo;

/**
 * @author ysf
 */
public class CacheFieldIdManager {
	private String tableName;
	private String fieldName;
	private Long nextId;
	private Long maxId;

	public CacheFieldIdManager(String tableName, String fieldName, Long nextId, Long maxId) {
		super();
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.nextId = nextId;
		this.maxId = maxId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Long getNextId() {
		return nextId;
	}

	public void setNextId(Long nextId) {
		this.nextId = nextId;
	}

	public Long getMaxId() {
		return maxId;
	}

	public void setMaxId(Long maxId) {
		this.maxId = maxId;
	}

}
