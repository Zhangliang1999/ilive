package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryParktake;

public interface ILiveLotteryParktakeDao {

	public Pagination getPage(Integer isPrize, String search,Long lotteryId, Integer pageNo, Integer pageSize);

	public List<ILiveLotteryParktake> getList(Integer isPrize, String search, Long lotteryId);
	public List<ILiveLotteryParktake> getListByLotteryIdAndPhone(Long lotteryId, String phone);
	
	public void saveUser(ILiveLotteryParktake iLiveLotteryParktake);
	
}
