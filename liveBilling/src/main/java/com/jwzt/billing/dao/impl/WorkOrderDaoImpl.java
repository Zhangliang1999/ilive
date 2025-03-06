package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.WorkOrderDao;
import com.jwzt.billing.entity.WorkOrder;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Repository
public class WorkOrderDaoImpl extends BaseHibernateDao<WorkOrder, Long> implements WorkOrderDao {

	@Override
	public Pagination pageByParams(Integer enterpriseId, Integer workOrderType, Integer status, Date startTime,
			Date endTime, int pageNo, int pageSize) {
		Finder finder = createFinderByParams(enterpriseId, workOrderType, status, startTime, endTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkOrder> listByParams(Integer enterpriseId, Integer workOrderType, Integer status, Date startTime,
			Date endTime) {
		Finder finder = createFinderByParams(enterpriseId, workOrderType, status, startTime, endTime);
		return find(finder);
	}

	@Override
	public WorkOrder getBeanById(Long id) {
		WorkOrder bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public WorkOrder save(WorkOrder bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	public WorkOrder update(WorkOrder bean) {
		if (null != bean) {
			getSession().update(bean);
		}
		return bean;
	}

	@Override
	protected Class<WorkOrder> getEntityClass() {
		return WorkOrder.class;
	}

	private Finder createFinderByParams(Integer enterpriseId, Integer workOrderType, Integer status, Date startTime,
			Date endTime) {
		Finder finder = Finder.create("select bean from WorkOrder bean where 1=1");
		if (null != workOrderType) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != workOrderType) {
			finder.append(" and bean.workOrderType = :workOrderType");
			finder.setParam("workOrderType", workOrderType);
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
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
