package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveCommentsDao;
import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveComments;
import com.bvRadio.iLive.iLive.manager.ILiveCommentsMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
@Service
public class ILiveCommentsMngImpl implements ILiveCommentsMng {
	
	@Autowired
	private ILiveCommentsDao iLiveCommentsDao;
	
	@Autowired
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;//主键
	
	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;

	@Override
	@Transactional
	public void saveILiveComments(ILiveComments liveComments) {
		try {
			Long nextId = iLiveFieldIdManagerDao.getNextId("ilive_comments", "comments_id", 1);
			liveComments.setCommentsId(nextId);
			iLiveCommentsDao.saveILiveComments(liveComments);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ILiveComments> selectILiveCommentsByMsgId(Long msgId,
			boolean isChecked, boolean isDelete) {
		List<ILiveComments> list = new ArrayList<ILiveComments>();
		try {
			list = iLiveCommentsDao.selectILiveCommentsByMsgId(msgId,isChecked,isDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ILiveComments> selectILiveCommentsListByMsgId(Long msgId,boolean isDelete ) {
		List<ILiveComments> list = new ArrayList<ILiveComments>();
		try {
			list = iLiveCommentsDao.selectILiveCommentsListByMsgId(msgId,isDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@Transactional
	public void deleteILiveComments(Long commentsId) {
		try {
			iLiveCommentsDao.deleteILiveComments(commentsId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ILiveComments getILiveCommentsById(Long commentsId) throws Exception {
		ILiveComments iLiveComments = iLiveCommentsDao.getILiveCommentsById(commentsId);
		return iLiveComments;
	}

	@Override
	@Transactional
	public void updateIliveCommentsById(ILiveComments iLiveComments) throws Exception {
			iLiveCommentsDao.updateIliveCommentsById(iLiveComments);
	}


}
