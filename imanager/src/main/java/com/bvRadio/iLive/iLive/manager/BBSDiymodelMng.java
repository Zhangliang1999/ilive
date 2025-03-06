package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;

public interface BBSDiymodelMng {
	public Pagination getPage(int pageNo, int pageSize);

	public List<BBSDiymodel> getList();

	public List<BBSDiymodel> getListByDiyformId(Integer diyformId);

	public BBSDiymodel findById(Integer id);

	public BBSDiymodel save(BBSDiymodel bbsDiymodel);

	public BBSDiymodel update(BBSDiymodel bean);

	public BBSDiymodel deleteById(Integer id);

	public List<BBSDiymodel> deleteByDiyformId(Integer diyformId);

	public BBSDiymodel[] deleteByIds(Integer[] ids);

}