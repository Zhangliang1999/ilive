package com.jwzt.common.manager;

public interface FieldIdManagerMng {

	public Long getNextId(String tableName, String fieldName, Long step);

}
