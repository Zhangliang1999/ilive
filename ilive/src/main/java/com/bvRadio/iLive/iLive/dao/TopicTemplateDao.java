package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.TopicTemplate;

public interface TopicTemplateDao {

	void save(TopicTemplate topicTemplate);
	
	TopicTemplate getById(Long id);
	
	TopicTemplate getByEnterprise(Integer enterpriseId);

	void update(TopicTemplate topicTemplate);
	
	void delete(Long id);
}
