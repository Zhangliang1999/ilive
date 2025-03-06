package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.TopicTemplate;

public interface TopicTemplateMng {

	Long save(TopicTemplate topicTemplate);
	
	TopicTemplate getById(Long id);
	
	TopicTemplate getByEnterprise(Integer enterpriseId);

	void update(TopicTemplate topicTemplate);
	
	void delete(Long id);
}
