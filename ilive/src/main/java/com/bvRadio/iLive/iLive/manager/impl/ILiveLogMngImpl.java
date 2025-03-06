package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRecordDao;
import com.bvRadio.iLive.iLive.dao.ILiveLiveReportDao;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.dao.ILiveLogDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRecord;
import com.bvRadio.iLive.iLive.entity.ILiveLiveReport;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveLog;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRecordMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveReportMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveLogMng;

@Service
@Transactional
public class ILiveLogMngImpl implements ILiveLogMng {

	@Override
	@Transactional(readOnly = true)
	public ILiveLog findById(Integer id) {
		ILiveLog live = dao.findById(id);
		return live;
	}

	@Override
	public ILiveLog save(ILiveLog bean) {
		long nextId=-1;
		if(bean.getId()!=null){
			nextId=bean.getId();
		}else{
			nextId = iLiveFieldIdManagerMng.getNextId("ilive_log", "id", 1);
		}
		if (nextId != -1) {
			bean.setId((int)nextId);
			dao.save(bean);
		}
		return bean;
	}

	@Override
	public ILiveLog deleteById(Integer liveId) {
		ILiveLog live = dao.deleteById(liveId);
		return live;
	}
	@Override
	public ILiveLog update(ILiveLog bean) {
		Updater<ILiveLog> updater = new Updater<ILiveLog>(bean);
		ILiveLog live = dao.updateByUpdater(updater);
		return live;
		
	}		
	@Autowired
	private ILiveLogDao dao;
	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;
	



	


}
