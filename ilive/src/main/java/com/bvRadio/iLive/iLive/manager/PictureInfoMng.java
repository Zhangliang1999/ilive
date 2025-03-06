package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.PictureInfo;

public interface PictureInfoMng {

	Pagination getPage(String name,Integer enterpriseId,Integer pageNo,Integer pageSize);
	
	Long save(PictureInfo pictureInfo);
	
	void update(PictureInfo pictureInfo);
	
	void delete(Long id);
	
	PictureInfo getById(Long id);

	Pagination getCollaborativePage(String name,Integer pageNo,Integer pageSize, Long userId);

	List<PictureInfo> getListByEnterpriseId(Integer enterpriseId);
}
