package com.bvRadio.iLive.iLive.manager;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveSigninUser;

public interface ILiveSigninUserMng {

	
	
	String save(ILiveSigninUser ILiveSigninUser);
	
	void update(ILiveSigninUser ILiveSigninUser);
	
	void delete(Long id);
	
	ILiveSigninUser getById(Long id);
	
	
	ILiveSigninUser getListByEnterpriseId(Long signId,String userPhone);

	Pagination getPage(String signId, String name, String roomName, Date startTime, Date endTime, Integer pageNo, Integer pageSize);

	List<ILiveSigninUser> getList(String signId, String name, String roomName, Date startTime, Date endTime);
}
