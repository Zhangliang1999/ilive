package com.bvRadio.iLive.iLive.manager.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseMemberDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class ILiveEnterpriseMemberMngImpl implements ILiveEnterpriseMemberMng {

	@Autowired
	private ILiveEnterpriseMemberDao iliveEnterpriseDao;
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Override
	public Pagination getPage(String queryName, Integer pageNo, Integer pageSize,Integer enterpriseId) {
		return iliveEnterpriseDao.getPage(queryName,pageNo,pageSize,enterpriseId);
	}

	@Override
	public void deleteWhite(Integer enterpriseId) {
		iliveEnterpriseDao.deleteWhite(enterpriseId);
	}

	@Override
	public void addWhite(ILiveEnterpriseWhiteBill white) {
		Integer nextId = filedIdMng.getNextId("ilive_enterprise_whitebill", "whitebill_id", 1).intValue();
		white.setWhitebillId(nextId);
		iliveEnterpriseDao.addWhite(white);
	}

}
