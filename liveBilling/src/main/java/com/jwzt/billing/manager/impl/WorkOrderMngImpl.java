package com.jwzt.billing.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.WorkOrderDao;
import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.billing.entity.WorkOrder;
import com.jwzt.billing.manager.EnterpriseBillingMng;
import com.jwzt.billing.manager.WorkOrderMng;
import com.jwzt.common.manager.FieldIdManagerMng;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Service
public class WorkOrderMngImpl implements WorkOrderMng {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(Integer enterpriseId, Integer paymentType, Integer status, Date startTime,
			Date endTime, int pageNo, int pageSize, boolean initBean) {
		Pagination page = dao.pageByParams(enterpriseId, paymentType, status, startTime, endTime, pageNo, pageSize);
		if (null != page && initBean) {
			List<WorkOrder> list = (List<WorkOrder>) page.getList();
			if (null != list) {
				for (WorkOrder bean : list) {
					initBean(bean);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<WorkOrder> listByParams(Integer enterpriseId, Integer paymentType, Integer status, Date startTime,
			Date endTime, boolean initBean) {
		List<WorkOrder> list = dao.listByParams(enterpriseId, paymentType, status, startTime, endTime);
		if (null != list && initBean) {
			for (WorkOrder bean : list) {
				initBean(bean);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public WorkOrder getBeanById(Long id, boolean initBean) {
		WorkOrder bean = dao.getBeanById(id);
		if (initBean) {
			initBean(bean);
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WorkOrder save(final WorkOrder bean) {
		if (null != bean) {
			Long nextId = fieldIdManagerMng.getNextId("billing_work_order", "id", 1L);
			if (nextId > 0) {
				bean.setId(nextId);
				bean.initFieldValue();
				dao.save(bean);
				Integer workOrderType = bean.getWorkOrderType();
				Integer enterpriseId = bean.getEnterpriseId();
				if (null != enterpriseId) {
					if (WorkOrder.WORK_ORDER_TYPE_OPEN_REVENUE_ACCOUNT.equals(workOrderType)) {
						enterpriseBillingMng.updateRevenueAccount(enterpriseId, false, nextId);
					} else if (WorkOrder.WORK_ORDER_TYPE_OPEN_RED_PACKAGE_ACCOUNT.equals(workOrderType)) {
						enterpriseBillingMng.updateRedPackageAccount(enterpriseId, false, nextId);
					}
				}
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WorkOrder update(WorkOrder bean) {
		if (null != bean) {
			bean = dao.update(bean);
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WorkOrder processById(Long id) {
		WorkOrder bean = null;
		if (null != id) {
			bean = dao.getBeanById(id);
			if (null != bean) {
				bean.setStatus(WorkOrder.STATUS_PROCESSING);
				bean = dao.update(bean);
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WorkOrder completeById(Long id) {
		WorkOrder bean = null;
		if (null != id) {
			bean = dao.getBeanById(id);
			if (null != bean) {
				bean.setStatus(WorkOrder.STATUS_COMPLETED);
				bean = dao.update(bean);
				Integer enterpriseId = bean.getEnterpriseId();
				if (null != enterpriseId) {
					EnterpriseBilling enterpriseBilling = enterpriseBillingMng.getBeanById(enterpriseId, false);
					if (null != enterpriseBilling) {
						Integer workOrderType = bean.getWorkOrderType();
						if (WorkOrder.WORK_ORDER_TYPE_OPEN_REVENUE_ACCOUNT.equals(workOrderType)) {
							enterpriseBillingMng.updateRevenueAccount(enterpriseId, true, id);
						} else if (WorkOrder.WORK_ORDER_TYPE_OPEN_RED_PACKAGE_ACCOUNT.equals(workOrderType)) {
							enterpriseBillingMng.updateRedPackageAccount(enterpriseId, true, id);
						}
					}
				}
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WorkOrder cancelById(Long id, String cancelReason) {
		WorkOrder bean = null;
		if (null != id) {
			bean = dao.getBeanById(id);
			if (null != bean) {
				bean.setStatus(WorkOrder.STATUS_CANCELED);
				bean = dao.update(bean);
			}
		}
		return bean;
	}

	private void initBean(final WorkOrder bean) {
		if (null != bean) {
			Integer enterpriseId = bean.getEnterpriseId();
			if (null != enterpriseId) {
				EnterpriseBilling enterpriseBilling = enterpriseBillingMng.getBeanById(enterpriseId, false);
				bean.setEnterpriseBilling(enterpriseBilling);
			}
		}
	}

	@Autowired
	private WorkOrderDao dao;
	@Autowired
	private FieldIdManagerMng fieldIdManagerMng;
	@Autowired
	private EnterpriseBillingMng enterpriseBillingMng;
}
