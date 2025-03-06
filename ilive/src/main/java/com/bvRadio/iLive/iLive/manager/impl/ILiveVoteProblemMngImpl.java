package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveVoteProblemDao;
import com.bvRadio.iLive.iLive.entity.ILiveVoteProblem;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteProblemMng;

@Service
@Transactional
public class ILiveVoteProblemMngImpl implements ILiveVoteProblemMng{

	@Autowired
	private ILiveVoteProblemDao iLiveVoteProblemDao;	//投票问题
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public List<ILiveVoteProblem> getListByVoteId(Long voteId) {
		return iLiveVoteProblemDao.getListByVoteId(voteId);
	}

	@Override
	public Long save(ILiveVoteProblem iLiveVoteProblem) {
		Long nextId = fieldIdMng.getNextId("ilive_vote_problem", "id", 1);
		iLiveVoteProblem.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveVoteProblem.setCreateTime(now);
		iLiveVoteProblem.setUpdateTime(now);
		iLiveVoteProblemDao.save(iLiveVoteProblem);
		return nextId;
	}

	@Override
	public void update(ILiveVoteProblem iLiveVoteProblem) {
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveVoteProblem.setUpdateTime(now);
		iLiveVoteProblemDao.update(iLiveVoteProblem);
	}

	@Override
	public void deleteById(Long id) {
		iLiveVoteProblemDao.deleteById(id);
	}

	@Override
	public ILiveVoteProblem getById(Long id) {
		return iLiveVoteProblemDao.getById(id);
	}

	@Override
	public void deleteAllByVoteId(Long voteId) {
		iLiveVoteProblemDao.deleteAllByVoteId(voteId);
	}

}
