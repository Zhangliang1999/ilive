package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.RechargeRulesBean;

/**
 * 充值规则
 * @author YanXL
 *
 */
public interface RechargeRulesDao {
	/**
	 * 分页查询数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param queryRmb 人民币
	 * @return
	 * @throws Exception
	 */
	public Pagination selectPaginationByPage(Integer pageNo,Integer pageSize, Integer queryRmb) throws Exception;
	/**
	 * 查询所有策略
	 * @return
	 * @throws Exception
	 */
	public List<RechargeRulesBean> selectRechargeRulesBeans() throws Exception;
	/**
	 * 获取拥有最大价格的数据
	 * @return
	 * @throws Exception
	 */
	public List<RechargeRulesBean> selectRechargeRulesBeanByRmbMax() throws Exception;
	/**
	 * 添加规则
	 * @param bean
	 * @throws Exception
	 */
	public RechargeRulesBean insertRechargeRulesBean(RechargeRulesBean bean) throws Exception;
	/**
	 * 删除数据
	 * @param rechargeRulesBean
	 */
	public void deleteRechargeRule(RechargeRulesBean rechargeRulesBean) throws Exception;
	/**
	 * 根据主键ID查询数据信息
	 * @param rules_id 主键
	 * @return
	 */
	public RechargeRulesBean selectRechargeRulesBeanByID(Integer rules_id) throws Exception;
	/**
	 * 修改数据
	 * @param bean
	 * @throws Exception
	 */
	public void updateRechargeRule(RechargeRulesBean bean) throws Exception;

}
