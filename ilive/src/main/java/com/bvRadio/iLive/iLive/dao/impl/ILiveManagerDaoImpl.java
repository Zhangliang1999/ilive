package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;

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

	@SuppressWarnings("unchecked")
	@Override
	public ILiveManager getILiveMangerByMobile(String mobile) {
		if (mobile == null || mobile.equals("")) {
			return null;
		}
		String hql = "from ILiveManager where (mobile=:mobile or userName=:userName ) and isDel=0 ";
		Finder finder = Finder.create(hql);
		finder.setParam("mobile", mobile);
		finder.setParam("userName", mobile);
		List<ILiveManager> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			return find.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ILiveManager getILiveMangerByPhoneNumber(String mobile) {
		if (mobile == null || mobile.equals("")) {
			return null;
		}
		String hql = "from ILiveManager where mobile=:mobile and isDel=0 ";
		Finder finder = Finder.create(hql);
		finder.setParam("mobile", mobile);
		List<ILiveManager> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			return find.get(0);
		}
		return null;
	}
	@Override
	public List<ILiveManager> getILiveMangerListByMobile(String mobile) {
		if (mobile == null || mobile.equals("")) {
			return null;
		}
		String hql = "from ILiveManager where (mobile=:mobile or userName=:userName ) and isDel=0 ";
		Finder finder = Finder.create(hql);
		finder.setParam("mobile", mobile);
		finder.setParam("userName", mobile);
		List<ILiveManager> find = this.find(finder);
		
		return find;
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
		if (loginToken == null || loginToken.equals("")) {
			return null;
		}
		return (ILiveManager) this.findUnique("from ILiveManager where loginToken=? and userId=? and isDel=0 ",
				loginToken, userId);
	}

	@Override
	public void updateLiveManager(ILiveManager manager) {
		this.getSession().update(manager);
	}

	@Override
	public ILiveManager getILiveManagerByUserName(String userName) {
		if (userName == null || userName.equals("")) {
			return null;
		}
		return (ILiveManager) this.findUnique("from ILiveManager where userName=? and isDel=0 ", userName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveManager> getIiveManagerList(Long[] managerList) {
		Finder finder = Finder.create("from ILiveManager where userId in (:userId) and isDel=0 ");
		finder.setParamList("userId", managerList);
		return this.find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveManager> getIiveManagerListAndName(String username, Long[] managerList) {
		Finder finder = Finder
				.create("from ILiveManager where userId in (:userId) and isDel=0 and userName like :userName");
		finder.setParamList("userId", managerList);
		finder.setParam("userName", "%" + username + "%");
		return this.find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveManager getILiveMangerByWxOpenId(String wxOpenId) {
		if (wxOpenId == null || wxOpenId.equals("")) {
			return null;
		}
		List<ILiveManager> finder = this.find("from ILiveManager where wxOpenId=? and isDel=0", wxOpenId);
		if (finder != null && !finder.isEmpty()) {
			return (ILiveManager) finder.get(0);
		}
		return null;
	}

	@Override
	public ILiveManager getILiveMangerByLoginToken(Long userId, String loginToken) {
		if (loginToken == null || loginToken.equals("")) {
			return null;
		}
		return (ILiveManager) this.findUnique("from ILiveManager where userId=? and loginToken=? and isDel=0 ", userId,
				loginToken);
	}

	/**
	 * 根据微信UNIONID
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ILiveManager getILiveMangerByWxUnionId(String wxUnionId) {
		if (wxUnionId == null || wxUnionId.equals("")) {
			return null;
		}
		List<ILiveManager> finder = this.find("from ILiveManager where wxUnionId=? and isDel=0", wxUnionId);
		if (finder != null && !finder.isEmpty()) {
			return (ILiveManager) finder.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveManager> selectILiveManagerByRoomId(Integer roomId,Integer level) throws Exception {
		String hql ="";
		if(ILiveManager.USER_LEVEL_SUB.equals(level)) {
			 hql = "from ILiveManager where  userId in(select subAccountId from IliveSubRoom where liveRoomId=:roomId) and isDel=0 and  level=:level";
		}else if(ILiveManager.USER_LEVEL_Assistant.equals(level)){
			 hql = "from ILiveManager where   roomId=:roomId and isDel=0 and  level=:level";
					
		}
		Finder finder = Finder.create(hql);
		finder.setParam("roomId", roomId);
		finder.setParam("level", level);
		List<ILiveManager> find = this.find(finder);
		return find;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveManager selectILiveManagerByPhoneNumAndUserName(
			String userName, String phoneNum) {
		Finder finder = Finder.create(" from ILiveManager where userName=:userName and isDel=0 and mobile=:mobile");
		List<ILiveManager> find = find(finder);
		if (find != null && !find.isEmpty()) {
			return find.get(0);
		}
		return null;
	}

	@Override
	public void deleteILiveManager(Long userId) {
		ILiveManager iLiveManager = get(userId);
		getSession().delete(iLiveManager);
	}

	@Override
	public List<ILiveManager> getILiveManagerById(Long userId) {
		List<ILiveManager> finder = this.find("from ILiveManager where userId=? and isDel=0", userId);
		return finder;
	}

	@Override
	public List<ILiveManager> getILiveManagerByEnterpriseId(Integer enterpriseId) {
		Finder finder = Finder.create("from ILiveManager where enterPrise.enterpriseId=:enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		return this.find(finder);
	}

	@Override
	public ILiveManager getILiveManagerByDXID(String appid) {
		Finder finder = Finder.create("from ILiveManager where dxzyappId = :dxzyappId");
		finder.setParam("dxzyappId", appid);
		@SuppressWarnings("unchecked")
		List<ILiveManager> find = find(finder);
		if (find==null || find.isEmpty()) {
			return null;
		}
		return find.get(0);
	}

	@Override
	public ILiveManager getILiveMangerByPhoneNumber(String appId, String userId) {
		Finder finder = Finder.create("from ILiveManager where zhxyappId = :zhxyappId and zhxyuserId=:zhxyuserId");
		finder.setParam("zhxyappId", appId);
		finder.setParam("zhxyuserId", userId);
		@SuppressWarnings("unchecked")
		List<ILiveManager> find = find(finder);
		if (find==null || find.isEmpty()) {
			return null;
		}
		return find.get(0);
	}

	@Override
	public ILiveManager getILiveMangerByAppId(String appId) {
		Finder finder = Finder.create("from ILiveManager where enterPrise.appId = :zhxyappId ");
		finder.setParam("zhxyappId", appId);
		@SuppressWarnings("unchecked")
		List<ILiveManager> find = find(finder);
		if (find==null || find.isEmpty()) {
			return null;
		}
		return find.get(0);
	}
}
