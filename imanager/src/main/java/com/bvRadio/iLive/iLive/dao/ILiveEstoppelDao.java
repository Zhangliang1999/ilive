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
	 * 获取所有禁言信息
	 * @return
	 * @throws Exception
	 */
	public List<ILiveEstoppel> selectILiveEstoppels() throws Exception;
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
}
