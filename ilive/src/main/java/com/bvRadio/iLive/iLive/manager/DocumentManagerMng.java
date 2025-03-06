package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.DocumentManager;

public interface DocumentManagerMng {

	Pagination getPage(String name,Integer enterpriseId,Integer pageNo,Integer pageSize);
	
	Long save(DocumentManager documentManager);
	
	void update(DocumentManager documentManager);
	
	void delete(Long id);
	
	DocumentManager getById(Long id);
	
	boolean saveDoc(DocumentManager documentManager,String str);
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
	
	List<DocumentManager> getListByEnterpriseId(Integer enterpriseId);
	
	void batchDel(List<Long> list);
}
