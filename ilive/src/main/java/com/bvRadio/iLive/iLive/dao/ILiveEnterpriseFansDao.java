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

	public List<Integer> getMyEnterprise(Long userId);

	public Pagination getIliveEnterPriseByUserId(Long userId, int pageNo, int pageSize);

	public Long getTotalConcernNum(Integer enterpriseId);

	public void updateEnterFans(ILiveEnterpriseFans entFans);

	public List<ILiveEnterpriseFans> getEnterpriseListByTop(Long userId);
	
	public List<Long> getListByEnterpriseId(Integer enterpriseId);

	public boolean isblack(long userId, Integer enterpriseId);

	public Pagination getPage(String queryNum, Integer enterpriseId, Integer pageNo, int i);

	public Pagination getPageBlack(String queryNum, Integer enterpriseId, Integer pageNo, Integer pageSize);

	public Pagination getIliveEnterPriseByUserId(Long userId, Integer pageNo, Integer pageSize, String keyWord);

	Pagination getIliveEnterPriseByUserId1(Long userId, Integer pageNo, Integer pageSize, String keyWord);

	
}
