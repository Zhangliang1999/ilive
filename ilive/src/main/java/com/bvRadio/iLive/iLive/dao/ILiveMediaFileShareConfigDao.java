package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveMediaFileShareConfig;

public interface ILiveMediaFileShareConfigDao {

	/**
	 * 获取直播间配置
	 * @param fileId
	 * @return
	 */
	List<ILiveMediaFileShareConfig> getShareConfig(Integer fileId);

	void addIliveMediaFileShareConfig(ILiveMediaFileShareConfig shareConfig);

	void updateConfigDao(ILiveMediaFileShareConfig configShare);
	
	
	ILiveMediaFileShareConfig getConfigShare(Long circleId);

	Long selectMaxId();

}
