package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;

public interface ILivePrizeDao {

	public Pagination getPage(String prizeName, Integer roomId,Integer pageNo, Integer pageSize);
	
	Pagination getPageByEnterpriseId(String prizeName, Integer enterpriseId, Integer pageNo, Integer pageSize);
	
	public void save(ILiveLottery lottery);
	
	public ILiveLottery getPrize(Long id);
	
	public void setLetEnd();
	
	public void update(ILiveLottery lottery);
	
	public List<ILiveLottery> getListByRoomId(Integer roomId);
	
	public List<ILiveLottery> getListUserH5ByRoomId(Integer roomId);
	public List<ILiveLottery> getListUserH5ByEnterpriseId(Integer enterpriseId);
	
	public void letClose(Integer roomId);
	
	public ILiveLottery isStartPrize(Integer roomId);
	
	ILiveLottery isEnterpriseStartPrize(Integer enterpriseId);
	
	ILiveLottery isStartPrize2(Integer enterpriseId);
	
	List<ILiveLottery> getListUserH5ByUserId(Long userId);
	
	Pagination getpageByUserId(Long userId, String prizeName, Integer pageNo, Integer pageSize);
}
