package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.PresentRecordBean;
import com.bvRadio.iLive.iLive.entity.UserBean;

/**
 * 提现 事务控制
 * @author YanXL
 *
 */
public interface PresentRecordMng {
	
	/**
	 * 分页查询数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param keyword 关键值（用户ID、订单号）
	 * @param present_type  提现状态
	 * @return
	 */
	public Pagination getPage(Integer pageNo,Integer pageSize, Integer present_type, Integer keyword);
	/**
	 * 根据ID查询信息
	 * @param present_id 主键ID
	 * @return
	 */
	public PresentRecordBean findPresentRecordByPresentId(long present_id);
	/**
	 * 修改提现信息
	 * @param present_type 提现结果
	 * @param present_id 主键ID
	 * @return
	 */
	public String updatePresentRecordBeanBypresent_type(Integer present_type,
			long present_id);
	/**
	 * 个人分页查询数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param userBean 登录用户
	 * @param present_type  提现状态
	 * @return
	 */
	public Pagination getPersonalPage(Integer pageNo, Integer pageSize, Integer present_type,
			UserBean userBean);
	/**
	 * 添加提现
	 * @param presentRecordBean
	 * @return
	 */
	public String addPresentRecordBean(PresentRecordBean presentRecordBean);
	
}
