package com.bvRadio.iLive.iLive.manager;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.common.page.Pagination;

public interface ILiveMessageMng {
	public Pagination getPage(Integer liveId, Integer liveMsgType, Integer msgType, Date startTime,
			Date endTime, String senderName, String msgContent, Integer status, Boolean deleted,
			Boolean checked, Integer orderBy, int pageNo, int pageSize);

	
	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Integer msgType,
			Date startTime, Date endTime, String senderName, String msgContent, Integer status,
			Boolean deleted, Boolean checked, Integer orderBy);

	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Long startId,
			Integer size);

	public ILiveMessage findById(Long msgId);

	public ILiveMessage save(ILiveMessage bean);

	/**
	 * 审核/取消审核评论
	 * 
	 * @param msgId
	 * @param checked
	 * @return
	 */
	public ILiveMessage updateCheckById(Long msgId, boolean checked);

	/**
	 * 审核/取消审核评论
	 * 
	 * @param ids
	 *            评论IDs
	 * @return
	 */
	public ILiveMessage[] updateCheckByIds(Long[] ids, boolean checked);

	public ILiveMessage deleteById(Long msgId);

	public ILiveMessage[] deleteByIds(Long[] ids);
	/**
	 * 修改
	 * @param bean
	 * @return
	 */
	public ILiveMessage update(ILiveMessage bean);

	public int countMessageNum(Integer liveId, Integer liveMsgType, Date startTime, Date endTime);
	
	/**
	 * 根据场次ID获取互动消息
	 * @param liveEventId 场次ID
	 * @return
	 */
	public List<ILiveMessage> selectILiveMessageMngByEventId(Long liveEventId);
	
	/**
	 * 根据场次ID获取互动消息
	 * @param liveEventId 场次ID
	 * @return
	 */
	public List<ILiveMessage> selectILiveMessageMngByEventIdAndType(Long liveEventId,Integer liveMsgType,Integer pageNo);
	
	/**
	 * 根据场次ID获取互动消息
	 * @param liveEventId 场次ID
	 * @return
	 */
	public int getNumByEventIdAndType(Long liveEventId,Integer liveMsgType);

	/**
	 * 清空数据
	 * @param interactiveMap
	 */
	public void deleteInteractiveMapAll(List<ILiveMessage> interactiveMap);


	public Pagination getPage(Integer roomId, Integer interactType,String searchContent, int cpn, int pageSize);

	Pagination getPageRecordByUser(Long userId,Integer roomId,String keyword,Integer pageNo,Integer pageSize);

	List<ILiveMessage> getListRecordByUser(Long userId,Integer roomId,String keyword);
}