package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.action.front.vo.AppUserInfo;
import com.bvRadio.iLive.iLive.action.front.vo.ThirdLoginVo;
import com.bvRadio.iLive.iLive.dao.ILiveManagerDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.exception.ManagerExsitException;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.util.SystemMd5Util;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.jwzt.jssdk.EmojiFilter;
import com.jwzt.jssdk.WechatUserInfo;

@Service
@Transactional
public class ILiveManagerMngImpl implements ILiveManagerMng {

	/**
	 * 日志
	 */
	Logger logger = LoggerFactory.getLogger(ILiveManagerMngImpl.class);

	/**
	 * 
	 */
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveEnterpriseFansMng enterpriseFansMng;

	@Override
	@Transactional(readOnly = true)
	public ILiveManager getILiveManager(Long managerId) {
		return iLiveManagerDao.getILiveManager(managerId);
	}

	@Override
	public boolean saveIliveManager(ILiveManager manager) {
		boolean ret=false;
		try {
			iLiveManagerDao.saveManager(manager);
			ret=true;
		} catch (Exception e) {
			logger.error("注册用户" + e.toString());
		}
		return ret;
	}

	@Override
	public ILiveManager selectILiveManagerById(Long userId) {
		ILiveManager iLiveManager = iLiveManagerDao.selectILiveManagerById(userId);
		return iLiveManager;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveManager getILiveMangerByMobile(String mobile) {
		return iLiveManagerDao.getILiveMangerByMobile(mobile);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ILiveManager> getILiveMangerListByMobile(String mobile) {
		return iLiveManagerDao.getILiveMangerListByMobile(mobile);
	}
	/**
	 * 接口实现方法添加了同步锁 synchronized 2018-11-12 09:56:04
	 * 防止页面无反应的时候多次注册
	 */
	@Override
	public synchronized long registeredManager(String username, String password, Integer validType, String terminalType) {
		ILiveManager iLiveMangerByMobile = iLiveManagerDao.getILiveMangerByPhoneNumber(username);
		if (iLiveMangerByMobile != null) {
			throw new ManagerExsitException();
		}
		iLiveMangerByMobile = new ILiveManager();
		long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
		iLiveMangerByMobile.setUserId(userId);
		String salt = String.valueOf(new Random().nextInt(randomTime));
		iLiveMangerByMobile.setCertStatus(0);
		if (validType.equals(1)) {
			iLiveMangerByMobile.setMobile(username);
		} else {
			iLiveMangerByMobile.setMobile(username);
		}
		try {
			String nailNamePrefix = username.substring(0, 3);
			String nailNameSuffix = username.substring(7);
			iLiveMangerByMobile.setNailName(nailNamePrefix + "****" + nailNameSuffix);
		} catch (Exception e) {
			iLiveMangerByMobile.setNailName("");
		}
		iLiveMangerByMobile.setSalt(salt);
		// 15011547890
		if (password != null) {
			String md5Pwd = SystemMd5Util.md5(password, salt);
			iLiveMangerByMobile.setUserPwd(md5Pwd);
		}
		iLiveMangerByMobile.setIsDel(0);
		String userImg = ConfigUtils.get("defaultTerminalUserImg");
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setWxOpenId("");
		iLiveMangerByMobile.setTerminalType(terminalType);
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveMangerByMobile.setJpushId(username == null ? "" : username);
		iLiveMangerByMobile.setUpdatePasswordTime(new Timestamp(new Date().getTime()));
		iLiveManagerDao.saveManager(iLiveMangerByMobile);

		/**
		 * 校验类型
		 */
		return userId;
	}

	@Autowired
	private ILiveManagerDao iLiveManagerDao;

	@Autowired
	private ILiveSubLevelDao iLiveSubLevelDao;

	@Autowired
	private ILiveFieldIdManagerMng fieldManagerMng;

	private static final Integer randomTime = 2000;

	@Override
	@Transactional(readOnly = true)
	public ILiveManager checkLogin(String loginToken, Long userId) {
		return iLiveManagerDao.checkLogin(loginToken, userId);
	}

	@Override
	public void updateLiveManager(ILiveManager manager) {
		iLiveManagerDao.updateLiveManager(manager);
	}
	/**
	 * 接口实现方法添加了同步锁 synchronized 2018-11-12 09:56:04
	 * 防止页面无反应的时候多次注册
	 */
	@Override
	public synchronized long registeredManager(String phoneNum, String password, int validType, Integer type, String terminalType) {
		ILiveManager iLiveMangerByMobile = iLiveManagerDao.getILiveMangerByMobile(phoneNum);
		if (iLiveMangerByMobile != null) {
			throw new ManagerExsitException();
		}
		iLiveMangerByMobile = new ILiveManager();
		long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
		iLiveMangerByMobile.setUserId(userId);
		String salt = String.valueOf(new Random().nextInt(randomTime));
		iLiveMangerByMobile.setMobile(phoneNum);
		try {
			String nailNamePrefix = phoneNum.substring(0, 3);
			String nailNameSuffix = phoneNum.substring(7);
			iLiveMangerByMobile.setNailName(nailNamePrefix + "****" + nailNameSuffix);
		} catch (Exception e) {
			iLiveMangerByMobile.setNailName("");
		}
		// 15011547890
		iLiveMangerByMobile.setSalt(salt);
		if (type != 2) {
			if (password != null) {
				String md5Pwd = SystemMd5Util.md5(password, salt);
				iLiveMangerByMobile.setUserPwd(md5Pwd);
			}
		}
		iLiveMangerByMobile.setIsDel(0);
		String userImg = ConfigUtils.get("defaultTerminalUserImg");
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setCertStatus(0);
		iLiveMangerByMobile.setTerminalType(terminalType);
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		iLiveMangerByMobile.setWxOpenId("");
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveMangerByMobile.setJpushId(phoneNum);
		iLiveManagerDao.saveManager(iLiveMangerByMobile);
		/**
		 * 加关注一个企业
		 */
		try {
			int defaultEnterpriseId = Integer.parseInt(ConfigUtils.get("defaultConcernEnterpriseId"));
			enterpriseFansMng.addEnterpriseConcern(defaultEnterpriseId, userId);
		} catch (Exception e) {
			logger.error("注册用户关注的时候发生异常" + e.toString());
		}
		return userId;
	}

	@Transactional(readOnly = true)
	@Override
	public ILiveManager getILiveManagerByUserName(String userName) {
		return iLiveManagerDao.getILiveManagerByUserName(userName);
	}

	@Override
	public ILiveManager getILiveMangerByWxOpenId(String wxOpenId) {
		return iLiveManagerDao.getILiveMangerByWxOpenId(wxOpenId);
	}

	@Override
	public ILiveManager registeredManagerByWx(WechatUserInfo wechatUserInfo) {
		ILiveManager iLiveMangerByMobile = new ILiveManager();
		long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
		String salt = String.valueOf(new Random().nextInt(randomTime));
		String nailName = wechatUserInfo.getNickName();
		String userImg = wechatUserInfo.getHeadimgurl();
		if (userImg == null || "".equals(userImg)) {
			userImg = ConfigUtils.get("defaultTerminalUserImg");
		}
		iLiveMangerByMobile.setNailName(EmojiFilter.filterEmoji(nailName));
		iLiveMangerByMobile.setUserId(userId);
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setCertStatus(0);
		// 15011547890
		iLiveMangerByMobile.setSalt(salt);
		iLiveMangerByMobile.setIsDel(0);
		iLiveMangerByMobile.setWxUnionId(wechatUserInfo.getUnionId());
		iLiveMangerByMobile.setMobile("");
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		iLiveMangerByMobile.setTerminalType("weixin");
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveManagerDao.saveManager(iLiveMangerByMobile);
		/**
		 * 加关注一个企业
		 */
		try {
			int defaultEnterpriseId = Integer.parseInt(ConfigUtils.get("defaultConcernEnterpriseId"));
			enterpriseFansMng.addEnterpriseConcern(defaultEnterpriseId, userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("微信注册用户关注的时候发生异常" + e.toString());
		}
		return iLiveMangerByMobile;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveManager getILiveMangerByLoginToken(Long userId, String loginToken) {
		return iLiveManagerDao.getILiveMangerByLoginToken(userId, loginToken);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppUserInfo> batchQueryUserId(Long[] userIds) {
		List<AppUserInfo> appUserList = new ArrayList<>();
		List<ILiveManager> iiveManagerList = iLiveManagerDao.getIiveManagerList(userIds);
		if (iiveManagerList != null && !iiveManagerList.isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (ILiveManager managerUser : iiveManagerList) {
				AppUserInfo appUser = this.convertManager2AppUser(managerUser, sdf);
				appUser.setTerminalType(managerUser.getTerminalType() == null ? "h5" : managerUser.getTerminalType());
				appUserList.add(appUser);
			}
		}
		return appUserList;
	}

	private AppUserInfo convertManager2AppUser(ILiveManager managerUser, SimpleDateFormat sdf) {
		AppUserInfo appUser = new AppUserInfo();
		appUser.setNailName(managerUser.getNailName());
		appUser.setPhoneNum(managerUser.getMobile());
		appUser.setUserImg(managerUser.getUserImg());
		appUser.setUserId(managerUser.getUserId());
		appUser.setRegisterTime(sdf.format(managerUser.getCreateTime()));
		return appUser;
	}

	@Override
	public ILiveManager getILiveMangerByWxUnionId(String unionId) {
		return iLiveManagerDao.getILiveMangerByWxUnionId(unionId);
	}

	@Override
	public ILiveManager registeredManagerByWxUnionId(ThirdLoginVo thirdLoginVo) {
		ILiveManager iLiveMangerByMobile = new ILiveManager();
		long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
		String salt = String.valueOf(new Random().nextInt(randomTime));
		String nailName = thirdLoginVo.getName();
		String userImg = thirdLoginVo.getUser_img();
		if (userImg == null || "".equals(userImg)) {
			userImg = ConfigUtils.get("defaultTerminalUserImg");
		}
		iLiveMangerByMobile.setNailName(EmojiFilter.filterEmoji(nailName));
		iLiveMangerByMobile.setUserId(userId);
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setCertStatus(0);
		// 15011547890
		iLiveMangerByMobile.setSalt(salt);
		iLiveMangerByMobile.setIsDel(0);
		iLiveMangerByMobile.setWxUnionId(thirdLoginVo.getUnionid());
		iLiveMangerByMobile.setMobile("");
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		iLiveMangerByMobile.setTerminalType("weixin");
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveManagerDao.saveManager(iLiveMangerByMobile);
		/**
		 * 加关注一个企业
		 */
		try {
			int defaultEnterpriseId = Integer.parseInt(ConfigUtils.get("defaultConcernEnterpriseId"));
			enterpriseFansMng.addEnterpriseConcern(defaultEnterpriseId, userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("微信注册用户关注的时候发生异常" + e.toString());
		}
		return iLiveMangerByMobile;
	}

	@Override
	public List<ILiveManager> selectILiveManagerByRoomId(Integer roomId,Integer level) throws Exception {
		List<ILiveManager> list = iLiveManagerDao.selectILiveManagerByRoomId(roomId,level);
		return list;
	}

	@Override
	public void addILiveManager(Integer roomId, String userName,
			String password, String userImage,Integer level,Integer enterpriseId) {
		ILiveManager iLiveManager = new ILiveManager();
		long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
		iLiveManager.setUserId(userId);
		String salt = String.valueOf(new Random().nextInt(randomTime));
		iLiveManager.setUserName(userName);
		iLiveManager.setNailName(userName);
		iLiveManager.setSalt(salt);
		String md5Pwd = SystemMd5Util.md5(password, salt);
		iLiveManager.setUserPwd(md5Pwd);
		iLiveManager.setSaltPwd(password);
		iLiveManager.setIsDel(0);
		if(userImage==null||userImage==""){
			userImage = ConfigUtils.get("defaultTerminalUserImg");
		}
		iLiveManager.setUserImg(userImage);
		iLiveManager.setCertStatus(1);
		iLiveManager.setTerminalType("web");
		iLiveManager.setCreateTime(new Timestamp(System.currentTimeMillis()));
		iLiveManager.setWxOpenId("");
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		iLiveManager.setEnterPrise(enterPrise);
		iLiveManager.setJpushId(userName);
		iLiveManager.setLevel(level);
		if(level.equals(ILiveManager.USER_LEVEL_Assistant)){
			iLiveManager.setMobile(userName);
		}
		iLiveManager.setRoomId(roomId);
		iLiveManagerDao.saveManager(iLiveManager);
		/**
		 * 加关注一个企业
		 */
		try {
			enterpriseFansMng.addEnterpriseConcern(enterpriseId, userId);
		} catch (Exception e) {
			logger.error("注册用户关注的时候发生异常" + e.toString());
		}
	}

	@Override
	public ILiveManager selectILiveManagerByPhoneNumAndUserName(
			String userName, String phoneNum) {
		ILiveManager iLiveManager = iLiveManagerDao.selectILiveManagerByPhoneNumAndUserName(userName,phoneNum);
		return iLiveManager;
	}

	@Override
	public void deleteILiveManager(Long userId) {
		iLiveManagerDao.deleteILiveManager(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveSubLevel> selectILiveSubById(Long userId) {
		return iLiveSubLevelDao.selectILiveSubById(userId);
	}

	@Override
	public List<ILiveManager> getILiveManagerById(Long userId) {
		return iLiveManagerDao.getILiveManagerById(userId);
	}
	@Override
	@Transactional(readOnly = true)
	public List<ILiveManager> getILiveManagerByEnterpriseId(Integer enterpriseId) {
		return iLiveManagerDao.getILiveManagerByEnterpriseId(enterpriseId);
	}

	@Override
	public ILiveManager getILiveMangerByPhoneNumber(String phoneNum) {
		return iLiveManagerDao.getILiveMangerByPhoneNumber(phoneNum);
	}

	@Override
	public ILiveManager getILiveManagerByDXID(String appid) {
		return iLiveManagerDao.getILiveManagerByDXID(appid);
	}

	@Override
	public ILiveManager savedxapp(ILiveManager iLiveManager) {
		ILiveManager iLiveMangerByMobile = new ILiveManager();
		long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
		String salt = String.valueOf(new Random().nextInt(randomTime));
		if (iLiveManager.getUserImg() == null || "".equals(iLiveManager.getUserImg())) {
			iLiveManager.setUserImg(ConfigUtils.get("defaultTerminalUserImg"));
		}
		iLiveMangerByMobile.setDxzyappId(iLiveManager.getDxzyappId());
		iLiveMangerByMobile.setUserId(userId);
		iLiveMangerByMobile.setCertStatus(0);
		// 15011547890
		iLiveMangerByMobile.setSalt(salt);
		iLiveMangerByMobile.setIsDel(0);
		iLiveMangerByMobile.setMobile("");
		iLiveMangerByMobile.setNailName(iLiveManager.getNailName());
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveManagerDao.saveManager(iLiveMangerByMobile);
		/**
		 * 加关注一个企业
		 */
		try {
			int defaultEnterpriseId = Integer.parseInt(ConfigUtils.get("defaultConcernEnterpriseId"));
			enterpriseFansMng.addEnterpriseConcern(defaultEnterpriseId, userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("微信注册用户关注的时候发生异常" + e.toString());
		}
		return iLiveMangerByMobile;
	}

	@Override
	public ILiveManager getILiveMangerByPhoneNumber(String appId, String UserId) {
		// TODO Auto-generated method stub
		return iLiveManagerDao.getILiveMangerByPhoneNumber(appId,UserId);
	}

	@Override
	public long zhxyregisteredManager(String username, String password, Integer validType, String terminalType,
			String zhxyUserId, String zhxyappId,String nailName) {
		ILiveManager iLiveMangerByMobile = iLiveManagerDao.getILiveMangerByPhoneNumber(username);
		if (iLiveMangerByMobile != null) {
			throw new ManagerExsitException();
		}
		iLiveMangerByMobile = new ILiveManager();
		long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
		iLiveMangerByMobile.setUserId(userId);
		String salt = String.valueOf(new Random().nextInt(randomTime));
		iLiveMangerByMobile.setCertStatus(0);
		if (validType.equals(1)) {
			iLiveMangerByMobile.setMobile(username);
		} else {
			iLiveMangerByMobile.setMobile(username);
		}
		
			iLiveMangerByMobile.setNailName(nailName);
		
		iLiveMangerByMobile.setSalt(salt);
		// 15011547890
		if (password != null) {
			String md5Pwd = SystemMd5Util.md5(password, salt);
			iLiveMangerByMobile.setUserPwd(md5Pwd);
		}
		iLiveMangerByMobile.setIsDel(0);
		String userImg = ConfigUtils.get("defaultTerminalUserImg");
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setWxOpenId("");
		iLiveMangerByMobile.setTerminalType(terminalType);
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveMangerByMobile.setJpushId(username == null ? "" : username);
		iLiveMangerByMobile.setUpdatePasswordTime(new Timestamp(new Date().getTime()));
		iLiveMangerByMobile.setZhxyappId(zhxyappId);
		iLiveMangerByMobile.setZhxyuserId(zhxyUserId);
		iLiveManagerDao.saveManager(iLiveMangerByMobile);

		/**
		 * 校验类型
		 */
		return userId;
	}

	@Override
	public ILiveManager getILiveMangerByAppId(String appId) {
		// TODO Auto-generated method stub
		return iLiveManagerDao.getILiveMangerByAppId(appId);
	}

	@Override
	public long zhxyregisteredManager(String username, String password, Integer validType, String terminalType,
			String zhxyUserId, String zhxyappId) {
		ILiveManager iLiveMangerByMobile = iLiveManagerDao.getILiveMangerByPhoneNumber(username);
		if (iLiveMangerByMobile != null) {
			throw new ManagerExsitException();
		}
		iLiveMangerByMobile = new ILiveManager();
		long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
		iLiveMangerByMobile.setUserId(userId);
		String salt = String.valueOf(new Random().nextInt(randomTime));
		iLiveMangerByMobile.setCertStatus(0);
		if (validType.equals(1)) {
			iLiveMangerByMobile.setMobile(username);
		} else {
			iLiveMangerByMobile.setMobile(username);
		}
		try {
			String nailNamePrefix = username.substring(0, 3);
			String nailNameSuffix = username.substring(7);
			iLiveMangerByMobile.setNailName(nailNamePrefix + "****" + nailNameSuffix);
		} catch (Exception e) {
			iLiveMangerByMobile.setNailName("");
		}
		iLiveMangerByMobile.setSalt(salt);
		// 15011547890
		if (password != null) {
			String md5Pwd = SystemMd5Util.md5(password, salt);
			iLiveMangerByMobile.setUserPwd(md5Pwd);
		}
		iLiveMangerByMobile.setIsDel(0);
		String userImg = ConfigUtils.get("defaultTerminalUserImg");
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setWxOpenId("");
		iLiveMangerByMobile.setTerminalType(terminalType);
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveMangerByMobile.setJpushId(username == null ? "" : username);
		iLiveMangerByMobile.setUpdatePasswordTime(new Timestamp(new Date().getTime()));
		iLiveMangerByMobile.setZhxyappId(zhxyappId);
		iLiveMangerByMobile.setZhxyuserId(zhxyUserId);
		iLiveManagerDao.saveManager(iLiveMangerByMobile);

		/**
		 * 校验类型
		 */
		return userId;
	}
	
	
	
}
