package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.RequestLogDao;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.vo.RankInfo;

@Repository
public class RequestLogDaoImpl extends BaseHibernateDao<RequestLog, String> implements RequestLogDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> listNullIpCode() {
		Finder finder = Finder.create("select bean.ipCode from RequestLog bean where 1=1");
		finder.append(" and bean.ipCode not in (select ip.ipCode from IpAddress ip)");
		return find(finder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RankInfo> listRankGroupByUser(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			Date startTime, Date endTime, Integer topNum) {
		Finder finder = Finder
				.create("select user.nickName as name,count(bean.id) as num from RequestLog bean,UserInfo user"
						+ " where bean.userId=user.id");
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if (null != liveEventId) {
			finder.append(" and bean.liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != videoId) {
			finder.append(" and bean.videoId = :videoId");
			finder.setParam("videoId", videoId);
		}
		if (null != logTypes && logTypes.length > 0) {
			finder.append(" and bean.logType in (:logTypes)");
			finder.setParamList("logTypes", logTypes);
		}
		finder.append(" group by bean.userId");
		finder.append(" order by count(bean.id) desc");
		if (null != topNum && topNum > 0) {
			finder.setMaxResults(topNum);
		} else {
			finder.setMaxResults(20);
		}
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(RankInfo.class));
		return (List<RankInfo>) query.list();
	}

	@Override
	public Pagination pageByParams(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime, int pageNo, int pageSize) {
		Finder finder = createFinderParams(liveEventId, enterpriseId, videoId, logTypes, userIds, startTime, endTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RequestLog> listByParams(Long roomId , Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime) {
		Finder finder = createFinderParams(roomId , liveEventId, enterpriseId, videoId, logTypes, userIds, startTime, endTime);
		return find(finder);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RequestLog> listByParams( Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime) {
		Finder finder = createFinderParams(null , liveEventId, enterpriseId, videoId, logTypes, userIds, startTime, endTime);
		return find(finder);
	}

	private Finder createFinderParams(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime) {
		
		return createFinderParams(null, liveEventId,  enterpriseId,  videoId,  logTypes,
				 userIds,  startTime,  endTime);
		
	}
	
	
	private Finder createFinderParams(Long roomId , Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from RequestLog bean where 1=1");
		if (null != startTime) {
			finder.append(" and createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if (null != roomId) {
			finder.append(" and roomId = :roomId");
			finder.setParam("roomId", roomId.intValue());
		}
		if (null != liveEventId) {
			finder.append(" and liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != enterpriseId) {
			finder.append(" and enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != videoId) {
			finder.append(" and videoId = :videoId");
			finder.setParam("videoId", videoId);
		}
		if (null != logTypes && logTypes.length > 0) {
			finder.append(" and logType in (:logTypes)");
			finder.setParamList("logTypes", logTypes);
		}
		if (null != userIds && userIds.length > 0) {
			finder.append(" and userId in (:userIds)");
			finder.setParamList("userIds", userIds);
		}
		finder.append(" order by createTime asc");
		return finder;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long countUserNum(Long liveEventId, Integer enterpriseId, Long videoId, Integer requestType,
			Integer[] logTypes, Date startTime, Date endTime) {
		Finder finder = Finder.create("select userId from RequestLog where 1=1");
		if (null != startTime) {
			finder.append(" and createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if (null != requestType) {
			finder.append(" and requestType = :requestType");
			finder.setParam("requestType", requestType);
		}
		if (null != logTypes && logTypes.length > 0) {
			finder.append(" and logType in (:logTypes)");
			finder.setParamList("logTypes", logTypes);
		}
		if (null != liveEventId) {
			finder.append(" and liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != enterpriseId) {
			finder.append(" and enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != videoId) {
			finder.append(" and videoId = :videoId");
			finder.setParam("videoId", videoId);
		}
		finder.append(" group by userId");

		List list = find(finder);
		if (null != list && list.size() > 0) {
			return (Long) Long.valueOf(list.size());
		}
		return 0L;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long countRequestNum(Long liveEventId, Integer enterpriseId, Long videoId, Integer requestType , Integer[] logTypes,
			Date startTime, Date endTime) {
		Finder finder = Finder.create("select count(*) from RequestLog where 1=1");
		if (null != startTime) {
			finder.append(" and createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if (null != liveEventId) {
			finder.append(" and liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != enterpriseId) {
			finder.append(" and enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != videoId) {
			finder.append(" and videoId = :videoId");
			finder.setParam("videoId", videoId);
		}
		if (null != requestType) {
			finder.append(" and requestType = :requestType");
			finder.setParam("requestType", requestType);
		}
		if (null != logTypes && logTypes.length > 0) {
			finder.append(" and logType in (:logTypes)");
			finder.setParamList("logTypes", logTypes);
		}

		List list = find(finder);
		if (null != list && list.size() > 0) {
			return (Long) list.get(0);
		}
		return 0L;
	}

	@Override
	public RequestLog save(RequestLog bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	public RequestLog update(RequestLog bean) {
		getSession().update(bean);
		return bean;
	}

	@Override
	protected Class<RequestLog> getEntityClass() {
		return RequestLog.class;
	}

	@Override
	public Pagination getLiveViewList(Long userId,Integer roomId,Long videoId,Integer pageNo,Integer pageSize,Integer type) {
		Finder finder = getFinder(userId, roomId, videoId, type);
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RequestLog> getLiveViewList(Long userId, Integer roomId, Long videoId, Integer type) {
		Finder finder = getFinder(userId, roomId, videoId, type);
		return find(finder);
	}
	private Finder getFinder(Long userId, Integer roomId, Long videoId, Integer type) {
		Finder finder = Finder.create("from RequestLog where logType = :logType and userId = :userId");
		finder.setParam("userId", String.valueOf(userId));
		if (type==1) {
			finder.setParam("logType", 1);
			if (roomId!=null) {
				finder.append(" and roomId = :roomId");
				finder.setParam("roomId", roomId);
			}
		}else if (type==2) {
			finder.setParam("logType", 6);
			if (videoId!=null) {
				finder.append(" and videoId = :videoId");
				finder.setParam("videoId", videoId);
			}
		}else if (type==3) {
			finder.setParam("logType", 2);
			if (roomId!=null) {
				finder.append(" and roomId = :roomId");
				finder.setParam("roomId", roomId);
			}
		}
		finder.append(" order by createTime DESC");
		return finder;
	}

	@Override
	public Integer getNumByEvent(Long eventId, Integer type) {
		Finder finder = Finder.create("from RequestLog where liveEventId = :liveEventId"
				+ " and logType = :logType");
		finder.setParam("liveEventId", eventId);
		finder.setParam("logType", type);
		List find = find(finder);
		if (find!=null) {
			return find.size();
		}
		return 0;
	}

	@Override
	public Integer getNumByRoom(Integer roomId, Integer type) {
		Finder finder = Finder.create("from RequestLog where roomId = :roomId"
				+ " and logType = :logType");
		finder.setParam("roomId", roomId);
		finder.setParam("logType", type);
		List find = find(finder);
		if (find!=null) {
			return find.size();
		}
		return 0;
	}

	@Override
	public Integer getNumByEvent(Long eventId, Integer type, boolean isDistinct) {
		if (!isDistinct) {
			return getNumByEvent(eventId,type);
		}
		Finder finder = Finder.create("select distinct userId from RequestLog where liveEventId = :liveEventId"
				+ " and logType = :logType");
		finder.setParam("liveEventId", eventId);
		finder.setParam("logType", type);
		List find = find(finder);
		if (find!=null) {
			return find.size();
		}
		return 0;
	}

}