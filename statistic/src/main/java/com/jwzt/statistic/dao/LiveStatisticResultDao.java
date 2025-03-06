package com.jwzt.statistic.dao;

import java.util.Date;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.LiveStatisticResult;

public interface LiveStatisticResultDao {
	public Long countCommentNumForLiveDatabase(Long liveEventId);

	public Long countLikeNumForLiveDatabase(Long liveEventId);

	public LiveStatisticResult getBeanWithMaxNewRegisterUserNum(Date startTime, Date endTime);

	public LiveStatisticResult getBeanById(Long id);

	public LiveStatisticResult save(LiveStatisticResult bean);

	public LiveStatisticResult updateByUpdater(Updater<LiveStatisticResult> updater);
}