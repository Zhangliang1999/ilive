package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.EnterpriseStatisticResultDao;
import com.jwzt.statistic.entity.EnterpriseStatisticResult;
import com.jwzt.statistic.manager.EnterpriseStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class EnterpriseStatisticResultMngImpl implements EnterpriseStatisticResultMng {

	@Override
	@Transactional(readOnly = true)
	public EnterpriseStatisticResult getLastBeanByEndTime(Integer enterpriseId, Date endTime) {
		return dao.getLastBeanByEndTime(enterpriseId, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EnterpriseStatisticResult> listByParams(Integer enterpriseId, Date startTime, Date endTime) {
		return dao.listByParams(enterpriseId, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public EnterpriseStatisticResult sumByParams(Integer enterpriseId, Date startTime, Date endTime) {
		return dao.sumByParams(enterpriseId, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public EnterpriseStatisticResult getBeanById(Long id) {
		EnterpriseStatisticResult bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional(readOnly = true)
	public EnterpriseStatisticResult sumByLiveMostEnterprise() {
		return dao.sumByLiveMostEnterprise();
	}

	@Override
	@Transactional(readOnly = true)
	public int enterpriseByMost() {
		return dao.enterpriseByMost();
	}

	@Override
	@Transactional
	public EnterpriseStatisticResult save(EnterpriseStatisticResult bean) {
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
	public void update(EnterpriseStatisticResult bean) {
		if (null != bean) {
			Updater<EnterpriseStatisticResult> updater = new Updater<EnterpriseStatisticResult>(bean);
			dao.updateByUpdater(updater);
		}
	}

	@Autowired
	private EnterpriseStatisticResultDao dao;

}
