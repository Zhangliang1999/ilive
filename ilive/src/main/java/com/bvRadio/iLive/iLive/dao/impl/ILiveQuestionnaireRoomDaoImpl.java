package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireRoom;

public class ILiveQuestionnaireRoomDaoImpl extends HibernateBaseDao<ILiveQuestionnaireRoomDao, Long> implements ILiveQuestionnaireRoomDao {

	@Override
	public void save(ILiveQuestionnaireRoom iLiveQuestionnaireRoom) {
		this.getSession().save(iLiveQuestionnaireRoom);
	}

	@Override
	public void update(ILiveQuestionnaireRoom iLiveQuestionnaireRoom) {
		this.getSession().update(iLiveQuestionnaireRoom);
	}

	@Override
	public ILiveQuestionnaireRoom getStartByRoomId(Integer roomId) {
		Finder finder = Finder.create("from ILiveQuestionnaireRoom where roomId = :roomId and isOpen = 1");
		finder.setParam("roomId", roomId);
		@SuppressWarnings("unchecked")
		List<ILiveQuestionnaireRoom> list = find(finder);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	protected Class<ILiveQuestionnaireRoomDao> getEntityClass() {
		return ILiveQuestionnaireRoomDao.class;
	}

	@Override
	public ILiveQuestionnaireRoom selectByRoomIdAndQuestionnaireId(Integer roomId, Long questionnaireId) {
		Finder finder = Finder.create("from ILiveQuestionnaireRoom where roomId = :roomId and questionnaireId = :questionnaireId");
		finder.setParam("roomId", roomId);
		finder.setParam("questionnaireId", questionnaireId);
		@SuppressWarnings("unchecked")
		List<ILiveQuestionnaireRoom> list = find(finder);
		if (list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void clearEnd(Integer roomId, List<Long> list) {
		System.out.println("**************************************进入dao");
		try{
			String sql = "update ILiveQuestionnaireRoom set isOpen = 0 where roomId = :roomId and questionnaireId not in (:list)";
			Query query = this.getSession().createQuery(sql);
			query.setParameter("roomId", roomId);
			query.setParameterList("list", list);
			query.executeUpdate();
		}catch(Exception e){e.printStackTrace();}
		
	}

}
