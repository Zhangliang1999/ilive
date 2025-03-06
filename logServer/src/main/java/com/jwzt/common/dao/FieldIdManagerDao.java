package com.jwzt.common.dao;

public interface FieldIdManagerDao {

	public Long getNextId(String tableName, String fieldName, Long step);

}