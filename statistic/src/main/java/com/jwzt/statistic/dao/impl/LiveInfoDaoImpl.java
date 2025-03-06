package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.LiveInfoDao;
import com.jwzt.statistic.entity.LiveInfo;

@Repository
public class LiveInfoDaoImpl extends BaseHibernateDao<LiveInfo, Long> implements LiveInfoDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<LiveInfo> listNeedStatistic() {
		Finder finder = Finder.create("select bean from LiveInfo bean where 1=1");
		finder.append(" and (isFinshed is null or isFinshed = 0)");
		finder.append(" and (lastStatisticTime is null or liveEndTime is null or lastStatisticTime < liveEndTime)");
		return find(finder);
	}

	@Override
	public Pagination pageByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,String enterpriseName,
			Date startTime, Date endTime, int pageNo, int pageSize,Integer startNum,Integer endNum) {
		Finder finder = createFinderByParams(roomId, liveEventId, liveTitle, enterpriseId,enterpriseName, startTime, endTime,startNum,endNum);
		return find(finder, pageNo, pageSize);
	}

	/**
	 * 查询发起直播最多的商户（商户名称、发起直播场数、总观看人数）
	 */
	@Override
	public LiveInfo sumByLiveMostEnterprise(){
		Finder finder = Finder.create("select linfo.enterpriseId as enterpriseId,linfo.enterpriseName as enterpriseName,"
				+ "count(linfo.enterpriseId) as liveEventTotalNum,sum(lresult.userNum) as liveViewUserTotalNum"
				+ " from LiveInfo linfo ,LiveStatisticResult lresult"
				+ " where lresult.id = linfo.id group by linfo.enterpriseId"
				+ " order by count(linfo.enterpriseId)desc,sum(lresult.userNum) desc ");
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(LiveInfo.class));
	    List<LiveInfo> list = query.list();
		return list.get(0);
	}
	
	/**
	 * 查询该企业下发起直播最多的商户（商户名称、发起直播场数、总观看人数）
	 */
	@Override
	public LiveInfo sumByLiveMostEnterprise(Integer enterpriseId){
		Finder finder = Finder.create("select linfo.enterpriseId as enterpriseId,linfo.enterpriseName as enterpriseName,"
				+ "count(linfo.enterpriseId) as liveEventTotalNum,sum(lresult.userNum) as liveViewUserTotalNum"
				+ " from LiveInfo linfo ,LiveStatisticResult lresult"
				+ " where lresult.id = linfo.id and linfo.enterpriseId = :enterpriseId  group by linfo.enterpriseId"
				+ " order by count(linfo.enterpriseId)desc,sum(lresult.userNum) desc ");
		finder.setParam("enterpriseId", enterpriseId);
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(LiveInfo.class));
		List<LiveInfo> list = query.list();
		return list.get(0);
	}

	/**
	 * 最近30天活跃的商户数
	 */
	@Override
	public int enterpriseByMost(){
		String hql = "select linfo.enterpriseId as enterpriseId,count(linfo.enterpriseId) as  liveEventTotalNum"
				+ "	from LiveInfo linfo ,LiveStatisticResult lresult "
				+ "	where lresult.id = linfo.id and timestampdiff(DAY,linfo.liveBeginTime,now()) <= 30"
				+ "	group by linfo.enterpriseId ";
		Query query = this.getSession().createQuery(hql.toString());
	    List<LiveInfo> list = query.list();
		return list.size();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<LiveInfo> listByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,
			Date startTime, Date endTime) {
		Finder finder = createFinderByParams(roomId, liveEventId, liveTitle, enterpriseId, null,startTime, endTime,null,null);
		return find(finder);
	}

	private Finder createFinderByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,String enterpriseName,
			Date startTime, Date endTime,Integer startNum,Integer endNum) {
		Finder finder = Finder.create("select bean from LiveInfo bean where 1=1");
		if (null != roomId) {
			finder.append(" and bean.roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		if (null != liveEventId) {
			finder.append(" and bean.liveEventId = :liveEventId");
			finder.setParam("liveEventId", liveEventId);
		}
		if (null != liveTitle) {
			finder.append(" and bean.liveTitle like :liveTitle");
			finder.setParam("liveTitle", "%" + liveTitle + "%");
		}
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if(null != enterpriseName){
			finder.append(" and bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%" + enterpriseName + "%");
		}
		if (null != startTime) {
			finder.append(" and bean.liveBeginTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.liveBeginTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if(null != endNum && null != startNum){
			finder.append(" and timestampdiff(MINUTE,liveBeginTime,liveEndTime) between :startNum and :endNum");
			finder.setParam("startNum", startNum);
			finder.setParam("endNum", endNum);
		}else if(null == endNum && null != startNum){
			finder.append(" and timestampdiff(MINUTE,liveBeginTime,liveEndTime) > :startNum");
			finder.setParam("startNum", startNum);
		}
		finder.append(" order by liveBeginTime desc");
		return finder;
	}

	@Override
	public LiveInfo getBeanById(Long id) {
		LiveInfo bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public LiveInfo getBeanByLiveEventId(Long liveEventId) {
		return findUniqueByProperty("liveEventId", liveEventId);
	}

	@Override
	public LiveInfo save(LiveInfo bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<LiveInfo> getEntityClass() {
		return LiveInfo.class;
	}

}