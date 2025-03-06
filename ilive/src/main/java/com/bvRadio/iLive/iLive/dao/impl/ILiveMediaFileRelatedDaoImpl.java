package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileRelatedDao;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileRelated;

@Repository
public class ILiveMediaFileRelatedDaoImpl extends HibernateBaseDao<ILiveMediaFileRelated, String>
		implements ILiveMediaFileRelatedDao {

	@Override
	protected Class<ILiveMediaFileRelated> getEntityClass() {
		return ILiveMediaFileRelated.class;
	}

	@Override
	public ILiveMediaFileRelated save(ILiveMediaFileRelated bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	public ILiveMediaFileRelated deleteById(String id) {
		ILiveMediaFileRelated bean = get(id);
		if (null != bean) {
			getSession().delete(bean);
		}
		return bean;
	}

	@Override
	public Pagination pageByParams(Long mainFileId, int pageNo, int pageSize) {
		Finder finder = createFinderByParams(mainFileId);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMediaFileRelated> listByParams(Long mainFileId) {
		Finder finder = createFinderByParams(mainFileId);
		return find(finder);
	}

	private Finder createFinderByParams(Long mainFileId) {
		Finder finder = Finder.create("select bean from ILiveMediaFileRelated bean ");
		finder.append(" where 1=1 ");
		if (mainFileId != null) {
			finder.append(" and bean.mainFileId=:mainFileId");
			finder.setParam("mainFileId", mainFileId);
		}
		finder.append(" order by orderNum ");
		return finder;
	}

}
