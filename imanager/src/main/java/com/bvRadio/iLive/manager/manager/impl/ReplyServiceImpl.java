package com.bvRadio.iLive.manager.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.manager.dao.ReplyDao;
import com.bvRadio.iLive.manager.entity.Reply;
import com.bvRadio.iLive.manager.manager.ReplyService;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService{

	@Autowired
	private ReplyDao replyDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Override
	public Long save(Reply reply) {
		Long nextId = filedIdMng.getNextId("reply", "id", 1);
		Timestamp now = new Timestamp(new Date().getTime());
		reply.setIsDel(0);
		reply.setCreateTime(now);
		replyDao.save(reply);
		return nextId;
	}

	@Override
	public void deleteById(Long id) {
		replyDao.deleteById(id);
	}

	@Override
	public void update(Reply reply) {
		replyDao.update(reply);
	}

	@Override
	public Reply getById(Long id) {
		return replyDao.getById(id);
	}

	@Override
	public List<Reply> getListByReportId(Long reportId) {
		return replyDao.getListByReportId(reportId);
	}

}
