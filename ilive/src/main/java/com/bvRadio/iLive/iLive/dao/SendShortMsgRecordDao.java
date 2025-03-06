package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;
import com.bvRadio.iLive.iLive.entity.SendShortMsgRecode;

public interface SendShortMsgRecordDao {

	List<SendShortMsgRecode> getListByMobile(String mobile);
	
	void save(SendShortMsgRecode sendShortMsgRecode);
	void update(SendShortMsgRecode sendShortMsgRecode);
}
