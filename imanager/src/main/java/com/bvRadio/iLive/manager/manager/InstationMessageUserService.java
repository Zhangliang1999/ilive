package com.bvRadio.iLive.manager.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.entity.InstationMessageUser;

public interface InstationMessageUserService {

	Long save(InstationMessageUser instationMessageUser);
	
	void update(InstationMessageUser instationMessageUser);
	
	InstationMessageUser getById(Long id);
	
	List<InstationMessageUser> getByMsgId(Long msgId);
	
	Pagination getPage(Long mesId,Integer pageNo,Integer pageSize);
	
	Pagination getPageByUserId(Long userId,Integer pageNo,Integer pageSize);
}
