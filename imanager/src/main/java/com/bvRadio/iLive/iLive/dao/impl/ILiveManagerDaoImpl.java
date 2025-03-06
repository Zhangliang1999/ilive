package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

public class ILiveManagerDaoImpl extends HibernateBaseDao<ILiveManager, Long> implements ILiveManagerDao {

	@Override
	public ILiveManager selectILiveManagerById(Long userId) {
		ILiveManager iLiveManager = get(userId);
		return iLiveManager;
	}

	@Override
	protected Class<ILiveManager> getEntityClass() {
		return ILiveManager.class;
	}

	@Override
	public ILiveManager getILiveMangerByMobile(String mobile) {
		return this.findUniqueByProperty("mobile", mobile);
	}

	@Override
	public void saveManager(ILiveManager iLiveMangerByMobile) {
		this.getSession().save(iLiveMangerByMobile);
	}

	@Override
	public ILiveManager getILiveManager(Long managerId) {
		return this.get(managerId);
	}

	@Override
	public ILiveManager checkLogin(String loginToken, Long userId) {
		return (ILiveManager) this.findUnique("from ILiveManager where loginToken=? and userId=?", loginToken, userId);
	}

	@Override
	public void updateLiveManager(ILiveManager manager) {
		this.getSession().update(manager);
	}

	@Override
	public ILiveManager getILiveManagerByUserName(String userName) {
		return (ILiveManager) this.findUnique("from ILiveManager where userName=?", userName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveManager> getILiveManagerByEnterpriseId(Integer enterpriseId) {
		Finder finder = Finder.create("from ILiveManager where enterPrise.enterpriseId=:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		return this.find(finder);
	}

	@Override
	public Pagination getPage(ILiveManager user, Integer pageSize, Integer pageNo) {
		Finder finder = Finder.create("from ILiveManager where isDel = 0");
		if (user!=null && user.getCertStatus()!=null && user.getCertStatus()==4) {
			//企业用户管理
			finder.append(" and certStatus = 4 and (isBlack!=1 or isBlack=null)");
		}else if(user!=null && user.getIsBlack()!=null && user.getIsBlack() == 1) {
			//黑名单用户管理
			finder.append(" and isBlack = 1");
		}else {
			//web用户
			finder.append(" and (isBlack!=1 or isBlack=null)");
		}
		//查询
		if(user!=null) {
			if(user.getUserId()!=null) {
				finder.append(" and userId = :userId");
				finder.setParam("userId", user.getUserId());
			}
			if(user.getUserName()!=null&&!user.getUserName().equals("")) {
				finder.append(" and userName like :userName");
				finder.setParam("userName", "%"+user.getUserName()+"%");
			}
			if(user.getNailName()!=null&&!("").equals(user.getNailName())) {
				finder.append(" and nailName like :nailName");
				finder.setParam("nailName", "%"+user.getNailName()+"%");
			}
			if (user.getStartTime()!=null&&!"".equals(user.getStartTime())) {
				Timestamp startTime = new Timestamp(user.getStartTime().getTime());
				finder.append(" and createTime >= :startTime");
				finder.setParam("startTime", startTime);
			}
			if (user.getEndTime()!=null&&!"".equals(user.getEndTime())) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
				Timestamp endTime = new Timestamp(user.getEndTime().getTime());
				finder.append(" and createTime <= :endTime");
				finder.setParam("endTime", endTime);
			}
		}
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}

	@Override
	public Pagination getUserRecord(ILiveManager iLiveManager, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveManager where isDel = 0 ");
		if (iLiveManager.getIsAllBlack()==null) {
			finder.append(" and (isAllBlack is NULL or isAllBlack = 0)");
		}else {
			finder.append(" and isAllBlack = 1");
		}
		if (iLiveManager.getUserId()!=null) {
			finder.append(" and userId = :userId");
			finder.setParam("userId", iLiveManager.getUserId());
		}
		if (iLiveManager.getMobile()!=null&&!iLiveManager.getMobile().equals("")) {
			finder.append(" and mobile like :mobile");
			finder.setParam("mobile", "%"+iLiveManager.getMobile()+"%");
		}
		if (iLiveManager.getNailName()!=null&&!iLiveManager.getNailName().equals("")) {
			finder.append(" and nailName like :nailName");
			finder.setParam("nailName", "%"+iLiveManager.getNailName()+"%");
		}
		if (iLiveManager.getBeizhu()!=null&&!iLiveManager.getBeizhu().equals("")) {
			finder.append(" and beizhu like :beizhu");
			finder.setParam("beizhu", "%"+iLiveManager.getBeizhu()+"%");
		}
		if (iLiveManager.getRegisterSource()!=null&&iLiveManager.getRegisterSource()!=-1) {
			finder.append(" and registerSource = :registerSource");
			finder.setParam("registerSource", iLiveManager.getRegisterSource());
		}
		if (iLiveManager.getRegisterStartTime()!=null) {
			finder.append(" and createTime > :registerStartTime");
			finder.setParam("registerStartTime", iLiveManager.getRegisterStartTime());
		}
		if (iLiveManager.getRegisterEndTime()!=null) {
			finder.append(" and createTime < :registerEndTime");
			finder.setParam("registerEndTime",iLiveManager.getRegisterEndTime());
		}
		
		finder.append(" order by createTime DESC");
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveManager> getUserRecordList(ILiveManager iLiveManager) {
		Finder finder = Finder.create("from ILiveManager where isDel = 0 ");
		if (iLiveManager.getIsAllBlack()==null) {
			finder.append(" and (isAllBlack is NULL or isAllBlack = 0)");
		}else {
			finder.append(" and isAllBlack = 1");
		}
		
		if (iLiveManager.getUserId()!=null) {
			finder.append(" and userId = :userId");
			finder.setParam("userId", iLiveManager.getUserId());
		}
		if (iLiveManager.getMobile()!=null&&!iLiveManager.getMobile().equals("")) {
			finder.append(" and mobile like :mobile");
			finder.setParam("mobile", "%"+iLiveManager.getMobile()+"%");
		}
		if (iLiveManager.getNailName()!=null&&!iLiveManager.getNailName().equals("")) {
			finder.append(" and nailName like :nailName");
			finder.setParam("nailName", "%"+iLiveManager.getNailName()+"%");
		}
		if (iLiveManager.getBeizhu()!=null&&!iLiveManager.getBeizhu().equals("")) {
			finder.append(" and beizhu like :beizhu");
			finder.setParam("beizhu", "%"+iLiveManager.getBeizhu()+"%");
		}
		if (iLiveManager.getRegisterSource()!=null&&iLiveManager.getRegisterSource()!=-1) {
			finder.append(" and registerSource = :registerSource");
			finder.setParam("registerSource", iLiveManager.getRegisterSource());
		}
		finder.append(" order by createTime DESC");
		return find(finder);
	}

}
