package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ContentSelectPublishDao;
import com.bvRadio.iLive.iLive.entity.ContentSelect;
import com.bvRadio.iLive.iLive.entity.ContentSelectPublish;

@Repository
public class ContentSelectPublishDaoImpl extends HibernateBaseDao<ContentSelectPublish, Integer> implements ContentSelectPublishDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentSelectPublish> getPublishContentByEnterprise(Integer enterpriseId) {
		this.getSession().clear();
		String hql = "from ContentSelectPublish where enterpriseId = :enterpriseId order by structureId,indexs";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ContentSelectPublish> list = query.list();
		return list;
	}

	@Override
	public void savePublishContent(ContentSelect contentSelectPublish) {
		this.getSession().clear();
		ContentSelectPublish con = new ContentSelectPublish();
		con.setId(contentSelectPublish.getId());
		con.setEnterpriseId(contentSelectPublish.getEnterpriseId());
		con.setStructureId(contentSelectPublish.getStructureId());
		con.setContentType(contentSelectPublish.getContentType());
		con.setContentId(contentSelectPublish.getContentId());
		con.setContentName(contentSelectPublish.getContentName());
		con.setContentImg(contentSelectPublish.getContentImg());
		con.setContentBrief(contentSelectPublish.getContentBrief());
		con.setIndexs(contentSelectPublish.getIndexs());
		con.setLiveStatus(contentSelectPublish.getLiveStatus());
		con.setUrlName(contentSelectPublish.getUrlName());
		con.setContentUrl(contentSelectPublish.getContentUrl());
		this.getSession().save(con);
	}

	@Override
	public void deletePublishContent(Integer enterpriseId) {
		this.getSession().clear();
		String hql = "delete from ContentSelectPublish where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}

	@Override
	protected Class<ContentSelectPublish> getEntityClass() {
		return ContentSelectPublish.class;
	}

	@Override
	public ContentSelectPublish getById(Integer id) {
		return this.get(id);
	}

	@Override
	public void deleteContentPublish(Integer contentId) {
		String hql = "delete from ContentSelectPublish where contentId=:contentId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("contentId", contentId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContentSelectPublish getByContentId(Integer contentId, Integer contentType) {
		this.getSession().clear();
		String hql = "from ContentSelectPublish where contentId = :contentId and contentType=:contentType";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("contentId", contentId);
		query.setParameter("contentType", contentType);
		List<ContentSelectPublish> list = query.list();
		if(list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	@Override
	public void update(ContentSelectPublish contentSelectPublish) {
		this.getSession().update(contentSelectPublish);
	}

}
