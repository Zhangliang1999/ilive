package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveRoomThirdDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.IliveRoomThird;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;
@Repository
public class ILiveRoomThirdDaoImp  extends HibernateBaseDao<IliveRoomThird, Long> implements  ILiveRoomThirdDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<IliveRoomThird> selectILiveRoomThirdById(Integer roomId) {
		Finder finder = Finder.create("from IliveRoomThird where roomId ="+roomId);
		return this.find(finder);
	}

	@Override
	protected Class<IliveRoomThird> getEntityClass() {
		// TODO Auto-generated method stub
		return IliveRoomThird.class;
	}

	@Override
	public void save(IliveRoomThird iLiveSubLevel) {
		this.getSession().save(iLiveSubLevel);
		
	}

	@Override
	public Integer selectMaxId() {
		String hql = "from IliveRoomThird  order by id desc";
		Finder finder = Finder.create(hql);
		List<IliveRoomThird> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Integer id=find.get(0).getId();
			return (int) (find.get(0).getId());
		}
		return null;
	}

	@Override
	public void delete(Integer roomId) {
		String hql = "from IliveRoomThird where 1=1  and id=:roomId ";
		Finder finder = Finder.create(hql);
		finder.setParam("roomId", roomId);
		getSession().delete(this.find(finder).get(0));
		
	}

	@Override
	public void update(IliveRoomThird iliveRoomThird) {
		// TODO Auto-generated method stub
		this.getSession().update(iliveRoomThird);
	}

	@Override
	public IliveRoomThird getRoomThird(Integer roomId) {
		if (roomId == null) {
			return null;
		}
		return (IliveRoomThird) this.findUnique("from IliveRoomThird where id=?", roomId);
	
	}
	@Override
	public Pagination selectILiveRoomThirdPage(Integer pageNo, Integer pageSize,
			Integer roomId) throws Exception {
		Finder finder = Finder.create("from IliveRoomThird bean where bean.roomId=:roomId");
		finder.setParam("roomId", roomId);
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<IliveRoomThird> getILiveRoomThirdById(Integer id) {
		Finder finder = Finder.create("from IliveRoomThird where id ="+id);
		return this.find(finder);
	}

	@Override
	public void updateStatues(Integer roomId,Integer Status) {
		String hql="UPDATE ilive_room_third SET tstatus="+Status+" WHERE room_id="+roomId;
		Query queryupdate=this.getSession().createSQLQuery(hql);
		queryupdate.executeUpdate();
	}
}
