package com.bvRadio.iLive.manager.manager.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.manager.dao.ILiveEnterpriseDao;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;

@Service
@Transactional
public class ILiveEnterpriseMngImpl implements ILiveEnterpriseMng {

	@Autowired
	private ILiveEnterpriseDao enterpriseDao;
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;

	@Autowired
	private ILiveManagerDao managerDao;
	
	@Autowired
	private ILiveEnterpriseFansMng iLiveEnterpriseFansMng;

	@Override
	@Transactional(readOnly = true)
	public List<ILiveEnterprise> getList() {
		return enterpriseDao.getList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int getUserNum(){
		return enterpriseDao.getUserNum();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int getContractUserNum(){
		return enterpriseDao.getContractUserNum();
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveEnterprise getILiveEnterpriseById(Integer enterpriseId) {
		return enterpriseDao.getILiveEnterpriseById(enterpriseId);
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(String enterprisetype, String content, int pass, Integer pageNo, int pageSize) {
		return enterpriseDao.getPage(enterprisetype, content, pass, pageNo, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(String content, int pass, Integer pageNo, int pageSize) {
		return enterpriseDao.getPage(content, pass, pageNo, pageSize);
	}

	@Override
	public void update(ILiveEnterprise iLiveEnterprise) {
		enterpriseDao.update(iLiveEnterprise);
	}

	@Override
	public void del(Integer enterpriseId) {
		enterpriseDao.del(enterpriseId);
	}

	@Override
	public boolean saveEnterprise(ILiveEnterprise iLiveEnterprise) {
		Long nextId = filedIdMng.getNextId("ilive_enterprise", "enterprise_id", 1);
		iLiveEnterprise.setEnterpriseId(nextId.intValue());
		enterpriseDao.saveILiveEnterprise(iLiveEnterprise);
		return true;
	}

	@Override
	public boolean saveEnterpriseForPhone(ILiveEnterprise iLiveEnterprise, ILiveManager iLiveManagerRefresh) {
		Long nextId = filedIdMng.getNextId("ilive_enterprise", "enterprise_id", 1);
		iLiveEnterprise.setEnterpriseId(nextId.intValue());
		// 认证中
		iLiveEnterprise.setCertStatus(1);
		boolean saveILiveEnterprise = enterpriseDao.saveILiveEnterprise(iLiveEnterprise);
		ILiveEnterprise iLiveEnterpriseById = this.getILiveEnterpriseById(nextId.intValue());
		iLiveManagerRefresh.setCertStatus(3);
		iLiveManagerRefresh.setEnterPrise(iLiveEnterpriseById);
		managerDao.updateLiveManager(iLiveManagerRefresh);
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getILiveEnterprisesByCertStatus(Integer certStauts, Integer pageNo, Integer pageSize) {
		return enterpriseDao.getILiveEnterprisesByCertStatus(certStauts, pageNo, pageSize);
	}

	@Override
	@Transactional
	public void updateEnterpriseWithPerson(ILiveEnterprise enterprise, List<ILiveManager> managerList) {
		enterpriseDao.updateByBean(enterprise);
		if (managerList != null && !managerList.isEmpty()) {
			for (ILiveManager manager : managerList) {
				manager.setCertStatus(enterprise.getCertStatus());
				managerDao.updateLiveManager(manager);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination getILiveEnterprisesByCertStatusByList(List<Integer> certStatusList, String enterpriseType,
			String content, Integer pageNo, Integer pageSize) {
		return enterpriseDao.getILiveEnterprisesByCertStatusByList(certStatusList, enterpriseType, content, pageNo,
				pageSize);
	}
	@Override
	@Transactional(readOnly = true)
	public Pagination getILiveEnterprisesByCertStatusByList(List<Integer> certStatusList, String enterpriseType,
			String content, Integer pageNo, Integer pageSize,Integer stamp) {
		return enterpriseDao.getILiveEnterprisesByCertStatusByList(certStatusList, enterpriseType, content, pageNo,
				pageSize,stamp);
	}

	@Override
	@Transactional
	public void forbidden(Integer enterpriseId,Integer status) {
		ILiveEnterprise enterprise = getILiveEnterpriseById(enterpriseId);
		enterprise.setDisabled(status);
		update(enterprise);
		iLiveEnterpriseFansMng.updateBatchEnterpriseState(enterpriseId);
	}

	@Override
	public Pagination getPage(ILiveEnterprise iLiveEnterprise, Integer pageNo, int pageSize) {
		return enterpriseDao.getPage(iLiveEnterprise, pageNo, pageSize);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void batchUpdate(List<ILiveEnterprise> list) {
		Iterator<ILiveEnterprise> iterator = list.iterator();
		while(iterator.hasNext()) {
			ILiveEnterprise enterprise = iterator.next();
			enterpriseDao.update(enterprise);
		}
	}

	@Override
	public List<ILiveEnterprise> getList(ILiveEnterprise iLiveEnterprise) {
		return enterpriseDao.getList(iLiveEnterprise);
	}

	@Override
	public void startDev(Integer enterpriseId) {
		ILiveEnterprise iLiveEnterprise = getILiveEnterpriseById(enterpriseId);
		iLiveEnterprise.setIsDeveloper(1);
		String appID = getRandomString(16);
		String secret = UUID.randomUUID().toString();
		iLiveEnterprise.setAppId(appID);
		iLiveEnterprise.setSecret(secret);
		update(iLiveEnterprise);
	}
	public  String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		 Random random = new Random();
		 StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
		 int number = random.nextInt(base.length());
		 sb.append(base.charAt(number));
		}
		//此处做个校检，如果数据库有同样的appid了需要重新生成
		Pagination pager=enterpriseDao.getPageByAppId(sb.toString(),null,null);
		if(pager.getList().size()>0){
			getRandomString(16);
		}
		 return sb.toString();
	}

	@Override
	public Pagination getautoPage(ILiveEnterprise iLiveEnterprise, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return enterpriseDao.getautoPage(iLiveEnterprise, pageNo, pageSize);
	}
}
