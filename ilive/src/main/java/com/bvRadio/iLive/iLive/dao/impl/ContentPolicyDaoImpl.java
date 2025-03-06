package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ContentPolicyDao;
import com.bvRadio.iLive.iLive.entity.ContentPolicy;

@Repository
public class ContentPolicyDaoImpl extends HibernateBaseDao<ContentPolicy, Long> implements ContentPolicyDao {

	@SuppressWarnings("unchecked")
	@Override
	public ContentPolicy getPolicy() {
		String hql = "from ContentPolicy";
		Query query = this.getSession().createQuery(hql);
		List<ContentPolicy> list = query.list();
		if(list.size() == 0) {
			return null;
		}else {
			return list.get(0);
		}
		
	}

	@Override
	protected Class<ContentPolicy> getEntityClass() {
		return ContentPolicy.class;
	}

	@Override
	public void updatePolicy(ContentPolicy policy) {
		StringBuilder hql = new StringBuilder("update ContentPolicy set ");
		if(policy.getPolicy() != null) {
			hql.append("policy = '"+policy.getPolicy()+"'");
		}
		if(policy.getNum() !=null) {
			hql.append(" ,num = '"+policy.getNum()+"'");
		}
		hql.append(" where shows = :shows");
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("shows", policy.getShows());
		query.executeUpdate();
	}

	@Override
	public void save(ContentPolicy policy) {
		this.getSession().save(policy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContentPolicy getPolicyByShows(Integer shows,Integer enterpriseId) {
		String hql = "from ContentPolicy where shows = :shows and enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("shows", shows);
		query.setParameter("enterpriseId", enterpriseId);
		List<ContentPolicy> list = query.list();
		if(list.size() == 0) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContentPolicy gettitle2(Integer shows) {
		String hql = "from ContentPolicy where shows = :shows";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("shows", shows);
		List<ContentPolicy> list = query.list();
		if(list.size()==0) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public void saveTitle2(ContentPolicy policy) {
		this.getSession().save(policy);
	}

	@Override
	public void updateTitle2(String title2,Integer shows,Integer enterpriseId) {
		String hql = "update ContentPolicy set title2 = :title2 where shows = :shows and enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("title2", title2);
		query.setParameter("shows", shows);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
		
			
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContentPolicy gettitle4(Integer shows) {
		String hql = "from ContentPolicy where shows = 4";
		Query query = this.getSession().createQuery(hql);
		List<ContentPolicy> list = query.list();
		if(list.size()==0) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public void saveLink(ContentPolicy policy) {
		this.getSession().save(policy);
	}

	@Override
	public void updateLink(String link, String linkName,Integer enterpriseId,Integer shows) {
		String hql = "update ContentPolicy set link = :link , linkName = :linkName where shows = :shows and enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("link", link);
		query.setParameter("linkName", linkName);
		query.setParameter("shows", shows);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}

	@Override
	public void deletelink(Integer enterpriseId,Integer structureId) {
		String hql = "update ContentPolicy set link = '' , linkName = '' where structureId = :structureId and enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("structureId", structureId);
		query.executeUpdate();
	}

	@Override
	public void updateImg(String imgurl,Integer enterpriseId) {
		String hql = "update ContentPolicy set imgurl = :imgurl where shows = 1 and enterpriseId = :enterpriseId";
		Query query  = this.getSession().createQuery(hql);
		query.setParameter("imgurl", imgurl);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}

	@Override
	public void deleteimg(Integer enterpriseId,Integer structureId) {
		String hql = "update ContentPolicy set imgurl = '' where shows = 1 and enterpriseId =:enterpriseId";
		Query query  = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}
	
}
