package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseSetup;

public interface ILiveEnterpriseSetupMng {
	
	Long save(ILiveEnterpriseSetup ILiveEnterpriseSetup);
	
	void update(ILiveEnterpriseSetup ILiveEnterpriseSetup);
	
	
	ILiveEnterpriseSetup getById(Long id);
	
	
	ILiveEnterpriseSetup getByEnterpriseId(Integer enterpriseId);
	
	
}
