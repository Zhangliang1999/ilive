package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveFileAuthenticationRecordDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthenticationRecord;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationRecordMng;

public class ILiveFileAuthenticationRecordMngImpl implements ILiveFileAuthenticationRecordMng {

	@Autowired
	private ILiveFileAuthenticationRecordDao iLiveFileAuthenticationRecordDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	@Override
	@Transactional
	public ILiveFileAuthenticationRecord addILiveViewAuthBill(ILiveFileAuthenticationRecord fileAuthRecord) {
		Long nextId = iLiveFieldIdManagerMng.getNextId("ilive_file_auth_record", "record_id", 1);
		fileAuthRecord.setRecordId(nextId);
		iLiveFileAuthenticationRecordDao.addILiveViewAuthBill(fileAuthRecord);
		return fileAuthRecord;
	}

	@Override
	public ILiveFileAuthenticationRecord getILiveFileViewAuthBill(long userId, Long fileId) {
		return iLiveFileAuthenticationRecordDao.getILiveFileViewAuthBill(userId, fileId);
	}

	@Override
	public boolean deleteFileAuthenticationRecordByFileId(Long fileId) {
		// TODO Auto-generated method stub
		return iLiveFileAuthenticationRecordDao.deleteFileAuthenticationRecordByFileId(fileId);
	}

}
