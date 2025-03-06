package com.jwzt.statistic.manager;

import java.util.List;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.LoginRecord;

public interface LoginRecordMng {
	Pagination getPageByUserId(Long userId,Integer pageNo,Integer pageSize);
	
	void save(LoginRecord record);
	
	List<LoginRecord> getListByUserId(Long userId,Integer limit);
}
