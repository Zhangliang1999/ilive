package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;

public interface ILiveViewWhiteBillDao {

	boolean checkUserInWhiteList(String userId, Long liveEventId);

	List<ILiveViewWhiteBill> getAllViewWhiteBill(Long liveEventId);

}
