package com.jwzt.billing.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.RevenueFlowDao;
import com.jwzt.billing.entity.RevenueFlow;
import com.jwzt.billing.exception.FlowNotFoundException;
import com.jwzt.billing.exception.FlowSettlementStatusErrorException;
import com.jwzt.billing.exception.NotSameEnterpriseException;
import com.jwzt.billing.manager.RevenueFlowManager;
import com.jwzt.common.manager.FieldIdManagerMng;
import com.jwzt.common.page.Pagination;

@Service
public class RevenueFlowManagerImpl implements RevenueFlowManager {
	@Autowired
	private RevenueFlowDao revenueFlowDao;

	@Autowired
	private FieldIdManagerMng fieldIdManagerMng;

	@Override
	@Transactional
	public void addRevenueFlow(RevenueFlow revenueFlow) throws Exception {
		Long nextId = fieldIdManagerMng.getNextId("billing_revenue_flow", "flow_id", 1l);
		revenueFlow.setFlowId(nextId);
		revenueFlowDao.insertRevenueFlow(revenueFlow);
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination selectRevenueFlowByPage(Integer pageNo, Integer settlementStatus, Integer flowType,
			String enterpriseContent, String userContent, Date startTime, Date endTime) throws Exception {
		Pagination pagination = revenueFlowDao.selectRevenueFlowByPage(pageNo, settlementStatus, flowType,
				enterpriseContent, userContent, startTime, endTime);
		return pagination;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RevenueFlow> selectRevenueFlowBySettlementStatus(Integer settlementStatus) throws Exception {
		List<RevenueFlow> flows = revenueFlowDao.selectRevenueFlowBySettlementStatus(settlementStatus);
		return flows;
	}

	@Override
	@Transactional
	public void updateRevenueFlow(Long flowId, Integer settlementStatus) throws Exception{
		revenueFlowDao.updateRevenueFlow(flowId, settlementStatus);
	}
	@Override
	@Transactional
	public void updateRevenueFlow(Long[] flowIds, Integer settlementStatus) throws Exception {
		for (int i = 0; i < flowIds.length; i++) {
			Long flowId = flowIds[i];
			revenueFlowDao.updateRevenueFlow(flowId, settlementStatus);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public RevenueFlow selectRevenueFlowByFlowId(Long flowId) throws Exception {
		RevenueFlow RevenueFlow = revenueFlowDao.selectRevenueFlowByFlowId(flowId);
		return RevenueFlow;
	}

	@Override
	public Pagination selectRevenueFlowByPage(Integer pageNo, String userRoom, Integer flowType, Date startDate,
			Date endDate) throws Exception {
		Pagination pagination = revenueFlowDao.selectRevenueFlowByPage(pageNo, userRoom, flowType, startDate, endDate);
		return pagination;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RevenueFlow> selectRevenueFlowsByEnterprise(String enterpriseName) throws Exception {
		List<RevenueFlow> flows = revenueFlowDao.selectRevenueFlowsByEnterprise(enterpriseName);
		return flows;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RevenueFlow> selectRevenueFlowsByEnterpriseId(Integer enterpriseId) throws Exception {
		List<RevenueFlow> flows = revenueFlowDao.selectRevenueFlowsByEnterpriseId(enterpriseId);
		return flows;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RevenueFlow> listRevenueFlowForSettlement(Integer enterpriseId, Integer settlementStatus,
			Date startTime, Date endTime) throws FlowNotFoundException, FlowSettlementStatusErrorException, Exception {
		final List<RevenueFlow> flowList = revenueFlowDao.listRevenueFlowByParams(null, enterpriseId, null,
				settlementStatus, startTime, endTime);
		return flowList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RevenueFlow> listRevenueFlowForSettlementByIds(Integer enterpriseId, Long[] revenueFlowIds)
			throws FlowNotFoundException, FlowSettlementStatusErrorException, NotSameEnterpriseException, Exception {
		final List<RevenueFlow> flowList = new ArrayList<RevenueFlow>();
		final List<Long> notSameEnterpriseFlowIdList = new ArrayList<Long>();
		final List<Long> notFountFlowIdList = new ArrayList<Long>();
		final List<Long> settledFlowIdList = new ArrayList<Long>();
		for (final Long revenueFlowId : revenueFlowIds) {
			RevenueFlow selectRevenueFlow = selectRevenueFlowByFlowId(revenueFlowId);
			if (null == selectRevenueFlow) {
				notFountFlowIdList.add(revenueFlowId);
				continue;
			}
			if (null != enterpriseId && !enterpriseId.equals(selectRevenueFlow.getEnterpriseId())) {
				notSameEnterpriseFlowIdList.add(revenueFlowId);
			}
			enterpriseId = selectRevenueFlow.getEnterpriseId();
			Integer settlementStatus = selectRevenueFlow.getSettlementStatus();
			if (RevenueFlow.SETTLEMENT_STATUS_YES.equals(settlementStatus)) {
				settledFlowIdList.add(revenueFlowId);
				continue;
			}
			flowList.add(selectRevenueFlow);
		}
		if (notSameEnterpriseFlowIdList.size() > 0) {
			throw new NotSameEnterpriseException("不是一个企业的流水", notSameEnterpriseFlowIdList);
		}
		if (notFountFlowIdList.size() > 0) {
			throw new FlowNotFoundException("结算单有不存在的流水ID", notFountFlowIdList);
		}
		if (settledFlowIdList.size() > 0) {
			throw new FlowSettlementStatusErrorException("结算单有已结算的流水ID", settledFlowIdList);
		}
		return flowList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RevenueFlow> listRevenueFlowByParams(Long[] revenueFlowIds, Integer enterpriseId, String enterpriseName,
			Integer settlementStatus, Date startTime, Date endTime) throws Exception {
		final List<RevenueFlow> flowList = revenueFlowDao.listRevenueFlowByParams(revenueFlowIds, enterpriseId,
				enterpriseName, settlementStatus, startTime, endTime);
		return flowList;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime, int pageNo,
			int pageSize) {
		final Pagination page = revenueFlowDao.pageByParams(settlementStatus, flowType, flagId, enterpriseId,
				enterpriseName, userId, userName, startTime, endTime, pageNo, pageSize);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RevenueFlow> listByParams(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime) {
		final List<RevenueFlow> flowList = revenueFlowDao.listByParams(settlementStatus, flowType, flagId, enterpriseId,
				enterpriseName, userId, userName, startTime, endTime);
		return flowList;
	}

	@Override
	@Transactional(readOnly = true)
	public Double sumUnsettlementAccountByEnterprise(Integer enterpriseId) {
		final Double sumUnsettlementAccountByEnterprise = revenueFlowDao
				.sumUnsettlementAccountByEnterprise(enterpriseId);
		return sumUnsettlementAccountByEnterprise;
	}
}
