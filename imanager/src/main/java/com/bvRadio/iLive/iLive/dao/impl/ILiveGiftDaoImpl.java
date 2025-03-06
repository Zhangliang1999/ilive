package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveGiftDao;
import com.bvRadio.iLive.iLive.entity.ILiveGift;

public class ILiveGiftDaoImpl extends HibernateBaseDao<ILiveGift, Integer> implements ILiveGiftDao  {

	@Override
	public void save(ILiveGift iLiveGift) {
		getSession().save(iLiveGift);
		getSession().flush();
	}

	@Override
	public ILiveGift delete(Integer id) {
		ILiveGift entity = super.get(id);
		getSession().delete(entity);
		getSession().flush();
		return entity;
	}
	
	public Pagination getPage(int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveGift bean");
		finder.append(" order by bean.id desc");
		return find(finder, pageNo, pageSize);
	}
	
	@Override
	protected Class<ILiveGift> getEntityClass() {
		return ILiveGift.class;
	}
	

	@Override
	public ILiveGift findById(Integer id) {
		ILiveGift entity = get(id);
		return entity;
	}

	@Override
	public void update(ILiveGift bean) {
			getSession().update(bean);
			getSession().flush();
	}

	@Override
	public void deleteByIds(Integer[] ids) {
		ILiveGift[] gifts = null;
		if (null != ids) {
			gifts = new ILiveGift[ids.length];
			for (int i = 0; i < ids.length; i++) {
				gifts[i] = delete(ids[i]);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveGift> selectILiveGiftList() {
		Finder finder = Finder.create("select bean from ILiveGift bean");
		List<ILiveGift> find = find(finder);
		return find;
	}

}
