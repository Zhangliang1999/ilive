package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

public interface TerminalUserMng {
	public Pagination getPage(String queryNum,Integer pageNo,Integer pageSize);
	public ILiveManager getById(Long userId);
	public void delUser(Long userId);
	public void updateUser(ILiveManager user);
}
