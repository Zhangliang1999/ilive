package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveFileDoc;

public interface ILiveFileDocMng {

	void save(ILiveFileDoc iLiveFileDoc);
	
	void update(ILiveFileDoc iLiveFileDoc);
	ILiveFileDoc getById(Long fileId);

	List<ILiveFileDoc> getListById(Long fileId);

	void delete(Long fileDocId);

	List<ILiveFileDoc> getListByDocId(Long docId);

	ILiveFileDoc getByFileIdDocId(Long fileId,Long docId);
}
