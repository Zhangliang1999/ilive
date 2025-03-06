package com.bvRadio.iLive.iLive.dao;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;

public interface ILiveMessageDao {
	public Pagination getPage(Integer liveId, Integer liveMsgType, Integer msgType, Date startTime,
			Date endTime, String senderName, String msgContent, Integer status, Boolean deleted,
			Boolean checked, Integer orderBy, int pageNo, int pageSize);

	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Integer msgType,
			Date startTime, Date endTime, String senderName, String msgContent, Integer status,
			Boolean deleted, Boolean checked, Integer orderBy,Boolean emptyAll);

	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Long startId, Integer size);
	
	public List<ILiveMessage> getListForWeb(Integer liveId, Integer liveMsgType, Integer status, Boolean deleted,
			Boolean checked, Long startId, Integer size);
	
	public ILiveMessage findById(Long msgId);

	public ILiveMessage save(ILiveMessage bean);

	public ILiveMessage deleteById(Long msgId);

	public void update(ILiveMessage bean);

	public ILiveMessage updateByUpdater(Updater<ILiveMessage> updater);

	public int countMessageNum(Integer liveId, Integer liveMsgType, Date startTime, Date endTime);

	/**
	 * 根据场次ID获取互动消息
	 * @param liveEventId
	 * @return
	 */
	public List<ILiveMessage> selectILiveMessageMngByEventId(Long liveEventId);
	/**
	 * 根据场次ID获取互动消息
	 * @param liveEventId
	 * @return
	 */
	public List<ILiveMessage> selectILiveMessageMngByEventIdAndType(Long liveEventId,Integer liveMsgType,Integer pageNo);
	
	
	public int getNumByEventIdAndType(Long liveEventId, Integer liveMsgType);

	
	/**
	 * 互动列表分页
	 * @param roomId
	 * @param interactType
	 * @param cpn
	 * @param pageSize
	 * @return
	 */
	public Pagination getPage(Integer roomId, Integer interactType, String searchContent,int cpn, int pageSize);

	
	/**
	 * 获取页码
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getQuestionAndAnwer(Long userId, Integer pageNo, Integer pageSize);

	/**
	 * 获得互动消息列表
	 * @param liveEventId
	 * @return
	 */
	public List<ILiveMessage> selectILiveMessageMngByEventIdAndType(Long liveEventId, Integer liveMsgType, Integer pageNo,
			Integer count, Integer check);

	/**
	 * 获得互动消息列表
	 * @param liveEventId
	 * @return
	 */
	public int getNumByEventIdAndType(Long liveEventId, Integer liveMsgType, boolean check);

	
	
}