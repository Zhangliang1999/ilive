package com.jwzt.billing.dao;
/**
* @author ysf
*/

import java.util.List;

import com.jwzt.billing.entity.SettlementLogAndRevenueFlow;

public interface SettlementLogAndRevenueFlowDao {
	List<SettlementLogAndRevenueFlow> listByParams(Long settlementLogId, Long revenueFlowId);

	SettlementLogAndRevenueFlow save(SettlementLogAndRevenueFlow bean);

}
