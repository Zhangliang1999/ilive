package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

/**
 * 用户
 * 
 * @author YanXL
 *
 */
public interface ILiveManagerMng {

	/**
	 * 企业管理者查询
	 */
	public ILiveManager getILiveManager(Long managerId);

	/**
	 * 企业
	 */
	public boolean saveIliveManager(ILiveManager manager);

	/**
	 * 根据主键获取数据信息
	 * 
	 * @param userId
	 *            主键
	 * @return
	 */
	public ILiveManager selectILiveManagerById(Long userId);

	/**
	 * 根据手机号获取信息
	 */
	public ILiveManager getILiveMangerByMobile(String mobile);

	/**
	 * 注册用户
	 * 
	 * @param username
	 * @param password
	 */
	public long registeredManager(String username, String password, Integer validType);

	/**
	 * 校验登录
	 * 
	 * @param loginToken
	 * @param userId
	 * @return
	 */
	public ILiveManager checkLogin(String loginToken, Long userId);

	public void updateLiveManager(ILiveManager manager);

	public long registeredManager(String phoneNum, String password, int i, Integer type);

	/**
	 * 根据用户名找到用户
	 * @param userName
	 * @return
	 */
	public ILiveManager getILiveManagerByUserName(String userName);
	

	public List<ILiveManager> getILiveManagerByEnterpriseId(Integer enterpriseId);

	Pagination getPage(ILiveManager user,Integer pageSize,Integer pageNo);
	
	
	Pagination getUserRecord(ILiveManager iLiveManager,Integer pageNo,Integer pageSize);
	
	List<ILiveManager> getUserRecordList(ILiveManager iLiveManager);
	
	/**
	 * 批量更新
	 * @param list
	 */
	void batchUpdate(List<ILiveManager> list);
}
