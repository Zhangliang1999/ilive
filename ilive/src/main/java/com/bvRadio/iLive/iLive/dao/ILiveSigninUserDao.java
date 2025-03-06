package com.bvRadio.iLive.iLive.dao;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveSigninUser;

public interface ILiveSigninUserDao {

	Pagination getPage(String signId,String name, String roomName, Date startTime, Date endTime, Integer pageNo, Integer pageSize);
	
	void save(ILiveSigninUser ILiveSigninUser);
	
	void update(ILiveSigninUser ILiveSigninUsero);
	
	void delete(Long id);
	
	ILiveSigninUser getById(Long id);

	Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId);
	
	Pagination getDocByEnterpriseId(Integer enterpriseId, Integer pageNo, Integer pageSize);
	
	ILiveSigninUser getListByEnterpriseId(Long signId,String userPhone);
	List<ILiveSigninUser> getList(String signId, String name, String roomName, Date startTime, Date endTime);
}
