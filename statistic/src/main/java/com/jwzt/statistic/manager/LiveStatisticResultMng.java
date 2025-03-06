package com.jwzt.statistic.manager;

import java.util.Date;

import com.jwzt.statistic.entity.LiveStatisticResult;

public interface LiveStatisticResultMng {

	public Long countCommentNumForLiveDatabase(Long liveEventId);

	public Long countLikeNumForLiveDatabase(Long liveEventId);

	public LiveStatisticResult getBeanWithMaxNewRegisterUserNum(Date startTime, Date endTime);

	public LiveStatisticResult getBeanById(Long id);

	public LiveStatisticResult save(LiveStatisticResult bean);

	public void update(LiveStatisticResult bean);
}