package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.MinuteStatisticResultDao;
import com.jwzt.statistic.entity.MinuteStatisticResult;
import com.jwzt.statistic.manager.MinuteStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class MinuteStatisticResultMngImpl implements MinuteStatisticResultMng {

	@Override
	@Transactional(readOnly = true)
	public MinuteStatisticResult getBeanWithMaxMinuteUserNum(Long liveEventId, Date startTime, Date endTime) {
		MinuteStatisticResult bean = dao.getBeanWithMaxMinuteUserNum(liveEventId, startTime, endTime);
		return bean;
	}

	@Override
	@Transactional(readOnly = true)
	public MinuteStatisticResult getBeanByParams(Long liveEventId, Date startTime, Date endTime) {
		MinuteStatisticResult bean = dao.getBeanByParams(liveEventId, startTime, endTime);
		return bean;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MinuteStatisticResult> listByLiveEventId(Long liveEventId) {
		List<MinuteStatisticResult> list = dao.listByLiveEventId(liveEventId);
		return list;
	}

	@Override
	@Transactional
	public MinuteStatisticResult save(MinuteStatisticResult bean) {
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
	public void update(MinuteStatisticResult bean) {
		if (null != bean) {
			Updater<MinuteStatisticResult> updater = new Updater<MinuteStatisticResult>(bean);
			dao.updateByUpdater(updater);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public MinuteStatisticResult getBeanWithMaxMinuteUserNumByEnterpriseId(Long enterpriseId, Date startTime, Date endTime) {
		MinuteStatisticResult bean = dao.getBeanWithMaxMinuteUserNumByEnterpriseId(enterpriseId, startTime, endTime);
		return bean;
	}

	@Autowired
	private MinuteStatisticResultDao dao;
}
