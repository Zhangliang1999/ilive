package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.TerminalUserDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

@Repository
public class TerminalUserDaoImpl extends HibernateBaseDao<ILiveManager, Integer> implements TerminalUserDao {

	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select bean from ILiveManager bean where bean.isDel = 0 order by bean.userId Desc");
		if(queryNum!=null) {
			finder.append(" and bean.userName like :userName");
			finder.setParam("userName","%"+queryNum+"%");
		}
		return find(finder,pageNo==null?1:pageNo,pageSize);
	}

	@Override
	protected Class<ILiveManager> getEntityClass() {
		return ILiveManager.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveManager getById(Long userId) {
		String hql = "from ILiveManager where userId = :userId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		List<ILiveManager> list = query.list();
		if (list.size()==0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public void delUser(Long userId) {
		String hql = "update ILiveManager set isDel = 1 where userId = :userId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.executeUpdate();
	}

	@Override
	public void updateUser(ILiveManager user) {
		StringBuilder hql = new StringBuilder("update ILiveManager set isDel = 0");
		if(user.getNailName()!=null && (!user.getNailName().equals(""))) {
			hql.append(",nailName = '"+user.getNailName()+"'");
		}
		if(user.getBirthday()!=null && (!user.getBirthday().equals(""))) {
			hql.append(",birthday = '"+user.getBirthday()+"'");
		}
		if(user.getConstellatory()!=null && (!user.getConstellatory().equals(""))) {
			hql.append(",constellatory = '"+user.getConstellatory()+"'");
		}
		if(user.getSex()!=null && (!user.getSex().equals(""))) {
			hql.append(",sex = '"+user.getSex()+"'");
		}
		if(user.getEduLevel()!=null && (!user.getEduLevel().equals(""))) {
			hql.append(",eduLevel = '"+user.getEduLevel()+"'");
		}
		if(user.getIncomeLevel()!=null && (!user.getIncomeLevel().equals(""))) {
			hql.append(",incomeLevel = '"+user.getIncomeLevel()+"'");
		}
		if(user.getOccupation()!=null && (!user.getOccupation().equals(""))) {
			hql.append(",occupation = '"+user.getOccupation()+"'");
		}
		hql.append(" where userId = "+user.getUserId()+"");
		// System.out.println(hql.toString());
		Query query = this.getSession().createQuery(hql.toString());
		query.executeUpdate();
	}

}
