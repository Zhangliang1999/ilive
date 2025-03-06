package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
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
		finder.append(" order by bean.createTime");
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
		finder.append(" order by bean.createTime");
		return 	find(finder);
	}

	@Override
	public void deleteILiveComments(Long commentsId) throws Exception {
		ILiveComments iLiveComments = get(commentsId);
		iLiveComments.setIsDelete(true);
		getSession().update(iLiveComments);
	}

	@Override
	public Pagination getByUserId(ILiveComments iLiveComments, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveComments where userId=:userId and isDelete=false");
		finder.setParam("userId", iLiveComments.getUserId());
		if(iLiveComments.getMsgId()!=null) {
			finder.append(" and msgId = :msgId");
			finder.setParam("msgId", iLiveComments.getMsgId());
		}
		if(iLiveComments.getComments()!=null&&!iLiveComments.getComments().equals("")) {
			finder.append(" and comments like :comments");
			finder.setParam("comments", "%"+iLiveComments.getComments()+"%");
		}
		finder.append(" order by createTime");
		return 	find(finder,pageNo,pageSize);
	}

	@Override
	public ILiveComments queryById(Long commentsId) {
		return this.get(commentsId);
	}

	@Override
	public void update(ILiveComments iLiveComments) {
		this.getSession().update(iLiveComments);
	}

}
