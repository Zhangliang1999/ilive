package com.jwzt.statistic.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.statistic.dao.FtpServerDao;
import com.jwzt.statistic.entity.FtpServer;
import com.jwzt.statistic.manager.FtpServerManager;
@Service
public class FtpServerManagerImpl implements FtpServerManager {
	
	@Autowired
	private FtpServerDao ftpServerDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<FtpServer> selectFtpServerAll() {
		List<FtpServer> list = new ArrayList<FtpServer>();
		try {
			list = ftpServerDao.selectFtpServerAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
