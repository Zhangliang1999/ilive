package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.VideoInfo;

public interface VideoInfoDao {
	public Pagination pageByParams(Long videoId, String videoTitle, Date startTime, Date endTime, int pageNo,
			int pageSize);

	public List<VideoInfo> listByParams(Long videoId, String videoTitle, Date startTime, Date endTime);

	public VideoInfo getBeanById(Long id);

	public VideoInfo save(VideoInfo bean);

	public VideoInfo updateByUpdater(Updater<VideoInfo> updater);
}