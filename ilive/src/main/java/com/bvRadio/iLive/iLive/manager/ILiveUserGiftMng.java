package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveUserGift;
/**
 * 收取礼物事务
 * @author YanXL
 *
 */
public interface ILiveUserGiftMng {
	/**
	 * 直播间 收取 礼物记录
	 * @param userContent 用户内容
	 * @param roomContent 直播间内容
	 * @param roomId 直播间ID
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Pagination selectILiveUserGiftPage(String userContent,
			String roomContent, Integer roomId, Integer pageNo, Integer pageSize) throws Exception;
	/**
	 * 添加数据
	 * @param iLiveUserGift
	 * @throws Exception 
	 */
	public void addILiveUserGift(ILiveUserGift iLiveUserGift) throws Exception;
	/**
	 * 获取检索所有数据
	 * @param userContent
	 * @param roomContent
	 * @param roomId
	 * @return
	 * @throws Exception 
	 */
	public List<ILiveUserGift> selectILiveUserGiftExcel(String userContent,
			String roomContent, Integer roomId) throws Exception;

}
