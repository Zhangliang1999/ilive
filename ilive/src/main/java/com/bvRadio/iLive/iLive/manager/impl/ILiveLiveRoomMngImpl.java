package com.bvRadio.iLive.iLive.manager.impl;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.json.JSONUtils;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.RoomCreateVo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomEditVo;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.constants.ILiveStaticsCache;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;
import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMeetRequestMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveRandomRecordTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomShareConfigMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.HttpRequest;
import com.bvRadio.iLive.iLive.util.ILiveUMSMessageUtil;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.QrCodeUtils;
import com.bvRadio.iLive.iLive.util.RoomNoticeUtil;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.util.StringUtil;
import com.bvRadio.iLive.iLive.util.WeightRandom;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.PostMan;

@Service
@Transactional
public class ILiveLiveRoomMngImpl implements ILiveLiveRoomMng {

	Logger logger = LoggerFactory.getLogger(ILiveLiveRoomMngImpl.class);

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	@Autowired
	private ILiveServerAccessMethodMng serverAccessMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveRandomRecordTaskMng iLiveRandomRecordTaskMng;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveVideoHistoryMng iLiveVideoHisotryMng;

	@Autowired
	private BBSDiyformMng bbsDiyformMng;

	@Autowired
	private BBSDiymodelMng bbsDiymodelMng;

	@Autowired
	private ILiveRoomShareConfigMng roomShareConfigMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;
	@Autowired
	private ILiveViewWhiteBillMng viewWhiteMng;
	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;
	@Autowired
	private ILiveMeetRequestMng meetRequestMng;
	
