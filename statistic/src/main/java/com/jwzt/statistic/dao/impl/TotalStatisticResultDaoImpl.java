package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.TotalStatisticResultDao;
import com.jwzt.statistic.entity.TotalStatisticResult;

@Repository
public class TotalStatisticResultDaoImpl extends BaseHibernateDao<TotalStatisticResult, Long>
		implements TotalStatisticResultDao {

	@Override
	public TotalStatisticResult sumByParams(Date startTime, Date endTime) {
		Finder finder = Finder.create("select" + " sum(bean.liveEventTotalNum) as liveEventTotalNum,"
				+ " sum(bean.liveEnterpriseTotalNum) as liveEnterpriseTotalNum,"
				+ " sum(bean.liveTotalDuration) as liveTotalDuration,"
				+ " sum(bean.liveViewTotalNum) as liveViewTotalNum,"
				+ " sum(bean.liveViewUserTotalNum) as liveViewUserTotalNum,"
				+ " sum(bean.userTotalNum) as userTotalNum," + " sum(bean.viewTotalDuration) as viewTotalDuration,"
				+ " sum(bean.viewTotalNum) as viewTotalNum," + " sum(bean.liveLikeTotalNum) as liveLikeTotalNum,"
				+ " sum(bean.liveCommentTotalNum) as liveCommentTotalNum,"
				+ " sum(bean.liveShareTotalNum) as liveShareTotalNum,"
				+ " sum(bean.newRegisterUserNumAboutLive) as newRegisterUserNumAboutLive,"
				+ " sum(bean.oldRegisterUserNumAboutLive) as oldRegisterUserNumAboutLive,"
				+ " sum(bean.newRegisterUserNumAboutDay) as newRegisterUserNumAboutDay,"
				+ " sum(bean.oldRegisterUserNumAboutDay) as oldRegisterUserNumAboutDay,"
				+ " sum(bean.newRegisterEnterpriseNum) as newRegisterEnterpriseNum,"
				+ " sum(bean.oldRegisterEnterpriseNum) as oldRegisterEnterpriseNum,"
				+ " sum(bean.durationLiveNum0To10) as durationLiveNum0To10,"
				+ " sum(bean.durationLiveNum10To100) as durationLiveNum10To100,"
				+ " sum(bean.durationLiveNum100To200) as durationLiveNum100To200,"
				+ " sum(bean.durationLiveNum200To300) as durationLiveNum200To300,"
				+ " sum(bean.durationLiveNum300To) as durationLiveNum300To,"
				+ " sum(bean.durationViewUserNum0To5) as durationViewUserNum0To5,"
				+ " sum(bean.durationViewUserNum5To10) as durationViewUserNum5To10,"
				+ " sum(bean.durationViewUserNum10To30) as durationViewUserNum10To30,"
				+ " sum(bean.durationViewUserNum30To60) as durationViewUserNum30To60,"
				+ " sum(bean.durationViewUserNum60To) as durationViewUserNum60To,"
				+ " sum(bean.androidRegisterUserNum) as androidRegisterUserNum,"
				+ " sum(bean.iosRegisterUserNum) as iosRegisterUserNum,"
				+ " sum(bean.pcRegisterUserNum) as pcRegisterUserNum,"
				+ " sum(bean.wapRegisterUserNum) as wapRegisterUserNum,"
				+ " sum(bean.otherRegisterUserNum) as otherRegisterUserNum,"
				+ " sum(bean.androidViewUserNum) as androidViewUserNum,"
				+ " sum(bean.iosViewUserNum) as iosViewUserNum," + " sum(bean.pcViewUserNum) as pcViewUserNum,"
				+ " sum(bean.wapViewUserNum) as wapViewUserNum," + " sum(bean.otherViewUserNum) as otherViewUserNum,"
				+ " sum(bean.viewTimeHour0To3) as viewTimeHour0To3,"
				+ " sum(bean.viewTimeHour3To6) as viewTimeHour3To6,"
				+ " sum(bean.viewTimeHour6To9) as viewTimeHour6To9,"
				+ " sum(bean.viewTimeHour9To12) as viewTimeHour9To12,"
				+ " sum(bean.viewTimeHour12To15) as viewTimeHour12To15,"
				+ " sum(bean.viewTimeHour15To18) as viewTimeHour15To18,"
				+ " sum(bean.viewTimeHour18To21) as viewTimeHour18To21,"
				+ " sum(bean.viewTimeHour21To24) as viewTimeHour21To24" + " from TotalStatisticResult bean where 1=1");
		if (null != startTime) {
			finder.append(" and bean.statisticTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(TotalStatisticResult.class));
		return (TotalStatisticResult) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TotalStatisticResult> listByParams(Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from TotalStatisticResult bean where 1=1");
		if (null != startTime) {
			finder.append(" and bean.statisticTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TotalStatisticResult getLastBeanByEndTime(Date endTime) {
		Finder finder = Finder.create("select bean from TotalStatisticResult bean where 1=1");
		if (null != endTime) {
			finder.append(" and bean.statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.statisticTime desc");
		finder.setMaxResults(1);
		List<TotalStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public TotalStatisticResult getBeanById(Long id) {
		TotalStatisticResult bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public TotalStatisticResult save(TotalStatisticResult bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<TotalStatisticResult> getEntityClass() {
		return TotalStatisticResult.class;
	}

}