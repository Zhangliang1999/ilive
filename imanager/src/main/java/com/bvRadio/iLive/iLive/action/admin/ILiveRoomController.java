package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.common.web.springmvc.DateTypeEditor;
import com.bvRadio.iLive.iLive.config.SystemXMLTomcatUrl;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveGuests;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveGuestsMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "room")
public class ILiveRoomController {

	@Autowired
	private ILiveEstoppelMng iLiveEstoppelMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	/**
	 * 直播间列表页
	 * 
	 * @param roomName
	 * @param roomStatus
	 * @param pageNo
	 * @param pageSize
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "list.do")
	public String iLiveRoomList(String q, Integer s, Integer pageNo, Integer pageSize, ModelMap map,
			HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			Pagination pager = iLiveLiveRoomMng.getPager(q, s, Long.valueOf(user.getUserId()), cpn(pageNo), 20);
			if (s == null) {
				s = ILivePlayStatusConstant.NONE_STATE;
			}
			map.addAttribute("roomStatus", s);
			map.addAttribute("roomName", q == null ? "" : q);
			map.addAttribute("pager", pager);
			map.addAttribute("topActive", "1");
			return "liveroom/room_list";
		} else {
			return "liveroom/room_list";
		}
	}

	/**
	 * 进入直播间创建
	 * 
	 * @param event
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "create.do")
	public String iLiveRoomCreate(ILiveEvent event, HttpServletRequest request, ModelMap modelMap) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			// 选择直播服务器组
			Integer serverGroupId = this.selectServerGroup();
			long roomId = filedIdMng.getNextId("ilive_live_room", "room_id", 1);
			ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(serverGroupId);
			modelMap.addAttribute("roomId", roomId);
			modelMap.addAttribute("serverGroupId", serverGroupId);
			String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
			modelMap.addAttribute("liveAddr", liveAddr);
			String playAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/pc" + "/" + roomId + ".html";
			modelMap.addAttribute("playAddr", playAddr);
			modelMap.addAttribute("topActive", "1");
			return "liveroom/room_create";
		} else {
			return "liveroom/room_list";
		}
	}

	private final static String RTMP_PROTOACAL = "rtmp://";

	@RequestMapping(value = "edit.do")
	public String iLiveRoomEdit(Integer roomId, HttpServletRequest request, ModelMap modelMap) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			// 选择直播服务器组
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			modelMap.addAttribute("iliveRoom", iliveRoom);
			ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			modelMap.addAttribute("roomId", roomId);
			modelMap.addAttribute("serverGroupId", iliveRoom.getServerGroupId());
			String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
			modelMap.addAttribute("liveAddr", liveAddr);
			String playAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/play" + "/" + roomId + ".html";
			modelMap.addAttribute("playAddr", playAddr);
			modelMap.addAttribute("leftActive", "2_1");
			modelMap.addAttribute("topActive", "1");
			return "liveroom/room_edit";
		} else {
			return null;
		}
	}

	/**
	 * 删除
	 * 
	 * @param roomId
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "delete.do")
	public String iLiveRoomDelete(Integer roomId, HttpServletRequest request, ModelMap modelMap) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			// 选择直播服务器组
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setDeleteStatus(1);
			iLiveLiveRoomMng.update(iliveRoom);
			return "redirect:list.do";
		} else {
			return "redirect:list.do";
		}
	}

	/**
	 * 关闭直播间
	 * 
	 * @param roomId
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "close.do")
	public void iLiveRoomClose(Integer roomId, HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		JSONObject jsonObject = new JSONObject();
		if (perm) {
			// 选择直播服务器组
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setOpenStatus(0);
			iLiveLiveRoomMng.update(iliveRoom);
			jsonObject.put("status", "1");
		} else {
			jsonObject.put("status", "0");
		}
		ResponseUtils.renderJson(response, jsonObject.toString());
	}

	/**
	 * 开启直播间
	 * 
	 * @param roomId
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "open.do")
	public void iLiveRoomOpen(Integer roomId, HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		JSONObject jsonObject = new JSONObject();
		if (perm) {
			// 选择直播服务器组
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setOpenStatus(1);
			iLiveLiveRoomMng.update(iliveRoom);
			jsonObject.put("status", "1");
		} else {
			jsonObject.put("status", "1");
		}
		ResponseUtils.renderJson(response, jsonObject.toString());
	}

	/**
	 * 修改直播间
	 * 
	 * @param event
	 * @param iLiveRoomId
	 * @param liveEventSwitch
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "update.do")
	public String iLiveRoomUpdate(ILiveEvent event, Integer iLiveRoomId, Integer liveEventSwitch,
			HttpServletRequest request, ModelMap modelMap) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			// 选择直播服务器组
			iLiveLiveRoomMng.update(event, iLiveRoomId, liveEventSwitch);
			return "redirect:list.do";
		} else {
			return null;
		}
	}

	private Integer selectServerGroup() {
		return 100;
	}

	/**
	 * 生成直播间
	 * 
	 * @param iLiveEvent
	 * @param liveRoomId
	 * @param serverGroupId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "finish_create.do")
	public String createRoom(ILiveEvent iLiveEvent, Integer liveRoomId, Integer serverGroupId,
			HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			iLiveLiveRoomMng.saveRoom(iLiveEvent, liveRoomId, serverGroupId, user);
			return "redirect:list.do";
		} else {
			return null;
		}
	}

	/**
	 * 校验用户去是否有权限
	 * 
	 * @param user
	 * @return
	 */
	private boolean validatorUserPerm(UserBean user) {
		return true;
	}

