package com.bvRadio.iLive.iLive.manager;

import java.text.ParseException;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.RoomCreateVo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomEditVo;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;

public interface ILiveLiveRoomMng {

	/**
	 * 根据直播间ID获取数据
	 * 
	 * @param id
	 *            直播间ID
	 * @return
	 */
	public ILiveLiveRoom findById(Integer id);

	public ILiveLiveRoom save(ILiveLiveRoom bean);

	public ILiveLiveRoom deleteById(Integer id);

	public ILiveLiveRoom update(ILiveLiveRoom bean);

	public boolean isExistRoom(Integer roomId);

	public ILiveLiveRoom findByroomId(Integer roomId);

	/**
	 * 获取所有直播间
	 * 
	 * @return
	 */
	public List<ILiveLiveRoom> findRoomList();
	
	//根据企业id获取直播间
	public List<ILiveLiveRoom> findRoomListByEnterprise(Integer enterpriseId);

	/**
	 * 直播房间列表查询
	 * 
	 * @param roomName
	 *            房间名称
	 * @param roomStatus
	 *            房间状态
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            页数大小
	 * @return 直播间列表
	 */
	public Pagination getPager(String roomName, Integer roomStatus, Long managerId, Integer pageNo, Integer pageSize);

	/**
	 * 创建直播间
	 * 
	 * @param event
	 * @return
	 */
	public boolean saveRoom(ILiveEvent liveEvent, Integer roomId, Integer serverGroupId, UserBean userBean);

	/**
	 * 获取直播间对象
	 * 
	 * @param liveRoomId
	 * @return
	 */
	public ILiveLiveRoom getIliveRoom(Integer liveRoomId);

	public void updateRoom(ILiveEvent iLiveEvent, UserBean user);

	public void update(ILiveEvent event, Integer iLiveRoomId, Integer liveEventSwitch);

	/**
	 * 保存新的一个直播间对象
	 * 
	 * @param roomCreateVo
	 * @return
	 * @throws ParseException
	 */
	public boolean saveNewBean(RoomCreateVo roomCreateVo) throws ParseException;

	/**
	 * 修改一个直播间
	 * 
	 * @param roomCreateVo
	 * @return
	 * @throws ParseException
	 */
	public boolean editNewBean(RoomEditVo roomEditVo) throws ParseException;

	/**
	 * 获取所有列表
	 * 
	 * @return
	 */
	public Pagination getPager(Integer pageNo, Integer pageSize);
	/**
	 * 获取所有列表
	 * 
	 * @return
	 */
	public Pagination getPager(Integer liveStatus,Integer pageNo, Integer pageSize);
	
	/**
	 * 根据条件查询获取所有列表
	 * @return
	 */
	public Pagination getPager(Integer formroomtype,Integer formexamine,Integer formlivestate,Integer formroomstate,String queryName,Integer pageNo, Integer pageSize);

	/**
	 * 根据条件查询获取所有列表
	 * @return
	 */
	public Pagination getPagerNew(List<Object>roomIds,Integer liveStatus,Integer pageNo, Integer pageSize);

	/**
	 * 生成房间ID
	 * 
	 * @return
	 */
	public int createNextId();

	/**
	 * 获取观看端的分页接口
	 * @param keyWord
	 * @param pageNo
	 * @param pageSize
	 * @param searchType
	 * @return
	 */
	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType);

	
	/**
	 * 初始化房间
	 * @param iLiveMangerByMobile
	 */
	public void initRoom(ILiveManager iLiveMangerByMobile);

	
	/**
	 * 为操作者的直播间接口
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize);
	
	public int queryNumbyState(Integer state);

	void updateDisabledById(Integer roomId,Integer disabled);
	void updateLiveStatusById(Integer roomId,Integer liveStatus);
	
	List<ILiveLiveRoom> getRoomListByliveStatus(Integer liveStatus);
	
}
