package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.LiveInfo;

public interface LiveInfoMng {

	public List<LiveInfo> listNeedStatistic();

	public Pagination pageByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,String enterpriseName,
			Date startTime, Date endTime, int pageNo, int pageSize,Integer startNum,Integer endNum);

	public List<LiveInfo> listByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,
			Date startTime, Date endTime);

	public LiveInfo getBeanById(Long id);

	public LiveInfo getBeanByLiveEventId(Long liveEventId);

	public LiveInfo save(LiveInfo bean);

	public void update(LiveInfo bean);
	public int enterpriseByMost();
	public LiveInfo sumByLiveMostEnterprise();
	public LiveInfo sumByLiveMostEnterprise(Integer enterpriseId);
}