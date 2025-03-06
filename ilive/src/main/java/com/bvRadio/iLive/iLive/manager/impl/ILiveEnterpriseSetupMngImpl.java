package com.bvRadio.iLive.iLive.manager.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseSetupDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseSetup;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseSetupMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
@Service
@Transactional
public class ILiveEnterpriseSetupMngImpl implements ILiveEnterpriseSetupMng {

	@Autowired
	private ILiveEnterpriseSetupDao managerDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	

	@Override
	public Long save(ILiveEnterpriseSetup ILiveEnterpriseSetup) {
		Long nextId = filedIdMng.getNextId("ilive_enterprise_setup", "id", 1);
		ILiveEnterpriseSetup.setId(nextId);
		
		managerDao.save(ILiveEnterpriseSetup);
		return nextId;
	}

	@Override
	public void update(ILiveEnterpriseSetup ILiveEnterpriseSetup) {
		managerDao.update(ILiveEnterpriseSetup);
	}


	@Override
	public ILiveEnterpriseSetup getById(Long id) {
		ILiveEnterpriseSetup doc = managerDao.getById(id);
		if(doc==null) {
			return null;
		}
		
		return doc;
	}

	

	

	@Override
	public ILiveEnterpriseSetup getByEnterpriseId(Integer enterpriseId) {
		return managerDao.getByEnterpriseId(enterpriseId);
	}



}
