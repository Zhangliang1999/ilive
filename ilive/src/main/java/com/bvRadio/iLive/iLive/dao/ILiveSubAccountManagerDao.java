package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubAccountManager;

/**
 * 子账户管理
 * @author Administrator
 *
 */
public interface ILiveSubAccountManagerDao {
	/**
	 * 获取企业下所有申请子账户
	 * @param pageNo
	 * @param pageSize
	 * @param enterpriseId
	 * @return
	 * @throws Exception
	 */
	public Pagination selectSubAccountManagerPage(Integer pageNo, Integer pageSize,
			Integer enterpriseId) throws Exception;
	/**
	 * 新增子用户
	 * @param manager
	 * @throws Exception
	 */
	public void addILiveSubAccountMng(ILiveSubAccountManager manager) throws Exception;
	public List<ILiveSubAccountManager> getILiveManagerPage(String user);
	public void updateILiveSubAccountMng(ILiveSubAccountManager manager) throws Exception;
	Long selectMaxId();
	public ILiveSubAccountManager getILiveSubAccountManager(String enterpriseId);
}
