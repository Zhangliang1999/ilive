package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.AccountDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.manager.AccountMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class AccountMngImpl implements AccountMng {

	@Autowired
	private AccountDao accountDao;
	/*@Autowired
	private ILiveFieldIdManagerMng filedIdMng;*/
	
	@Override
	public void saveaccount(ILiveEnterprise iLiveEnterprise) {
		accountDao.saveaccount(iLiveEnterprise);
	}

	@Override
	public ILiveEnterprise getEnterpriseById(Integer enterpriseId) {
		return accountDao.getEnterpriseById(enterpriseId);
	}

}
