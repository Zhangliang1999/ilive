package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.SendShortMessaheRecordDao;
import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.SendShortMessaheRecordMng;

@Service
@Transactional
public class SendShortMessaheRecordMngImpl implements SendShortMessaheRecordMng {

	@Autowired
	private SendShortMessaheRecordDao sendShortMessaheRecordDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public List<SendShortMessaheRecord> getListByRecordId(Long recordId,String mobile) {
		return sendShortMessaheRecordDao.getListByRecordId(recordId,mobile);
	}

	@Override
	public Long save(SendShortMessaheRecord sendShortMessaheRecord) {
		Long nextId = fieldIdMng.getNextId("send_shortmessahe_record", "id", 1);
		sendShortMessaheRecord.setId(nextId);
		sendShortMessaheRecord.setCreateTime(new Timestamp(new Date().getTime()));
		sendShortMessaheRecordDao.save(sendShortMessaheRecord);
		return nextId;
	}

	@Override
	public Integer getNumberByEnterpriseId(Integer enterpriseId) {
		return sendShortMessaheRecordDao.getNumberByEnterpriseId(enterpriseId);
	}

	@Override
	public List<SendShortMessaheRecord> getListByRecord(Long id, String mobile, String start, String stop) {
		// TODO Auto-generated method stub
		return sendShortMessaheRecordDao.getListByRecord(id,mobile,start,stop);
	}

}
