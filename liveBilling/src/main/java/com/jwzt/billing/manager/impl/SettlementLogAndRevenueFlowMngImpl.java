package com.jwzt.billing.manager.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.SettlementLogAndRevenueFlowDao;
import com.jwzt.billing.entity.SettlementLogAndRevenueFlow;
import com.jwzt.billing.manager.SettlementLogAndRevenueFlowMng;

/**
 * @author ysf
 */
@Service
public class SettlementLogAndRevenueFlowMngImpl implements SettlementLogAndRevenueFlowMng {

	@Override
	@Transactional(readOnly = true)
	public List<SettlementLogAndRevenueFlow> listBySettlementLog(Long settlementLogId) {
		return dao.listByParams(settlementLogId, null);
	}

	@Override
	@Transactional
	public SettlementLogAndRevenueFlow save(Long settlementLogId, Long revenueFlowId) {
		SettlementLogAndRevenueFlow bean = null;
		if (null != settlementLogId && null != revenueFlowId) {
			bean = new SettlementLogAndRevenueFlow();
			String id = UUID.randomUUID().toString();
			bean.setId(id);
			bean.setSettlementLogId(settlementLogId);
			bean.setRevenueFlowId(revenueFlowId);
			bean.initFieldValue();
			dao.save(bean);
		}
		return bean;
	}

	@Autowired
	private SettlementLogAndRevenueFlowDao dao;

}
