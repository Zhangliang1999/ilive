package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSendMsgDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.entity.ILiveSendMsg;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveSendMsgMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Transactional
@Service
public class ILiveSendMsgMngImp implements ILiveSendMsgMng {
   @Autowired ILiveSendMsgDao iLiveSendMsgDao;
	@Override
	public List<ILiveSendMsg> selectILiveSubById(Long roomId) {
		// TODO Auto-generated method stub
		return iLiveSendMsgDao.selectILiveSubById(roomId);
	}

	@Override
	public void save(ILiveSendMsg iLiveSubLevel) {
		// TODO Auto-generated method stub
		iLiveSendMsgDao.save(iLiveSubLevel);
	}

	@Override
	public Long selectMaxId() {
		// TODO Auto-generated method stub
		return iLiveSendMsgDao.selectMaxId();
	}

	@Override
	public void delete(Long roomId) {
		// TODO Auto-generated method stub
		iLiveSendMsgDao.delete(roomId);
	}

	@Override
	public void update(ILiveSendMsg iLiveSubLevel) {
		// TODO Auto-generated method stub
		iLiveSendMsgDao.update(iLiveSubLevel);
	}

	@Override
	public ILiveSendMsg getRecode(Long roomId) {
		// TODO Auto-generated method stub
		return iLiveSendMsgDao.getRecode(roomId);
	}

	@Override
	public Pagination selectILiveRoomThirdPage(Integer pageNo, Integer pageSize,
			Integer roomId) {
		Pagination pagination = new Pagination(pageNo, pageSize, 0);
		try {
			pagination = iLiveSendMsgDao.selectILiveRoomThirdPage(pageNo,pageSize,roomId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	
	

}
