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
		String hql = " from ILiveMediaFileComments where iLiveMediaFile.fileId=:fileId and checkState =1 and delFlag=0   ";
		Finder finder = Finder.create(hql);
		finder.setParam("fileId", fileId);
		finder.append(" and mediaInfoDealState=1 ");
		finder.append(" order by createTime desc ");
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
	public Pagination getFileCommentsById(ILiveMediaFileComments commons, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveMediaFileComments where userId=:userId and delFlag=0");
		finder.setParam("userId", commons.getUserId());
		if(commons.getiLiveMediaFile()!=null&&commons.getiLiveMediaFile().getFileId()!=null) {
			finder.append(" and iLiveMediaFile.fileId = :fileId");
			finder.setParam("fileId",commons.getiLiveMediaFile().getFileId());
		}
		if(commons.getComments()!=null&&!commons.getComments().equals("")) {
			finder.append(" and comments like :comments");
			finder.setParam("comments","%" + commons.getComments()+"%");
		}
		
		finder.append(" order by updateTime DESC");
		Pagination find = find(finder, pageNo, pageSize);
		return find;
	}

	@Override
	public void updateComments(ILiveMediaFileComments commons) {
		this.getSession().update(commons);
	}

	@Override
	public Pagination getPageByUserId(Long userId, Long fileId, String keyword, Integer pageNo, Integer pageSize) {
		return find(getFinder(userId, fileId, keyword), pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}
	
	private Finder getFinder(Long userId, Long fileId, String keyword) {
		Finder finder = Finder.create("from ILiveMediaFileComments where userId=:userId and delFlag=0");
		finder.setParam("userId", userId);
		if (fileId!=null) {
			finder.append(" and iLiveMediaFile.fileId = :fileId");
			finder.setParam("fileId", fileId);
		}
		if (keyword!=null&&!keyword.equals("")) {
			finder.append(" and comments like :comments");
			finder.setParam("comments", "%"+keyword+"%");
		}
		return finder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFileComments> getListByUserId(Long userId, Long fileId, String keyword) {
		return find(getFinder(userId, fileId, keyword));
	}

}
