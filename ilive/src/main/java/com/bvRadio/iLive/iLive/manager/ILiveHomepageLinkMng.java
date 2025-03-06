package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageLink;

public interface ILiveHomepageLinkMng {

	public ILiveHomepageLink save(ILiveHomepageLink link);
	
	public ILiveHomepageLink getById(Long id);
	
	public void update(ILiveHomepageLink link);
	
	public Pagination getPage(Integer roomId,Integer pageNo, Integer pageSize);
	
	public List<ILiveHomepageLink> getListByEnterpriseId(Integer enterpriseId);
}
