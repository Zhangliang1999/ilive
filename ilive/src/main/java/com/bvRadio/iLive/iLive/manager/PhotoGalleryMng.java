package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.PhotoGallery;

public interface PhotoGalleryMng {

	Pagination getPage(Integer enterpriseId,Integer pageNo,Integer pageSize);
	
	void save(PhotoGallery photoGallery);
	
	void update(PhotoGallery photoGallery);
	
	void delete(Long id);
}
