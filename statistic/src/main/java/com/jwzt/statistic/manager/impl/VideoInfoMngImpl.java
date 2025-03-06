package com.jwzt.statistic.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.VideoInfoDao;
import com.jwzt.statistic.entity.VideoInfo;
import com.jwzt.statistic.entity.VideoStatisticResult;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.VideoInfoMng;
import com.jwzt.statistic.manager.VideoStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class VideoInfoMngImpl implements VideoInfoMng {
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(Long videoId, String videoTitle, Date startTime, Date endTime, int pageNo,
			int pageSize) {
		Pagination page = dao.pageByParams(videoId, videoTitle, startTime, endTime, pageNo, pageSize);
		if (null != page) {
			List<VideoInfo> list = (List<VideoInfo>) page.getList();
			if (null != list) {
				for (VideoInfo bean : list) {
					initBean(bean, startTime, endTime);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<VideoInfo> listByParams(Long videoId, String videoTitle, Date startTime, Date endTime,
			boolean initBean) {
		List<VideoInfo> list = dao.listByParams(videoId, videoTitle, startTime, endTime);
		if (null != list && initBean) {
			for (VideoInfo bean : list) {
				initBean(bean, startTime, endTime);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public VideoInfo getBeanById(Long id) {
		VideoInfo bean = dao.getBeanById(id);
		if (bean!=null) {
			List<VideoStatisticResult> listByParams = videoStatisticResultMng.listByParams(id, null, null);
			if (listByParams!=null&&listByParams.size()>0) {
				VideoStatisticResult videoStatisticResult = listByParams.get(0);
				Long share = requestLogMng.getNumByRoom(bean.getRoomId(), 3).longValue();
				Long like = requestLogMng.getNumByRoom(bean.getRoomId(), 4).longValue();
				videoStatisticResult.setShareTotalNum(share);
				videoStatisticResult.setLikeTotalNum(like);
				bean.setStatisticResult(videoStatisticResult);
				
				bean.setStatisticResult(videoStatisticResult);
			}
		}
		return bean;
	}

	@Override
	@Transactional
	public VideoInfo save(VideoInfo bean) {
		if (null != bean) {
			bean.initFieldValue();
			return dao.save(bean);
		}
		return null;
	}

	@Override
	@Transactional
	public VideoInfo update(VideoInfo bean) {
		if (null != bean) {
			Updater<VideoInfo> updater = new Updater<VideoInfo>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	@Override
	@Transactional
	public VideoInfo saveOrUpdateFromDataMap(final Map<?, ?> dataMap) {
		String fileSize = (String) dataMap.get("fileSize");
		String widthHeight = (String) dataMap.get("widthHeight");
		String fileImg = (String) dataMap.get("fileImg");
		String fileDuration = (String) dataMap.get("fileDuration");
		String fileName = (String) dataMap.get("fileName");
		Integer roomId = (Integer) dataMap.get("roomId");
		Object userIdObj = dataMap.get("userId");
		Long userId = null;
		if (null != userIdObj) {
			if (Long.class.isInstance(userIdObj)) {
				userId = (Long) userIdObj;
			} else {
				userId = ((Integer) userIdObj).longValue();
			}
		}
		Object fileIdObj = dataMap.get("fileId");
		Long fileId = null;
		if (null != fileIdObj) {
			if (Long.class.isInstance(fileIdObj)) {
				fileId = (Long) fileIdObj;
			} else {
				fileId = ((Integer) fileIdObj).longValue();
			}
		}
		Object liveEventIdObj = dataMap.get("liveEventId");
		Long liveEventId = null;
		if (null != liveEventIdObj) {
			if (Long.class.isInstance(liveEventIdObj)) {
				liveEventId = (Long) liveEventIdObj;
			} else {
				liveEventId = ((Integer) liveEventIdObj).longValue();
			}
		}
		Integer enterpriseId = (Integer) dataMap.get("enterpriseId");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createTime;
		try {
			String createTimeStr = (String) dataMap.get("createTime");
			createTime = sdf.parse(createTimeStr);
		} catch (Exception e) {
			createTime = null;
		}
		VideoInfo videoInfo = getBeanById(fileId);
		if (null == videoInfo) {
			videoInfo = new VideoInfo();
			videoInfo.setId(fileId);
			videoInfo.setTitle(fileName);
			videoInfo.setFileImg(fileImg);
			videoInfo.setWidthHeight(widthHeight);
			videoInfo.setRoomId(roomId);
			videoInfo.setEnterpriseId(enterpriseId);
			videoInfo.setLiveEventId(liveEventId);
			videoInfo.setUserId(userId);
			try {
				videoInfo.setDuration(Long.parseLong(fileDuration));
			} catch (Exception e) {
			}
			try {
				videoInfo.setFileSize(Long.parseLong(fileSize));
			} catch (Exception e) {
			}
			if (null == createTime) {
				videoInfo.setCreateTime(null);
			} else {
				videoInfo.setCreateTime(new Timestamp(createTime.getTime()));
			}
			videoInfo = save(videoInfo);
		} else {
			videoInfo.setTitle(fileName);
			videoInfo.setFileImg(fileImg);
			videoInfo.setWidthHeight(widthHeight);
			videoInfo.setRoomId(roomId);
			videoInfo.setEnterpriseId(enterpriseId);
			videoInfo.setLiveEventId(liveEventId);
			videoInfo.setUserId(userId);
			try {
				videoInfo.setDuration(Long.parseLong(fileDuration));
			} catch (Exception e) {
			}
			try {
				videoInfo.setFileSize(Long.parseLong(fileSize));
			} catch (Exception e) {
			}
			if (null == createTime) {
				videoInfo.setCreateTime(null);
			} else {
				videoInfo.setCreateTime(new Timestamp(createTime.getTime()));
			}
			videoInfo = update(videoInfo);
		}
		return videoInfo;
	}

	private void initBean(VideoInfo bean, Date startTime, Date endTime) {
		if (null != bean) {
			Long id = bean.getId();
			if (null != id) {
				VideoStatisticResult videoStatisticResult = videoStatisticResultMng.sumByParams(id, startTime, endTime);
				if (null != videoStatisticResult) {
					videoStatisticResult.fillBlank();
					bean.setStatisticResult(videoStatisticResult);
				}
			}
		}
	}

	@Autowired
	private VideoInfoDao dao;
	@Autowired
	private VideoStatisticResultMng videoStatisticResultMng;
	
	@Autowired
	private RequestLogMng requestLogMng;
}
