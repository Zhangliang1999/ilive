package com.jwzt.billing.manager;

import java.util.List;

import com.jwzt.billing.entity.SettlementLogAndRevenueFlow;

/**
 * @author ysf
 */
public interface SettlementLogAndRevenueFlowMng {
	List<SettlementLogAndRevenueFlow> listBySettlementLog(Long settlementLogId);

	SettlementLogAndRevenueFlow save(Long settlementLogId, Long revenueFlowId);
}
