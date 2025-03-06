package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveServerDao;
import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;
import com.bvRadio.iLive.iLive.entity.ILiveServer;
import com.bvRadio.iLive.iLive.manager.ILiveServerMng;


/**
 * @author administrator
 * 直播服务管理类
 */
public class ILiveServerMngImpl implements ILiveServerMng {
	
	@Autowired
	private ILiveServerDao iLiveServerDao;

	@Override
	public ILiveServer getILiveServer(Integer serverId) {
		return iLiveServerDao.getILiveServer(serverId);
	}
}
