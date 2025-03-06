package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.UserBalancesBean;

/**
 * 用户财务信息
 * @author YanXL
 *
 */
public interface UserBalancesDao {
	/**
	 * 用户财务分页信息
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param userName 用户名称
	 * @return 数据
	 * @throws Exception
	 */
	public Pagination selectPaginationBypage(Integer pageNo,Integer pageSize,String userName) throws Exception;
	/**
	 * 根据主键ID查询用户信息
	 * @param user_id 主键ID
	 * @return
	 * @throws Exception
	 */
	public UserBalancesBean selectUserBalancesBean(Integer user_id) throws Exception;
	/**
	 * 添加用户账户信息
	 * @param userBalancesBean 账户信息
	 * @throws Exception
	 */
	public void insertUserBalancesBean(UserBalancesBean userBalancesBean) throws Exception;
	/**
	 * 修改账户信息
	 * @param userBalancesBean 账户信息
	 * @throws Exception
	 */
	public void updateUserBalancesBean(UserBalancesBean userBalancesBean) throws Exception;
	/**
	 * 查询所有用户
	 * @return
	 * @throws Exception
	 */
	public List<UserBalancesBean> selectUserBalancesBeanAll() throws Exception;
}
