package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveVoteRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveVoteRoom;

public class ILiveVoteRoomDaoImpl extends HibernateBaseDao<ILiveVoteRoomDao, Long> implements ILiveVoteRoomDao {

	@Override
	public void save(ILiveVoteRoom iLiveVoteRoom) {
		this.getSession().save(iLiveVoteRoom);
	}

	@Override
	public void update(ILiveVoteRoom iLiveVoteRoom) {
		this.getSession().update(iLiveVoteRoom);
	}

	@Override
	public ILiveVoteRoom getStartByRoomId(Integer roomId) {
		Finder finder = Finder.create("from ILiveVoteRoom where roomId = :roomId and isOpen = 1");
		finder.setParam("roomId", roomId);
		@SuppressWarnings("unchecked")
		List<ILiveVoteRoom> list = find(finder);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	protected Class<ILiveVoteRoomDao> getEntityClass() {
		return ILiveVoteRoomDao.class;
	}

	@Override
	public ILiveVoteRoom selectByRoomIdAndVoteId(Integer roomId, Long voteId) {
		Finder finder = Finder.create("from ILiveVoteRoom where roomId = :roomId and voteId = :voteId");
		finder.setParam("roomId", roomId);
		finder.setParam("voteId", voteId);
		@SuppressWarnings("unchecked")
		List<ILiveVoteRoom> list = find(finder);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void clearEnd(Integer roomId, List<Long> list) {
		System.out.println("**************************************进入dao");
		try{
			String sql = "update ILiveVoteRoom set isOpen = 0 where roomId = :roomId and voteId not in (:list)";
			Query query = this.getSession().createQuery(sql);
			query.setParameter("roomId", roomId);
			query.setParameterList("list", list);
			query.executeUpdate();
		}catch(Exception e){e.printStackTrace();}
		
	}

}
