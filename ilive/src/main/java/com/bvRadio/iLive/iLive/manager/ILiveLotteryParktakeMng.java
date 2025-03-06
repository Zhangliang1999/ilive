package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryParktake;

public interface ILiveLotteryParktakeMng {

	public Pagination getPage(Integer isPrize,String search,Long lotteryId,Integer pageNo,Integer pageSize);

	public List<ILiveLotteryParktake> getList(Integer isPrize,String search,Long lotteryId);
	
	public List<ILiveLotteryParktake> getListByLotteryIdAndPhone(Long lotteryId,String phone);
	
	/**
	 * 保存抽奖用户信息
	 * @param iLiveLotteryParktake
	 * @return
	 */
	public Long saveUser(ILiveLotteryParktake iLiveLotteryParktake);
	
}
