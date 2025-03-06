package com.jwzt.statistic.dao.impl;

import java.util.List;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.LoginRecordDao;
import com.jwzt.statistic.entity.LoginRecord;

public class LoginRecordDaoImpl extends BaseHibernateDao<LoginRecord, Long> implements LoginRecordDao {


	@Override
	public Pagination getPageByUserId(Long userId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from LoginRecord where userId=:userId order by createTime DESC");
		finder.setParam("userId",userId);
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}

	@Override
	public void save(LoginRecord record) {
		this.getSession().save(record);
	}

	@Override
	protected Class<LoginRecord> getEntityClass() {
		return LoginRecord.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LoginRecord> getListByUserId(Long userId, Integer limit) {
		Finder finder = Finder.create("from LoginRecord where userId=:userId order by createTime DESC");
		finder.setParam("userId",userId);
		return find(finder);
	}

}
