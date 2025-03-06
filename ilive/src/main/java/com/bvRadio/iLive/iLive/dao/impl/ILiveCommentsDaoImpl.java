package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveCommentsDao;
import com.bvRadio.iLive.iLive.entity.ILiveComments;
@Repository
public class ILiveCommentsDaoImpl extends HibernateBaseDao<ILiveComments, Long> implements ILiveCommentsDao {

	@Override
	public void saveILiveComments(ILiveComments liveComments) throws Exception {
		getSession().save(liveComments);
	}

	@Override
	protected Class<ILiveComments> getEntityClass() {
		return ILiveComments.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveComments> selectILiveCommentsByMsgId(Long msgId,
			boolean isChecked, boolean isDelete) throws Exception {
		Finder finder = Finder.create("select bean from ILiveComments bean");
		finder.append(" where bean.isChecked=:isChecked ");
		finder.setParam("isChecked", isChecked);
		finder.append(" and bean.isDelete = :isDelete");
		finder.setParam("isDelete", isDelete);
		if (null != msgId) {
			finder.append(" and bean.msgId = :msgId");
			finder.setParam("msgId", msgId);
		}
		finder.append(" order by bean.createTime desc");
		return 	find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveComments> selectILiveCommentsListByMsgId(Long msgId,boolean isDelete)
			throws Exception {
		Finder finder = Finder.create("select bean from ILiveComments bean");
		finder.append(" where isDelete=:isDelete ");
		finder.setParam("isDelete", isDelete);
		if (null != msgId) {
			finder.append(" and bean.msgId = :msgId");
			finder.setParam("msgId", msgId);
		}
		finder.append(" order by bean.createTime desc");
		return 	find(finder);
	}

	@Override
	public void deleteILiveComments(Long commentsId) throws Exception {
		ILiveComments iLiveComments = get(commentsId);
		iLiveComments.setIsDelete(true);
		getSession().update(iLiveComments);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveComments> selectILiveCommentsByUserIdAndMsgId(Long msgId,
			Long userId) throws Exception {
		Finder finder = Finder.create("select bean from ILiveComments bean");
		finder.append(" where bean.userId=:userId ");
		finder.setParam("userId", userId);
		finder.append(" and bean.msgId = :msgId");
		finder.setParam("msgId", msgId);
		return find(finder);
	}

	@Override
	public ILiveComments getILiveCommentsById(Long commentsId) throws Exception {
		ILiveComments iLiveComments = get(commentsId);
		return iLiveComments;
	}

	@Override
	public void updateIliveCommentsById(ILiveComments iLiveComments)
			throws Exception {
		getSession().update(iLiveComments);
	}


}
