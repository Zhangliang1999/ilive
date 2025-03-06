package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveUserEntity;

public interface ILiveUserDao {
	
	ILiveUserEntity findById(Integer uid);

	void deleteById(Integer id);

	void save(ILiveUserEntity user);

	void updateUser(ILiveUserEntity liveUser);

}
