package com.jwzt.statistic.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.statistic.dao.FtpServerDao;
import com.jwzt.statistic.entity.FtpServer;
@Repository
public class FtpServerDaoImpl extends BaseHibernateDao<FtpServer, Integer> implements FtpServerDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<FtpServer> selectFtpServerAll() throws Exception {
		List<FtpServer> find = find("select bean from FtpServer bean");
		return find;
	}

	@Override
	protected Class<FtpServer> getEntityClass() {
		return FtpServer.class;
	}

}
