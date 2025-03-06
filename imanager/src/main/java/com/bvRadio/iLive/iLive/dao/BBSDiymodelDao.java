package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;

public interface BBSDiymodelDao {
	public Pagination getPage(int pageNo, int pageSize);

	public List<BBSDiymodel> getList();

	public List<BBSDiymodel> getListByDiyformId(Integer diyformId);

	public BBSDiymodel findById(Integer id);

	public BBSDiymodel save(BBSDiymodel bean);

	public BBSDiymodel updateByUpdater(Updater<BBSDiymodel> updater);

	public BBSDiymodel deleteById(Integer id);

	public List<BBSDiymodel> deleteByDiyformId(Integer diyformId);

	public void update(BBSDiymodel bean);
}