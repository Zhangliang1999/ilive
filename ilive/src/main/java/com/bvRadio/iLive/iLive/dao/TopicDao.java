package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.Topic;

public interface TopicDao {
	
	Pagination getPage(Long userId,String name,Integer pageNo,Integer pageSize);
	
	void save(Topic topic);
	
	void update(Topic topic);
	
	List<Topic> getByEnterpriseId(Integer enterpriseId);
	
	Topic getById(Long id);
	
	void delete(Long id);
	
}
