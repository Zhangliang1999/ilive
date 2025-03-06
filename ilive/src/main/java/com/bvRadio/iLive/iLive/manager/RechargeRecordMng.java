package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.UserBean;

/**
 * 充值记录 事务
 * @author YanXL
 *
 */
public interface RechargeRecordMng {
	/**
	 * 分页查询数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param keyword  关键数（用户ID、商户订单号）
	 * @param payment_type  充值类型值
	 * @param payment_status 支付状态值
	 * @return
	 */
	public Pagination getPage(Integer pageNo,Integer pageSize, Integer payment_status, Integer payment_type, Integer keyword);
	/**
	 * 个人分页查询数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param userBean  登录用户
	 * @param payment_status 支付状态
	 * @return
	 */
	public Pagination getPersonalPage(Integer pageNo, Integer pageSize, UserBean userBean, Integer payment_status);
}
