package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.TopicInnerType;

public interface TopicInnerTypeDao {

	void save(TopicInnerType topicInnerType);
	
	void update(TopicInnerType topicInnerType);
	
	void delete(Long id);
	
	void deleteAllByTopicId(Long topicId);
	
	TopicInnerType getById(Long id);
	
	List<TopicInnerType> getListByTopicId(Long topicId);
}
