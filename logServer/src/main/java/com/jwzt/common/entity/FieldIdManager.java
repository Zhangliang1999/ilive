package com.jwzt.common.entity;

import com.jwzt.common.entity.base.BaseFieldIdManager;

@SuppressWarnings("serial")
public class FieldIdManager extends BaseFieldIdManager implements java.io.Serializable {

	public FieldIdManager() {
	}

	public FieldIdManager(Integer id, String tableName, String fieldName) {
		super(id, tableName, fieldName);
	}

}
