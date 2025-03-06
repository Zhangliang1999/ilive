package com.bvRadio.iLive.iLive.manager;

import java.util.List;

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
	public ILiveGuests addILiveGuest(ILiveGuests iLiveGuests);
	/**
	 * 根据直播间ID 和 状态   获取 嘉宾
	 * @param roomId
	 * @param isDelete
	 * @return
	 */
	public List<ILiveGuests> selectILiveGuestsList(Integer roomId,boolean isDelete);
	/**
	 * 根据嘉宾主键ID获取信息
	 * @param guestsId 主键
	 * @return
	 */
	public ILiveGuests selectILiveGuestsByID(Long guestsId);
	/**
	 * 删除嘉宾
	 * @param guestsId
	 */
	public void deleteILiveGuests(Long guestsId);

}
