package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.TopicDao;
import com.bvRadio.iLive.iLive.entity.Topic;
import com.bvRadio.iLive.iLive.entity.TopicInnerContent;
import com.bvRadio.iLive.iLive.entity.TopicInnerType;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.TopicInnerContentMng;
import com.bvRadio.iLive.iLive.manager.TopicInnerTypeMng;
import com.bvRadio.iLive.iLive.manager.TopicMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class TopicMngImpl implements TopicMng {

	@Autowired
	private TopicDao topicDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private TopicInnerTypeMng topicInnerTypeMng;	//类型
	
	@Autowired
	private TopicInnerContentMng topicInnerContentMng;	//内容
	
	@Override
	public Pagination getPage(Long userId, String name, Integer pageNo, Integer pageSize) {
		return topicDao.getPage(userId, name, pageNo, pageSize);
	}

	@Override
	public Long save(Topic topic) {
		Long nextId = fieldIdMng.getNextId("topic", "id", 1);
		topic.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		topic.setCreateTime(now);
		topic.setIsPublish(0);
		topic.setIsPut(0);
		topic.setDomain(Topic.prefixDoamin+nextId);
		topicDao.save(topic);
		
		//默认创建一个标题类型
		TopicInnerType type = new TopicInnerType();
		type.setOrderN(0);
		type.setTopicId(nextId);
		type.setType(6);
		type.setIsSave(1);
		Long typeId = topicInnerTypeMng.save(type);
		//默认创建一个标题内容
		TopicInnerContent content = new TopicInnerContent();
		content.setInnerTypeId(typeId);
		content.setName(topic.getName());
		content.setBackgroundColor("000");
		content.setIsSave(1);
		topicInnerContentMng.save(content);
		return nextId;
	}

	@Override
	public void update(Topic topic) {
		topicDao.update(topic);
		
		Topic byId = getById(topic.getId());
		List<TopicInnerType> listType = byId.getListType();
		for(TopicInnerType type:listType) {
			
			if (type.getType() == 6) {
				List<TopicInnerContent> contentList = type.getContentList();
				for(TopicInnerContent content:contentList) {
					content.setName(topic.getName());
					topicInnerContentMng.update(content);
				}
			}
			
		}
		
	}

	@Override
	public List<Topic> getByEnterpriseId(Integer enterpriseId) {
		return topicDao.getByEnterpriseId(enterpriseId);
	}

	@Override
	public Topic getById(Long id) {
		Topic topic = topicDao.getById(id);
		List<TopicInnerType> listByTopicId = topicInnerTypeMng.getListByTopicId(id);
		topic.setListType(listByTopicId);
		return topic;
	}

	@Override
	public void delete(Long id) {
		topicDao.delete(id);
	}

	@Override
	public void sessionToDB(Topic topic) {
		List<TopicInnerType> listType = topic.getListType();
		topicInnerTypeMng.deleteAllByTopicId(topic.getId());
		for(TopicInnerType type:listType) {
			List<TopicInnerContent> contentList = type.getContentList();
			type.setTopicId(topic.getId());
			type.setIsSave(1);
			Long typeId = topicInnerTypeMng.save(type);
			for(TopicInnerContent content:contentList) {
				content.setInnerTypeId(typeId);
				content.setIsSave(1);
				topicInnerContentMng.save(content);
			}
		}
	}

	@Override
	public void sortColumnType(Topic topic, String str,Long typeId) {
		Iterator<TopicInnerType> iterator = topic.getListType().iterator();
		while (iterator.hasNext()) {
			TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
			if (topicInnerType.getId().equals(typeId)) {
				List<TopicInnerContent> contentList = topicInnerType.getContentList();
				contentList.clear();
				JSONArray jsonArray = JSONArray.fromObject(str);
				for(int i=0;i<jsonArray.size();i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					TopicInnerContent content = new TopicInnerContent();
					content.setBackgroundUrl(obj.getString("backurl"));
					content.setName(obj.getString("name"));
					content.setContentType(obj.getInt("type"));
					content.setOrderN(i);
					contentList.add(content);
				}
				break;
			}
		}
		
	}

	/**
	 * 多图排序
	 */
	@Override
	public void sortManyImg(Topic topic, String str, Long typeId,Integer num) {
		Iterator<TopicInnerType> iterator = topic.getListType().iterator();
		while (iterator.hasNext()) {
			TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
			if (topicInnerType.getId().equals(typeId)) {
				topicInnerType.setNum(num);
				List<TopicInnerContent> contentList = topicInnerType.getContentList();
				contentList.clear();
				JSONArray jsonArray = JSONArray.fromObject(str);
				for(int i=0;i<jsonArray.size();i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					TopicInnerContent content = new TopicInnerContent();
					content.setBackgroundUrl(obj.getString("backgroundUrl"));
					content.setName(obj.getString("name"));
					content.setContentType(obj.getInt("type"));
					content.setOrderN(i);
					contentList.add(content);
				}
				break;
			}
		}
	}

}
