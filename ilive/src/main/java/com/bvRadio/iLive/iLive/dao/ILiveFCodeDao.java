package com.bvRadio.iLive.iLive.dao;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;

public interface ILiveFCodeDao {
	public void save(ILiveFCode liveCode);

	public ILiveFCode getBeanById(Long codeId);
	
	public ILiveFCode getBeanByCode(Integer liveRoomId, Long fileId, String code);

	public Pagination pageByParams(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startUserTime, Date endUserTime, int pageNo, int pageSize);
	
	public List<ILiveFCode> listByParams(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startBindTime, Date endBindTime);

	public ILiveFCode updateByUpdater(Updater<ILiveFCode> updater);

	public Pagination getBeanByCode(Integer roomId, Long fileId, Integer type, int pageNo, int pageSize);

	public Pagination getBeanBySearchCode(Integer roomId, Long fileId, Integer type, Integer creatType,
			String searchCode, int pageNo, int pageSize);

	void update(ILiveFCode manager) throws Exception;

	public ILiveFCode checkFcode(long parseLong, Integer roomId, Long fileId);
}
