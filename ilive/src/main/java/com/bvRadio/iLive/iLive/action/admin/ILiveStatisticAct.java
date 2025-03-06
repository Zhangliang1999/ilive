package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.RandomUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "statistic")
public class ILiveStatisticAct {
	@RequestMapping("/getAuth.do")
	public void index(HttpServletResponse response, HttpServletRequest request, ModelMap model) {
		String authString = getAuthString(request);
		JSONObject resultJson = new JSONObject();
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "成功");
		resultJson.put("data", authString);
		ResponseUtils.renderJson(request, response, resultJson.toString());
		return;
	}

	@RequestMapping(value = "/liveList.do")
	public String liveList(Integer roomId, HttpServletRequest request, ModelMap model) {
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = roomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		// 登录用户
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		model.addAttribute("userBean", userBean);
		String statisticHomeUrl = ConfigUtils.get("statistic_home_url");
		model.addAttribute("statisticHomeUrl", statisticHomeUrl);
		String authString = getAuthString(request);
		//查询用户是否有用户观看数据明细查询权限
    	Integer certStatus = iLiveEnterprise.getCertStatus();
		boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_UserViewingData,certStatus);
		if(b){
			model.addAttribute("UserViewingData", "0");
		}else{
			model.addAttribute("UserViewingData", "1");
		}
		//查询用户是否有观看数据导出权限
		boolean b1 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_ViewData,certStatus);
		if(b1){
			model.addAttribute("ViewData", "0");
		}else{
			model.addAttribute("ViewData","1");
		}
		model.addAttribute("authString", authString);
		model.addAttribute("roomId", roomId);
		model.addAttribute("topActive", "1");
		model.addAttribute("leftActive", "6_3");
		return "statistic/liveList";
	}

	@RequestMapping(value = "/userViewLogList.do")
	public String userViewLogList(Integer roomId, HttpServletRequest request, ModelMap model) {
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = roomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		// 登录用户
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		model.addAttribute("userBean", userBean);
		String statisticHomeUrl = ConfigUtils.get("statistic_home_url");
		model.addAttribute("statisticHomeUrl", statisticHomeUrl);
		String authString = getAuthString(request);
		//查询用户是否有用户观看数据明细查询权限
    	Integer certStatus = iLiveEnterprise.getCertStatus();
		boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_UserViewingData,certStatus);
		if(b){
			model.addAttribute("UserViewingData", 0);
		}else{
			model.addAttribute("UserViewingData", 1);
		}
		model.addAttribute("authString", authString);
		model.addAttribute("roomId", roomId);
		model.addAttribute("topActive", "1");
		model.addAttribute("leftActive", "6_2");
		return "statistic/userViewLogList";
	}

	@RequestMapping(value = "/videoDetail.do")
	public String videoDetail(Long videoId,Integer roomId, HttpServletRequest request, ModelMap model) {
		// 直播间获取
		//ILiveLiveRoom iLiveLiveRoom = roomMng.findById(roomId);
		//model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		
		String statisticHomeUrl = ConfigUtils.get("statistic_home_url");
		model.addAttribute("statisticHomeUrl", statisticHomeUrl);
		String authString = getAuthString(request);
		model.addAttribute("authString", authString);
		model.addAttribute("videoId", videoId);
		model.addAttribute("topActive", "1");
		model.addAttribute("leftActive", "5_1");
		return "statistic/videoDetail";
	}

	@RequestMapping(value = "/enterpriseDetail.do")
	public String enterpriseDetail(HttpServletRequest request, ModelMap model) {
		// 登录用户
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		model.addAttribute("userBean", userBean);
		String statisticHomeUrl = ConfigUtils.get("statistic_home_url");
		model.addAttribute("statisticHomeUrl", statisticHomeUrl);
		String authString = getAuthString(request);
		model.addAttribute("authString", authString);
		model.addAttribute("enterpriseId", enterpriseId);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "8_3");
		return "statistic/enterpriseDetail";
	}

	private String getAuthString(HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		StringBuilder roomIdsBuilder = new StringBuilder();
		String queryString = request.getQueryString();
		if (user != null) {
			roomIdsBuilder.append(",");
			Integer enterpriseId = user.getEnterpriseId();
			List<ILiveLiveRoom> roomList = roomMng.findRoomListByEnterprise(enterpriseId);
			if (null != roomList) {
				for (ILiveLiveRoom liveRoom : roomList) {
					if (null != liveRoom) {
						Integer roomId = liveRoom.getRoomId();
						if (null != roomId) {
							roomIdsBuilder.append(roomId);
							roomIdsBuilder.append(",");
						}
					}
				}
			}
			queryString = "enterpriseId=" + enterpriseId + "&roomId=" + roomIdsBuilder.toString();
		}

		String timeStr = System.currentTimeMillis() / 1000 + "";
		String encodeMd5Str = Md5Util.encode(timeStr + "_chinanet_2018_jwzt_" + queryString);
		String authString = RandomUtils.getRadomNum(4) + timeStr + RandomUtils.getRadomNum(4) + "@" + queryString + "@"
				+ encodeMd5Str;
		try {
			authString = URLEncoder.encode(authString, "utf-8");
		} catch (Exception e) {
		}
		return authString;
	}

	@Autowired
	private ILiveLiveRoomMng roomMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
}
