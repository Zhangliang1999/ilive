package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveSignin;

public interface ILiveSigninMng {

	Pagination getPage(String userId,String name,Integer roomName,Integer status,Integer pageNo,Integer pageSize);
	Pagination getPageByEnterpriseId(String userId,String name,Integer enterpriseId,Integer status,Integer pageNo,Integer pageSize);
	
	Long save(ILiveSignin ILiveSignin);
	
	void update(ILiveSignin ILiveSignin);
	
	void delete(Long id);
	
	ILiveSignin getById(Long id);
	
	boolean saveDoc(ILiveSignin ILiveSignin,String str);
	/**
	 * 获取子用户 内容
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	Pagination getCollaborativePage(String name, Integer pageNo,Integer pageSize, Long userId);
	
	Pagination getDocByEnterpriseId(Integer enterpriseId,Integer pageNo,Integer pageSize);
	
	List<ILiveSignin> getListByEnterpriseId(Integer enterpriseId);
	List<ILiveSignin> getByEnterpriseId(Integer enterpriseId);

	ILiveSignin getIsStart(Integer roomId);
	ILiveSignin getIsStart2(Integer enterpriseId);
	Pagination getPageByUserId(String name, String userId, Integer state,Integer pageNo,Integer pageSize);
}
