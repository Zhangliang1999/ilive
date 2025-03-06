package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.dao.ILiveThematicDao;
import com.bvRadio.iLive.iLive.entity.ILiveThematic;
import com.bvRadio.iLive.iLive.manager.ILiveThematicMng;

@Service
public class ILiveThematicMngImpl implements ILiveThematicMng {
	
	@Autowired
	private ILiveThematicDao iLiveThematicDao;
	
	@Autowired
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao; 
	
	@Override
	public Pagination selectILiveThematicPage(Integer pageNo, Integer pageSize,
			boolean isDelete) {
		Pagination pagination = new Pagination(pageNo, pageSize, 0);
		try {
			pagination = iLiveThematicDao.selectILiveThematicPage(pageNo, pageSize,isDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}


	@Override
	@Transactional
	public void addILiveThematic(ILiveThematic iLiveThematic) {
		try {
			Long nextId = iLiveFieldIdManagerDao.getNextId("ilive_thematic", "thematic_id", 1);
			iLiveThematic.setThematicId(nextId);
			iLiveThematicDao.addILiveThematic(iLiveThematic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	@Transactional
	public void deleteILiveThematicByIsDelete(Long thematicId, boolean isDelete) {
		try {
			iLiveThematicDao.deleteILiveThematicByIsDelete(thematicId,isDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public ILiveThematic selectIliveThematicById(Long thematicId) {
		ILiveThematic iLiveThematic = new ILiveThematic();
		try {
			iLiveThematic = iLiveThematicDao.selectIliveThematicById(thematicId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveThematic;
	}


	@Override
	@Transactional
	public void updateILiveThematic(ILiveThematic iLiveThematic) {
		try {
			iLiveThematicDao.updateILiveThematic(iLiveThematic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public List<ILiveThematic> getAllList() {
		return iLiveThematicDao.getAllList();
	}


	@Override
	public List<ILiveThematic> getListByEnterpriseId(Integer enterpriseId) {
		// TODO Auto-generated method stub
		return iLiveThematicDao.getListByEnterpriseId(enterpriseId);
	}

}
