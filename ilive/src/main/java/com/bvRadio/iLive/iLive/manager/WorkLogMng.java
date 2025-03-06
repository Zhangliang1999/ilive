package com.bvRadio.iLive.iLive.manager;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.iLive.entity.WorkLog;

public interface WorkLogMng {

	List<WorkLog> listByParams(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime);
	
	String save(final WorkLog bean);
}