	/**
	 * 进入房间
	 * 
	 * @param liveRoomId
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "getroom.do")
	public String updateRoom(Integer liveRoomId, HttpServletRequest request, ModelMap modelMap) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			ILiveLiveRoom liveRoom = iLiveLiveRoomMng.getIliveRoom(liveRoomId);
			modelMap.addAttribute("liveRoom", liveRoom);
			return "redirect:list.do";
		} else {
			return null;
		}
	}

	/**
	 * 修改房间信息
	 * 
	 * @param iLiveEvent
	 * @param liveRoomId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "update_rooom.do")
	public String updateRoom(ILiveEvent iLiveEvent, Integer liveRoomId, HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			iLiveLiveRoomMng.updateRoom(iLiveEvent, user);
			return "redirect:list.do";
		} else {
			return null;
		}
	}

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间

	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;

	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;

	@Autowired
	private ILiveGuestsMng iLiveGuestsMng;// 嘉宾

	/**
	 * 进入直播间
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "selectRoom.do")
	public String selectILiveRoom(Integer roomId, ModelMap model, HttpServletRequest request) {
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		ILiveEvent ILiveEvent = iLiveLiveRoom.getLiveEvent();
		// 直播场次获取
		// ILiveEvent ILiveEvent =
		// iLiveEventMng.selectILiveEventByID(liveEventId);
		model.addAttribute("ILiveEvent", ILiveEvent);
		// 登录用户
		UserBean userBean = ILiveUtils.getUser(request);
		model.addAttribute("userBean", userBean);
		// 虚拟网友
		try {
			List<UserBean> outUserBeanXml = SystemXMLTomcatUrl.outUserBeanXml();
			model.addAttribute("outUserBeanXml", outUserBeanXml);
		} catch (Exception e) {
			model.addAttribute("outUserBeanXml", new ArrayList<UserBean>());
		}
		// 嘉宾
		List<ILiveGuests> list = iLiveGuestsMng.selectILiveGuestsList(roomId, false);
		/**
		 * 获取直播间推流地址
		 */
		ILiveServerAccessMethod accessMethodBySeverGroupId = null;
		try {
			accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		model.addAttribute("iLiveGuestsList", list);
		String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
		List<PageDecorate> pageDecorateList = pageDecorateMng.getList(ILiveEvent.getLiveEventId());
		List<PageDecorate> filterList = this.filterPageDecorate(pageDecorateList);
		model.addAttribute("pageDecorateList", filterList);
		Gson gson = new Gson();
		model.addAttribute("decorateJSON", gson.toJson(filterList));
		model.addAttribute("liveAddr", liveAddr);
		model.addAttribute("pushStreamAddr", pushStreamAddr);
		model.addAttribute("leftActive", "1_1");
		model.addAttribute("topActive", "1");
		List<Map<String, String>> faceLists = ExpressionUtil.getAll();
		model.addAttribute("faceLists", faceLists);
		return "liveroom/room";
	}

	private List<PageDecorate> filterPageDecorate(List<PageDecorate> pageDecorateList) {
		List<PageDecorate> filterList = new ArrayList<>();
		if (pageDecorateList != null && !pageDecorateList.isEmpty()) {
			for (PageDecorate page : pageDecorateList) {
				if (page.getMenuType() == 4 || page.getMenuType() == 5) {
					continue;
				}
				filterList.add(page);
			}
		}
		return filterList;
	}

	/**
	 * 直播间禁言列表
	 * 
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "banlist.do")
	public String liveBan(Integer roomId, Integer pageNo, ModelMap modelMap) {
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		// 获取禁言列表
		Pagination pagination = iLiveEstoppelMng.getPager(iLiveLiveRoom.getRoomId(), cpn(pageNo), 20);
		modelMap.addAttribute("pagination", pagination);
		modelMap.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		modelMap.addAttribute("leftActive", "7_1");
		modelMap.addAttribute("topActive", "1");
		return "liveroommgr/liveban";
	}

	/**
	 * 直播间消费上限列表
	 * 
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "consumption_limit.do")
	public String consumeLimit(Integer roomId, ModelMap modelMap) {
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		modelMap.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		modelMap.addAttribute("leftActive", "7_2");
		modelMap.addAttribute("topActive", "1");
		return "liveroommgr/consumption_limit";
	}

	/**
	 * 直播间消费上限修改
	 * 
	 * @param roomId
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "consumption_limit_update.do")
	public void consumeLimitUpdate(Integer roomId, ModelMap modelMap, Double affordLimit, Integer moneyLimitSwitch,
			HttpServletResponse response) {
		// 直播间获取
		JSONObject jo = new JSONObject();
		try {
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			iLiveLiveRoom.setMoneyLimitSwitch(moneyLimitSwitch == 1);
			iLiveLiveRoom.setAffordLimit(affordLimit);
			iLiveLiveRoomMng.update(iLiveLiveRoom);
			jo.put("status", 1);
		} catch (Exception e) {
			jo.put("status", 0);
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, jo.toString());

	}

	/**
	 * 直播间协同办公人员管理
	 * 
	 * @param
	 */
	@RequestMapping(value = "cooperation_mgr.do")
	public String liveBan(Integer roomId, ModelMap modelMap) {
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		modelMap.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		modelMap.addAttribute("leftActive", "7_3");
		modelMap.addAttribute("topActive", "1");
		return "liveroommgr/cooperation_mgr";
	}

	/**
	 * 时间转换绑定
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(ILiveEvent.class, new DateTypeEditor());
	}
}
