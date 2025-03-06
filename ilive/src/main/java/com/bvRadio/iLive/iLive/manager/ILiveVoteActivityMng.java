package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveVoteActivity;

public interface ILiveVoteActivityMng {

	public Pagination getPage(Integer roomId,String name,Integer pageNo,Integer pageSize);
	public Pagination getPageByEnterpriseId(Integer enterpriseId,String name,Integer pageNo,Integer pageSize);
	
	public ILiveVoteActivity getById(Long voteId);

	public Long save(ILiveVoteActivity voteActivity);
	
	public void update(ILiveVoteActivity voteActivity);
	
	public void createVote(ILiveVoteActivity iLiveVoteActivity,String strList);
	public void updateVote(ILiveVoteActivity iLiveVoteActivity,String strList);
	
	public ILiveVoteActivity getActivityByRoomId(Integer roomId);
	public ILiveVoteActivity getActivityByenterpriseId(Integer enterpriseId);
	public ILiveVoteActivity getActivityByEnterpriseId(Integer enterpriseId);
	
	/**
	 * 获取直播间已开启的和没有开启的投票活动
	 * @param roomId
	 * @return
	 */
	public List<ILiveVoteActivity> getH5VoteList(Integer roomId);
	public List<ILiveVoteActivity> getH5VoteListByEnterpriseId(Integer enterpriseId);
	public List<ILiveVoteActivity> getH5VoteListByUserId(Long userId);
	Pagination getpageByUserId(Long userId,String votename,Integer pageNo,Integer pageSize);
	/**
	 * 获取直播间已开启的投票活动
	 * @param roomId
	 * @return
	 */
	public ILiveVoteActivity getH5Vote(Integer roomId);
	public ILiveVoteActivity getH5Vote2(Integer enterpriseId);
	
	public void vote(Long voteId,Long userId,String string);
	
	public ILiveVoteActivity getResult(Long voteId);
}
