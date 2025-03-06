package com.bvRadio.iLive.iLive.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseFansDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.entity.ILiveThematic;

@Repository
public class ILiveEnterpriseFansDaoImpl extends HibernateBaseDao<ILiveEnterpriseFans, Integer>
		implements ILiveEnterpriseFansDao {

	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select bean from ILiveEnterpriseFans bean where  isBlacklist = null");
		if (queryNum != null) {
			finder.append(" and bean.userName like :userName");
			finder.setParam("userName", "%" + queryNum + "%");
		}
		finder.append(" order by bean.userId Desc");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public Pagination getPageBlack(String queryNum, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select bean from ILiveEnterpriseFans bean where isDel = 0 and isBlacklist = 1");
		if (queryNum != null) {
			finder.append(" and bean.userName like :userName");
			finder.setParam("userName", "%" + queryNum + "%");
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	protected Class<ILiveEnterpriseFans> getEntityClass() {
		return ILiveEnterpriseFans.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveEnterpriseFans findEnterpriseFans(Integer enterpriseId, Long userId) {
		String hql = "from ILiveEnterpriseFans where enterpriseId = :enterpriseId and userId = :userId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("userId", userId);
		List<ILiveEnterpriseFans> list = query.list();
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public void addEnterpriseConcern(ILiveEnterpriseFans fans) {
		this.getSession().save(fans);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isExist(Integer enterpriseId, Long userId) {
		String hql = "from ILiveEnterpriseFans where enterpriseId =:enterpriseId and userId =:userId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("userId", userId);
		List<ILiveEnterpriseFans> list = query.list();
		if (list == null || list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void delFans(Long id) {
		String hql = "update ILiveEnterpriseFans set isBlacklist = '0' where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public void letblack(Long id) {
		String hql = "update ILiveEnterpriseFans set isBlacklist = '1' where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public int getFansNum(Integer enterpriseId) {
		String hql = "select count(fans) from ILiveEnterpriseFans fans where enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		Long num = (Long) query.uniqueResult();
		if (num == null) {
			return 0;
		} else {
			int n = num.intValue();
			return n;
		}

	}

	@Override
	public void deleteEnterpriseConcern(ILiveEnterpriseFans fans) {
		this.getSession().delete(fans);
	}

	@Override
	public List<Integer> getMyEnterprise(Long userId) {
		Finder finder = Finder
				.create("from ILiveEnterpriseFans where userId=:userId and forbidState=0  order by topFlag desc,concernTime desc");
		finder.setParam("userId", userId);
		List<ILiveEnterpriseFans> findEnterprises = this.find(finder);
		List<Integer> list = new ArrayList<>();
		if (findEnterprises != null && !findEnterprises.isEmpty()) {
			for (ILiveEnterpriseFans fans : findEnterprises) {
				list.add(fans.getEnterpriseId());
			}
		}
		return list;
	}

	@Override
	public Pagination getIliveEnterPriseByUserId(Long userId, int pageNo, int pageSize) {
		Finder finder = Finder
				.create("from ILiveEnterpriseFans where userId=:userId order by topFlag desc ,concernTime desc ");
		finder.setParam("userId", userId);
		Pagination find = this.find(finder, pageNo, pageSize);
		return find;
	}

	@Override
	public Long getTotalConcernNum(Integer enterpriseId) {
		// TODO Auto-generated method stub
		String hql = "select count(bean) from ILiveEnterpriseFans bean where bean.enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		Long num = (Long) query.uniqueResult();
		return num;
	}

	@Override
	public void updateEnterFans(ILiveEnterpriseFans entFans) {
		this.getSession().update(entFans);
	}

	@Override
	public List<ILiveEnterpriseFans> getEnterpriseListByTop(Long userId) {
		Finder create = Finder.create("from ILiveEnterpriseFans where userId=:userId and topFlag=1 ");
		create.setParam("userId", userId);
		List<ILiveEnterpriseFans> find = this.find(create);
		return find;
	}

//<<<<<<< .mine
//	@Override
//	public List<Long> getListByEnterpriseId(Integer enterpriseId) {
//		String hql = "from ILiveThematic where enterpriseId = :enterpriseId";
//		Query query = this.getSession().createQuery(hql);
//		query.setParameter("enterpriseId", enterpriseId);
//		List<Long> list = query.list();
//		return list;
//	}
//
//||||||| .r42641
//=======
	@Override
	public List<Long> getListByEnterpriseId(Integer enterpriseId) {
		/*Finder create = Finder.create("select userId from ILiveEnterpriseFans where enterpriseId=:enterpriseId");
		create.setParam("enterpriseId",enterpriseId);
		List<ILiveEnterpriseFans> find = this.find(create);
		return find;*/
		
		String hql = "select userId from ILiveEnterpriseFans where enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		@SuppressWarnings("unchecked")
		List<Long> list = query.list();
		return list;
	}

	@Override
	public boolean isblack(long userId, Integer enterpriseId) {
		String hql = "from ILiveEnterpriseFans where enterpriseId =:enterpriseId and userId =:userId and isBlacklist = '1'";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("userId", userId);
		List<ILiveEnterpriseFans> list = query.list();
		if (list == null || list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Pagination getPage(String queryNum, Integer enterpriseId, Integer pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveEnterpriseFans bean where  enterpriseId =:enterpriseId and  isBlacklist = null");
		finder.setParam("enterpriseId", enterpriseId);
		if (queryNum != null&&!"".equals(queryNum)) {
			finder.append(" and bean.userId in(select userId from ILiveManager where nailName like:nailName  and isDel=0)");
			finder.setParam("nailName", "%"+queryNum+"%" );
		}else{
			finder.append(" and bean.userId in(select userId from ILiveManager where  isDel=0)");
		}
		finder.append(" order by bean.userId Desc");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public Pagination getPageBlack(String queryNum, Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select bean from ILiveEnterpriseFans bean where enterpriseId =:enterpriseId and isDel = 0 and isBlacklist = 1");
		finder.setParam("enterpriseId", enterpriseId);
		if (queryNum != null&&!"".equals(queryNum)) {
			finder.append(" and bean.userName like :userName");
			finder.setParam("userName", "%" + queryNum + "%");
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public Pagination getIliveEnterPriseByUserId(Long userId, Integer pageNo, Integer pageSize, String keyWord) {
		Finder finder = Finder
				.create("from ILiveEnterpriseFans where userId=:userId and enterpriseId in(select enterpriseId from ILiveEnterprise where enterpriseName like:enterpriseName) order by topFlag desc ,concernTime desc ");
		finder.setParam("userId", userId);
		finder.setParam("enterpriseName", "%"+keyWord+"%");
		Pagination find = this.find(finder, pageNo, pageSize);
		return find;
	}
	@Override
	public Pagination getIliveEnterPriseByUserId1(Long userId, Integer pageNo, Integer pageSize, String keyWord) {
		Finder finder = Finder
				.create("from ILiveEnterpriseFans where userId=:userId and enterpriseId in(select enterpriseId from ILiveEnterprise where enterpriseName like:enterpriseName and (privacy=0 or privacy is null)) order by topFlag desc ,concernTime desc ");
		finder.setParam("userId", userId);
		finder.setParam("enterpriseName", "%"+keyWord+"%");
		Pagination find = this.find(finder, pageNo, pageSize);
		return find;
	}
//>>>>>>> .r42762
}
