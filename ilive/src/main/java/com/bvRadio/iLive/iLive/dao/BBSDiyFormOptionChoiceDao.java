package com.bvRadio.iLive.iLive.dao;


import java.math.BigInteger;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;

public interface BBSDiyFormOptionChoiceDao {

	
	/**
	 * 查询文件分页
	 * @param sqlParam 查询参数 
	 * @param pageNo 页码
	 * @param pageSize 条数
	 * @return
	 */
	public Pagination getMedialFilePage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);
	
	
	/**
	 * 查询用户数量
	 * @param sqlParam
	 * @return
	 */
	public BigInteger selectUserCount(Map<String, Object> sqlParam);

	
}
