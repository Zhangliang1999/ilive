package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseTerminalUser;

public interface ILiveEnterpriseTerminalUserMng {

	/*
	 * 获取终端用户分页数据
	 */
	public Pagination getPage(String queryNum, Integer fanstype,Integer pageNo, Integer pageSize, Integer enterpriseId);
	
	/*
	 * 获取终端用户黑名单分页数据
	 */
	public Pagination getPageBlackList(Integer enterpriseId, String queryNum, Integer pageNo, Integer pageSize);
	
	/*
	 * 保存终端用户记录
	 */
	public ILiveEnterpriseTerminalUser saveTerminaluser(ILiveEnterpriseTerminalUser user);
	
	/*
	 * 根据id拉黑终端用户
	 */
	public void letbuserblack(Long id);
	
	/*
	 * 根据id删除终端用户
	 */
	public void removeTerminaluser(Long id);
	
	
	public ILiveEnterpriseTerminalUser getTerminalUser(Long userId,Integer enterpriseId);

	public void updateTerminalUser(ILiveEnterpriseTerminalUser terminalUser);
	List<ILiveEnterpriseTerminalUser> queryList(String queryNum,Integer fanstype);
	List<ILiveEnterpriseTerminalUser> queryList(String queryNum,Integer fanstype,Integer enterpriseId);

	public void removeBlackuser(Long id);
}
