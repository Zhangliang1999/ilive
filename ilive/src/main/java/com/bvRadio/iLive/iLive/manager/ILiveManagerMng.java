package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.action.front.vo.AppUserInfo;
import com.bvRadio.iLive.iLive.action.front.vo.ThirdLoginVo;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.jwzt.jssdk.WechatUserInfo;

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
	 * 根据手机号获取信息
	 */
	public List<ILiveManager> getILiveMangerListByMobile(String mobile);

	/**
	 * 注册用户
	 * 
	 * @param username
	 * @param password
	 */
	public long registeredManager(String username, String password, Integer validType,String terminalType);
	/**
	 * 智慧校園注册用户
	 * 
	 * @param username
	 * @param password
	 */
	public long zhxyregisteredManager(String username, String password, Integer validType,String terminalType,String zhxyUserId,String zhxyappId);
	/**
	 * 校验登录
	 * 
	 * @param loginToken
	 * @param userId
	 * @return
	 */
	public ILiveManager checkLogin(String loginToken, Long userId);

	/**
	 * 更新用户
	 * 
	 * @param manager
	 */
	public void updateLiveManager(ILiveManager manager);

	/**
	 * 注册用户ID
	 * 
	 * @param phoneNum
	 * @param password
	 * @param i
	 * @param type
	 * @return
	 */
	public long registeredManager(String phoneNum, String password, int i, Integer type,String terminalType);

	/**
	 * 根据用户名找到用户
	 * 
	 * @param userName
	 * @return
	 */
	public ILiveManager getILiveManagerByUserName(String userName);

	/**
	 * 获得用户根据微信Id
	 * 
	 * @param wxOpenId
	 * @return
	 */
	public ILiveManager getILiveMangerByWxOpenId(String wxOpenId);

	/**
	 * 根据微信注册用户
	 * 
	 * @param obj
	 */
	public ILiveManager registeredManagerByWx(WechatUserInfo wechatUserInfo);

	/**
	 * 根据loginToken和用户ID获得token
	 * @param userId
	 * @param loginToken
	 * @return
	 */
	public ILiveManager getILiveMangerByLoginToken(Long userId, String loginToken);

	
	/**
	 * 批量查询简单用户
	 * @param userIds
	 * @return
	 */
	public List<AppUserInfo> batchQueryUserId(Long[] userIds);

	
	/**
	 * 获取unionID
	 * @param unionId
	 * @return
	 */
	public ILiveManager getILiveMangerByWxUnionId(String unionId);

	public ILiveManager registeredManagerByWxUnionId(ThirdLoginVo thirdLoginVo);
	
	/**
	 * 根据直播间ID 获取数据
	 * @param roomId 直播间ID
	 * @param level 
	 * @return
	 * @throws Exception 
	 */
	public List<ILiveManager> selectILiveManagerByRoomId(Integer roomId, Integer level) throws Exception;
	/**
	 * 新增用户
	 * @param roomId 直播间ID
	 * @param userName 用户名称
	 * @param password 密码
	 * @param userImage 头像
	 * @param enterpriseId 企业ID
	 */
	public void addILiveManager(Integer roomId, String userName, String password, String userImage,Integer level, Integer enterpriseId);
	/**
	 * 根据手机号和账户获取数据
	 * @param userName
	 * @param phoneNum
	 * @return
	 */
	public ILiveManager selectILiveManagerByPhoneNumAndUserName(
			String userName, String phoneNum);
	/**
	 * 真实删除用户
	 * @param userId
	 * @return
	 */
	public void deleteILiveManager(Long userId);

	public List<ILiveSubLevel> selectILiveSubById(Long userId);

	public List<ILiveManager> getILiveManagerById(Long userId);

	public List<ILiveManager> getILiveManagerByEnterpriseId(Integer enterpriseId);

	public ILiveManager getILiveMangerByPhoneNumber(String phoneNum);
	
	/**
	 * 根据电信掌上营业厅的userid查询
	 * @param appid
	 * @return
	 */
	public ILiveManager getILiveManagerByDXID(String appid);
	
	/**
	 * 保存电信掌上营业厅的账户
	 * @param iLiveManager
	 * @return
	 */
	public ILiveManager savedxapp(ILiveManager iLiveManager);
	/**
	 * 根据智慧校園傳過來的userId和appId查詢用戶信息
	 * @param appId
	 * @param UserId
	 * @return
	 */
	public ILiveManager getILiveMangerByPhoneNumber(String appId,String UserId);
	/**
	 * 根据智慧校園傳過來的appId查詢用戶信息
	 * @param appId
	 * @param UserId
	 * @return
	 */
	public ILiveManager getILiveMangerByAppId(String appId);

	long zhxyregisteredManager(String username, String password, Integer validType, String terminalType,
			String zhxyUserId, String zhxyappId, String nailName);
}
