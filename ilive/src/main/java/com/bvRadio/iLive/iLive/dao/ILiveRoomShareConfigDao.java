package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;

public interface ILiveRoomShareConfigDao {

	/**
	 * 获取直播间配置
	 * @param roomId
	 * @return
	 */
	List<ILiveRoomShareConfig> getShareConfig(Integer roomId);

	void addIliveRoomShareConfig(ILiveRoomShareConfig shareConfig);

	void updateConfigDao(ILiveRoomShareConfig configShare);
	
	
	ILiveRoomShareConfig getConfigShare(Long circleId);

}
