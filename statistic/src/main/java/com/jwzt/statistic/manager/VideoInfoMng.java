package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.VideoInfo;

public interface VideoInfoMng {
	public Pagination pageByParams(Long videoId, String videoTitle, Date startTime, Date endTime, int pageNo,
			int pageSize);

	public List<VideoInfo> listByParams(Long videoId, String videoTitle, Date startTime, Date endTime,
			boolean initBean);

	public VideoInfo getBeanById(Long id);

	public VideoInfo save(VideoInfo bean);

	public VideoInfo update(VideoInfo bean);

	public VideoInfo saveOrUpdateFromDataMap(final Map<?, ?> dataMap);
}