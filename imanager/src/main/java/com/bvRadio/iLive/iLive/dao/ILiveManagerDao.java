package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

/**
 * 用户数据连接
 * 
 * @author YanXL
 *
 */
public interface ILiveManagerDao {
	/**
	 * 根据主键获取数据
	 * 
	 * @param userId
	 *            主键ID
	 * @return
	 */
	public ILiveManager selectILiveManagerById(Long userId);

	/**
	 * 根据手机号获取信息
	 */
	public ILiveManager getILiveMangerByMobile(String mobile);

	/**
	 * 保存管理用户
	 * 
	 * @param iLiveMangerByMobile
	 */
	public void saveManager(ILiveManager iLiveMangerByMobile);

	/**
	 * 获取
	 * 
	 * @param managerId
	 * @return
	 */
	public ILiveManager getILiveManager(Long managerId);

	/**
	 * 检测登录
	 * 
	 * @param loginToken
	 * @param userId
	 * @return
	 */
	public ILiveManager checkLogin(String loginToken, Long userId);

	/**
	 * 修改
	 * 
	 * @param manager
	 */
	public void updateLiveManager(ILiveManager manager);

	public ILiveManager getILiveManagerByUserName(String userName);

	/**
	 * 根据企业Id获取所有该企业的人
	 * @param enterpriseId
	 * @return
	 */
	public List<ILiveManager> getILiveManagerByEnterpriseId(Integer enterpriseId);
	
	Pagination getPage(ILiveManager user, Integer pageSize, Integer pageNo);

	public Pagination getUserRecord(ILiveManager iLiveManager, Integer pageNo, Integer pageSize);
	
	public List<ILiveManager> getUserRecordList(ILiveManager iLiveManager);
}
