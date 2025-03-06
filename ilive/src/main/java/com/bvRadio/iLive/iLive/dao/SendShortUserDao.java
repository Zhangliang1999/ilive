package com.bvRadio.iLive.iLive.dao;

import java.sql.Timestamp;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.SendShortUser;

public interface SendShortUserDao {

	Pagination getPage(Integer roomId,Long id,String sendUser,Integer pageNo,Integer pageSize);
	
	SendShortUser getById(Long id);
	
	void save(SendShortUser sendShortUser);

	Pagination getPage(Integer roomId, Long id, String sendUser, Integer pageNo, Integer pageSize, Timestamp startTime,
			Timestamp endTime);
}
