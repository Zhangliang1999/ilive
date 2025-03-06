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
	 * 获取所有禁言信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ILiveEstoppel> selectILiveEstoppels();

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
}
