package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.VideoStatisticResultDao;
import com.jwzt.statistic.entity.VideoStatisticResult;
import com.jwzt.statistic.manager.VideoStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class VideoStatisticResultMngImpl implements VideoStatisticResultMng {

	@Override
	@Transactional(readOnly = true)
	public List<VideoStatisticResult> listByParams(Long videoId, Date startTime, Date endTime) {
		return dao.listByParams(videoId, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public VideoStatisticResult sumByParams(Long videoId, Date startTime, Date endTime) {
		return dao.sumByParams(videoId, startTime, endTime);
	}
	@Override
	@Transactional(readOnly = true)
	public VideoStatisticResult sumByParams2(Long videoId, Date startTime, Date endTime) {
		return dao.sumByParams2(videoId, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public VideoStatisticResult getBeanById(Long id) {
		VideoStatisticResult bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional
	public VideoStatisticResult save(VideoStatisticResult bean) {
		if (null != bean) {
			String id = UUID.randomUUID().toString().replace("-", "");
			bean.setId(id);
			bean.initFieldValue();
			return dao.save(bean);
		}
		return null;
	}

	@Override
	@Transactional
	public void update(VideoStatisticResult bean) {
		if (null != bean) {
			Updater<VideoStatisticResult> updater = new Updater<VideoStatisticResult>(bean);
			dao.updateByUpdater(updater);
		}
	}

	@Autowired
	private VideoStatisticResultDao dao;
}
