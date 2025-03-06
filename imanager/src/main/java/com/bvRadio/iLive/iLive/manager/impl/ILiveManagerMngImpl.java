package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.exception.ManagerExsitException;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Service
@Transactional
public class ILiveManagerMngImpl implements ILiveManagerMng {

	/**
	 * 
	 */
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Override
	@Transactional(readOnly = true)
	public ILiveManager getILiveManager(Long managerId) {

		return iLiveManagerDao.getILiveManager(managerId);
	}

	@Override
	public boolean saveIliveManager(ILiveManager manager) {

		return false;
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
	public long registeredManager(String username, String password, Integer validType) {
		ILiveManager iLiveMangerByMobile = iLiveManagerDao.getILiveMangerByMobile(username);
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
			String md5Pwd = md5(password, salt);
			iLiveMangerByMobile.setUserPwd(md5Pwd);
		}
		iLiveMangerByMobile.setIsDel(0);
		String userImg = ConfigUtils.get("defaultTerminalUserImg");
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveMangerByMobile.setJpushId(username);
		iLiveManagerDao.saveManager(iLiveMangerByMobile);

		/**
		 * 校验类型
		 */
		// if (validType == 1) {
		// // 服务器组ID
		// // int serverGroupId = 100;
		// String serverGroupIdStr = ConfigUtils.get("defaultVodServerGroupId");
		// ILiveEvent liveEvent = new ILiveEvent();
		// liveEvent.setLiveTitle("试用房间");
		// liveEvent.setOpenBarrageSwitch(1);
		// liveEvent.setOpenCheckSwitch(1);
		// liveEvent.setLiveStartTime(new
		// Timestamp(System.currentTimeMillis()));
		// Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.DATE, 30);
		//
		// // 一个月 直播结束时间
		// liveEvent.setLiveEndTime(new Timestamp(calendar.getTimeInMillis()));
		// int liveRoomId = iLiveRoomMng.createNextId();
		// UserBean userBean = new UserBean();
		// userBean.setUsername(username);
		// userBean.setUserId(String.valueOf(userId));
		// userBean.setEnterpriseId(10);
		// iLiveRoomMng.saveRoom(liveEvent, liveRoomId,
		// Integer.parseInt(serverGroupIdStr), userBean);
		// }
		return userId;
	}

	@Autowired
	private ILiveManagerDao iLiveManagerDao;

	@Autowired
	private ILiveFieldIdManagerMng fieldManagerMng;

	private static final String hashAlgorithmName = "MD5";

	private static final Integer randomTime = 2000;

	public static String md5(String pass, String saltSource) {
		Object salt = new Md5Hash(saltSource);
		int hashIterations = 1024;
		Object result = new SimpleHash(hashAlgorithmName, pass, salt, hashIterations);
		String password = result.toString();
		return password;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveManager checkLogin(String loginToken, Long userId) {
		return iLiveManagerDao.checkLogin(loginToken, userId);
	}

	@Override
	public void updateLiveManager(ILiveManager manager) {
		iLiveManagerDao.updateLiveManager(manager);
	}

	@Override
	public long registeredManager(String phoneNum, String password, int validType, Integer type) {
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
				String md5Pwd = md5(password, salt);
				iLiveMangerByMobile.setUserPwd(md5Pwd);
			}
		}
		iLiveMangerByMobile.setIsDel(0);
		String userImg = ConfigUtils.get("defaultTerminalUserImg");
		iLiveMangerByMobile.setUserImg(userImg);
		iLiveMangerByMobile.setCertStatus(0);
		iLiveMangerByMobile.setCreateTime(new Timestamp(System.currentTimeMillis()));
		ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
		iLiveMangerByMobile.setEnterPrise(enterPrise);
		iLiveMangerByMobile.setJpushId(phoneNum);
		iLiveManagerDao.saveManager(iLiveMangerByMobile);
		return userId;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveManager getILiveManagerByUserName(String userName) {
		return iLiveManagerDao.getILiveManagerByUserName(userName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveManager> getILiveManagerByEnterpriseId(Integer enterpriseId) {
		return iLiveManagerDao.getILiveManagerByEnterpriseId(enterpriseId);
	}

	@Override
	public Pagination getPage(ILiveManager user, Integer pageSize, Integer pageNo) {
		return iLiveManagerDao.getPage(user,pageSize,pageNo);
	}

	@Override
	public Pagination getUserRecord(ILiveManager iLiveManager, Integer pageNo, Integer pageSize) {
		return iLiveManagerDao.getUserRecord(iLiveManager,pageNo,pageSize);
	}

	@Override
	public List<ILiveManager> getUserRecordList(ILiveManager iLiveManager) {
		return iLiveManagerDao.getUserRecordList(iLiveManager);
	}

	@Override
	public void batchUpdate(List<ILiveManager> list) {
		for(ILiveManager user:list) {
			iLiveManagerDao.updateLiveManager(user);
		}
	}

}
