package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseSetup;

public interface ILiveEnterpriseSetupDao {

	
	void save(ILiveEnterpriseSetup ILiveEnterpriseSetup);
	
	void update(ILiveEnterpriseSetup ILiveEnterpriseSetupo);
	
	
	ILiveEnterpriseSetup getById(Long id);

	
	ILiveEnterpriseSetup getByEnterpriseId(Integer enterpriseId);
	
}
