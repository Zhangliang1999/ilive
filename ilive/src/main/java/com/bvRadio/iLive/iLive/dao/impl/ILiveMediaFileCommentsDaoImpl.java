package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileCommentsDao;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;

public class ILiveMediaFileCommentsDaoImpl extends HibernateBaseDao<ILiveMediaFileComments, Long>
		implements ILiveMediaFileCommentsDao {

	@Override
	public void addFileComments(ILiveMediaFileComments comments) {
		this.getSession().save(comments);
	}

	@Override
	public Pagination getFileComments(Long fileId, Integer pageNo, Integer pageSize) {
		String hql = " from ILiveMediaFileComments comments where iLiveMediaFile.fileId=:fileId ";
		Finder finder = Finder.create(hql);
		finder.setParam("fileId", fileId);
		finder.append(" and comments.checkState=1 ");
		finder.append(" and comments.delFlag=0 ");
		finder.append(" order by comments.topFlagNum desc,comments.topTime desc, comments.createTime desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<ILiveMediaFileComments> getEntityClass() {
		return ILiveMediaFileComments.class;
	}

	@Override
	public ILiveMediaFileComments getFileCommentById(Long commentId) {
		return this.get(commentId);
	}

	@Override
	public Pagination selectILiveMediaFileCommentsPage(Long fileId, Integer pageNo, Integer pageSize,
			Integer uncheckQueryFlag, String search) throws Exception {
		String hql = " from ILiveMediaFileComments bean where bean.iLiveMediaFile.fileId=:fileId";
		Finder finder = Finder.create(hql);
		finder.setParam("fileId", fileId);
		finder.append(" and bean.delFlag=:delFlag");
		if (uncheckQueryFlag != null && uncheckQueryFlag == 0) {
			finder.append(" and bean.checkState=:checkState");
			finder.setParam("checkState", uncheckQueryFlag);
		}
		if (search != null && !"".equals(search)) {
			finder.append(" and bean.comments like :comments");
			finder.setParam("comments", "%" + search + "%");
		}
		finder.setParam("delFlag", 0);
		finder.append(" order by topFlagNum desc,topTime desc, createTime desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void updateCheckState(Long commentsId, Integer checkState) throws Exception {
		ILiveMediaFileComments iLiveMediaFileComments = get(commentsId);
		iLiveMediaFileComments.setCheckState(checkState);
		getSession().update(iLiveMediaFileComments);
	}

	@Override
	public void updateDeleteState(Long commentsId) {
		ILiveMediaFileComments iLiveMediaFileComments = get(commentsId);
		iLiveMediaFileComments.setDelFlag(1);
		getSession().update(iLiveMediaFileComments);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFileComments> getFileCommentByIds(Long[] ids) {
		Finder create = Finder.create(" from ILiveMediaFileComments where commentsId in (:ids)");
		create.setParamList("ids", ids);
		return this.find(create);
	}

	@Override
	public void updateComments(ILiveMediaFileComments comments) {
		this.getSession().update(comments);
	}

	@Override
	public Long getCommentsMount(Long fileId) {
		String hql = "select count(comments) from ILiveMediaFileComments comments where comments.delFlag=0 and comments.iLiveMediaFile.fileId=?";
		Long findUnique = (Long) this.findUnique(hql, fileId);
		return findUnique;
	}

}
