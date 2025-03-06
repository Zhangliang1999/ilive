package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.AutoLoginDao;
import com.bvRadio.iLive.iLive.entity.AutoLogin;
import com.bvRadio.iLive.iLive.manager.AutoLoginService;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class AutoLoginServiceImpl implements AutoLoginService {
	
	@Autowired
	private AutoLoginDao autoLoginDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Override
	public Integer save(AutoLogin autoLogin) {
		Long nextId = filedIdMng.getNextId("auto_login", "id", 1);
		autoLogin.setAppId(nextId.intValue()+"");
		autoLogin.setIsDel(0);
		return nextId.intValue();
	}

	@Override
	public void update(AutoLogin autoLogin) {
		autoLoginDao.update(autoLogin);
	}

	@Override
	public AutoLogin getById(String id) {
		return autoLoginDao.getById(id);
	}

	@Override
	public AutoLogin getByAppId(String appId) {
		return autoLoginDao.getByAppId(appId);
	}

}
