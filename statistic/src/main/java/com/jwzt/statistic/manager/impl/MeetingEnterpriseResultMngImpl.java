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
import com.jwzt.statistic.dao.MeetingEnterpriseResultDao;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.manager.MeetingEnterpriseResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class MeetingEnterpriseResultMngImpl implements MeetingEnterpriseResultMng {

	@Override
	@Transactional(readOnly = true)
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType) {
		return dao.countUserNum(ids, startTimeOfRegisterTime, endTimeOfRegisterTime, terminalType);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MeetingEnterpriseResult> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime) {
		return dao.listByParams(ids, username, startTimeOfRegisterTime, endTimeOfRegisterTime);
	}

	@Override
	@Transactional(readOnly = true)
	public MeetingEnterpriseResult getBeanById(Long id) {
		MeetingEnterpriseResult bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional
	public MeetingEnterpriseResult save(MeetingEnterpriseResult bean) {
		return dao.save(bean);
	}

	

	

	@Autowired
	private MeetingEnterpriseResultDao dao;





	@Override
	@Transactional(readOnly = true)
	public Long getBean(Integer loginType) {
		// TODO Auto-generated method stub
		return dao.getBean(loginType);
	}

	@Override
	@Transactional(readOnly = true)
	public MeetingEnterpriseResult getBeanByEnterpriseId(Long enterpriseId) {
		// TODO Auto-generated method stub
		return dao.getBeanByEnterpriseId(enterpriseId);
	}

	@Override
	@Transactional
	public MeetingEnterpriseResult update(MeetingEnterpriseResult bean) {
		// TODO Auto-generated method stub
		return dao.update(bean);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MeetingEnterpriseResult> getList() {
		// TODO Auto-generated method stub
		return dao.getList();
	}
}
