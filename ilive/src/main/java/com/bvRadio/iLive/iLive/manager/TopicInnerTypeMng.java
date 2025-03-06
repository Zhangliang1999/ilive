package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.Topic;
import com.bvRadio.iLive.iLive.entity.TopicInnerContent;
import com.bvRadio.iLive.iLive.entity.TopicInnerType;

public interface TopicInnerTypeMng {

	Long save(TopicInnerType topicInnerType);
	
	void updateSessionType(TopicInnerType topicInnerType);
	
	void update(TopicInnerType topicInnerType);
	
	void delete(Long id);
	
	void deleteAllByTopicId(Long topicId);
	
	TopicInnerType getById(Long id);
	
	List<TopicInnerType> getListByTopicId(Long topicId);
	
	TopicInnerType createTextType(Topic topic,TopicInnerContent content);
	
	TopicInnerType createOneImgType(Topic topic,String url);
	
	TopicInnerType createManyImgType(Topic topic,String json,Integer num);
	
	TopicInnerType createColumnType(Topic topic,String json,Integer mark);
}
