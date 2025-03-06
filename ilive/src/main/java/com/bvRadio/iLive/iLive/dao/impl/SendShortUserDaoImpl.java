package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.SendShortUserDao;
import com.bvRadio.iLive.iLive.entity.SendShortUser;

@Repository
public class SendShortUserDaoImpl extends HibernateBaseDao<SendShortUser, Long> implements SendShortUserDao {

	@Override
	public Pagination getPage(Integer roomId,Long id,String sendUser, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from SendShortUser where roomId = :roomId");
		finder.setParam("roomId", roomId);
		if (sendUser!=null&&!"".equals(sendUser)) {
			finder.append(" and mobile like :userName");
			finder.setParam("userName", "%"+sendUser+"%");
		}
		if(id!=null) {
			finder.append(" and id = :id");
			finder.setParam("id", id);
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public SendShortUser getById(Long id) {
		return this.get(id);
	}

	@Override
	protected Class<SendShortUser> getEntityClass() {
		return SendShortUser.class;
	}

	@Override
	public void save(SendShortUser sendShortUser) {
		this.getSession().save(sendShortUser);
	}

	@Override
	public Pagination getPage(Integer roomId, Long id, String sendUser, Integer pageNo, Integer pageSize,
			Timestamp startTime, Timestamp endTime) {
		Finder finder = Finder.create("from SendShortUser where roomId = :roomId");
		finder.setParam("roomId", roomId);
		if (sendUser!=null&&!"".equals(sendUser)) {
			finder.append(" and mobile like :userName");
			finder.setParam("userName", "%"+sendUser+"%");
		}
		if(id!=null) {
			finder.append(" and id = :id");
			finder.setParam("id", id);
		}
		if(startTime!=null){
			finder.append(" and createTime > :startTime");
			finder.setParam("startTime", startTime);
		}
		if(endTime!=null){
			finder.append(" and createTime < :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

}
