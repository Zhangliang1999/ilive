package com.bvRadio.iLive.iLive.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveRoomRegisterDao;
import com.bvRadio.iLive.iLive.entity.ILiveRoomRegister;

@Repository
public class ILiveRoomRegisterDaoImpl extends HibernateBaseDao<ILiveRoomRegister, Long> implements ILiveRoomRegisterDao {

	@Override
	public void saveILiveRoomRegiste(ILiveRoomRegister iLiveRoomRegister) {
		this.getSession().save(iLiveRoomRegister);
	}

	@Override
	public ILiveRoomRegister queryILiveRoomRegisterByUserIdAndRoomId(String userId, Integer roomId) {
		return null;
	}

	@Override
	protected Class<ILiveRoomRegister> getEntityClass() {
		return ILiveRoomRegister.class;
	}

	@Override
	public boolean queryILiveIsRegister(String userId, Long liveEventId) {
		String hql = "select count(*) from ILiveRoomRegister where liveEventId = :liveEventId and userId = :userId and state = 0";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("liveEventId", liveEventId);
		query.setParameter("userId", userId);
		long num = (Long) query.uniqueResult();
		return num == 0?false:true;
	}

	@Override
	public boolean queryIliveRoomRegister(String userId, Integer roomId) {
		String hql = "select count(*) from ILiveRoomRegister where roomId = :roomId and userId = :userId and state = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		query.setParameter("userId", userId);
		long num = (Long) query.uniqueResult();
		return num == 0?false:true;
	}

	@Override
	public boolean queryMediaRegister(String userId, Long fileId) {
		String hql = "select count(*) from ILiveRoomRegister where fileId = :fileId and userId = :userId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("fileId", fileId);
		query.setParameter("userId", userId);
		long num = (Long) query.uniqueResult();
		return num == 0?false:true;
	}

	@Override
	public Pagination querySignByRoomId(Integer roomId,Long liveEventId, String name, Integer state, Integer pageNo, Integer pageSize) {
		Finder finder = null;
		if(state == 1) {
			//state为1点赞行为  根据直播间获取
			finder = Finder.create("from ILiveRoomRegister where roomId = :roomId and state = 1");
			finder.setParam("roomId", roomId);
		}else if(state == 0) {
			//state为0签到行为 根据直播场次获取
			finder = Finder.create("from ILiveRoomRegister where liveEventId = :liveEventId and state = 0");
			finder.setParam("liveEventId", liveEventId);
		}
		
		finder.append(" order by createTime DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Long MediaRegisterCount(Long fileId) {
		String hql = "select count(*) from ILiveRoomRegister where fileId = :fileId and state = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("fileId", fileId);
		long num = (Long) query.uniqueResult();
		return num;
	}

	@Override
	public Long ILiveIsRegisterCount(Long liveEventId) {
		String hql = "select count(*) from ILiveRoomRegister where roomId = :roomId  and state = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", liveEventId.intValue());
		long num = (Long) query.uniqueResult();
		return num;
	}

}
