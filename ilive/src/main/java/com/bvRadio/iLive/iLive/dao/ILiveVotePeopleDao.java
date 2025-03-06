package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveVotePeople;

public interface ILiveVotePeopleDao {

	public List<ILiveVotePeople> getListByUserId(Long userId,Long voteId);
	
	public Integer getAllPeopleByVoteId(Long voteId);
	
	public Integer getPeopleByOptionId(Long optionId);
	
	public void save(ILiveVotePeople iLiveVotePeople);
	
	public Integer getPeopleByProblemId(Long problemId);
}
