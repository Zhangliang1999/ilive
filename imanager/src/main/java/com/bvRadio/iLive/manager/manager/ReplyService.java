package com.bvRadio.iLive.manager.manager;

import java.util.List;

import com.bvRadio.iLive.manager.entity.Reply;

public interface ReplyService {

	Long save(Reply reply);
	
	void deleteById(Long id);
	
	void update(Reply reply);
	
	Reply getById(Long id);
	
	List<Reply> getListByReportId(Long reportId);
}
