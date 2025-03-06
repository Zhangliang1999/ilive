package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.CommissionBean;

/**
 * 佣金等级数据连接层管理
 * @author YanXL
 *
 */
public interface CommissionDao {
	/**
	 *  分页查询
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @return
	 */
	public Pagination selectCommissionPagination(Integer pageNo,Integer pageSize) throws Exception;
	/**
	 * 添加佣金级别管理
	 * @param commissionBean 佣金级别信息
	 * @throws Exception
	 */
	public void insertCommissionBean(CommissionBean commissionBean) throws Exception;
	/**
	 * 查询最大金额的数值
	 * @return
	 * @throws Exception
	 */
	public List<CommissionBean> selectCommissionBeanByMax() throws Exception;
	/**
	 * 根据主键ID查询信息
	 * @param commission_id 主键ID
	 * @return
	 * @throws Exception
	 */
	public CommissionBean selectCommissionBeanByCommissionId(Integer commission_id) throws Exception;
	/**
	 * 根据id删除
	 * @param commission_id 主键id
	 * @throws Exception
	 */
	public void deleteCommissionBean(CommissionBean commissionBean) throws Exception;
	/**
	 * 查询所有数据
	 * @return
	 * @throws Exception
	 */
	public List<CommissionBean> selectCommissionBeanAll() throws Exception;
}
