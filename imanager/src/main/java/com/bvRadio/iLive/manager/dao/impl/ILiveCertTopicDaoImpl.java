package com.bvRadio.iLive.manager.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.manager.dao.ILiveCertTopicDao;
import com.bvRadio.iLive.manager.entity.ILiveCertTopic;

public class ILiveCertTopicDaoImpl extends HibernateBaseDao<ILiveCertTopic, Long> implements ILiveCertTopicDao {

	@Override
	protected Class<ILiveCertTopic> getEntityClass() {
		return ILiveCertTopic.class;
	}

	@Override
	public List<ILiveCertTopic> getCertTopicList(Integer enterpriseId) {
		Finder create = Finder
				.create(" from ILiveCertTopic where enterpriseId=:enterpriseId order by commentTime desc ");
		create.setParam("enterpriseId", enterpriseId);
		List<ILiveCertTopic> find = this.find(create);
		return find;
	}

	@Override
	public boolean addCertTopic(ILiveCertTopic certTopic) {
		this.getSession().save(certTopic);
		return true;
	}

}
