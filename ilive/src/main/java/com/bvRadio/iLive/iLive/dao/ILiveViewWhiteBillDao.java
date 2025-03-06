package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;

public interface ILiveViewWhiteBillDao {

	boolean checkUserInWhiteList(String userId, Long liveEventId);

	List<ILiveViewWhiteBill> getAllViewWhiteBill(Long liveEventId);

	List<String> distinctUserPhone(String[] phoneNums, Long liveEventId);

	void batchInsertUserPhone(List<ILiveViewWhiteBill> distinctUserPhone);
	
	void saveIliveViewWhiteBill(ILiveViewWhiteBill bill);
	
	void clearViewWhiteBill(Long liveEventId);

	Pagination getMyInviteLive(Long userId,String phoneNum, int pageNo, int pageSize);
	void deleteById(String phoneNum,Long liveEventId);
	Pagination getPage(String queryNum, Integer pageNo, Integer pageSize, Long iLiveEventId);
	List<ILiveViewWhiteBill> getAllViewWhiteBilll(String queryNum,Long liveEventId);
	void updateEventId(long oldLiveEventId, Long saveIliveMng);

}
