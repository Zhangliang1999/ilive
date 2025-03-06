package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.MinuteStatisticResultDao;
import com.jwzt.statistic.entity.MinuteStatisticResult;

@Repository
public class MinuteStatisticResultDaoImpl extends BaseHibernateDao<MinuteStatisticResult, String>
		implements MinuteStatisticResultDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<MinuteStatisticResult> listByLiveEventId(Long liveEventId) {
		Finder finder = Finder.create("select bean from MinuteStatisticResult bean where 1=1");
		if (null != liveEventId) {
			finder.append(" and liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MinuteStatisticResult getBeanByParams(Long liveEventId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from MinuteStatisticResult bean where 1=1");
		if (null != liveEventId) {
			finder.append(" and bean.liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != startTime) {
			finder.append(" and startTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and endTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.setMaxResults(1);
		List<MinuteStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MinuteStatisticResult getBeanWithMaxMinuteUserNum(Long liveEventId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from MinuteStatisticResult bean where 1=1");
		if (null != liveEventId) {
			finder.append(" and bean.liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != startTime) {
			finder.append(" and startTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and endTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.userNum desc");
		finder.setMaxResults(1);
		List<MinuteStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public MinuteStatisticResult getBeanWithMaxMinuteUserNumByEnterpriseId(Long enterpriseId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from MinuteStatisticResult bean , LiveInfo liveInfo where liveInfo.liveEventId=bean.liveEventId");
		if (null != enterpriseId) {
			finder.append(" and liveInfo.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId.intValue());
		}
		if (null != startTime) {
			finder.append(" and bean.startTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.endTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.userNum desc");
		finder.setMaxResults(1);
		List<MinuteStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public MinuteStatisticResult save(MinuteStatisticResult bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<MinuteStatisticResult> getEntityClass() {
		return MinuteStatisticResult.class;
	}

}