package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.dao.LocationStatisticResultDao;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.manager.LocationStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class LocationStatisticResultMngImpl implements LocationStatisticResultMng {

	@Override
	@Transactional(readOnly = true)
	public List<LocationStatisticResult> listByFlag(Long flagId, Integer flagType, Date startTime, Date endTime) {
		List<LocationStatisticResult> list = dao.listByFlag(flagId, flagType, startTime, endTime);
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LocationStatisticResult> listStatisticResutlByFlag(Long flagId, Integer flagType, Date startTime,
			Date endTime) {
		List<LocationStatisticResult> list = null;
		if (LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW.equals(flagType)) {
			list = dao.listStatisticResutlForLiveEventView(flagId);
		} else if (LocationStatisticResult.FLAG_TYPE_ENTERPRISE_BEGIN_LIVE.equals(flagType)) {
			list = dao.listStatisticResutlForEnterpriseBeginLive(flagId, startTime, endTime);
		} else if (LocationStatisticResult.FLAG_TYPE_ENTERPRISE_LIVE_VIEW.equals(flagType)) {
			list = dao.listStatisticResutlForEnterpriseLiveView(flagId, startTime, endTime);
		} else if (LocationStatisticResult.FLAG_TYPE_VIDEO_VIEW.equals(flagType)) {
			list = dao.listStatisticResutlForVideo(flagId, startTime, endTime);
		} else if (LocationStatisticResult.FLAG_TYPE_TOTAL_VIEW.equals(flagType)) {
			list = dao.listStatisticResutlForTotalView(startTime, endTime);
		} else if (LocationStatisticResult.FLAG_TYPE_TOTAL_BEGIN_LIVE.equals(flagType)) {
			list = dao.listStatisticResutlForTotalBeginLive(startTime, endTime);
		}
		return list;
	}
	@Override
	@Transactional(readOnly = true)
	public List<LocationStatisticResult> listStatisticResutlByFlagGroupByCity(Long flagId, Integer flagType, Date startTime,
			Date endTime) {
		List<LocationStatisticResult> list = null;
		if (LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW.equals(flagType)) {
			list = dao.listStatisticResutlForLiveEventViewGroupByCity(flagId);
		} else if (LocationStatisticResult.FLAG_TYPE_ENTERPRISE_BEGIN_LIVE.equals(flagType)) {
			list = dao.listStatisticResutlForEnterpriseBeginLive(flagId, startTime, endTime);
		} else if (LocationStatisticResult.FLAG_TYPE_ENTERPRISE_LIVE_VIEW.equals(flagType)) {
			list = dao.listStatisticResutlForEnterpriseLiveView(flagId, startTime, endTime);
		} else if (LocationStatisticResult.FLAG_TYPE_VIDEO_VIEW.equals(flagType)) {
			list = dao.listStatisticResutlForVideo(flagId, startTime, endTime);
		} else if (LocationStatisticResult.FLAG_TYPE_TOTAL_VIEW.equals(flagType)) {
			list = dao.listStatisticResutlForTotalView(startTime, endTime);
		} else if (LocationStatisticResult.FLAG_TYPE_TOTAL_BEGIN_LIVE.equals(flagType)) {
			list = dao.listStatisticResutlForTotalBeginLive(startTime, endTime);
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public LocationStatisticResult getBeanByProvinceName(String provinceName, Long flagId, Integer flagType,
			Date startTime, Date endTime) {
		return dao.getBeanByProvinceName(provinceName, flagId, flagType, startTime, endTime);
	}

	@Override
	@Transactional
	public LocationStatisticResult save(LocationStatisticResult bean) {
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
	public void update(LocationStatisticResult bean) {
		if (null != bean) {
			Updater<LocationStatisticResult> updater = new Updater<LocationStatisticResult>(bean);
			dao.updateByUpdater(updater);
		}
	}

	@Autowired
	private LocationStatisticResultDao dao;
}
