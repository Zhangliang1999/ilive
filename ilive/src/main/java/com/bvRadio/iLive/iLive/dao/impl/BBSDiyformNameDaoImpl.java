package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.BBSDiyformNameDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyformName;

public class BBSDiyformNameDaoImpl extends HibernateBaseDao<BBSDiyformName, Integer> 
	implements BBSDiyformNameDao {

	@Override
	public void save(BBSDiyformName bbsDiyformName) {
		this.getSession().save(bbsDiyformName);
	}

	@Override
	public BBSDiyformName get(Integer bbsDiyformId, Integer updateMark) {
		Finder finder = Finder.create("from BBSDiyformName where diyformId = :diyformId and updateMark = :updateMark");
		finder.setParam("diyformId", bbsDiyformId);
		finder.setParam("updateMark", updateMark);
		@SuppressWarnings("unchecked")
		List<BBSDiyformName> find = find(finder);
		if (find == null || find.isEmpty()) {
			return null;
		}
		return (BBSDiyformName) find.get(0);
	}

	@Override
	public BBSDiyformName getLast(Integer bbsDiyformId) {
		Finder finder = Finder.create("from BBSDiyformName where diyformId = :diyformId order by updateMark");
		finder.setParam("diyformId", bbsDiyformId);
		@SuppressWarnings("unchecked")
		List<BBSDiyformName> find = find(finder);
		if (find == null || find.isEmpty()) {
			return null;
		}
		return find.get(0);
	}

	@Override
	public void update(BBSDiyformName bbsDiyformName) {
		this.getSession().update(bbsDiyformName);
	}

	@Override
	protected Class<BBSDiyformName> getEntityClass() {
		return BBSDiyformName.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BBSDiyformName> getListByFormId(Integer formId) {
		Finder finder = Finder.create("from BBSDiyformName where diyformId = :diyformId order by updateMark");
		finder.setParam("diyformId", formId);
		return find(finder);
	}

}
