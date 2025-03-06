package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.TerminalUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.TerminalUserMng;
@Service
@Transactional
public class TerminalUserMngImpl implements TerminalUserMng {
	@Autowired
	private TerminalUserDao userDao;

	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize) {
		return userDao.getPage(queryNum,pageNo,pageSize);
	}

	@Override
	public ILiveManager getById(Long userId) {
		return userDao.getById(userId);
	}

	@Override
	public void delUser(Long userId) {
		userDao.delUser(userId);
	}

	@Override
	public void updateUser(ILiveManager user) {
		userDao.updateUser(user);
	}

}
