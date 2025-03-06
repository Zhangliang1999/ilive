package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;

public interface DocumentPictureMng {

	Long save(DocumentPicture documentPicture);
	
	void delete(Long id);
	
	List<DocumentPicture> getListByDocId(Long docId,Integer pageSize,Integer pageNo);
	
	List<DocumentPicture> getListByDocId(Long docId);
	
	Pagination getPage(Long docId,Integer pageNo,Integer pageSize);
}
