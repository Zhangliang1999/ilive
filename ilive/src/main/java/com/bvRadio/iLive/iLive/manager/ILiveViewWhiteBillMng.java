package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;

/**
 * 直播间观看白名单
 * @author administrator
 *
 */
public interface ILiveViewWhiteBillMng {

	boolean checkUserInWhiteList(String userId, Long liveEventId);

	List<ILiveViewWhiteBill> getAllViewWhiteBill(Long liveEventId);

	List<ILiveViewWhiteBill> distinctUserPhone(String[] phoneNums, Long enterpriseId);
	
	void batchInsertUserPhone(List<ILiveViewWhiteBill> billList);

	void saveIliveViewWhiteBill(ILiveViewWhiteBill bill);

	List<AppILiveRoom> getMyInviteLive(Long userId, String phoneNum,int pageNo, int pageSize);

	void batchInsertUser(List<Object[]> readXlsx,Long iliveEventId);
    void deleteById(String phoneNum,Long liveEventId);
    List<ILiveViewWhiteBill> getAllViewWhiteBilll(String queryNum,Long liveEventId);
	Pagination getPage(String queryNum, Integer pageNo, Integer pageSize, Long iLiveEventId);

	void updateEventId(long oldLiveEventId, Long saveIliveMng);

}
