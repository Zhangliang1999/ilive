package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;

public interface ILiveEnterpriseFansMng {

	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize);

	public Pagination getPageBlack(String queryNum, Integer pageNo, Integer pageSize);

	public boolean addEnterpriseConcern(Integer enterpriseId, Long userId);

	public ILiveEnterpriseFans findEnterpriseFans(Integer enterpriseId, Long userId);

	public boolean isExist(Integer enterpriseId, Long userId);

	public void delFans(Long id);

	public void letblack(Long id);

	public int getFansNum(Integer enterpriseId);

	public void deleteEnterpriseConcern(Integer enterpriseId, Long userId);

	public void deleteEnterpriseConcern(ILiveEnterpriseFans fans);
	
	public void updateBatchEnterpriseState(Integer enterpriseId);
	
	Pagination getPageByUserId(Long userId,Integer pageNo,Integer pageSize);
	
	List<ILiveEnterpriseFans> getListByUserId(Long userId);
}
