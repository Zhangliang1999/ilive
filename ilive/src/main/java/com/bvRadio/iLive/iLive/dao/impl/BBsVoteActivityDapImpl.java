package com.bvRadio.iLive.iLive.dao.impl;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBsVoteActivityDao;
import com.bvRadio.iLive.iLive.entity.BBSVoteActivity;

@Repository
public class BBsVoteActivityDapImpl extends HibernateBaseDao<BBSVoteActivity, Long> implements BBsVoteActivityDao{

	@Override
	protected Class<BBSVoteActivity> getEntityClass() {
		return BBSVoteActivity.class;
	}

	@Override
	public boolean saveBBsVoteActivity(BBSVoteActivity bbsVoteActivity) {
		this.getSession().save(bbsVoteActivity);
		return true;
	}

	@Override
	public void deleteBBsVoteActivity(Long id) {
		BBSVoteActivity bbsVoteActivity = get(id);
		bbsVoteActivity.setDelFlag(1);
		getSession().update(bbsVoteActivity);
		getSession().flush();
	}

	@Override
	public void deleteBBsVoteActivityByIds(Long[] ids) {
		if(ids!=null&&ids.length>0){
			for (Long id : ids) {
				deleteBBsVoteActivity(id);
			}
		}
	}


	@Override
	public BBSVoteActivity selectBBsVoteActivityById(
			Long voteId) throws Exception {
		return get(voteId);
	}

	@Override
	public Pagination getBBsVoteAvtivityPage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from BBSVoteActivity bBSVoteActivity "
				+ " where 1=1 ");
		//删除标记参数
		Integer delFlag = (Integer)sqlParam.get("delFlag");
		if(delFlag!=null){		
			finder.append(" and (bBSVoteActivity.delFlag is null or  bBSVoteActivity.delFlag != :delFlag)");
			finder.setParam("delFlag", delFlag);
		}
		//直播间Id
		Integer roomId = (Integer)sqlParam.get("roomId");
		if(roomId!=null){
			finder.append(" and bBSVoteActivity.iLiveLiveRoom.roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		//活动名称参数
		String voteTitle = (String)sqlParam.get("voteTitle");
		if(StringUtils.isNotBlank(voteTitle)){
			finder.append(" and bBSVoteActivity.voteTitle like :voteTitle");
			finder.setParam("voteTitle", "%"+voteTitle+"%");
		}
		
		finder.append(" order by bBSVoteActivity.createTime desc");
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}

	
	@Override
	public void updateBBsVoteActivityById(BBSVoteActivity sqlParam) {
		BBSVoteActivity bBSVoteActivity = get(sqlParam.getVoteId());
//		if(StringUtils.isNotBlank(sqlParam.getMediaFileName())){
//			iLiveMediaFile.setMediaFileName(sqlParam.getMediaFileName());
//		}
//		if(StringUtils.isNotBlank(sqlParam.getMediaFileDesc())){
//			iLiveMediaFile.setMediaFileDesc(sqlParam.getMediaFileDesc());
//		}
		getSession().update(bBSVoteActivity);
		getSession().flush();			
	}
}
