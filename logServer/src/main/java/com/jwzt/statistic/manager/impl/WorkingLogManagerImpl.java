package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.WorkingLogDao;
import com.jwzt.statistic.entity.WorkingLog;
import com.jwzt.statistic.manager.WorkingLogManager;
@Service
public class WorkingLogManagerImpl implements WorkingLogManager {
	@Autowired
	private WorkingLogDao workingLogDao;
	
	@Override
	@Transactional
	public void addWorkingLog(WorkingLog workingLog) throws Exception {
		String replace = UUID.randomUUID().toString().replace("-", "");
		workingLog.setId(replace);
		workingLogDao.insertWorkingLog(workingLog);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination selectWorkingLogAll(Integer systemId,
			Integer modelId, String contentId, String content,
			String contentName, Integer userId, String userName,
			String terminal, Date startTime, Date endTime,Integer pageNo, Integer pageSize) throws Exception {
		if(pageSize==null){
			pageSize = 20;
		}
		Pagination data = workingLogDao.findWorkingLogAll(systemId,modelId,contentId,content,contentName,userId,userName,terminal,startTime,endTime,pageNo,pageSize);
		if(data==null){
			data = new Pagination(pageNo, pageSize, 0);
		}
		return data;
	}

}
