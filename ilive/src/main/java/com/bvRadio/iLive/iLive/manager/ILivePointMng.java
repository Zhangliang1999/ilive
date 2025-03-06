package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILivePoint;

public interface ILivePointMng {
	public Pagination getPage(int pageNo, int pageSize);

	public List<ILivePoint> getList();

	public ILivePoint findById(Integer id);

	public ILivePoint save(ILivePoint bean);

	public ILivePoint update(ILivePoint bean);

	public ILivePoint deleteById(Integer id);

	public ILivePoint[] deleteByIds(Integer[] ids);
	
	public ILivePoint findDefaultPoint();

}