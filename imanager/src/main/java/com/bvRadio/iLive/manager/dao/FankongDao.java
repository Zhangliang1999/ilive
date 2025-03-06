package com.bvRadio.iLive.manager.dao;

import java.sql.Timestamp;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.entity.CSAndYellow;

public interface FankongDao {

	List<CSAndYellow> getList(Integer roomId);
	
	CSAndYellow getById(String id);
	
	Pagination getPage(Integer roomId,Timestamp checkTime,Integer monitorLevel,Integer pageNo,Integer pageSize);
	
	void update(CSAndYellow csAndYellow);
	
	Pagination getPage(Integer roomId, Timestamp startTime, Timestamp endTime, Integer type1, Integer type2,
			Integer pageNo, Integer pageSize);
}
