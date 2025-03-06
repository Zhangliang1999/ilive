package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ReportAndComplain;

public interface ReportAndComplainService {
	Long save(ReportAndComplain reportAndComplain);
	
	void delete(Long id);
	
	void update(ReportAndComplain reportAndComplain);
	
	ReportAndComplain getById(Long id);
	
	Pagination getPage(Long id,Long username,Integer roomId,Integer type,Integer pageNo,Integer pageSize);
}
