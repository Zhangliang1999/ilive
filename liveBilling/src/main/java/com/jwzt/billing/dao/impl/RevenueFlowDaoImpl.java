package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.RevenueFlowDao;
import com.jwzt.billing.entity.RevenueFlow;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;

@Repository
public class RevenueFlowDaoImpl extends BaseHibernateDao<RevenueFlow, Long> implements RevenueFlowDao {

	@Override
	protected Class<RevenueFlow> getEntityClass() {
		return RevenueFlow.class;
	}

	@Override
	public void insertRevenueFlow(RevenueFlow revenueFlow) {
		getSession().save(revenueFlow);
	}

	@Override
	public Pagination selectRevenueFlowByPage(Integer pageNo, Integer settlementStatus, Integer flowType,
			String enterpriseContent, String userContent, Date startTime, Date endTime) throws Exception {
		Finder finder = Finder.create("from RevenueFlow bean");
		finder.append(" where 1=1 ");
		if (settlementStatus != null && settlementStatus != 0) {
			finder.append(" and bean.settlementStatus=:settlementStatus ");
			finder.setParam("settlementStatus", settlementStatus);
		}
		if (flowType != null && flowType != 0) {
			finder.append(" and bean.flowType=:flowType ");
			finder.setParam("flowType", flowType);
		}
		if (enterpriseContent != null && !enterpriseContent.equals("")) {
			finder.append(" and ( bean.enterpriseName=:enterpriseName ");
			finder.setParam("enterpriseName", enterpriseContent);
			try {
				Integer parseInt = Integer.parseInt(enterpriseContent);
				finder.append(" or bean.enterpriseId=:enterpriseId) ");
				finder.setParam("enterpriseId", parseInt);
			} catch (Exception e) {
				finder.append(" ) ");
			}
		}
		if (userContent != null && !userContent.equals("")) {
			finder.append(" and ( bean.userName=:userName ");
			finder.setParam("userName", userContent);
			try {
				Integer parseInt = Integer.parseInt(userContent);
				finder.append(" or bean.userId=:userId) ");
				finder.setParam("userId", parseInt);
			} catch (Exception e) {
				finder.append(" ) ");
			}
		}
		if (startTime != null) {
			finder.append(" and bean.flowTime>:startTime");
			finder.setParam("endTime", endTime);
		}
		if (endTime != null) {
			finder.append(" and bean.flowTime<:endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.flowTime desc ");
		Pagination pagination = find(finder, pageNo, 20);
		return pagination;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RevenueFlow> selectRevenueFlowBySettlementStatus(Integer settlementStatus) throws Exception {
		Finder finder = Finder.create("from RevenueFlow bean where bean.settlementStatus=:settlementStatus ");
		finder.setParam("settlementStatus", settlementStatus);
		List<RevenueFlow> flows = find(finder);
		return flows;
	}

	@Override
	public void updateRevenueFlow(Long flowId, Integer settlementStatus) throws Exception {
		RevenueFlow RevenueFlow = get(flowId);
		RevenueFlow.setSettlementStatus(settlementStatus);
		getSession().update(RevenueFlow);
	}

	@Override
	public RevenueFlow selectRevenueFlowByFlowId(Long flowId) throws Exception {
		return get(flowId);
	}

	@Override
	public Pagination selectRevenueFlowByPage(Integer pageNo, String userRoom, Integer flowType, Date startDate,
			Date endDate) throws Exception {
		Finder finder = Finder.create("from RevenueFlow bean");
		finder.append(" where 1=1 ");
		if (flowType != 0 && flowType != null) {
			finder.append(" and bean.flowType=:flowType ");
			finder.setParam("flowType", flowType);
		}
		if (userRoom != null && !userRoom.equals("")) {
			finder.append(" and (bean.userName=:userName ");
			finder.setParam("userName", userRoom);
			try {
				Integer valueOf = Integer.valueOf(userRoom);
				finder.append(" or bean.roomId=:roomId) ");
				finder.setParam("roomId", valueOf);
			} catch (Exception e) {
				finder.append(" ) ");
			}
		}
		if (startDate != null && endDate != null) {
			finder.append(" and bean.flowTime>:startTime  and bean.flowTime<:endTime");
			finder.setParam("startTime", startDate);
			finder.setParam("endTime", endDate);
		}
		finder.append(" order by bean.flowTime desc ");
		Pagination pagination = find(finder, pageNo, 20);
		return pagination;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RevenueFlow> selectRevenueFlowsByEnterprise(String enterpriseName) throws Exception {
		Finder finder = Finder.create("from RevenueFlow bean where bean.enterpriseName=:enterpriseName ");
		finder.setParam("enterpriseName", enterpriseName);
		List<RevenueFlow> flows = find(finder);
		return flows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RevenueFlow> selectRevenueFlowsByEnterpriseId(Integer enterpriseId) throws Exception {
		Finder finder = Finder.create("from RevenueFlow bean where bean.enterpriseId=:enterpriseId ");
		finder.setParam("enterpriseId", enterpriseId);
		List<RevenueFlow> flows = find(finder);
		return flows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RevenueFlow> listRevenueFlowByParams(Long[] revenueFlowIds, Integer enterpriseId, String enterpriseName,
			Integer settlementStatus, Date startTime, Date endTime) {
		Finder finder = Finder.create("from RevenueFlow bean where 1=1");
		if (null != revenueFlowIds && revenueFlowIds.length > 0) {
			finder.append(" and bean.flowId in(:revenueFlowIds)");
			finder.setParamList("revenueFlowIds", revenueFlowIds);
		}
		if (StringUtils.isNotBlank(enterpriseName)) {
			finder.append(" and bean.enterpriseName = :enterpriseName");
			finder.setParam("enterpriseName", enterpriseName);
		}
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != settlementStatus) {
			finder.append(" and bean.settlementStatus = :settlementStatus");
			finder.setParam("settlementStatus", settlementStatus);
		}
		if (null != startTime) {
			finder.append(" and bean.flowTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.flowTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by flowTime desc");
		List<RevenueFlow> flows = find(finder);
		return flows;
	}

	@Override
	public Pagination pageByParams(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime, int pageNo,
			int pageSize) {
		Finder finder = createFinder(settlementStatus, flowType, flagId, enterpriseId, enterpriseName, userId, userName,
				startTime, endTime);
		Pagination pagination = find(finder, pageNo, pageSize);
		return pagination;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RevenueFlow> listByParams(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime) {
		Finder finder = createFinder(settlementStatus, flowType, flagId, enterpriseId, enterpriseName, userId, userName,
				startTime, endTime);
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double sumUnsettlementAccountByEnterprise(Integer enterpriseId) {
		Finder finder = Finder.create("select sum(bean.flowPrice) from RevenueFlow bean");
		finder.append(" where 1=1 ");
		finder.append(" and bean.settlementStatus=:settlementStatus ");
		finder.setParam("settlementStatus", RevenueFlow.SETTLEMENT_STATUS_NO);
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		finder.setMaxResults(1);
		List<Double> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return 0.0;
	}
	
	private Finder createFinder(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime) {
		Finder finder = Finder.create("from RevenueFlow bean");
		finder.append(" where 1=1 ");
		if (settlementStatus != null && settlementStatus != 0) {
			finder.append(" and bean.settlementStatus=:settlementStatus ");
			finder.setParam("settlementStatus", settlementStatus);
		}
		if (flowType != null && flowType != 0) {
			finder.append(" and bean.flowType=:flowType ");
			finder.setParam("flowType", flowType);
		}
		if (enterpriseId != null || StringUtils.isNotBlank(enterpriseName)) {
			finder.append(" and ( 1=-1");
			if (null != enterpriseId) {
				finder.append(" or bean.enterpriseId = :enterpriseId");
				finder.setParam("enterpriseId", enterpriseId);
			}
			if (StringUtils.isNotBlank(enterpriseName)) {
				finder.append(" or bean.enterpriseName like :enterpriseName");
				finder.setParam("enterpriseName", "%" + enterpriseName + "%");
			}
			finder.append(" ) ");
		}
		if (userId != null || StringUtils.isNotBlank(userName)) {
			finder.append(" and ( 1=-1");
			if (null != userId) {
				finder.append(" or bean.userId = :userId");
				finder.setParam("userId", userId);
			}
			if (StringUtils.isNotBlank(userName)) {
				finder.append(" or bean.userName like :userName");
				finder.setParam("userName", "%" + userName + "%");
			}
			finder.append(" ) ");
		}
		if (startTime != null) {
			finder.append(" and bean.flowTime>:startTime");
			finder.setParam("endTime", endTime);
		}
		if (endTime != null) {
			finder.append(" and bean.flowTime<:endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.flowTime desc ");
		return finder;
	}
}
