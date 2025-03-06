package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveFCodeDao;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveFCodeMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;

@Service

public class ILiveFCodeMngImpl implements ILiveFCodeMng {

	@Autowired
	private ILiveFCodeDao liveFCodeDao;
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Override
	@Transactional
	public List<ILiveFCode> save(Integer liveRoomId, Long fileId, String codeName, Date startTime, Date endTime, Integer codeNum) {
		List<ILiveFCode> list = null;
		if (null != codeNum && codeNum > 0) {
			String F = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			Random random = new Random();
			list = new ArrayList<ILiveFCode>();
			for (int i = 0; i < codeNum; i++) {
				ILiveFCode bean = null;
				Long nextId = filedIdMng.getNextId("ilive_fcode", "code_id", 1);
				if (nextId > 0) {
					bean = new ILiveFCode();
					bean.setId(nextId);
					bean.setLiveRoomId(liveRoomId);
					bean.setFileId(fileId);
					bean.setCodeName(codeName);
					bean.initFieldValue();
					StringBuffer buffer = new StringBuffer();
					for (int j = 0; j < 6; j++) {
						int ran = random.nextInt(35);
						buffer.append(F.charAt(ran));
					}
					String code = buffer.toString();
					ILiveFCode beanByCode = null;
					do {
						beanByCode = liveFCodeDao.getBeanByCode(liveRoomId, fileId, code);
					} while (null != beanByCode);
					bean.setCode(code);
					liveFCodeDao.save(bean);
					list.add(bean);
				}
			}
		}
		return list;
	}

	@Override
	@Transactional
	public ILiveFCode cancelByCodeId(Long id) {
		ILiveFCode bean = null;
		if (null != id) {
			bean = getBeanById(id);
			if(null!=bean) {
				Integer status = bean.getStatus();
				if (ILiveFCode.STATUS_NEW.equals(status)) {
					bean.setStatus(ILiveFCode.STATUS_CANCELED);
					Updater<ILiveFCode> updater = new Updater<ILiveFCode>(bean);
					liveFCodeDao.updateByUpdater(updater);
				}
			}
		}
		return bean;
	}
	
	@Override
	@Transactional
	public ILiveFCode[] cancelByCodeIds(Long[] ids) {
		ILiveFCode[] beans = null;
		if (null != ids) {
			beans = new ILiveFCode[ids.length];
			for (int i = 0; i < ids.length; i++) {
				Long id = ids[i];
				beans[i] = cancelByCodeId(id);
			}
		}
		return beans;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveFCode getBeanById(Long id) {
		ILiveFCode bean = liveFCodeDao.getBeanById(id);
		initBean(bean, true);
		return bean;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveFCode getBeanByCode(Integer liveRoomId, Long fileId, String code) {
		ILiveFCode bean = null;
		if ((null != liveRoomId || null != fileId) && StringUtils.isNotBlank(code)) {
			code = code.toUpperCase();
			bean = liveFCodeDao.getBeanByCode(liveRoomId, fileId, code);
			initBean(bean, true);
		}
		return bean;
	}

	@Override
	@Transactional
	public ILiveFCode useByCodeId(Long id, Long bindUserId, Long fileId) {
		ILiveFCode bean = null;
		if (null != id && null != bindUserId) {
			bean = new ILiveFCode();
			bean.setId(id);
			bean.setBindUserId(bindUserId);
			bean.setFileId(fileId);
			bean.setStatus(ILiveFCode.STATUS_USED);
			bean.setBindTime(new Timestamp(System.currentTimeMillis()));
			Updater<ILiveFCode> updater = new Updater<ILiveFCode>(bean);
			liveFCodeDao.updateByUpdater(updater);
		}
		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startBindTime, Date endBindTime, int pageNo, int pageSize) {
		Pagination page = liveFCodeDao.pageByParams(codeName, code, status, liveRoomId, fileId, startBindTime,
				endBindTime, pageNo, pageSize);
		if (null != page) {
			List<ILiveFCode> list = (List<ILiveFCode>) page.getList();
			if (null != list) {
				for (ILiveFCode bean : list) {
					initBean(bean, true);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveFCode> listByParams(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startBindTime, Date endBindTime) {
		List<ILiveFCode> list = liveFCodeDao.listByParams(codeName, code, status, liveRoomId, fileId, startBindTime,
				endBindTime);
		if (null != list) {
			for (ILiveFCode bean : list) {
				initBean(bean, true);
			}
		}
		return list;
	}

	private void initBean(ILiveFCode bean, boolean initBindUser) {
		if (null != bean) {
			if (initBindUser) {
				Long bindUserId = bean.getBindUserId();
				if (null != bindUserId) {
					ILiveManager bindUser = iLiveManagerMng.selectILiveManagerById(bindUserId);
					bean.setBindUser(bindUser);
				}
			}
		}
	}

	@Override
	@Transactional
	public void update(ILiveFCode fCode) throws Exception  {
			liveFCodeDao.update(fCode);
	}

	@Override
	public Pagination getBeanByCode(Integer roomId, Long fileId, Integer type, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return liveFCodeDao.getBeanByCode(roomId, fileId, type,pageNo,pageSize);
	}

	@Override
	public List<ILiveFCode> getBeanByCode(Integer roomId, Long fileId) {
		// TODO Auto-generated method stub
		return liveFCodeDao.listByParams(null, null, null, roomId, fileId, null, null);
	}

	@Override
	public Pagination getBeanBySearchCode(Integer roomId, Long fileId, Integer type, Integer creatType,
			String searchCode, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return liveFCodeDao.getBeanBySearchCode(roomId,fileId,type,creatType,
				searchCode,pageNo,pageSize);
	}

	@Override
	public ILiveFCode checkFcode(long parseLong, Integer roomId, Long fileId) {
		// TODO Auto-generated method stub
		return liveFCodeDao.checkFcode(parseLong,roomId,fileId);
	}

}
