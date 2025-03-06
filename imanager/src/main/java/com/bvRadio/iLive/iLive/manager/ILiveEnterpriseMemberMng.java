package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;

public interface ILiveEnterpriseMemberMng {

	public Pagination getPage(String queryName,Integer pageNo, Integer pageSize,Integer enterpriseId);
	public void deleteWhite(Integer whitebillId);
	public void addWhite(ILiveEnterpriseWhiteBill white);
}
