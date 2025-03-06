package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveGuests;

/**
 * 嘉宾管理实务层
 * @author YanXL
 *
 */
public interface ILiveGuestsMng {
	/**
	 * 添加嘉宾
	 * @param iLiveGuests 嘉宾类型
	 */
	public void addILiveGuest(ILiveGuests iLiveGuests);
	/**
	 * 根据主键获取信息
	 * @param roomId 主键
	 * @return
	 */
	public ILiveGuests selectILiveGuestsById(Integer roomId);
	/**
	 * 修改数据
	 * @param iLiveGuests
	 */
	public void updateILiveGuests(ILiveGuests iLiveGuests);
}
