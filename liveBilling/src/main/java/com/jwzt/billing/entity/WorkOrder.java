package com.jwzt.billing.entity;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwzt.billing.entity.base.BaseWorkOrder;
import com.jwzt.billing.entity.bo.WorkLog;

/**
 * @author ysf
 */
public class WorkOrder extends BaseWorkOrder {

	private static final long serialVersionUID = -1831201123758897566L;
	/**
	 * 工单类型：开通收益账户
	 */
	public final static Integer WORK_ORDER_TYPE_OPEN_REVENUE_ACCOUNT = 1;
	/**
	 * 工单类型：开通红包账户
	 */
	public final static Integer WORK_ORDER_TYPE_OPEN_RED_PACKAGE_ACCOUNT = 2;
	/**
	 * 状态；新增
	 */
	public static final Integer STATUS_NEW = 1;
	/**
	 * 状态；处理中
	 */
	public static final Integer STATUS_PROCESSING = 2;
	/**
	 * 状态；完成
	 */
	public static final Integer STATUS_COMPLETED = 3;
	/**
	 * 状态；取消
	 */
	public static final Integer STATUS_CANCELED = 4;

	private List<WorkLog> workLogList;
	@JsonIgnore
	private EnterpriseBilling enterpriseBilling;

	public String getEnterpriseName() {
		if (null != enterpriseBilling) {
			return enterpriseBilling.getEnterpriseName();
		}
		return null;
	}

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatus()) {
			setStatus(STATUS_NEW);
		}
	}

	public WorkOrder() {
		super();
	}

	public List<WorkLog> getWorkLogList() {
		return workLogList;
	}

	public void setWorkLogList(List<WorkLog> workLogList) {
		this.workLogList = workLogList;
	}

	public EnterpriseBilling getEnterpriseBilling() {
		return enterpriseBilling;
	}

	public void setEnterpriseBilling(EnterpriseBilling enterpriseBilling) {
		this.enterpriseBilling = enterpriseBilling;
	}
	
}
