package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveUserEntity;
import com.bvRadio.iLive.iLive.manager.ILiveUserMng;


@Service
@Transactional
public class ILiveUserMngImpl implements ILiveUserMng {

	@Override
	@Transactional(readOnly = true)
	public ILiveUserEntity findById(Integer uid) {
		return iLiveUserDao.findById(uid);
	}

	@Override
	public void deleteById(Integer id) {
		iLiveUserDao.deleteById(id);
	}

	@Override
	public void save(ILiveUserEntity user) {
		iLiveUserDao.save(user);
	}
	
	@Override
	public void updateUser(ILiveUserEntity liveUser) {
		iLiveUserDao.updateUser(liveUser);
	}
	
	@Autowired
	private ILiveUserDao iLiveUserDao;


	

}