	@Override
	@Transactional(readOnly = true)
	public ILiveLiveRoom findById(Integer id) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomDao.findById(id);
		return iLiveLiveRoom;
	}

	@Override
	public ILiveLiveRoom update(ILiveLiveRoom bean) {
		if(bean.getRelatedVideo()==null){
			bean.setRelatedVideo(1);
		}
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
	public Pagination getPager(String roomName, Integer roomStatus, Integer roomType,Long managerId, Integer pageNo, Integer pageSize) {
		Pagination pager = iLiveLiveRoomDao.getPager(roomName, roomStatus,roomType, pageNo, pageSize, managerId);
		return pager;
	}
	/**
	 * 直播间分页
	 */
	@Transactional(readOnly = true)
	@Override
	public Pagination getPagerEnterpriseId(String roomName, Integer roomStatus, Integer roomType,Integer enterpriseId, Integer pageNo, Integer pageSize,Long userId) {
		Pagination pager = iLiveLiveRoomDao.getPagerEnterpriseId(roomName, roomStatus,roomType, pageNo, pageSize, enterpriseId,userId);
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
	public boolean saveRoom(ILiveEvent liveEvent, Integer Meet,Integer roomId, Integer serverGroupId, UserBean userBean) {
		liveEvent.setHostName("");
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
		//liveEvent.setOpenBarrageSwitch(0);
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
		liveRoom.setDisabled(0);
		liveRoom.setRoomId(roomId);
		liveRoom.setDeleteStatus(0);
		liveRoom.setServerGroupId(serverGroupId);
		liveRoom.setEnterpriseId(userBean.getEnterpriseId());
		liveRoom.setMoneyLimitSwitch(false);
		liveRoom.setOpenBackupStream(0);
		liveRoom.setStreamStatus(0);
		liveRoom.setRoomType(Meet);
		liveRoom.setRelatedVideo(1);
		//判断是不是子账户
		ILiveManager iLiveManager=iLiveManagerMng.selectILiveManagerById(Long.parseLong(userBean.getUserId()));
		if(ILiveManager.USER_LEVEL_SUB.equals(iLiveManager.getLevel())) {
			liveRoom.setSubAccountId(liveRoom.getSubAccountId()+"&"+userBean.getUserId());
		}
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

		// PageDecorate pd3 = new PageDecorate();
		// pd3.setLiveEventId(liveEventId);
		// pd3.setMenuName("话题互动");
		// pd3.setMenuOrder(3);
		// pd3.setMenuType(1);
		// pd3.setHrefValue("twzb");

		PageDecorate pd5 = new PageDecorate();
		pd5.setLiveEventId(liveEventId);
		pd5.setMenuName("往期回看");
		pd5.setMenuOrder(4);
		pd5.setMenuType(5);
		pd5.setHrefValue("splb");

		PageDecorate pd4 = new PageDecorate();
		pd4.setLiveEventId(liveEventId);
		pd4.setMenuName("直播问答");
		pd4.setMenuOrder(100);
		pd4.setMenuType(3);
		pd4.setHrefValue("wd");

		pageList.add(pd);
		pageList.add(pd2);
		// pageList.add(pd3);
		pageList.add(pd4);
		pageList.add(pd5);

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
		liveEvent.setHostName("");
		liveEvent.setLiveStatus(ILivePlayStatusConstant.UN_START);
		iLiveEventMng.saveIliveMng(liveEvent);
		ILiveLiveRoom liveRoom = new ILiveLiveRoom();
		liveRoom.setEnterpriseId(userBean.getEnterpriseId());
		liveRoom.setCreatePerson(userBean.getUsername());
		liveRoom.setCreateTime(new Timestamp(System.currentTimeMillis()));
		liveRoom.setLiveEvent(liveEvent);
		liveRoom.setManagerId(Long.valueOf(userBean.getUserId()));
		this.update(liveRoom);
	}

	@Override
	public void update(ILiveEvent event, Integer iLiveRoomId, Integer liveSwitch) {
		ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.getILiveRoom(iLiveRoomId);
		ILiveEvent liveEvent = iLiveRoom.getLiveEvent();
		Long oldLiveEventId = liveEvent.getLiveEventId();
		if (liveSwitch.intValue() == 1) {
			ILiveEvent eventNew = new ILiveEvent();
			try {
				BeanUtilsExt.copyProperties(eventNew, liveEvent);
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
					? ConfigUtils.get("defaultLiveBgAddr") : event.getPlayBgAddr());
			eventNew.setConverAddr(event.getConverAddr() == null || event.getConverAddr().equals("")
					? ConfigUtils.get("defaultCoverAddr") : event.getConverAddr());
			eventNew.setRoomId(iLiveRoomId);
			eventNew.setIsDelete(false);
			eventNew.setIsSign(event.getIsSign());
			Long saveIliveMng = iLiveEventMng.saveIliveMng(eventNew);
			
			// 1 处理菜单
			List<PageDecorate> list = pageDecorateMng.getList(oldLiveEventId);
			if (list != null) {
				for (PageDecorate pd : list) {
					PageDecorate pdNew = new PageDecorate();
					try {
						BeanUtilsExt.copyProperties(pdNew, pd);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					pdNew.setLiveEventId(saveIliveMng);
					pageDecorateMng.addPageDecorateInit(pdNew);
				}
			}
			// 2处理表单
			Integer formId = liveEvent.getFormId();
			if (formId > 0) {
				try {
					BBSDiyform diyfrom = bbsDiyformMng.getDiyfromById(formId);
					BBSDiyform newForm = this.convertDiyForm2NewForm(diyfrom);
					newForm = bbsDiyformMng.save(newForm);
					formId = newForm.getDiyformId();
					Set<BBSDiymodel> bbsDiymodels = diyfrom.getBbsDiymodels();
					if (bbsDiymodels != null) {
						Set<BBSDiymodel> newModelSet = new HashSet<>();
						BBSDiymodel newModel = null;
						for (BBSDiymodel model : bbsDiymodels) {
							newModel = this.convertModel2NewModel(model, newForm);
							newModel = bbsDiymodelMng.save(newModel);
							newModelSet.add(newModel);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 3处理白名单
			viewWhiteMng.updateEventId(oldLiveEventId,saveIliveMng);
			eventNew.setLiveEventId(saveIliveMng);
			eventNew.setFormId(formId);
			iLiveRoom.setLiveEvent(eventNew);
			this.update(iLiveRoom);
			try {
				ApplicationCache.getOnlineNumber().remove(iLiveRoomId);
			} catch (Exception e) {
			}
			try {
				List<ILiveRoomShareConfig> shareConfig = roomShareConfigMng.getShareConfig(eventNew.getRoomId());
				if (shareConfig != null && !shareConfig.isEmpty()) {
					for (ILiveRoomShareConfig share : shareConfig) {
						if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_SINGLE)) {
							// 朋友
							share.setLiveTitle(event.getLiveTitle());
							share.setLiveDesc(event.getLiveDesc());
							roomShareConfigMng.updateShare(share);
						} else if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_CIRCLE)) {
							// 朋友圈
							share.setLiveTitle(event.getLiveTitle());
							share.setLiveDesc(event.getLiveDesc());
							roomShareConfigMng.updateShare(share);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			try{
				if ( eventNew.getLiveStatus()==0 ){
					//处理一下当直播间被设置为预告以后需要将UMS播放打开，以便预监功能正常。
					ILiveServerAccessMethod accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(iLiveRoom.getServerGroupId());
					ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iLiveRoom);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
		} else {
			liveEvent.setLiveStartTime(event.getLiveStartTime());
			liveEvent.setLiveTitle(event.getLiveTitle());
			liveEvent.setLiveDesc(event.getLiveDesc());
			liveEvent.setLiveEndTime(event.getLiveEndTime());
			liveEvent.setOpenBarrageSwitch(event.getOpenBarrageSwitch());
			liveEvent.setOpenCheckSwitch(event.getOpenCheckSwitch());
			liveEvent.setLiveStatus(event.getLiveStatus());
			liveEvent.setPlayBgAddr(event.getPlayBgAddr() == null || event.getPlayBgAddr().equals("")
					? ConfigUtils.get("defaultLiveBgAddr") : event.getPlayBgAddr());
			liveEvent.setConverAddr(event.getConverAddr() == null || event.getConverAddr().equals("")
					? ConfigUtils.get("defaultCoverAddr") : event.getConverAddr());
			liveEvent.setIsSign(event.getIsSign());
			iLiveEventMng.updateILiveEvent(liveEvent);
			try {
				List<ILiveRoomShareConfig> shareConfig = roomShareConfigMng.getShareConfig(liveEvent.getRoomId());
				if (shareConfig != null && !shareConfig.isEmpty()) {
					for (ILiveRoomShareConfig share : shareConfig) {
						if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_SINGLE)
								&& share.getOpenStatus() != null && share.getOpenStatus().intValue() == 0) {
							String appendDesc = "直播开始时间:";
							String time = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分")
									.format(new Date(liveEvent.getLiveStartTime().getTime()));
							appendDesc = appendDesc + time;
							if (!com.jwzt.comm.StringUtils.isEmpty(liveEvent.getLiveDesc())) {
								appendDesc += ",";
								appendDesc += liveEvent.getLiveDesc();
							}
							// 朋友
							share.setLiveTitle(liveEvent.getLiveTitle());
							share.setLiveDesc(appendDesc);
							Integer enterpriseId = iLiveRoom.getEnterpriseId();
							ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
							String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
							if (enterpriseImg == null || "".equals(enterpriseImg)) {
								enterpriseImg = ConfigUtils.get("defaultTysxLogoUrl");
							}
							share.setLiveImg(enterpriseImg);
							roomShareConfigMng.updateShare(share);
						} else if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_CIRCLE)
								&& share.getOpenStatus() != null && share.getOpenStatus().intValue() == 0) {
							// 朋友圈
							share.setLiveTitle(liveEvent.getLiveTitle());
							String appendDesc = "直播开始时间:";
							String time = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分")
									.format(new Date(liveEvent.getLiveStartTime().getTime()));
							appendDesc = appendDesc + time;
							if (!com.jwzt.comm.StringUtils.isEmpty(liveEvent.getLiveDesc())) {
								appendDesc += ",";
								appendDesc += liveEvent.getLiveDesc();
							}
							share.setLiveDesc(appendDesc);
							Integer enterpriseId = iLiveRoom.getEnterpriseId();
							ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
							String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
							if (enterpriseImg == null || "".equals(enterpriseImg)) {
								enterpriseImg = ConfigUtils.get("defaultTysxLogoUrl");
							}
							share.setLiveImg(enterpriseImg);
							roomShareConfigMng.updateShare(share);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void autoupdate(ILiveEvent event, Integer iLiveRoomId, Integer liveSwitch) {
		ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.findByRoomId(iLiveRoomId);
		ILiveEvent liveEvent = iLiveRoom.getLiveEvent();
		Long oldLiveEventId = liveEvent.getLiveEventId();
		if (liveSwitch.intValue() == 1) {
			ILiveEvent eventNew = new ILiveEvent();
			try {
				BeanUtilsExt.copyProperties(eventNew, liveEvent);
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
					? ConfigUtils.get("defaultLiveBgAddr") : event.getPlayBgAddr());
			eventNew.setConverAddr(event.getConverAddr() == null || event.getConverAddr().equals("")
					? ConfigUtils.get("defaultCoverAddr") : event.getConverAddr());
			eventNew.setRoomId(iLiveRoomId);
			eventNew.setIsDelete(false);
			eventNew.setIsSign(event.getIsSign());
			Long saveIliveMng = iLiveEventMng.saveIliveMng(eventNew);
			
			// 1 处理菜单
			List<PageDecorate> list = pageDecorateMng.getList(oldLiveEventId);
			if (list != null) {
				for (PageDecorate pd : list) {
					PageDecorate pdNew = new PageDecorate();
					try {
						BeanUtilsExt.copyProperties(pdNew, pd);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					pdNew.setLiveEventId(saveIliveMng);
					pageDecorateMng.addPageDecorateInit(pdNew);
				}
			}
			// 2处理表单
			Integer formId = liveEvent.getFormId();
			if (formId > 0) {
				try {
					BBSDiyform diyfrom = bbsDiyformMng.getDiyfromById(formId);
					BBSDiyform newForm = this.convertDiyForm2NewForm(diyfrom);
					newForm = bbsDiyformMng.save(newForm);
					formId = newForm.getDiyformId();
					Set<BBSDiymodel> bbsDiymodels = diyfrom.getBbsDiymodels();
					if (bbsDiymodels != null) {
						Set<BBSDiymodel> newModelSet = new HashSet<>();
						BBSDiymodel newModel = null;
						for (BBSDiymodel model : bbsDiymodels) {
							newModel = this.convertModel2NewModel(model, newForm);
							newModel = bbsDiymodelMng.save(newModel);
							newModelSet.add(newModel);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 3处理白名单
			viewWhiteMng.updateEventId(oldLiveEventId,saveIliveMng);
			eventNew.setLiveEventId(saveIliveMng);
			eventNew.setFormId(formId);
			iLiveRoom.setLiveEvent(eventNew);
			this.update(iLiveRoom);
			try {
				ApplicationCache.getOnlineNumber().remove(iLiveRoomId);
			} catch (Exception e) {
			}
			try {
				List<ILiveRoomShareConfig> shareConfig = roomShareConfigMng.getShareConfig(eventNew.getRoomId());
				if (shareConfig != null && !shareConfig.isEmpty()) {
					for (ILiveRoomShareConfig share : shareConfig) {
						if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_SINGLE)) {
							// 朋友
							share.setLiveTitle(event.getLiveTitle());
							share.setLiveDesc(event.getLiveDesc());
							roomShareConfigMng.updateShare(share);
						} else if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_CIRCLE)) {
							// 朋友圈
							share.setLiveTitle(event.getLiveTitle());
							share.setLiveDesc(event.getLiveDesc());
							roomShareConfigMng.updateShare(share);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			try{
				if ( eventNew.getLiveStatus()==0 ){
					//处理一下当直播间被设置为预告以后需要将UMS播放打开，以便预监功能正常。
					ILiveServerAccessMethod accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(iLiveRoom.getServerGroupId());
					ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iLiveRoom);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
		} else {
			liveEvent.setLiveStartTime(event.getLiveStartTime());
			liveEvent.setLiveTitle(event.getLiveTitle());
			liveEvent.setLiveDesc(event.getLiveDesc());
			liveEvent.setLiveEndTime(event.getLiveEndTime());
			liveEvent.setOpenBarrageSwitch(event.getOpenBarrageSwitch());
			liveEvent.setOpenCheckSwitch(event.getOpenCheckSwitch());
			liveEvent.setLiveStatus(event.getLiveStatus());
			liveEvent.setPlayBgAddr(event.getPlayBgAddr() == null || event.getPlayBgAddr().equals("")
					? ConfigUtils.get("defaultLiveBgAddr") : event.getPlayBgAddr());
			liveEvent.setConverAddr(event.getConverAddr() == null || event.getConverAddr().equals("")
					? ConfigUtils.get("defaultCoverAddr") : event.getConverAddr());
			liveEvent.setIsSign(event.getIsSign());
			iLiveEventMng.updateILiveEvent(liveEvent);
			try {
				List<ILiveRoomShareConfig> shareConfig = roomShareConfigMng.getShareConfig(liveEvent.getRoomId());
				if (shareConfig != null && !shareConfig.isEmpty()) {
					for (ILiveRoomShareConfig share : shareConfig) {
						if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_SINGLE)
								&& share.getOpenStatus() != null && share.getOpenStatus().intValue() == 0) {
							String appendDesc = "直播开始时间:";
							String time = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分")
									.format(new Date(liveEvent.getLiveStartTime().getTime()));
							appendDesc = appendDesc + time;
							if (!com.jwzt.comm.StringUtils.isEmpty(liveEvent.getLiveDesc())) {
								appendDesc += ",";
								appendDesc += liveEvent.getLiveDesc();
							}
							// 朋友
							share.setLiveTitle(liveEvent.getLiveTitle());
							share.setLiveDesc(appendDesc);
							Integer enterpriseId = iLiveRoom.getEnterpriseId();
							ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
							String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
							if (enterpriseImg == null || "".equals(enterpriseImg)) {
								enterpriseImg = ConfigUtils.get("defaultTysxLogoUrl");
							}
							share.setLiveImg(enterpriseImg);
							roomShareConfigMng.updateShare(share);
						} else if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_CIRCLE)
								&& share.getOpenStatus() != null && share.getOpenStatus().intValue() == 0) {
							// 朋友圈
							share.setLiveTitle(liveEvent.getLiveTitle());
							String appendDesc = "直播开始时间:";
							String time = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分")
									.format(new Date(liveEvent.getLiveStartTime().getTime()));
							appendDesc = appendDesc + time;
							if (!com.jwzt.comm.StringUtils.isEmpty(liveEvent.getLiveDesc())) {
								appendDesc += ",";
								appendDesc += liveEvent.getLiveDesc();
							}
							share.setLiveDesc(appendDesc);
							Integer enterpriseId = iLiveRoom.getEnterpriseId();
							ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
							String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
							if (enterpriseImg == null || "".equals(enterpriseImg)) {
								enterpriseImg = ConfigUtils.get("defaultTysxLogoUrl");
							}
							share.setLiveImg(enterpriseImg);
							roomShareConfigMng.updateShare(share);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Integer saveNewBean(RoomCreateVo roomCreateVo) throws Exception {
		long roomId = iLiveFieldIdManagerMng.getNextId("ilive_live_room", "room_id", 1);
		ILiveEvent liveEvent = new ILiveEvent();
		liveEvent.setHostName(roomCreateVo.getHostname());
		liveEvent.setLiveTitle(roomCreateVo.getLiveTitle());
		liveEvent.setLiveDesc(roomCreateVo.getLiveDesc());
		liveEvent.setLiveStatus(ILivePlayStatusConstant.UN_START);
		liveEvent.setNeedLogin(roomCreateVo.getNeedLogin());
		liveEvent.setLiveStartTime(new Timestamp(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomCreateVo.getLiveStartTime()).getTime()));
		liveEvent.setLiveEndTime(new Timestamp(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomCreateVo.getLiveStartTime()).getTime()
						+ 24 * 60 * 60 * 1000));
		liveEvent.setAutoCheckSecond(-1);
		liveEvent.setPlayType(1);
		// 默认关闭全局禁言
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
		liveEvent.setOpenSignupSwitch(0);
		liveEvent.setIsDelete(false);
		liveEvent.setPlayBgAddr(roomCreateVo.getConverAddr() == null || roomCreateVo.getConverAddr().equals("")
				? ConfigUtils.get("defaultLiveBgAddr") : roomCreateVo.getConverAddr());
		if(roomCreateVo.getRoomType()==1) {
			
			liveEvent.setConverAddr(roomCreateVo.getConverAddr() == null || roomCreateVo.getConverAddr().equals("")
			? ConfigUtils.get("defaultMeetCoverAddr") : roomCreateVo.getConverAddr());
		}else {
			liveEvent.setConverAddr(roomCreateVo.getConverAddr() == null || roomCreateVo.getConverAddr().equals("")
					? ConfigUtils.get("defaultCoverAddr") : roomCreateVo.getConverAddr());
		}
		
		liveEvent.setLogoPosition(
				Integer.parseInt(roomCreateVo.getLogoPosition() == null ? "0" : roomCreateVo.getLogoPosition()));
		liveEvent.setLogoUrl(roomCreateVo.getLogoUrl());
		liveEvent.setViewPassword(roomCreateVo.getViewPassword());
		// 设置为默认公开观看
		liveEvent.setViewAuthorized(
				Integer.parseInt(roomCreateVo.getViewAuthorized() == null ? "1" : roomCreateVo.getViewAuthorized()));
		String defaultLiveServerGroupId = null;
		if(ConfigUtils.get("server_access_method").equals("1")) {
			WeightRandom  weightRandom =new WeightRandom();
			defaultLiveServerGroupId = weightRandom.getRandomResoult();
		}else {
			defaultLiveServerGroupId = ConfigUtils.get("defaultLiveServerGroupId");
		}
		ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultLiveServerGroupId));
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
		liveRoom.setRoomType(roomCreateVo.getRoomType());
		liveRoom.setServerGroupId(Integer.parseInt(defaultLiveServerGroupId));
		liveRoom.setMoneyLimitSwitch(false);
		liveRoom.setDisabled(0);
		liveRoom.setRelatedVideo(1);
		if(roomCreateVo.getLiveType() != null){
			liveRoom.setLiveType(roomCreateVo.getLiveType());
		}else{
			liveRoom.setLiveType(1);
		}
		
		Long userId = roomCreateVo.getUserId();
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		liveRoom.setEnterpriseId(iLiveManager.getEnterPrise().getEnterpriseId());
		Integer liveDelay = roomCreateVo.getLiveDelay();
		if(liveDelay!=null && liveDelay>0){
			liveRoom.setLiveDelay(liveDelay);
		}else{
			liveRoom.setLiveDelay(0);
		}
		
		iLiveLiveRoomDao.saveRoom(liveRoom);
		return (int) roomId;
	}

	
	/**
	 * 修改直播间连麦状态
	 * 
	 * @param roomCreateVo
	 * @return
	 * @throws ParseException
	 */
	@Override
	public boolean editLianmaiNewBean(Integer roomId , Integer liveType) throws Exception {
		Boolean ret = false;
		try{
			ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.getILiveRoom(roomId);
			if(liveType != null){
				iLiveRoom.setLiveType(liveType);
			}
			this.update(iLiveRoom);
			ret = true;
		}catch(Exception e){
			e.printStackTrace();
		}

		return ret;
	}
	
	/**
	 * 修改直播间
	 * 
	 * @param roomCreateVo
	 * @return
	 * @throws ParseException
	 */
	@Override
	public boolean editNewBean(RoomEditVo roomEditVo, Integer noticeRestore) throws ParseException {
		ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.getILiveRoom(roomEditVo.getRoomId());
		ILiveEvent liveEvent = iLiveRoom.getLiveEvent();
		Long oldLiveEventId = liveEvent.getLiveEventId();
		if (roomEditVo.getLiveSwitch().intValue() == 1) {
			ILiveEvent iLiveEventNew = new ILiveEvent();
			try {
				BeanUtilsExt.copyProperties(iLiveEventNew, liveEvent);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			iLiveEventNew.setLiveStatus(ILivePlayStatusConstant.UN_START);
			iLiveEventNew.setNeedLogin(roomEditVo.getNeedLogin());
			iLiveEventNew.setLiveStartTime(roomEditVo.getLiveStartTime()==null?liveEvent.getLiveStartTime():new Timestamp(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomEditVo.getLiveStartTime()).getTime()));
			iLiveEventNew.setLiveTitle(roomEditVo.getLiveTitle()==null||roomEditVo.getLiveTitle().equals("")?liveEvent.getLiveTitle():roomEditVo.getLiveTitle());
			iLiveEventNew.setLiveDesc(roomEditVo.getLiveDesc()==null||roomEditVo.getLiveDesc().equals("")?liveEvent.getLiveDesc():roomEditVo.getLiveDesc());
			iLiveEventNew.setHostName(roomEditVo.getHostname()==null||roomEditVo.getHostname().equals("")?liveEvent.getHostName():roomEditVo.getHostname());
			iLiveEventNew.setConverAddr(roomEditVo.getConverAddr() == null || roomEditVo.getConverAddr().equals("")
					?  liveEvent.getConverAddr() : roomEditVo.getConverAddr());
			iLiveEventNew.setPlayBgAddr(roomEditVo.getConverAddr() == null || roomEditVo.getConverAddr().equals("")
					?  liveEvent.getConverAddr() : roomEditVo.getConverAddr());
			iLiveEventNew.setPushMsgSwitch(roomEditVo.getPushMsgSwitch() == null ? 0 : roomEditVo.getPushMsgSwitch());
			iLiveEventNew.setLogoPosition(
					Integer.parseInt(roomEditVo.getLogoPosition() == null ? "1" : roomEditVo.getLogoPosition()));
			iLiveEventNew.setLogoUrl(roomEditVo.getLogoUrl()==null||roomEditVo.getLogoUrl().equals("")?liveEvent.getLogoUrl():roomEditVo.getLogoUrl());
			iLiveEventNew.setViewPassword(roomEditVo.getViewPassword() == null ? "" : roomEditVo.getViewPassword());
			// 设置为默认公开观看
			iLiveEventNew.setViewAuthorized(
					Integer.parseInt(roomEditVo.getViewAuthorized() == null ? "1" : roomEditVo.getViewAuthorized()));
			List<PageDecorate> pageRecordList = pageDecorateMng.getList(liveEvent.getLiveEventId());
			iLiveEventNew.setPageRecordList(pageRecordList);
			Long saveIliveMng = iLiveEventMng.saveIliveMng(iLiveEventNew);
			// 1 处理菜单
			List<PageDecorate> list = pageDecorateMng.getList(oldLiveEventId);
			if (list != null) {
				for (PageDecorate pd : list) {
					PageDecorate pdNew = new PageDecorate();
					try {
						BeanUtilsExt.copyProperties(pdNew, pd);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					pdNew.setLiveEventId(saveIliveMng);
					pageDecorateMng.addPageDecorateInit(pdNew);
				}
			}
			// 2处理表单
			Integer formId = liveEvent.getFormId();
			if (formId > 0) {
				try {
					BBSDiyform diyfrom = bbsDiyformMng.getDiyfromById(formId);
					BBSDiyform newForm = this.convertDiyForm2NewForm(diyfrom);
					newForm = bbsDiyformMng.save(newForm);
					formId = newForm.getDiyformId();
					Set<BBSDiymodel> bbsDiymodels = diyfrom.getBbsDiymodels();
					if (bbsDiymodels != null) {
						Set<BBSDiymodel> newModelSet = new HashSet<>();
						BBSDiymodel newModel = null;
						for (BBSDiymodel model : bbsDiymodels) {
							newModel = this.convertModel2NewModel(model, newForm);
							newModel = bbsDiymodelMng.save(newModel);
							newModelSet.add(newModel);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 3处理白名单
			iLiveEventNew.setLiveEventId(saveIliveMng);
			iLiveEventNew.setFormId(formId);
			iLiveRoom.setLiveEvent(iLiveEventNew);
			
			if(roomEditVo.getLiveDelay() != null)
				iLiveRoom.setLiveDelay(roomEditVo.getLiveDelay());
			
			//判断直播间开启或者关闭连麦设置
			if(roomEditVo.getLiveType()!= null){
				iLiveRoom.setLiveType(roomEditVo.getLiveType());
			}
			this.update(iLiveRoom);
			
			try {
				List<ILiveRoomShareConfig> shareConfig = roomShareConfigMng.getShareConfig(roomEditVo.getRoomId());
				if (shareConfig != null && !shareConfig.isEmpty()) {
					for (ILiveRoomShareConfig share : shareConfig) {
						if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_SINGLE)
								&& share.getOpenStatus() != null && share.getOpenStatus().intValue() == 0) {
							String appendDesc = "直播开始时间:";
							String time = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分")
									.format(new Date(iLiveEventNew.getLiveStartTime().getTime()));
							appendDesc = appendDesc + time;
							if (!com.jwzt.comm.StringUtils.isEmpty(roomEditVo.getLiveDesc())) {
								appendDesc += ",";
								appendDesc += roomEditVo.getLiveDesc();
							}
							// 朋友
							share.setLiveTitle(roomEditVo.getLiveTitle());
							share.setLiveDesc(appendDesc);
							Integer enterpriseId = iLiveRoom.getEnterpriseId();
							ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
							String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
							if (enterpriseImg == null || "".equals(enterpriseImg)) {
								enterpriseImg = ConfigUtils.get("defaultTysxLogoUrl");
							}
							share.setLiveImg(enterpriseImg);
							roomShareConfigMng.updateShare(share);
						} else if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_CIRCLE)
								&& share.getOpenStatus() != null && share.getOpenStatus().intValue() == 0) {
							// 朋友圈
							share.setLiveTitle(roomEditVo.getLiveTitle());
							String appendDesc = "直播开始时间:";
							String time = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分")
									.format(new Date(iLiveEventNew.getLiveStartTime().getTime()));
							appendDesc = appendDesc + time;
							if (!com.jwzt.comm.StringUtils.isEmpty(roomEditVo.getLiveDesc())) {
								appendDesc += ",";
								appendDesc += roomEditVo.getLiveDesc();
							}
							share.setLiveDesc(appendDesc);
							Integer enterpriseId = iLiveRoom.getEnterpriseId();
							ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
							String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
							if (enterpriseImg == null || "".equals(enterpriseImg)) {
								enterpriseImg = ConfigUtils.get("defaultTysxLogoUrl");
							}
							share.setLiveImg(enterpriseImg);
							roomShareConfigMng.updateShare(share);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			try{
				if ( iLiveEventNew.getLiveStatus()==0 ){
					//处理一下当直播间被设置为预告以后需要将UMS播放打开，以便预监功能正常。
					ILiveServerAccessMethod accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(iLiveRoom.getServerGroupId());
					ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iLiveRoom);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return true;
		} else {
			try {
				liveEvent.setLiveStartTime(new Timestamp(
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roomEditVo.getLiveStartTime()).getTime()));
				liveEvent.setLiveTitle(roomEditVo.getLiveTitle());
				liveEvent.setNeedLogin(roomEditVo.getNeedLogin());
				liveEvent.setHostName(roomEditVo.getHostname());
				liveEvent.setLiveDesc(roomEditVo.getLiveDesc());
				liveEvent.setConverAddr(roomEditVo.getConverAddr() == null || roomEditVo.getConverAddr().equals("")
						? ConfigUtils.get("defaultLiveBgAddr") : roomEditVo.getConverAddr());
				liveEvent.setPlayBgAddr(roomEditVo.getConverAddr() == null || roomEditVo.getConverAddr().equals("")
						? ConfigUtils.get("defaultLiveBgAddr") : roomEditVo.getConverAddr());
				liveEvent.setPushMsgSwitch(roomEditVo.getPushMsgSwitch() == null ? 0 : roomEditVo.getPushMsgSwitch());
				liveEvent.setLogoPosition(
						Integer.parseInt(roomEditVo.getLogoPosition() == null ? "1" : roomEditVo.getLogoPosition()));
				liveEvent.setLogoUrl(roomEditVo.getLogoUrl());
				liveEvent.setViewPassword(roomEditVo.getViewPassword() == null ? "" : roomEditVo.getViewPassword());
				// 设置为默认公开观看
				liveEvent.setViewAuthorized(Integer
						.parseInt(roomEditVo.getViewAuthorized() == null ? "1" : roomEditVo.getViewAuthorized()));
				iLiveEventMng.updateILiveEvent(liveEvent);
				
				//判断直播间开启或者关闭连麦设置
				if(roomEditVo.getLiveType()!= null){
					iLiveRoom.setLiveType(roomEditVo.getLiveType());
				}
				
				if(roomEditVo.getLiveDelay() != null)
					iLiveRoom.setLiveDelay(roomEditVo.getLiveDelay());
				
				this.update(iLiveRoom);
				
				try {
					List<ILiveRoomShareConfig> shareConfig = roomShareConfigMng.getShareConfig(roomEditVo.getRoomId());
					if (shareConfig != null && !shareConfig.isEmpty()) {
						for (ILiveRoomShareConfig share : shareConfig) {
							if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_SINGLE)
									&& share.getOpenStatus() != null && share.getOpenStatus().intValue() == 0) {
								String appendDesc = "直播开始时间:";
								String time = new SimpleDateFormat("yyyy年MM月dd日")
										.format(new Date(liveEvent.getLiveStartTime().getTime()));
								appendDesc = appendDesc + time;
								if (!com.jwzt.comm.StringUtils.isEmpty(roomEditVo.getLiveDesc())) {
									appendDesc += ",";
									appendDesc += roomEditVo.getLiveDesc();
								}
								// 朋友
								share.setLiveTitle(roomEditVo.getLiveTitle());
								share.setLiveDesc(appendDesc);
								Integer enterpriseId = iLiveRoom.getEnterpriseId();
								ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
								String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
								if (enterpriseImg == null || "".equals(enterpriseImg)) {
									enterpriseImg = ConfigUtils.get("defaultTysxLogoUrl");
								}
								share.setLiveImg(enterpriseImg);
								roomShareConfigMng.updateShare(share);
							} else if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_CIRCLE)
									&& share.getOpenStatus() != null && share.getOpenStatus().intValue() == 0) {
								// 朋友圈
								share.setLiveTitle(roomEditVo.getLiveTitle());
								String appendDesc = "直播开始时间:";
								String time = new SimpleDateFormat("yyyy年MM月dd日")
										.format(new Date(liveEvent.getLiveStartTime().getTime()));
								appendDesc = appendDesc + time;
								if (!com.jwzt.comm.StringUtils.isEmpty(roomEditVo.getLiveDesc())) {
									appendDesc += ",";
									appendDesc += roomEditVo.getLiveDesc();
								}
								share.setLiveDesc(appendDesc);
								Integer enterpriseId = iLiveRoom.getEnterpriseId();
								ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
								String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
								if (enterpriseImg == null || "".equals(enterpriseImg)) {
									enterpriseImg = ConfigUtils.get("defaultTysxLogoUrl");
								}
								share.setLiveImg(enterpriseImg);
								roomShareConfigMng.updateShare(share);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
	}

	@Override
	public Pagination getPager(Integer pageNo, Integer pageSize) {
		return iLiveLiveRoomDao.getPager(pageNo, pageSize);
	}

	private AppILiveRoom convertRoomPo2Vo(ILiveLiveRoom room) {
		AppILiveRoom appRoom = new AppILiveRoom();
		appRoom.setLiveStatus(room.getLiveEvent().getLiveStatus());
		appRoom.setRoomId(room.getRoomId());
		appRoom.setRoomDesc(room.getLiveEvent().getLiveDesc() == null ? "" : room.getLiveEvent().getLiveDesc());
		appRoom.setRoomImg(StringPatternUtil.convertEmpty(room.getLiveEvent().getConverAddr()));
		appRoom.setRoomName(StringPatternUtil.convertEmpty(room.getLiveEvent().getLiveTitle()));
		appRoom.setStartTime(room.getLiveEvent().getLiveStartTime());
//		appRoom.setRoom(room);
		return appRoom;
	}

	@Override
	@Transactional
	public int createNextId() {
		Long roomId = iLiveFieldIdManagerMng.getNextId("ilive_live_room", "room_id", 1);
		return roomId.intValue();
	}

	@Override
	public Integer initRoom(ILiveManager iLiveMangerByMobile) {
		//生成使用直播间
		long roomId = iLiveFieldIdManagerMng.getNextId("ilive_live_room", "room_id", 1);
		ILiveEvent liveEvent = new ILiveEvent();
		liveEvent.setHostName("");
		liveEvent.setLiveTitle("试用直播间");
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
		liveEvent.setViewAuthorized(1);
		liveEvent.setPlayBgAddr(ConfigUtils.get("defaultLiveBgAddr"));
		liveEvent.setConverAddr(ConfigUtils.get("defaultCoverAddr"));
		String defaultLiveServerGroupId = null;
		if(ConfigUtils.get("server_access_method").equals("1")) {
			WeightRandom  weightRandom =new WeightRandom();
			defaultLiveServerGroupId = weightRandom.getRandomResoult();
		}else {
			defaultLiveServerGroupId = ConfigUtils.get("defaultLiveServerGroupId");
		}
		ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultLiveServerGroupId));
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
		liveRoom.setRoomType(0);
		liveRoom.setOpenStatus(1);
		liveRoom.setManagerId(iLiveMangerByMobile.getUserId());
		liveRoom.setRoomId((int) roomId);
		liveRoom.setDeleteStatus(0);
		liveRoom.setServerGroupId(Integer.parseInt(defaultLiveServerGroupId));
		liveRoom.setMoneyLimitSwitch(false);
		liveRoom.setUseState(1);
		// 构建新的企业
		ILiveEnterprise enterprise = this.buildRawEnterprise(iLiveMangerByMobile.getMobile());
		ILiveEnterprise newEnterprise = iLiveEnterpriseMng.saveILiveEnterpriseForPhone(enterprise);
		liveRoom.setEnterpriseId(newEnterprise.getEnterpriseId());
		liveRoom.setDisabled(0);
		iLiveLiveRoomDao.saveRoom(liveRoom);
		iLiveMangerByMobile.setCertStatus(2);
		iLiveMangerByMobile.setEnterPrise(newEnterprise);
		iLiveManagerMng.updateLiveManager(iLiveMangerByMobile);
		
		return newEnterprise.getEnterpriseId();
	}

	private ILiveEnterprise buildRawEnterprise(String enterpriseName) {
		ILiveEnterprise rawEnterprise = new ILiveEnterprise();
		// 为提交认证
		rawEnterprise.setCertStatus(0);
		String defaultImg = ConfigUtils.get("defaultEnterpriseImg");
		rawEnterprise.setEnterpriseImg(defaultImg);
		rawEnterprise.setEnterpriseName(enterpriseName);
		return rawEnterprise;
	}

	@Override
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize,Integer searchType,Integer roomType) {
		return iLiveLiveRoomDao.getPagerForOperator(userId, pageNo, pageSize,searchType,roomType);
	}

	@Override
	public List<ILiveLiveRoom> findRoomListByEnterprise(Integer enterpriseId) {
		return iLiveLiveRoomDao.findRoomListByEnterprise(enterpriseId);
	}

	@Override
	public String getH5PlayUrlByRoom(ILiveLiveRoom liveRoom) {
		ILiveServerAccessMethod severGroupId = serverAccessMng
				.getAccessMethodBySeverGroupId(liveRoom.getServerGroupId());
		String liveAddr = severGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + liveRoom.getRoomId();
		return liveAddr;
	}

	@Override
	public List<ILiveLiveRoom> findRoomListPassByEnterprise(Integer enterpriseId) {
		return iLiveLiveRoomDao.findRoomListPassByEnterprise(enterpriseId);
	}

	@Override
	public Pagination getTop1LiveByEnterpriseId(Integer enterpriseId) {
		return iLiveLiveRoomDao.getTop1LiveByEnterpriseId(enterpriseId);
	}

	@Override
	public ILiveLiveRoom findByEventId(Long liveEventId) {
		return iLiveLiveRoomDao.findByEventId(liveEventId);
	}

	@Override
	public List<AppILiveRoom> getTop4ForView(String keyword) {
		List<AppILiveRoom> list = new ArrayList<>();
		List<ILiveLiveRoom> rooms = iLiveLiveRoomDao.getTop4ForView(keyword);
		if (rooms != null && !rooms.isEmpty()) {
			for (ILiveLiveRoom room : rooms) {
				Integer enterpriseId = room.getEnterpriseId();
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
				ILiveAppEnterprise appEnterprise = this.convertPo2Vo(iLiveEnterPrise);
				AppILiveRoom approom = this.convertRoomPo2Vo(room);
				approom.setAppEnterprise(appEnterprise);
				approom.setRoomType(room.getRoomType());
				list.add(approom);
			}
		}
		return list;
	}

	private ILiveAppEnterprise convertPo2Vo(ILiveEnterprise enterprise) {
		ILiveAppEnterprise appEnterprise = new ILiveAppEnterprise();
		appEnterprise.setCertStatus(enterprise.getCertStatus());
		appEnterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
		appEnterprise.setConcernTotal(0L);
		appEnterprise.setEnterpriseId(enterprise.getEnterpriseId());
		appEnterprise.setEnterpriseName(StringPatternUtil.convertEmpty(enterprise.getEnterpriseName()));
		appEnterprise.setEnterpriseImg(StringPatternUtil.convertEmpty(enterprise.getEnterpriseImg()));
		String defaultEnterpriseServerId = ConfigUtils.get("defaultEnterpriseServerId");
		ILiveServerAccessMethod serverGroup = serverAccessMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultEnterpriseServerId));
		String homePageUrl = serverGroup.getH5HttpUrl() + "/home/index.html?enterpriseId="
				+ enterprise.getEnterpriseId();
		appEnterprise.setHomePageUrl(homePageUrl);
		return appEnterprise;
	}

	@Override
	public List<AppILiveRoom> getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType) {
		List<AppILiveRoom> list = new ArrayList<>();
		List<ILiveLiveRoom> pagerForView = iLiveLiveRoomDao.getPagerForView(keyWord, pageNo, pageSize, searchType);
		if (pagerForView != null) {
			for (ILiveLiveRoom room : pagerForView) {
				AppILiveRoom approom = this.convertRoomPo2Vo(room);
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
				ILiveAppEnterprise appEnterprise = this.convertPo2Vo(iLiveEnterPrise);
				approom.setAppEnterprise(appEnterprise);
				approom.setRoomType(room.getRoomType());
				list.add(approom);
			}
		}
		return list;
	}
	
	@Override
	public List<ILiveLiveRoom> findRoomListPassByEnterpriseAndSize(Integer enterpriseId, Integer num) {
		return iLiveLiveRoomDao.findRoomListPassByEnterpriseAndSize(enterpriseId, num);
	}

	@Override
	public long getMaxViewNum(long liveEventId, Integer roomId) {
		// 最大观看个数
		Long viewNum = ILiveStaticsCache.maxViewNumCache.get(liveEventId);
		if (viewNum == null) {
			viewNum = 0L;
			ILiveStaticsCache.maxViewNumCache.put(liveEventId, 0L);
			// ILiveStaticsCache.timeUserNum.put(roomId, 0L);
		} else {
			ConcurrentHashMap<String, UserBean> hashtable = ApplicationCache.getUserListMap().get(roomId);
			if (hashtable != null) {
				int size = hashtable.size();
				// ILiveStaticsCache.timeUserNum.put(roomId, (long) size);
				if (viewNum < size) {
					System.out.println("##########改变最高并发人数发生改变变为####：" + size);
					ILiveStaticsCache.maxViewNumCache.put(liveEventId, (long) size);
				}
			}

		}
		return viewNum;
	}

	/**
	 * 获取在线人数
	 * 
	 * @return
	 */
	// public long getTimerNumByRoomId(Integer roomId) {
	// return ILiveStaticsCache.timeUserNum.get(roomId);
	// }

	public void startTask(final long iliveEventId, final Integer roomId) {
		final Timer timer = new Timer();
		if (ILiveStaticsCache.maxViewState.get(iliveEventId) == null) {
			ILiveStaticsCache.maxViewState.put(iliveEventId, false);
		}
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (ILiveStaticsCache.maxViewState.get(iliveEventId)) {
					timer.cancel();// 停止定时器
					ILiveStaticsCache.maxViewState.put(iliveEventId, false);
				} else {
					getMaxViewNum(iliveEventId, roomId);
				}
			}
		};
		timer.schedule(task, 0, 6000);// 10秒一次
	}

	@Override
	public void stopTask(final long iliveEventId) {
		ILiveStaticsCache.maxViewState.put(iliveEventId, true);
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveLiveRoom getIliveRoomByUserId(Long userId) {
		List<ILiveLiveRoom> roomList = iLiveLiveRoomDao.getIliveRoomByUserId(userId);
		if (roomList == null || roomList.isEmpty()) {
			return null;
		} else {
			return roomList.get(0);
		}
	}

	@Override
	public void createRecommends(String[] splitArr, File file) {
		List<AppILiveRoom> list = new ArrayList<>();
		for (String roomId : splitArr) {
			ILiveLiveRoom iLiveRoom = iLiveLiveRoomDao.getILiveRoom(Integer.parseInt(roomId));
			Integer enterpriseId = iLiveRoom.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			AppILiveRoom appRoom = this.convertRoomPo2Vo(iLiveRoom);
			ILiveAppEnterprise appEnterprise = this.convertPo2Vo(iLiveEnterPrise);
			appRoom.setAppEnterprise(appEnterprise);
			list.add(appRoom);
		}
		net.sf.json.JSONArray json = null;
		try {
			File parentFile = file.getParentFile();
			if (!parentFile.isDirectory()) {
				parentFile.mkdirs();
			}
			FileWriter writer = new FileWriter(file);
			json = net.sf.json.JSONArray.fromObject(list);
			json.write(writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Pagination getPagerForManager(String roomName, Integer roomStatus, Long userId, Integer pageNo,
			Integer pageSize) {
		Pagination pagination = iLiveLiveRoomDao.getCertStatusRoom(userId, pageNo, pageSize);
		return pagination;
	}

	@Override
	public List<ILiveLiveRoom> getAllLivingRoom() {
		return iLiveLiveRoomDao.getAllLivingRoom();
	}

	@Override
	@Transactional
	public void stopLive(final ILiveLiveRoom iliveRoom) {
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		ILiveServerAccessMethod accessMethodBySeverGroupId = serverAccessMng
				.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
		try {
			ILiveUMSMessageUtil.INSTANCE.stopPlay(accessMethodBySeverGroupId, iliveRoom);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
		liveEvent.setLiveEndTime(new Timestamp(System.currentTimeMillis()));
		liveEvent.setRealEndTime(new Timestamp(System.currentTimeMillis()));
		iLiveEventMng.updateILiveEvent(liveEvent);
		iliveRoom.setLiveEvent(liveEvent);
		RoomNoticeUtil.nptice(iliveRoom);
		this.stopTask(liveEvent.getLiveEventId());
		// 正常向流媒体服务器推流操作
		String realIpAddr = ConfigUtils.get("localIP");
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
		iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
		ILiveUserViewStatics.INSTANCE.stopLive(iliveRoom, realIpAddr);
		final Integer serverGroupId = iliveRoom.getServerGroupId();
		final Long userIdLong = iliveRoom.getManagerId();
		final Integer enterpriseId = iliveRoom.getEnterpriseId();
		final ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(liveEvent.getLiveEventId(),
				userIdLong, ILivePlayStatusConstant.PLAY_ING);
		if (ILivePlayStatusConstant.PAUSE_ING != liveEvent.getLiveStatus()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					ILiveServerAccessMethod serverAccessMethod = serverAccessMng
							.getAccessMethodBySeverGroupId(serverGroupId);
					String postUrl = "http://" + serverAccessMethod.getHttp_address() + ":"
							+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
					String mountName = "live" + iliveRoom.getRoomId();
					int vodGroupId = Integer.parseInt(ConfigUtils.get("defaultVodServerGroupId"));
					int length = (int) (System.currentTimeMillis()
							- iliveRoom.getLiveEvent().getRecordStartTime().getTime()) / 1000;
					String common = "";
					try {
						common = "?function=RecordLive&mountName=" + mountName + "&startTime="
								+ URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(iliveRoom.getLiveEvent().getRecordStartTime()), "UTF-8")
								+ "&length=" + length + "&destGroupId=" + vodGroupId;
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					postUrl = postUrl + common;
					String downloadUrl = PostMan.downloadUrl(postUrl);
					if (downloadUrl != null) {
						String trimDownloadUrl = downloadUrl.trim();
						String relativePath = StringUtil.getTagValue(trimDownloadUrl, "ret");
						if (!relativePath.trim().equals("")) {
							try {
								ILiveMediaFile liveMediaFile = new ILiveMediaFile();
								liveMediaFile.setMediaFileName(iliveRoom.getLiveEvent().getLiveTitle() + "-"
										+ new SimpleDateFormat("yyyyMMddHHmmss")
												.format(iliveRoom.getLiveEvent().getRecordStartTime()));
								liveMediaFile.setFilePath(relativePath);
								liveMediaFile.setCreateStartTime(iliveRoom.getLiveEvent().getRecordStartTime());
								liveMediaFile.setMediaCreateTime(new Timestamp(System.currentTimeMillis()));
								liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
								ILiveServerAccessMethod vodAccessMethod = serverAccessMng
										.getAccessMethodBySeverGroupId(vodGroupId);
								liveMediaFile.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
								liveMediaFile.setCreateType(0);
								liveMediaFile.setDuration(length);
								liveMediaFile.setFileType(1);
								liveMediaFile.setOnlineFlag(1);
								// 通过用户拿到企业
								liveMediaFile.setEnterpriseId(userIdLong);
								liveMediaFile.setUserId(userIdLong);
								liveMediaFile.setMediaInfoStatus(0);
								liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
								liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
								liveMediaFile.setDelFlag(0);
								liveMediaFile.setAllowComment(1);
								liveMediaFile.setMediaInfoDealState(0);
								try {
									Thread.sleep(15000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
								iLiveVideoHisotryMng.saveVideoHistory(iliveRoom.getRoomId(), saveIliveMediaFile,
										userIdLong);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					if (task != null) {
						try {
							task.setEndTime(System.currentTimeMillis());
							task.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
							iLiveRandomRecordTaskMng.updateRecordTask(task);
							length = (int) (System.currentTimeMillis() - task.getStartTime()) / 1000;
							try {
								common = "?function=RecordLive&mountName=" + mountName + "&startTime="
										+ URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
												.format(new Timestamp(task.getStartTime())), "UTF-8")
										+ "&length=" + length + "&destGroupId=" + vodGroupId;
							} catch (UnsupportedEncodingException e1) {
								e1.printStackTrace();
							}
							postUrl = postUrl + common;
							downloadUrl = PostMan.downloadUrl(postUrl);
							if (downloadUrl != null) {
								String trimDownloadUrl = downloadUrl.trim();
								String relativePath = StringUtil.getTagValue(trimDownloadUrl, "ret");
								if (!relativePath.trim().equals("")) {
									ILiveMediaFile liveMediaFile = new ILiveMediaFile();
									liveMediaFile.setMediaFileName(iliveRoom.getLiveEvent().getLiveTitle() + "-"
											+ new SimpleDateFormat("yyyyMMddHHmmss")
													.format(iliveRoom.getLiveEvent().getRecordStartTime()));
									liveMediaFile.setFilePath(relativePath);
									liveMediaFile.setCreateStartTime(iliveRoom.getLiveEvent().getRecordStartTime());
									liveMediaFile.setMediaCreateTime(new Timestamp(System.currentTimeMillis()));
									liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
									ILiveServerAccessMethod vodAccessMethod = accessMethodMng
											.getAccessMethodBySeverGroupId(vodGroupId);
									liveMediaFile.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
									liveMediaFile.setCreateType(0);
									liveMediaFile.setDuration(length);
									liveMediaFile.setFileType(1);
									liveMediaFile.setOnlineFlag(1);
									// 通过用户拿到企业
									liveMediaFile.setEnterpriseId((long) enterpriseId);
									liveMediaFile.setUserId(userIdLong);
									liveMediaFile.setMediaInfoStatus(0);
									liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
									liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
									liveMediaFile.setDelFlag(0);
									liveMediaFile.setMediaInfoDealState(0);
									liveMediaFile.setAllowComment(1);
									try {
										Thread.sleep(15000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
									iLiveVideoHisotryMng.saveVideoHistory(iliveRoom.getRoomId(), saveIliveMediaFile,
											userIdLong);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

	private BBSDiymodel convertModel2NewModel(BBSDiymodel model, BBSDiyform bbsDiyform) {
		BBSDiymodel newModel = new BBSDiymodel();
		newModel.setOptValue(model.getOptValue());
		newModel.setDiymodelType(model.getDiymodelType());
		newModel.setDiymodelTitle(model.getDiymodelTitle());
		newModel.setDiyOrder(model.getDiyOrder());
		newModel.setBbsDiyform(bbsDiyform);
		newModel.setNeedAnswer(model.getNeedAnswer());
		newModel.setDiymodelKey(model.getDiymodelKey());
		newModel.setNeedMsgValid(model.getNeedMsgValid() == null ? 0 : model.getNeedMsgValid());
		newModel.setOpenAnswer(model.getOpenAnswer() == null ? 0 : model.getOpenAnswer());
		return newModel;
	}

	private BBSDiyform convertDiyForm2NewForm(BBSDiyform diyfrom) {
		BBSDiyform newForm = new BBSDiyform();
		newForm.setDiyformName(diyfrom.getDiyformName());
		newForm.setCreateTime(new Timestamp(System.currentTimeMillis()));
		newForm.setDelFlag(0);
		return newForm;
	}

	@Override
	public List<ILiveLiveRoom> findByEnterpriseIdAndName(String name, Integer enterpriseId) {
		return iLiveLiveRoomDao.findByEnterpriseIdAndName(name,enterpriseId);
	}

	@Override
	public List<ILiveLiveRoom> getliveroomlevel(Integer roomId) {
		return iLiveLiveRoomDao.getliveroomlevel(roomId);
	}

	@Override
	public Pagination getPager(Integer enterpriseId,Integer liveStatus, Integer pageNo, Integer pageSize) {
		return iLiveLiveRoomDao.getPager(enterpriseId,liveStatus, pageNo, pageSize);
	}

	@Override
	public List<JSONObject> getPagerForView1(String keyWord, Integer pageNo, Integer pageSize, Integer searchType,
			boolean per,Integer enterpriseId,Long userId,Integer level) {
		List<ILiveLiveRoom> list = new ArrayList<>();
		Pagination pager = this.getPagerForOperator(userId, pageNo, pageSize,null,keyWord);
		List<ILiveLiveRoom> pagerForView =(List<ILiveLiveRoom>) pager.getList();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		if (pagerForView != null&&pagerForView.size()>0) {
			for (ILiveLiveRoom room : pagerForView) {
				Integer serverGroupId = room.getServerGroupId();
				ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(serverGroupId);
				String rtmpAddr = "rtmp://" + serverAccessMethod.getOrgLiveHttpUrl() + ":"
						+ serverAccessMethod.getUmsport() + "/live/live" + room.getRoomId() + "_tzwj_5000k";
				room.setRtmpAddr(rtmpAddr);
				String hlsAddr="http://"+serverAccessMethod.getHttp_address()+"/live/live" + room.getRoomId()+"/5000k/tzwj_video.m3u8";
				room.setHlsPlayUrl(hlsAddr);
				String rtmpPlay="rtmp://"+serverAccessMethod.getCdnLiveHttpUrl()+":"
						+serverAccessMethod.getUmsport()+"/live/live" + room.getRoomId() + "_tzwj_5000k";
				room.setRtmpPlayUrl(rtmpPlay);
				JSONObject putNewLiveInJson = room.putNewLiveInJson(room);
				jsonList.add(putNewLiveInJson);
			}
		}
		return jsonList;
	}

	private Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize, Integer object,
			String keyWord) {
		return iLiveLiveRoomDao.getPagerForOperator(userId, pageNo, pageSize,object,keyWord);
	}

	@Override
	public Pagination getNoMeetPager(String keyword,Integer managerId, Integer type, Integer pageNo, Integer pageSize) {
		return iLiveLiveRoomDao.getNoMeetPager(keyword, managerId,type, pageNo, pageSize);
	}

	@Override
	public List<AppILiveRoom> getPagerById(Integer roomId) {
		List<AppILiveRoom> list = new ArrayList<>();
		List<ILiveLiveRoom> pagerForView = iLiveLiveRoomDao.getliveroomlevel(roomId);
		if (pagerForView != null) {
			for (ILiveLiveRoom room : pagerForView) {
				AppILiveRoom approom = this.convertRoomPo2Vo(room);
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
				ILiveAppEnterprise appEnterprise = this.convertPo2Vo(iLiveEnterPrise);
				approom.setAppEnterprise(appEnterprise);
				list.add(approom);
			}
		}
		return list;
	}

	@Override
	@Transactional
	public Integer initMeet(ILiveManager iLiveMangerByMobile,Integer enterPriseId, HttpServletRequest request) {
		
				long roomId = iLiveFieldIdManagerMng.getNextId("ilive_live_room", "room_id", 1);
				ILiveEvent liveEvent = new ILiveEvent();
				liveEvent.setHostName("");
				liveEvent.setLiveTitle("试用会议间");
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
				liveEvent.setViewAuthorized(1);
				liveEvent.setPlayBgAddr(ConfigUtils.get("defaultLiveBgAddr"));
				liveEvent.setConverAddr(ConfigUtils.get("defaultCoverAddr"));
				String serverGroupIdStr = null;
				if(ConfigUtils.get("server_access_method").equals("1")) {
					WeightRandom  weightRandom =new WeightRandom();
					serverGroupIdStr = weightRandom.getRandomResoult();
				}else {
					serverGroupIdStr = ConfigUtils.get("defaultLiveServerGroupId");
				}
				int liveGroupId = Integer.parseInt(serverGroupIdStr);
				ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
						.getAccessMethodBySeverGroupId(liveGroupId);
				String pushStreamAddr1 = RTMP_PROTOCAL + accessMethodBySeverGroupId.getH5HttpUrl() + ":"
						+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
				liveEvent.setPushStreamAddr(pushStreamAddr1);
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
				liveRoom.setServerGroupId(liveGroupId);
				liveRoom.setMoneyLimitSwitch(false);
				liveRoom.setEnterpriseId(enterPriseId);
				liveRoom.setRoomType(1);
				liveRoom.setDisabled(0);
				liveRoom.setUseState(1);
				iLiveLiveRoomDao.saveRoom(liveRoom);
				ILiveMeetRequest host =new ILiveMeetRequest();
				ILiveMeetRequest student =new ILiveMeetRequest();
				host.setRoomId((int) roomId);
				host.setPassword((int)((Math.random()*9+1)*100000)+"");
				host.setRoomName(liveRoom.getLiveEvent().getLiveTitle());
				host.setType(1);
				student.setRoomId((int) roomId);
				student.setPassword((int)((Math.random()*9+1)*100000)+"");
				student.setType(3);
				student.setRoomName(liveRoom.getLiveEvent().getLiveTitle());
				String hostId=UUID.randomUUID().toString();
				String studentId=UUID.randomUUID().toString();
				host.setId(hostId);
				student.setId(studentId);
				for (int i = 0; i < 2; i++) {
					String ext = "png";
					String tempFileName = System.currentTimeMillis() + "." + ext;
					String realPath = request.getSession().getServletContext().getRealPath("/temp");
					File tempFile = new File(realPath + "/" + tempFileName);
					//TwoDimensionCode.encoderQRCode(ConfigUtils.get("meet_play_url")+"?roomId="+room.getMeetPushRoomId(),  realPath + "/" + tempFileName,"jpg");
					String defaultLogoPath = ConfigUtils.get("default_logo_path");
					String logoPath = FileUtils.getRootPath(defaultLogoPath);
					if(i==0) {
						QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/enterMeet/index.html?roomId="+roomId+"&id="+hostId.substring(0, 6), realPath + "/" + tempFileName, logoPath);
					}else {
						QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/enterMeet/index.html?roomId="+roomId+"&id="+studentId.substring(0, 6), realPath + "/" + tempFileName, logoPath);
					}
					
					String filePath = FileUtils.getTimeFilePath(tempFileName);
					
						boolean result = false;
						ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
						if (uploadServer != null) {
							FileInputStream in;
							try {
								in = new FileInputStream(tempFile);
								result = uploadServer.upload(filePath, in);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						if(tempFile.exists()) {
							tempFile.delete();
						}
						String httpUrl = uploadServer.getHttpUrl() + uploadServer.getFtpPath() + "/" + filePath;
						if(i==0) {
							host.setPic(httpUrl);
						}else {
							student.setPic(httpUrl);
						}
				}
				
					
					
					host.setLoginUrl(ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+roomId);
					
					
					student.setLoginUrl(ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+roomId);
					
		            
				meetRequestMng.save(host);
				meetRequestMng.save(student);
				Map<String,Object> param = new HashMap<String,Object>();
		    	param.put("tenant_id",ConfigUtils.get("tenant_id"));
		    	param.put("apikey",ConfigUtils.get("apikey")); 
		        String message = JSONUtils.toJSONString(param);
		    	String data=HttpRequest.sendPost(ConfigUtils.get("meet_url_check"), message,null);
			    net.sf.json.JSONObject json_result = net.sf.json.JSONObject.fromObject(data);
			    String meetToken=json_result.get("data").toString();
			    Boolean flag= JedisUtils.exists("meetRoomToken");
			    if(!flag) {
			    	JedisUtils.set("meetRoomToken", meetToken, 0);
			    }
			    Map<String,Object> param2 = new HashMap<String,Object>();
			    param2.put("event_id", roomId+"");
			    param2.put("service_type", "2");
			    param2.put("start_time", liveRoom.getLiveEvent().getLiveStartTime().getTime()/1000);
			    param2.put("end_time", liveRoom.getLiveEvent().getLiveEndTime().getTime()/1000);
			    param2.put("inadvance", -1);
			    param2.put("viewer", true);
			    param2.put("record", false);
			    param2.put("tenant_id", ConfigUtils.get("tenant_id"));
			    param2.put("room_type", "1");
			   
		        String message2 = JSONUtils.toJSONString(param2);
			    String data2=HttpRequest.sendPost(ConfigUtils.get("meet_url_create"), message2,meetToken);
			    net.sf.json.JSONObject json_result2 = net.sf.json.JSONObject.fromObject(data2);
			    net.sf.json.JSONObject json_result3 = net.sf.json.JSONObject.fromObject(json_result2.get("data").toString());
			    liveRoom.setMeetId(json_result3.get("event_id").toString());
			    this.update(liveRoom);
		return null;
	}

}
