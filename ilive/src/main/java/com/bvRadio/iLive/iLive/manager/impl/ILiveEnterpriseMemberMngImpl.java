package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseMemberDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;

@Service
@Transactional
public class ILiveEnterpriseMemberMngImpl implements ILiveEnterpriseMemberMng {

	@Autowired
	private ILiveEnterpriseMemberDao iliveEnterpriseDao;
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	@Autowired
	private ILiveViewWhiteBillMng viewWhiteMng;
	

	@Override
	public Pagination getPage(String queryName, Integer pageNo, Integer pageSize, Integer enterpriseId) {
		return iliveEnterpriseDao.getPage(queryName, pageNo, pageSize, enterpriseId);
	}

	@Override
	public void deleteWhite(Integer whtebillId) {
		iliveEnterpriseDao.deleteWhite(whtebillId);
	}

	@Override
	public void addWhite(ILiveEnterpriseWhiteBill white) {
		Integer nextId = filedIdMng.getNextId("ilive_enterprise_whitebill", "whitebill_id", 1).intValue();
		white.setWhitebillId(nextId);
		iliveEnterpriseDao.addWhite(white);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void batchInsertUser(List<Object[]> readXlsx, UserBean userBean,Long iLiveEventId) {
		int count = 0;
		
		Map<String, ILiveViewWhiteBill> map=new HashMap<String, ILiveViewWhiteBill>();
		for (Object[] objArr : readXlsx) {
			count++;
			if (count == 1) {
				continue;
			}
			ILiveViewWhiteBill viewWhite = new ILiveViewWhiteBill();
			viewWhite.setExportTime(new Timestamp(System.currentTimeMillis()));
			long billId = filedIdMng.getNextId("ilive_view_whitebill", "bill_id", 1);
			viewWhite.setLiveEventId(iLiveEventId);
			viewWhite.setBillId(billId);
			boolean exsit = true;
			for (int i = 0; i < objArr.length; i++) {
				Object object = objArr[i];
				if (i == 0) {
					String phoneNum = (String) object;
					boolean flag= viewWhiteMng.checkUserInWhiteList(phoneNum, iLiveEventId);
					if (!flag) {
					} else {
						
							exsit=false;
						
						
					}
					viewWhite.setPhoneNum(phoneNum);
				} else if (i == 1) {
					viewWhite.setUserName((String) object);
				}
			}
			if (exsit) {
				map.put(viewWhite.getPhoneNum(), viewWhite);
				
			}

		}
		addWhite(map,iLiveEventId);
	}

	@Override
	public List<ILiveEnterpriseWhiteBill> getList(Integer enterpriseId) {
		return iliveEnterpriseDao.getList(enterpriseId);
	}

	@Override
	public void deleteAll(Integer enterpriseId) {
		iliveEnterpriseDao.deleteAll(enterpriseId);
	}

	@Override
	public List<ILiveEnterpriseWhiteBill> getEnterpriseWhite(Integer enterpriseId, String phoneNum) {
		return iliveEnterpriseDao.getEnterpriseWhite(enterpriseId, phoneNum);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void addWhite(Map<String, ILiveViewWhiteBill> map,Long iLiveEventId) {
		if (map!=null) {
			
			for (ILiveViewWhiteBill whiteBill : map.values()) { 
				viewWhiteMng.saveIliveViewWhiteBill(whiteBill);
				}
		}
	}

}
