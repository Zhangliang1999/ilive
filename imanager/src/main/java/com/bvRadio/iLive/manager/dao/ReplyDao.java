package com.bvRadio.iLive.manager.dao;

import java.util.List;

import com.bvRadio.iLive.manager.entity.Reply;

public interface ReplyDao {

	void save(Reply reply);
	
	void deleteById(Long id);
	
	void update(Reply reply);
	
	Reply getById(Long id);
	
	List<Reply> getListByReportId(Long reportId);
}
