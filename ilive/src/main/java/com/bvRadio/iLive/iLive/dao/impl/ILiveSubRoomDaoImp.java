package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;
@Repository
public class ILiveSubRoomDaoImp  extends HibernateBaseDao<IliveSubRoom, Long> implements  ILiveSubRoomDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<IliveSubRoom> selectILiveSubById(Long userId) {
		Finder finder = Finder.create("from IliveSubRoom where subAccountId ="+userId);
		return this.find(finder);
	}

	@Override
	protected Class<IliveSubRoom> getEntityClass() {
		// TODO Auto-generated method stub
		return IliveSubRoom.class;
	}

	@Override
	public void save(IliveSubRoom iLiveSubLevel) {
		this.getSession().save(iLiveSubLevel);
		
	}

	@Override
	public Integer selectMaxId() {
		String hql = "from IliveSubRoom  order by id desc";
		Finder finder = Finder.create(hql);
		List<IliveSubRoom> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Integer id=find.get(0).getId();
			return (int) (find.get(0).getId()+1);
		}
		return null;
	}

	@Override
	public void delete(Long userId,String roomId) {
		String hql = "from IliveSubRoom where subAccountId=:userId  and liveRoomId=:roomId ";
		Finder finder = Finder.create(hql);
		finder.setParam("userId", userId);
		finder.setParam("roomId", Integer.parseInt(roomId));
		getSession().delete(this.find(finder).get(0));
		
	}

	@Override
	public void update(IliveSubRoom iLiveSubLevel) {
		// TODO Auto-generated method stub
		this.getSession().update(iLiveSubLevel);
	}

	@Override
	public IliveSubRoom getSubRoom(Long userId) {
		if (userId == null) {
			return null;
		}
		return (IliveSubRoom) this.findUnique("from IliveSubRoom where userId=?", userId);
	
	}

	@Override
	public void delete(String roomId) {
		String hql = "from IliveSubRoom where  liveRoomId=:roomId ";
		Finder finder = Finder.create(hql);
		finder.setParam("roomId", Integer.parseInt(roomId));
		for(int i=0;i<this.find(finder).size();i++){
			getSession().delete(this.find(finder).get(i));
		}
		
	}

	@Override
	public List<IliveSubRoom> getSubRoomByIds(String userIds) {
		Finder finder = Finder.create("from IliveSubRoom where subAccountId in ("+userIds+")");
		return this.find(finder);
	}
	
}
