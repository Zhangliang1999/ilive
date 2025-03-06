package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveGift;

/**
 * 礼物操作   事务
 * @author YanXL
 *
 */
public interface ILiveGiftMng {
	/**
	 * 根据用户ID获取用户自己现在礼物
	 *  userId=0 roomId=0 时获取系统默认礼物
	 * @param userId
	 * @param roomId 
	 * @return
	 */
	public List<ILiveGift> selectIliveGiftByUserId(Long userId, Integer roomId);
	/**
	 * 新增礼物
	 * @param iLiveGift
	 * @throws Exception 
	 */
	public void addILiveGift(ILiveGift iLiveGift) throws Exception;
	/**
	 * 删除
	 * @param giftId
	 * @throws Exception 
	 */
	public void deleteILiveGiftById(Long giftId) throws Exception;
	/**
	 * 根据直播间ID和礼物类型获取礼物数据
	 * @param roomId 直播间ID
	 * @param giftType
	 * @return
	 */
	public List<ILiveGift> selectIliveGiftByGiftType(Integer roomId,
			Integer giftType);
	/**
	 * 根据主键ID获取数据
	 * @param giftId
	 * @return
	 */
	public ILiveGift selectIliveGiftByGiftId(Long giftId);
}
