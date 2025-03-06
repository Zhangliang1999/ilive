package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveVoteProblem;

public interface ILiveVoteProblemMng {

	List<ILiveVoteProblem> getListByVoteId(Long voteId);
	
	public Long save(ILiveVoteProblem iLiveVoteProblem);
	
	public void update(ILiveVoteProblem iLiveVoteProblem);
	
	public void deleteById(Long id);
	
	ILiveVoteProblem getById(Long id);
	
	public void deleteAllByVoteId(Long voteId);
}
