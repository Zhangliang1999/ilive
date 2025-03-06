package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.PictureInfoDao;
import com.bvRadio.iLive.iLive.entity.PictureInfo;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.PictureInfoMng;

@Service
@Transactional
public class PictureInfoMngImpl implements PictureInfoMng {

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private PictureInfoDao pictureInfoDao;		//图片
	
	@Override
	public Pagination getPage(String name, Integer enterpriseId,Integer pageNo, Integer pageSize) {
		return pictureInfoDao.getPage(name,enterpriseId, pageNo, pageSize);
	}

	@Override
	public Long save(PictureInfo pictureInfo) {
		Long nextId = fieldIdMng.getNextId("picture_info", "id", 1);
		pictureInfo.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		pictureInfo.setCreateTime(now);
		pictureInfo.setUpdateTime(now);
		pictureInfoDao.save(pictureInfo);
		return nextId;
	}

	@Override
	public void update(PictureInfo pictureInfo) {
		pictureInfoDao.update(pictureInfo);
	}

	@Override
	public void delete(Long id) {
		pictureInfoDao.delete(id);
	}

	@Override
	public PictureInfo getById(Long id) {
		return pictureInfoDao.getById(id);
	}

	@Override
	public Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId) {
		return pictureInfoDao.getCollaborativePage(name, pageNo, pageSize,userId);
	}

	@Override
	public List<PictureInfo> getListByEnterpriseId(Integer enterpriseId) {
		// TODO Auto-generated method stub
		return pictureInfoDao.getListByEnterpriseId(enterpriseId);
	}

}
