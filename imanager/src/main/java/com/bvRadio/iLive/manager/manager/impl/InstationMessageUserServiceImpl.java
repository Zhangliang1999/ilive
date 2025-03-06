package com.bvRadio.iLive.manager.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.manager.dao.InstationMessageUserDao;
import com.bvRadio.iLive.manager.entity.InstationMessageUser;
import com.bvRadio.iLive.manager.manager.InstationMessageUserService;

@Service
@Transactional
public class InstationMessageUserServiceImpl implements InstationMessageUserService{

	@Autowired
	private InstationMessageUserDao instationMessageUserDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;
	
	@Override
	public Long save(InstationMessageUser instationMessageUser) {
		Long nextId = fieldIdMng.getNextId("instation_message_user", "id", 1);
		instationMessageUser.setId(nextId);
		instationMessageUserDao.save(instationMessageUser);
		return nextId;
	}

	@Override
	public void update(InstationMessageUser instationMessageUser) {
		instationMessageUserDao.update(instationMessageUser);
	}

	@Override
	public InstationMessageUser getById(Long id) {
		return instationMessageUserDao.getById(id);
	}

	@Override
	public List<InstationMessageUser> getByMsgId(Long msgId) {
		return instationMessageUserDao.getByMsgId(msgId);
	}

	@Override
	public Pagination getPage(Long mesId, Integer pageNo, Integer pageSize) {
		return instationMessageUserDao.getPage(mesId, pageNo, pageSize);
	}

	@Override
	public Pagination getPageByUserId(Long userId, Integer pageNo, Integer pageSize) {
		return instationMessageUserDao.getPageByUserId(userId, pageNo, pageSize);
	}

}
