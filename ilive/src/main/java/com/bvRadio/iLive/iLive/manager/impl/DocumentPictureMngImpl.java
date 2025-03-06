package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.DocumentPictureDao;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class DocumentPictureMngImpl implements DocumentPictureMng {

	@Autowired
	private DocumentPictureDao documentPictureDao;	//文档图片
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public Long save(DocumentPicture documentPicture) {
		Long nextId = fieldIdMng.getNextId("document_picture", "id", 1);
		documentPicture.setId(nextId);
		documentPicture.setCreateTime(new Timestamp(new Date().getTime()));
		documentPictureDao.save(documentPicture);
		return nextId;
	}

	@Override
	public void delete(Long id) {
		documentPictureDao.delete(id);
	}

	@Override
	public List<DocumentPicture> getListByDocId(Long docId,Integer pageSize,Integer pageNo) {
		return documentPictureDao.getListByDocId(docId,pageSize,pageNo);
	}

	@Override
	public Pagination getPage(Long docId, Integer pageNo, Integer pageSize) {
		return documentPictureDao.getPage(docId,pageNo,pageSize);
	}

	@Override
	public List<DocumentPicture> getListByDocId(Long docId) {
		return documentPictureDao.getListByDocId(docId);
	}

}
