package com.jwzt.billing.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.SettlementLogDao;
import com.jwzt.billing.entity.RevenueFlow;
import com.jwzt.billing.entity.SettlementLog;
import com.jwzt.billing.entity.SettlementLogAndRevenueFlow;
import com.jwzt.billing.exception.ErrorPlatformRateException;
import com.jwzt.billing.exception.FlowNotFoundException;
import com.jwzt.billing.exception.FlowSettlementStatusErrorException;
import com.jwzt.billing.exception.SettlementLogNotFoundException;
import com.jwzt.billing.manager.RevenueFlowManager;
import com.jwzt.billing.manager.SettlementLogAndRevenueFlowMng;
import com.jwzt.billing.manager.SettlementLogMng;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.manager.FieldIdManagerMng;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Service
public class SettlementLogMngImpl implements SettlementLogMng {

	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(Integer enterpriseId, String enterpriseName, Integer[] status, Integer invoiceStatus,
			Date startTime, Date endTime, int pageNo, int pageSize) {
		return dao.pageByParams(enterpriseId, enterpriseName, status, invoiceStatus, startTime, endTime, pageNo,
				pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SettlementLog> listByParams(Integer enterpriseId, String enterpriseName, Integer[] status,
			Integer invoiceStatus, Date startTime, Date endTime) {
		return dao.listByParams(enterpriseId, enterpriseName, status, invoiceStatus, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public SettlementLog getBeanById(Long id) {
		SettlementLog bean = dao.getBeanById(id);
		initBean(bean, true);
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized SettlementLog save(final Integer enterpriseId, final String enterpriseName,
			final Long[] revenueFlowIds, final Double platformRate)
			throws ErrorPlatformRateException, FlowNotFoundException, FlowSettlementStatusErrorException, Exception {
		if (platformRate < 0 || platformRate > 1) {
			throw new ErrorPlatformRateException("错误的平台费率");
		}
		SettlementLog bean = null;
		if (null != enterpriseId && null != revenueFlowIds && revenueFlowIds.length > 0) {
			Long nextId = fieldIdManagerMng.getNextId("billing_settlement_log", "id", 1L);
			if (nextId > 0) {
				bean = new SettlementLog();
				bean.setId(nextId);
				// 获取流水清单，或抛出不正确流水ID的异常
				List<RevenueFlow> flowList = revenueFlowManager.listRevenueFlowForSettlementByIds(enterpriseId,
						revenueFlowIds);
				BigDecimal settleAmount = new BigDecimal("0.00");
				// 开始结算
				for (final RevenueFlow revenueFlow : flowList) {
					Long flowId = revenueFlow.getFlowId();
					Double flowPrice = revenueFlow.getFlowPrice();
					settleAmount = settleAmount.add(new BigDecimal(flowPrice.toString()));
					settlementLogAndRevenueFlowMng.save(nextId, flowId);
					revenueFlowManager.updateRevenueFlow(flowId, RevenueFlow.SETTLEMENT_STATUS_YES);
				}
				// 平台费用 四舍五入
				BigDecimal platformAmount = settleAmount.multiply(new BigDecimal(platformRate.toString()));
				platformAmount = platformAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal actualAmount = settleAmount.subtract(platformAmount);
				bean.setActualAmount(actualAmount.doubleValue());
				bean.setSettleAmount(settleAmount.doubleValue());
				bean.setPlatformAmount(platformAmount.doubleValue());
				bean.setEnterpriseId(enterpriseId);
				bean.setEnterpriseName(enterpriseName);
				bean.setStatus(SettlementLog.STATUS_SETTLED);
				bean.initFieldValue();
				dao.save(bean);
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SettlementLog cancelById(Long id) throws SettlementLogNotFoundException, Exception {
		SettlementLog bean = null;
		if (null != id) {
			bean = dao.getBeanById(id);
			if (null == bean) {
				throw new SettlementLogNotFoundException("结算单不存在");
			}
			bean.setId(id);
			bean.setStatus(SettlementLog.STATUS_CANCELED);
			Updater<SettlementLog> updater = new Updater<SettlementLog>(bean);
			dao.updateByUpdater(updater);
			List<SettlementLogAndRevenueFlow> settlementLogAndRevenueFlowList = settlementLogAndRevenueFlowMng
					.listBySettlementLog(id);
			if (null != settlementLogAndRevenueFlowList) {
				List<Long> flowIdList = new ArrayList<Long>();
				for (SettlementLogAndRevenueFlow settlementLogAndRevenueFlow : settlementLogAndRevenueFlowList) {
					if (null != settlementLogAndRevenueFlow) {
						Long revenueFlowId = settlementLogAndRevenueFlow.getRevenueFlowId();
						if (null != revenueFlowId) {
							RevenueFlow revenueFlow = revenueFlowManager.selectRevenueFlowByFlowId(revenueFlowId);
							Long flowId = revenueFlow.getFlowId();
							flowIdList.add(flowId);
						}
					}
				}
				Long[] flowIds = new Long[flowIdList.size()];
				flowIds = flowIdList.toArray(flowIds);
				revenueFlowManager.updateRevenueFlow(flowIds, RevenueFlow.SETTLEMENT_STATUS_NO);
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SettlementLog updateInvoiceStatus(Long id, Integer invoiceStatus) {
		SettlementLog bean = null;
		if (null != id && null != invoiceStatus) {
			bean = new SettlementLog();
			bean.setId(id);
			bean.setInvoiceStatus(invoiceStatus);
			Updater<SettlementLog> updater = new Updater<SettlementLog>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	private void initBean(final SettlementLog bean, final boolean initRevenueFlowList) {
		if (null != bean) {
			Long settlementLogId = bean.getId();
			if (null != settlementLogId) {
				List<SettlementLogAndRevenueFlow> settlementLogAndRevenueFlowList = settlementLogAndRevenueFlowMng
						.listBySettlementLog(settlementLogId);
				List<RevenueFlow> revenueFlowList = null;
				if (null != settlementLogAndRevenueFlowList) {
					Long[] revenueFlowIds = new Long[settlementLogAndRevenueFlowList.size()];
					for (int i = 0; i < settlementLogAndRevenueFlowList.size(); i++) {
						SettlementLogAndRevenueFlow settlementLogAndRevenueFlow = settlementLogAndRevenueFlowList.get(i);
						Long revenueFlowId = settlementLogAndRevenueFlow.getRevenueFlowId();
						revenueFlowIds[i] = revenueFlowId;
					}
					try {
						revenueFlowList = revenueFlowManager.listRevenueFlowByParams(revenueFlowIds, null, null, null,
								null, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					bean.setRevenueFlowList(revenueFlowList);
				}
			}
		}
	}

	@Autowired
	private SettlementLogDao dao;
	@Autowired
	private FieldIdManagerMng fieldIdManagerMng;
	@Autowired
	private SettlementLogAndRevenueFlowMng settlementLogAndRevenueFlowMng;
	@Autowired
	private RevenueFlowManager revenueFlowManager;
}
