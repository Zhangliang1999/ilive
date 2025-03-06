package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRecordDao;
import com.bvRadio.iLive.iLive.dao.ILiveLiveReportDao;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRecord;
import com.bvRadio.iLive.iLive.entity.ILiveLiveReport;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

@Repository
public class ILiveLiveRecordDaoImpl extends HibernateBaseDao<ILiveLiveRecord, Integer> implements ILiveLiveRecordDao {
	public Pagination getPage(Integer id, Integer userId, Integer roomId,
			Timestamp beginTime, Integer income,
			Integer totalNumber,int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveLiveRecord bean");
		finder.append(" where 1=1");
		if (null != id) {
			finder.append(" and bean.id = :id");
			finder.setParam("id", id);
		}
		if (null!= userId) {
			finder.append(" and bean.userId = :userId");
			finder.setParam("userId", userId);
		}
		if (null!= roomId) {
			finder.append(" and bean.roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		if (null!= income) {
			finder.append(" and bean.income > :income");
			finder.setParam("income", income);
		}
		if (null!= totalNumber) {
			finder.append(" and bean.totalNumber > :totalNumber");
			finder.setParam("totalNumber", totalNumber);
		}
		finder.append(" order by bean.beginTime desc");
		return find(finder, pageNo, pageSize);
	}
	public ILiveLiveRecord findById(Integer id) {
		ILiveLiveRecord entity = get(id);
		return entity;
	}

	public ILiveLiveRecord save(ILiveLiveRecord bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveLiveRecord bean) {
		getSession().update(bean);
	}

	public ILiveLiveRecord deleteById(Integer id) {
		ILiveLiveRecord entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ILiveLiveRecord> getEntityClass() {
		return ILiveLiveRecord.class;
	}

	

}