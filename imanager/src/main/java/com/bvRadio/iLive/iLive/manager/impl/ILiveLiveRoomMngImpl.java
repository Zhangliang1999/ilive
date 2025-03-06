package com.bvRadio.iLive.iLive.manager.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.RoomCreateVo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomEditVo;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Service
@Transactional
public class ILiveLiveRoomMngImpl implements ILiveLiveRoomMng {

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	@Override
	@Transactional(readOnly = true)
	public ILiveLiveRoom findById(Integer id) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomDao.findById(id);
		return iLiveLiveRoom;
	}

	@Override
	public ILiveLiveRoom save(ILiveLiveRoom bean) {
		return null;
	}

	@Override
	public ILiveLiveRoom deleteById(Integer id) {
		return null;
	}

	@Override
	public ILiveLiveRoom update(ILiveLiveRoom bean) {
		iLiveLiveRoomDao.update(bean);
		return null;
	}

	@Override
	public boolean isExistRoom(Integer roomId) {
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveLiveRoom findByroomId(Integer roomId) {
		return iLiveLiveRoomDao.findById(roomId);
	}

	@Override
	public List<ILiveLiveRoom> findRoomList() {
		List<ILiveLiveRoom> findRoomList = iLiveLiveRoomDao.findRoomList();
		if (findRoomList != null) {
			for (ILiveLiveRoom room : findRoomList) {
				room.getLiveEvent().getLiveTitle();
			}
		}
		return findRoomList;
	}

	/**
	 * 直播间分页
	 */
	@Transactional(readOnly = true)
	@Override
	public Pagination getPager(String roomName, Integer roomStatus, Long managerId, Integer pageNo, Integer pageSize) {
		Pagination pager = iLiveLiveRoomDao.getPager(roomName, roomStatus, pageNo, pageSize, managerId);
		return pager;
	}

	private static final String RTMP_PROTOCAL = "rtmp://";

	@Autowired
	private ILivePageDecorateMng iLivePageDecorateMng;

	/**
	 * 保存直播间
	 */
	@Override
	@Transactional
	public boolean saveRoom(ILiveEvent liveEvent, Integer roomId, Integer serverGroupId, UserBean userBean) {
		liveEvent.setHostName("测试");
		liveEvent.setLiveStatus(ILivePlayStatusConstant.UN_START);
		liveEvent.setAutoCheckSecond(-1);
		// 默认设备直播
		liveEvent.setPlayType(1);
		liveEvent.setEstoppelType(1);
		liveEvent.setFormId(0);
		liveEvent.setRoomId(roomId);
		liveEvent.setConverAddr(liveEvent.getConverAddr() == null || liveEvent.getConverAddr().equals("")
				? ConfigUtils.get("defaultCoverAddr") : liveEvent.getConverAddr());
		liveEvent.setPlayBgAddr(liveEvent.getPlayBgAddr() == null || liveEvent.getPlayBgAddr().equals("")
				? ConfigUtils.get("defaultLiveBgAddr") : liveEvent.getPlayBgAddr());
		// 启用禁言开关
		liveEvent.setOpenForbidTalkSwitch(0);
		// 启用F码开关
		liveEvent.setOpenFCodeSwitch(0);
		// 启用倒计时开关
		liveEvent.setOpenCountdownSwitch(0);
		// 启用观看次数开启功能
		liveEvent.setOpenViewNumSwitch(1);
		// 启用敏感字开关
		liveEvent.setOpenSensitivewordSwitch(1);
		// 是否启用报名开关
		liveEvent.setOpenFormSwitch(0);
		// 是否启用LOGO开关
		liveEvent.setOpenLogoSwitch(0);
		liveEvent.setOpenDataBeautifySwitch(0);
		// 弹幕互动是否开启
		liveEvent.setOpenBarrageSwitch(0);
		// 倒计时开关是否开启
		liveEvent.setOpenCountdownSwitch(0);
		// 同步直播是否开启
		liveEvent.setOpenSysnPlaySwitch(0);
		liveEvent.setIsDelete(false);
		// 设置为默认公开观看
		liveEvent.setViewAuthorized(1);
		// 设置报名开关
		liveEvent.setOpenSignupSwitch(0);
		ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
				.getAccessMethodBySeverGroupId(serverGroupId);
		String pushStreamAddr = RTMP_PROTOCAL + accessMethodBySeverGroupId.getH5HttpUrl() + ":"
				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		liveEvent.setPushStreamAddr(pushStreamAddr);
		Long eventId = iLiveEventMng.saveIliveMng(liveEvent);
		List<PageDecorate> decorateList = this.buildPageList(eventId);
		for (PageDecorate decorate : decorateList) {
			iLivePageDecorateMng.addPageDecorateInit(decorate);
		}
		liveEvent.setLiveEventId(eventId);
		ILiveLiveRoom liveRoom = new ILiveLiveRoom();
		liveRoom.setEnterpriseId(userBean.getEnterpriseId());
		liveRoom.setCreatePerson(userBean.getUsername());
		liveRoom.setCreateTime(new Timestamp(System.currentTimeMillis()));
		liveRoom.setLiveEvent(liveEvent);
		liveRoom.setOpenStatus(1);
		liveRoom.setManagerId(Long.valueOf(userBean.getUserId()));
		liveRoom.setRoomId(roomId);
		liveRoom.setDeleteStatus(0);
		liveRoom.setServerGroupId(serverGroupId);
		liveRoom.setMoneyLimitSwitch(false);
		iLiveLiveRoomDao.saveRoom(liveRoom);
		return true;
	}

	/**
	 * 构建初始菜单
	 * 
	 * @param liveEventId
	 * @return
	 */
	private List<PageDecorate> buildPageList(Long liveEventId) {
		List<PageDecorate> pageList = new ArrayList<>();

		PageDecorate pd = new PageDecorate();
		pd.setLiveEventId(liveEventId);
		pd.setMenuName("直播简介");
		pd.setMenuOrder(1);
		pd.setMenuType(4);
		pd.setHrefValue("zbjj");

		PageDecorate pd2 = new PageDecorate();
		pd2.setLiveEventId(liveEventId);
		pd2.setMenuName("聊天互动");
		pd2.setMenuOrder(2);
		pd2.setMenuType(2);
		pd2.setHrefValue("lthd");

		PageDecorate pd3 = new PageDecorate();
		pd3.setLiveEventId(liveEventId);
		pd3.setMenuName("话题");
		pd3.setMenuOrder(3);
		pd3.setMenuType(1);
		pd3.setHrefValue("twzb");

		PageDecorate pd4 = new PageDecorate();
		pd4.setLiveEventId(liveEventId);
		pd4.setMenuName("在线问答");
		pd4.setMenuOrder(4);
		pd4.setMenuType(3);
		pd4.setHrefValue("wd");

		pageList.add(pd);
		pageList.add(pd2);
		pageList.add(pd3);
		return pageList;
	}

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveLiveRoomDao iLiveLiveRoomDao;

	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	/**
	 * 获取直播间
	 */
	@Override
	@Transactional(readOnly = true)
	public ILiveLiveRoom getIliveRoom(Integer liveRoomId) {
		ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.getILiveRoom(liveRoomId);
		return iLiveRoom;
	}

	/**
	 * 修改直播间
	 */
	@Override
	public void updateRoom(ILiveEvent liveEvent, UserBean userBean) {
		liveEvent.setHostName("测试");
		liveEvent.setLiveStatus(ILivePlayStatusConstant.UN_START);
		Long eventId = iLiveEventMng.saveIliveMng(liveEvent);
		ILiveLiveRoom liveRoom = new ILiveLiveRoom();
		liveRoom.setEnterpriseId(userBean.getEnterpriseId());
		liveRoom.setCreatePerson(userBean.getUsername());
		liveRoom.setCreateTime(new Timestamp(System.currentTimeMillis()));
		liveRoom.setLiveEvent(liveEvent);
		liveRoom.setOpenStatus(1);
		liveRoom.setManagerId(Long.valueOf(userBean.getUserId()));
		iLiveLiveRoomDao.update(liveRoom);
	}

	@Override
	public void update(ILiveEvent event, Integer iLiveRoomId, Integer liveSwitch) {
		ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.getILiveRoom(iLiveRoomId);
		ILiveEvent liveEvent = iLiveRoom.getLiveEvent();
		Long oldLiveEventId = event.getLiveEventId();
		if (liveSwitch == 1) {
			ILiveEvent eventNew = new ILiveEvent();
			try {
				BeanUtils.copyProperties(eventNew, liveEvent);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			eventNew.setLiveStartTime(event.getLiveStartTime());
			eventNew.setLiveTitle(event.getLiveTitle());
			eventNew.setLiveDesc(event.getLiveDesc());
			eventNew.setLiveEndTime(event.getLiveEndTime());
			eventNew.setOpenBarrageSwitch(event.getOpenBarrageSwitch());
			eventNew.setOpenCheckSwitch(event.getOpenCheckSwitch());
			eventNew.setLiveStatus(event.getLiveStatus());
			eventNew.setPlayBgAddr(event.getPlayBgAddr() == null || event.getPlayBgAddr().equals("")
					? ConfigUtils.get("defaultCoverAddr") : event.getPlayBgAddr());
			eventNew.setConverAddr(event.getConverAddr() == null || event.getConverAddr().equals("")
					? ConfigUtils.get("defaultLiveBgAddr") : event.getConverAddr());
			eventNew.setRoomId(iLiveRoomId);
			eventNew.setIsDelete(false);
			Long saveIliveMng = iLiveEventMng.saveIliveMng(eventNew);
			List<PageDecorate> list = pageDecorateMng.getList(oldLiveEventId);
			if (list != null) {
				for (PageDecorate pd : list) {
					PageDecorate pdNew = new PageDecorate();
					try {
						BeanUtils.copyProperties(pdNew, pd);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					pdNew.setLiveEventId(saveIliveMng);
					iLivePageDecorateMng.addPageDecorateInit(pdNew);
				}
			}
			iLiveRoom.setLiveEvent(eventNew);
			iLiveLiveRoomDao.update(iLiveRoom);
		} else {
			liveEvent.setLiveStartTime(event.getLiveStartTime());
			liveEvent.setLiveTitle(event.getLiveTitle());
			liveEvent.setLiveDesc(event.getLiveDesc());
			liveEvent.setLiveEndTime(event.getLiveEndTime());
			liveEvent.setOpenBarrageSwitch(event.getOpenBarrageSwitch());
			liveEvent.setOpenCheckSwitch(event.getOpenCheckSwitch());
			liveEvent.setLiveStatus(event.getLiveStatus());
			liveEvent.setPlayBgAddr(event.getPlayBgAddr() == null || event.getPlayBgAddr().equals("")
					? ConfigUtils.get("defaultCoverAddr") : event.getPlayBgAddr());
			liveEvent.setConverAddr(event.getConverAddr() == null || event.getConverAddr().equals("")
					? ConfigUtils.get("defaultLiveBgAddr") : event.getConverAddr());
			iLiveEventMng.updateILiveEvent(liveEvent);
		}
	}

	@Override
	public boolean saveNewBean(RoomCreateVo roomCreateVo) throws ParseException {
		long roomId = iLiveFieldIdManagerMng.getNextId("ilive_event", "live_event_id", 1);
		ILiveEvent liveEvent = new ILiveEvent();
		liveEvent.setHostName(roomCreateVo.getHostname());
		liveEvent.setLiveTitle(roomCreateVo.getLiveTitle());
		liveEvent.setLiveDesc(roomCreateVo.getLiveDesc());
		liveEvent.setLiveStatus(ILivePlayStatusConstant.UN_START);
		liveEvent.setLiveStartTime(new Timestamp(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomCreateVo.getLiveStartTime()).getTime()));
		liveEvent.setLiveEndTime(new Timestamp(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomCreateVo.getLiveStartTime()).getTime()
						+ 24 * 60 * 60 * 1000));
		liveEvent.setAutoCheckSecond(-1);
		liveEvent.setPlayType(1);
		liveEvent.setEstoppelType(1);
		liveEvent.setFormId(0);
		liveEvent.setRoomId((int) roomId);
		// 启用禁言开关
		liveEvent.setOpenForbidTalkSwitch(0);
		// 启用F码开关
		liveEvent.setOpenFCodeSwitch(0);
		// 启用倒计时开关
		liveEvent.setOpenCountdownSwitch(0);
		// 启用观看次数开启功能
		liveEvent.setOpenViewNumSwitch(1);
		// 启用敏感字开关
		liveEvent.setOpenSensitivewordSwitch(1);
		// 是否启用报名开关
		liveEvent.setOpenFormSwitch(0);
		// 是否启用LOGO开关
		liveEvent.setOpenLogoSwitch(roomCreateVo.getOpenLogoSwitch());
		liveEvent.setOpenDataBeautifySwitch(0);
		// 弹幕互动是否开启
		liveEvent.setOpenBarrageSwitch(0);
		// 倒计时开关是否开启
		liveEvent.setOpenCountdownSwitch(0);
		// 同步直播是否开启
		liveEvent.setOpenSysnPlaySwitch(0);
		liveEvent.setIsDelete(false);
		liveEvent.setPlayBgAddr(roomCreateVo.getConverAddr() == null || roomCreateVo.getConverAddr().equals("")
				? ConfigUtils.get("defaultLiveBgAddr") : roomCreateVo.getConverAddr());
		liveEvent.setConverAddr(roomCreateVo.getConverAddr() == null || roomCreateVo.getConverAddr().equals("")
				? ConfigUtils.get("defaultLiveBgAddr") : roomCreateVo.getConverAddr());
		liveEvent.setLogoPosition(
				Integer.parseInt(roomCreateVo.getLogoPosition() == null ? "0" : roomCreateVo.getLogoPosition()));
		liveEvent.setLogoUrl(roomCreateVo.getLogoUrl());
		liveEvent.setViewPassword(roomCreateVo.getViewPassword());
		// 设置为默认公开观看
		liveEvent.setViewAuthorized(
				Integer.parseInt(roomCreateVo.getViewAuthorized() == null ? "0" : roomCreateVo.getViewAuthorized()));
		String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
		ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
		String pushStreamAddr = RTMP_PROTOCAL + accessMethodBySeverGroupId.getH5HttpUrl() + ":"
				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		liveEvent.setPushStreamAddr(pushStreamAddr);
		Long eventId = iLiveEventMng.saveIliveMng(liveEvent);
		List<PageDecorate> decorateList = this.buildPageList(eventId);
		for (PageDecorate decorate : decorateList) {
			iLivePageDecorateMng.addPageDecorateInit(decorate);
		}
		ILiveLiveRoom liveRoom = new ILiveLiveRoom();
		liveRoom.setCreatePerson(roomCreateVo.getUserName());
		liveRoom.setCreateTime(new Timestamp(System.currentTimeMillis()));
		liveRoom.setLiveEvent(liveEvent);
		liveRoom.setOpenStatus(1);
		liveRoom.setManagerId(roomCreateVo.getUserId());
		liveRoom.setRoomId((int) roomId);
		liveRoom.setDeleteStatus(0);
		liveRoom.setServerGroupId(Integer.parseInt(defaultVodServerGroupId));
		liveRoom.setMoneyLimitSwitch(false);
		iLiveLiveRoomDao.saveRoom(liveRoom);
		return true;
	}

	/**
	 * 修改直播间
	 * 
	 * @param roomCreateVo
	 * @return
	 * @throws ParseException
	 */
	@Override
	public boolean editNewBean(RoomEditVo roomEditVo) throws ParseException {
		ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.getILiveRoom(roomEditVo.getRoomId());
		ILiveEvent liveEvent = iLiveRoom.getLiveEvent();
		Long oldLiveEventId = liveEvent.getLiveEventId();
		if (roomEditVo.getLiveSwitch() == 1) {
			ILiveEvent eventNew = new ILiveEvent();
			try {
				BeanUtils.copyProperties(eventNew, liveEvent);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			eventNew.setLiveStartTime(new Timestamp(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomEditVo.getLiveStartTime()).getTime()));
			liveEvent.setLiveEndTime(new Timestamp(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomEditVo.getLiveStartTime()).getTime()
							+ 24 * 60 * 60 * 1000));
			Long saveIliveMng = iLiveEventMng.saveIliveMng(eventNew);
			List<PageDecorate> list = pageDecorateMng.getList(oldLiveEventId);
			if (list != null) {
				for (PageDecorate pd : list) {
					PageDecorate pdNew = new PageDecorate();
					try {
						BeanUtils.copyProperties(pdNew, pd);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					pdNew.setLiveEventId(saveIliveMng);
					iLivePageDecorateMng.addPageDecorateInit(pdNew);
				}
			}
			iLiveRoom.setLiveEvent(eventNew);
			iLiveLiveRoomDao.update(iLiveRoom);
			return true;
		} else {
			try {
				ILiveEvent eventNew = new ILiveEvent();
				eventNew.setLiveStartTime(new Timestamp(
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomEditVo.getLiveStartTime()).getTime()));
				eventNew.setLiveTitle(roomEditVo.getLiveTitle());
				eventNew.setConverAddr(roomEditVo.getConverAddr());
				eventNew.setLogoPosition(
						Integer.parseInt(roomEditVo.getLogoPosition() == null ? "1" : roomEditVo.getLogoPosition()));
				eventNew.setLogoUrl(roomEditVo.getLogoUrl());
				eventNew.setViewPassword(roomEditVo.getViewPassword());
				// 设置为默认公开观看
				eventNew.setViewAuthorized(Integer
						.parseInt(roomEditVo.getViewAuthorized() == null ? "0" : roomEditVo.getViewAuthorized()));
				iLiveEventMng.updateILiveEvent(liveEvent);
				return true;
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
	}

	@Override
	public Pagination getPager(Integer pageNo, Integer pageSize) {
		return iLiveLiveRoomDao.getPager(pageNo, pageSize);
	}

	@Override
	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType) {
		return iLiveLiveRoomDao.getPagerForView(keyWord, pageNo, pageSize, searchType);
	}

	@Override
	@Transactional
	public int createNextId() {
		long roomId = iLiveFieldIdManagerMng.getNextId("ilive_live_room", "room_id", 1);
		return (int) roomId;
	}

	@Override
	public void initRoom(ILiveManager iLiveMangerByMobile) {
		long roomId = iLiveFieldIdManagerMng.getNextId("ilive_event", "live_event_id", 1);
		ILiveEvent liveEvent = new ILiveEvent();
		liveEvent.setHostName("默认企业");
		liveEvent.setLiveTitle("试用房间");
		liveEvent.setLiveDesc("");
		liveEvent.setLiveStatus(ILivePlayStatusConstant.UN_START);
		liveEvent.setLiveStartTime(new Timestamp(System.currentTimeMillis()));
		liveEvent.setLiveEndTime(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
		liveEvent.setAutoCheckSecond(-1);
		liveEvent.setPlayType(1);
		liveEvent.setEstoppelType(1);
		liveEvent.setFormId(0);
		liveEvent.setRoomId((int) roomId);
		// 启用禁言开关
		liveEvent.setOpenForbidTalkSwitch(0);
		// 启用F码开关
		liveEvent.setOpenFCodeSwitch(0);
		// 启用倒计时开关
		liveEvent.setOpenCountdownSwitch(0);
		// 启用观看次数开启功能
		liveEvent.setOpenViewNumSwitch(1);
		// 启用敏感字开关
		liveEvent.setOpenSensitivewordSwitch(1);
		// 是否启用报名开关
		liveEvent.setOpenFormSwitch(0);
		// 是否启用LOGO开关
		liveEvent.setOpenLogoSwitch(0);
		liveEvent.setOpenDataBeautifySwitch(0);
		// 弹幕互动是否开启
		liveEvent.setOpenBarrageSwitch(0);
		// 倒计时开关是否开启
		liveEvent.setOpenCountdownSwitch(0);
		// 同步直播是否开启
		liveEvent.setOpenSysnPlaySwitch(0);
		liveEvent.setIsDelete(false);
		liveEvent.setEstoppelType(1);
		liveEvent.setLogoPosition(0);
		liveEvent.setLogoUrl("");
		liveEvent.setViewPassword("");
		// 设置为默认公开观看
		liveEvent.setViewAuthorized(0);
		liveEvent.setPlayBgAddr(ConfigUtils.get("defaultLiveBgAddr"));
		liveEvent.setConverAddr(ConfigUtils.get("defaultCoverAddr"));
		String defaultServerId = ConfigUtils.get("defaultVodServerGroupId");
		ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultServerId));
		String pushStreamAddr = RTMP_PROTOCAL + accessMethodBySeverGroupId.getH5HttpUrl() + ":"
				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		liveEvent.setPushStreamAddr(pushStreamAddr);
		Long eventId = iLiveEventMng.saveIliveMng(liveEvent);
		List<PageDecorate> decorateList = this.buildPageList(eventId);
		for (PageDecorate decorate : decorateList) {
			iLivePageDecorateMng.addPageDecorateInit(decorate);
		}
		ILiveLiveRoom liveRoom = new ILiveLiveRoom();
		liveRoom.setUseState(0);
		liveRoom.setCreatePerson(iLiveMangerByMobile.getUserName());
		liveRoom.setCreateTime(new Timestamp(System.currentTimeMillis()));
		liveRoom.setLiveEvent(liveEvent);
		liveRoom.setOpenStatus(1);
		liveRoom.setManagerId(iLiveMangerByMobile.getUserId());
		liveRoom.setRoomId((int) roomId);
		liveRoom.setDeleteStatus(0);
		liveRoom.setServerGroupId(Integer.parseInt(defaultServerId));
		liveRoom.setMoneyLimitSwitch(false);
		iLiveLiveRoomDao.saveRoom(liveRoom);
		iLiveMangerByMobile.setCertStatus(2);
		iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
	}

	@Override
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize) {
		return iLiveLiveRoomDao.getPagerForOperator(userId, pageNo, pageSize);
	}

	@Override
	public List<ILiveLiveRoom> findRoomListByEnterprise(Integer enterpriseId) {
		return iLiveLiveRoomDao.findRoomListByEnterprise(enterpriseId);
	}

	@Override
	public Pagination getPager(Integer formroomtype, Integer formexamine, Integer formlivestate, Integer formroomstate,
			String queryName, Integer pageNo, Integer pageSize) {
		return iLiveLiveRoomDao.getPager(formroomtype,formexamine,formlivestate,formroomstate,queryName,pageNo,pageSize);
	}
	@Override
	public Pagination getPagerNew(List<Object>roomIds,Integer liveStatus,Integer pageNo, Integer pageSize){
		return iLiveLiveRoomDao.getPagerNew(roomIds,liveStatus,pageNo,pageSize);
	}

	@Override
	public int queryNumbyState(Integer state) {
		return iLiveLiveRoomDao.queryNumbyState(state);
	}

	@Override
	@Transactional
	public void updateDisabledById(Integer roomId,Integer disabled) {
		iLiveLiveRoomDao.updateDisabledById(roomId,disabled);
	}

	@Override
	@Transactional
	public void updateLiveStatusById(Integer roomId, Integer liveStatus) {
		iLiveLiveRoomDao.updateLiveStatusById(roomId,liveStatus);
	}

	@Override
	public List<ILiveLiveRoom> getRoomListByliveStatus(Integer liveStatus) {
		return iLiveLiveRoomDao.getRoomListByliveStatus(liveStatus);
	}

	@Override
	public Pagination getPager(Integer liveStatus,Integer pageNo, Integer pageSize) {
		return iLiveLiveRoomDao.getPager(liveStatus,pageNo,pageSize);
	}

}
