package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

public interface ILiveEnterpriseMng {

	public boolean saveILiveEnterprise(ILiveEnterprise iLiveEnterprise);

	/**
	 * 获得企业
	 * 
	 * @param enterpriseId
	 * @return
	 */
	public ILiveEnterprise getILiveEnterPrise(Integer enterpriseId);

	public ILiveEnterprise getdefaultEnterprise();

	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType);

}
