package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.VideoInfoDao;
import com.jwzt.statistic.entity.VideoInfo;

@Repository
public class VideoInfoDaoImpl extends BaseHibernateDao<VideoInfo, Long> implements VideoInfoDao {

	@Override
	public Pagination pageByParams(Long videoId, String videoTitle, Date startTime, Date endTime, int pageNo,
			int pageSize) {
		Finder finder = createFinderByParams(videoId, videoTitle, startTime, endTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VideoInfo> listByParams(Long videoId, String videoTitle, Date startTime, Date endTime) {
		Finder finder = createFinderByParams(videoId, videoTitle, startTime, endTime);
		return find(finder);
	}

	private Finder createFinderByParams(Long videoId, String videoTitle, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from VideoInfo bean where 1=1");
		if (null != videoId) {
			finder.append(" and bean.id = :videoId");
			finder.setParam("videoId", videoId);
		}
		if (null != videoTitle) {
			finder.append(" and bean.title like :videoTitle");
			finder.setParam("videoTitle", "%" + videoTitle + "%");
		}
		if (null != startTime) {
			finder.append(" and bean.createTime>=:startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.endTime<=:endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.createTime desc");
		return finder;
	}

	@Override
	public VideoInfo getBeanById(Long id) {
		VideoInfo bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public VideoInfo save(VideoInfo bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<VideoInfo> getEntityClass() {
		return VideoInfo.class;
	}

}