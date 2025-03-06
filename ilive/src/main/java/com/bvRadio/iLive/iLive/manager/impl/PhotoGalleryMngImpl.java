package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.PhotoGalleryDao;
import com.bvRadio.iLive.iLive.entity.PhotoGallery;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.PhotoGalleryMng;

@Service
@Transactional
public class PhotoGalleryMngImpl implements PhotoGalleryMng {

	@Autowired
	private PhotoGalleryDao photoGalleryDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public Pagination getPage(Integer enterpriseId,Integer pageNo, Integer pageSize) {
		return photoGalleryDao.getPage(enterpriseId,pageNo, pageSize);
	}

	@Override
	public void save(PhotoGallery photoGallery) {
		Long nextId = fieldIdMng.getNextId("photo_gallery", "id", 1);
		photoGallery.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		photoGallery.setCreateTime(now);
		photoGallery.setUpdateTime(now);
		photoGalleryDao.save(photoGallery);
	}

	@Override
	public void update(PhotoGallery photoGallery) {
		photoGalleryDao.update(photoGallery);
	}

	@Override
	public void delete(Long id) {
		photoGalleryDao.delete(id);
	}

}
