package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEstoppelDao;
import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;

@Service
@Transactional
public class ILiveEstoppelMngImpl implements ILiveEstoppelMng {

	@Autowired
	private ILiveEstoppelDao iLiveEstoppelDao;// 禁言管理

	@Override
	public List<ILiveEstoppel> selectILiveEstoppels(Integer roomId) {
		List<ILiveEstoppel> list = new ArrayList<ILiveEstoppel>();
		try {
			list = iLiveEstoppelDao.selectILiveEstoppels(roomId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void insertILiveEstoppel(ILiveEstoppel iLiveEstoppel) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			iLiveEstoppel.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			iLiveEstoppelDao.insertILiveEstoppel(iLiveEstoppel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteILiveEstoppel(ILiveEstoppel iLiveEstoppel) {
		try {
			iLiveEstoppelDao.deleteILiveEstoppel(iLiveEstoppel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Override
	@Transactional(readOnly = true)
	public Pagination getPager(Integer roomId, int cpn, int pageSize) {
		Pagination pager = iLiveEstoppelDao.getPager(roomId, cpn, pageSize);
		List<ILiveEstoppel> list = (List<ILiveEstoppel>) pager.getList();
		List<Map<String, Object>> newList = new ArrayList<>();
		if (list != null && !list.isEmpty()) {
			Map<String, Object> map = null;
			for (ILiveEstoppel toppel : list) {
				map = new HashMap<>();
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(toppel.getUserId());
				map.put("phoneNum", iLiveManager.getMobile());
				map.put("userName", iLiveManager.getNailName());
				map.put("userIp", iLiveManager.getLastIP());
				map.put("userId", iLiveManager.getUserId());
				map.put("time", toppel.getCreateTime());
				newList.add(map);
			}
		}
		pager.setList(newList);
		return pager;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean getILiveEstoppelYesOrNo(Integer roomId, Long userId) {
		boolean ret = false;
		try {
			ILiveEstoppel iLiveEstoppel = iLiveEstoppelDao.getILiveEstoppelYesOrNo(roomId, userId);
			if (iLiveEstoppel != null) {
				ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public ILiveEstoppel getILiveEstoppel(Integer roomId, Long userId) {
		ILiveEstoppel iLiveEstoppel = iLiveEstoppelDao.getILiveEstoppelYesOrNo(roomId, userId);
		return iLiveEstoppel;
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination queryPager(String params, Integer roomId, int cpn, int pageSize) {
		Pagination pager = iLiveEstoppelDao.queryPager(params,roomId, cpn, pageSize);
		List<ILiveEstoppel> list = (List<ILiveEstoppel>) pager.getList();
		List<Map<String, Object>> newList = new ArrayList<>();
		if (list != null && !list.isEmpty()) {
			Map<String, Object> map = null;
			for (ILiveEstoppel toppel : list) {
				map = new HashMap<>();
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(toppel.getUserId());
				map.put("phoneNum", iLiveManager.getMobile());
				map.put("userName", iLiveManager.getUserName());
				map.put("userIp", iLiveManager.getLastIP());
				map.put("userId", iLiveManager.getUserId());
				map.put("time", toppel.getCreateTime());
				newList.add(map);
			}
		}
		pager.setList(newList);
		return pager;
	}

}
