package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveFCodeDetail;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFCodeMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "config")
public class ILiveRoomConfigController {

	@Autowired
	private ILiveEventMng iLiveEventMng;// 场次管理

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间

	@Autowired
	private ILiveFCodeMng iLiveFCodeMng; // F码

	@Autowired
	private ILiveEnterpriseMemberMng iLiveEnterpriseMemberMng;

	@Autowired
	private ILiveViewWhiteBillMng iLiveViewWhiteBillMng;

	/**
	 * F码详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "fdetail.do")
	public String iLiveConfigFDetail(Model model, Integer roomId, Long codeId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		ILiveFCode liveCode = iLiveFCodeMng.getFCodeById(codeId);
		List<ILiveFCodeDetail> listFCode = iLiveFCodeMng.getDetailByCodeId(codeId);
		model.addAttribute("liveCode", liveCode);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("listFCode", listFCode);
		model.addAttribute("topActive", "1");
		return "liveconfig/fdetailnew";
	}

	@RequestMapping(value = "/enterprise/whitelist.do")
	public String iLiveWhiteList() {

		return null;
	}

	/**
	 * F码设置
	 * 
	 * @return
	 */
	@RequestMapping(value = "fset.do")
	public String iLiveConfigFSet(Model model, Integer roomId, Integer pageNo, String userName) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		Pagination pagination = null;
		pagination = iLiveFCodeMng.getMainPager(userName, cpn(pageNo), 20);
		model.addAttribute("pagination", pagination);
		List<ILiveFCode> liveFCode = iLiveFCodeMng.getAllFCode();
		model.addAttribute("liveFCodeList", liveFCode);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("topActive", "1");
		return "liveconfig/fset";
	}

	/**
	 * logo设置页面跳转
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "logoset.do")
	public String iLiveConfigLogoSet(Model model, Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("leftActive", "2_2");
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("topActive", "1");
		return "liveconfig/LOGOset";
	}

	/**
	 * logo设置页面
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "logosetEdit.do")
	public String iLiveConfigLogoSetEdit(String logoUrl, Integer roomId, Integer openLogoSwitch, Integer logoPosition) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if (openLogoSwitch == 1) {
			liveEvent.setOpenLogoSwitch(1);
			liveEvent.setLogoPosition(logoPosition);
			liveEvent.setLogoUrl(logoUrl);
			switch (logoPosition) {
			case 1:
				liveEvent.setLogoClass("leftTop");
				break;
			case 2:
				liveEvent.setLogoClass("rightTop");
				break;
			case 3:
				liveEvent.setLogoClass("leftBottom");
				break;
			case 4:
				liveEvent.setLogoClass("rightBottom");
				break;
			}
		} else {
			liveEvent.setOpenLogoSwitch(0);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		return "";
	}

	/**
	 * 打赏设置
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "rewardset.do")
	public String iLiveConfigRewardSet(Model model, Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		model.addAttribute("leftActive", "2_5");
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("topActive", "1");
		return "liveconfig/reward_set";
	}

	/**
	 * 倒计时设计页面跳转
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "countdownset.do")
	public String iLiveConfigCountdownSet(Model model, Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		model.addAttribute("leftActive", "2_6");
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("roomId", roomId);
		model.addAttribute("topActive", "1");
		return "liveconfig/countdown_set";
	}

	/**
	 * 倒计时设计
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "countdownsetEdit.do")
	public String iLiveConfigCountdownSetEdit(Model model, Integer roomId, Timestamp time, String countdownTitle,
			Integer openCountdownSwitch) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if (openCountdownSwitch == 1) {
			liveEvent.setLiveStartTime(time);
			liveEvent.setCountdownTitle(countdownTitle);
			liveEvent.setOpenCountdownSwitch(1);
		} else {
			liveEvent.setOpenCountdownSwitch(0);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		return "";
	}

	/**
	 * 数据显示美化
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "datadisplaybeautify.do")
	public String iLiveConfigDatadisplayBeautify(Model model, Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		model.addAttribute("leftActive", "2_7");
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("topActive", "1");
		return "liveconfig/datadisplay_beautify";
	}

	/**
	 * 数据美化功能
	 * 
	 * @param roomId
	 * @param baseNum
	 * @param multiple
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "datadisplaybeautifyEdit.do")
	public String iLiveConfigDatadisplayBeautifyEdit(Integer roomId, Integer baseNum, Integer multiple,
			Integer openDataBeautifySwitch) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if (openDataBeautifySwitch == 1) {
			liveEvent.setBaseNum(baseNum);
			liveEvent.setMultiple(multiple);
			Long currentNum;
			if (liveEvent.getCurrentNum() == null) {
				currentNum = 0l;
				liveEvent.setCurrentNum(currentNum);
			} else {
				currentNum = liveEvent.getCurrentNum();
			}
			Long showNum = baseNum + currentNum * baseNum;
			liveEvent.setShowNum(showNum);
			liveEvent.setOpenDataBeautifySwitch(1);
		} else {
			liveEvent.setOpenDataBeautifySwitch(0);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		return "";
	}

	/**
	 * 推广分享
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "spreadshare.do")
	public String iLiveConfigSpreadShare(Model model, Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		model.addAttribute("leftActive", "2_8");
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("topActive", "1");
		return "liveconfig/spread_share";
	}

	/**
	 * 观看限制
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "livelim.do")
	public String iLiveLim(Integer roomId, Model model, HttpServletRequest request) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		UserBean user = ILiveUtils.getUser(request);
		if (user.getEnterpriseId().equals(ConfigUtils.get("defaultEnterpriseId"))) {
			model.addAttribute("enterprisewhiteList", new ArrayList<>());
			model.addAttribute("viewWhiteList", new ArrayList<>());
		} else {
			Pagination page = iLiveEnterpriseMemberMng.getPage(null, 1, 100, user.getEnterpriseId());
			List<ILiveEnterpriseWhiteBill> enterprisewhiteList = (List<ILiveEnterpriseWhiteBill>) page.getList();
			List<ILiveViewWhiteBill> viewWhiteList = iLiveViewWhiteBillMng
					.getAllViewWhiteBill(liveEvent.getLiveEventId());
			model.addAttribute("enterprisewhiteList", enterprisewhiteList);
			model.addAttribute("viewWhiteList", viewWhiteList);

		}
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("leftActive", "2_3");
		model.addAttribute("topActive", "1");
		return "liveconfig/livelim";
	}

	/**
	 * 免费观看
	 * 
	 * @param roomId
	 * @param is_login
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "freeLogin.do")
	public String freeLogin(Integer roomId, Integer is_login, Model model) {
		ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = liveRoom.getLiveEvent();
		if (is_login == null) {
			liveEvent.setViewAuthorized(1);
		} else {
			liveEvent.setViewAuthorized(5);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		model.addAttribute("iLiveLiveRoom", liveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("liveEvent", liveEvent);
		return "liveconfig/livelim";
	}

	/**
	 * 密码观看
	 * 
	 * @param roomId
	 * @param viewPassword
	 * @param welcomeMsg
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "passwordLogin.do")
	public String passwordLogin(Integer roomId, String viewPassword, String welcomeMsg, Model model) {
		ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = liveRoom.getLiveEvent();
		liveEvent.setViewAuthorized(2);
		liveEvent.setViewPassword(viewPassword);
		liveEvent.setWelcomeMsg(welcomeMsg);
		iLiveEventMng.updateILiveEvent(liveEvent);
		model.addAttribute("iLiveLiveRoom", liveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("topActive", "1");
		return "liveconfig/livelim";
	}

	/**
	 * 支付观看
	 * 
	 * @param model
	 * @param roomId
	 * @param viewMoney
	 * @param welcomeMsg
	 * @param openFCodeSwitch
	 * @param is_openF
	 * @return
	 */
	@RequestMapping(value = "payView.do")
	public String pagView(Model model, Integer roomId, Double viewMoney, String welcomeMsg, Integer openFCodeSwitch,
			Integer is_openF) {
		ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = liveRoom.getLiveEvent();
		liveEvent.setViewAuthorized(3);
		liveEvent.setViewMoney(viewMoney);
		liveEvent.setWelcomeMsg(welcomeMsg);
		if (is_openF != null) {
			openFCodeSwitch = 0;
			liveEvent.setOpenFCodeSwitch(openFCodeSwitch);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		model.addAttribute("iLiveLiveRoom", liveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("liveEvent", liveEvent);
		return "liveconfig/livelim";
	}

	/**
	 * 添加F码
	 * 
	 * @param liveFCode
	 * @param codeNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addFCode.do")
	public String addFCode(ILiveFCode liveFCode, Integer codeNum) {
		System.out.println(liveFCode.getCodeName() + "   " + codeNum);
		Timestamp t1 = new Timestamp(new Date().getTime());
		Timestamp t2 = new Timestamp(new Date().getTime());
		Timestamp t3 = new Timestamp(new Date().getTime());
		liveFCode.setStartTime(t1);
		liveFCode.setEndTime(t2);
		liveFCode.setConsumerTime(t3);
		Long id = iLiveFCodeMng.saveFCode(liveFCode);
		return id + "";
	}

	/**
	 * 删除F码
	 * 
	 * @param codeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "removeFCode.do")
	public String removeFCode(Long codeId) {
		iLiveFCodeMng.deleteByCodeId(codeId);
		return "success";
	}

	/**
	 * 白名单观看
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "whiteList.do")
	public String whiteList(Model model, Integer roomId) {
		ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = liveRoom.getLiveEvent();
		liveEvent.setViewAuthorized(4);

		iLiveEventMng.updateILiveEvent(liveEvent);
		model.addAttribute("iLiveLiveRoom", liveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("topActive", "1");
		return "liveconfig/livelim";
	}

	private Integer selectServerGroup() {
		return 100;
	}

	@ResponseBody
	@RequestMapping(value = "playconfigEdit.do")
	public String playconfigEdit(Integer roomId, String readyPlayAddress, Integer openReadyPlayAddressSwitch) {
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		if (openReadyPlayAddressSwitch == 1) {
			liveEvent.setOpenReadyPlayAddressSwitch(1);
			liveEvent.setReadyPlayAddress(readyPlayAddress);
		} else {
			liveEvent.setOpenReadyPlayAddressSwitch(0);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		return "SUCCESS";
	}

}
