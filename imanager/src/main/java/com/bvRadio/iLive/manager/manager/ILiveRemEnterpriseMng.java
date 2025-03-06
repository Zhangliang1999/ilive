package com.bvRadio.iLive.manager.manager;

import java.util.List;

import com.bvRadio.iLive.manager.entity.ILiveRemEnterprise;

public interface ILiveRemEnterpriseMng {
	
	public List<ILiveRemEnterprise> getPage(String name);

	public void update(ILiveRemEnterprise iLiveRemEnterprise);

	public void del(Long id);

	public void saveEnterprise(ILiveRemEnterprise iLiveRemEnterprise);

	public ILiveRemEnterprise get(Long id);

	public boolean getByEnterpriseId(Integer id);

	public ILiveRemEnterprise getIsEnterpriseRem(Integer enterpriseId);

	public List<ILiveRemEnterprise> getprivacyPage(String name);

}
