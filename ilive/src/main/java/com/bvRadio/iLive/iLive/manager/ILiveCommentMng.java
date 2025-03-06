package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveComment;

public interface ILiveCommentMng {

	
	
	void save(ILiveComment ILiveComment);
	
	void update(ILiveComment ILiveComment);
	
	void delete(String id);
	
	ILiveComment getById(String id);
	

	ILiveComment getIsStart(Integer roomId);
}
