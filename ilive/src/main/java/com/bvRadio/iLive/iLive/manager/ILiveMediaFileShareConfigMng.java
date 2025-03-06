package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveMediaFileShareConfig;
import com.bvRadio.iLive.iLive.entity.vo.ILiveShareInfoVo;

public interface ILiveMediaFileShareConfigMng {
	
	
	/**
	 * 文件ID
	 * @param fileId
	 * @return
	 */
	public List<ILiveMediaFileShareConfig> getShareConfig(Integer fileId);
	
	
	/**
	 * 直播
	 * @return 
	 */
	public List<ILiveMediaFileShareConfig> addIliveMediaFileShareConfig(ILiveMediaFileShareConfig shareConfig);


	
	/**
	 * 修改配置
	 * @param iLiveShareInfoVo
	 */
	public void updateConfigShare(ILiveShareInfoVo iLiveShareInfoVo);


	public void updateShare(ILiveMediaFileShareConfig share);
	
	public Long selectMaxId();
	

}
