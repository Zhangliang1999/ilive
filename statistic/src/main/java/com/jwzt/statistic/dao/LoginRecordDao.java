package com.jwzt.statistic.dao;

import java.util.List;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.LoginRecord;

public interface LoginRecordDao {

	Pagination getPageByUserId(Long userId,Integer pageNo,Integer pageSize);
	
	void save(LoginRecord record);
	
	List<LoginRecord> getListByUserId(Long userId,Integer limit);
}
