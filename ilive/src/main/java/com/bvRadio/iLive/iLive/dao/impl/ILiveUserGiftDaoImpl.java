package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveUserGiftDao;
import com.bvRadio.iLive.iLive.entity.ILiveUserGift;
@Repository
public class ILiveUserGiftDaoImpl extends HibernateBaseDao<ILiveUserGift, Long> implements ILiveUserGiftDao {

	@Override
	public Pagination selectILiveUserGiftPage(String userContent,
			String roomContent, Integer roomId,Integer pageNo,
			Integer pageSize) throws Exception {
		Finder finder = Finder.create("from ILiveUserGift bean");
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
	protected Class<ILiveUserGift> getEntityClass() {
		return ILiveUserGift.class;
	}

	@Override
	public void insertILiveUserGift(ILiveUserGift iLiveUserGift)
			throws Exception {
		getSession().save(iLiveUserGift);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveUserGift> selectILiveUserGiftExcel(String userContent,
			String roomContent, Integer roomId) throws Exception {
		Finder finder = Finder.create("from ILiveUserGift bean");
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
		List<ILiveUserGift> find = find(finder);
		return find;
	}

}
