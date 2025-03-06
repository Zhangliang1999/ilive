package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.EnterpriseStatisticResultDao;
import com.jwzt.statistic.entity.EnterpriseStatisticResult;

@Repository
public class EnterpriseStatisticResultDaoImpl extends BaseHibernateDao<EnterpriseStatisticResult, Long>
		implements EnterpriseStatisticResultDao {
	@SuppressWarnings("unchecked")
	@Override
	public EnterpriseStatisticResult getLastBeanByEndTime(Integer enterpriseId, Date endTime) {
		Finder finder = Finder.create("select bean from EnterpriseStatisticResult bean where 1=1");
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != endTime) {
			finder.append(" and bean.statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.statisticTime desc");
		finder.setMaxResults(1);
		List<EnterpriseStatisticResult> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnterpriseStatisticResult> listByParams(Integer enterpriseId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from EnterpriseStatisticResult bean where 1=1");
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
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

	/**
	 * 查询发起直播最多的商户（发起直播场数、总观看人数）
	 */
	@Override
	public EnterpriseStatisticResult sumByLiveMostEnterprise(){
		Finder finder = Finder.create("select bean.enterpriseId as enterpriseId,count(bean.enterpriseId) as liveEventTotalNum,"
				+ "sum(bean.liveViewUserTotalNum) as liveViewUserTotalNum from EnterpriseStatisticResult bean"
		+ " group by bean.enterpriseId order by liveEventTotalNum desc,liveViewUserTotalNum desc");
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(EnterpriseStatisticResult.class));
	    List<EnterpriseStatisticResult> list = query.list();
		return list.get(0);
	}

	@Override
	public int enterpriseByMost(){
		String hql = "select count(bean.enterpriseId) from EnterpriseStatisticResult bean where timestampdiff(DAY,bean.createTime,now()) <= 30"
				+ " group by bean.enterpriseId";
		Query query = this.getSession().createQuery(hql.toString());
	    List<EnterpriseStatisticResult> list = query.list();
		return list.size();
	}
	
	@Override
	public EnterpriseStatisticResult sumByParams(Integer enterpriseId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select" + " sum(bean.liveEventTotalNum) as liveEventTotalNum,"
				+ " sum(bean.liveTotalDuration) as liveTotalDuration,"
				+ " sum(bean.liveViewTotalNum) as liveViewTotalNum,"
				+ " sum(bean.liveViewUserTotalNum) as liveViewUserTotalNum,"
				+ " sum(bean.liveLikeTotalNum) as liveLikeTotalNum,"
				+ " sum(bean.liveCommentTotalNum) as liveCommentTotalNum,"
				+ " sum(bean.liveShareTotalNum) as liveShareTotalNum,"
				+ " sum(bean.androidViewUserNum) as androidViewUserNum,"
				+ " sum(bean.iosViewUserNum) as iosViewUserNum," + " sum(bean.pcViewUserNum) as pcViewUserNum,"
				+ " sum(bean.wapViewUserNum) as wapViewUserNum," + " sum(bean.otherViewUserNum) as otherViewUserNum,"
				+ " sum(bean.durationViewUserNum0To5) as durationViewUserNum0To5,"
				+ " sum(bean.durationViewUserNum5To10) as durationViewUserNum5To10,"
				+ " sum(bean.durationViewUserNum10To30) as durationViewUserNum10To30,"
				+ " sum(bean.durationViewUserNum30To60) as durationViewUserNum30To60,"
				+ " sum(bean.durationViewUserNum60To) as durationViewUserNum60To,"
				+ " sum(bean.viewTimeHour0To3) as viewTimeHour0To3,"
				+ " sum(bean.viewTimeHour3To6) as viewTimeHour3To6,"
				+ " sum(bean.viewTimeHour6To9) as viewTimeHour6To9,"
				+ " sum(bean.viewTimeHour9To12) as viewTimeHour9To12,"
				+ " sum(bean.viewTimeHour12To15) as viewTimeHour12To15,"
				+ " sum(bean.viewTimeHour15To18) as viewTimeHour15To18,"
				+ " sum(bean.viewTimeHour18To21) as viewTimeHour18To21,"
				+ " sum(bean.viewTimeHour21To24) as viewTimeHour21To24,"
				+ " sum(bean.beginLiveHour0To3) as beginLiveHour0To3,"
				+ " sum(bean.beginLiveHour3To6) as beginLiveHour3To6,"
				+ " sum(bean.beginLiveHour6To9) as beginLiveHour6To9,"
				+ " sum(bean.beginLiveHour9To12) as beginLiveHour9To12,"
				+ " sum(bean.beginLiveHour12To15) as beginLiveHour12To15,"
				+ " sum(bean.beginLiveHour15To18) as beginLiveHour15To18,"
				+ " sum(bean.beginLiveHour18To21) as beginLiveHour18To21,"
				+ " sum(bean.beginLiveHour21To24) as beginLiveHour21To24," + " sum(bean.fansNum) as fansNum,"
				+ " sum(bean.redPacketMoney) as redPacketMoney," + " sum(bean.giftNum) as giftNum"
				+ " from EnterpriseStatisticResult bean where 1=1");
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != startTime) {
			finder.append(" and bean.statisticTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(EnterpriseStatisticResult.class));
		return (EnterpriseStatisticResult) query.uniqueResult();
	}

	@Override
	public EnterpriseStatisticResult getBeanById(Long id) {
		EnterpriseStatisticResult bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public EnterpriseStatisticResult save(EnterpriseStatisticResult bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<EnterpriseStatisticResult> getEntityClass() {
		return EnterpriseStatisticResult.class;
	}

}