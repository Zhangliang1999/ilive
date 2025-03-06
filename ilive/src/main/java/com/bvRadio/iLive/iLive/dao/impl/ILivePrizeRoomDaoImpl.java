package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILivePrizeRoomDao;
import com.bvRadio.iLive.iLive.entity.ILivePrizeRoom;

public class ILivePrizeRoomDaoImpl extends HibernateBaseDao<ILivePrizeRoom, Long> implements ILivePrizeRoomDao {

	@Override
	public void save(ILivePrizeRoom iLivePrizeRoom) {
		this.getSession().save(iLivePrizeRoom);
	}

	@Override
	public void update(ILivePrizeRoom iLivePrizeRoom) {
		this.getSession().update(iLivePrizeRoom);
	}

	@Override
	public ILivePrizeRoom selectStartPrize(Integer roomId) {
		Finder finder = Finder.create("from ILivePrizeRoom where roomId = :roomId and isOpen = 1");
		finder.setParam("roomId", roomId);
		@SuppressWarnings("unchecked")
		List<ILivePrizeRoom> find = find(finder);
		if (find!=null&&find.size()>0) {
			return find.get(0);
		}
		return null;
	}

	@Override
	public ILivePrizeRoom selectPrize(Integer roomId, Long prizeId) {
		Finder finder = Finder.create("from ILivePrizeRoom where roomId = :roomId and prizeId = :prizeId");
		finder.setParam("roomId", roomId);
		finder.setParam("prizeId", prizeId);
		@SuppressWarnings("unchecked")
		List<ILivePrizeRoom> find = find(finder);
		if (find!=null&&find.size()>0) {
			return find.get(0);
		}
		return null;
	}

	@Override
	protected Class<ILivePrizeRoom> getEntityClass() {
		return ILivePrizeRoom.class;
	}

	@Override
	public void clearEnd(Integer roomId,List<Long> list) {
		try{
			if(list!=null&&list.size()!=0) {
				String hql = "update ILivePrizeRoom set isOpen = 0"
						+ " where roomId = :roomId and prizeId not in (:list)";
				Query query = this.getSession().createQuery(hql);
				query.setParameter("roomId", roomId);
				query.setParameterList("list", list);
				query.executeUpdate();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
