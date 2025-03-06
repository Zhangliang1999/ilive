package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveStreamFileDao;
import com.bvRadio.iLive.iLive.entity.ILiveStreamFile;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveStreamFileMng;

@Service
@Transactional
public class ILiveStreamFileMngImpl implements ILiveStreamFileMng {
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	@Autowired
	private ILiveStreamFileDao iLiveStreamFileDao;		//图片
	
	@Override
	public Pagination getPage(String name, Integer enterpriseId, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return iLiveStreamFileDao.getPage(name,enterpriseId, pageNo, pageSize);
	}

	@Override
	public Long save(ILiveStreamFile iLiveStreamFile) {
		// TODO Auto-generated method stub
		Long nextId = fieldIdMng.getNextId("ilive_stream_file", "id", 1);
		iLiveStreamFile.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveStreamFile.setCreateTime(now);
		iLiveStreamFile.setUpdateTime(now);
		iLiveStreamFileDao.save(iLiveStreamFile);
		return nextId;
	}

	@Override
	public void update(ILiveStreamFile iLiveStreamFile) {
		// TODO Auto-generated method stub
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveStreamFile.setUpdateTime(now);
		iLiveStreamFileDao.update(iLiveStreamFile);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		iLiveStreamFileDao.delete(id);
	}

	@Override
	public ILiveStreamFile getById(Long id) {
		// TODO Auto-generated method stub
		return iLiveStreamFileDao.getById(id);
	}

	@Override
	public Pagination getCollaborativePage(String name, Integer pageNo, Integer pageSize, Long userId) {
		// TODO Auto-generated method stub
		return iLiveStreamFileDao.getCollaborativePage(name, pageNo, pageSize,userId);
	}

	@Override
	public List<ILiveStreamFile> getListByUserId(Integer enterpriseId) {
		// TODO Auto-generated method stub
		return iLiveStreamFileDao.getListByUserId(enterpriseId);
	}
	
	
}
