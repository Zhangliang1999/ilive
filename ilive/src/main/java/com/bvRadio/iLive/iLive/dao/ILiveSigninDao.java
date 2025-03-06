package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveSignin;

public interface ILiveSigninDao {

	Pagination getPage(String userId,String name,Integer roomName,Integer status,Integer pageNo,Integer pageSize);
	Pagination getPageByEnterpriseId(String userId,String name,Integer enterpriseId,Integer status,Integer pageNo,Integer pageSize);
	
	void save(ILiveSignin ILiveSignin);
	
	void update(ILiveSignin ILiveSignino);
	
	void delete(Long id);
	
	ILiveSignin getById(Long id);

	Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId);
	
	Pagination getDocByEnterpriseId(Integer enterpriseId, Integer pageNo, Integer pageSize);
	
	List<ILiveSignin> getListByEnterpriseId(Integer enterpriseId);
	ILiveSignin getIsStart(Integer roomId);
	ILiveSignin getIsStart2(Integer enterpriseId);
	
	List<ILiveSignin> getByEnterpriseId(Integer enterpriseId);
	Pagination getPageByUserId(String name, String userId, Integer state,Integer pageNo,Integer pageSize);
}
