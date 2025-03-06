package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSubAccountDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

@Repository
public class ILiveSubAccountDaoImpl extends HibernateBaseDao<ILiveManager, Long> implements ILiveSubAccountDao {

	@Override
	public Pagination selectILiveManagerPage(Integer pageNo, Integer pageSize,
			Integer enterpriseId) throws Exception {
		Finder finder = Finder.create("from ILiveManager bean where bean.enterPrise.enterpriseId=:enterpriseId and bean.isDel=0 and bean.level=:level order by bean.userId Desc");
		finder.setParam("enterpriseId", enterpriseId);
		finder.setParam("level", ILiveManager.USER_LEVEL_SUB);
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<ILiveManager> getEntityClass() {
		return ILiveManager.class;
	}

	@Override
	public void addILiveSubAccountMng(ILiveManager manager) throws Exception {
		getSession().save(manager);
	}

	@Override
	public List<ILiveManager> getILiveManagerPage(Integer enterpriseId,String roomId,String userId) {
		String hql = "from ILiveManager bean where bean.enterPrise.enterpriseId="+enterpriseId+"and bean.isDel=0 and (bean.userId not  in ( select subAccountId from IliveSubRoom where liveRoomId="+roomId+" and subAccountId!="+userId+")) and bean.level =3";
		Query query=getSession().createQuery(hql);
		List<ILiveManager> list= query.list();
		return  list;
	}

	@Override
	public List<ILiveManager> getILiveManagerPage(Integer enterpriseId) {
		String hql = "from ILiveManager bean where bean.enterPrise.enterpriseId=:enterpriseId and bean.isDel=0 and bean.level=3 order by bean.userId Desc";
		Query query=getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveManager> list= query.list();
		return  list;
	}

}
