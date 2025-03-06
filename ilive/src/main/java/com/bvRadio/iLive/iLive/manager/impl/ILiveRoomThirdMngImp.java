package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveRoomThirdDao;
import com.bvRadio.iLive.iLive.entity.IliveRoomThird;
import com.bvRadio.iLive.iLive.manager.ILiveRoomThirdMng;



@Transactional
@Service
public class ILiveRoomThirdMngImp implements ILiveRoomThirdMng {
@Autowired ILiveRoomThirdDao iLiveRoomThirdDao;
	@Override
	public List<IliveRoomThird> selectILiveRoomThirdById(Integer roomId) {
		// TODO Auto-generated method stub
		return iLiveRoomThirdDao.selectILiveRoomThirdById(roomId);
	}

	@Override
	public void save(IliveRoomThird iliveRoomThird) {
		// TODO Auto-generated method stub
		iLiveRoomThirdDao.save(iliveRoomThird);
	}

	@Override
	public Integer selectMaxId() {
		// TODO Auto-generated method stub
		return iLiveRoomThirdDao.selectMaxId();
	}

	@Override
	public void delete(Integer roomId) {
		// TODO Auto-generated method stub
		iLiveRoomThirdDao.delete(roomId);
	}

	@Override
	public void update(IliveRoomThird iliveRoomThird) {
		// TODO Auto-generated method stub
		iLiveRoomThirdDao.update(iliveRoomThird);
	}

	@Override
	public IliveRoomThird getRoomThird(Integer roomId) {
		// TODO Auto-generated method stub
		return iLiveRoomThirdDao.getRoomThird(roomId);
	}
	@Override
	@Transactional(readOnly=true)
	public Pagination selectILiveRoomThirdPage(Integer pageNo, Integer pageSize,
			Integer roomId) {
		Pagination pagination = new Pagination(pageNo, pageSize, 0);
		try {
			pagination = iLiveRoomThirdDao.selectILiveRoomThirdPage(pageNo,pageSize,roomId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	public List<IliveRoomThird> getILiveRoomThirdById(Integer id) {
		// TODO Auto-generated method stub
		return iLiveRoomThirdDao.getILiveRoomThirdById(id);
	}

	@Override
	public void updateStatues(Integer roomId,Integer Status) {
		// TODO Auto-generated method stub
		iLiveRoomThirdDao.updateStatues(roomId,Status);
	}
	

}
