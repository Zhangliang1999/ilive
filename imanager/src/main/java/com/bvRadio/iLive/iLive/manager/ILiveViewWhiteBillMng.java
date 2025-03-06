package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;

/**
 * 直播间观看白名单
 * @author administrator
 *
 */
public interface ILiveViewWhiteBillMng {

	boolean checkUserInWhiteList(String userId, Long liveEventId);

	List<ILiveViewWhiteBill> getAllViewWhiteBill(Long liveEventId);

}
