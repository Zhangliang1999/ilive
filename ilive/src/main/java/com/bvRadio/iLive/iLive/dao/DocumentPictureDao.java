package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;

public interface DocumentPictureDao {

	void save(DocumentPicture documentPicture);
	
	void delete(Long id);
	
	List<DocumentPicture> getListByDocId(Long docId,Integer pageSize,Integer pageNo);
	
	Pagination getPage(Long docId, Integer pageNo, Integer pageSize);
	
	List<DocumentPicture> getListByDocId(Long docId);
}
