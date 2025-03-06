package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

/**
 * 子账户管理
 * @author Administrator
 *
 */
public interface ILiveSubAccountMng {
	/**
	 * 根据企业ID获取企业下的所有用户
	 * @param pageNo 
	 * @param pageSize
	 * @param enterpriseId
	 * @return
	 */
	public Pagination selectILiveManagerPage(Integer pageNo, Integer pageSize, Integer enterpriseId);
	/**
	 * 
	 * @param manager
	 */
	public boolean addILiveSubAccountMng(ILiveManager manager);
	public List<ILiveManager> getILiveManagerPage(Integer enterpriseId,String roomId,String userId);
	public List<ILiveManager> selectILiveManagerPage(Integer enterpriseId);
	

}
