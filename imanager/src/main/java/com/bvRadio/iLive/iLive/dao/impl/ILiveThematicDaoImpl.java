package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveThematicDao;
import com.bvRadio.iLive.iLive.entity.ILiveThematic;

@Repository
public class ILiveThematicDaoImpl extends HibernateBaseDao<ILiveThematic, Long> implements ILiveThematicDao {

	@Override
	public Pagination selectILiveThematicPage(Integer pageNo, Integer pageSize,
			boolean isDelete) throws Exception {
		Finder finder = Finder.create("select bean from ILiveThematic bean");
		finder.append(" where bean.isDelete=:isDelete ");
		finder.setParam("isDelete", isDelete);
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<ILiveThematic> getEntityClass() {
		return ILiveThematic.class;
	}

	@Override
	public void addILiveThematic(ILiveThematic iLiveThematic) throws Exception {
		getSession().save(iLiveThematic);
	}

	@Override
	public void deleteILiveThematicByIsDelete(Long thematicId, boolean isDelete)
			throws Exception {
		ILiveThematic iLiveThematic = get(thematicId);
		iLiveThematic.setIsDelete(isDelete);
		getSession().update(iLiveThematic);
		
	}

	@Override
	public ILiveThematic selectIliveThematicById(Long thematicId)
			throws Exception {
		return get(thematicId);
	}

	@Override
	public void updateILiveThematic(ILiveThematic iLiveThematic)
			throws Exception {
		getSession().update(iLiveThematic);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveThematic> getAllList() {
		String hql = "from ILiveThematic";
		Query query = this.getSession().createQuery(hql);
		List<ILiveThematic> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveThematic> getListByEnterpriseId(Integer enterpriseId) {
		String hql = "from ILiveThematic where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveThematic> list = query.list();
		return list;
	}

}
