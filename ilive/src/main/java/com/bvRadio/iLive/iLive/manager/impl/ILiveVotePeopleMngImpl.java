package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveVotePeopleDao;
import com.bvRadio.iLive.iLive.entity.ILiveVotePeople;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveVotePeopleMng;

@Service
@Transactional
public class ILiveVotePeopleMngImpl implements ILiveVotePeopleMng {

	@Autowired
	private ILiveVotePeopleDao iLiveVotePeopleDao;	//用户投票记录

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public List<ILiveVotePeople> getListByUserId(Long userId,Long voteId) {
		return iLiveVotePeopleDao.getListByUserId(userId,voteId);
	}

	@Override
	public Long save(ILiveVotePeople iLiveVotePeople) {
		Long nextId = fieldIdMng.getNextId("ilive_vote_people", "id", 1);
		iLiveVotePeople.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveVotePeople.setCreateTime(now);
		iLiveVotePeopleDao.save(iLiveVotePeople);
		return nextId;
	}

	@Override
	public Integer getAllPeopleByVoteId(Long voteId) {
		return iLiveVotePeopleDao.getAllPeopleByVoteId(voteId);
	}

	@Override
	public Integer getPeopleByOptionId(Long optionId) {
		return iLiveVotePeopleDao.getPeopleByOptionId(optionId);
	}

	@Override
	public Integer getPeopleByProblemId(Long problemId) {
		return iLiveVotePeopleDao.getPeopleByProblemId(problemId);
	}
}
