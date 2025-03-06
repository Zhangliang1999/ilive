package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveSubLevelDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
@Repository
public class ILiveSubLevelDaoImp  extends HibernateBaseDao<ILiveSubLevel, Long> implements  ILiveSubLevelDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveSubLevel> selectILiveSubById(Long userId) {
		Finder finder = Finder.create("from ILiveSubLevel where userId ="+userId);
		return this.find(finder);
	}

	@Override
	protected Class<ILiveSubLevel> getEntityClass() {
		// TODO Auto-generated method stub
		return ILiveSubLevel.class;
	}

	@Override
	public void save(ILiveSubLevel iLiveSubLevel) {
		this.getSession().save(iLiveSubLevel);
		
	}

	@Override
	public Long selectMaxId() {
		String hql = "from ILiveSubLevel  order by id desc";
		Finder finder = Finder.create(hql);
		List<ILiveSubLevel> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Long id=find.get(0).getId();
			return find.get(0).getId()+1;
		}
		return null;
	}

	@Override
	public void delete(Long userId) {
		List iLiveSubLevel = this.find("from ILiveSubLevel where userId=?", userId);
		if(iLiveSubLevel!=null||iLiveSubLevel.size()>0) {
			for(int i=0;i<iLiveSubLevel.size();i++) {
				ILiveSubLevel level=(ILiveSubLevel)iLiveSubLevel.get(i);
				getSession().delete(level);
			}
		}
		
		
	}

	@Override
	public void update(ILiveSubLevel iLiveSubLevel) {
		// TODO Auto-generated method stub
		this.getSession().update(iLiveSubLevel);
	}

	@Override
	public ILiveSubLevel getSubLevel(Long userId) {
		if (userId == null) {
			return null;
		}
		return (ILiveSubLevel) this.findUnique("from ILiveSubLevel where userId=?", userId);
	
	}

	@Override
	public List<ILiveSubLevel> selectIfCan(Long userId, String permission) {
		Finder finder = Finder.create("from ILiveSubLevel where userId ="+userId+" and subTop='"+permission+"'");
		return this.find(finder);
	}
	
}
