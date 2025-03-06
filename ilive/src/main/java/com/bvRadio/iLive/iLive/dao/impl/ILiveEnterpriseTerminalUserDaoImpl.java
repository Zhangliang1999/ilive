package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseTerminalUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseTerminalUser;

@Repository
public class ILiveEnterpriseTerminalUserDaoImpl extends HibernateBaseDao<ILiveEnterpriseTerminalUser, Long>
		implements ILiveEnterpriseTerminalUserDao {

	@Override
	public Pagination getPage(String queryNum,Integer fanstype, Integer pageNo, Integer pageSize, Integer enterpriseId) {
		Finder finder = Finder
				.create("select bean from ILiveEnterpriseTerminalUser bean where isDel != 1 ");
		if(queryNum!=null&&!"".equals(queryNum)) {
			finder.append(" and bean.nailName like :nailName");
			finder.setParam("nailName", "%"+queryNum+"%");
		}
		if(fanstype!=null) {
			finder.append(" and bean.fansType >= :fansType");
			finder.setParam("fansType", fanstype);
		}
		if(enterpriseId!=null){
			finder.append(" and bean.enterpriseId=:enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}
	
	@Override
	public Pagination getPageBlackList(Integer enterpriseId,String queryNum, Integer pageNo, Integer pageSize){
		Finder finder = Finder
				.create("select bean from ILiveEnterpriseTerminalUser bean where isDel != 1 and isBlacklist = 1");
		if(enterpriseId!=null){
			finder.append(" and bean.enterpriseId=:enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		finder.append(" order by bean.userId Desc");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	protected Class<ILiveEnterpriseTerminalUser> getEntityClass() {
		return ILiveEnterpriseTerminalUser.class;
	}

	@Override
	public ILiveEnterpriseTerminalUser saveTerminaluser(ILiveEnterpriseTerminalUser user) {
		this.getSession().save(user);
		return user;
	}

	@Override
	public ILiveEnterpriseTerminalUser getIliveEnterpriseTerminalByQuery(Long userId, Integer enterpriseId) {
		ILiveEnterpriseTerminalUser terminalUser = (ILiveEnterpriseTerminalUser) this
				.findUnique("from ILiveEnterpriseTerminalUser where userId=? and enterpriseId=?", userId, enterpriseId);
		return terminalUser;
	}

	@Override
	public void updateIliveEnterpriseTerminalUser(ILiveEnterpriseTerminalUser terminalUser) {
		this.getSession().update(terminalUser);
	}

	@Override
	public void letbuserblack(Long id) {
		String hql = "update ILiveEnterpriseTerminalUser set isBlacklist = 1 where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public void removeTerminaluser(Long id) {
		// TODO Auto-generated method stub
		String hql = "update ILiveEnterpriseTerminalUser set isDel = 1 where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterpriseTerminalUser> queryList(String queryNum, Integer fanstype) {
		StringBuilder hql = new StringBuilder("from ILiveEnterpriseTerminalUser where isDel != 1 and isBlacklist != 1");
		if(queryNum!=null) {
			hql.append(" and nailName like '%"+queryNum+"%'");
		}
		if(fanstype!=null) {
			hql.append(" and fansType>="+fanstype);
		}
		Query query = this.getSession().createQuery(hql.toString());
		List<ILiveEnterpriseTerminalUser> list = query.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterpriseTerminalUser> queryList(String queryNum, Integer fanstype, Integer enterpriseId) {
		StringBuilder hql = new StringBuilder("from ILiveEnterpriseTerminalUser where isDel != 1 and isBlacklist != 1 and enterpriseId=:enterpriseId");
		if(queryNum!=null) {
			hql.append(" and nailName like '%"+queryNum+"%'");
		}
		if(fanstype!=null) {
			hql.append(" and fansType>="+fanstype);
		}
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveEnterpriseTerminalUser> list = query.list();
		return list;
	}

	@Override
	public void removeBlackuser(Long id) {
		// TODO Auto-generated method stub
		String hql = "update ILiveEnterpriseTerminalUser set isBlacklist = 0 where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

}
