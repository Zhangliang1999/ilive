package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.InputSource;

import com.alibaba.druid.support.json.JSONUtils;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.common.web.springmvc.DateTypeEditor;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.config.SystemXMLTomcatUrl;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.entity.ILiveAPIGateWayRouter;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;
import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.IliveRoomThird;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveAPIGateWayRouterMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEstoppelMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMeetRequestMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveRandomRecordTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveRollingAdvertisingMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomThirdMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.util.ConvertThread;
import com.bvRadio.iLive.iLive.util.DownloadUtil;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.HttpRequest;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.QrCodeUtils;
import com.bvRadio.iLive.iLive.util.ResponseJson;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.util.TwoDimensionCode;
import com.bvRadio.iLive.iLive.util.WeightRandom;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.Gson;
import com.jwzt.ums.api.UmsApi;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "room")
public class ILiveRoomController {

	private final static Logger logger = LoggerFactory.getLogger(ILiveRoomController.class);
	
	@Autowired
	private ILiveEstoppelMng iLiveEstoppelMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	@Autowired
	private ILiveRandomRecordTaskMng iLiveRandomRecordTaskMng;

	@Autowired
	private ILiveAPIGateWayRouterMng iLiveAPIGateWayRouterMng;
	
	@Autowired
	private ILiveRollingAdvertisingMng iLiveRollingAdvertisingMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	
	@Autowired
	private WorkLogMng workLogMng;
	
	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@Autowired
	private ILiveRoomThirdMng iLiveRoomThirdMng;
	 
	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	@Autowired
	private ILiveMeetRequestMng meetRequestMng;
	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;
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
	public String iLiveRoomList(String q, Integer s, Integer pageNo, Integer roomType,Integer pageSize, ModelMap map,
			HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		Integer certStatus = null;
		if(iLiveEnterPrise!=null){
			certStatus = iLiveEnterPrise.getCertStatus();
		}
		try {
			EnterpriseUtil.selectEnterpriseCache(enterpriseId, certStatus);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			Integer roomId = user.getRoomId()==null?0:user.getRoomId();
			if(roomId!=0&&user.getLevel().equals(ILiveManager.USER_LEVEL_Collaborative)){
				return "redirect:selectRoom.do?roomId="+roomId;
			}else{
				if (q != null && !"".equals(q)) {
					try {
						q = URLDecoder.decode(q, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_createLive, certStatus);
				if(b){
					map.addAttribute("enterpriseType", 0);
					map.addAttribute("enterpriseContent", EnterpriseCache.ENTERPRISE_FUNCTION_createLive_Content);
				}else{
					map.addAttribute("enterpriseType", 1);
					map.addAttribute("enterpriseContent", "");
				}
				//查询子账户是否具有直播间权限
				boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_liveRoom);
				Integer defaultEnterpriseId = Integer.parseInt(ConfigUtils.get("defaultEnterpriseId"));
				ILiveEnterprise enterPrise = iLiveEnterpriseMng.getdefaultEnterprise();
				Integer enterpriseId2 = enterPrise.getEnterpriseId();
				if(enterpriseId==defaultEnterpriseId||enterpriseId==enterpriseId2){
					Pagination pager = iLiveLiveRoomMng.getPager(q, s, roomType,Long.valueOf(user.getUserId()), cpn(pageNo), 20);
					map.addAttribute("pager", pager);
				}else{
					if(user.getLevel().equals(ILiveManager.USER_LEVEL_SUB)&&!per){
						Pagination pager = iLiveLiveRoomMng.getPagerEnterpriseId(q, s,roomType, enterpriseId, cpn(pageNo), 20,Long.valueOf(user.getUserId()));
						map.addAttribute("pager", pager);
						map.addAttribute("totalPage", pager.getTotalPage());
					}else{
						Pagination pager = iLiveLiveRoomMng.getPagerEnterpriseId(q, s, roomType,enterpriseId, cpn(pageNo), 20,null);
						map.addAttribute("pager", pager);
						map.addAttribute("totalPage", pager.getTotalPage());
					}
				}
				
				if (s == null) {
					s = ILivePlayStatusConstant.NONE_STATE;
				}
				map.addAttribute("roomStatus", s);
				map.addAttribute("roomType", roomType);
				if (s == ILivePlayStatusConstant.CLOSE_STATE) {
					map.addAttribute("openStatus", 0);
				} else {
					map.addAttribute("openStatus", 1);
				}
				List<ILiveAPIGateWayRouter> routerList = iLiveAPIGateWayRouterMng.getRouterList();
				String apiRouterUrl = "";
				if (routerList != null && !routerList.isEmpty()) {
					ILiveAPIGateWayRouter iLiveAPIGateWayRouter = routerList.get(0);
					apiRouterUrl = iLiveAPIGateWayRouter.getRouterUrl();
				}
				map.addAttribute("apiRouterUrl", apiRouterUrl);
				map.addAttribute("h5Base", ConfigUtils.get("free_login_review"));
				map.addAttribute("roomName", q == null ? "" : q);
				map.addAttribute("topActive", "1");
				map.addAttribute("subTop", "1");
				map.addAttribute("user", user);
				map.addAttribute("meeting_switch",ConfigUtils.get("meeting_switch"));
				map.addAttribute("apiMeetUrl",ConfigUtils.get("meet_url_enterMeet"));
				map.addAttribute("tenant_id",ConfigUtils.get("tenant_id"));
				return "liveroom/room_list";
			}
		} else {
			return "liveroom/room_list";
		}
	}

	@RequestMapping(value = "checkstarttime.do")
	public void checkStartTimeValid(String startTime, HttpServletResponse response) {
		long currentTimeMillis = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		JSONObject resultJson = new JSONObject();
		try {
			Date parseDate = sdf.parse(startTime);
			long time = parseDate.getTime();
			if (time < currentTimeMillis) {
				resultJson.put("code", "0");
				resultJson.put("message", "设置为预告状态时,开始时间必须大于当前时间");
				resultJson.put("data", "0");
			} else {
				resultJson.put("code", "1");
				resultJson.put("message", "成功");
				resultJson.put("data", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 修改直播间logo和背景图
	 * 
	 */
	@RequestMapping(value = "updateLogo.do")
	public void updateLogo(Integer roomId,String logoImg,String bgdImg, HttpServletResponse response) {
		
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
			if(logoImg==null||"".equals(logoImg)) {
				liveRoom.setLogoImg(null);
			}else {
				liveRoom.setLogoImg(logoImg);
			}
			if(bgdImg==null||"".equals(bgdImg)) {
				liveRoom.setBgdImg(null);
			}else {
				liveRoom.setBgdImg(bgdImg);
			}
			
			iLiveLiveRoomMng.update(liveRoom);
			resultJson.put("code", 1);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 0);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 获取现在直播间观看权限
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getliveroomlevel.do")
	public void getliveroomlevel(Integer roomId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
		Integer code=liveRoom.getLiveEvent().getNeedLogin();
		Integer view=liveRoom.getLiveEvent().getViewAuthorized();
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("date", code+""+view);
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 保存第三方推流信息
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/saveThird.do")
	public void saveThird(Integer roomId,String thirdName,String rtmpAddr,String liveCode,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		IliveRoomThird iliveRoomThird=new IliveRoomThird();
		Integer maxid=iLiveRoomThirdMng.selectMaxId();
		if(maxid==null) {
			maxid=1;
		}else {
			maxid=maxid+1;
		}
		iliveRoomThird.setId(maxid);
		iliveRoomThird.setRoomId(roomId);
		iliveRoomThird.setThirdName(thirdName);
		iliveRoomThird.setRtmpAddr(rtmpAddr);
		iliveRoomThird.setLiveCode(liveCode);
		iliveRoomThird.setTstatus(1);
		iLiveRoomThirdMng.save(iliveRoomThird);
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("msg", "保存成功");
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 保存第三方推流信息
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getUpdateThirdMsg.do")
	public void getUpdateThirdMsg(Integer id,String liveCode,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		List<IliveRoomThird> list=iLiveRoomThirdMng.getILiveRoomThirdById(id);
		if(list!=null&&list.size()>0) {
			resultJson.put("date", list);
		}
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("msg", "保存成功");
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 修改第三方推流信息
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/updateThird.do")
	public void updateThird(Integer id,String rtmpAddr,String liveCode,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		IliveRoomThird list=iLiveRoomThirdMng.getRoomThird(id);
		list.setRtmpAddr(rtmpAddr);
		list.setLiveCode(liveCode);
		iLiveRoomThirdMng.update(list);
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("msg", "保存成功");
		resultJson.putOnce("date", list);
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 删除第三方推流信息
	 * @param pageNo
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/deleteThird.do")
	public void deleteThird(Integer id,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		iLiveRoomThirdMng.delete(id);
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("msg", "保存成功");
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 开始/结束第三方推流信息
	 * @param roomId
	 * @param open 开始 1 结束 0 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/startOrStopThird.do")
	public void startOrStopThird(Integer roomId,Integer open,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		String SSrcAddr="";
		Integer status=null;
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
				.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
		UmsApi umsApi = new UmsApi(accessMethod.getOrgLiveHttpUrl()
				, String.valueOf(accessMethod.getUmsport()));
		String SMountName = "live" + roomId + "_tzwj_5000k";
		if(open==1) {
			status=2;
			List<IliveRoomThird> list=iLiveRoomThirdMng.selectILiveRoomThirdById(roomId);
			if(list!=null&&list.size()>0) {
				for(int i=0;i<list.size();i++) {
					SSrcAddr=SSrcAddr+list.get(i).getRtmpAddr()+"/"+list.get(i).getLiveCode()+";";
				}
			}
		}else {
			status=3;
		}
		boolean ret=umsApi.SetRelayAddrThird("1", "live", SMountName, SSrcAddr);
		if(ret) {
			//修改推流状态为推流中
			iLiveRoomThirdMng.updateStatues(roomId,status);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("msg", "第三方推流成功");
		}else {
			resultJson.put("code", 0);
			resultJson.put("msg", "第三方推流失败");
		}
		
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	/**
	 * 进入直播间创建
	 * 
	 * @param event
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "create.do")
	public String iLiveRoomCreate(ILiveEvent event,HttpServletRequest request, ModelMap modelMap) {
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
			String playAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/pc" + "/pc.html?roomId=" + roomId;
			modelMap.addAttribute("playAddr", playAddr);
			modelMap.addAttribute("topActive", "1");
			return "liveroom/room_create";
		} else {
			return "liveroom/room_list";
		}
	}
	/**
	 * 进入会议创建
	 * 
	 * @param event
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "createMeet.do")
	public String createMeet(ILiveEvent event,HttpServletRequest request, ModelMap modelMap) {
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
			return "liveroom/meet_room_create";
		} else {
			return "liveroom/room_list";
		}
	}

	private final static String RTMP_PROTOACAL = "rtmp://";

	private static final String HTTP_PROTOCAL = "http://";

	private final static ConcurrentHashMap<Integer, Integer> statusMap = 
			new ConcurrentHashMap<>();
	
	
	@RequestMapping(value = "edit.do")
	public String iLiveRoomEdit(Integer roomId, HttpServletRequest request, ModelMap modelMap) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			// 选择直播服务器组
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			if(iliveRoom.getLiveEvent()!=null) {
				statusMap.put(roomId,iliveRoom.getLiveEvent().getLiveStatus());
			}
			modelMap.addAttribute("iliveRoom", iliveRoom);
			ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
			modelMap.addAttribute("roomId", roomId);
			modelMap.addAttribute("iLiveLiveRoom", iliveRoom);
			modelMap.addAttribute("serverGroupId", iliveRoom.getServerGroupId());
			modelMap.addAttribute("liveAddr", liveAddr);
			String playAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/play" + "/" + roomId + ".html";
			modelMap.addAttribute("playAddr", playAddr);
			String returnUrl ;
			returnUrl ="liveroom/room_edit";
			modelMap.addAttribute("leftActive", "2_1");
			modelMap.addAttribute("topActive", "1");
			return returnUrl;
		} else {
			return null;
		}
	}
	
	@RequestMapping(value = "meetEdit.do")
	public String meetRoomEdit(Integer roomId, HttpServletRequest request, ModelMap modelMap) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			// 选择直播服务器组
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			if(iliveRoom.getLiveEvent()!=null) {
				statusMap.put(roomId,iliveRoom.getLiveEvent().getLiveStatus());
			}
			modelMap.addAttribute("iliveRoom", iliveRoom);
			ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
			modelMap.addAttribute("roomId", roomId);
			modelMap.addAttribute("iLiveLiveRoom", iliveRoom);
			modelMap.addAttribute("serverGroupId", iliveRoom.getServerGroupId());
			modelMap.addAttribute("liveAddr", liveAddr);
			String playAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/play" + "/" + roomId + ".html";
			modelMap.addAttribute("playAddr", playAddr);
			String returnUrl ;
			returnUrl ="liveroom/meet_edit";
			modelMap.addAttribute("leftActive", "8_1");
			modelMap.addAttribute("topActive", "1");
			return returnUrl;
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
			// 通知有删除直播间
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(3);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			if (userMap != null) {
				Iterator<String> userIterator = userMap.keySet().iterator();
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean userBean = userMap.get(key);
					List<ILiveMessage> userMsgList = userBean.getMsgList();
					if (userMsgList == null) {
						userMsgList = new ArrayList<ILiveMessage>();
					}
					userMsgList.add(iLiveMessage);
					userBean.setMsgList(userMsgList);
					userMap.put(key, userBean);
				}
			}
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			chatInteractiveMap.remove(roomId);
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
			chatInteractiveMapNO.remove(roomId);
			Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
			quizLiveMap.remove(roomId);
			Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
			roomListMap.remove(roomId);
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

			// 通知有删除直播间
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(3);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> userMap = userListMap.get(roomId);
			if (userMap != null) {
				Iterator<String> userIterator = userMap.keySet().iterator();
				while (userIterator.hasNext()) {
					String key = userIterator.next();
					UserBean userBean = userMap.get(key);
					List<ILiveMessage> userMsgList = userBean.getMsgList();
					if (userMsgList == null) {
						userMsgList = new ArrayList<ILiveMessage>();
					}
					userMsgList.add(iLiveMessage);
					userBean.setMsgList(userMsgList);
					userMap.put(key, userBean);
				}
			}

			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = ApplicationCache.getChatInteractiveMap();
			chatInteractiveMap.remove(roomId);
			Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = ApplicationCache.getChatInteractiveMapNO();
			chatInteractiveMapNO.remove(roomId);
			Hashtable<Integer, List<ILiveMessage>> quizLiveMap = ApplicationCache.getQuizLiveMap();
			quizLiveMap.remove(roomId);
			Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
			roomListMap.remove(roomId);
			
			iLiveLiveRoomMng.update(iliveRoom);
			workLogMng.save(new WorkLog(WorkLog.MODEL_ENDLIVE, iliveRoom.getRoomId()+"", net.sf.json.JSONObject.fromObject(iliveRoom).toString(), WorkLog.MODEL_ENDLIVE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
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
			workLogMng.save(new WorkLog(WorkLog.MODEL_STARTLIVE, iliveRoom.getRoomId()+"", net.sf.json.JSONObject.fromObject(iliveRoom).toString(), WorkLog.MODEL_STARTLIVE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			jsonObject.put("code", "1");
		} else {
			jsonObject.put("code", "1");
//			jsonObject.put("date", "您已无有效的直播时长，请前往认证或者订购套餐。如需帮助，请联系客服118010。");
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
		if(JedisUtils.exists(("roomInfo:"+iLiveRoomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+iLiveRoomId);
		}
		final ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(iLiveRoomId);
		if(event.getLiveStatus()==0) {
			final String converTaskLogo=ConfigUtils.get("conver_task_logo");
			if(iliveRoom.getOpenLogoSwitch()!=null&&iliveRoom.getOpenLogoSwitch()==1) {
				if(iliveRoom.getConvertTaskId()!=null&&iliveRoom.getConvertTaskId()>0) {
					
				}else {
					try {
						
			          //创建一个SAXBuilder对象
			              SAXBuilder saxBuilder = new SAXBuilder();            
			            //读取prop.xml资源
			              Document Doc = saxBuilder.build(request.getSession().getServletContext().getRealPath("/temp")+"/xml/4.xml");
			            //获取根元素(prop)
			            Element Root = Doc.getRootElement();
			            
			            Element name=  Root.getChild("name");
			            name.setText(iliveRoom.getLiveEvent().getLiveTitle());
			            Element uri=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("uri");
			           
			            uri.setText(iliveRoom.getConvertLogo());
			            Element rtmpuri=  Root.getChild("inputs").getChild("network").getChild("uri");
			            ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			    		try {
			    			accessMethodBySeverGroupId = accessMethodMng
			    					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			    		} catch (Exception e) {
			    			e.printStackTrace();
			    		}
			    		String pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
			    				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + iLiveRoomId + "_tzwj_5000k";
			    		
			            String[] addr= pushStreamAddr.split("_");
			            rtmpuri.setText(addr[0]+"o_"+addr[1]+"_"+addr[2]);
			            Element Top=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("top");
			            Top.setText(iliveRoom.getLogoWidth()+"");
			            Element Left=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("left");
			            Left.setText(iliveRoom.getLogoHight()+"");
			            Element rtmpuri1=  Root.getChild("outputgroups").getChild("flashstreaming").getChild("uri");
			            rtmpuri1.setText(pushStreamAddr);
			            //格式化输出xml文件字符串
						Format format = Format.getCompactFormat();
						format.setEncoding("UTF-8");
						//这行保证输出后的xml的格式
						format.setIndent(" ");
						// 创建xml输出流操作类
						XMLOutputter xmlout = new XMLOutputter(format);
						ByteArrayOutputStream byteRsp = new ByteArrayOutputStream();
						xmlout.output(Doc, byteRsp);
						String str = byteRsp.toString("utf-8");
			            String data1=HttpUtils.sendStr(converTaskLogo+"/api/task", "POST", str);
			            Integer taskId=getTaskId(data1);
			            String res  =HttpUtils.sendStr(converTaskLogo+"/api/task/"+taskId+"/start", "PUT", "");
			            if(taskId!=null) {
			            	iliveRoom.setConvertTaskId(taskId);
			            }
			            
					} catch (JDOMException | IOException e) {
						
						e.printStackTrace();
					}
				}
				
			}else {
				if(iliveRoom.getConvertTaskId()!=null&&iliveRoom.getConvertTaskId()>0) {

					try {
						String data1=HttpUtils.sendStr(converTaskLogo+"/api/task/"+iliveRoom.getConvertTaskId()+"/stop", "PUT", "");
						
			            	ConvertThread thread =new ConvertThread(iLiveRoomId, iLiveLiveRoomMng, iliveRoom.getConvertTaskId());
			            	thread.run();
				           
				           
					} catch (Exception e) {
						
						e.printStackTrace();
					} 
				}
				
			}
		}
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			
			//如果为预告状态，发送UMS消息
			if (event.getLiveStatus()==0&&statusMap.get(iliveRoom.getRoomId())==event.getLiveStatus()) {
				advancenotifyUms(iLiveRoomId);
				iliveRoom.setIsNowInsert(0);
				iLiveLiveRoomMng.update(iliveRoom);
				statusMap.remove(iliveRoom.getRoomId());
			}
			// 选择直播服务器组
			iLiveLiveRoomMng.update(event, iLiveRoomId, liveEventSwitch);
			
			
			workLogMng.save(new WorkLog(WorkLog.MDEOL_CREATEROOM, iLiveRoomId+"", net.sf.json.JSONObject.fromObject(iliveRoom).toString(), WorkLog.MODEL_CREATEROOM_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			return "redirect:list.do";
		} else {
			return null;
		}
	}
	/**
	 * 修改会议直播间
	 * 
	 * @param event
	 * @param iLiveRoomId
	 * @param liveEventSwitch
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "updateMeet.do")
	public String meetRoomUpdate(ILiveEvent event, Integer iLiveRoomId, Integer liveEventSwitch,
			HttpServletRequest request, ModelMap modelMap) {
		UserBean user = ILiveUtils.getUser(request);
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(iLiveRoomId);
		
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			
			//如果为预告状态，发送UMS消息
			if (event.getLiveStatus()==0&&statusMap.get(iliveRoom.getRoomId())==event.getLiveStatus()) {
				advancenotifyUms(iLiveRoomId);
				iliveRoom.setIsNowInsert(0);
				iLiveLiveRoomMng.update(iliveRoom);
				statusMap.remove(iliveRoom.getRoomId());
			}
			// 选择直播服务器组
			iLiveLiveRoomMng.update(event, iLiveRoomId, liveEventSwitch);
			ILiveMeetRequest host =meetRequestMng.getHostByMeetId(iLiveRoomId);
			ILiveMeetRequest student =meetRequestMng.getStudentByMeetId(iLiveRoomId);
			host.setRoomName(event.getLiveTitle());
			student.setRoomName(event.getLiveTitle());
			meetRequestMng.update(host);
			meetRequestMng.update(student);
			if(JedisUtils.exists("meetRoomInfo:"+iLiveRoomId)) {
				JedisUtils.delObject("meetRoomInfo:"+iLiveRoomId);
				JedisUtils.setObject("meetRoomInfo:"+iLiveRoomId, SerializeUtil.serialize(iLiveLiveRoomMng.getIliveRoom(iLiveRoomId)), 0);
			}else {
				 JedisUtils.setObject("meetRoomInfo:"+iLiveRoomId, SerializeUtil.serialize(iLiveLiveRoomMng.getIliveRoom(iLiveRoomId)), 0);
			}
			
			workLogMng.save(new WorkLog(WorkLog.MDEOL_CREATEROOM, iLiveRoomId+"", net.sf.json.JSONObject.fromObject(iliveRoom).toString(), WorkLog.MODEL_CREATEROOM_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			return "redirect:list.do";
		} else {
			return null;
		}
	}
public static Integer getTaskId(String xml) {
		
		try {
			//创建一个新的字符串
	        StringReader read = new StringReader(xml);
	        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	        InputSource source = new InputSource(read);
	        //创建一个新的SAXBuilder
	        SAXBuilder sb = new SAXBuilder();
	      //通过输入源构造一个Document
	        Document doc= sb.build(source);
			 //取的根元素
	        Element root = doc.getRootElement();
	        
	        return Integer.parseInt(root.getAttributeValue("id"));
		} catch (JDOMException | IOException e) {
			
			e.printStackTrace();
		}
		return null;
       
	}

	private Integer selectServerGroup() {
		return Integer.parseInt(ConfigUtils.get("defaultLiveServerGroupId"));
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
	public String createRoom(ILiveEvent iLiveEvent,Integer liveRoomId, Integer serverGroupId,
			HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			try {
				String serverGroupIdStr = ConfigUtils.get("defaultLiveServerGroupId");
				int liveGroupId = Integer.parseInt(serverGroupIdStr);
				iLiveLiveRoomMng.saveRoom(iLiveEvent, 0,liveRoomId, liveGroupId, user);
				ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(liveRoomId);
				workLogMng.save(new WorkLog(WorkLog.MDEOL_CREATEROOM, liveRoomId+"", iliveRoom.toString(), WorkLog.MODEL_CREATEROOM_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			} catch(Exception e) {
				e.printStackTrace();
			}
			return "redirect:list.do";
		} else {
			return null;
		}
	}
	
	/**
	 * 生成会议房间
	 * 
	 * @param iLiveEvent
	 * @param liveRoomId
	 * @param serverGroupId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "finish_createMeet.do")
	public String createMeetRoom(ILiveEvent iLiveEvent,Integer liveRoomId, Integer serverGroupId,
			HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			try {
				String serverGroupIdStr = ConfigUtils.get("defaultLiveServerGroupId");
				int liveGroupId = Integer.parseInt(serverGroupIdStr);
				iLiveLiveRoomMng.saveRoom(iLiveEvent, 1,liveRoomId, liveGroupId, user);
				ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(liveRoomId);
				
					
					ILiveMeetRequest host =new ILiveMeetRequest();
					ILiveMeetRequest student =new ILiveMeetRequest();
					host.setRoomId(liveRoomId);
					host.setPassword((int)((Math.random()*9+1)*100000)+"");
					host.setRoomName(iliveRoom.getLiveEvent().getLiveTitle());
					host.setType(1);
					student.setRoomId(liveRoomId);
					student.setPassword((int)((Math.random()*9+1)*100000)+"");
					student.setType(3);
					student.setRoomName(iliveRoom.getLiveEvent().getLiveTitle());
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
							QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/enterMeet/index.html?roomId="+liveRoomId+"&id="+hostId.substring(0, 6), realPath + "/" + tempFileName, logoPath);
						}else {
							QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/enterMeet/index.html?roomId="+liveRoomId+"&id="+studentId.substring(0, 6), realPath + "/" + tempFileName, logoPath);
						}
						
						String filePath = FileUtils.getTimeFilePath(tempFileName);
						
							boolean result = false;
							ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
							if (uploadServer != null) {
								FileInputStream in = new FileInputStream(tempFile);
								result = uploadServer.upload(filePath, in);
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
					
						
						
						host.setLoginUrl(ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+liveRoomId);
						
						
						student.setLoginUrl(ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+liveRoomId);
						
                        
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
				    param2.put("event_id", liveRoomId+"");
				    param2.put("service_type", "2");
				    param2.put("start_time", iliveRoom.getLiveEvent().getLiveStartTime().getTime()/1000);
				    param2.put("end_time", iliveRoom.getLiveEvent().getLiveEndTime().getTime()/1000);
				    param2.put("inadvance", -1);
				    param2.put("viewer", true);
				    param2.put("record", false);
				    param2.put("tenant_id", ConfigUtils.get("tenant_id"));
				    param2.put("room_type", "1");
				   
			        String message2 = JSONUtils.toJSONString(param2);
				    String data2=HttpRequest.sendPost(ConfigUtils.get("meet_url_create"), message2,meetToken);
				    net.sf.json.JSONObject json_result2 = net.sf.json.JSONObject.fromObject(data2);
				    net.sf.json.JSONObject json_result3 = net.sf.json.JSONObject.fromObject(json_result2.get("data").toString());
				    iliveRoom.setMeetId(json_result3.get("event_id").toString());
				    iLiveLiveRoomMng.update(iliveRoom);
				
				
				
				workLogMng.save(new WorkLog(WorkLog.MDEOL_CREATEROOM, liveRoomId+"", iliveRoom.toString(), WorkLog.MODEL_CREATEROOM_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			} catch(Exception e) {
				e.printStackTrace();
			}
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
//		Integer cer=user.getCertStatus();
//		Long userId=Long.parseLong(user.getUserId());
//		ILiveManager manager=iLiveManagerMng.selectILiveManagerById(userId);
//		Long creattime=manager.getCreateTime().getTime();
//		Long newtime=System.currentTimeMillis();
//		Long time=newtime-creattime;
//		if((time/(1000*60*60*24))>30&&cer!=4) {
//			return false;
//		}else {
		return true;
//		}
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
	private DocumentManagerMng documentManagerMng;
	
	@Autowired
	private DocumentPictureMng documentPictureMng;
	
   
	/**
	 * 进入直播间
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "selectRoom.do")
	public String selectILiveRoom(Integer htmlType,Integer roomId, ModelMap model, HttpServletRequest request) {
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		//判断是否是会议模式
		request.getSession().setAttribute("meeting_switch",iLiveLiveRoom != null && new Integer("1").equals(iLiveLiveRoom.getRoomType()) ? true : false);
		Integer enterpriseId = iLiveLiveRoom.getEnterpriseId();
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		Integer certStatus = iLiveEnterprise.getCertStatus();
		//查询是否开通云导播
		boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_CloudGuidedSeeding,certStatus);
		if(b){
			model.addAttribute("enterpriseType", 0);
			model.addAttribute("enterpriseContent", EnterpriseCache.ENTERPRISE_FUNCTION_CloudGuidedSeeding_Content);
		}else{
			model.addAttribute("enterpriseType", 1);
			model.addAttribute("enterpriseContent", "");
		}
//		//查询是否开通拉流直播
//		boolean b1 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_lahueLive,certStatus);
//		if(b1){
//			model.addAttribute("enterpriseType1", 0);
//			model.addAttribute("enterpriseContent1", EnterpriseCache.ENTERPRISE_FUNCTION_lahueLive_Content);
//		}else{
//			model.addAttribute("enterpriseType1", 1);
//			model.addAttribute("enterpriseContent1", "");
//		}
//		//查询是否开通垫片
//		boolean b2 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_Gasket,certStatus);
//		if(b2){
//			model.addAttribute("enterpriseType2", 0);
//			model.addAttribute("enterpriseContent2", EnterpriseCache.ENTERPRISE_FUNCTION_Gasket_Content);
//		}else{
//			model.addAttribute("enterpriseType2", 1);
//			model.addAttribute("enterpriseContent2", "");
//		}
//		//查询是否开通第三方推流
//		boolean b3 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_PushStream,certStatus);
//		if(b3){
//			model.addAttribute("enterpriseType3", 0);
//			model.addAttribute("enterpriseContent3", EnterpriseCache.ENTERPRISE_FUNCTION_PushStream_Content);
//		}else{
//			model.addAttribute("enterpriseType3", 1);
//			model.addAttribute("enterpriseContent3", "");
//		}
//		//查询是否开通桌面直播工具
//		boolean b4 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_DeskTop,certStatus);
//		if(b4){
//			model.addAttribute("enterpriseType4", 0);
//			model.addAttribute("enterpriseContent4", EnterpriseCache.ENTERPRISE_FUNCTION_DeskTop_Content);
//		}else{
//			model.addAttribute("enterpriseType4", 1);
//			model.addAttribute("enterpriseContent4", "");
//		}
//		//检查是否开通直播助手
//		boolean b5 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_LiveAssistant,certStatus);
//		if(b5){
//			model.addAttribute("LiveAssistant", 0);
//		}else{
//			model.addAttribute("LiveAssistant", 1);
//		}
//		//检查是否开通子账号管理
//		boolean b6 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_SubAccountManagement,certStatus);
//		if(b6){
//			model.addAttribute("SubAccountManagement", 0);
//		}else{
//			model.addAttribute("SubAccountManagement", 1);
//		}
//		//检查是否开通子账号管理
//		boolean b7 = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_LiveSelfServiceLogo,certStatus);
//		if(b7){
//			model.addAttribute("LiveSelfServiceLogo", 0);
//		}else{
//			model.addAttribute("LiveSelfServiceLogo", 1);
//		}
//		//检查是否开通收益账户
//		Integer getIfPassRevenue=EnterpriseUtil.getIfPassRevenue(enterpriseId, certStatus);
//		if(getIfPassRevenue!=null) {
//			model.addAttribute("getIfPassRevenue", getIfPassRevenue);
//		}
		ILiveEvent iLiveEvent = iLiveLiveRoom.getLiveEvent();
		Long documentId = iLiveEvent.getDocumentId();
		documentId= documentId==null?0:documentId;
		iLiveEvent.setDocumentId(documentId);
		if(documentId!=0){
			DocumentManager documentManager = documentManagerMng.getById(documentId);
			if (documentManager==null) {
				model.put("documentManager", new DocumentManager());
				model.put("documentPictures", new ArrayList<DocumentPicture>());
			}else {
				model.put("documentManager", documentManager);
				//图集
				List<DocumentPicture> documentPictures = documentPictureMng.getListByDocId(documentId);
				model.put("documentPictures", documentPictures);
			}
		}else{
			model.put("documentManager", new DocumentManager());
			model.put("documentPictures", new ArrayList<DocumentPicture>());
		}
		//文档
		List<DocumentManager> documentManagers = documentManagerMng.getListByEnterpriseId(enterpriseId);
		model.put("documentManagers", documentManagers);
		
		Integer documentDownload = iLiveEvent.getDocumentDownload();
		iLiveEvent.setDocumentDownload(documentDownload==null?0:documentDownload);
		Integer documentPageNO = iLiveEvent.getDocumentPageNO();
		iLiveEvent.setDocumentPageNO(documentPageNO==null?1:documentPageNO);
		Integer documentManual = iLiveEvent.getDocumentManual();
		iLiveEvent.setDocumentManual(documentManual==null?0:documentManual);
		// 直播场次获取
		model.addAttribute("ILiveEvent", iLiveEvent);
		// 登录用户
		UserBean userBean = ILiveUtils.getUser(request);
		model.addAttribute("userBean", userBean);
		//广告滚动
		ILiveRollingAdvertising iLiveRollingAdvertising = iLiveRollingAdvertisingMng.selectILiveRollingAdvertising(roomId);
		model.addAttribute("iLiveRollingAdvertising", iLiveRollingAdvertising);
		// 虚拟网友
		try {
			List<UserBean> outUserBeanXml = SystemXMLTomcatUrl.outUserBeanXml();
			model.addAttribute("outUserBeanXml", outUserBeanXml);
		} catch (Exception e) {
			model.addAttribute("outUserBeanXml", new ArrayList<UserBean>());
		}
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
		String pushStreamAddr="";
		String PushStreamAddr=RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		if(iLiveLiveRoom.getOpenLogoSwitch()!=null&&iLiveLiveRoom.getOpenLogoSwitch()==1) {
			 pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "o_tzwj_5000k";
		}else {
			 pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		}
		
		String hlsPlayAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
		String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
		String livePCAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/pc" + "/play.html?roomId=" + roomId;
		List<PageDecorate> pageDecorateList = pageDecorateMng.getList(iLiveEvent.getLiveEventId());
		List<PageDecorate> filterList = this.filterPageDecorate(pageDecorateList);
		model.addAttribute("pageDecorateList", filterList);
		Gson gson = new Gson();
		for (PageDecorate pageDecorate : filterList) {
			pageDecorate.setRichContent("");
		}
		ILiveRandomRecordTask taskByQuery = iLiveRandomRecordTaskMng.getTaskByQuery(
				iLiveLiveRoom.getLiveEvent().getLiveEventId(), Long.parseLong(userBean.getUserId()),
				ILivePlayStatusConstant.PLAY_ING);
		if (taskByQuery == null) {
			model.addAttribute("recordStatus", ILivePlayStatusConstant.UN_START);
		} else {
			model.addAttribute("recordStatus", ILivePlayStatusConstant.PLAY_ING);
			model.addAttribute("recordStartTime", (System.currentTimeMillis() - taskByQuery.getStartTime()) / 1000);
		}
		List<ILiveAPIGateWayRouter> routerList = iLiveAPIGateWayRouterMng.getRouterList();
		String apiRouterUrl = "";
		if (routerList != null && !routerList.isEmpty()) {
			ILiveAPIGateWayRouter iLiveAPIGateWayRouter = routerList.get(0);
			apiRouterUrl = iLiveAPIGateWayRouter.getRouterUrl();
		}
		Pagination pagination = iLiveRoomThirdMng.selectILiveRoomThirdPage( 1,5,roomId);
		List<IliveRoomThird> list=iLiveRoomThirdMng.selectILiveRoomThirdById(roomId);
		model.addAttribute("iLiveEvent", iLiveEvent);
		model.addAttribute("apiRouterUrl", apiRouterUrl);
		model.addAttribute("decorateJSON", gson.toJson(filterList));
		model.addAttribute("liveAddr", liveAddr);
		model.addAttribute("livePCAddr", livePCAddr);
		model.addAttribute("hlsPlayAddr", hlsPlayAddr);
		model.addAttribute("pushStreamAddr", pushStreamAddr);
		model.addAttribute("PushStreamAddr", PushStreamAddr);
		model.addAttribute("pager", pagination ==null?"":pagination);
		model.addAttribute("thirdStatus", list.size()>0?list.get(0).getTstatus():1);
		model.addAttribute("leftActive", "1_1");
		model.addAttribute("topActive", "1");
		CopyOnWriteArrayList<ConcurrentHashMap<String, String>> faceLists = ExpressionUtil.getAll();
		model.addAttribute("faceLists", faceLists);
		if(htmlType!=null){
			return "liveroom/assistant/room_Assistant";
		}else{
			return "liveroom/room";
		}
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
	public String liveBan(String params,Integer roomId, Integer pageNo, ModelMap modelMap) {
		if(!StringUtils.isBlank(params)){
			try {
				params=new String(params.trim().getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		// 获取禁言列表
		Pagination pagination = iLiveEstoppelMng.queryPager(params,iLiveLiveRoom.getRoomId(), cpn(pageNo), 20);
		modelMap.addAttribute("pagination", pagination);
		modelMap.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		modelMap.addAttribute("leftActive", "7_1");
		modelMap.addAttribute("topActive", "1");
		String apiRouterUrl = "";
		List<ILiveAPIGateWayRouter> routerList = iLiveAPIGateWayRouterMng.getRouterList();
		if (routerList != null && !routerList.isEmpty()) {
			ILiveAPIGateWayRouter iLiveAPIGateWayRouter = routerList.get(0);
			apiRouterUrl = iLiveAPIGateWayRouter.getRouterUrl();
		}
		modelMap.addAttribute("apiRouterUrl", apiRouterUrl);
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
	 * 导播工具下载
	 */
	@RequestMapping(value = "download/broadcaster.do")
	public void downloadBroadCasterUtil(HttpServletRequest request, HttpServletResponse response) {
		try {
			DownloadUtil.downloadExe("bvlivecastersetup.exe", "BVLiveCaster", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	@RequestMapping(value="sentitive.do",method=RequestMethod.POST)
	public void sentitive(HttpServletResponse response,String title,String desc) {
		JSONObject result = new JSONObject();
		try {
			if (title!=null&&!title.equals("")) {
				Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(title);
				Set<String> sensitiveWord2 = new HashSet<>();
				if (desc!=null&&!desc.equals("")) {
					sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(desc);
				}
				if (sensitiveWord.size()!=0||sensitiveWord2.size()!=0) {
					result.put("code", "0");
					if (sensitiveWord.size()!=0) {
						result.put("data", JSONArray.fromObject(sensitiveWord));
					}
					if (sensitiveWord2.size()!=0) {
						result.put("data", JSONArray.fromObject(sensitiveWord2));
					}
				}else {
					result.put("code", "1");
				}
			}else {
				result.put("code", "1");
				result.put("data", "标题不能为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping(value="updateslim.do")
	public void updateslim(Integer roomId,String url,String localurl,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setRelateSlimFileUrl(url);
			iliveRoom.setRelateSlimFileLocalUrl(localurl);
			iLiveLiveRoomMng.update(iliveRoom);
			try {
				Integer openSlimModel = iliveRoom.getOpenSlimModel();
				if (openSlimModel!=null && openSlimModel==1) {
					notifyUms(roomId,false,0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put("code", "0");
			result.put("msg", "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
			result.put("msg", "更新失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping(value="updateSlimModel")
	public void updateSlimModel(HttpServletResponse response,Integer roomId,Integer isOpen) {
		JSONObject result = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setOpenSlimModel(isOpen);
			iLiveLiveRoomMng.update(iliveRoom);
			if (isOpen==1) {
				notifyUms(roomId,false,0);
			}else {
				closenotifyUms(roomId);
			}
			result.put("code", "1");
			result.put("msg", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("msg", "修改失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	@RequestMapping(value="updateBackupStream")
	public void updateBackupStream(HttpServletResponse response,Integer roomId,Integer isOpen) {
		JSONObject result = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setOpenBackupStream(isOpen);
			iLiveLiveRoomMng.update(iliveRoom);
			if (isOpen==1) {
				
			}else {
				
			}
			result.put("code", "1");
			result.put("msg", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("msg", "修改失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	/**
	 * 更新拉流地址
	 * @return
	 */
	@RequestMapping(value="updatePullAddr")
	public void updatePullAddr(HttpServletResponse response,Integer roomId,String addr) {
		JSONObject result = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setPullAddr(addr);
			iLiveLiveRoomMng.update(iliveRoom);
			result.put("msg", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "保存失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 设置延时
	 * @param response
	 * @param roomId
	 * @param delay
	 */
	@RequestMapping(value="setDelay")
	public void setDelay(HttpServletResponse response,Integer roomId,Integer delay) {
		JSONObject result = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setLiveDelay(delay);
			iLiveLiveRoomMng.update(iliveRoom);
			notifyUms(roomId,false,delay);
			result.put("msg", "操作成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 删除垫片
	 * @param response
	 * @param roomId
	 * @param delay
	 */
	@RequestMapping(value="delDianpian")
	public void delDianpian(HttpServletResponse response,Integer roomId) {
		JSONObject result = new JSONObject();
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			iliveRoom.setRelateSlimFileUrl("");
			iLiveLiveRoomMng.update(iliveRoom);
			result.put("msg", "删除成功");
			result.put("code", "0");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "操作失败,出现意外情况");
			result.put("code", "1");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	private void notifyUms(Integer roomId,boolean isNow,Integer second) {
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			UmsApi umsApi = getUmsAPI(roomId);
			String backup_video_url = ConfigUtils.get("backup_video");
			String mountName = "live" + roomId + "_tzwj_5000k";
			advancenotifyUms(roomId);
			umsApi.closeMount("live", mountName);
			//直播间设置延时
			if (iliveRoom.getOpenSlimModel()!=null&&iliveRoom.getOpenSlimModel()==1 && iliveRoom.getRelateSlimFileLocalUrl()!=null) {
				umsApi.setPublishAutoBackup("live", mountName, 
						backup_video_url+iliveRoom.getRelateSlimFileLocalUrl(),
						second==null?"0":String.valueOf(second),false);
			}else {
				umsApi.setPublishAutoBackup("live", mountName, 
						"",second==null?"0":String.valueOf(second),false);
			}
			umsApi.openMount("live", mountName);
		} catch (Exception e) {
			logger.error("ums设置延时失败");
		}
	}
	
	/**
	 * 即刻插播垫片/取消即刻插播垫片
	 * @param roomId
	 * @param isNow
	 */
	@RequestMapping(value="nowinsert")
	public void nowinsert(Integer roomId,Integer isNow,HttpServletResponse response) {
		try {
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			if (iliveRoom.getOpenSlimModel()!=null&&iliveRoom.getOpenSlimModel()==1 && iliveRoom.getRelateSlimFileLocalUrl()!=null) {
				iliveRoom.setIsNowInsert(isNow);
				UmsApi umsApi = getUmsAPI(roomId);
				String mountName = "live" + roomId + "_tzwj_5000k";
				String backup_video_url = ConfigUtils.get("backup_video");
				String second = iliveRoom.getLiveDelay()==null?"0":iliveRoom.getLiveDelay().intValue()+"";
				advancenotifyUms(roomId);
				if (isNow==1) {
					umsApi.setPublishAutoBackup("live", mountName, 
							backup_video_url+iliveRoom.getRelateSlimFileLocalUrl(),
							"0",true);
				}else {
					umsApi.setPublishAutoBackup("live", mountName, 
							backup_video_url+iliveRoom.getRelateSlimFileLocalUrl(),
							"0",false);
				}
				iLiveLiveRoomMng.update(iliveRoom);
				ResponseUtils.renderJson(response, ResponseJson.defaultOK());
			}else {
				ResponseUtils.renderJson(response, ResponseJson.fail("垫片未打开或没有垫片文件"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtils.renderJson(response, ResponseJson.defaultFail());
		}
	}
	
	private UmsApi getUmsAPI(Integer roomId) {
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
				.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
		accessMethod.setFtp_address(ConfigUtils.get("backupvideo_ftp_address"));
		accessMethod.setFtp_user(ConfigUtils.get("backupvideo_ftp_username"));
		accessMethod.setFtp_pwd(ConfigUtils.get("backupvideo_ftp_password"));
		accessMethod.setFtpPort(Integer.valueOf(ConfigUtils.get("backupvideo_ftp_port")));
		accessMethod.getMountInfo().setFtp_path(ConfigUtils.get("backupvideo_ftppath"));
		
		UmsApi umsApi = new UmsApi(accessMethod.getOrgLiveHttpUrl()
				, String.valueOf(accessMethod.getUmsport()));
		return umsApi;
		//return new UmsApi("live01.zbt.tv189.net", "1935");
	}
	
	private void closenotifyUms(Integer roomId) {
		try {
			UmsApi umsApi = getUmsAPI(roomId);
			
			String mountName = "live" + roomId + "_tzwj_5000k";
			advancenotifyUms(roomId);
			umsApi.closeMount("live", mountName);
			//直播间设置延时
			umsApi.delPublishAutoBackup("live", mountName);
			boolean openMount = umsApi.openMount("live", mountName);
			System.out.println(openMount);
		} catch (Exception e) {
			logger.error("ums设置延时失败");
		}
	}
	
	private void advancenotifyUms(Integer roomId) {
		try {
			UmsApi umsApi = getUmsAPI(roomId);
			
			String mountName = "live" + roomId + "_tzwj_5000k";
			umsApi.closeMount("live", mountName);
			//直播间恢复预告重置
			umsApi.delPublishAutoBackup("live", mountName);
			umsApi.openMount("live", mountName);
		} catch (Exception e) {
			logger.error("ums设置失败");
		}
	}
	
	@RequestMapping(value="checkent")
	public void checkent(HttpServletRequest request,HttpServletResponse response) {
		UserBean user = ILiveUtils.getUser(request);
		JSONObject result = new JSONObject();
		if (user!=null) {
			Integer enterpriseId = user.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			boolean check = EnterpriseUtil.selectIfEn(enterpriseId,
					EnterpriseCache.ENTERPRISE_FUNCTION_LiveMatrixMonitoring,
					iLiveEnterPrise.getCertStatus());
			if (check) {
				result.put("code", "0");
				result.put("msg", "审核成功");
			}else {
				result.put("code", "2");
				result.put("msg", EnterpriseCache.ENTERPRISE_FUNCTION_LiveMatrixMonitoring_Content);
			}
		}else {
			result.put("code", "1");
			result.put("msg", "用户未登录");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 直播矩阵监控
	 * @return
	 */
	@RequestMapping(value="monitor")
	public String monitor(Model model,Integer formroomtype,
			Integer formexamine,Integer formlivestate,
			Integer formroomstate,String formqueryName,HttpServletRequest request) {
		
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		//获取直播间数量
		String pushStreamAddr = null;
		Pagination page = iLiveLiveRoomMng.getPager(enterpriseId,1,1, 9);
		@SuppressWarnings("unchecked")
		List<ILiveLiveRoom> iliveroom = (List<ILiveLiveRoom>) page.getList();
		System.out.println(page.getTotalCount());
		model.addAttribute("pageNo", 1);
		model.addAttribute("pushStreamAddr", pushStreamAddr);
		model.addAttribute("maxPage", page.getTotalPage());
		model.addAttribute("list", iliveroom);
		model.addAttribute("topActive", "1");
		return "liveroom/monitor";
	}
	
	@RequestMapping(value="/getPage.do")
	public void mString(Model model,Integer pageNo,HttpServletResponse response,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		Pagination page = iLiveLiveRoomMng.getPager(enterpriseId,1,pageNo, 9);
		@SuppressWarnings("unchecked")
		List<ILiveLiveRoom> iliveroom = (List<ILiveLiveRoom>) page.getList();
		Iterator<ILiveLiveRoom> iterator = iliveroom.iterator();
		net.sf.json.JSONArray array = new JSONArray();
		//for(ILiveLiveRoom room:iliveroom){
		while (iterator.hasNext()) {
			ILiveLiveRoom room = iterator.next();
			ILiveEnterprise iLiveEnterPrise = null;
			if(room.getEnterpriseId()==null) {
				iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(10);
			}else {
				iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
			}
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = accessMethodMng
						.getAccessMethodBySeverGroupId(room.getServerGroupId());
				String hlsPlayAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
						+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + room.getRoomId() + "/5000k/tzwj_video.m3u8";
				room.getLiveEvent().setViewAddr(hlsPlayAddr);
				System.out.println("播放地址： "+hlsPlayAddr);
				net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
				jsonObject.put("roomId", room.getRoomId());
				jsonObject.put("roomName", room.getLiveEvent().getLiveTitle());
				String enterpriseName ;
				if(iLiveEnterPrise==null||iLiveEnterPrise.getEnterpriseName()==null) {
					enterpriseName="";
				}else {
					enterpriseName = iLiveEnterPrise.getEnterpriseName();
				}
				jsonObject.put("enterpriseName", enterpriseName);
				jsonObject.put("url", hlsPlayAddr);
				array.add(jsonObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ResponseUtils.renderJson(response, array.toString());
	}
	
	@RequestMapping(value="getpattern")
	public String pattern(Integer roomId,Model model) {
		ILiveLiveRoom room = iLiveLiveRoomMng.getIliveRoom(roomId);
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
		room.setEnterpriseName(iLiveEnterPrise.getEnterpriseName());
		ILiveServerAccessMethod accessMethodBySeverGroupId = null;
		String pushStreamAddr = null;
		try {
			accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(room.getServerGroupId());
			String pushStreamAddr1 = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + room.getRoomId() + "_tzwj_5000k";
			pushStreamAddr = pushStreamAddr1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("room", room);
		model.addAttribute("pushStreamAddr", pushStreamAddr);
		return "liveroom/patternVideo";
	}
	@RequestMapping(value="meetInviteDiy.do")
	public String meetInviteDiy(Integer roomId,Model model) {
		model.addAttribute("roomId", roomId);
		model.addAttribute("leftActive", "8_3");
		return "liveroom/meetInviteDiy";
	}
	/**
	 * 新创建直播接口
	 * 
	 * @param event
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "createNewRoom.do")
	public void createNewRoom(ILiveEvent event,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			// 选择直播服务器组
			Integer serverGroupId =null;
			if(ConfigUtils.get("server_access_method").equals("1")) {
				WeightRandom  weightRandom =new WeightRandom();
				serverGroupId = Integer.parseInt(weightRandom.getRandomResoult());
			}else {
				serverGroupId = this.selectServerGroup();
			}
			
			long roomId = filedIdMng.getNextId("ilive_live_room", "room_id", 1);
			ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(serverGroupId);
			resultJson.put("roomId", roomId);
			resultJson.put("serverGroupId", serverGroupId);
			String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
			resultJson.put("liveAddr", liveAddr);
			String playAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/pc" + "/pc.html?roomId=" + roomId;
			resultJson.put("playAddr", playAddr);
			ResponseUtils.renderJson(response, resultJson.toString());
		} else {
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}
	/**
	 * 新创建会议接口
	 * 
	 * @param event
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "createNewMeet.do")
	public void createNewMeet(ILiveEvent event,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			// 选择直播服务器组
			try {
				Integer serverGroupId =null;
				if(ConfigUtils.get("server_access_method").equals("1")) {
					WeightRandom  weightRandom =new WeightRandom();
					serverGroupId = Integer.parseInt(weightRandom.getRandomResoult());
				}else {
					serverGroupId = this.selectServerGroup();
				}
				long roomId = filedIdMng.getNextId("ilive_live_room", "room_id", 1);
				ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
						.getAccessMethodBySeverGroupId(serverGroupId);
				resultJson.put("roomId", roomId);
				resultJson.put("serverGroupId", serverGroupId);
				String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
				resultJson.put("liveAddr", liveAddr);
				String playAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/pc" + "/pc.html?roomId=" + roomId;
				resultJson.put("playAddr", playAddr);
				resultJson.put("code", 1);
				resultJson.put("msg", "成功");
				ResponseUtils.renderJson(response, resultJson.toString());
			} catch (Exception e) {
				resultJson.put("code", 0);
				resultJson.put("msg", "失败");
				ResponseUtils.renderJson(response, resultJson.toString());
			}
			
		} else {
			resultJson.put("code", 0);
			resultJson.put("msg", "无权限");
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}
	/**
	 * 生成直播间新接口
	 * 
	 * @param iLiveEvent
	 * @param liveRoomId
	 * @param serverGroupId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "new_finish_create.do")
	public void newcreateRoom(ILiveEvent iLiveEvent,Integer liveRoomId, Integer serverGroupId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			try {
				if(ConfigUtils.get("server_access_method").equals("1")) {
					WeightRandom  weightRandom =new WeightRandom();
					serverGroupId = Integer.parseInt(weightRandom.getRandomResoult());
				}else {
					serverGroupId = this.selectServerGroup();
				}
				iLiveLiveRoomMng.saveRoom(iLiveEvent, 0,liveRoomId, serverGroupId, user);
				ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(liveRoomId);
				workLogMng.save(new WorkLog(WorkLog.MDEOL_CREATEROOM, liveRoomId+"", iliveRoom.toString(), WorkLog.MODEL_CREATEROOM_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
				resultJson.put("code", 1);
				resultJson.put("msg", "成功");
				ResponseUtils.renderJson(response, resultJson.toString());
			} catch(Exception e) {
				e.printStackTrace();
				resultJson.put("code", 0);
				resultJson.put("msg", "失败");
				ResponseUtils.renderJson(response, resultJson.toString());
			}
			
		} else {
			resultJson.put("code", 0);
			resultJson.put("msg", "无权限");
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}
	/**
	 * 生成会议房间新接口
	 * 
	 * @param iLiveEvent
	 * @param liveRoomId
	 * @param serverGroupId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "new_finish_createMeet.do")
	public void newcreateMeetRoom(ILiveEvent iLiveEvent,Integer liveRoomId, Integer serverGroupId,
			HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		boolean perm = this.validatorUserPerm(user);
		if (perm) {
			try {
				
				if(ConfigUtils.get("server_access_method").equals("1")) {
					WeightRandom  weightRandom =new WeightRandom();
					serverGroupId = Integer.parseInt(weightRandom.getRandomResoult());
				}else {
					serverGroupId = this.selectServerGroup();
				}
				iLiveLiveRoomMng.saveRoom(iLiveEvent, 1,liveRoomId, serverGroupId, user);
				ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(liveRoomId);
				
					
					ILiveMeetRequest host =new ILiveMeetRequest();
					ILiveMeetRequest student =new ILiveMeetRequest();
					host.setRoomId(liveRoomId);
					host.setPassword((int)((Math.random()*9+1)*100000)+"");
					host.setRoomName(iliveRoom.getLiveEvent().getLiveTitle());
					host.setType(1);
					student.setRoomId(liveRoomId);
					student.setPassword((int)((Math.random()*9+1)*100000)+"");
					student.setType(3);
					student.setRoomName(iliveRoom.getLiveEvent().getLiveTitle());
					
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
							QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/enterMeet/index.html?roomId="+liveRoomId+"&id="+hostId.substring(0, 6), realPath + "/" + tempFileName, logoPath);
						}else {
							QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/enterMeet/index.html?roomId="+liveRoomId+"&id="+studentId.substring(0, 6), realPath + "/" + tempFileName, logoPath);
						}
						
						String filePath = FileUtils.getTimeFilePath(tempFileName);
						
							boolean result = false;
							ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
							if (uploadServer != null) {
								FileInputStream in = new FileInputStream(tempFile);
								result = uploadServer.upload(filePath, in);
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
					
						
						
						host.setLoginUrl(ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+liveRoomId);
						
						
						student.setLoginUrl(ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+liveRoomId);
						
                        
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
				    param2.put("event_id", liveRoomId+"");
				    param2.put("service_type", "2");
				    param2.put("start_time", iliveRoom.getLiveEvent().getLiveStartTime().getTime()/1000);
				    param2.put("end_time", iliveRoom.getLiveEvent().getLiveEndTime().getTime()/1000);
				    param2.put("inadvance", -1);
				    param2.put("viewer", true);
				    param2.put("record", false);
				    param2.put("tenant_id", ConfigUtils.get("tenant_id"));
				    param2.put("room_type", "1");
				   
			        String message2 = JSONUtils.toJSONString(param2);
				    String data2=HttpRequest.sendPost(ConfigUtils.get("meet_url_create"), message2,meetToken);
				    net.sf.json.JSONObject json_result2 = net.sf.json.JSONObject.fromObject(data2);
				    net.sf.json.JSONObject json_result3 = net.sf.json.JSONObject.fromObject(json_result2.get("data").toString());
				    iliveRoom.setMeetId(json_result3.get("event_id").toString());
				    iLiveLiveRoomMng.update(iliveRoom);
				    workLogMng.save(new WorkLog(WorkLog.MDEOL_CREATEROOM, liveRoomId+"", iliveRoom.toString(), WorkLog.MODEL_CREATEROOM_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
				    resultJson.put("code", 1);
					resultJson.put("msg", "成功");
					ResponseUtils.renderJson(response, resultJson.toString());
			} catch(Exception e) {
				e.printStackTrace();
				resultJson.put("code", 0);
				resultJson.put("msg", "失败");
				ResponseUtils.renderJson(response, resultJson.toString());
			}
		} else {
			resultJson.put("code", 0);
			resultJson.put("msg", "无权限");
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}
}
