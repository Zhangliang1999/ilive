package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

/**
 * 子账户管理
 * @author Administrator
 *
 */
public interface ILiveSubAccountDao {
	/**
	 * 获取企业下所有子账户
	 * @param pageNo
	 * @param pageSize
	 * @param enterpriseId
	 * @return
	 * @throws Exception
	 */
	public Pagination selectILiveManagerPage(Integer pageNo, Integer pageSize,
			Integer enterpriseId) throws Exception;
	/**
	 * 新增子用户
	 * @param manager
	 * @throws Exception
	 */
	public void addILiveSubAccountMng(ILiveManager manager) throws Exception;
	public List<ILiveManager> getILiveManagerPage(Integer enterpriseId,String roomId,String userId);
	public List<ILiveManager> getILiveManagerPage(Integer enterpriseId);

}
