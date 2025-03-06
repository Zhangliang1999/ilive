package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bvRadio.iLive.iLive.dao.ILiveCommentDao;
import com.bvRadio.iLive.iLive.entity.ILiveComment;
import com.bvRadio.iLive.iLive.manager.ILiveCommentMng;
@Service
@Transactional
public class ILiveCommentMngImpl implements ILiveCommentMng {

	@Autowired
	private ILiveCommentDao managerDao;
	
	
	

	@Override
	public void save(ILiveComment ILiveComment) {
		
		Timestamp now = new Timestamp(new Date().getTime());
		ILiveComment.setCreateTime(now);
		ILiveComment.setUpdateTime(now);
		managerDao.save(ILiveComment);
	}

	@Override
	public void update(ILiveComment ILiveComment) {
		managerDao.update(ILiveComment);
	}

	@Override
	public void delete(String id) {
		managerDao.delete(id);
	}

	@Override
	public ILiveComment getById(String id) {
		ILiveComment doc = managerDao.getById(id);
		if(doc==null) {
			return null;
		}
		
		return doc;
	}


	@Override
	public ILiveComment getIsStart(Integer roomId) {
		// TODO Auto-generated method stub
		return managerDao.getIsStart(roomId);
	}

}
