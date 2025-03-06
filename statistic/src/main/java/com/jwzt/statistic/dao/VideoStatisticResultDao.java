package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.VideoStatisticResult;

public interface VideoStatisticResultDao {

	public List<VideoStatisticResult> listByParams(Long videoId, Date startTime, Date endTime);

	public VideoStatisticResult sumByParams(Long videoId, Date startTime, Date endTime);
	public VideoStatisticResult sumByParams2(Long videoId, Date startTime, Date endTime);

	public VideoStatisticResult getBeanById(Long id);

	public VideoStatisticResult save(VideoStatisticResult bean);

	public VideoStatisticResult updateByUpdater(Updater<VideoStatisticResult> updater);
}