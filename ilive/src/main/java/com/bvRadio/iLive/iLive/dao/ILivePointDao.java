package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILivePoint;

public interface ILivePointDao {

	public List<ILivePoint> getList();

	public Pagination getPage(int pageNo, int pageSize);

	public ILivePoint findById(Integer id);

	public ILivePoint save(ILivePoint bean);

	public ILivePoint update(ILivePoint bean);

	public ILivePoint deleteById(Integer id);

	public ILivePoint findDefaultPoint();
}
