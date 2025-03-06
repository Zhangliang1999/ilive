package com.bvRadio.iLive.iLive.manager;

import java.math.BigInteger;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;

public interface BBSDiyFormOptionChoiceMng {
	public Pagination getMedialFilePage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);

	public BigInteger selectUserCount(Map<String, Object> sqlParam);

}