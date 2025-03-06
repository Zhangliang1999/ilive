package com.jwzt.statistic.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.MeetingStatisticResultDao;
import com.jwzt.statistic.entity.MeetingStatisticResult;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.entity.vo.MeetingHoursResult;
import com.jwzt.statistic.entity.vo.MeetingStasticShowResult;

@Repository
public class MeetingStatisticResultDaoImpl extends BaseHibernateDao<MeetingStatisticResult, Long>
		implements MeetingStatisticResultDao {

	@Override
	public MeetingStasticShowResult sumByParams() {
		Finder finder = Finder.create("select"
				+ " sum(bean.MeetingTime) as MeetingSumTime,"
				+ " avg(bean.MeetingTime) as MeetingAvgTime from MeetingStatisticResult bean where 1=1");
		
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(MeetingStasticShowResult.class));
		System.out.println("查询结果："+query.uniqueResult());
		return  (MeetingStasticShowResult) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MeetingStatisticResult> listByParams(Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from MeetingStatisticResult bean where 1=1");
		if (null != startTime&&null != endTime) {
			finder.append(" and ((bean.meetingStartTime >= :startTime and bean.meetingEndTime <= :endTime) or (bean.meetingStartTime >= :startTime and bean.meetingEndTime >= :endTime) or (bean.meetingStartTime <= :startTime and bean.meetingEndTime >= :endTime) or (bean.meetingStartTime <= :startTime and bean.meetingEndTime <= :endTime))");
			finder.setParam("startTime", startTime);
			finder.setParam("endTime", endTime);
		}
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MeetingStatisticResult getLastBeanByEndTime(Date endTime) {
		Finder finder = Finder.create("select bean from MeetingStatisticResult bean where 1=1");
		if (null != endTime) {
			finder.append(" and bean.meetingEndTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.meetingStartTime desc");
		finder.setMaxResults(1);
		List<MeetingStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public MeetingStatisticResult getBeanById(Long id) {
		MeetingStatisticResult bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public MeetingStatisticResult save(MeetingStatisticResult bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<MeetingStatisticResult> getEntityClass() {
		return MeetingStatisticResult.class;
	}

	@Override
	public Long getBeanByMeetingTime(Long minNum, Long maxNum) {
		Finder finder = Finder.create("select bean from MeetingStatisticResult bean where 1=1");
		if (null != minNum) {
			finder.append(" and bean.MeetingTime >= :startTime");
			finder.setParam("startTime", minNum);
		}
		if (null != maxNum) {
			finder.append(" and bean.MeetingTime <= :endTime");
			finder.setParam("endTime", maxNum);
		}
		List<MeetingStatisticResult> list = find(finder);
		return (long) list.size();
	}

	@Override
	public List<MeetingEnterpriseResult> top10() {
		Finder finder = Finder.create(" select  enterpriseId,enterpriseName,count(enterpriseId) as useTime from MeetingStatisticResult group by enterpriseId order by count(enterpriseId) desc");
		
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(MeetingEnterpriseResult.class));
		List<MeetingEnterpriseResult> list=find(finder);
		return list;
	}

	@Override
	public Long getBeanByRecordOrRedirect(Integer record, Integer redirect) {
		Finder finder = Finder.create("select bean from MeetingStatisticResult bean where 1=1");
		if (null != record) {
			finder.append(" and bean.meetingRecord = :meetingRecord");
			finder.setParam("meetingRecord", record);
		}
		if (null != redirect) {
			finder.append(" and bean.MeetingRedirect = :MeetingRedirect");
			finder.setParam("MeetingRedirect", redirect);
		}
		List<MeetingStatisticResult> list = find(finder);
		return (long) list.size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public MeetingEnterpriseResult top10ByIndex(Integer index) {
		Finder finder = Finder.create(" select  bean.enterpriseId as enterpriseId,bean.enterpriseName as enterpriseName,count(enterpriseId) as useTime from MeetingStatisticResult bean group by bean.enterpriseId order by count(bean.enterpriseId) desc");
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(MeetingEnterpriseResult.class));
		List<MeetingEnterpriseResult> list=find(finder);
		return list.get(index);
	}

	@Override
	public Long getBeanByEnterpriseId(Long enterpriseId) {
		Finder finder = Finder.create("select bean from MeetingStatisticResult bean where 1=1");
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		finder.append(" order by bean.meetingStartTime desc");
		List<MeetingStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return (long) list.size();
		}
		return 0L;
	}

	@Override
	public Long getBeanByFigure(Integer figure) {
		Finder finder = Finder.create("select bean from MeetingStatisticResult bean where 1=1");
		if (null != figure) {
			finder.append(" and bean.MeetingFigure = :MeetingFigure");
			finder.setParam("MeetingFigure", figure.longValue());
		}
		finder.append(" order by bean.meetingStartTime desc");
		List<MeetingStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return (long) list.size();
		}
		return 0L;
	}

}