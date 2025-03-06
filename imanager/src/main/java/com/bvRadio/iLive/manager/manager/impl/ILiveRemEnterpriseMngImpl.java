package com.bvRadio.iLive.manager.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.manager.dao.ILiveRemEnterpriseDao;
import com.bvRadio.iLive.manager.entity.ILiveRemEnterprise;
import com.bvRadio.iLive.manager.manager.ILiveRemEnterpriseMng;

@Service
@Transactional
public class ILiveRemEnterpriseMngImpl implements ILiveRemEnterpriseMng {

	@Autowired
	private ILiveRemEnterpriseDao dao;
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	

	@Override
	public void update(ILiveRemEnterprise iLiveEnterprise) {
		dao.update(iLiveEnterprise);
	}

	@Override
	public void del(Long enterpriseId) {
		dao.del(enterpriseId);
	}

	@Override
	public void saveEnterprise(ILiveRemEnterprise iLiveEnterprise) {
		Long nextId = filedIdMng.getNextId("ilive_rem_enterprise", "enterprise_id", 1);
		iLiveEnterprise.setId(nextId);
		iLiveEnterprise.setOrderNum(nextId);
		dao.saveEnterprise(iLiveEnterprise);
	}

	@Override
	public List<ILiveRemEnterprise> getPage(String name) {
		return dao.getPage(name);
	}

	@Override
	public ILiveRemEnterprise get(Long id) {
		
		return dao.get(id);
	}

	@Override
	public boolean getByEnterpriseId(Integer id) {
		
		return dao.getByEnterpriseId(id);
	}

	@Override
	public ILiveRemEnterprise getIsEnterpriseRem(Integer enterpriseId) {
		
		return dao.getIsEnterpriseRem(enterpriseId);
	}

	@Override
	public List<ILiveRemEnterprise> getprivacyPage(String name) {
		// TODO Auto-generated method stub
		return dao.getprivacyPage(name);
	}

	



}
