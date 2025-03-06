package com.bvRadio.iLive.iLive.manager;

public interface ILiveFieldIdManagerMng {

	public Long getNextId(String tableName, String fieldName, Integer step);

}
