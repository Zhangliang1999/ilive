package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;

public interface ILiveEventDao {
	/**
	 * 根据主键获取场次信息
	 * @param liveEventId
	 * @return
	 */
	public ILiveEvent selectILiveEventByID(Long liveEventId);

	public Long saveIliveMng(ILiveEvent iLiveEvent);

	public void updateILiveEvent(ILiveEvent dbEvent);
	/**
	 * 场次分页数据
	 * @param roomId 直播间ID
	 * @param liveEventId 场次ID
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @return
	 */
	public Pagination selectILiveEventPage(Integer roomId, Long liveEventId,
			Integer pageNo, Integer pageSize) throws Exception;
	/**
	 * 根据场次ID修改  删除状态
	 * @param liveEventId
	 * @param isDelete
	 * @throws Exception
	 */
	public void updateILiveEventByIsDelete(Long liveEventId, boolean isDelete) throws Exception;
	
	public Pagination getPageByRoomId(Integer roomId, Integer pageSize, Integer pageNo);
	
	public List<ILiveEvent> findAllEventByRoomId(Integer roomId);

}
