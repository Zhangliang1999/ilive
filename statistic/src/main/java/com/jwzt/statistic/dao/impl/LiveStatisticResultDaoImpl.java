package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.LiveStatisticResultDao;
import com.jwzt.statistic.entity.LiveStatisticResult;

@Repository
public class LiveStatisticResultDaoImpl extends BaseHibernateDao<LiveStatisticResult, Long>
		implements LiveStatisticResultDao {
	@SuppressWarnings("rawtypes")
	@Override
	public Long countCommentNumForLiveDatabase(Long liveEventId) {
		SQLQuery sqlQuery = getSession()
				.createSQLQuery("select count(*) as num from db_ilive.ilive_message where live_id = :liveEventId");
		sqlQuery.setLong("liveEventId", liveEventId);
		sqlQuery.addScalar("num", Hibernate.LONG);
		List list = sqlQuery.list();
		if (null != list && list.size() > 0) {
			return (Long) list.get(0);
		}
		return 0L;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long countLikeNumForLiveDatabase(Long liveEventId) {
		SQLQuery sqlQuery = getSession()
				.createSQLQuery("select count(*) as num from db_ilive.ilive_room_register where live_event_id = :liveEventId");
		sqlQuery.setLong("liveEventId", liveEventId);
		sqlQuery.addScalar("num", Hibernate.LONG);
		List list = sqlQuery.list();
		if (null != list && list.size() > 0) {
			return (Long) list.get(0);
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LiveStatisticResult getBeanWithMaxNewRegisterUserNum(Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from LiveStatisticResult bean,LiveInfo live where bean.id=live.id");
		if (null != startTime) {
			finder.append(" and live.liveBeginTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and live.liveBeginTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.newRegisterUserNum desc");
		finder.setMaxResults(1);
		List<LiveStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public LiveStatisticResult getBeanById(Long id) {
		LiveStatisticResult bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public LiveStatisticResult save(LiveStatisticResult bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<LiveStatisticResult> getEntityClass() {
		return LiveStatisticResult.class;
	}

}