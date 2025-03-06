package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveComment;

public interface ILiveCommentDao {

	
	
	void save(ILiveComment ILiveComment);
	
	void update(ILiveComment ILiveCommento);
	
	void delete(String id);
	
	ILiveComment getById(String id);

	
	ILiveComment getIsStart(Integer roomId);
}
