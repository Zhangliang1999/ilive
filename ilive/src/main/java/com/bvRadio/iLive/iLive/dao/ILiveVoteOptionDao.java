package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveVoteOption;

public interface ILiveVoteOptionDao {

	public List<ILiveVoteOption> getListByProblemId(Long problemId);
	
	public void save(ILiveVoteOption iLiveVoteOption);
	
	public void update(ILiveVoteOption iLiveVoteOption);
	
	public void delete(Long id);
	
	public ILiveVoteOption getById(Long id);
	
	public void deleteAllByProblemId(Long problemId);
	
}
