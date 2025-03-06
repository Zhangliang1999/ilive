package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.TopicInnerTypeDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.Topic;
import com.bvRadio.iLive.iLive.entity.TopicInnerContent;
import com.bvRadio.iLive.iLive.entity.TopicInnerType;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.TopicInnerContentMng;
import com.bvRadio.iLive.iLive.manager.TopicInnerTypeMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class TopicInnerTypeMngImpl implements TopicInnerTypeMng {

	@Autowired
	private TopicInnerTypeDao topicInnerTypeDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private TopicInnerContentMng topicInnerContentMng;
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	
	@Override
	public Long save(TopicInnerType topicInnerType) {
		Long nextId = fieldIdMng.getNextId("topic_inner_type", "id", 1);
		topicInnerType.setId(nextId);
		topicInnerTypeDao.save(topicInnerType);
		return nextId;
	}

	@Override
	public void update(TopicInnerType topicInnerType) {
		topicInnerTypeDao.update(topicInnerType);
	}

	@Override
	public void delete(Long id) {
		topicInnerContentMng.deleteByTypeId(id);
		topicInnerTypeDao.delete(id);
	}

	@Override
	public TopicInnerType getById(Long id) {
		TopicInnerType innerType = topicInnerTypeDao.getById(id);
		List<TopicInnerContent> list = topicInnerContentMng.getListByTypeId(id);
		innerType.setContentList(list);
		return innerType;
	}

	@Override
	public List<TopicInnerType> getListByTopicId(Long topicId) {
		List<TopicInnerType> typeList = topicInnerTypeDao.getListByTopicId(topicId);
		for(TopicInnerType type :typeList) {
			List<TopicInnerContent> list = topicInnerContentMng.getListByTypeId(type.getId());
			for(TopicInnerContent content:list) {
				if (content!=null && content.getContentType()!=null && content.getContentType() ==1) {
					if (content.getContentId()!=null) {
						ILiveLiveRoom room = iLiveLiveRoomMng.findById(content.getContentId());
						Integer liveStatus = room.getLiveEvent().getLiveStatus();
						content.setStatus(liveStatus);
					}
				}
			}
			type.setContentList(list);
		}
		return typeList;
	}

	@Override
	public void updateSessionType(TopicInnerType topicInnerType) {
		topicInnerTypeDao.update(topicInnerType);
		List<TopicInnerContent> list = topicInnerType.getContentList();
		topicInnerContentMng.deleteByTypeId(topicInnerType.getId());
		for(TopicInnerContent content:list) {
			if (content.getContentType()!=null && content.getContentType()==2) {
				try {
					ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId((long)content.getContentId());
					Integer serverMountId = iLiveMediaFile.getServerMountId();
					ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
					Integer roomId = iLiveMediaFile.getLiveRoomId();
					String mediavedioAddr = serverAccess.getH5HttpUrl() + "/phone" + "/review.html?roomId="
							+ (roomId == null ? 0 : roomId) + "&fileId=" + content.getContentId();
					
					content.setContentUrl(mediavedioAddr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			topicInnerContentMng.save(content);
		}
	}

	@Override
	public void deleteAllByTopicId(Long topicId) {
		List<TopicInnerType> listByTopicId = getListByTopicId(topicId);
		for(TopicInnerType type:listByTopicId) {
			topicInnerContentMng.deleteByTypeId(type.getId());
		}
		topicInnerTypeDao.deleteAllByTopicId(topicId);
	}

	@Override
	public TopicInnerType createTextType(Topic topic, TopicInnerContent content) {
		TopicInnerType type = new TopicInnerType();
		type.setTopicId(topic.getId());
		type.setType(1);
		type.setOrderN(topic.getListType().size());
		type.setIsSave(0);
		Long typeid = save(type);
		content.setInnerTypeId(typeid);
		Long contentId = topicInnerContentMng.save(content);
		TopicInnerContent byId = topicInnerContentMng.getById(contentId);
		List<TopicInnerContent> list = new ArrayList<>();
		list.add(byId);
		type.setContentList(list);
		return type;
	}

	@Override
	public TopicInnerType createOneImgType(Topic topic, String url) {
		TopicInnerType type = new TopicInnerType();
		type.setTopicId(topic.getId());
		type.setType(2);
		type.setOrderN(topic.getListType().size());
		type.setIsSave(0);
		Long typeid = save(type);
		TopicInnerContent content = new TopicInnerContent();
		content.setInnerTypeId(typeid);
		content.setBackgroundUrl(url);
		Long contentId = topicInnerContentMng.save(content);
		TopicInnerContent byId = topicInnerContentMng.getById(contentId);
		List<TopicInnerContent> list = new ArrayList<>();
		list.add(byId);
		type.setContentList(list);
		return type;
	}

	@Override
	public TopicInnerType createManyImgType(Topic topic, String json, Integer num) {
		TopicInnerType type = new TopicInnerType();
		type.setTopicId(topic.getId());
		type.setType(3);
		type.setOrderN(topic.getListType().size());
		type.setIsSave(0);
		Long typeid = save(type);
		
		JSONArray jsonArray = JSONArray.fromObject(json);
		List<TopicInnerContent> contentList = new ArrayList<>();
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			TopicInnerContent content = new TopicInnerContent();
			content.setBackgroundUrl(obj.getString("backgroundUrl"));
			content.setName(obj.getString("name"));
			content.setContentType(obj.getInt("type"));
			content.setInnerTypeId(typeid);
			contentList.add(content);
		}
		type.setContentList(contentList);
		return type;
	}

	@Override
	public TopicInnerType createColumnType(Topic topic, String json, Integer mark) {
		TopicInnerType type = new TopicInnerType();
		type.setTopicId(topic.getId());
		if(mark==1) {
			type.setType(4);
		}else if(mark==2) {
			type.setType(5);
		}
		type.setOrderN(topic.getListType().size());
		type.setIsSave(0);
		Long typeid = save(type);
		JSONArray jsonArray = JSONArray.fromObject(json);
		List<TopicInnerContent> contentList = new ArrayList<>();
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			TopicInnerContent content = new TopicInnerContent();
			content.setBackgroundUrl(obj.getString("background"));
			content.setName(obj.getString("name"));
			content.setContentType(obj.getInt("type"));
			content.setInnerTypeId(typeid);
			contentList.add(content);
		}
		type.setContentList(contentList);
		return type;
	}

}
