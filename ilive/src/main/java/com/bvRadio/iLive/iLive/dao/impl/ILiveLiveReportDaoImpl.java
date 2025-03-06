package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveLiveReportDao;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveReport;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

@Repository
public class ILiveLiveReportDaoImpl extends HibernateBaseDao<ILiveLiveReport, Integer> implements ILiveLiveReportDao {
	public Pagination getPage(Integer id, String reporter, String reported,
			String content, Integer statu, Timestamp submitTime,
			Timestamp dealTime,int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveLiveReport bean");
		finder.append(" where 1=1");
		if (null != id) {
			finder.append(" and bean.id = :id");
			finder.setParam("id", id);
		}
		if (!StringUtils.isBlank(reporter)) {
			finder.append(" and bean.reporter = :reporter");
			finder.setParam("reporter", "%" + reporter + "%");
		}
		if (!StringUtils.isBlank(reported)) {
			finder.append(" and bean.reported = :reported");
			finder.setParam("reported", "%" + reported + "%");
		}
		if (!StringUtils.isBlank(content)) {
			finder.append(" and bean.content = :content");
			finder.setParam("content", "%" + content + "%");
		}
		if (null!=statu) {
			finder.append(" and bean.statu = :statu");
			finder.setParam("statu", statu);
		}
		finder.append(" order by bean.submitTime desc");
		return find(finder, pageNo, pageSize);
	}
	public ILiveLiveReport findById(Integer id) {
		ILiveLiveReport entity = get(id);
		return entity;
	}

	public ILiveLiveReport save(ILiveLiveReport bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveLiveReport bean) {
		getSession().update(bean);
	}

	public ILiveLiveReport deleteById(Integer id) {
		ILiveLiveReport entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ILiveLiveReport> getEntityClass() {
		return ILiveLiveReport.class;
	}

	

}