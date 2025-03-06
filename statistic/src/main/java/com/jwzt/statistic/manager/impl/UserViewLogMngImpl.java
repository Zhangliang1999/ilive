package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.UserViewLogDao;
import com.jwzt.statistic.entity.UserViewLog;
import com.jwzt.statistic.manager.UserViewLogMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class UserViewLogMngImpl implements UserViewLogMng {

	@Override
	@Transactional(readOnly = true)
	public Long sumViewDuarationByParams(Long liveEventId, Integer enterpriseId, Date startTime, Date endTime) {
		return dao.sumViewDuarationByParams(liveEventId, enterpriseId, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public Long countUserNumByParams(Long liveEventId, Integer enterpriseId, Long startDuration, Long endDuration,
			Date startTime, Date endTime) {
		return dao.countUserNumByParams(liveEventId, enterpriseId, startDuration, endDuration, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserViewLog> listByLiveEventId(Long liveEventId) {
		List<UserViewLog> list = dao.listByLiveEventId(liveEventId);
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public UserViewLog getBeanByParams(Long liveEventId, Integer requestType, Integer userType, String userId) {
		return dao.getBeanByParams(liveEventId, requestType, userType, userId);
	}

	@Override
	@Transactional
	public UserViewLog save(UserViewLog bean) {
		if (null != bean) {
			String id = UUID.randomUUID().toString().replace("-", "");
			bean.setId(id);
			bean.initFieldValue();
			return dao.save(bean);
		}
		return null;
	}

	@Override
	@Transactional
	public void update(UserViewLog bean) {
		if (null != bean) {
			Updater<UserViewLog> updater = new Updater<UserViewLog>(bean);
			dao.updateByUpdater(updater);
		}
	}

	@Autowired
	private UserViewLogDao dao;
}
