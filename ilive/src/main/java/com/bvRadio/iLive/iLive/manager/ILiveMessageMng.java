package com.bvRadio.iLive.iLive.manager;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.iLive.action.front.vo.AppQuestionAnswerVo;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.common.page.Pagination;

public interface ILiveMessageMng {
	public Pagination getPage(Integer liveId, Integer liveMsgType, Integer msgType, Date startTime,
			Date endTime, String senderName, String msgContent, Integer status, Boolean deleted,
			Boolean checked, Integer orderBy, int pageNo, int pageSize);

	/**
	 * 直播间  初始化   数据
	 * @param liveId
	 * @param liveMsgType
	 * @param msgType
	 * @param startTime
	 * @param endTime
	 * @param senderName
	 * @param msgContent
	 * @param status
	 * @param deleted
	 * @param checked
	 * @param orderBy
	 * @param emptyAll 清空标示
	 * @return
	 */
	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Integer msgType,
			Date startTime, Date endTime, String senderName, String msgContent, Integer status,
			Boolean deleted, Boolean checked, Integer orderBy, Boolean emptyAll);

	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Long startId,
			Integer size);

	/**
	 * 2018-06-19 web大屏获取消息列表
	 * 
	 * @param liveId
	 * @param liveMsgType
	 * @param status
	 * @param deleted
	 * @param checked
	 * @param startId
	 * @param size
	 * @return
	 */
	public List<ILiveMessage> getListForWeb(Integer liveId, Integer liveMsgType, Integer status, Boolean deleted,
			Boolean checked, Long startId, Integer size);
	/**
	 * 根据主键获取数据
	 * @param msgId
	 * @return
	 */
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


	/**
	 * 获取问答接口
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<AppQuestionAnswerVo> getQuestionAndAnwer(Long userId, Integer pageNo, Integer pageSize);



}