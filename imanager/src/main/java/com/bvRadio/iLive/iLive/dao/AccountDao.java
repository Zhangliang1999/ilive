package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

public interface AccountDao {
	public void saveaccount(ILiveEnterprise iLiveEnterprise);
	public ILiveEnterprise getEnterpriseById(Integer enterpriseId);
}
