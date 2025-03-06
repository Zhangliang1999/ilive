package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.RechargeRulesBean;

/**
 * 充值规则
 * @author YanXL
 *
 */
public interface RechargeRulesMng {
	/**
	 * 分页查询列表数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param queryRmb 人民币
	 * @return
	 */
	public Pagination getPaginationByPage(Integer pageNo,Integer pageSize, Integer queryRmb);
	/**
	 * 获取数据库中数据最高价格
	 * @return
	 */
	public Integer getRechargeRuleMaxRmb();
	/**
	 * 添加规则
	 * @param bean 规则信息
	 */
	public void saveRechargeRuleBean(RechargeRulesBean bean);
	/**
	 * 根据主键ID删除
	 * @param rules_id
	 * @return
	 */
	public String deleteRechargeRule(Integer rules_id);
	/**
	 * 批量删除规则
	 * @param rules_ids
	 * @return
	 */
	public String deleteRechargeRuleAll(String rules_ids);
	/**
	 * 根据ID查询数据
	 * @param rules_id 主键ID
	 * @return
	 */
	public RechargeRulesBean findRechargeRulesBeanByID(Integer rules_id);
	/**
	 * 修改规则数据
	 * @param bean
	 */
	public void updateRechargeRule(RechargeRulesBean bean);
	
	
}
