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

	public Pagination getPager(String roomName, Integer roomStatus,Integer roomType, Integer pageNo, Integer pageSize,
			Long enterpriseId);
	public Pagination getPagerEnterpriseId(String roomName, Integer roomStatus,Integer roomType, Integer pageNo, Integer pageSize,
			Integer enterpriseId, Long userId);
	public void updateILiveRoom(ILiveLiveRoom liveRoom);

	public boolean saveRoom(ILiveLiveRoom liveRoom);

	public ILiveLiveRoom getILiveRoom(Integer liveRoomId);

	public Pagination getPager(Integer pageNo, Integer pageSize);

	public List<ILiveLiveRoom> getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType);

	/**
	 * 获得操作人的信息
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @param roomType 
	 * @return
	 */
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize,Integer searchType, Integer roomType);

	public List<ILiveLiveRoom> findRoomListPassByEnterprise(Integer enterpriseId);

	public List<ILiveLiveRoom> findRoomListPassByEnterpriseAndSize(Integer enterpriseId, Integer num);

	public Pagination getTop1LiveByEnterpriseId(Integer enterpriseId);

	// 根据场次id获取直播间
	public ILiveLiveRoom findByEventId(Long liveEventId);

	public List<ILiveLiveRoom> getTop4ForView(String keyword);

	public List<ILiveLiveRoom> getIliveRoomByUserId(Long userId);

	public Pagination getCertStatusRoom(Long userId, Integer pageNo, Integer pageSize);

	/**
	 * 获取所有正在直播的直播间
	 * 
	 * @return
	 */
	public List<ILiveLiveRoom> getAllLivingRoom();
	
	List<ILiveLiveRoom> findByEnterpriseIdAndName(String name, Integer enterpriseId);

	public List<ILiveLiveRoom> getliveroomlevel(Integer roomId);
	
	Pagination getPager(Integer enterpriseId,Integer liveStatus,Integer pageNo, Integer pageSize);


	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize, Integer object,
			String keyWord);
	public Pagination getNoMeetPager(String keyword,Integer managerId, Integer type, Integer pageNo, Integer pageSize);
}