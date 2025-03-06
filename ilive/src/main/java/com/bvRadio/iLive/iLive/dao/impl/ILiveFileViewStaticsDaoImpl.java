package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveFileViewStaticsDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileViewStatics;

public class ILiveFileViewStaticsDaoImpl extends HibernateBaseDao<ILiveFileViewStatics, Long> implements ILiveFileViewStaticsDao {

	
	
	@Override
	protected Class<ILiveFileViewStatics> getEntityClass() {
		return ILiveFileViewStatics.class;
	}
	
	
	/**
	 * 返回对象
	 * @param fileId
	 * @return
	 */
	public ILiveFileViewStatics getViewNum(Long fileId) {
		ILiveFileViewStatics iLiveFileViewStatics = this.get(fileId);
		return iLiveFileViewStatics;
	}


	
	/**
	 * 新增数量
	 */
	@Override
	public void addViewNum(ILiveFileViewStatics viewStatics) {
		this.getSession().update(viewStatics);
	}
	
	
	/**
	 * 初始化人数
	 * @param viewStatics
	 */
	public void initViewNum(ILiveFileViewStatics viewStatics) {
		this.getSession().save(viewStatics);
	}

}
