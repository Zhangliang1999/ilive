package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveFieldIdManager;

@SuppressWarnings("serial")
public class ILiveFieldIdManager extends BaseILiveFieldIdManager implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public ILiveFieldIdManager() {
	}

	/** minimal constructor */
	public ILiveFieldIdManager(Integer id, String tableName, String fieldName) {
		super(id, tableName, fieldName);
	}

	/** full constructor */
	public ILiveFieldIdManager(Integer id, String tableName, String fieldName, Long nextId, Integer maxId) {
		super(id, tableName, fieldName, nextId, maxId);
	}

}
