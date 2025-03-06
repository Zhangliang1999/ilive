package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveVoteProblem;

public interface ILiveVoteProblemDao {

	public List<ILiveVoteProblem> getListByVoteId(Long voteId);
	
	public void save(ILiveVoteProblem iLiveVoteProblem);
	
	public void update(ILiveVoteProblem iLiveVoteProblem);
	
	public void deleteById(Long id);
	
	public ILiveVoteProblem getById(Long id);
	
	public void deleteAllByVoteId(Long voteId);
}
