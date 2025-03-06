package com.bvRadio.iLive.iLive.manager.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBsVoteActivityDao;
import com.bvRadio.iLive.iLive.entity.BBSVoteActivity;
import com.bvRadio.iLive.iLive.manager.BBsVoteActivetyMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

/**
 * 媒资管理类实现
 * 
 * @author administrator
 */

@Service
@Transactional
public class BBsVoteActivityMngImpl implements BBsVoteActivetyMng {

	@Autowired
	private BBsVoteActivityDao bBsVoteActivityDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldMng;

	@Transactional
	@Override
	public boolean saveBBSVoteActivity(BBSVoteActivity bBSVoteActivity) {
		long voteId = iLiveFieldMng.getNextId("bbs_vote_activity_control", "vote_id", 1);
		bBSVoteActivity.setVoteId(voteId);
		bBsVoteActivityDao.saveBBsVoteActivity(bBSVoteActivity);
		return true;
	}
		
	@Transactional
	@Override
	public void deleteBBsVoteActivity(Long id) {
		bBsVoteActivityDao.deleteBBsVoteActivity(id);
	}
	
	@Transactional
	@Override
	public void deleteBBsVoteActivityByIds(Long[] ids) {
		bBsVoteActivityDao.deleteBBsVoteActivityByIds(ids);
	}

	@Override
	public BBSVoteActivity selectBBsVoteActivityById(
			Long voteId) throws Exception  {
		return bBsVoteActivityDao.selectBBsVoteActivityById(voteId);
	}



	@Transactional(readOnly=true)
	@Override
	public Pagination getBBsVoteAvtivityPage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize){
		return bBsVoteActivityDao.getBBsVoteAvtivityPage(sqlParam, pageNo, pageSize);
	}
		
	

	@Override
	@Transactional
	public void updateBBsVoteActivityById(BBSVoteActivity sqlParam) {
		bBsVoteActivityDao.updateBBsVoteActivityById(sqlParam);
	}

}
