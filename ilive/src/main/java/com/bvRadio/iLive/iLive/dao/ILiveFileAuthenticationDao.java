package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;

public interface ILiveFileAuthenticationDao {

	ILiveFileAuthentication getFileAuthenticationByFileId(Long fileId);

	void addFileAuth(ILiveFileAuthentication fileAuthentication);

	void updateFileAuth(ILiveFileAuthentication fileAuthentication);

}
