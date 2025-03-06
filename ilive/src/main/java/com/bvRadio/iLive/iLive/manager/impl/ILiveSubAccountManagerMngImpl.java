package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSubAccountManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveSubAccountManager;
import com.bvRadio.iLive.iLive.manager.ILiveSubAccountManagerMng;
@Service
@Transactional
public class ILiveSubAccountManagerMngImpl implements ILiveSubAccountManagerMng {
	@Autowired
	private ILiveSubAccountManagerDao iLiveSubAccountManagerDao;
	
	/**
	 * 日志
	 */
	Logger logger = LoggerFactory.getLogger(ILiveSubAccountManagerMngImpl.class);
	@Override
	public Pagination selectSubAccountManagerPage(Integer pageNo, Integer pageSize, Integer enterpriseId)
			throws Exception {
		// TODO Auto-generated method stub
		return iLiveSubAccountManagerDao.selectSubAccountManagerPage(pageNo, pageSize, enterpriseId);
	}
	@Override
	public void addILiveSubAccountMng(ILiveSubAccountManager manager) throws Exception {
		iLiveSubAccountManagerDao.addILiveSubAccountMng(manager);
		
	}
	@Override
	public List<ILiveSubAccountManager> getILiveManagerPage(String user) {
		// TODO Auto-generated method stub
		return iLiveSubAccountManagerDao.getILiveManagerPage(user);
	}
	@Override
	public void updateILiveSubAccountMng(ILiveSubAccountManager manager) throws Exception {
		iLiveSubAccountManagerDao.updateILiveSubAccountMng(manager);
		
	}
	@Override
	public Long selectMaxId() {
		// TODO Auto-generated method stub
		return iLiveSubAccountManagerDao.selectMaxId();
	}
	@Override
	public ILiveSubAccountManager getILiveSubAccountManager(String enterpriseId) {
		// TODO Auto-generated method stub
		return iLiveSubAccountManagerDao.getILiveSubAccountManager(enterpriseId);
	}
	

	
}
