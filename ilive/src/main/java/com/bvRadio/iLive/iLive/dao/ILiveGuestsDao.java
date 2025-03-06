package com.bvRadio.iLive.iLive.dao;

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
	public void insertILiveGuests(ILiveGuests iLiveGuests) throws Exception;
	/**
	 * 根据主键获取数据
	 * @param guestsId 主键
	 * @return
	 * @throws Exception
	 */
	public ILiveGuests selectILiveGuestsByID(Integer roomId) throws Exception;
	/**
	 * 修改对象
	 * @param iLiveGuests
	 * @throws Exception
	 */
	public void updateILiveGuests(ILiveGuests iLiveGuests) throws Exception;
	
}
