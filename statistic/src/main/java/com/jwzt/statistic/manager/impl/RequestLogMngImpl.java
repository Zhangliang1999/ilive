package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.RequestLogDao;
import com.jwzt.statistic.entity.IpAddress;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.UserInfo;
import com.jwzt.statistic.entity.vo.RankInfo;
import com.jwzt.statistic.manager.IpAddressMng;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.UserInfoMng;

/**
 * 
 * @author ysf
 *
 */
@Service
@Transactional
public class RequestLogMngImpl implements RequestLogMng {
	@Override
	@Transactional(readOnly = true)
	public List<Long> listNullIpCode() {
		return dao.listNullIpCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime, boolean initLiveInfoData, boolean initUserInfoData,
			boolean initIpAddressData, int pageNo, int pageSize) {
		Pagination page = dao.pageByParams(liveEventId, enterpriseId, videoId, logTypes, userIds, startTime, endTime,
				pageNo, pageSize);
		if (null != page) {
			List<RequestLog> list = (List<RequestLog>) page.getList();
			if (null != list) {
				for (RequestLog bean : list) {
					initBean(bean, initLiveInfoData, initUserInfoData, initIpAddressData);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RequestLog> listByParams(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime, boolean initLiveInfoData, boolean initUserInfoData,
			boolean initIpAddressData) {
		List<RequestLog> list = dao.listByParams(liveEventId, enterpriseId, videoId, logTypes, userIds, startTime,
				endTime);
		if (null != list) {
			for (RequestLog bean : list) {
				initBean(bean, initLiveInfoData, initUserInfoData, initIpAddressData);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RankInfo> listRankGroupByUser(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			Date startTime, Date endTime, Integer topNum) {
		return dao.listRankGroupByUser(liveEventId, enterpriseId, videoId, logTypes, startTime, endTime, topNum);
	}

	@Override
	@Transactional(readOnly = true)
	public Long countUserNum(Long liveEventId, Integer enterpriseId, Long videoId, Integer requestType,
			Integer[] logTypes, Date startTime, Date endTime) {
		return dao.countUserNum(liveEventId, enterpriseId, videoId, requestType, logTypes, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public Long countRequestNum(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			Date startTime, Date endTime) {
		return dao.countRequestNum(liveEventId, enterpriseId, videoId, null , logTypes, startTime, endTime);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long countRequestNum(Long liveEventId, Integer enterpriseId, Long videoId, Integer requestType , Integer[] logTypes,
			Date startTime, Date endTime) {
		return dao.countRequestNum(liveEventId, enterpriseId, videoId,requestType, logTypes, startTime, endTime);
	}

	@Override
	@Transactional
	public RequestLog save(RequestLog bean) {
		String id = UUID.randomUUID().toString().replace("-", "");
		bean.setId(id);
		bean.initFieldValue();
		return dao.save(bean);
	}

	@Override
	@Transactional
	public RequestLog update(RequestLog bean) {
		return dao.update(bean);
	}

	private void initBean(RequestLog bean, boolean initLiveInfoData, boolean initUserInfoData,
			boolean initIpAddressData) {
		if (null != bean) {
			if (initLiveInfoData) {
				Long liveEventId = bean.getLiveEventId();
				if (null != liveEventId) {
					LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
					bean.setLiveInfo(liveInfo);
				}
			}
			if (initUserInfoData) {
				String userId = bean.getUserId();
				if (StringUtils.isNotBlank(userId)) {
					try {
						UserInfo userInfo = userInfoMng.getBeanById(Long.parseLong(userId));
						bean.setUserInfo(userInfo);
					} catch (Exception e) {
					}
				}
			}
			if (initIpAddressData) {
				Long ipCode = bean.getIpCode();
				if (null != ipCode) {
					IpAddress ipAddress = ipAddressMng.getBeanByIpCode(ipCode);
					bean.setIpAddress(ipAddress);
				}
			}
		}
	}

	@Autowired
	private RequestLogDao dao;
	@Autowired
	private IpAddressMng ipAddressMng;
	@Autowired
	private LiveInfoMng liveInfoMng;
	@Autowired
	private UserInfoMng userInfoMng;
	@Override
	public Pagination getLiveViewList(Long userId,Integer roomId,Long videoId,Integer pageNo,Integer pageSize,Integer type) {
		return dao.getLiveViewList(userId,roomId,videoId,pageNo,pageSize,type);
	}

	@Override
	public List<RequestLog> getLiveViewList(Long userId, Integer roomId, Long videoId, Integer type) {
		return dao.getLiveViewList(userId,roomId,videoId,type);
	}

	@Override
	@Transactional
	public Integer getNumByEvent(Long eventId, Integer type) {
		return dao.getNumByEvent(eventId,type);
	}

	@Override
	@Transactional
	public Integer getNumByRoom(Integer roomId, Integer type) {
		return dao.getNumByRoom(roomId,type);
	}

	@Override
	public Integer getNumByEvent(Long eventId, Integer type, boolean isDistinct) {
		return dao.getNumByEvent(eventId,type,isDistinct);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<RequestLog> listByParams(Long roomId , Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime, boolean initLiveInfoData, boolean initUserInfoData,
			boolean initIpAddressData) {
		List<RequestLog> list = dao.listByParams(roomId , liveEventId, enterpriseId, videoId, logTypes, userIds, startTime,
				endTime);
		if (null != list) {
			for (RequestLog bean : list) {
				initBean(bean, initLiveInfoData, initUserInfoData, initIpAddressData);
			}
		}
		return list;
	}
}
