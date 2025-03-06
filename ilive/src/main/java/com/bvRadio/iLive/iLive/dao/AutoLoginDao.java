package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.AutoLogin;

public interface AutoLoginDao {

	void save(AutoLogin autoLogin);
	
	void update(AutoLogin autoLogin);
	
	AutoLogin getById(String id);
	
	AutoLogin getByAppId(String appId);
}
