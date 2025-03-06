package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.AutoLogin;

public interface AutoLoginService {

	Integer save(AutoLogin autoLogin);
	
	void update(AutoLogin autoLogin);
	
	AutoLogin getById(String id);
	
	AutoLogin getByAppId(String appId);
}
