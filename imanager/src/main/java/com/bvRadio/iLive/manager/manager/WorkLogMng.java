package com.bvRadio.iLive.manager.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.entity.WorkLog;

public interface WorkLogMng {

	List<WorkLog> listByParams(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime,Integer pageNo,Integer pageSize);
	Map<?,?> getList(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime,Integer pageNo,Integer pageSize);
	
	Pagination getPage(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime);
	
	String save(final WorkLog bean);
}
