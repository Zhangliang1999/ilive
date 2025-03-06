package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.LocationStatisticResultDao;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.RequestLog;

@Repository
public class LocationStatisticResultDaoImpl extends BaseHibernateDao<LocationStatisticResult, Integer>
		implements LocationStatisticResultDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationStatisticResult> listByFlag(Long flagId, Integer flagType, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from LocationStatisticResult bean where 1=1");
		if (null != flagId) {
			finder.append(" and flagId = :flagId");
			finder.setParam("flagId", flagId);
		}
		if (null != flagType) {
			finder.append(" and flagType = :flagType");
			finder.setParam("flagType", flagType);
		}
		if (null != startTime) {
			finder.append(" and statisticTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by totalNum desc");
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationStatisticResult> listStatisticResutlForLiveEventView(Long liveEventId) {
		StringBuilder sqlStringBuilder = new StringBuilder("select bean.provinceName, count(bean.userId) totalNum");
		sqlStringBuilder.append(" from (select log.user_id userId,ip.region provinceName");
		sqlStringBuilder
				.append(" from statistic_request_log log left join statistic_ip_address ip on log.ip_code=ip.ip_code");
		sqlStringBuilder.append(" where log.live_event_id = :liveEventId");
		sqlStringBuilder.append(" and log.log_type = :logType");
		sqlStringBuilder.append(" group by log.user_id,ip.region) bean");
		sqlStringBuilder.append(" group by bean.provinceName");
		SQLQuery sqlQuery = getSession().createSQLQuery(sqlStringBuilder.toString());
		sqlQuery.setLong("liveEventId", liveEventId);
		sqlQuery.setInteger("logType", RequestLog.LOG_TYPE_USER_ENTER);
		sqlQuery.addScalar("totalNum", Hibernate.LONG).addScalar("provinceName", Hibernate.STRING)
				.setResultTransformer(Transformers.aliasToBean(LocationStatisticResult.class));
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationStatisticResult> listStatisticResutlForLiveEventViewGroupByCity(Long liveEventId) {
		StringBuilder sqlStringBuilder = new StringBuilder(
				"select bean.provinceName, bean.city, count(bean.userId) totalNum");
		sqlStringBuilder.append(" from (select log.user_id userId,ip.region provinceName, ip.city city");
		sqlStringBuilder
				.append(" from statistic_request_log log left join statistic_ip_address ip on log.ip_code=ip.ip_code");
		sqlStringBuilder.append(" where log.live_event_id = :liveEventId");
		sqlStringBuilder.append(" and log.log_type = :logType");
		sqlStringBuilder.append(" group by log.user_id,ip.city) bean");
		sqlStringBuilder.append(" group by bean.city");
		SQLQuery sqlQuery = getSession().createSQLQuery(sqlStringBuilder.toString());
		sqlQuery.setLong("liveEventId", liveEventId);
		sqlQuery.setInteger("logType", RequestLog.LOG_TYPE_USER_ENTER);
		sqlQuery.addScalar("totalNum", Hibernate.LONG).addScalar("provinceName", Hibernate.STRING)
				.addScalar("city", Hibernate.STRING)
				.setResultTransformer(Transformers.aliasToBean(LocationStatisticResult.class));
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationStatisticResult> listStatisticResutlForEnterpriseBeginLive(Long enterpriseId, Date startTime,
			Date endTime) {
		StringBuilder sqlStringBuilder = new StringBuilder("select bean.provinceName, count(bean.enterpriseId) totalNum");
		sqlStringBuilder.append(" from (select log.enterprise_id enterpriseId,ip.region provinceName");
		sqlStringBuilder
				.append(" from statistic_request_log log left join statistic_ip_address ip on log.ip_code=ip.ip_code");
		sqlStringBuilder.append(" where log.enterprise_id = :enterpriseId");
		sqlStringBuilder.append(" and log.log_type = :logType");
		sqlStringBuilder.append(" and log.create_time >= :startTime");
		sqlStringBuilder.append(" and log.create_time <= :endTime");
		sqlStringBuilder.append(" group by log.enterprise_id,ip.region) bean");
		sqlStringBuilder.append(" group by bean.provinceName");
		SQLQuery sqlQuery = getSession().createSQLQuery(sqlStringBuilder.toString());
		sqlQuery.setLong("enterpriseId", enterpriseId);
		sqlQuery.setInteger("logType", RequestLog.LOG_TYPE_LIVE_BEGIN);
		sqlQuery.setTimestamp("startTime", startTime);
		sqlQuery.setTimestamp("endTime", endTime);
		sqlQuery.addScalar("totalNum", Hibernate.LONG).addScalar("provinceName", Hibernate.STRING)
				.setResultTransformer(Transformers.aliasToBean(LocationStatisticResult.class));
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationStatisticResult> listStatisticResutlForEnterpriseLiveView(Long enterpriseId, Date startTime,
			Date endTime) {
		StringBuilder sqlStringBuilder = new StringBuilder("select bean.provinceName, count(bean.userId) totalNum");
		sqlStringBuilder.append(" from (select log.user_id userId,ip.region provinceName");
		sqlStringBuilder
				.append(" from statistic_request_log log left join statistic_ip_address ip on log.ip_code=ip.ip_code");
		sqlStringBuilder.append(" where log.enterprise_id = :enterpriseId");
		sqlStringBuilder.append(" and log.log_type = :logType");
		sqlStringBuilder.append(" and log.create_time >= :startTime");
		sqlStringBuilder.append(" and log.create_time <= :endTime");
		sqlStringBuilder.append(" group by log.user_id,ip.region) bean");
		sqlStringBuilder.append(" group by bean.provinceName");
		SQLQuery sqlQuery = getSession().createSQLQuery(sqlStringBuilder.toString());
		sqlQuery.setLong("enterpriseId", enterpriseId);
		sqlQuery.setInteger("logType", RequestLog.LOG_TYPE_USER_ENTER);
		sqlQuery.setTimestamp("startTime", startTime);
		sqlQuery.setTimestamp("endTime", endTime);
		sqlQuery.addScalar("totalNum", Hibernate.LONG).addScalar("provinceName", Hibernate.STRING)
				.setResultTransformer(Transformers.aliasToBean(LocationStatisticResult.class));
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationStatisticResult> listStatisticResutlForVideo(Long videoId, Date startTime, Date endTime) {
		StringBuilder sqlStringBuilder = new StringBuilder("select bean.provinceName, count(bean.userId) totalNum");
		sqlStringBuilder.append(" from (select log.user_id userId,ip.region provinceName");
		sqlStringBuilder
				.append(" from statistic_request_log log left join statistic_ip_address ip on log.ip_code=ip.ip_code");
		sqlStringBuilder.append(" where log.video_id = :videoId");
		sqlStringBuilder.append(" and log.log_type = :logType");
		if (startTime!=null) {
			sqlStringBuilder.append(" and log.create_time >= :startTime");
		}
		if (endTime!=null) {
			sqlStringBuilder.append(" and log.create_time <= :endTime");
		}
		sqlStringBuilder.append(" group by log.user_id,ip.region) bean");
		sqlStringBuilder.append(" group by bean.provinceName");
		SQLQuery sqlQuery = getSession().createSQLQuery(sqlStringBuilder.toString());
		sqlQuery.setLong("videoId", videoId);
		//sqlQuery.setInteger("logType", RequestLog.LOG_TYPE_USER_ENTER);
		sqlQuery.setInteger("logType", RequestLog.LOG_TYPE_USER_VIEW);
		if (startTime!=null) {
			sqlQuery.setTimestamp("startTime", startTime);
		}
		if (endTime!=null) {
			sqlQuery.setTimestamp("endTime", endTime);
		}
		sqlQuery.addScalar("totalNum", Hibernate.LONG).addScalar("provinceName", Hibernate.STRING)
				.setResultTransformer(Transformers.aliasToBean(LocationStatisticResult.class));
		List list = sqlQuery.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationStatisticResult> listStatisticResutlForTotalView(Date startTime, Date endTime) {
		StringBuilder sqlStringBuilder = new StringBuilder("select bean.provinceName, count(bean.userId) totalNum");
		sqlStringBuilder.append(" from (select log.user_id userId,ip.region provinceName");
		sqlStringBuilder
				.append(" from statistic_request_log log left join statistic_ip_address ip on log.ip_code=ip.ip_code");
		sqlStringBuilder.append(" where log.log_type = :logType");
		sqlStringBuilder.append(" and log.create_time >= :startTime");
		sqlStringBuilder.append(" and log.create_time <= :endTime");
		sqlStringBuilder.append(" group by log.user_id,ip.region) bean");
		sqlStringBuilder.append(" group by bean.provinceName");
		SQLQuery sqlQuery = getSession().createSQLQuery(sqlStringBuilder.toString());
		sqlQuery.setInteger("logType", RequestLog.LOG_TYPE_USER_ENTER);
		sqlQuery.setTimestamp("startTime", startTime);
		sqlQuery.setTimestamp("endTime", endTime);
		sqlQuery.addScalar("totalNum", Hibernate.LONG).addScalar("provinceName", Hibernate.STRING)
				.setResultTransformer(Transformers.aliasToBean(LocationStatisticResult.class));
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationStatisticResult> listStatisticResutlForTotalBeginLive(Date startTime, Date endTime) {
		StringBuilder sqlStringBuilder = new StringBuilder("select bean.provinceName, count(bean.userId) totalNum");
		sqlStringBuilder.append(" from (select log.user_id userId,ip.region provinceName");
		sqlStringBuilder
				.append(" from statistic_request_log log left join statistic_ip_address ip on log.ip_code=ip.ip_code");
		sqlStringBuilder.append(" where log.log_type = :logType");
		sqlStringBuilder.append(" and log.create_time >= :startTime");
		sqlStringBuilder.append(" and log.create_time <= :endTime");
		sqlStringBuilder.append(" group by log.user_id,ip.region) bean");
		sqlStringBuilder.append(" group by bean.provinceName");
		SQLQuery sqlQuery = getSession().createSQLQuery(sqlStringBuilder.toString());
		sqlQuery.setInteger("logType", RequestLog.LOG_TYPE_LIVE_BEGIN);
		sqlQuery.setTimestamp("startTime", startTime);
		sqlQuery.setTimestamp("endTime", endTime);
		sqlQuery.addScalar("totalNum", Hibernate.LONG).addScalar("provinceName", Hibernate.STRING)
				.setResultTransformer(Transformers.aliasToBean(LocationStatisticResult.class));
		return sqlQuery.list();
	}

	@Override
	public LocationStatisticResult getBeanByProvinceName(String provinceName, Long flagId, Integer flagType,
			Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from LocationStatisticResult bean where 1=1");
		if (null != flagId) {
			finder.append(" and flagId = :flagId");
			finder.setParam("flagId", flagId);
		}
		if (null != flagType) {
			finder.append(" and flagType = :flagType");
			finder.setParam("flagType", flagType);
		}
		if (StringUtils.isNotBlank(provinceName)) {
			finder.append(" and provinceName = :provinceName");
			finder.setParam("provinceName", provinceName);
		}
		if (null != startTime) {
			finder.append(" and statisticTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.setMaxResults(1);
		return (LocationStatisticResult) finder.createQuery(getSession()).uniqueResult();
	}

	@Override
	public LocationStatisticResult save(LocationStatisticResult bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<LocationStatisticResult> getEntityClass() {
		return LocationStatisticResult.class;
	}

}