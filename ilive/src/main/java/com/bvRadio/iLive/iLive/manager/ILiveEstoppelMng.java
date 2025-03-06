package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;

/**
 * 禁言管理
 * 
 * @author YanXL
 *
 */
public interface ILiveEstoppelMng {
	/**
	 * 获取直播间禁言信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ILiveEstoppel> selectILiveEstoppels(Integer roomId);

	/**
	 * 添加 禁言
	 * 
	 * @param iLiveEstoppel
	 * @throws Exception
	 */
	public void insertILiveEstoppel(ILiveEstoppel iLiveEstoppel);

	/**
	 * 删除禁言消息
	 * 
	 * @param iLiveEstoppel
	 * @throws Exception
	 */
	public void deleteILiveEstoppel(ILiveEstoppel iLiveEstoppel);

	/**
	 * 获取直播禁言列表
	 * 
	 * @param roomId房间号ID
	 * @param cpn
	 *            当前页码
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Pagination getPager(Integer roomId, int cpn, int pageSize);
	/**
	 * 
	 * @param params 
	 * @param roomId
	 * @param cpn
	 * @param pageSize
	 * @return
	 */
	public Pagination queryPager(String params,Integer roomId, int cpn, int pageSize);
	/**
	 * 判断用户是否被禁言
	 * @param roomId 直播间ID
	 * @param userId 用户ID
	 * @return
	 */
	public boolean getILiveEstoppelYesOrNo(Integer roomId, Long userId);

	public ILiveEstoppel getILiveEstoppel(Integer roomId, Long userId);
}
