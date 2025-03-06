package com.bvRadio.iLive.manager.manager.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.manager.dao.ReportAndComplainDao;
import com.bvRadio.iLive.manager.entity.ReportAndComplain;
import com.bvRadio.iLive.manager.manager.ReportAndComplainService;

@Service
@Transactional
public class ReportAndComplainServiceImpl implements ReportAndComplainService {

	@Autowired
	private ReportAndComplainDao reportAndComplainDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Override
	public Long save(ReportAndComplain reportAndComplain) {
		Long nextId = filedIdMng.getNextId("report_and_complain", "id", 1);
		Timestamp now = new Timestamp(new Date().getTime());
		reportAndComplain.setId(nextId);
		reportAndComplain.setCreateTime(now);
		reportAndComplain.setIsDel(0);
		reportAndComplain.setReplyStatus(0);
		reportAndComplainDao.save(reportAndComplain);
		return nextId;
	}

	@Override
	public void delete(Long id) {
		reportAndComplainDao.delete(id);
	}

	@Override
	public void update(ReportAndComplain reportAndComplain) {
		reportAndComplainDao.update(reportAndComplain);
	}

	@Override
	public ReportAndComplain getById(Long id) {
		return reportAndComplainDao.getById(id);
	}

	@Override
	public Pagination getPage(Long id, Long username, Integer roomId,Integer type, Integer pageNo, Integer pageSize) {
		return reportAndComplainDao.getPage(id, username, roomId, type,pageNo, pageSize);
	}

}
