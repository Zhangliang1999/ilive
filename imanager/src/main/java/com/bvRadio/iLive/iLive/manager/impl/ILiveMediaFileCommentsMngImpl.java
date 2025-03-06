package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileCommentsDao;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;

@Service
@Transactional
public class ILiveMediaFileCommentsMngImpl implements ILiveMediaFileCommentsMng {

	@Autowired
	private ILiveMediaFileCommentsDao iLiveMediaFileCommentsDao;

	@Autowired
	private ILiveFieldIdManagerMng fieldManagerMng;

	@Override
	@Transactional
	public Long addFileComments(ILiveMediaFileComments comments) {
		Long nextId = fieldManagerMng.getNextId("ilive_media_file_comments", "comments_id", 1);
		comments.setCommentsId(nextId);
		iLiveMediaFileCommentsDao.addFileComments(comments);
		return nextId;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getFileComments(Long fileId, Integer pageNo, Integer pageSize) {
		return iLiveMediaFileCommentsDao.getFileComments(fileId, pageNo, pageSize);
	}

	
	
	@Override
	@Transactional(readOnly = true)
	public ILiveMediaFileComments getFileCommentById(Long commentId) {
		return iLiveMediaFileCommentsDao.getFileCommentById(commentId);
	}

	@Override
	public Pagination getFileCommentsById(ILiveMediaFileComments commons, Integer pageNo, Integer pageSize) {
		return iLiveMediaFileCommentsDao.getFileCommentsById(commons,pageNo,pageSize);
	}

	@Override
	public void update(ILiveMediaFileComments commons) {
		iLiveMediaFileCommentsDao.updateComments(commons);
	}

	@Override
	public Pagination getPageByUserId(Long userId, Long fileId, String keyword, Integer pageNo, Integer pageSize) {
		return iLiveMediaFileCommentsDao.getPageByUserId(userId,fileId,keyword,pageNo,pageSize);
	}

	@Override
	public List<ILiveMediaFileComments> getListByUserId(Long userId, Long fileId, String keyword) {
		return iLiveMediaFileCommentsDao.getListByUserId(userId,fileId,keyword);
	}

}
