package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.SettlementLogDao;
import com.jwzt.billing.entity.SettlementLog;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Repository
public class SettlementLogDaoImpl extends BaseHibernateDao<SettlementLog, Long> implements SettlementLogDao {

	@Override
	public Pagination pageByParams(Integer enterpriseId, String enterpriseName, Integer[] status, Integer invoiceStatus,
			Date startTime, Date endTime, int pageNo, int pageSize) {
		Finder finder = createFinderByParams(enterpriseId, enterpriseName, status, invoiceStatus, startTime, endTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SettlementLog> listByParams(Integer enterpriseId, String enterpriseName, Integer[] status,
			Integer invoiceStatus, Date startTime, Date endTime) {
		Finder finder = createFinderByParams(enterpriseId, enterpriseName, status, invoiceStatus, startTime, endTime);
		return find(finder);
	}

	@Override
	public SettlementLog getBeanById(Long id) {
		SettlementLog bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public SettlementLog save(SettlementLog bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	protected Class<SettlementLog> getEntityClass() {
		return SettlementLog.class;
	}

	private Finder createFinderByParams(Integer enterpriseId, String enterpriseName, Integer[] status,
			Integer invoiceStatus, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from SettlementLog bean where 1=1");
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (StringUtils.isNotBlank(enterpriseName)) {
			finder.append(" and bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%" + enterpriseName + "%");
		}
		if (null != status && status.length > 0) {
			finder.append(" and bean.status in (:status)");
			finder.setParamList("status", status);
		}
		if (null != invoiceStatus) {
			finder.append(" and bean.invoiceStatus = :invoiceStatus");
			finder.setParam("invoiceStatus", invoiceStatus);
		}
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.createTime desc");
		return finder;
	}

}
