package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.PhotoGallery;

public interface PhotoGalleryDao {

	Pagination getPage(Integer enterpriseId,Integer pageNo,Integer pageSize);
	
	void update(PhotoGallery photoGallery);
	
	void save(PhotoGallery photoGallery);
	
	void delete(Long id);
}
