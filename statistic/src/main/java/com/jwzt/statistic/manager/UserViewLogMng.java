package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.statistic.entity.UserViewLog;

public interface UserViewLogMng {

	public Long sumViewDuarationByParams(Long liveEventId, Integer enterpriseId, Date startTime, Date endTime);

	public Long countUserNumByParams(Long liveEventId, Integer enterpriseId, Long startDuration, Long endDuration,
			Date startTime, Date endTime);

	public List<UserViewLog> listByLiveEventId(Long liveEventId);

	public UserViewLog getBeanByParams(Long liveEventId, Integer requestType, Integer userType, String userId);

	public UserViewLog save(UserViewLog bean);

	public void update(UserViewLog bean);
}