package com.bvRadio.iLive.iLive.manager;

import java.util.Date;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;

public interface ILiveFCodeMng {
	public List<ILiveFCode> save(Integer liveRoomId, Long fileId, String codeName, Date startTime, Date endTime, Integer codeNum);

	public ILiveFCode cancelByCodeId(Long id);

	public ILiveFCode[] cancelByCodeIds(Long[] ids);

	public ILiveFCode getBeanById(Long id);

	public ILiveFCode getBeanByCode(Integer liveRoomId, Long fileId, String code);

	public ILiveFCode useByCodeId(Long id, Long bindUserId, Long fileId);

	public Pagination pageByParams(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startBindTime, Date endBindTime, int pageNo, int pageSize);

	public List<ILiveFCode> listByParams(String codeName, String code, Integer status, Integer liveRoomId, Long fileId,
			Date startBindTime, Date endBindTime);

	public void update(ILiveFCode fCode) throws Exception;

	public Pagination getBeanByCode(Integer roomId, Long fileId, Integer type, int pageNo, int pageSize);

	public List<ILiveFCode> getBeanByCode(Integer roomId, Long fileId);

	public Pagination getBeanBySearchCode(Integer roomId, Long fileId, Integer type, Integer creatType,
			String searchCode, int pageNo, int pageSize);

	public ILiveFCode checkFcode(long parseLong, Integer roomId, Long fileId);

	
}
