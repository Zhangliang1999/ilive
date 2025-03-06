package com.bvRadio.iLive.manager.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

public interface ILiveEnterpriseMng {
	
	
	public List<ILiveEnterprise> getList();

	/**
	 * 获取企业
	 * @param enterpriseId
	 * @return
	 */
	public ILiveEnterprise getILiveEnterpriseById(Integer enterpriseId);

	public Pagination getPage(String enterprisetype, String content, int pass, Integer pageNo, int pageSize);

	public Pagination getPage(String content, int pass, Integer pageNo, int pageSize);

	public void update(ILiveEnterprise iLiveEnterprise);

	public void del(Integer enterpriseId);

	public boolean saveEnterprise(ILiveEnterprise iLiveEnterprise);

	public boolean saveEnterpriseForPhone(ILiveEnterprise iLiveEnterprise, ILiveManager iLiveManagerRefresh);

	public Integer saveEnterpriseForWeb(ILiveEnterprise iLiveEnterprise, ILiveManager iLiveManagerRefresh);
}
