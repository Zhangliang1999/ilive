package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;

public interface ILiveEnterpriseMemberDao {

	public Pagination getPage(String queryName, Integer pageNo,Integer pageSize,Integer enterpriseId);
	public void deleteWhite(Integer enterpriseId);
	public void addWhite(ILiveEnterpriseWhiteBill white) ;
}
