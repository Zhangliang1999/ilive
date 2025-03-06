package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.AssistentLogin;

public interface AssistentLoginDao {
	/**
	 * 新增记录
	 * @param assistentLogin
	 */
	public void addAssistentLogin(AssistentLogin assistentLogin) throws Exception;
	/**
	 * 分页数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param roomId 
	 * @return
	 * @throws Exception
	 */
	public Pagination selectAssistentLoginByPage(Integer roomId,Integer pageNo,Integer pageSize) throws Exception;
}
