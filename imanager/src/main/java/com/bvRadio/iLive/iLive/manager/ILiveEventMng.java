package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
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
	
	Pagination getPageByRoomId(Integer roomId,Integer pageSize,Integer pageNo);
	/**
	 * 根据直播间id获取或有直播场次
	 */
	List<ILiveEvent> findAllEventByRoomId(Integer roomId);

}
