package com.bvRadio.iLive.manager.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.manager.dao.FankongDao;
import com.bvRadio.iLive.manager.entity.CSAndYellow;
import com.bvRadio.iLive.manager.manager.FankongMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class FankongMngImpl implements FankongMng{

	@Autowired
	private FankongDao fankongDao;
	
	@Override
	public List<CSAndYellow> getList(Integer roomId) {
		return fankongDao.getList(roomId);
	}

	@Override
	public CSAndYellow getById(String id) {
		return fankongDao.getById(id);
	}

	@Override
	public Pagination getPage(Integer roomId, Timestamp checkTime, Integer monitorLevel, Integer pageNo,
			Integer pageSize) {
		return fankongDao.getPage(roomId, checkTime, monitorLevel, pageNo, pageSize);
	}

	@Override
	public void update(CSAndYellow csAndYellow) {
		fankongDao.update(csAndYellow);
	}

	@Override
	public boolean ignoreMany(String str) {
		JSONArray arr = JSONArray.fromObject(str);
		for(int i=0;i<arr.size();i++) {
			JSONObject obj = arr.getJSONObject(i);
			CSAndYellow cs = fankongDao.getById(obj.getString("id"));
			cs.setMonitorLevel(3);
			fankongDao.update(cs);
		}
		return true;
	}

	@Override
	public Pagination getPage(Integer roomId, Timestamp startTime, Timestamp endTime, Integer type1, Integer type2,
			Integer pageNo, Integer pageSize) {
		return fankongDao.getPage(roomId, startTime, endTime, type1, type2, pageNo, pageSize);
	}

}
