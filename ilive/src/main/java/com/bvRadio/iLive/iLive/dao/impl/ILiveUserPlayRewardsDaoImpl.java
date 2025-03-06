package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveUserPlayRewardsDao;
import com.bvRadio.iLive.iLive.entity.ILiveUserPlayRewards;
@Repository
public class ILiveUserPlayRewardsDaoImpl extends
		HibernateBaseDao<ILiveUserPlayRewards, Long> implements
		ILiveUserPlayRewardsDao {

	@Override
	public Pagination selectILiveUserPlayRewardsByPage(String userContent,
			String roomContent, Integer roomId, Integer pageNo, Integer pageSize)
			throws Exception {
		Finder finder = Finder.create("from ILiveUserPlayRewards bean");
		finder.append(" where bean.roomId=:roomId");
		finder.setParam("roomId", roomId);
		if(userContent!=null){
			finder.append(" and (bean.userName=:userName ");
			finder.setParam("userName", userContent);
			try {
				long parseLong = Long.parseLong(userContent);
				finder.append(" or bean.userId=:userId)");
				finder.setParam("userId", parseLong);
			} catch (Exception e) {
				finder.append(" )");
			}
		}
		if(roomContent!=null){
			finder.append(" and (bean.roomTitle=:roomTitle ");
			finder.setParam("roomTitle", roomContent);
			try {
				long parseLong = Long.parseLong(roomContent);
				finder.append(" or bean.evenId=:evenId)");
				finder.setParam("evenId", parseLong);
			} catch (Exception e) {
				finder.append(" )");
			}
		}
		Pagination pagination = find(finder, pageNo, pageSize);
		return pagination;
	}

	@Override
	public void insertILiveUserPlayRewards(
			ILiveUserPlayRewards iLiveUserPlayRewards) throws Exception {
		getSession().save(iLiveUserPlayRewards);
	}

	@Override
	protected Class<ILiveUserPlayRewards> getEntityClass() {
		return ILiveUserPlayRewards.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveUserPlayRewards> selectILiveUserPlayRewardsByExcel(
			String userContent, String roomContent, Integer roomId)
			throws Exception {
		Finder finder = Finder.create("from ILiveUserPlayRewards bean");
		finder.append(" where bean.roomId=:roomId");
		finder.setParam("roomId", roomId);
		if(userContent!=null){
			finder.append(" and (bean.userName=:userName ");
			finder.setParam("userName", userContent);
			try {
				long parseLong = Long.parseLong(userContent);
				finder.append(" or bean.userId=:userId)");
				finder.setParam("userId", parseLong);
			} catch (Exception e) {
				finder.append(" )");
			}
		}
		if(roomContent!=null){
			finder.append(" and (bean.roomTitle=:roomTitle ");
			finder.setParam("roomTitle", roomContent);
			try {
				long parseLong = Long.parseLong(roomContent);
				finder.append(" or bean.evenId=:evenId)");
				finder.setParam("evenId", parseLong);
			} catch (Exception e) {
				finder.append(" )");
			}
		}
		List<ILiveUserPlayRewards> find = find(finder);
		return find;
	}

}
