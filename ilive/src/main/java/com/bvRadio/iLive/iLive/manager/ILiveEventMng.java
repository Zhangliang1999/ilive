package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.LiveEventsVo;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
/**
 * 场次管理
 * @author YanXL
 *
 */
public interface ILiveEventMng {
	/**
	 * 根据场次ID获取直播信息
	 * @param liveEventId 场次ID
	 * @return
	 */
	public ILiveEvent selectILiveEventByID(Long liveEventId);

	public boolean saveIliveMng(ILiveEvent iLiveEvent,Integer iliveRoomId);

	public Long saveIliveMng(ILiveEvent iLiveEvent);

	public void updateILiveEvent(ILiveEvent dbEvent);
	/**
	 * 直播场次分页数据
	 * @param roomId 直播间ID
	 * @param liveEventId 场次ID
	 * @param pageSize  每页数据条数
	 * @param pageNo  页码
	 * @return
	 */
	public Pagination selectILiveEventPage(Integer roomId, Long liveEventId, Integer pageNo, Integer pageSize);
	/**
	 * 根据主键修改删除状态
	 * @param liveEventId
	 * @param isDelete
	 */
	public void updateILiveEventByIsDelete(Long liveEventId, boolean isDelete);
	/**
	 * 根据主键 修改  话题 评论状态
	 * @param evenId
	 * @param commentsAllow
	 */
	public void updateILiveEventByCommentsAllow(Long evenId,
			Integer commentsAllow);
	/**
	 * 根据主键修改话题审核状态
	 * @param evenId
	 * @param commentsAudit
	 */
	public void updateILiveEventByCommentsAudit(Long evenId,
			Integer commentsAudit);
	
	/**
	 * 放置缓存
	 * @param eventId
	 */
	public  void putLiveEventUserCache(Long eventId);
	
	/**
	 * 获取距离当前时间还有time分钟就开始的直播间
	 * @param time	单位 秒
	 * @return
	 */
	public List<ILiveEvent> getLiveEventByStartTime(Integer time);

	public List<LiveEventsVo> getLiveEventsByRoomId(Integer roomId, Integer pageNo, Integer pageSize);


	public List<ILiveEvent> getListEvent(Integer roomId);
	
}
