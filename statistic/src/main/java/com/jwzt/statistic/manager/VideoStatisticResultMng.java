package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.statistic.entity.VideoStatisticResult;

public interface VideoStatisticResultMng {

	public List<VideoStatisticResult> listByParams(Long videoId, Date startTime, Date endTime);

	public VideoStatisticResult sumByParams(Long videoId, Date startTime, Date endTime);
	public VideoStatisticResult sumByParams2(Long videoId, Date startTime, Date endTime);

	public VideoStatisticResult getBeanById(Long id);

	public VideoStatisticResult save(VideoStatisticResult bean);

	public void update(VideoStatisticResult bean);
}