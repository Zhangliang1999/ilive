package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;

public interface SendShortMessaheRecordDao {

	List<SendShortMessaheRecord> getListByRecordId(Long recordId,String mobile);
	
	void save(SendShortMessaheRecord sendShortMessaheRecord);
	
	Integer getNumberByEnterpriseId(Integer enterpriseId);

	List<SendShortMessaheRecord> getListByRecord(Long id, String mobile, String start, String stop);
}
