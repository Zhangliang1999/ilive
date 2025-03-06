package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.SendShortMessaheRecordDao;
import com.bvRadio.iLive.iLive.dao.SendShortMsgRecordDao;
import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;
import com.bvRadio.iLive.iLive.entity.SendShortMsgRecode;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.SendShortMessaheRecordMng;
import com.bvRadio.iLive.iLive.manager.SendShortMsgRecordMng;

@Service
@Transactional
public class SendShortMsgRecordMngImpl implements SendShortMsgRecordMng {

	@Autowired
	private SendShortMsgRecordDao sendShortMsgRecordDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public List<SendShortMsgRecode> getListByMobile(String mobile) {
		return sendShortMsgRecordDao.getListByMobile(mobile);
	}

	@Override
	public void save(SendShortMsgRecode sendShortMsgRecode) {
		Long nextId = fieldIdMng.getNextId("send_shortmsg_record", "id", 1);
		sendShortMsgRecode.setId(nextId);
		sendShortMsgRecordDao.save(sendShortMsgRecode);
	}

	@Override
	public void update(SendShortMsgRecode sendShortMsgRecode) {
		// TODO Auto-generated method stub
		sendShortMsgRecordDao.update(sendShortMsgRecode);
	}

	

}
