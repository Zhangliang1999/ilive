package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ReportAndComplainDao;
import com.bvRadio.iLive.iLive.entity.ReportAndComplain;

@Repository
public class ReportAndComplainDaoImpl extends HibernateBaseDao<ReportAndComplain, Long> implements ReportAndComplainDao {

	@Override
	public void save(ReportAndComplain reportAndComplain) {
		this.getSession().save(reportAndComplain);
	}

	@Override
	public void delete(Long id) {
		ReportAndComplain reportAndComplain = this.get(id);
		reportAndComplain.setIsDel(1);
		this.getSession().update(reportAndComplain);
	}

	@Override
	public void update(ReportAndComplain reportAndComplain) {
		this.getSession().update(reportAndComplain);
	}

	@Override
	public ReportAndComplain getById(Long id) {
		return this.get(id);
	}

	@Override
	public Pagination getPage(Long id, Long username, Integer roomId,Integer type, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ReportAndComplain where isDel = 0 and type = :type");
		finder.setParam("type", type);
		if(id!=null&&id!=0) {
			finder.append(" and id = :id");
			finder.setParam("id", id);
		}
		if (username!=null&&!username.equals("")) {
			finder.append(" and nowRoomName like :nowRoomName");
			finder.setParam("nowRoomName", "%"+username+"%");
		}
		if(roomId!=null&&roomId!=0) {
			finder.append(" and roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<ReportAndComplain> getEntityClass() {
		return ReportAndComplain.class;
	}

}
