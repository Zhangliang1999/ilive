package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseTerminalUser;

public interface ILiveEnterpriseTerminalUserDao {

	public Pagination getPage(String queryNum,Integer fanstype, Integer pageNo, Integer pageSize, Integer enterpriseId);
	
	public Pagination getPageBlackList(Integer enterpriseId, String queryNum, Integer pageNo, Integer pageSize);
	
	public ILiveEnterpriseTerminalUser saveTerminaluser(ILiveEnterpriseTerminalUser user);

	public ILiveEnterpriseTerminalUser getIliveEnterpriseTerminalByQuery(Long userId, Integer enterpriseId);

	public void updateIliveEnterpriseTerminalUser(ILiveEnterpriseTerminalUser terminalUser);
	
	public void letbuserblack(Long id);
	
	public void removeTerminaluser(Long id);
	public List<ILiveEnterpriseTerminalUser> queryList(String queryNum, Integer fanstype);
	public List<ILiveEnterpriseTerminalUser> queryList(String queryNum, Integer fanstype, Integer enterpriseId);

	public void removeBlackuser(Long id);
}
