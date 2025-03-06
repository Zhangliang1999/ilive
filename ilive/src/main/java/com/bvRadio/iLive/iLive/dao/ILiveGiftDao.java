package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveGift;

/**
 * 礼物管理
 * @author YanXL
 *
 */
public interface ILiveGiftDao {
	/**
	 * 根据用户ID获取用户自己现在礼物
	 *  userId=0 时获取系统默认礼物
	 * @param userId
	 * @param roomId 
	 * @return
	 */
	public List<ILiveGift> selectIliveGiftByUserId(Long userId, Integer roomId) throws Exception;
	/**
	 * 新增礼物
	 * @param iLiveGift
	 * @throws Exception
	 */
	public void saveILiveGift(ILiveGift iLiveGift) throws Exception;
	/**
	 * 删除
	 * @param giftId
	 * @throws Exception
	 */
	public void deleteILiveGiftById(Long giftId) throws Exception;
	/**
	 * 根据直播间ID和礼物类型获取数据
	 * @param roomId
	 * @param giftType
	 * @return
	 * @throws Exception
	 */
	public List<ILiveGift> selectIliveGiftByGiftType(Integer roomId,
			Integer giftType) throws Exception;
	/**
	 * 根据主键获取数据
	 * @param giftId
	 * @return
	 * @throws Exception
	 */
	public ILiveGift selectIliveGiftByGiftId(Long giftId) throws Exception;
	
}
