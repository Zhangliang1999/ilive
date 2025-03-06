package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.MeetingStatisticResultDao;
import com.jwzt.statistic.entity.MeetingStatisticResult;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.entity.vo.MeetingHoursResult;
import com.jwzt.statistic.entity.vo.MeetingStasticShowResult;
import com.jwzt.statistic.manager.MeetingStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class MeetingStatisticResultMngImpl implements MeetingStatisticResultMng {

	@Override
	@Transactional(readOnly = true)
	public MeetingStatisticResult getLastBeanByEndTime(Date endTime) {
		return dao.getLastBeanByEndTime(endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MeetingStatisticResult> listByParams(Date startTime, Date endTime) {
		return dao.listByParams(startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public MeetingStasticShowResult sumByParams() {
		return dao.sumByParams();
	}

	@Override
	@Transactional(readOnly = true)
	public MeetingStatisticResult getBeanById(Long id) {
		MeetingStatisticResult bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional
	public MeetingStatisticResult save(MeetingStatisticResult bean) {
		if (null != bean) {
			String id = UUID.randomUUID().toString().replace("-", "");
			bean.setId(id);
			return dao.save(bean);
		}
		return null;
	}

	@Override
	@Transactional
	public void update(MeetingStatisticResult bean) {
		if (null != bean) {
			Updater<MeetingStatisticResult> updater = new Updater<MeetingStatisticResult>(bean);
			dao.updateByUpdater(updater);
		}
	}

	@Autowired
	private MeetingStatisticResultDao dao;

	@Override
	public Long getBeanByMeetingTime(Long minNum, Long maxNum) {
		// TODO Auto-generated method stub
		return dao.getBeanByMeetingTime(minNum,maxNum);
	}

	@Override
	public List<MeetingEnterpriseResult> top10() {
		// TODO Auto-generated method stub
		return dao.top10();
	}

	@Override
	public Long getBeanByRecordOrRedirect(Integer record, Integer redirect) {
		// TODO Auto-generated method stub
		return dao.getBeanByRecordOrRedirect(record,redirect);
	}

	@Override
	public MeetingEnterpriseResult top10ByIndex(Integer index) {
		// TODO Auto-generated method stub
		return dao.top10ByIndex(index);
	}

	@Override
	public Long getBeanByEnterpriseId(Long enterpriseId) {
		// TODO Auto-generated method stub
		return dao.getBeanByEnterpriseId(enterpriseId);
	}

	@Override
	public Long getBeanByFigure(Integer figure) {
		// TODO Auto-generated method stub
		return dao.getBeanByFigure(figure);
	}

	
}
