package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

public interface AccountMng {
	public void saveaccount(ILiveEnterprise iLiveEnterprise);
	public ILiveEnterprise getEnterpriseById(Integer enterpriseId);
}
