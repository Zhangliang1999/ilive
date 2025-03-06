package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.PresentRecordBean;
import com.bvRadio.iLive.iLive.entity.UserBean;

/**
 * 提现 数据连接层
 * @author YanXL
 *
 */
public interface PresentRecordDao {
	/**
	 * 提现数据
	 * @param pageNo 页面
	 * @param pageSize 每页数据条数
	 * @param keyword 关键值（用户ID、订单号）
	 * @param present_type  提现状态 
	 * @return
	 * @throws Exception
	 */
	public Pagination getPagination(int pageNo, int pageSize, Integer present_type, Integer keyword) throws Exception;
	/**
	 * 个人提现数据记录
	 * @param pageNo 页面
	 * @param pageSize 每页数据条数
	 * @param present_type  提现状态 
	 * @param userBean 登录用户
	 * @return
	 * @throws Exception
	 */
	public Pagination getPersonalPagination(int pageNo, int pageSize, Integer present_type,UserBean userBean) throws Exception;
	
	/**
	 * 添加提现记录
	 * @param presentRecordBean 提现信息
	 * @throws Exception
	 */
	public PresentRecordBean insertRechargeRecordBean(PresentRecordBean presentRecordBean) throws Exception;
	/**
	 * 根据主键id查询信息
	 * @param present_id 主键ID
	 * @return
	 */
	public PresentRecordBean selectPresentRecordByPresentId(long present_id) throws Exception;
	/**
	 * 修改提现信息
	 * @param presentRecordBean
	 */
	public void updatePresentRecord(PresentRecordBean presentRecordBean);
}
