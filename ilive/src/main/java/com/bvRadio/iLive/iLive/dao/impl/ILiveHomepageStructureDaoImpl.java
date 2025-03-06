package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveHomepageStructureDao;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructure;

@Repository
public class ILiveHomepageStructureDaoImpl  extends HibernateBaseDao<ILiveHomepageStructure, Integer> implements ILiveHomepageStructureDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveHomepageStructure> getStructureByEnterprise(Integer enterpriseId) {
		String hql = "from ILiveHomepageStructure where enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		return query.list();
	}

	@Override
	protected Class<ILiveHomepageStructure> getEntityClass() {
		return ILiveHomepageStructure.class;
	}

	@Override
	public void updateStructureNum(Integer showNum, Integer policy,Integer structureId,Integer enterpriseId) {
		String hql = "update ILiveHomepageStructure set showNum = :showNum ,policy = :policy where structureId = :structureId and enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("showNum",showNum);
		query.setParameter("policy",policy);
		query.setParameter("structureId", structureId);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveHomepageStructure getStructureById(Integer structureId,Integer enterpriseId) {
		String hql = "from ILiveHomepageStructure where structureId = :structureId and enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("structureId", structureId);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveHomepageStructure> list = query.list();
		if(list.size()==0) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public void updaeStructureContentType(Integer showContentType, Integer structureId, Integer enterpriseId) {
		String hql = "update ILiveHomepageStructure set showContentType = :showContentType where structureId = :structureId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("showContentType",showContentType);
		query.setParameter("structureId", structureId);
		query.executeUpdate();
	}

	@Override
	public void deleteStructure(Integer enterpriseId) {
		this.getSession().clear();
		String hql = "delete ILiveHomepageStructure where enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}

	@Override
	public void saveStructure(ILiveHomepageStructure iLiveHomepageStructure) {
		this.getSession().save(iLiveHomepageStructure);
	}

	@Override
	public ILiveHomepageStructure getById(Integer id) {
		return this.get(id);
	}


}
