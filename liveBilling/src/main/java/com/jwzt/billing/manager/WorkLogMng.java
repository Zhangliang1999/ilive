package com.jwzt.billing.manager;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.bo.WorkLog;

/**
 * @author ysf
 */
public interface WorkLogMng {

	List<WorkLog> listByParams(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime) throws IOException;

	WorkLog save(WorkLog bean) throws IOException;
}
