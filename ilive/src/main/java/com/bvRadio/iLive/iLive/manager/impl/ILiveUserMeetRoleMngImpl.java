package com.bvRadio.iLive.iLive.manager.impl;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bvRadio.iLive.iLive.dao.ILiveUserMeetRoleDao;
import com.bvRadio.iLive.iLive.entity.ILiveUserMeetRole;
import com.bvRadio.iLive.iLive.manager.ILiveUserMeetRoleMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
@Service
@Transactional
public class ILiveUserMeetRoleMngImpl implements ILiveUserMeetRoleMng {

	@Autowired
	private ILiveUserMeetRoleDao managerDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	

	@Override
	public Long save(ILiveUserMeetRole ILiveUserMeetRole) {
		Long nextId = filedIdMng.getNextId("ilive_user_meet_role", "id", 1);
		ILiveUserMeetRole.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		ILiveUserMeetRole.setCreateTime(now);
		managerDao.save(ILiveUserMeetRole);
		return nextId;
	}

	@Override
	public void update(ILiveUserMeetRole ILiveUserMeetRole) {
		Timestamp now = new Timestamp(new Date().getTime());
		ILiveUserMeetRole.setCreateTime(now);
		managerDao.update(ILiveUserMeetRole);
	}


	@Override
	public ILiveUserMeetRole getById(Long id) {
		ILiveUserMeetRole doc = managerDao.getById(id);
		if(doc==null) {
			return null;
		}
		
		return doc;
	}

	@Override
	public ILiveUserMeetRole getByUserMeetId(Integer roomId, String userId) {
		
		return managerDao.getByUserMeetId(roomId,userId);
	}

	@Override
	public List<ILiveUserMeetRole> getListByType(Integer roomId, Integer type) {
		return managerDao.getListByType(roomId, type);
	}

	@Override
	public void delete(List<ILiveUserMeetRole> role) {
		for (ILiveUserMeetRole iLiveUserMeetRole : role) {
			managerDao.delete(iLiveUserMeetRole.getId());
		}
		
	}









}
