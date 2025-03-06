package com.jwzt.billing.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.RevenueFlow;
import com.jwzt.billing.exception.FlowNotFoundException;
import com.jwzt.billing.exception.FlowSettlementStatusErrorException;
import com.jwzt.billing.exception.NotSameEnterpriseException;
import com.jwzt.common.page.Pagination;

/**
 * 流水收益  
 * @author YanXL
 *
 */
public interface RevenueFlowManager {
	/**
	 * 新增收益流水数据
	 * @param revenueFlow
	 * @throws Exception
	 */
	public void addRevenueFlow(RevenueFlow revenueFlow) throws Exception;
	/**
	 * 获取收益记录
	 * @param pageNo 页码
	 * @param settlementStatus 结算状态
	 * @param flowType 收益类型
	 * @param enterpriseContent 企业信息
	 * @param userContent 用户信息
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws Exception
	 */
	public Pagination selectRevenueFlowByPage(Integer pageNo,
			Integer settlementStatus, Integer flowType,
			String enterpriseContent, String userContent, Date startTime,
			Date endTime) throws Exception;
	/**
	 * 根据结算状态获取数据
	 * @param settlementStatus 结算状态
	 * @return
	 * @throws Exception
	 */
	public List<RevenueFlow> selectRevenueFlowBySettlementStatus(
			Integer settlementStatus) throws Exception;

	/**
	 * 修改流水结算状态
	 * 
	 * @param flowId
	 *            收益流水ID
	 * @param settlementStatus
	 *            结算类型
	 */
	public void updateRevenueFlow(Long flowId, Integer settlementStatus) throws Exception;
	/**
	 * 修改流水结算状态
	 * @param flowIds 流水主键数组
	 * @param settlementStatus 结算状态
	 * @throws Exception
	 */
	public void updateRevenueFlow(Long[] flowIds,Integer settlementStatus) throws Exception;
	/**
	 * 根据主键ID获取数据
	 * @param flowId 流水ID
	 * @return
	 * @throws Exception
	 */
	public RevenueFlow selectRevenueFlowByFlowId(Long flowId) throws Exception;
	/**
	 * 获取收益流水
	 * @param pageNo 页码
	 * @param userRoom 用户或直播间ID
	 * @param flowType 收益类型
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public Pagination selectRevenueFlowByPage(Integer pageNo,
			String userRoom, Integer flowType, Date startDate, Date endDate) throws Exception;
	/**
	 * 根据企业名称  获取企业流水
	 * @param enterpriseName  名称
	 * @return
	 * @throws Exception
	 */
	public List<RevenueFlow> selectRevenueFlowsByEnterprise(String enterpriseName) throws Exception;
	/**
	 * 根据企业ID 获取企业流水
	 * @param enterpriseId ID
	 * @return
	 * @throws Exception
	 */
	public List<RevenueFlow> selectRevenueFlowsByEnterpriseId(Integer enterpriseId) throws Exception;
	
	/**
	 * 根据流水ID列表，获取结算用的流水清单
	 * 
	 * @param revenueFlowIds
	 * @param enterpriseName
	 * @param settlementStatus
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws FlowNotFoundException
	 *             流水不存在异常
	 * @throws FlowSettlementStatusErrorException
	 *             流水已结算异常
	 * @throws Exception
	 */
	public List<RevenueFlow> listRevenueFlowForSettlement(Integer enterpriseId,
			Integer settlementStatus, Date startTime, Date endTime)
			throws Exception;

	public List<RevenueFlow> listRevenueFlowForSettlementByIds(Integer enterpriseId, Long[] revenueFlowIds)
			throws FlowNotFoundException, FlowSettlementStatusErrorException, NotSameEnterpriseException, Exception;

	public List<RevenueFlow> listRevenueFlowByParams(Long[] revenueFlowIds, Integer enterpriseId, String enterpriseName,
			Integer settlementStatus, Date startTime, Date endTime) throws Exception;
	
	public Pagination pageByParams(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime, int pageNo,
			int pageSize);

	public List<RevenueFlow> listByParams(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime);

	/**
	 * 获取企业未结算金额
	 * 
	 * @param enterpriseId
	 * @return
	 */
	public Double sumUnsettlementAccountByEnterprise(Integer enterpriseId);
}
