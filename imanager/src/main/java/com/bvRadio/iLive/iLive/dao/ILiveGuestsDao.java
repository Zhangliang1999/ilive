package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveGuests;

/**
 * 嘉宾   数据连接
 * @author YanXL
 *
 */
public interface ILiveGuestsDao {
	/**
	 * 添加数据
	 * @param iLiveGuests
	 */
	public ILiveGuests insertILiveGuests(ILiveGuests iLiveGuests) throws Exception;
	/**
	 * 获取直播间嘉宾
	 * @param roomId
	 * @param isDelete
	 * @return
	 */
	public List<ILiveGuests> selectILiveGuestsList(Integer roomId, boolean isDelete) throws Exception ;
	/**
	 * 根据主键获取数据
	 * @param guestsId 主键
	 * @return
	 * @throws Exception
	 */
	public ILiveGuests selectILiveGuestsByID(Long guestsId) throws Exception;
	/**
	 * 删除
	 * @param guestsId
	 */
	public void deleteILiveGuests(Long guestsId) throws Exception;
	
	
}
