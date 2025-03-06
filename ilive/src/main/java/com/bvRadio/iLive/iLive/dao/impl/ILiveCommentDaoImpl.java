package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveCommentDao;
import com.bvRadio.iLive.iLive.entity.ILiveComment;

@Repository
public class ILiveCommentDaoImpl extends HibernateBaseDao<ILiveComment, Integer> implements ILiveCommentDao {


	@Override
	public void save(ILiveComment ILiveComment) {
		this.getSession().save(ILiveComment);
	}

	@Override
	public void update(ILiveComment ILiveCommento) {
		this.getSession().update(ILiveCommento);
	}

	@Override
	public void delete(String id) {
		String hql = "delete from ILiveComment where commentId = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public ILiveComment getById(String commentId) {
		String hql = "from ILiveComment where commentId = :commentId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("commentId", commentId);
		List<ILiveComment> list = query.list();
		return list.get(0);
	}

	@Override
	protected Class<ILiveComment> getEntityClass() {
		return ILiveComment.class;
	}



	


	@Override
	public ILiveComment getIsStart(Integer roomId) {
		String hql = "from ILiveComment where room.roomId = :roomId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveComment> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}
	
	/*@Override
	public ILiveComment getIsStart(Integer roomId) {
		Date time=new Date();
		String hql = "from ILiveComment where room.roomId = :roomId and isAllow = 1 and endTime >:time";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		query.setParameter("time", time);
		List<ILiveComment> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}*/

}
