package com.jwzt.statistic.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.EnterpriseInfoDao;
import com.jwzt.statistic.entity.EnterpriseInfo;
import com.jwzt.statistic.entity.EnterpriseStatisticResult;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.manager.EnterpriseStatisticResultMng;

/**
 * 
 * @author ysf
 *
 */
@Service
public class EnterpriseInfoMngImpl implements EnterpriseInfoMng {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(Integer enterpriseId, String enterpriseName, Date startOfCreateTime,
			Date endOfCreateTime, Date startOfStatisticTime, Date endOfStatisticTime, boolean initStatisticData,
			int pageNo, int pageSize,Integer certStatus, Integer entype, String bindPhone,Integer stamp) {
		Pagination page = dao.pageByParams(enterpriseId, enterpriseName, startOfCreateTime, endOfCreateTime, pageNo,
				pageSize,certStatus,entype,bindPhone,stamp);
		if (initStatisticData) {
			if (null != page) {
				List<EnterpriseInfo> list = (List<EnterpriseInfo>) page.getList();
				if (null != list) {
					for (EnterpriseInfo bean : list) {
						bean.transformMeaning();
						initBean(bean, startOfStatisticTime, endOfStatisticTime, initStatisticData);
					}
				}

			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EnterpriseInfo> listByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			boolean initStatisticData) {
		List<EnterpriseInfo> list = dao.listByParams(enterpriseId, enterpriseName, startTime, endTime);
		if (null != list) {
			for (EnterpriseInfo bean : list) {
				initBean(bean, startTime, endTime, initStatisticData);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public EnterpriseInfo getBeanById(Integer id) {
		EnterpriseInfo bean = dao.getBeanById(id);
		return bean;
	}

	@Override
	@Transactional
	public EnterpriseInfo save(EnterpriseInfo bean) {
		return dao.save(bean);
	}

	@Override
	@Transactional
	public EnterpriseInfo update(EnterpriseInfo bean) {
		if (null != bean) {
			Updater<EnterpriseInfo> updater = new Updater<EnterpriseInfo>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	@Override
	@Transactional
	public EnterpriseInfo saveOrUpdateFromDataMap(final Map<?, ?> dataMap) {
		String enterpriseName = (String) dataMap.get("enterpriseName");
		String enterpriseImg = (String) dataMap.get("enterpriseImg");
		String enterpriseDesc = (String) dataMap.get("enterpriseDesc");
		String homePageUrl = (String) dataMap.get("homePageUrl");
		Integer enterpriseId = (Integer) dataMap.get("enterpriseId");
		Integer certStatus = (Integer) dataMap.get("certStatus");
		String contactPhone = (String) dataMap.get("contactPhone"); 
		Integer entype = (Integer) dataMap.get("entype");
		String bindPhone = (String) dataMap.get("bindPhone");
		Integer stamp = (Integer) dataMap.get("stamp");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date applyTime;
		try {
			String applyTimeStr = (String) dataMap.get("applyTime");
			applyTime = sdf.parse(applyTimeStr);
		} catch (Exception e) {
			applyTime = null;
		}
		Date certTime;
		try {
			String certTimeStr = (String) dataMap.get("certTime");
			certTime = sdf.parse(certTimeStr);
		} catch (Exception e) {
			certTime = null;
		}
		EnterpriseInfo enterpriseInfo = getBeanById(enterpriseId);
		if (null == enterpriseInfo) {
			enterpriseInfo = new EnterpriseInfo();
			enterpriseInfo.setId(enterpriseId);
			enterpriseInfo.setEnterpriseName(enterpriseName);
			enterpriseInfo.setApplyTime(new Timestamp(applyTime.getTime()));
			enterpriseInfo.setEnterpriseImg(enterpriseImg);
			enterpriseInfo.setEnterpriseDesc(enterpriseDesc);
			enterpriseInfo.setHomePageUrl(homePageUrl);
			enterpriseInfo.setCertStatus(certStatus);
			if (null == certTime) {
				enterpriseInfo.setCertTime(null);
			} else {
				enterpriseInfo.setCertTime(new Timestamp(certTime.getTime()));
			}
			enterpriseInfo.setContactPhone(contactPhone);
			enterpriseInfo.setEntype(entype);
			enterpriseInfo.setBindPhone(bindPhone);
			enterpriseInfo.setStamp(stamp);
			enterpriseInfo = save(enterpriseInfo);
		} else {
			enterpriseInfo.setEnterpriseName(enterpriseName);
			enterpriseInfo.setApplyTime(new Timestamp(applyTime.getTime()));
			enterpriseInfo.setEnterpriseImg(enterpriseImg);
			enterpriseInfo.setEnterpriseDesc(enterpriseDesc);
			enterpriseInfo.setHomePageUrl(homePageUrl);
			enterpriseInfo.setCertStatus(certStatus);
			if (null == certTime) {
				enterpriseInfo.setCertTime(null);
			} else {
				enterpriseInfo.setCertTime(new Timestamp(certTime.getTime()));
			}
			enterpriseInfo.setContactPhone(contactPhone);
			enterpriseInfo.setEntype(entype);
			enterpriseInfo.setBindPhone(bindPhone);
			enterpriseInfo.setStamp(stamp);
			enterpriseInfo = update(enterpriseInfo);
		}
		return enterpriseInfo;
	}

	private void initBean(EnterpriseInfo bean, Date startTime, Date endTime, boolean initStatisticData) {
		if (null != bean) {
			Integer id = bean.getId();
			if (null != id) {
				if (initStatisticData) {
					EnterpriseStatisticResult enterpriseStatisticResult = enterpriseStatisticResultMng.sumByParams(id,
							startTime, endTime);
					if (null != enterpriseStatisticResult) {
						enterpriseStatisticResult.fillBlank();
						bean.setStatisticResult(enterpriseStatisticResult);
					}
				}
			}
		}
	}

	@Autowired
	private EnterpriseInfoDao dao;
	@Autowired
	private EnterpriseStatisticResultMng enterpriseStatisticResultMng;
}
