package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.AssistentLogin;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.entity.ILiveAPIGateWayRouter;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;
import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.AssistentLoginMng;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveAPIGateWayRouterMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveRandomRecordTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveRollingAdvertisingMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.realm.UsernamePasswordLoginTypeToken;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.gson.Gson;
/**
 * 管理助手
 * @author YanXL
 *
 */
@RequestMapping(value="assistant")
public class ILiveRoomApiController {
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	@Autowired
	private ILiveRollingAdvertisingMng iLiveRollingAdvertisingMng;
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	@Autowired
	private ILiveRandomRecordTaskMng iLiveRandomRecordTaskMng;
	@Autowired
	private ILivePageDecorateMng pageDecorateMng;
	@Autowired
	private ILiveAPIGateWayRouterMng iLiveAPIGateWayRouterMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private DocumentManagerMng documentManagerMng;
	@Autowired
	private DocumentPictureMng documentPictureMng;
	@Autowired
	private AssistentLoginMng assistentLoginMng;
	private final static String RTMP_PROTOACAL = "rtmp://";

	private static final String HTTP_PROTOCAL = "http://";
	Logger logger = LoggerFactory.getLogger(ILiveRoomApiController.class);
	/**
	 * 助理登录页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public String loginHtml(ModelMap model,Integer roomId,HttpServletRequest request,HttpServletResponse response){
		if(roomId==null){
			return "redirect:/admin/login.do";
		}else{
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			model.put("liveTitle", iLiveLiveRoom.getLiveEvent().getLiveTitle());
			model.put("playBgAddr", iLiveLiveRoom.getLiveEvent().getPlayBgAddr());
		}
		return "assistant/assistant_login";
	}
	/**
	 * 助理退出
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/logout.do")
	public String logoutHtml(ModelMap model,Integer roomId){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/admin/assistant/login.do?roomId="+roomId;
	}
	/**
	 * 登录验证
	 * @return
	 */
	@RequestMapping(value="/loginPassword.do")
	public void loginAssistant(String invitationCode,String username,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			if (username == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "登录名称不能为空");
				resultJson.put("data", new JSONObject());
			} else if (invitationCode == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "邀请码不能为空");
				resultJson.put("data", new JSONObject());
			}
			ILiveManager iLiveManager = iLiveManagerMng.getILiveMangerByMobile(invitationCode);
			if (iLiveManager == null) {
				// 开启注册登录
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "登录失败,请确认账户或密码是否正确");
				resultJson.put("data", new JSONObject());
			}else{
				iLiveManager.setNailName(username);
				iLiveManagerMng.updateLiveManager(iLiveManager);
				Subject subject = SecurityUtils.getSubject();
				UsernamePasswordLoginTypeToken utt = new UsernamePasswordLoginTypeToken(invitationCode, "123456", false, null,"2", null, null);
				try {
					// 执行认证操作.
					subject.login(utt);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "登录成功");
					Integer roomId = iLiveManager.getRoomId()==null?0:iLiveManager.getRoomId();
					resultJson.put("roomId", roomId);
					try {
						//助手登录记录保存
						AssistentLogin assistentLogin = new AssistentLogin();
						assistentLogin.setUserName(username);
						assistentLogin.setRoomId(roomId);
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						assistentLogin.setLoginTime(Timestamp.valueOf(format.format(new Date())));
						assistentLogin.setIp(getIpAddress(request));
						assistentLogin.setInvitationCode(invitationCode);
						assistentLoginMng.addAssistentLogin(assistentLogin);
					} catch (Exception e) {
						System.out.println(e);
					}
				} catch (UnknownAccountException e) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户不存在");
				} catch (IncorrectCredentialsException e) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "用户名/密码错误");
				} catch (ExcessiveAttemptsException e) {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "登录失败多次，账户锁定10分钟");
				} catch (AuthenticationException e) {
					e.printStackTrace();
					// 其他错误，比如锁定，如果想单独处理请单独catch处理
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "登录失败");
				}
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "登录失败,请确认账户或密码是否正确");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
		}			
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 进入直播间
	 * 
	 * @param roomId
	 *            直播间ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "selectRoom.do",method=RequestMethod.GET)
	public String selectILiveRoom(ModelMap model, HttpServletRequest request,HttpServletResponse response) {
		// 登录用户
		UserBean userBean = ILiveUtils.getUser(request);
		List<ILiveAPIGateWayRouter> routerList = iLiveAPIGateWayRouterMng.getRouterList();
		if(userBean==null){
			return "redirect:/admin/assistant/login.do";
		}
		Integer roomId = userBean.getRoomId();
		if(roomId==null){
			return "redirect:/admin/assistant/login.do";
		}
		// 直播间获取
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		ILiveEvent iLiveEvent = iLiveLiveRoom.getLiveEvent();
		Long documentId = iLiveEvent.getDocumentId();
		documentId= documentId==null?0:documentId;
		iLiveEvent.setDocumentId(documentId);
		if(documentId!=0){
			DocumentManager documentManager = documentManagerMng.getById(documentId);
			model.put("documentManager", documentManager);
			//图集
			List<DocumentPicture> documentPictures = documentPictureMng.getListByDocId(documentId);
			model.put("documentPictures", documentPictures);
		}else{
			model.put("documentManager", new DocumentManager());
			model.put("documentPictures", new ArrayList<DocumentPicture>());
		}
		//文档
		List<DocumentManager> documentManagers = documentManagerMng.getListByEnterpriseId(iLiveLiveRoom.getEnterpriseId());
		model.put("documentManagers", documentManagers);
		
		Integer documentDownload = iLiveEvent.getDocumentDownload();
		iLiveEvent.setDocumentDownload(documentDownload==null?0:documentDownload);
		Integer documentPageNO = iLiveEvent.getDocumentPageNO();
		iLiveEvent.setDocumentPageNO(documentPageNO==null?1:documentPageNO);
		Integer documentManual = iLiveEvent.getDocumentManual();
		iLiveEvent.setDocumentManual(documentManual==null?0:documentManual);
		// 直播场次获取
		model.addAttribute("ILiveEvent", iLiveEvent);
		model.addAttribute("userBean", userBean);
		//广告滚动
		ILiveRollingAdvertising iLiveRollingAdvertising = iLiveRollingAdvertisingMng.selectILiveRollingAdvertising(roomId);
		model.addAttribute("iLiveRollingAdvertising", iLiveRollingAdvertising);
		/**
		 * 获取直播间推流地址
		 */
		ILiveServerAccessMethod accessMethodBySeverGroupId = null;
		try {
			accessMethodBySeverGroupId = accessMethodMng.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		String hlsPlayAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
		String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
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
		String apiRouterUrl = "";
		if (routerList != null && !routerList.isEmpty()) {
			ILiveAPIGateWayRouter iLiveAPIGateWayRouter = routerList.get(0);
			apiRouterUrl = iLiveAPIGateWayRouter.getRouterUrl();
		}
		model.addAttribute("apiRouterUrl", apiRouterUrl);
		model.addAttribute("decorateJSON", gson.toJson(filterList));
		model.addAttribute("liveAddr", liveAddr);
		model.addAttribute("hlsPlayAddr", hlsPlayAddr);
		model.addAttribute("pushStreamAddr", pushStreamAddr);
		CopyOnWriteArrayList<ConcurrentHashMap<String, String>> faceLists = ExpressionUtil.getAll();
		model.addAttribute("faceLists", faceLists);
		return "assistant/room_Assistant";
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
	public String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    return ip; 
	  }
}
