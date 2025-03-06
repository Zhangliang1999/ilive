package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveStreamFileDao;
import com.bvRadio.iLive.iLive.dao.PictureInfoDao;
import com.bvRadio.iLive.iLive.entity.ILiveStreamFile;
import com.bvRadio.iLive.iLive.entity.PictureInfo;

@Repository
public class ILiveStreamFileDaoImpl extends HibernateBaseDao<ILiveStreamFile, Long> implements ILiveStreamFileDao {

	@Override
	public Pagination getPage(String name, Integer enterpriseId, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		Finder finder = Finder.create("from ILiveStreamFile where enterpriseId = :enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		if(name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" order by createTime DESC");
		return find(finder, pageNo, pageSize);
	}
	

	@Override
	public void save(ILiveStreamFile iLiveStreamFile) {
		// TODO Auto-generated method stub
		this.getSession().save(iLiveStreamFile);
	}

	@Override
	public void update(ILiveStreamFile iLiveStreamFile) {
		// TODO Auto-generated method stub
		this.getSession().update(iLiveStreamFile);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		String hql = "delete from ILiveStreamFile where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id",id);
		query.executeUpdate();
	}

	@Override
	public ILiveStreamFile getById(Long id) {
		// TODO Auto-generated method stub
		return this.get(id);
	}

	@Override
	public Pagination getCollaborativePage(String name, Integer pageNo, Integer pageSize, Long userId) {
		// TODO Auto-generated method stub
		Finder finder = Finder.create("from ILiveStreamFile");
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
	protected Class<ILiveStreamFile> getEntityClass() {
		// TODO Auto-generated method stub
		return ILiveStreamFile.class;
	}


	@Override
	public List<ILiveStreamFile> getListByUserId(Integer enterpriseId) {
		// TODO Auto-generated method stub
		Query query = this.getSession().createQuery("from ILiveStreamFile where enterpriseId = ?");
		 query.setParameter(0, enterpriseId);
        //2、使用Query对象的list方法得到数据集合
        List<ILiveStreamFile> list = query.list();
		return list;
	}
	
	
}
