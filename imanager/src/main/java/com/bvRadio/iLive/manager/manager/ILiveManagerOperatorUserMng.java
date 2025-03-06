package com.bvRadio.iLive.manager.manager;

import java.util.List;

import com.bvRadio.iLive.manager.entity.IliveOperationUser;

public interface ILiveManagerOperatorUserMng {

	
	/**
	 * 根据用户名获得操作用户
	 * @param principal
	 * @return
	 */
	public IliveOperationUser getUserByUserName(String principal);

	public IliveOperationUser getUserById(Long userId);
	
	List<IliveOperationUser> listByParams();
	

}
