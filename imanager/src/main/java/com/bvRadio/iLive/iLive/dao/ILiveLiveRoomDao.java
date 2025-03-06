package com.bvRadio.iLive.iLive.dao;

import java.sql.Timestamp;
import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

public interface ILiveLiveRoomDao {
	public Pagination getPage(Integer id, String roomName, Integer roomId, String urlAddr, String pullAddr,
			Integer isOpened, Timestamp createTime, String userId, String userName, int pageNo, int pageSize);

	public ILiveLiveRoom findById(Integer id);

	public ILiveLiveRoom save(ILiveLiveRoom bean);

	public ILiveLiveRoom deleteById(Integer id);

	public void update(ILiveLiveRoom bean);

	public List<ILiveLiveRoom> findByNumber(String name);

	public ILiveLiveRoom updateByUpdater(Updater<ILiveLiveRoom> updater);

	public ILiveLiveRoom findByRoomId(Integer roomId);
	
	public List<ILiveLiveRoom> findRoomListByEnterprise(Integer enterpriseId);

	/**
	 * 获取开启房间信息
	 * 
	 * @return
	 */
	public List<ILiveLiveRoom> findRoomList();

	public Pagination getPager(String roomName, Integer roomStatus, Integer pageNo, Integer pageSize,
			Long enterpriseId);

	public void updateILiveRoom(ILiveLiveRoom liveRoom);

	public boolean saveRoom(ILiveLiveRoom liveRoom);

	public ILiveLiveRoom getILiveRoom(Integer liveRoomId);

	public Pagination getPager(Integer pageNo, Integer pageSize);
	public Pagination getPager(Integer liveStatus,Integer pageNo, Integer pageSize);

	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType);

	/**
	 * 获得操作人的信息
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize);
	
	public Pagination getPager(Integer formroomtype, Integer formexamine, Integer formlivestate, Integer formroomstate,
			String queryName, Integer pageNo, Integer pageSize);
	public Pagination getPagerNew(List<Object>roomIds,Integer liveStatus,Integer pageNo, Integer pageSize);
	public int queryNumbyState(Integer state);
	public void updateDisabledById(Integer roomId,Integer disabled);
	public void updateLiveStatusById(Integer roomId, Integer liveStatus);
	List<ILiveLiveRoom> getRoomListByliveStatus(Integer liveStatus);
	
}