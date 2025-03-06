package com.bvRadio.iLive.manager.dao;

import java.util.List;

import com.bvRadio.iLive.manager.entity.IliveOperationUser;

public interface ILiveManagerOperatorUserDao {

	/**
	 * 获得用户根据用户名
	 * @param principal
	 * @return
	 */
	public IliveOperationUser getUserByUserName(String principal);

	public IliveOperationUser getUserById(Long userId);

	List<IliveOperationUser> listByParams();
	
	
}
