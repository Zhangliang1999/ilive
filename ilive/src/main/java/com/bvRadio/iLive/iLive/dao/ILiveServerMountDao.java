package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.MountInfo;

public interface ILiveServerMountDao {
	
	
	
	/**
	 * 根据ID获得发布点
	 * @param mountId
	 * @return
	 */
	public MountInfo getMountInfoById(Integer mountId);
	
	
	

}
