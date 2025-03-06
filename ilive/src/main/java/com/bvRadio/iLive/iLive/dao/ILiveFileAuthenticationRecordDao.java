package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveFileAuthenticationRecord;

public interface ILiveFileAuthenticationRecordDao {

	ILiveFileAuthenticationRecord getILiveFileViewAuthBill(long useId, Long fileId);

	void addILiveViewAuthBill(ILiveFileAuthenticationRecord fileAuthRecord);

	boolean deleteFileAuthenticationRecordByFileId(Long fileId);

}
