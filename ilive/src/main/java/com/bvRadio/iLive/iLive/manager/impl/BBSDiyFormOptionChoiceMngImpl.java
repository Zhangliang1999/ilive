package com.bvRadio.iLive.iLive.manager.impl;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBSDiyFormOptionChoiceDao;
import com.bvRadio.iLive.iLive.manager.BBSDiyFormOptionChoiceMng;

@Service
@Transactional
public class BBSDiyFormOptionChoiceMngImpl implements BBSDiyFormOptionChoiceMng {
	@Autowired
	private BBSDiyFormOptionChoiceDao bBSDiyFormOptionChoiceDao;

	@Override
	public Pagination getMedialFilePage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		return bBSDiyFormOptionChoiceDao.getMedialFilePage(sqlParam, pageNo, pageSize);
	}

	@Override
	public BigInteger selectUserCount(Map<String, Object> sqlParam) {
		return bBSDiyFormOptionChoiceDao.selectUserCount(sqlParam);
	}
}
