package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveFileAuthenticationRecord;

public interface ILiveFileAuthenticationRecordMng {


	ILiveFileAuthenticationRecord addILiveViewAuthBill(ILiveFileAuthenticationRecord fileAuthRecord);

	ILiveFileAuthenticationRecord getILiveFileViewAuthBill(long userId, Long fileId);

	public boolean deleteFileAuthenticationRecordByFileId(Long fileId);

}
