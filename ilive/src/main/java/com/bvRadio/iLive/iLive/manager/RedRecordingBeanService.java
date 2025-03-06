package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.UserBean;
/**
 * 红包 管理 事务
 * @author YanXL
 *
 */
public interface RedRecordingBeanService {
	/**
	 * 分页红包数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据
	 * @return
	 * @throws Exception
	 */
	public Pagination selectRedRecordingByPage(Integer pageNo,Integer pageSize);
	/**
	 * 根据用户获得用户送和收礼物数据
	 * @param cpn 页码
	 * @param i 每页数据条数
	 * @param userBean 用户
	 * @return
	 */
	public Pagination selectRedRecordingByPageUserBean(int cpn, int i,
			UserBean userBean);
}
