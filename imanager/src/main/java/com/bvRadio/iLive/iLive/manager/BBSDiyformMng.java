package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;

public interface BBSDiyformMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public Pagination getaVotePage(String voteName,Integer pageNo, Integer pageSize);

	public List<BBSDiyform> getList();

	public BBSDiyform findById(Integer id);

	public BBSDiyform save(BBSDiyform bbsDiyform);

	public BBSDiyform save(BBSDiyform bbsDiyform, List<BBSDiymodel> bbsDiymodelList);

	public BBSDiyform update(BBSDiyform bbsDiyform, List<BBSDiymodel> bbsDiymodelList);

	public BBSDiyform deleteById(Integer id);
	
	public BBSDiyform deleteVoteById(Integer id);

	public BBSDiyform[] deleteByIds(Integer[] ids);

	public BBSDiyform getDiyfromById(int parseInt);

}