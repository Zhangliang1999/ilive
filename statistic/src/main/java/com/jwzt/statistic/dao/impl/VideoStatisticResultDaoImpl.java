package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.VideoStatisticResultDao;
import com.jwzt.statistic.entity.VideoStatisticResult;

@Repository
public class VideoStatisticResultDaoImpl extends BaseHibernateDao<VideoStatisticResult, Long>
		implements VideoStatisticResultDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<VideoStatisticResult> listByParams(Long videoId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from VideoStatisticResult bean where 1=1");
		if (null != videoId) {
			finder.append(" and bean.videoId = :videoId");
			finder.setParam("videoId", videoId);
		}
		if (null != startTime) {
			finder.append(" and bean.statisticTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.statisticTime DESC,bean.createTime DESC");
		return find(finder);
	}

	@Override
	public VideoStatisticResult sumByParams(Long videoId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select sum(bean.userTotalNum) as userTotalNum,"
				+ " sum(bean.viewTotalNum) as viewTotalNum, sum(bean.shareTotalNum) as shareTotalNum,"
				+ " sum(bean.likeTotalNum) as likeTotalNum, sum(bean.commentTotalNum) as commentTotalNum,"
				+ " sum(bean.androidUserNum) as androidUserNum, sum(bean.iosUserNum) as iosUserNum,"
				+ " sum(bean.pcUserNum) as pcUserNum, sum(bean.wapUserNum) as wapUserNum,"
				+ " sum(bean.otherUserNum) as otherUserNum, sum(bean.newRegisterUserNum) as newRegisterUserNum,"
				+ " sum(bean.oldRegisterUserNum) as oldRegisterUserNum, sum(bean.otherRegisterUserNum) as otherRegisterUserNum"
				+ " from VideoStatisticResult bean where 1=1");
		if (null != videoId) {
			finder.append(" and bean.videoId = :videoId");
			finder.setParam("videoId", videoId);
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
		query.setResultTransformer(Transformers.aliasToBean(VideoStatisticResult.class));
		return (VideoStatisticResult) query.uniqueResult();
	}
	
	@Override
	public VideoStatisticResult sumByParams2(Long videoId, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from VideoStatisticResult bean where 1=1");
		if (null != videoId) {
			finder.append(" and bean.videoId = :videoId");
			finder.setParam("videoId", videoId);
		}
		if (null != startTime) {
			finder.append(" and bean.statisticTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.statisticTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.statisticTime DESC");
		List<VideoStatisticResult> find = find(finder);
		return find.get(0);
	}

	@Override
	public VideoStatisticResult getBeanById(Long id) {
		VideoStatisticResult bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public VideoStatisticResult save(VideoStatisticResult bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<VideoStatisticResult> getEntityClass() {
		return VideoStatisticResult.class;
	}

}