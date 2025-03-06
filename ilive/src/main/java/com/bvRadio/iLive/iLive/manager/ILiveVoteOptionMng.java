package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveVoteOption;

public interface ILiveVoteOptionMng {

	public List<ILiveVoteOption> getListByProblemId(Long problemId);
	
	public Long save(ILiveVoteOption iLiveVoteOption);
	
	public void update(ILiveVoteOption iLiveVoteOption);
	
	public void delete(Long id);
	
	ILiveVoteOption getById(Long id);
	
	void deleteAllByProblemId(Long problemId);
	
}
