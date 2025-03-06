package com.bvRadio.iLive.manager.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.manager.dao.InstationMessageDao;
import com.bvRadio.iLive.manager.entity.InstationMessage;
import com.bvRadio.iLive.manager.entity.InstationMessageUser;
import com.bvRadio.iLive.manager.manager.InstationMessageService;
import com.bvRadio.iLive.manager.manager.InstationMessageUserService;

@Service
@Transactional
public class InstationMessageServiceImpl implements InstationMessageService {

	@Autowired
	private InstationMessageDao instationMessageDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;
	
	@Autowired
	private InstationMessageUserService instationMessageUserService;
	
	@Override
	public Long save(InstationMessage instationMessage) {
		Long nextId = fieldIdMng.getNextId("instation_message", "id", 1);
		instationMessage.setId(nextId);
		instationMessage.setIsDel(0);
		instationMessage.setCreateTime(new Timestamp(new Date().getTime()));
		instationMessageDao.save(instationMessage);
		return nextId;
	}

	@Override
	public Pagination getPage(Long id, Long userId, Timestamp startTime, Timestamp endTime, Integer pageNo,
			Integer pageSize) {
		return instationMessageDao.getPage(id, userId, startTime, endTime, pageNo, pageSize);
	}

	@Override
	public void update(InstationMessage instationMessage) {
		instationMessageDao.update(instationMessage);
	}

	@Override
	public void delete(Long id) {
		instationMessageDao.delete(id);
	}

	@Override
	public InstationMessage getById(Long id) {
		InstationMessage msg = instationMessageDao.getById(id);
		List<InstationMessageUser> list = instationMessageUserService.getByMsgId(msg.getId());
		msg.setList(list);
		return msg;
	}

}
