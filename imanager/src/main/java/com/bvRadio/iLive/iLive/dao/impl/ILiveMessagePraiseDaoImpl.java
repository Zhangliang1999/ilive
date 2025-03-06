package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveMessagePraiseDao;
import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;
@Repository
public class ILiveMessagePraiseDaoImpl extends HibernateBaseDao<ILiveMessagePraise, Long> implements ILiveMessagePraiseDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMessagePraise> selectILiveMessagePraisByMsgId(Long msgId)
			throws Exception {
		Finder finder = Finder.create("select bean from ILiveMessagePraise bean ");
		finder.append(" where bean.msgId=:msgId ");
		finder.setParam("msgId", msgId);
		return find(finder);
	}

	@Override
	protected Class<ILiveMessagePraise> getEntityClass() {
		return ILiveMessagePraise.class;
	}

	@Override
	public void insertILiveMessagePraise(ILiveMessagePraise iLiveMessagePraise)
			throws Exception {
		getSession().save(iLiveMessagePraise);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveMessagePraise selectILiveMessagePraise(Long msgId, Long userId)
			throws Exception {
		Finder finder = Finder.create("select bean from ILiveMessagePraise bean ");
		finder.append(" where bean.msgId=:msgId ");
		finder.setParam("msgId", msgId);
		finder.append(" and bean.userId=:userId ");
		finder.setParam("userId", userId);
		List<ILiveMessagePraise> list = find(finder);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

}
