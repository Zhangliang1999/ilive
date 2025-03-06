package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.LiveInfo;

public interface LiveInfoDao {
	public List<LiveInfo> listNeedStatistic();

	public Pagination pageByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,String enterpriseName,
			Date startTime, Date endTime, int pageNo, int pageSize,Integer startNum,Integer endNum);

	public List<LiveInfo> listByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,
			Date startTime, Date endTime);

	public LiveInfo getBeanById(Long id);

	public LiveInfo getBeanByLiveEventId(Long liveEventId);

	public LiveInfo save(LiveInfo bean);

	public LiveInfo updateByUpdater(Updater<LiveInfo> updater);

	public int enterpriseByMost();
	public LiveInfo sumByLiveMostEnterprise();
	LiveInfo sumByLiveMostEnterprise(Integer enterpriseId);
}