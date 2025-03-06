package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveVoteOptionDao;
import com.bvRadio.iLive.iLive.entity.ILiveVoteOption;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteOptionMng;

@Transactional
public class ILiveVoteOptionMngImpl implements ILiveVoteOptionMng {

	@Autowired
	private ILiveVoteOptionDao iLiveVoteOptionDao;		//投票问题选项
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public List<ILiveVoteOption> getListByProblemId(Long problemId) {
		return iLiveVoteOptionDao.getListByProblemId(problemId);
	}

	@Override
	public Long save(ILiveVoteOption iLiveVoteOption) {
		Long nextId = fieldIdMng.getNextId("ilive_vote_option", "id", 1);
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveVoteOption.setId(nextId);
		iLiveVoteOption.setCreateTime(now);
		iLiveVoteOption.setUpdateTime(now);
		iLiveVoteOptionDao.save(iLiveVoteOption);
		return nextId;
	}

	@Override
	public void update(ILiveVoteOption iLiveVoteOption) {
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveVoteOption.setUpdateTime(now);
		iLiveVoteOptionDao.update(iLiveVoteOption);
	}

	@Override
	public void delete(Long id) {
		iLiveVoteOptionDao.delete(id);
	}

	@Override
	public ILiveVoteOption getById(Long id) {
		return iLiveVoteOptionDao.getById(id);
	}

	@Override
	public void deleteAllByProblemId(Long problemId) {
		iLiveVoteOptionDao.deleteAllByProblemId(problemId);
	}


}
