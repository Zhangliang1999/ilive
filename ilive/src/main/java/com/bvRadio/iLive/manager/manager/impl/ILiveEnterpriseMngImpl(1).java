package com.bvRadio.iLive.manager.manager.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.main;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
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

	@Override
	@Transactional(readOnly = true)
	public List<ILiveEnterprise> getList() {
		return enterpriseDao.getList();
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
	public boolean saveEnterpriseForPhone(ILiveEnterprise certEnterprise, ILiveManager iLiveManagerRefresh) {
		ILiveEnterprise iliveEnterprise = iLiveManagerRefresh.getEnterPrise();
		// 企业设置为认证中
		iliveEnterprise.setCertStatus(1);
		iliveEnterprise.setEnterpriseName(certEnterprise.getEnterpriseName());
		iliveEnterprise.setEnterpriseType(certEnterprise.getEnterpriseType());
		iliveEnterprise.setEnterpriseInfo(certEnterprise.getEnterpriseInfo());
		iliveEnterprise.setRoad(certEnterprise.getRoad());
		iliveEnterprise.setEnterpriseRegName(certEnterprise.getEnterpriseRegName());
		iliveEnterprise.setEnterpriseRegNo(certEnterprise.getEnterpriseRegNo());
		iliveEnterprise.setEnterpriseLicence(certEnterprise.getEnterpriseLicence());
		iliveEnterprise.setContactPhone(certEnterprise.getContactPhone());
		iliveEnterprise.setContactName(certEnterprise.getContactName());
		iliveEnterprise.setIdCard(certEnterprise.getIdCard());
		iliveEnterprise.setIdCardImg(certEnterprise.getIdCardImg());
		iliveEnterprise.setEnterprisePhone(certEnterprise.getEnterprisePhone());
		iliveEnterprise.setEnterpriseEmail(certEnterprise.getEnterpriseEmail());
		iliveEnterprise.setEnterpriseImg(certEnterprise.getEnterpriseImg());
		iliveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
		iLiveManagerRefresh.setCertStatus(3);
		enterpriseDao.update(iliveEnterprise);
		iLiveManagerRefresh.setEnterPrise(iliveEnterprise);
		managerDao.updateLiveManager(iLiveManagerRefresh);
		return true;
	}

	@Override
	public Integer saveEnterpriseForWeb(ILiveEnterprise certEnterprise, ILiveManager iLiveManagerRefresh) {
		// Long nextId = filedIdMng.getNextId("ilive_enterprise",
		// "enterprise_id", 1);
		// iLiveEnterprise.setEnterpriseId(nextId.intValue());
		// 认证中
		// iLiveEnterprise.setCertStatus(1);
		// 大于默认企业ID时
		// if (iLiveManagerRefresh.getEnterPrise().getEnterpriseId() == 10) {
		// // 修改企业
		// boolean saveILiveEnterprise =
		// enterpriseDao.saveILiveEnterprise(iLiveEnterprise);
		// ILiveEnterprise iLiveEnterpriseById =
		// this.getILiveEnterpriseById(nextId.intValue());
		// iLiveManagerRefresh.setCertStatus(3);
		// iLiveManagerRefresh.setEnterPrise(iLiveEnterpriseById);
		// managerDao.updateLiveManager(iLiveManagerRefresh);
		// return nextId.intValue();
		// } else {
		// // 修改企业
		// ILiveEnterprise enterPrise = iLiveManagerRefresh.getEnterPrise();
		// enterPrise = this.convertVo2Po(iLiveEnterprise, enterPrise);
		// enterpriseDao.updateEntity(enterPrise);
		// return enterPrise.getEnterpriseId();
		// }
		ILiveEnterprise iliveEnterprise = iLiveManagerRefresh.getEnterPrise();
		// 企业设置为认证中
		iliveEnterprise.setCertStatus(1);
		iliveEnterprise.setEnterpriseName(certEnterprise.getEnterpriseName());
		iliveEnterprise.setEnterpriseType(certEnterprise.getEnterpriseType());
		iliveEnterprise.setEnterpriseInfo(certEnterprise.getEnterpriseInfo());
		iliveEnterprise.setRoad(certEnterprise.getRoad());
		iliveEnterprise.setEnterpriseRegName(certEnterprise.getEnterpriseRegName());
		iliveEnterprise.setEnterpriseRegNo(certEnterprise.getEnterpriseRegNo());
		iliveEnterprise.setEnterpriseLicence(certEnterprise.getEnterpriseLicence());
		iliveEnterprise.setCertResource(certEnterprise.getCertResource());
		iliveEnterprise.setContactPhone(certEnterprise.getContactPhone());
		iliveEnterprise.setContactName(certEnterprise.getContactName());
		iliveEnterprise.setIdCard(certEnterprise.getIdCard());
		iliveEnterprise.setIdCardImg(certEnterprise.getIdCardImg());
		iliveEnterprise.setEnterprisePhone(certEnterprise.getEnterprisePhone());
		iliveEnterprise.setEnterpriseEmail(certEnterprise.getEnterpriseEmail());
		iliveEnterprise.setEnterpriseImg(certEnterprise.getEnterpriseImg());
		iliveEnterprise.setApplyTime(new Timestamp(System.currentTimeMillis()));
		iliveEnterprise.setStamp(certEnterprise.getStamp());
		iliveEnterprise.setIliveName(certEnterprise.getIliveName());
		iliveEnterprise.setUserId(certEnterprise.getUserId());
		iliveEnterprise.setUserPhone(certEnterprise.getUserPhone());
		iLiveManagerRefresh.setCertStatus(3);
		enterpriseDao.update(iliveEnterprise);
		iLiveManagerRefresh.setEnterPrise(iliveEnterprise);
		managerDao.updateLiveManager(iLiveManagerRefresh);
		return iliveEnterprise.getEnterpriseId();

	}

	private ILiveEnterprise convertVo2Po(ILiveEnterprise iLiveEnterprise, ILiveEnterprise enterPrise) {
		try {
			BeanUtilsExt.copyProperties(enterPrise, iLiveEnterprise);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return enterPrise;
	}

	// public static void main(String[] args) {
	// ILiveMediaFile file = new ILiveMediaFile();
	// file.setEnterpriseId(100L);
	// file.setCreateType(1);
	// ILiveMediaFile file2 = new ILiveMediaFile();
	// file2.setFileCover("aaaa");
	// file2.setFileId(101L);
	// try {
	// BeanUtils.copyProperties(file, file2);
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// }
	//
	// // System.out.println(file.getCreateType());
	// // System.out.println(file.getEnterpriseId());
	// // System.out.println(file.getFileCover());
	// }

}
