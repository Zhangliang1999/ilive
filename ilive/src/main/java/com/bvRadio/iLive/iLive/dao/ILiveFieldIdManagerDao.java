package com.bvRadio.iLive.iLive.dao;

public interface ILiveFieldIdManagerDao {

	public Long getNextId(String tableName, String fieldName, Integer step);

}