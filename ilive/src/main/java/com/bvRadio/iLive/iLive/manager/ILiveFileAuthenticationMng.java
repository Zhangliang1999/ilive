package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;

public interface ILiveFileAuthenticationMng {

	ILiveFileAuthentication getFileAuthenticationByFileId(Long fileId);

	void addFileAuth(ILiveFileAuthentication fileAuthentication);

	void updateFileAuth(ILiveFileAuthentication fileAuthentication);

}
