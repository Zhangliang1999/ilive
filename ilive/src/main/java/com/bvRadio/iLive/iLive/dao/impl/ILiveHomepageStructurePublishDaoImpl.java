package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveHomepageStructurePublishDao;
import com.bvRadio.iLive.iLive.entity.ContentSelect;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructure;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructurePublish;

public class ILiveHomepageStructurePublishDaoImpl extends HibernateBaseDao<ILiveHomepageStructurePublish, Integer> implements ILiveHomepageStructurePublishDao {

	@Override
	public void deletePublishStructure(Integer enterpriseId) {
		String hql = "delete from ILiveHomepageStructurePublish where enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}

	@Override
	public List<ILiveHomepageStructurePublish> getPublishStructureByEnterprise(Integer enterpriseId) {
		String hql = "from ILiveHomepageStructurePublish where enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveHomepageStructurePublish> list = query.list();
		return list;
	}

	@Override
	protected Class<ILiveHomepageStructurePublish> getEntityClass() {
		return ILiveHomepageStructurePublish.class;
	}

	@Override
	public void savePublishStructure(ILiveHomepageStructure iLiveHomepageStructure) {
		this.getSession().clear();
		ILiveHomepageStructurePublish structurePublish = new ILiveHomepageStructurePublish();
		structurePublish.setId(iLiveHomepageStructure.getId());
		structurePublish.setStructureId(iLiveHomepageStructure.getStructureId());
		structurePublish.setType(iLiveHomepageStructure.getType());
		structurePublish.setEnterpriseId(iLiveHomepageStructure.getEnterpriseId());
		structurePublish.setOrders(iLiveHomepageStructure.getOrders());
		structurePublish.setPolicy(iLiveHomepageStructure.getPolicy());
		structurePublish.setShowContentType(iLiveHomepageStructure.getShowContentType());
		structurePublish.setShowNum(iLiveHomepageStructure.getShowNum());
		this.getSession().save(structurePublish);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveHomepageStructurePublish getPublishStructureById(Integer structureId,Integer enterpriseId) {
		this.getSession().clear();
		String hql = "from ILiveHomepageStructurePublish where structureId = :structureId and enterpriseId=:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("structureId", structureId);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveHomepageStructurePublish> list = query.list();
		if(list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	@Override
	public ILiveHomepageStructurePublish getById(Integer id) {
		return this.get(id);
	}

}
