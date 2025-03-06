package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveFCodeDao;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveSubAccountManager;

@Repository
public class ILiveFCodeDaoImpl extends HibernateBaseDao<ILiveFCode, Long> implements ILiveFCodeDao {

	@Override
	public void save(ILiveFCode liveCode) {
		getSession().save(liveCode);
	}

	@Override
	public ILiveFCode getBeanById(Long id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveFCode getBeanByCode(Integer liveRoomId, Long fileId, String code) {
		Finder finder = Finder.create("select bean from ILiveFCode bean where 1=1");
		if (StringUtils.isNotBlank(code)) {
			finder.append(" and bean.code = :code");
			finder.setParam("code", code);
		}
		if (null != liveRoomId&&liveRoomId!=0) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", liveRoomId);
		}
		if (null != fileId&&fileId!=0L) {
			finder.append(" and bean.fileId = :fileId");
			finder.setParam("fileId", fileId);
		}
		finder.append(" order by bean.id desc");
		List<ILiveFCode> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Pagination pageByParams(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startBindTime, Date endBindTime, int pageNo, int pageSize) {
		Finder finder = createFinder(codeName, code, status, liveRoomId, fileId, startBindTime, endBindTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveFCode> listByParams(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startBindTime, Date endBindTime) {
		Finder finder = createFinder(codeName, code, status, liveRoomId, fileId, startBindTime, endBindTime);
		return find(finder);
	}

	private Finder createFinder(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startBindTime, Date endBindTime) {
		Finder finder = Finder.create("select bean from ILiveFCode bean where 1=1");
		if (StringUtils.isNotBlank(codeName)) {
			finder.append(" and bean.codeName like :codeName");
			finder.setParam("codeName", "%" + codeName + "%");
		}
		if (StringUtils.isNotBlank(code)) {
			finder.append(" and bean.code like :code");
			finder.setParam("code", "%" + code + "%");
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != liveRoomId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", liveRoomId);
		}
		if (null != fileId) {
			finder.append(" and bean.fileId = :fileId");
			finder.setParam("fileId", fileId);
		}
		if (null != startBindTime) {
			finder.append(" and bean.bindTime >= :startBindTime");
			finder.setParam("startBindTime", startBindTime);
		}
		if (null != endBindTime) {
			finder.append(" and bean.bindTime <= :endBindTime");
			finder.setParam("endBindTime", endBindTime);
		}
		finder.append(" order by bean.bindTime desc,bean.id desc");
		return finder;
	}

	@Override
	protected Class<ILiveFCode> getEntityClass() {
		return ILiveFCode.class;
	}

	@Override
	public Pagination getBeanByCode(Integer roomId, Long fileId, Integer type,int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveFCode bean where 1=1");
		
		if (null != roomId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", roomId);
		}
		if (null != fileId) {
			finder.append(" and bean.fileId = :fileId");
			finder.setParam("fileId", fileId);
		}
		if (null != type&&type==1) {
			finder.append(" and bean.status != 3");
		}
		finder.append(" order by bean.id desc");
		
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Pagination getBeanBySearchCode(Integer roomId, Long fileId, Integer type, Integer creatType,
			String searchCode, int pageNo, int pageSize) {
Finder finder = Finder.create("select bean from ILiveFCode bean where 1=1");
		
		if (null != roomId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", roomId);
		}
		if (null != fileId) {
			finder.append(" and bean.fileId = :fileId");
			finder.setParam("fileId", fileId);
		}
		
		if(creatType!=null){
			finder.append(" and bean.status =:status");
			finder.setParam("status", creatType);
		}
		if(searchCode!=null){
			finder.append(" and (bean.code like :searchCode or bean.bindUserName like :searchCode)");
			finder.setParam("searchCode", "%"+searchCode+"%");
		}
		if (null != type&&type==1) {
			finder.append(" and bean.status != 3");
		}
		finder.append(" order by bean.id desc");
		
		return find(finder, pageNo, pageSize);
	}
	@Override
	public void update(ILiveFCode manager) throws Exception {
		this.getSession().update(manager);
		
	}

	@Override
	public ILiveFCode checkFcode(long bindUserId, Integer roomId, Long fileId) {
		Finder finder = Finder.create("select bean from ILiveFCode bean where 1=1");
		if (bindUserId!=0L) {
			finder.append(" and bean.bindUserId = :bindUserId");
			finder.setParam("bindUserId", bindUserId);
		}
		if (null != roomId&&roomId!=0) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", roomId);
		}
		if (null != fileId&&fileId!=0L) {
			finder.append(" and bean.fileId = :fileId");
			finder.setParam("fileId", fileId);
		}
		finder.append(" and bean.status!=3 order by bean.id desc");
		List<ILiveFCode> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
