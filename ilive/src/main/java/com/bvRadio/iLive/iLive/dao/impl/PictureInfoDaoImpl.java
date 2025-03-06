package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.PictureInfoDao;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.PictureInfo;

@Repository
public class PictureInfoDaoImpl extends HibernateBaseDao<PictureInfo, Long> implements PictureInfoDao {

	@Override
	public Pagination getPage(String name,Integer enterpriseId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from PictureInfo where enterpriseId = :enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		if(name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" order by createTime DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void save(PictureInfo pictureInfo) {
		this.getSession().save(pictureInfo);
	}

	@Override
	public void update(PictureInfo pictureInfo) {
		this.getSession().update(pictureInfo);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from PictureInfo where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id",id);
		query.executeUpdate();
	}

	@Override
	protected Class<PictureInfo> getEntityClass() {
		return PictureInfo.class;
	}

	@Override
	public PictureInfo getById(Long id) {
		return this.get(id);
	}

	@Override
	public Pagination getCollaborativePage(String name, Integer pageNo,
			Integer pageSize, Long userId) {
		Finder finder = Finder.create("from PictureInfo");
		finder.append(" where 1=1");
		if(name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" and userId=:userId");
		finder.setParam("userId", userId);
		finder.append(" order by createTime DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<PictureInfo> getListByEnterpriseId(Integer enterpriseId) {
		String hql = "from PictureInfo where enterpriseId =:enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		@SuppressWarnings("unchecked")
		List<PictureInfo> list = query.list();
		return list;
	}

}
