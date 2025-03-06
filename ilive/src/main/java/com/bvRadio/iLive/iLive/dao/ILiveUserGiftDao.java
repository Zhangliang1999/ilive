package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveUserGift;

public interface ILiveUserGiftDao {
	/**
	 * 根据直播间ID获取收礼物记录
	 * @param userContent
	 * @param roomContent
	 * @param roomId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Pagination selectILiveUserGiftPage(String userContent,
			String roomContent, Integer roomId, Integer pageNo,
			Integer pageSize) throws Exception;
	/**
	 * 新增数据
	 * @param iLiveUserGift
	 * @throws Exception
	 */
	public void insertILiveUserGift(ILiveUserGift iLiveUserGift) throws Exception;
	/**
	 * 检索数据
	 * @param userContent
	 * @param roomContent
	 * @param roomId
	 * @return
	 */
	public List<ILiveUserGift> selectILiveUserGiftExcel(String userContent,
			String roomContent, Integer roomId) throws Exception;

}
