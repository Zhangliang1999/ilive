package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.TotalStatisticResultDao;
import com.jwzt.statistic.entity.TotalStatisticResult;
import com.jwzt.statistic.manager.TotalStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class TotalStatisticResultMngImpl implements TotalStatisticResultMng {

	@Override
	@Transactional(readOnly = true)
	public TotalStatisticResult getLastBeanByEndTime(Date endTime) {
		return dao.getLastBeanByEndTime(endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TotalStatisticResult> listByParams(Date startTime, Date endTime) {
		return dao.listByParams(startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public TotalStatisticResult sumByParams(Date startTime, Date endTime) {
		return dao.sumByParams(startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public TotalStatisticResult getBeanById(Long id) {
		TotalStatisticResult bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional
	public TotalStatisticResult save(TotalStatisticResult bean) {
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
	public void update(TotalStatisticResult bean) {
		if (null != bean) {
			Updater<TotalStatisticResult> updater = new Updater<TotalStatisticResult>(bean);
			dao.updateByUpdater(updater);
		}
	}

	@Autowired
	private TotalStatisticResultDao dao;
}
