package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLiangNumber;

public interface ILiveLiangNumberDao {
	public Pagination getPage(Integer id, Integer status,int pageNo, int pageSize);

	public ILiveLiangNumber findById(Integer id);

	public ILiveLiangNumber save(ILiveLiangNumber bean);

	public ILiveLiangNumber deleteById(Integer id);

	public void update(ILiveLiangNumber bean);
	
	public ILiveLiangNumber findByNumber(Integer number);
	
	public ILiveLiangNumber updateByUpdater(Updater<ILiveLiangNumber> updater);


}