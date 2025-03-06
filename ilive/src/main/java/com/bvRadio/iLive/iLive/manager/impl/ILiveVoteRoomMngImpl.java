package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveVoteRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveVoteActivity;
import com.bvRadio.iLive.iLive.entity.ILiveVoteRoom;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteRoomMng;

@Transactional
public class ILiveVoteRoomMngImpl implements ILiveVoteRoomMng {

	@Autowired
	private ILiveVoteRoomDao iLiveVoteRoomDao;
	
	@Autowired
	private ILiveVoteActivityMng iLiveVoteActivityMng;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public void save(ILiveVoteRoom iLiveVoteRoom) {
		Long nextId = fieldIdMng.getNextId("ilive_vote_room","id", 1);
		iLiveVoteRoom.setId(nextId);
		iLiveVoteRoomDao.save(iLiveVoteRoom);
	}

	@Override
	public void update(ILiveVoteRoom iLiveVoteRoom) {
		iLiveVoteRoomDao.update(iLiveVoteRoom);
	}

	@Override
	public ILiveVoteRoom getStartByRoomId(Integer roomId) {
		return iLiveVoteRoomDao.getStartByRoomId(roomId);
	}

	@Override
	public boolean isStartVote(Integer roomId) {
		ILiveVoteRoom startByRoomId = getStartByRoomId(roomId);
		return startByRoomId!=null;
	}

	@Override
	public ILiveVoteRoom selectByRoomIdAndVoteId(Integer roomId, Long voteId) {
		return iLiveVoteRoomDao.selectByRoomIdAndVoteId(roomId,voteId);
	}

	@Override
	public void clearEnd(Integer roomId, Integer enterpriseId) {
		System.out.println("***********************************************进入mng");
		List<ILiveVoteActivity> voteList = iLiveVoteActivityMng.getH5VoteListByEnterpriseId(enterpriseId);
		List<Long> idList = new ArrayList<>();
		for(ILiveVoteActivity vote:voteList) {
			idList.add(vote.getId());
		}
		if(idList!=null&&idList.size()!=0) {
			iLiveVoteRoomDao.clearEnd(roomId,idList);
		}
	}

}
