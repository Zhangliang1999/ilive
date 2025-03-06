package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;
import com.bvRadio.iLive.iLive.entity.SendShortMsgRecode;

public interface SendShortMsgRecordMng {

	List<SendShortMsgRecode> getListByMobile(String mobile);

	void save(SendShortMsgRecode sendShortMsgRecode);
	
	void update(SendShortMsgRecode sendShortMsgRecode);
	
}
