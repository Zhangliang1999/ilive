package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;

public interface ILiveEnterpriseFansDao {
	
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize);

	public Pagination getPageBlack(String queryNum, Integer pageNo, Integer pageSize);

	public ILiveEnterpriseFans findEnterpriseFans(Integer enterpriseId, Long userId);

	public void addEnterpriseConcern(ILiveEnterpriseFans fans);

	public boolean isExist(Integer enterpriseId, Long userId);

	public void delFans(Long id);

	public void letblack(Long id);

	public int getFansNum(Integer enterpriseId);

	public void deleteEnterpriseConcern(ILiveEnterpriseFans fans);

	public List<ILiveEnterpriseFans> getEnterpriseFansById(Integer enterpriseId);

	public void batchUpdateFans(List<ILiveEnterpriseFans> enterpriseList);
	
	public Pagination getPageByUserId(Long userId, Integer pageNo, Integer pageSize);
	
	public List<ILiveEnterpriseFans> getListByUserId(Long userId);
}
