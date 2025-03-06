package com.jwzt.common.manager;

public interface FieldIdManagerMng {
	/**
	 * 获取可分配的主键ID
	 * 
	 * @param tableName
	 *            表名
	 * @param fieldName
	 *            字段名
	 * @param step
	 *            步长，即去多少个可分配主键
	 * @return
	 */
	public Long getNextId(String tableName, String fieldName, Long step);

}
