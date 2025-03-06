package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSigninUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveSigninUser;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.manager.ILiveSigninUserMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
@Transactional
public class ILiveSigninUserMngImpl implements ILiveSigninUserMng {

	@Autowired
	private ILiveSigninUserDao managerDao;
	
	@Override
	public Pagination getPage(String signId,String name, String roomName, Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
		return managerDao.getPage(signId,name, roomName, startTime, endTime, pageNo, pageSize);
	}

	@Override
	public String save(ILiveSigninUser ILiveSigninUser) {
		Timestamp now = new Timestamp(new Date().getTime());
		ILiveSigninUser.setCreateTime(now);
		
		managerDao.save(ILiveSigninUser);
		return "";
	}

	@Override
	public void update(ILiveSigninUser ILiveSigninUser) {
		managerDao.update(ILiveSigninUser);
	}

	@Override
	public void delete(Long id) {
		managerDao.delete(id);
	}

	@Override
	public ILiveSigninUser getById(Long id) {
		ILiveSigninUser doc = managerDao.getById(id);
		if(doc==null) {
			return null;
		}
		
		return doc;
	}


	@Override
	public List<ILiveSigninUser> getList(String signId, String name, String roomName, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return managerDao.getList(signId, name, roomName, startTime, endTime);
	}

	@Override
	public ILiveSigninUser getListByEnterpriseId(Long signId, String userPhone) {
		
		return managerDao.getListByEnterpriseId(signId, userPhone);
	}

	
}
