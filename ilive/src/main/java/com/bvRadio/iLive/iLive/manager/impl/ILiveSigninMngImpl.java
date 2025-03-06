package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSigninDao;
import com.bvRadio.iLive.iLive.entity.ILiveSignin;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.manager.ILiveSigninMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
@Transactional
public class ILiveSigninMngImpl implements ILiveSigninMng {

	@Autowired
	private ILiveSigninDao managerDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Autowired
	private DocumentPictureMng picMng;
	
	

	@Override
	public Long save(ILiveSignin ILiveSignin) {
		Long nextId = filedIdMng.getNextId("ilive_sign_in", "sign_id", 1);
		ILiveSignin.setSignId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		ILiveSignin.setCreateTime(now);
		ILiveSignin.setUpdateTime(now);
		managerDao.save(ILiveSignin);
		return nextId;
	}

	@Override
	public void update(ILiveSignin ILiveSignin) {
		managerDao.update(ILiveSignin);
	}

	@Override
	public void delete(Long id) {
		managerDao.delete(id);
	}

	@Override
	public ILiveSignin getById(Long id) {
		ILiveSignin doc = managerDao.getById(id);
		if(doc==null) {
			return null;
		}
		
		return doc;
	}

	@Override
	@Transactional
	public boolean saveDoc(ILiveSignin ILiveSignin, String str) {
		Long id = this.save(ILiveSignin);
		JSONArray jsonArray = JSONArray.fromObject(str);
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			DocumentPicture picture = new DocumentPicture();
			picture.setDocId(id);
			picture.setUrl(jsonObject.getString("url"));
			picture.setName(jsonObject.getString("name"));
			picture.setType(jsonObject.getString("type"));
			picture.setSize(jsonObject.getInt("size"));
			picture.setWidth(jsonObject.getInt("width"));
			picture.setHeight(jsonObject.getInt("height"));
			picMng.save(picture);
		}
		return true;
	}

	@Override
	public Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId) {
		return managerDao.getCollaborativePage(name, pageNo, pageSize,userId);
	}

	@Override
	public Pagination getDocByEnterpriseId(Integer enterpriseId, Integer pageNo, Integer pageSize) {
		return managerDao.getDocByEnterpriseId(enterpriseId, pageNo, pageSize);
	}

	@Override
	public List<ILiveSignin> getListByEnterpriseId(Integer enterpriseId) {
		return managerDao.getListByEnterpriseId(enterpriseId);
	}

	@Override
	public Pagination getPage(String userId,String name, Integer roomName, Integer status, Integer pageNo, Integer pageSize) {
		return managerDao.getPage(userId,name, roomName, status, pageNo, pageSize);
	}
	
	@Override
	public Pagination getPageByEnterpriseId(String userId,String name,Integer enterpriseId,Integer status,Integer pageNo,Integer pageSize) {
		return managerDao.getPageByEnterpriseId(userId,name, enterpriseId, status, pageNo, pageSize);
	}

	@Override
	public ILiveSignin getIsStart(Integer roomId) {
		return managerDao.getIsStart(roomId);
	}
	@Override
	public ILiveSignin getIsStart2(Integer enterpriseId) {
		return managerDao.getIsStart2(enterpriseId);
	}

	@Override
	public List<ILiveSignin> getByEnterpriseId(Integer enterpriseId) {
		return managerDao.getByEnterpriseId(enterpriseId);
	}

	@Override
	public Pagination getPageByUserId(String name, String userId, Integer state, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return managerDao.getPageByUserId(name, userId, state, pageNo, pageSize);
	}

}
