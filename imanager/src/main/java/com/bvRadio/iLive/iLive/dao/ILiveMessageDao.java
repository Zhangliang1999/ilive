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
			Boolean deleted, Boolean checked, Integer orderBy);

	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Long startId, Integer size);

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

	public Pagination getPageRecordByUser(Long userId, Integer roomId, String keyword, Integer pageNo,
			Integer pageSize);
	
	public List<ILiveMessage> getListRecordByUser(Long userId, Integer roomId, String keyword);
}