package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;

public interface SendShortMessaheRecordMng {

	List<SendShortMessaheRecord> getListByRecordId(Long recordId,String mobile);

	Long save(SendShortMessaheRecord sendShortMessaheRecord);
	
	Integer getNumberByEnterpriseId(Integer enterpriseId);

	List<SendShortMessaheRecord> getListByRecord(Long id, String mobile, String start, String stop);
}
