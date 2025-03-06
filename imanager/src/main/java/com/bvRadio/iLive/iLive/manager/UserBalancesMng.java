package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.UserBalancesBean;

/**
 * 用户账户 事务层
 * @author Administrator
 *
 */
public interface UserBalancesMng {
	/**
	 * 用户账户信息
	 * @param pageNo 页码
	 * @param pageSize 每页数据数
	 * @param userName 用户名称
	 * @return
	 */
	public Pagination selectPaginationBypage(Integer pageNo, Integer pageSize,String userName);
	/**
	 * 根据主键ID查询信息
	 * @param user_id 主键
	 * @return
	 */
	public UserBalancesBean selectUserBalanceByUserId(Integer user_id);
	/**
	 * 佣金计算处理
	 */
	public void selectUserBalanceAllCommission();
	/**
	 * 添加用户账户
	 * @param userBalancesBean 
	 */
	public void addUserBalancesBean(UserBalancesBean userBalancesBean);
}
