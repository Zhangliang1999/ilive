package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.RechargeRecordBean;
import com.bvRadio.iLive.iLive.entity.UserBean;

/**
 * 充值记录 DAO
 * @author YanXL
 *
 */
public interface RechargeRecordDao {
	/**
	 * 分页获取充值记录
	 * @param pageNo 页码
	 * @param pageSize 每页显示的数据条数
	 * @param keyword 关键数（用户ID、商户订单号）
	 * @param payment_type  支付类型值
	 * @param payment_status 支付状态值
	 * @return 
	 * @throws Exception
	 */
	public Pagination getPage(int pageNo, int pageSize, Integer payment_status, Integer payment_type, Integer keyword) throws Exception;
	/**
	 * 个人分页获取充值记录
	 * @param pageNo 页码
	 * @param pageSize 每页显示的数据条数
	 * @param userBean 登录用户信息
	 * @param payment_status 支付状态
	 * @return 
	 * @throws Exception
	 */
	public Pagination getPersonalPage(int pageNo, int pageSize,UserBean userBean, Integer payment_status) throws Exception;
	
	/**
	 * 添加充值记录
	 * @param recordBean 充值信息
	 * @throws Exception
	 */
	public RechargeRecordBean insertRechargeRecordBean(RechargeRecordBean recordBean) throws Exception;
	/**
	 * 根据信息查询充值信息
	 * @param paymentType 支付类型ID
	 * @param out_trade_no 商户订单号
	 * @return 
	 * @throws Exception
	 */
	public RechargeRecordBean getRechargeRecordBeanByParam(Integer paymentType,String out_trade_no) throws Exception;
	/**
	 * 修改数据信息
	 * @param rechargeRecordBean 修改数据
	 * @throws Exception
	 */
	public void updateRechargeRecordBean(RechargeRecordBean rechargeRecordBean) throws Exception;
	/**
	 * 根据用户ID和支付状态查询信息
	 * @param user_id 用户ID
	 * @param paymentStatusYes 支付状态
	 * @return
	 */
	public List<RechargeRecordBean> selectRechargeRecordByPAYMENT_STATUS_YES(Integer user_id,
			Integer paymentStatusYes) throws Exception;
}
