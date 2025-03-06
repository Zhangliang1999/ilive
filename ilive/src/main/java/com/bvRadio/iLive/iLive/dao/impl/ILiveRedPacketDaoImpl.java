package com.bvRadio.iLive.iLive.dao.impl;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveRedPacketDao;
import com.bvRadio.iLive.iLive.entity.ILiveRedPacket;

@Repository
public class ILiveRedPacketDaoImpl extends HibernateBaseDao<ILiveRedPacket, Long> implements ILiveRedPacketDao {

	@Override
	public Pagination getPage(String name,Integer roomId,Integer pageNo,Integer pageSize) {
		Finder finder = Finder.create("from ILiveRedPacket where 1 = 1 ");
		
		if (name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		
		if (roomId!=null) {
			finder.append(" and room.roomId =:roomId");
			finder.setParam("roomId", roomId);
		}
		
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void save(ILiveRedPacket ILiveRedPacket) {
		this.getSession().save(ILiveRedPacket);
	}

	@Override
	public void update(ILiveRedPacket ILiveRedPacketo) {
		this.getSession().update(ILiveRedPacketo);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from ILiveRedPacket where packetId = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public ILiveRedPacket getById(Long packetId) {
		String hql = "from ILiveRedPacket where packetId = :packetId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("packetId", packetId);
		List<ILiveRedPacket> list = query.list();
		return list.get(0);
	}

	@Override
	protected Class<ILiveRedPacket> getEntityClass() {
		return ILiveRedPacket.class;
	}

	@Override
	public List<ILiveRedPacket> getListByRoomId(Integer roomId) {
		Date time=new Date();
		String hql = "from ILiveRedPacket where room.roomId = :roomId and endTime >:time";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		query.setParameter("time", time);
		List<ILiveRedPacket> list = query.list();
		return list;
	}

	@Override
	public ILiveRedPacket getIsStart(Integer roomId) {
		Date time=new Date();
		String hql = "from ILiveRedPacket where room.roomId = :roomId and isAllow = 1 and endTime >:time";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		query.setParameter("time", time);
		List<ILiveRedPacket> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	

}
