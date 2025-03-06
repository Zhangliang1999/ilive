package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveVoteRoom;

public interface ILiveVoteRoomDao {

	void save(ILiveVoteRoom iLiveVoteRoom);
	
	void update(ILiveVoteRoom iLiveVoteRoom);
	
	ILiveVoteRoom getStartByRoomId(Integer roomId);
	
	ILiveVoteRoom selectByRoomIdAndVoteId(Integer roomId, Long voteId);
	
	void clearEnd(Integer roomId,List<Long> list);
}
