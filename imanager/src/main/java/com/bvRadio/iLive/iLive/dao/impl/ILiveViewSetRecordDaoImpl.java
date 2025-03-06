package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveViewSetRecordDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewSetRecord;


@Repository
public class ILiveViewSetRecordDaoImpl extends HibernateBaseDao<ILiveViewSetRecord, Long> implements ILiveViewSetRecordDao {

	@Override
	public void saveRecord(ILiveViewSetRecord iLiveViewSetRecord) {
		this.getSession().save(iLiveViewSetRecord);
	}

	@Override
	protected Class<ILiveViewSetRecord> getEntityClass() {
		return ILiveViewSetRecord.class;
	}

	@Override
	public List<ILiveViewSetRecord> getByRoomId(Integer roomId) {
		String hql = "from ILiveViewSetRecord where roomId=:roomId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveViewSetRecord> list = query.list();
		return list;
	}

	@Override
	public Pagination getPage(Integer roomId,Integer pageNo,Integer pageSize) {
		Finder finder =Finder.create("from ILiveViewSetRecord where roomId = :roomId");
		finder.setParam("roomId", roomId);
		return find(finder, pageNo, pageSize);
	}

}
