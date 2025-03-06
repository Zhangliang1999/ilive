package com.jwzt.statistic.manager.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.UserInfoDao;
import com.jwzt.statistic.entity.UserInfo;
import com.jwzt.statistic.manager.UserInfoMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class UserInfoMngImpl implements UserInfoMng {

	@Override
	@Transactional(readOnly = true)
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType) {
		return dao.countUserNum(ids, startTimeOfRegisterTime, endTimeOfRegisterTime, terminalType);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserInfo> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime) {
		return dao.listByParams(ids, username, startTimeOfRegisterTime, endTimeOfRegisterTime);
	}

	@Override
	@Transactional(readOnly = true)
	public UserInfo getBeanById(Long id) {
		UserInfo bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional
	public UserInfo save(UserInfo bean) {
		bean.initFieldValue();
		return dao.save(bean);
	}

	@Override
	@Transactional
	public UserInfo update(UserInfo bean) {
		if (null != bean) {
			Updater<UserInfo> updater = new Updater<UserInfo>(bean);
			return dao.updateByUpdater(updater);
		}
		return null;
	}

	@Override
	@Transactional
	public UserInfo saveOrUpdateFromDataMap(final Map<?, ?> dataMap) {

		String nickName = (String) dataMap.get("nailName");
		String userImg = (String) dataMap.get("userImg");
		String registerTime = (String) dataMap.get("registerTime");
		String phoneNum = (String) dataMap.get("phoneNum");
		String terminalTypeStr = (String) dataMap.get("terminalType");
		Integer terminalType = null;
		// ios,android,pc,h5,weixin
		if ("ios".equalsIgnoreCase(terminalTypeStr)) {
			terminalType = UserInfo.TERMINAL_TYPE_IOS;
		} else if ("android".equalsIgnoreCase(terminalTypeStr)) {
			terminalType = UserInfo.TERMINAL_TYPE_ANDROID;
		} else if ("pc".equalsIgnoreCase(terminalTypeStr)) {
			terminalType = UserInfo.TERMINAL_TYPE_PC;
		} else if ("h5".equalsIgnoreCase(terminalTypeStr)) {
			terminalType = UserInfo.TERMINAL_TYPE_H5;
		} else if ("weixin".equalsIgnoreCase(terminalTypeStr)) {
			terminalType = UserInfo.TERMINAL_TYPE_WEIXIN;
		}
		Object userIdObj = dataMap.get("userId");
		Long userId = null;
		if (null != userIdObj) {
			if (Long.class.isInstance(userIdObj)) {
				userId = (Long) userIdObj;
			} else {
				userId = ((Integer) userIdObj).longValue();
			}
		}
		UserInfo userInfo = getBeanById(userId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (null == userInfo) {
			userInfo = new UserInfo();
			userInfo.setId(userId);
			userInfo.setNickName(nickName);
			try {
				userInfo.setRegisterTime(new Timestamp(sdf.parse(registerTime).getTime()));
			} catch (ParseException e) {
				userInfo.setRegisterTime(null);
			}
			userInfo.setPhoneNum(phoneNum);
			userInfo.setUserImg(userImg);
			userInfo.setTerminalType(terminalType);
			userInfo = save(userInfo);
		} else {
			userInfo.setNickName(nickName);
			try {
				userInfo.setRegisterTime(new Timestamp(sdf.parse(registerTime).getTime()));
			} catch (ParseException e) {
				userInfo.setRegisterTime(null);
			}
			userInfo.setPhoneNum(phoneNum);
			userInfo.setUserImg(userImg);
			userInfo.setTerminalType(terminalType);
			userInfo = update(userInfo);
		}
		return userInfo;
	}

	@Autowired
	private UserInfoDao dao;
}
