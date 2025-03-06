package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;
import com.bvRadio.iLive.iLive.entity.vo.ILiveShareInfoVo;

public interface ILiveRoomShareConfigMng {
	
	
	/**
	 * 直播间ID
	 * @param roomId
	 * @return
	 */
	public List<ILiveRoomShareConfig> getShareConfig(Integer roomId);
	
	
	/**
	 * 直播
	 * @return 
	 */
	public List<ILiveRoomShareConfig> addIliveRoomShareConfig(ILiveRoomShareConfig shareConfig);


	
	/**
	 * 修改配置
	 * @param iLiveShareInfoVo
	 */
	public void updateConfigShare(ILiveShareInfoVo iLiveShareInfoVo);


	public void updateShare(ILiveRoomShareConfig share);
	
	
	

}
