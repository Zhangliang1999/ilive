package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.UserViewLog;

public interface UserViewLogDao {
	public Long sumViewDuarationByParams(Long liveEventId, Integer enterpriseId, Date startTime, Date endTime);

	public Long countUserNumByParams(Long liveEventId, Integer enterpriseId, Long startDuration, Long endDuration,
			Date startTime, Date endTime);

	public List<UserViewLog> listByLiveEventId(Long liveEventId);

	public UserViewLog getBeanByParams(Long liveEventId, Integer requestType, Integer userType, String userId);

	public UserViewLog save(UserViewLog bean);

	public UserViewLog updateByUpdater(Updater<UserViewLog> updater);

}