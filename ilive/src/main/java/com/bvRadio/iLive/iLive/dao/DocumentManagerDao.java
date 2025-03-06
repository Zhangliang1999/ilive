package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.DocumentManager;

public interface DocumentManagerDao {

	Pagination getPage(String name,Integer enterpriseId,Integer pageNo,Integer pageSize);
	
	void save(DocumentManager documentManager);
	
	void update(DocumentManager documentManagero);
	
	void delete(Long id);
	
	DocumentManager getById(Long id);

	Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId);
	
	Pagination getDocByEnterpriseId(Integer enterpriseId, Integer pageNo, Integer pageSize);
	
	List<DocumentManager> getListByEnterpriseId(Integer enterpriseId);
}
