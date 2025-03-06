package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveServer;

/**
 * 直播服务器管理类
 * @author administrator
 */
public interface ILiveServerDao {

	/**
	 * 直播服务器
	 * @param serverId
	 * @return
	 */
	ILiveServer getILiveServer(Integer serverId);
	

}
