package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveUserEntity;

public interface ILiveUserMng {

	ILiveUserEntity findById(Integer uid);

	void deleteById(Integer id);

	void save(ILiveUserEntity user);

	void updateUser(ILiveUserEntity liveUser);

}
