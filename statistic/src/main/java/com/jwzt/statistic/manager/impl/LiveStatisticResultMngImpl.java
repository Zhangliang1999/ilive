package com.jwzt.statistic.manager.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.LiveStatisticResultDao;
import com.jwzt.statistic.entity.LiveStatisticResult;
import com.jwzt.statistic.manager.LiveStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class LiveStatisticResultMngImpl implements LiveStatisticResultMng {

	@Override
	@Transactional(readOnly = true)
	public Long countCommentNumForLiveDatabase(Long liveEventId) {
		return dao.countCommentNumForLiveDatabase(liveEventId);
	}

	@Override
	@Transactional(readOnly = true)
	public Long countLikeNumForLiveDatabase(Long liveEventId) {
		return dao.countLikeNumForLiveDatabase(liveEventId);
	}

	@Override
	@Transactional(readOnly = true)
	public LiveStatisticResult getBeanWithMaxNewRegisterUserNum(Date startTime, Date endTime) {
		return dao.getBeanWithMaxNewRegisterUserNum(startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public LiveStatisticResult getBeanById(Long id) {
		LiveStatisticResult bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional
	public LiveStatisticResult save(LiveStatisticResult bean) {
		if (null != bean) {
			bean.initFieldValue();
			return dao.save(bean);
		}
		return null;
	}

	@Override
	@Transactional
	public void update(LiveStatisticResult bean) {
		if (null != bean) {
			Updater<LiveStatisticResult> updater = new Updater<LiveStatisticResult>(bean);
			dao.updateByUpdater(updater);
		}
	}

	@Autowired
	private LiveStatisticResultDao dao;
}
