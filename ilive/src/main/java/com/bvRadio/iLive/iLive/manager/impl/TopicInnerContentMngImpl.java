package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.TopicInnerContentDao;
import com.bvRadio.iLive.iLive.entity.TopicInnerContent;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.TopicInnerContentMng;

@Service
@Transactional
public class TopicInnerContentMngImpl implements TopicInnerContentMng {

	@Autowired
	private TopicInnerContentDao topicInnerContentDao;	
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	
	@Override
	public List<TopicInnerContent> getListByTypeId(Long typeId) {
		return topicInnerContentDao.getListByTypeId(typeId);
	}

	@Override
	public Long save(TopicInnerContent topicInnerContent) {
		Long nextId = fieldIdMng.getNextId("topic_inner_content", "id", 1);
		topicInnerContent.setId(nextId);
		topicInnerContentDao.save(topicInnerContent);
		return nextId;
	}

	@Override
	public void update(TopicInnerContent topicInnerContent) {
		topicInnerContentDao.update(topicInnerContent);
	}

	@Override
	public void deleteById(Long id) {
		topicInnerContentDao.deleteById(id);
	}

	@Override
	public void deleteByTypeId(Long typeId) {
		topicInnerContentDao.deleteByTypeId(typeId);
	}

	@Override
	public TopicInnerContent getById(Long id) {
		return topicInnerContentDao.getById(id);
	}

}
