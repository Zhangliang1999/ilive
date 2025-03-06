package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLiangNumber;

public interface ILiveLiangNumberMng {
	public Pagination getPage(Integer id, Integer status,int pageNo, int pageSize);

	public ILiveLiangNumber findById(Integer id);

	public ILiveLiangNumber save(ILiveLiangNumber bean);

	public ILiveLiangNumber deleteById(Integer id);

	public ILiveLiangNumber update(ILiveLiangNumber bean);
	
	public boolean isExistNumber(Integer number);
	
	public void init();

}
