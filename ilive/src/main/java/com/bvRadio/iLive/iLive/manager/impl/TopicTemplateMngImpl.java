package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.TopicTemplateDao;
import com.bvRadio.iLive.iLive.entity.TopicTemplate;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.TopicTemplateMng;

@Service
@Transactional
public class TopicTemplateMngImpl implements TopicTemplateMng {

	@Autowired
	private TopicTemplateDao topicTemplateDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public Long save(TopicTemplate topicTemplate) {
		Long nextId = fieldIdMng.getNextId("topic_template", "id", 1);
		topicTemplate.setId(nextId);
		topicTemplateDao.save(topicTemplate);
		return nextId;
	}

	@Override
	public TopicTemplate getById(Long id) {
		return topicTemplateDao.getById(id);
	}

	@Override
	public TopicTemplate getByEnterprise(Integer enterpriseId) {
		return topicTemplateDao.getByEnterprise(enterpriseId);
	}

	@Override
	public void update(TopicTemplate topicTemplate) {
		topicTemplateDao.update(topicTemplate);
	}

	@Override
	public void delete(Long id) {
		topicTemplateDao.delete(id);
	}

}
