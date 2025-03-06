package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveVotePeople;

public interface ILiveVotePeopleMng {

	public List<ILiveVotePeople> getListByUserId(Long userId,Long voteId);
	
	public Long save(ILiveVotePeople iLiveVotePeople);
	
	public Integer getAllPeopleByVoteId(Long voteId);
	public Integer getPeopleByOptionId(Long optionId);
	public Integer getPeopleByProblemId(Long problemId);
	
}
