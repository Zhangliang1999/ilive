package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;

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

	public List<ILiveManager> getIiveManagerList(Long[] managerList);
	public List<ILiveManager> getIiveManagerListAndName(String username,Long[] managerList);

	public ILiveManager getILiveMangerByWxOpenId(String wxOpenId);

	public ILiveManager getILiveMangerByLoginToken(Long userId, String loginToken);

	public ILiveManager getILiveMangerByWxUnionId(String unionId);
	/**
	 * 根据直播间ID 获取协调数据
	 * @param roomId
	 * @param level 
	 * @throws Exception
	 */
	public List<ILiveManager> selectILiveManagerByRoomId(Integer roomId, Integer level) throws Exception;
	/**
	 * 根据账户和手机号获取数据
	 * @param userName
	 * @param phoneNum
	 * @return
	 */
	public ILiveManager selectILiveManagerByPhoneNumAndUserName(
			String userName, String phoneNum);
	/**
	 * 真实删除
	 * @param userId
	 */
	public void deleteILiveManager(Long userId);

	public List<ILiveManager> getILiveMangerListByMobile(String mobile);

	public List<ILiveManager> getILiveManagerById(Long userId);

	public List<ILiveManager> getILiveManagerByEnterpriseId(Integer enterpriseId);

	public ILiveManager getILiveMangerByPhoneNumber(String phoneNum);
	
	public ILiveManager getILiveManagerByDXID(String appid);

	public ILiveManager getILiveMangerByPhoneNumber(String appId, String userId);

	public ILiveManager getILiveMangerByAppId(String appId);

	

}
