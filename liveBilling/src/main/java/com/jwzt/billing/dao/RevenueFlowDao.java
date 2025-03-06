package com.jwzt.billing.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.RevenueFlow;
import com.jwzt.common.page.Pagination;

/**
 * 收益流水   
 * @author YanXL
 *
 */
public interface RevenueFlowDao {
	/**
	 * 新增数据
	 * @param RevenueFlow
	 */
	public void insertRevenueFlow(RevenueFlow revenueFlow) throws Exception;
	/**
	 * 分页数据
	 * @param pageNo 页码
	 * @param settlementStatus 结算状态
	 * @param flowType 收益类型
	 * @param enterpriseContent 企业信息
	 * @param userContent 用户信息
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Pagination selectRevenueFlowByPage(Integer pageNo,
			Integer settlementStatus, Integer flowType,
			String enterpriseContent, String userContent, Date startTime,
			Date endTime) throws Exception;
	/**
	 * 根据结算状态获取数据
	 * @param settlementStatus
	 * @return
	 */
	public List<RevenueFlow> selectRevenueFlowBySettlementStatus(
			Integer settlementStatus) throws Exception;
	/**
	 * 修改数据
	 * @param flowId 收益流水ID
	 * @param settlementStatus 结算类型
	 */
	public void updateRevenueFlow(Long flowId, Integer settlementStatus) throws Exception;
	/**
	 * 根据主键回去流水数据
	 * @param flowId 流水ID
	 * @return
	 * @throws Exception
	 */
	public RevenueFlow selectRevenueFlowByFlowId(Long flowId) throws Exception;
	/**
	 * 获取收益流水数据
	 * @param pageNo 页码
	 * @param userRoom 用户或直播间ID
	 * @param flowType 收益类型
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 * @throws Exception
	 */
	public Pagination selectRevenueFlowByPage(Integer pageNo,
			String userRoom, Integer flowType, Date startDate, Date endDate) throws Exception;
	/**
	 * 根据企业名称  获取企业流水
	 * @param enterpriseName  名称
	 * @return
	 * @throws Exception
	 */
	public List<RevenueFlow> selectRevenueFlowsByEnterprise(
			String enterpriseName) throws Exception;
	/**
	 * 根据企业ID 获取企业流水
	 * @param enterpriseId ID
	 * @return
	 * @throws Exception
	 */
	public List<RevenueFlow> selectRevenueFlowsByEnterpriseId(
			Integer enterpriseId) throws Exception;
	
	/**
	 * 获取企业流水列表，用户结算
	 * 
	 * @param revenueFlowIds
	 * @param enterpriseName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<RevenueFlow> listRevenueFlowByParams(Long[] revenueFlowIds, Integer enterpriseId, String enterpriseName,
			Integer settlementStatus, Date startTime, Date endTime);

	public Pagination pageByParams(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime, int pageNo,
			int pageSize);

	public List<RevenueFlow> listByParams(Integer settlementStatus, Integer flowType, Long flagId, Integer enterpriseId,
			String enterpriseName, Long userId, String userName, Date startTime, Date endTime);
	
	public Double sumUnsettlementAccountByEnterprise(Integer enterpriseId);
}
