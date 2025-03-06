package com.bvRadio.iLive.iLive.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppQuestionAnswerVo;
import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.dao.ILiveMessageDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;

@Service
@Transactional
public class ILiveMessageMngImpl implements ILiveMessageMng {

	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

	@Autowired
	private ILiveLiveRoomDao iLiveLiveRoomDao;// 直播间
	
	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;
	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;

	@Transactional(readOnly = true)
	public Pagination getPage(Integer liveId, Integer liveMsgType, Integer msgType, Date startTime, Date endTime,
			String senderName, String msgContent, Integer status, Boolean deleted, Boolean checked, Integer orderBy,
			int pageNo, int pageSize) {
		Pagination pagination = messageDao.getPage(liveId, liveMsgType, msgType, startTime, endTime, senderName,
				msgContent, status, deleted, checked, orderBy, pageNo, pageSize);
		return pagination;
	}

	@Transactional(readOnly = true)
	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Integer msgType, Date startTime,
			Date endTime, String senderName, String msgContent, Integer status, Boolean deleted, Boolean checked,
			Integer orderBy, Boolean emptyAll) {
		List<ILiveMessage> messageList = messageDao.getList(liveId, liveMsgType, msgType, startTime, endTime,
				senderName, msgContent, status, deleted, checked, orderBy, emptyAll);

		ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.getILiveRoom(liveId);
		ILiveEvent liveEvent = iLiveRoom.getLiveEvent();
		ILiveEventVo vo = new ILiveEventVo();
		vo.setCheckedTime(liveEvent.getAutoCheckSecond());
		vo.setEstoppleType(liveEvent.getEstoppelType());
		vo.setLiveStatus(liveEvent.getLiveStatus());
		vo.setPlayType(liveEvent.getPlayType());
		for (ILiveMessage iLiveMessage : messageList) {
			iLiveMessage.setRoomType(1);
			iLiveMessage.setiLiveEventVo(vo);
		}
		return messageList;
	}

	@Transactional(readOnly = true)
	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Long startId, Integer size) {
		List<ILiveMessage> messageList = messageDao.getList(liveId, liveMsgType, startId, size);
		return messageList;
	}

	@Transactional(readOnly = true)
	public List<ILiveMessage> getListForWeb(Integer liveId, Integer liveMsgType, Integer status, Boolean deleted,
			Boolean checked, Long startId, Integer size) {
		return messageDao.getListForWeb(liveId, liveMsgType, status, deleted, checked, startId, size);
	}

	@Transactional(readOnly = true)
	public ILiveMessage findById(Long msgId) {
		ILiveMessage entity = messageDao.findById(msgId);
		return entity;
	}

	public ILiveMessage save(ILiveMessage bean) {
		Long nextId = iLiveFieldIdManagerMng.getNextId("ilive_message", "msg_id", 1);
		bean.setMsgId(nextId);
		if (null != bean) {
			String messageContent = bean.getMsgContent();
			String messageOrginContent = bean.getMsgOrginContent();
			messageContent.replaceAll(regxpForHtml, "");
			messageOrginContent.replaceAll(regxpForHtml, "");
			//过滤敏感字
			messageContent = sentitivewordFilterMng.replaceSensitiveWord(messageContent);
			bean.setMsgContent(messageContent);
			bean.setMsgOrginContent(messageOrginContent);
			messageDao.save(bean);
		}
		return bean;
	}

	public ILiveMessage updateCheckById(Long msgId, boolean checked) {
		ILiveMessage bean = messageDao.findById(msgId);
		if (null != bean) {
			bean.setChecked(checked);
		}
		return bean;
	}

	public ILiveMessage[] updateCheckByIds(Long[] ids, boolean checked) {
		ILiveMessage[] beans = new ILiveMessage[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = updateCheckById(ids[i], checked);
		}
		return beans;
	}

	public ILiveMessage update(ILiveMessage bean) {
		if (null != bean && null != bean.getMsgId()) {
			messageDao.update(bean);
		}
		return bean;
	}



	public int countMessageNum(Integer liveId, Integer liveMsgType, Date startTime, Date endTime) {
		return messageDao.countMessageNum(liveId, liveMsgType, startTime, endTime);
	}

	@Autowired
	private ILiveMessageDao messageDao;

	@Autowired
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;

	@Override
	public List<ILiveMessage> selectILiveMessageMngByEventId(Long liveEventId) {
		List<ILiveMessage> list = messageDao.selectILiveMessageMngByEventId(liveEventId);
		return list;
	}

	@Override
	public List<ILiveMessage> selectILiveMessageMngByEventIdAndType(Long liveEventId, Integer liveMsgType,
			Integer pageNo) {
		List<ILiveMessage> list = messageDao.selectILiveMessageMngByEventIdAndType(liveEventId, liveMsgType, pageNo);
		return list;
	}

	@Override
	public void deleteInteractiveMapAll(List<ILiveMessage> interactiveMap) {
		for (ILiveMessage iLiveMessage : interactiveMap) {
			iLiveMessage.setEmptyAll(true);
			messageDao.update(iLiveMessage);
		}
	}

	@Override
	public int getNumByEventIdAndType(Long liveEventId, Integer liveMsgType) {
		return messageDao.getNumByEventIdAndType(liveEventId, liveMsgType);
	}

	@Override
	public Pagination getPage(Integer roomId, Integer interactType, String searchContent, int cpn, int pageSize) {
		return messageDao.getPage(roomId, interactType, searchContent, cpn, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppQuestionAnswerVo> getQuestionAndAnwer(Long userId, Integer pageNo, Integer pageSize) {
		Pagination pagination = messageDao.getQuestionAndAnwer(userId, pageNo, pageSize);
		List<AppQuestionAnswerVo> newList = new ArrayList<>();
		if ((pagination != null) && (pagination.getList() != null) && (!pagination.getList().isEmpty())) {
			List<ILiveMessage> msgList = (List<ILiveMessage>) pagination.getList();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			AppQuestionAnswerVo qa = null;
			for (ILiveMessage message : msgList) {
				qa = new AppQuestionAnswerVo();
				qa.setQuestionContent(message.getMsgContent());
				if (message.getReplyType().intValue() == 1) {
					qa.setAnswerContent(message.getReplyContent());
					qa.setAnswerPerson(message.getReplyName());
					qa.setAnswerTime(sdf.format(message.getCreateTime()));
				} else {
					qa.setAnswerContent("暂未回复");
					qa.setAnswerPerson("");
					qa.setAnswerTime(sdf.format(message.getCreateTime()));
				}
				newList.add(qa);
			}
		}
		return newList;
	}

}
