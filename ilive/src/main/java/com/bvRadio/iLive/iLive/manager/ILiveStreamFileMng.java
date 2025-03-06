package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveStreamFile;

public interface ILiveStreamFileMng {

	Pagination getPage(String name,Integer enterpriseId,Integer pageNo,Integer pageSize);
	
	Long save(ILiveStreamFile iLiveStreamFile);
	
	void update(ILiveStreamFile iLiveStreamFile);
	
	void delete(Long id);
	
	ILiveStreamFile getById(Long id);

	Pagination getCollaborativePage(String name,Integer pageNo,Integer pageSize, Long userId);
	
	List<ILiveStreamFile> getListByUserId(Integer enterpriseId);
}
