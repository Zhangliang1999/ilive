package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.TopicInnerContent;

public interface TopicInnerContentMng {

	List<TopicInnerContent> getListByTypeId(Long typeId);
	
	Long save(TopicInnerContent topicInnerContent);
	
	void update(TopicInnerContent topicInnerContent);
	
	/**
	 * 删除单个内容
	 * @param id
	 */
	void deleteById(Long id);
	
	/**
	 * 删除该结构id下所有内容
	 * @param typeId
	 */
	void deleteByTypeId(Long typeId);
	
	TopicInnerContent getById(Long id);
}
