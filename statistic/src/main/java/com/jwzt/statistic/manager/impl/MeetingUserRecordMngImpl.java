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
import com.jwzt.statistic.dao.MeetingUserRecordDao;
import com.jwzt.statistic.entity.MeetingUserRecord;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.manager.MeetingUserRecordMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class MeetingUserRecordMngImpl implements MeetingUserRecordMng {

	@Override
	@Transactional(readOnly = true)
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType) {
		return dao.countUserNum(ids, startTimeOfRegisterTime, endTimeOfRegisterTime, terminalType);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MeetingUserRecord> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime) {
		return dao.listByParams(ids, username, startTimeOfRegisterTime, endTimeOfRegisterTime);
	}

	@Override
	@Transactional(readOnly = true)
	public MeetingUserRecord getBeanById(Long id) {
		MeetingUserRecord bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional
	public MeetingUserRecord save(MeetingUserRecord bean) {
		return dao.save(bean);
	}

	

	

	@Autowired
	private MeetingUserRecordDao dao;





	@Override
	public Long getBean(Integer loginType) {
		// TODO Auto-generated method stub
		return dao.getBean(loginType);
	}
}
