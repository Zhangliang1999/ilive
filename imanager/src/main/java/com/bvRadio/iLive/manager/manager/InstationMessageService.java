package com.bvRadio.iLive.manager.manager;

import java.sql.Timestamp;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.entity.InstationMessage;

public interface InstationMessageService {

	Long save(InstationMessage instationMessage);
	
	Pagination getPage(Long id,Long userId,Timestamp startTime,Timestamp endTime,Integer pageNo,Integer pageSize);
	
	void update(InstationMessage instationMessage);
	
	void delete(Long id);
	
	InstationMessage getById(Long id);
	
}
