package com.jwzt.statistic.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.manager.FieldIdManagerMng;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.LiveInfoDao;
import com.jwzt.statistic.entity.IpAddress;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.LiveStatisticResult;
import com.jwzt.statistic.manager.IpAddressMng;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.LiveStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class LiveInfoMngImpl implements LiveInfoMng {
	
	@Override
	@Transactional(readOnly = true)
	public List<LiveInfo> listNeedStatistic() {
		List<LiveInfo> list = dao.listNeedStatistic();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,String enterpriseName,
			Date startTime, Date endTime, int pageNo, int pageSize,Integer startNum,Integer endNum) {
		Pagination page = dao.pageByParams(roomId, liveEventId, liveTitle, enterpriseId,enterpriseName, startTime, endTime, pageNo,
				pageSize,startNum,endNum);
		if (null != page) {
			List<LiveInfo> list = (List<LiveInfo>) page.getList();
			if (null != list) {
				for (LiveInfo bean : list) {
					initBean(bean);
				}
			}

		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public int enterpriseByMost(){
		return dao.enterpriseByMost();
	}
	
	@Override
	@Transactional(readOnly = true)
	public LiveInfo sumByLiveMostEnterprise(){
		return dao.sumByLiveMostEnterprise();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<LiveInfo> listByParams(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,
			Date startTime, Date endTime) {
		List<LiveInfo> list = dao.listByParams(roomId, liveEventId, liveTitle, enterpriseId, startTime, endTime);
		if (null != list) {
			for (LiveInfo bean : list) {
				initBean(bean);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public LiveInfo getBeanById(Long id) {
		LiveInfo bean = dao.getBeanById(id);
		initBean(bean);
		return bean;
	}

	@Override
	@Transactional(readOnly = true)
	public LiveInfo getBeanByLiveEventId(Long liveEventId) {
		LiveInfo bean = dao.getBeanByLiveEventId(liveEventId);
		initBean(bean);
		return bean;
	}

	@Override
	@Transactional
	public LiveInfo save(LiveInfo bean) {
		Long nextId = fieldIdManagerMng.getNextId("statistic_live_info", "id", 1L);
		if (nextId > 0) {
			bean.setId(nextId);
			return dao.save(bean);
		}
		return null;
	}

	@Override
	@Transactional
	public void update(LiveInfo bean) {
		if (null != bean) {
			Updater<LiveInfo> updater = new Updater<LiveInfo>(bean);
			dao.updateByUpdater(updater);
		}
	}

	private void initBean(LiveInfo bean) {
		if (null != bean) {
			Long id = bean.getId();
			if (null != id) {
				LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(id);
				if (null != liveStatisticResult) {
					liveStatisticResult.fillBlank();
					/*废弃卫旗写的错误代码
					IiveEventCurrentMax max = liveEventCurrentMaxMng.getByEventId(bean.getLiveEventId());
					if (max!=null) {
						long maxMinuteUserNum = max.getMaxPeople().longValue();
						long maxMinute = (max.getThisTime() - bean.getLiveBeginTime().getTime())/1000;
						liveStatisticResult.setMaxMinute(maxMinute);
						liveStatisticResult.setMaxMinuteUserNum(maxMinuteUserNum);
					}
					Long share = requestLogMng.getNumByRoom(bean.getRoomId(), 3).longValue();
					Long like = requestLogMng.getNumByRoom(bean.getRoomId(), 4).longValue();
					liveStatisticResult.setShareNum(share);
					liveStatisticResult.setLikeNum(like);
					*/
					bean.setStatisticResult(liveStatisticResult);
				}
			}
			Long ipCode = bean.getIpCode();
			if (null != ipCode) {
				IpAddress ipAddress = ipAddressMng.getBeanByIpCode(ipCode);
				bean.setIpAddress(ipAddress);
			}
		}
	}

	@Autowired
	private LiveInfoDao dao;
	@Autowired
	private FieldIdManagerMng fieldIdManagerMng;
	@Autowired
	private LiveStatisticResultMng liveStatisticResultMng;
	@Autowired
	private IpAddressMng ipAddressMng;

	@Override
	public LiveInfo sumByLiveMostEnterprise(Integer enterpriseId) {
		return dao.sumByLiveMostEnterprise(enterpriseId);
	}
}
