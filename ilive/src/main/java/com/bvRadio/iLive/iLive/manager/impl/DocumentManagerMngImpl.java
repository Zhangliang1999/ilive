package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.DocumentManagerDao;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
@Transactional
public class DocumentManagerMngImpl implements DocumentManagerMng {

	@Autowired
	private DocumentManagerDao managerDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Autowired
	private DocumentPictureMng picMng;
	
	@Override
	public Pagination getPage(String name,Integer enterpriseId, Integer pageNo, Integer pageSize) {
		return managerDao.getPage(name,enterpriseId, pageNo, pageSize);
	}

	@Override
	public Long save(DocumentManager documentManager) {
		Long nextId = filedIdMng.getNextId("document_manager", "id", 1);
		documentManager.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		documentManager.setCreateTime(now);
		documentManager.setUpdateTime(now);
		managerDao.save(documentManager);
		return nextId;
	}

	@Override
	public void update(DocumentManager documentManager) {
		managerDao.update(documentManager);
	}

	@Override
	public void delete(Long id) {
		managerDao.delete(id);
	}

	@Override
	public DocumentManager getById(Long id) {
		DocumentManager doc = managerDao.getById(id);
		if(doc==null) {
			return null;
		}
		List<DocumentPicture> list = picMng.getListByDocId(id);
		if(list!=null) {
			doc.setList(list);
		}
		return doc;
	}

	@Override
	@Transactional
	public boolean saveDoc(DocumentManager documentManager, String str) {
		Long id = this.save(documentManager);
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
	public List<DocumentManager> getListByEnterpriseId(Integer enterpriseId) {
		return managerDao.getListByEnterpriseId(enterpriseId);
	}

	@Override
	public void batchDel(List<Long> list) {
		for(long id:list) {
			managerDao.delete(id);
		}
		
	}

}
