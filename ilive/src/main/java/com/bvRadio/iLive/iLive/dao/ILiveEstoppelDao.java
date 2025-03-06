package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;

/**
 * 禁言 管理
 * @author YanXL
 *
 */
public interface ILiveEstoppelDao {
	
	/**
	 * 获取直播间禁言信息
	 * @return
	 * @throws Exception
	 */
	public List<ILiveEstoppel> selectILiveEstoppels(Integer roomId) throws Exception;
	/**
	 * 添加 禁言
	 * @param iLiveEstoppel
	 * @throws Exception
	 */
	public void insertILiveEstoppel(ILiveEstoppel iLiveEstoppel) throws Exception;
	
	/**
	 * 删除禁言消息
	 * @param iLiveEstoppel
	 * @throws Exception
	 */
	public void deleteILiveEstoppel(ILiveEstoppel iLiveEstoppel) throws Exception;
	
	/**
	 * 获取对象
	 * @param roomId
	 * @param cpn
	 * @param pageSize
	 * @return
	 */
	public Pagination getPager(Integer roomId, int cpn, int pageSize);
	/**
	 * 获取对象
	 * @param roomId
	 * @param userId
	 * @return
	 */
	public ILiveEstoppel getILiveEstoppelYesOrNo(Integer roomId, Long userId);
	/**
	 * 根据查询条件
	 * @param params
	 * @param roomId
	 * @param cpn
	 * @param pageSize
	 * @return
	 */
	public Pagination queryPager(String params, Integer roomId, int cpn, int pageSize);
}
