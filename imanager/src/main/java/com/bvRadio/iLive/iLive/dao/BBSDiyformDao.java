package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;

public interface BBSDiyformDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public Pagination getPageByParam(String voteName,Integer pageNo, Integer pageSize);

	public List<BBSDiyform> getList();

	public BBSDiyform findById(Integer id);

	public BBSDiyform save(BBSDiyform bean);

	public BBSDiyform updateByUpdater(Updater<BBSDiyform> updater);

	public BBSDiyform deleteById(Integer id);

	public void update(BBSDiyform bean);
}