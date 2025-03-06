package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveViewSetRecordDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewSetRecord;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewSetRecordMng;

@Service
public class ILiveViewSetRecordMngImpl implements ILiveViewSetRecordMng{

	@Autowired
	private ILiveViewSetRecordDao iLiveViewSetRecordDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Override
	@Transactional
	public Integer saveRecord(ILiveViewSetRecord iLiveViewSetRecord) {
		Long nextId = filedIdMng.getNextId("ilive_viewset_record", "id", 1);
		Timestamp createTime = new Timestamp(new Date().getTime());
		
		iLiveViewSetRecord.setId(nextId);
		iLiveViewSetRecord.setUpdateTime(createTime);
		iLiveViewSetRecordDao.saveRecord(iLiveViewSetRecord);
		return nextId.intValue();
	}

}
