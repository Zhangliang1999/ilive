package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileCommentsDao;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;

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
	@Transactional(readOnly = true)
	public Pagination selectILiveMediaFileCommentsPage(Long fileId, Integer pageNo, Integer pageSize,
			Integer uncheckQueryFlag,String search) {
		Pagination pagination = new Pagination(pageNo, pageSize, 0);
		try {
			pagination = iLiveMediaFileCommentsDao.selectILiveMediaFileCommentsPage(fileId, pageNo, pageSize,
					uncheckQueryFlag,search);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	@Transactional
	public void updateCheckState(Long commentsId, Integer checkState) throws Exception {
		iLiveMediaFileCommentsDao.updateCheckState(commentsId, checkState);
	}

	@Override
	@Transactional
	public void updateDeleteState(Long commentsId) {
		iLiveMediaFileCommentsDao.updateDeleteState(commentsId);
	}

	@Override
	@Transactional
	public void batchCheck(Long[] ids) {
		List<ILiveMediaFileComments> commentsList = iLiveMediaFileCommentsDao.getFileCommentByIds(ids);
		if (commentsList != null && !commentsList.isEmpty()) {
			for (ILiveMediaFileComments comments : commentsList) {
				if (comments.getCheckState() != 1) {
					comments.setCheckState(1);
					iLiveMediaFileCommentsDao.updateComments(comments);
				}

			}
		}
	}

	@Override
	@Transactional
	public void batchDelete(Long[] ids) {
		List<ILiveMediaFileComments> commentsList = iLiveMediaFileCommentsDao.getFileCommentByIds(ids);
		if (commentsList != null && !commentsList.isEmpty()) {
			for (ILiveMediaFileComments comments : commentsList) {
				comments.setDelFlag(1);
				iLiveMediaFileCommentsDao.updateComments(comments);
			}
		}
	}

	@Override
	@Transactional
	public void update(ILiveMediaFileComments comments) {
		iLiveMediaFileCommentsDao.updateComments(comments);
	}

}
