package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.AssistentLogin;
/**
 * 协助助手管理员登录 记录 
 * @author YanXL
 *
 */
public interface AssistentLoginMng {
	/**
	 * 新增记录
	 * @param assistentLogin
	 */
	public void addAssistentLogin(AssistentLogin assistentLogin) throws Exception;
	/**
	 * 分页数据
	 * @param roomId 
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @return
	 * @throws Exception
	 */
	public Pagination selectAssistentLoginByPage(Integer roomId,Integer pageNo,Integer pageSize) throws Exception;

}
