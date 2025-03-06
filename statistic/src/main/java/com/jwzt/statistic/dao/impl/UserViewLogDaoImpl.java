package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.UserViewLogDao;
import com.jwzt.statistic.entity.UserViewLog;

@Repository
public class UserViewLogDaoImpl extends BaseHibernateDao<UserViewLog, Integer> implements UserViewLogDao {
	@SuppressWarnings("unchecked")
	@Override
	public Long sumViewDuarationByParams(Long liveEventId, Integer enterpriseId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select sum(bean.viewTotalTime) from UserViewLog bean where 1=1");
		if (null != liveEventId) {
			finder.append(" and bean.liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != startTime) {
			finder.append(" and bean.lastStatisticTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.lastStatisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		List<Long> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long countUserNumByParams(Long liveEventId, Integer enterpriseId, Long startDuration, Long endDuration,
			Date startTime, Date endTime) {
		try{
			Finder finder = Finder.create("select bean from UserViewLog bean where 1=1");
			if (null != liveEventId) {
				finder.append(" and bean.liveEventId = :liveEventId");
				finder.setParam("liveEventId", liveEventId);
			}
			if (null != enterpriseId) {
				finder.append(" and bean.enterpriseId = :enterpriseId");
				finder.setParam("enterpriseId", enterpriseId);
			}
			if (null != startTime) {
				finder.append(" and bean.lastStatisticTime >= :startTime");
				finder.setParam("startTime", startTime);
			}
			if (null != endTime) {
				finder.append(" and bean.lastStatisticTime <= :endTime");
				finder.setParam("endTime", endTime);
			}
			//因为涉及到同一个用户同时用不同终端观看会被累加造成错误，所以不需要在按照同用户累加了
			//finder.append(" group by bean.liveEventId,bean.userId");
			//finder.append(" having 1=1");
			if (null != startDuration) {
				finder.append(" and bean.viewTotalTime >= :startDuration");
				finder.setParam("startDuration", startDuration);
			}
			if (null != endDuration) {
				finder.append(" and bean.viewTotalTime < :endDuration");
				finder.setParam("endDuration", endDuration);
			}
			List<Long> list = find(finder);
			if (null != list && list.size() > 0) {
				return Long.valueOf(list.size());
			}
			return 0L;
		}catch(Exception e){
			e.printStackTrace();
			return 0L;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserViewLog> listByLiveEventId(Long liveEventId) {
		Finder finder = Finder.create("select bean from UserViewLog bean where 1=1");
		if (null != liveEventId) {
			finder.append(" and liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserViewLog getBeanByParams(Long liveEventId, Integer requestType, Integer userType, String userId) {
		Finder finder = Finder.create("select bean from UserViewLog bean where 1=1");
		if (null != liveEventId) {
			finder.append(" and liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != requestType) {
			finder.append(" and requestType = :requestType");
			finder.setParam("requestType", requestType);
		}
		if (null != userType) {
			finder.append(" and userType = :userType");
			finder.setParam("userType", userType);
		}
		if (null != liveEventId) {
			finder.append(" and userId = :userId");
			finder.setParam("userId", userId);
		}
		finder.setMaxResults(1);
		List<UserViewLog> list = find(finder);
		if (null != list & list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public UserViewLog save(UserViewLog bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<UserViewLog> getEntityClass() {
		return UserViewLog.class;
	}

}