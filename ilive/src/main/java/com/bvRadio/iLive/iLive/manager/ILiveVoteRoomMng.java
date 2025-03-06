package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveVoteRoom;

public interface ILiveVoteRoomMng {

	void save(ILiveVoteRoom iLiveVoteRoom);
	
	void update(ILiveVoteRoom iLiveVoteRoom);
	
	ILiveVoteRoom getStartByRoomId(Integer roomId);
	
	boolean isStartVote(Integer roomId);
	
	ILiveVoteRoom selectByRoomIdAndVoteId(Integer roomId,Long voteId);
	
	void clearEnd(Integer roomId,Integer enterpriseId);
}
