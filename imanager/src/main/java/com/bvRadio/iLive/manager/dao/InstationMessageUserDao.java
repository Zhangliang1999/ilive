package com.bvRadio.iLive.manager.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.entity.InstationMessageUser;

public interface InstationMessageUserDao {

	void save(InstationMessageUser instationMessageUser);
	
	void update(InstationMessageUser instationMessageUser);
	
	InstationMessageUser getById(Long id);
	
	List<InstationMessageUser> getByMsgId(Long msgId);
	
	Pagination getPage(Long mesId,Integer pageNo,Integer pageSize);
	
	Pagination getPageByUserId(Long userId,Integer pageNo,Integer pageSize);
}
