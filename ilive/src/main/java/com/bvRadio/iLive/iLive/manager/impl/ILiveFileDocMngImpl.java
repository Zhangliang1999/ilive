package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveFileDocDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileDoc;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileDocMng;

@Transactional
public class ILiveFileDocMngImpl implements ILiveFileDocMng {

	@Autowired
	private ILiveFileDocDao iLiveFileDocDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public void save(ILiveFileDoc iLiveFileDoc) {
		Long nextId = fieldIdMng.getNextId("ilive_file_doc", "id", 1);
		iLiveFileDoc.setId(nextId);
		System.out.println("保存id-------------------------"+nextId);
		iLiveFileDocDao.save(iLiveFileDoc);
	}

	@Override
	public void update(ILiveFileDoc iLiveFileDoc) {
		iLiveFileDocDao.update(iLiveFileDoc);
	}

	@Override
	public ILiveFileDoc getById(Long fileId) {
		
		return iLiveFileDocDao.getById(fileId);
	}

	@Override
	public List<ILiveFileDoc> getListById(Long fileId) {
		
		return iLiveFileDocDao.getListById(fileId);
	}

	@Override
	public void delete(Long fileDocId) {
		iLiveFileDocDao.delete(fileDocId);
	}

	@Override
	public List<ILiveFileDoc> getListByDocId(Long docId) {
		
		return iLiveFileDocDao.getListByDocId(docId);
	}

	@Override
	public ILiveFileDoc getByFileIdDocId(Long fileId, Long docId) {
		return iLiveFileDocDao.getByFileIdDocId(fileId, docId);
	}

	

}
