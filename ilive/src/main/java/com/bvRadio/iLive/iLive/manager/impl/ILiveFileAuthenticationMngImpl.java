package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveFileAuthenticationDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationMng;

public class ILiveFileAuthenticationMngImpl implements ILiveFileAuthenticationMng {
	
	
	@Autowired
	private ILiveFileAuthenticationDao iLiveFileAuthenticationDao;
	
	@Override
	public ILiveFileAuthentication getFileAuthenticationByFileId(Long fileId) {
		return iLiveFileAuthenticationDao.getFileAuthenticationByFileId(fileId);
	}

	@Override
	@Transactional
	public void addFileAuth(ILiveFileAuthentication fileAuthentication) {
		iLiveFileAuthenticationDao.addFileAuth(fileAuthentication);
	}

	@Override
	@Transactional
	public void updateFileAuth(ILiveFileAuthentication fileAuthentication) {
		iLiveFileAuthenticationDao.updateFileAuth(fileAuthentication);
	}

}
