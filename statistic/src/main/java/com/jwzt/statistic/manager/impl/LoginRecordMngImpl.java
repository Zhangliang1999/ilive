package com.jwzt.statistic.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.LoginRecordDao;
import com.jwzt.statistic.entity.IpAddress;
import com.jwzt.statistic.entity.LoginRecord;
import com.jwzt.statistic.manager.IpAddressMng;
import com.jwzt.statistic.manager.LoginRecordMng;

@Transactional
public class LoginRecordMngImpl implements LoginRecordMng {

	@Autowired
	private LoginRecordDao loginRecordDao;
	
	@Autowired
	private IpAddressMng ipAddressMng;
	
	@Override
	public Pagination getPageByUserId(Long userId, Integer pageNo, Integer pageSize) {
		Pagination page = loginRecordDao.getPageByUserId(userId, pageNo, pageSize);
		List<LoginRecord> list = (List<LoginRecord>) page.getList();
		for(LoginRecord record:list) {
			IpAddress beanByIpCode = ipAddressMng.getBeanByIpCode(record.getIpCode());
			record.setIpAddress(beanByIpCode);
		}
		return page;
	}

	@Override
	public void save(LoginRecord record) {
		record.setCreateTime(new Timestamp(System.currentTimeMillis()));
		record.setId(System.currentTimeMillis());
		loginRecordDao.save(record);
	}

	@Override
	public List<LoginRecord> getListByUserId(Long userId, Integer limit) {
		return loginRecordDao.getListByUserId(userId, limit);
	}

}
