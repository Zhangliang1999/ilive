package com.jwzt.billing.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.SettlementLogAndRevenueFlowDao;
import com.jwzt.billing.entity.SettlementLogAndRevenueFlow;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;

/**
 * @author ysf
 */
@Repository
public class SettlementLogAndRevenueFlowDaoImpl extends BaseHibernateDao<SettlementLogAndRevenueFlow, String>
		implements SettlementLogAndRevenueFlowDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<SettlementLogAndRevenueFlow> listByParams(Long settlementLogId, Long revenueFlowId) {
		Finder finder = createFinder(settlementLogId, revenueFlowId);
		return find(finder);
	}

	private Finder createFinder(Long settlementLogId, Long revenueFlowId) {
		Finder finder = Finder.create("select bean from SettlementLogAndRevenueFlow bean where 1=1");
		if (null != settlementLogId) {
			finder.append(" and bean.settlementLogId = :settlementLogId");
			finder.setParam("settlementLogId", settlementLogId);
		}
		if (null != revenueFlowId) {
			finder.append(" and bean.revenueFlowId = :revenueFlowId");
			finder.setParam("revenueFlowId", revenueFlowId);
		}
		finder.append(" order by bean.id asc, bean.createTime asc");
		return finder;
	}

	@Override
	public SettlementLogAndRevenueFlow save(SettlementLogAndRevenueFlow bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	protected Class<SettlementLogAndRevenueFlow> getEntityClass() {
		return SettlementLogAndRevenueFlow.class;
	}

}
