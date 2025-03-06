package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSigninUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveSigninUser;

@Repository
public class ILiveSigninUserDaoImpl extends HibernateBaseDao<ILiveSigninUser, Long> implements ILiveSigninUserDao {

	@Override
	public Pagination getPage(String signid,String name, String roomName, Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveSigninUser where 1 = 1");
		if (signid!=null) {
			finder.append(" and sign.signId =:signid");
			finder.setParam("signid", Long.parseLong(signid));
		}
		if (name!=null&&!name.equals("")) {
			finder.append(" and (userName like :name or userId like :name or userPhone like :name)");
			finder.setParam("name", "%"+name+"%");
		}
		if (roomName!=null) {
			finder.append(" and room.roomId =:roomName");
			finder.setParam("roomName", roomName);
		}
		if(null != startTime){
			finder.append(" and createTime >=:startTime");
			finder.setParam("startTime", startTime);
			
		}
		if(null != endTime){
			finder.append(" and createTime <=:endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by createTime DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void save(ILiveSigninUser ILiveSigninUser) {
		this.getSession().save(ILiveSigninUser);
	}

	@Override
	public void update(ILiveSigninUser ILiveSigninUsero) {
		this.getSession().update(ILiveSigninUsero);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from ILiveSigninUser where signUserId = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public ILiveSigninUser getById(Long signId) {
		String hql = "from ILiveSigninUser where signUserId = :signId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("signId", signId);
		List<ILiveSigninUser> list = query.list();
		return list.get(0);
	}

	@Override
	protected Class<ILiveSigninUser> getEntityClass() {
		return ILiveSigninUser.class;
	}

	@Override
	public Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId) {
		Finder finder = Finder.create("from ILiveSigninUser");
		finder.append(" where 1=1 ");
		finder.append(" and userId =:userId ");
		finder.setParam("userId", userId);
		if (name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Pagination getDocByEnterpriseId(Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveSigninUser");
		finder.append(" where enterpriseId =:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public ILiveSigninUser getListByEnterpriseId(Long signId,String userPhone) {
		String hql = "from ILiveSigninUser where sign.signId =:signId and userPhone=:userPhone";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("signId", signId);
		query.setParameter("userPhone", userPhone);
		@SuppressWarnings("unchecked")
		List<ILiveSigninUser> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public List<ILiveSigninUser> getList(String signid, String name, String roomName, Date startTime, Date endTime) {
		Finder finder = Finder.create("from ILiveSigninUser where 1 = 1");
		if (signid!=null) {
			finder.append(" and sign.signId =:signid");
			finder.setParam("signid", Long.parseLong(signid));
		}
		if (name!=null&&!name.equals("")) {
			finder.append(" and userName like :name or userId like :name or userPhone like :name");
			finder.setParam("name", "%"+name+"%");
		}
		if (roomName!=null&&!roomName.equals("")) {
			finder.append(" and room.roomId like :roomName or room.liveEvent.liveTitle like :roomName");
			finder.setParam("roomName", "%"+roomName+"%");
		}
		if(null != startTime){
			finder.append(" and createTime >=:startTime");
			finder.setParam("startTime", startTime);
			
		}
		if(null != endTime){
			finder.append(" and createTime <=:endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by createTime DESC");
		return find(finder);
	}

}
