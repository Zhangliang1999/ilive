package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveVoteActivity;

public interface ILiveVoteActivityDao {

	public Pagination getPage(Integer roomId,String name, Integer pageNo, Integer pageSize);
	public Pagination getPageByEnterpriseId(Integer enterpriseId,String name, Integer pageNo, Integer pageSize);
	
	public ILiveVoteActivity getById(Long voteId);
	
	public void save(ILiveVoteActivity voteActivity);
	
	public void update(ILiveVoteActivity voteActivity);
	
	public ILiveVoteActivity getActivityByRoomId(Integer roomId);
	ILiveVoteActivity getActivityByenterpriseId(Integer enterpriseId);
	public ILiveVoteActivity getActivityByEnterpriseId(Integer enterpriseId);
	
	public List<ILiveVoteActivity> getH5VoteList(Integer roomId);
	public List<ILiveVoteActivity> getH5VoteList2(Integer enterpriseId);
	public List<ILiveVoteActivity> getH5VoteListByEnterpriseId(Integer enterpriseId);
	List<ILiveVoteActivity> getH5VoteListByUserId(Long userId);
	Pagination getpageByUserId(Long userId,String votename,Integer pageNo,Integer pageSize);
	public ILiveVoteActivity getH5Vote2(Integer enterpriseId);
	public ILiveVoteActivity getH5Vote(Integer roomId);
	
	void checkEndIsClose();
}
