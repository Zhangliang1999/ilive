package com.bvRadio.iLive.iLive.manager;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
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

	public ILiveLiveRoom update(ILiveLiveRoom bean);

	public boolean isExistRoom(Integer roomId);

	public ILiveLiveRoom findByroomId(Integer roomId);
	

	/**
	 * 获取所有直播间
	 * 
	 * @return
	 */
	public List<ILiveLiveRoom> findRoomList();

	// 根据企业id获取直播间
	public List<ILiveLiveRoom> findRoomListByEnterprise(Integer enterpriseId);

	// 根据企业id获取通过审核科观看的直播间
	public List<ILiveLiveRoom> findRoomListPassByEnterprise(Integer enterpriseId);

	// 根据企业id获取通过审核科观看的直播间和数量
	public List<ILiveLiveRoom> findRoomListPassByEnterpriseAndSize(Integer enterpriseId, Integer num);

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
	public Pagination getPager(String roomName, Integer roomStatus, Integer roomType,Long managerId, Integer pageNo, Integer pageSize);
	/**
	 * 直播房间列表查询
	 * 
	 * @param roomName
	 *            房间名称
	 * @param roomStatus
	 *            房间状态
	 * @param enterpriseId 企业ID
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            页数大小
	 * @param userId 
	 * @return 直播间列表
	 */
	public Pagination getPagerEnterpriseId(String roomName, Integer roomStatus,Integer roomType,
			Integer enterpriseId, Integer pageNo, Integer pageSize, Long userId);

	/**
	 * 创建直播间
	 * 
	 * @param event
	 * @return
	 */
	public boolean saveRoom(ILiveEvent liveEvent, Integer Meet,Integer roomId, Integer serverGroupId, UserBean userBean);

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
	public Integer saveNewBean(RoomCreateVo roomCreateVo) throws Exception;

	/**
	 * 修改一个直播间
	 * 
	 * @param roomCreateVo
	 * @return
	 * @throws ParseException
	 */
	public boolean editNewBean(RoomEditVo roomEditVo,Integer noticeRestore) throws Exception;

	/**
	 * 修改一个直播间的连麦状态
	 * 
	 * @param roomId 房间ID
	 * @param roomId 连麦状态 1：关闭连麦  0：开启连麦
	 * @return
	 * @throws ParseException
	 */
	public boolean editLianmaiNewBean(Integer roomId , Integer liveType) throws Exception; 
	/**
	 * 获取所有列表
	 * 
	 * @return
	 */
	public Pagination getPager(Integer pageNo, Integer pageSize);

	/**
	 * 生成房间ID
	 * 
	 * @return
	 */
	public int createNextId();

	/**
	 * 获取观看端的分页接口
	 * 
	 * @param keyWord
	 * @param pageNo
	 * @param pageSize
	 * @param searchType
	 * @return
	 */
	public List<AppILiveRoom> getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType);

	/**
	 * 初始化房间
	 * 
	 * @param iLiveMangerByMobile
	 */
	public Integer initRoom(ILiveManager iLiveMangerByMobile);
	/**
	 * 初始化会议
	 * 
	 * @param iLiveMangerByMobile
	 */
	public Integer initMeet(ILiveManager iLiveMangerByMobile,Integer enterpriseId,HttpServletRequest request);
	/**
	 * 为操作者的直播间接口
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @param roomType 
	 * @return
	 */
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize,Integer searchType, Integer roomType);

	/**
	 * 获取直播间的H5的播放地址
	 */
	public String getH5PlayUrlByRoom(ILiveLiveRoom liveRoom);

	public Pagination getTop1LiveByEnterpriseId(Integer enterpriseId);

	// 根据场次id获取直播间
	public ILiveLiveRoom findByEventId(Long liveEventId);

	/**
	 * 
	 * @param keyWord
	 * @return
	 */
	public List<AppILiveRoom> getTop4ForView(String keyWord);

	/**
	 * 最大并发数
	 * @param liveEventId
	 * @return
	 */
	public long getMaxViewNum(final long liveEventId, final Integer roomId);

	/**
	 * 开始任务
	 * 
	 * @param roomId
	 */
	public void startTask(final long liveEventId, Integer roomId);

	/**
	 * 结束任务
	 * @param roomId
	 */
	public void stopTask(final long liveEventId);

	/**
	 * 获取未认证企业的试用房间
	 * @param userId
	 * @return
	 */
	public ILiveLiveRoom getIliveRoomByUserId(Long userId);

	public void createRecommends(String[] splitArr, File file);

	public Pagination getPagerForManager(String roomName, Integer roomStatus, Long userId, Integer pageNo,
			Integer pageSize);

	public List<ILiveLiveRoom> getAllLivingRoom();

	public void stopLive(ILiveLiveRoom room);
	
	//根据企业ID和直播间名称获取内容
	List<ILiveLiveRoom> findByEnterpriseIdAndName(String name,Integer enterpriseId);

	public List<ILiveLiveRoom> getliveroomlevel(Integer roomId);

	/**
	 * 获取所有列表
	 * 
	 * @return
	 */
	public Pagination getPager(Integer enterpriseId,Integer liveStatus,Integer pageNo, Integer pageSize);

	public List<JSONObject> getPagerForView1(String keyWord, Integer pageNo, Integer pageSize, Integer searchType,
			boolean per,Integer enterpriseId,Long userId,Integer level);

	public Pagination getNoMeetPager(String keyword, Integer managerId, Integer type, Integer pageNo, Integer pageSize);

	public List<AppILiveRoom> getPagerById(Integer roomId);

	void autoupdate(ILiveEvent event, Integer iLiveRoomId, Integer liveSwitch);
}
