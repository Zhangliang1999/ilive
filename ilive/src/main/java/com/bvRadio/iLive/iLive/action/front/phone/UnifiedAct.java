package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UN_LOGIN;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.admin.ILiveRoomController;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.action.front.vo.AppSubBean;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.front.vo.IliveAppRetInfo;
import com.bvRadio.iLive.iLive.action.front.vo.RoomCreateVo;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.AutoLogin;
import com.bvRadio.iLive.iLive.entity.ILiveConvertTask;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.ILiveConvertTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubRoomMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.util.ConvertThread;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.IPUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.jwzt.ums.api.UmsApi;

import net.sf.json.JSONArray;


@SuppressWarnings("unused")
@RequestMapping(value="Unified")
@Controller
public class UnifiedAct {
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Autowired
	private WorkLogMng workLogMng;
	@Autowired
	private ILiveSubRoomMng iLiveSubRoomMng;
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	@Autowired
    private ILiveConvertTaskMng iLiveConvertTaskMng;
	private Logger logger = LoggerFactory.getLogger(UnifiedAct.class);
	
	
	
	/**
	 * 构建登录token
	 * 
	 * @return
	 */
	private String buildLoginToken() {
		UUID uuid = UUID.randomUUID();
		String token = Md5Util.encode(uuid.toString() + System.currentTimeMillis());
		return token;
	}
	
	private static final String REGEX_MOBILE = "(134[0-8]\\d{7})" +
            "|(" +
                "((13([0-3]|[5-9]))" +
                    "|149" +
                    "|15([0-3]|[5-9])" +
                    "|166" +
                    "|17(3|[5-8])" +
                    "|18[0-9]" +
                    "|19[8-9]" +
                ")" +
            "\\d{8}" +
            ")";
	private static boolean isMobile(String tel) {
		return Pattern.matches(REGEX_MOBILE, tel);
	}
	
	
	/**
	 * 统一校检方法
	 */
	public boolean ifAccess(String appId,String time,String token){
		ILiveEnterprise manage= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
		String md5Hex = DigestUtils.md5Hex(time+"&"+manage.getSecret()+"&"+"TV189");
		boolean ret;
		if(md5Hex.equals(token)){
			ret=true;
		}else{
		ret=false;
		}
		return ret;
	}
	/**
	 * 往session添加一个记录
	 */
	public void addSession(Long userId,HttpServletRequest request,HttpServletResponse response){
		ILiveManager manager=iLiveManagerMng.selectILiveManagerById(userId);
		String loginToken=this.buildLoginToken();
		UserBean userBean = new UserBean();
		userBean.setUserId(String.valueOf(userId));
		userBean.setLoginToken(loginToken);
		userBean.setNickname(manager.getNailName());
		userBean.setUserThumbImg(manager.getUserImg());
		userBean.setUsername(manager.getMobile());
		userBean.setCertStatus(manager.getCertStatus());
		userBean.setLevel(manager.getLevel()==null?0:manager.getLevel());
		logger.info("设置appUser");
		ILiveUtils.setAppUser(request, userBean);
		ILiveUtils.setUser(request, userBean);
	}
	public boolean checkValidIPAddress(String ipAddr,String appId) {
		//根据appId获取对应的ip地址
		ILiveEnterprise manage= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
		System.out.println("system_appip:"+manage.getIPAddress()+"*********userIp:"+ipAddr+"******************");
		if (!com.jwzt.comm.StringUtils.isEmpty(manage.getIPAddress())) {
			String ip=manage.getIPAddress();
			String[] split = ip.split(",");
			for (String ip1 : split) {
				if (ip1.equals(ipAddr)) {
					return true;
				}
			}
			
			return false;
		}
		return false;
	}
	/**
	 * 代理接口
	 */
	@RequestMapping(value="control.jspx")
	public void control(String method,String liveTitle,String startTime,String endTime,String liveStartTime,String viewAuthorized,String appId,String time,String token,HttpServletRequest request,
			HttpServletResponse response,Integer pageNo,Integer pageSize,String terminalType,Integer roomId,Integer LiveSwitch,ModelMap modelMap,String subaccountId,Long userId, Long fileId){
		JSONObject result = new JSONObject();
		ILiveEnterprise manage= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
		if(manage==null){
			result.put("code", AutoLogin.CODE_ERROR);
			result.put("data", new JSONObject());
			result.put("msg", "无此appId！");
			ResponseUtils.renderJson(request, response, result.toString());
			return;
		}
		//后期会加入ip校检
		String realIpAddr = IPUtils.getRealIpAddr(request);
		if (!checkValidIPAddress(realIpAddr,appId)) {
			result.put("code", ERROR_STATUS);
			result.put("message", "该IP没有权限访问");
			result.put("data", new JSONArray());
			ResponseUtils.renderJson(response, result.toString());
			return;
		}
		if(method.isEmpty()||method==null){
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "method为空");
		}else{
			//创建直播间
			String creatRoom="creatRoom";
			//直播间列表
			String RoomList="RoomList";
			//直播间修改
			String roomEdit="roomEdit";
			//绑定直播间主播
			String bindRoom="bindRoom";
			//查询直播间和账户绑定状态
			String getBindList="getBindList";
			//查询账户ID
			String getSubMsg="getSubMsg";
			//获取直播间状态
			String getRoomMsg="getRoomMsg";
			//查询房间访问记录
			String getRoomRecode="getRoomRecode";
			//查询点播文件列表
			String getMediaList="getMediaList";
			//删除点播文件
			String destroyVod="destroyVod";
			if(creatRoom.equals(method)){
				this.creatRoom(liveTitle, liveStartTime, viewAuthorized, appId, time, token, request, response);
			}else if(RoomList.equals(method)){
				this.RoomList(appId, time, token, pageNo==null?1:pageNo, pageSize==null?20:pageSize, terminalType, request, response);
			}else if(roomEdit.equals(method)){
				this.roomEdit(roomId, liveTitle, LiveSwitch, liveStartTime, viewAuthorized, appId, time, token, request, response);
			}else if(bindRoom.equals(method)){
				this.bindRoom(roomId, subaccountId, appId, time, token, request, response);
			}else if(getBindList.equals(method)){
				this.getBindList(appId, time, token, request, response);
			}else if(getSubMsg.equals(method)){
				this.getSubMsg(appId, time, token, request, response);
			}else if(getRoomMsg.equals(method)){
				this.getRoomMsg(roomId, appId, time, token, request, response);
			}else if(getRoomRecode.equals(method)){
				this.getRoomRecode(roomId, startTime, endTime, appId, time, token, request, response);
			}else if(getMediaList.equals(method)){
				this.getMediaList(appId, time, token, request, response);
			}else if(destroyVod.equals(method)){
				this.destroyVod(appId, time, token, userId, fileId, request, response);
			}else{
				result.put("code", 0);
				result.put("data", new JSONObject());
				result.put("message", "方法不存在");
			}
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	
	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;
	/**
	 * 直播间创建
	 */
	public void creatRoom(String liveTitle,String liveStartTime,String viewAuthorized,String appId,String time,String token,HttpServletRequest request,
			HttpServletResponse response){
		JSONObject result = new JSONObject();
		try {
			liveTitle=new String(liveTitle.trim().getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//校检token
		boolean ret=this.ifAccess(appId, time, token);
		if(ret){
			//根据appid查找企业id
			ILiveEnterprise iLiveEnterpris= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
			List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(iLiveEnterpris.getEnterpriseId());
			Long userId = null;
			if(manager!=null&&manager.size()>0){
				for(ILiveManager manager1:manager){
					if(manager1.getLevel()==null||manager1.getLevel()==0){
						userId=manager1.getUserId();
						//往session添加一个记录
						this.addSession(userId, request, response);
					}
				}
			}
			if(userId==null){
				result.put("code", 0);
				result.put("data", new JSONObject());
				result.put("message", "未查找到主账户信息");
			}else{
				RoomCreateVo roomCreateVo=new RoomCreateVo();
				roomCreateVo.setLiveTitle(liveTitle);
				roomCreateVo.setLiveStartTime(liveStartTime);
				roomCreateVo.setViewAuthorized(viewAuthorized==null?"1":viewAuthorized);
				roomCreateVo.setUserId(userId);
				ILiveManager manager1=iLiveManagerMng.selectILiveManagerById(userId);
				Integer level=manager1.getLevel();
				String subTop="";
				List<ILiveSubLevel> iLiveSubLevel = iLiveManagerMng.selectILiveSubById(userId);
				if(iLiveSubLevel!=null && iLiveSubLevel.size()>0) {
					 subTop=iLiveSubLevel.get(0).getSubTop();
					if(subTop==null) {
						subTop="0";
					}
				}
				if(roomCreateVo.getLiveDesc()==null) {
					roomCreateVo.setLiveDesc("");
				}
				if(roomCreateVo.getNeedLogin()==null){
					roomCreateVo.setNeedLogin(0);
				}
				if(roomCreateVo.getRoomType()==null){
					roomCreateVo.setRoomType(0);
				}
				if(level!=null&&level==3&&subTop.indexOf("1")<0) {
					result.put("code", ERROR_STATUS);
					result.put("message", "用户无权限创建直播间");
					result.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, result.toString());
					return;
				}else {
				try {
						Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(roomCreateVo.getLiveTitle());
						Set<String> sensitiveWord2 = new HashSet<>();
						if (roomCreateVo.getLiveDesc()!=null&&!roomCreateVo.getLiveDesc().equals("")) {
							sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(roomCreateVo.getLiveDesc());
						}
						if (sensitiveWord.size()!=0||sensitiveWord2.size()!=0) {
							result.put("code", ERROR_STATUS);
							result.put("message", "包含敏感词");
							if (sensitiveWord.size()!=0) {
								result.put("data", JSONArray.fromObject(sensitiveWord));
							}else if (sensitiveWord2.size()!=0) {
								result.put("data", JSONArray.fromObject(sensitiveWord2));
							}
							ResponseUtils.renderJson(request, response, result.toString());
							return;
						}
						
						
						ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
						Long enterpriseId = iLiveManager.getEnterpriseId();
						ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId.intValue());
						Integer certStatus = iLiveEnterprise.getCertStatus();
						boolean b = EnterpriseUtil.selectIfEn(enterpriseId.intValue(), EnterpriseCache.ENTERPRISE_FUNCTION_createLive,certStatus);
						if(b){
							result.put("code", ERROR_STATUS);
							result.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_createLive_Content);
							ResponseUtils.renderJson(request, response, result.toString());
							return;
						}
						Integer liveType1 = roomCreateVo.getLiveType()==null?1:roomCreateVo.getLiveType();
						if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType1)) {
							boolean b2 = EnterpriseUtil.selectIfEn(enterpriseId.intValue(), EnterpriseCache.ENTERPRISE_FUNCTION_LianMai,certStatus);
							if(b2){
								result.put("code", ERROR_STATUS);
								result.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_LianMai_Content);
								ResponseUtils.renderJson(request, response, result.toString());
								return;
							}
						}
						if (iLiveManager.getCertStatus().intValue() == 4
								&& iLiveEnterprise.getCertStatus().intValue() == 4) {
							Integer saveNewBean = iLiveLiveRoomMng.saveNewBean(roomCreateVo);
							if (saveNewBean > 0) {
								ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(saveNewBean);
								Integer serverGroupId = iliveRoom.getServerGroupId();
								ILiveServerAccessMethod accessMethodBySeverGroupId = null;
								try {
									accessMethodBySeverGroupId = iLiveServerAccessMethodMng
											.getAccessMethodBySeverGroupId(serverGroupId);
								} catch (Exception e) {
									e.printStackTrace();
								}
								String rtmpAddr;
								Integer liveType = iliveRoom.getLiveType();
								if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
									String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
									String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
									rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + saveNewBean
											+ "_host?vhost=" + aliyunLiveDomain;
								} else {
									rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
											+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + saveNewBean + "_tzwj_5000k";
								}
								iliveRoom.setRtmpAddr(rtmpAddr);
								JSONObject putNewLiveInJson = iliveRoom.putPlayNewLiveInJson(iliveRoom);
								result.put("code", SUCCESS_STATUS);
								result.put("message", "生成直播间成功");
								result.put("data", putNewLiveInJson);
							} else {
								result.put("code", ERROR_STATUS);
								result.put("message", "生成直播间失败");
								result.put("data", new JSONObject());
							}
						} else {
							result.put("code", ERROR_STATUS);
							result.put("message", "用户认证未通过,不能创建直播间");
							result.put("data", new JSONObject());
						}
					
				} catch (Exception e) {
					result.put("code", ERROR_STATUS);
					result.put("message", "生成直播间失败");
					result.put("data", new JSONObject());
					e.printStackTrace();
					logger.error(e.toString());
				}
				}
				
			}
		}else{
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "Token校检未通过");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	/**
	 * 直播间列表
	 */
	@SuppressWarnings("unchecked")
	public void RoomList(String appId,String time,String token,Integer pageNo,Integer pageSize,String terminalType,HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		//校检token
		boolean ret=this.ifAccess(appId, time, token);
		if(ret){
			//根据appid查找企业id
			ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
			List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(iLiveEnterprise.getEnterpriseId());
			Long userId = null;
			if(manager!=null&&manager.size()>0){
				for(ILiveManager manager1:manager){
					if(manager1.getLevel()==null||manager1.getLevel()==0){
						userId=manager1.getUserId();
						//往session添加一个记录
						this.addSession(userId, request, response);
					}
				}
			}
			if(userId==null){
				result.put("code", 0);
				result.put("data", new JSONObject());
				result.put("message", "未查找到主账户信息");
			}else{
//				ILiveRoomPhoneAct roomPhone=new ILiveRoomPhoneAct();
				try {
					Pagination pager = iLiveLiveRoomMng.getPagerForManager(null, null, userId, pageNo, pageSize);
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
					int intValue = iLiveManager.getCertStatus().intValue();
					List<JSONObject> jsonList = new ArrayList<JSONObject>();
					if (intValue == 4) {
						if (pager != null && pager.getList() != null) {
							List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pager.getList();
							if (!list.isEmpty()) {
								for (ILiveLiveRoom room : list) {
									JSONObject putNewLiveInJson = room.putPlayNewLiveInJson(room);
									jsonList.add(putNewLiveInJson);
								}
							}
						}
					} else if (intValue == 3 || intValue == 2 || intValue == 5) {
						if (pager != null && pager.getList() != null) {
							List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pager.getList();
							if (!list.isEmpty()) {
								for (ILiveLiveRoom room : list) {
									JSONObject putNewLiveInJson = room.putPlayNewLiveInJson(room);
									jsonList.add(putNewLiveInJson);
									break;
								}
							}
						}
					}
					result.put("code", SUCCESS_STATUS);
					result.put("message", "获取直播间列表成功");
					result.put("data", jsonList);
				} catch (Exception e) {
					result.put("code", ERROR_STATUS);
					result.put("message", "获取直播间列表失败");
					result.put("data", new JSONObject());
					e.printStackTrace();
					logger.error(e.toString());
				}
			}
		}else{
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "Token校检未通过");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	private final static String RTMP_PROTOACAL = "rtmp://";

	
	private static final String HTTP_PROTOCAL = "http://";
	private final static ConcurrentHashMap<Integer, Integer> statusMap = 
			new ConcurrentHashMap<>();
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;
	/**
	 * 直播间修改
	 */
	public void roomEdit(Integer roomId,String liveTitle,Integer LiveSwitch,String liveStartTime,String viewAuthorized,String appId,String time,String token,HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		try {
			liveTitle=new String(liveTitle.trim().getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//校检token
		boolean ret=this.ifAccess(appId, time, token);
		if(ret){
			//根据appid查找企业id
			ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
			List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(iLiveEnterprise.getEnterpriseId());
			Long userId = null;
			if(manager!=null&&manager.size()>0){
				for(ILiveManager manager1:manager){
					if(manager1.getLevel()==null||manager1.getLevel()==0){
						userId=manager1.getUserId();
						//往session添加一个记录
//						this.addSession(userId, request, response);
					}
				}
			}
			if(userId==null){
				result.put("code", 0);
				result.put("data", new JSONObject());
				result.put("message", "未查找到主账户信息");
			}else{
//				iLiveRoomController.iLiveRoomUpdate(event, roomId, LiveSwitch, request, modelMap);
				ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.findByroomId(roomId);
				ILiveEvent event=iliveRoom.getLiveEvent();
				event.setLiveTitle(liveTitle);
				event.setLiveStartTime(Timestamp.valueOf(liveStartTime));
				event.setViewAuthorized(Integer.parseInt(viewAuthorized));
				if(LiveSwitch==1){
					event.setLiveStatus(0);
				}
				//如果为预告状态，发送UMS消息
				if (event.getLiveStatus()==0&&statusMap.get(iliveRoom.getRoomId())==event.getLiveStatus()) {
					advancenotifyUms(roomId);
					iliveRoom.setIsNowInsert(0);
					iLiveLiveRoomMng.update(iliveRoom);
					statusMap.remove(iliveRoom.getRoomId());
				}
				// 选择直播服务器组
				iLiveLiveRoomMng.autoupdate(event, roomId, LiveSwitch);
				ILiveManager user=iLiveManagerMng.selectILiveManagerById(userId);
				workLogMng.save(new WorkLog(WorkLog.MDEOL_CREATEROOM, roomId+"", net.sf.json.JSONObject.fromObject(iliveRoom).toString(), WorkLog.MODEL_CREATEROOM_NAME, userId, user.getUserName(), ""));
				result.put("code", 1);
				result.put("message", "修改成功！");
				 
			}
		}else{
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "Token校检未通过");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	/**
	 * 获取直播间信息
	 */
	public void getRoomMsg(Integer roomId,String appId,String time,String token,HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		//校检token
		boolean ret1=this.ifAccess(appId, time, token);
		if(ret1){
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			try {
				ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
				List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(iLiveEnterprise.getEnterpriseId());
				Long userId = null;
				if(manager!=null&&manager.size()>0){
					for(ILiveManager manager1:manager){
						if(manager1.getLevel()==null||manager1.getLevel()==0){
							userId=manager1.getUserId();
							//往session添加一个记录
//							this.addSession(userId, request, response);
						}
					}
				}
				if(userId==null){
					result.put("code", 0);
					result.put("data", new JSONObject());
					result.put("message", "未查找到主账户信息");
				}else{
					
				}
				boolean ret=this.checkIfIn(roomId, userId);
				if (iliveRoom == null) {
					result.put("code", ERROR_STATUS);
					result.put("message", "直播房间不存在");
					result.put("data", new JSONObject());
				} 
				else if (ret) {
					result.put("code", ERROR_STATUS);
					result.put("message", "直播房间不属于该操作用户");
					result.put("data", new JSONObject());
				} 
				else if (iliveRoom.getOpenStatus() == 0) {
					result.put("code", ERROR_STATUS);
					result.put("message", "直播间已经关闭");
					result.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, result.toString());
					return;
				} else {
					if (ret) {
						result.put("code", ERROR_STATUS);
						result.put("message", "直播房间不属于该操作用户");
						result.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, result.toString());
						return;
					}
					Integer serverGroupId = iliveRoom.getServerGroupId();
					ILiveServerAccessMethod accessMethodBySeverGroupId = null;
					try {
						accessMethodBySeverGroupId = iLiveServerAccessMethodMng
								.getAccessMethodBySeverGroupId(serverGroupId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String rtmpAddr;
					Integer liveType = iliveRoom.getLiveType();
					if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
						String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
						String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
						rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + roomId
								+ "_host?vhost=" + aliyunLiveDomain;
					} else {
						if(iliveRoom.getOpenLogoSwitch()!=null&&iliveRoom.getOpenLogoSwitch()==1) {
							rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
									+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "o_tzwj_5000k";
						}else {
							rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
									+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
						}
						
					}
					iliveRoom.setRtmpAddr(rtmpAddr);
					JSONObject putNewLiveInJson = iliveRoom.putNewcommenLiveInJson(iliveRoom);

					result.put("code", SUCCESS_STATUS);
					result.put("message", "查询直播间状态成功");
					result.put("data", putNewLiveInJson);
				}
			} catch (Exception e) {
				result.put("code", ERROR_STATUS);
				result.put("message", "内部异常");
				result.put("data", new JSONObject());
				logger.error(e.toString());
				e.printStackTrace();
			}
		}else{
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "Token校检未通过");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	/**
	 * 绑定直播间主播接口（每次设置都会覆盖之前的设置）
	 */
	public void bindRoom(Integer roomId,String subaccountId,String appId,String time,String token,HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		//校检token
		boolean ret=this.ifAccess(appId, time, token);
		if(ret){
			//首先查询这个房间下面是否有原先设置，清除掉
			iLiveSubRoomMng.delete(roomId.toString());
			//subaccountId 主播账号ID 可多个，多个之间用 , 分割
			String[] arr = subaccountId.split(",");
			for(int i=0;i<arr.length;i++){
				try {
					ILiveManager iLiveManager=iLiveManagerMng.selectILiveManagerById(Long.parseLong(arr[i]));
					//将iliveroom设置中的子账号id添加进去
					iLiveManager.setRoomId(roomId);
					iLiveManagerMng.updateLiveManager(iLiveManager);
					//在房间号和子账号的关联表中添加一条数据
					IliveSubRoom iLiveSubRoom=new IliveSubRoom();
					Integer id=iLiveSubRoomMng.selectMaxId();
					if(id==null) {
						id=1;
					}else {
						id=id+1;
					}
					iLiveSubRoom.setId(id);
					iLiveSubRoom.setSubAccountId(iLiveManager.getUserId());
					iLiveSubRoom.setLiveRoomId(roomId);
					iLiveSubRoomMng.save(iLiveSubRoom);
					//查询绑定的直播间相关信息
					ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
					Integer serverGroupId = iliveRoom.getServerGroupId();
					ILiveServerAccessMethod accessMethodBySeverGroupId = null;
					try {
						accessMethodBySeverGroupId = iLiveServerAccessMethodMng
								.getAccessMethodBySeverGroupId(serverGroupId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String rtmpAddr;
					Integer liveType = iliveRoom.getLiveType();
					if (ILiveLiveRoom.LIVE_TYPE_MICROPHONE.equals(liveType)) {
						String aliyunLiveDomain = ConfigUtils.get("aliyun_live_domain");
						String aliyunLiveAppname = ConfigUtils.get("aliyun_live_app_name");
						rtmpAddr = "rtmp://video-center.alivecdn.com/" + aliyunLiveAppname + "/" + roomId
								+ "_host?vhost=" + aliyunLiveDomain;
					} else {
						if(iliveRoom.getOpenLogoSwitch()!=null&&iliveRoom.getOpenLogoSwitch()==1) {
							rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
									+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "o_tzwj_5000k";
						}else {
							rtmpAddr = "rtmp://" + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
									+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
						}
						
					}
					iliveRoom.setRtmpAddr(rtmpAddr);
					JSONObject putNewLiveInJson = iliveRoom.putNewcommenLiveInJson(iliveRoom);
					result.put("code", SUCCESS_STATUS);
					result.put("data", putNewLiveInJson);
					result.put("message", "绑定直播间主播接口成功");
				} catch (Exception e) {
					result.put("code", ERROR_STATUS);
					result.put("message", "绑定直播间主播接口失败");
					e.printStackTrace();
				}
			}
		}else{
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "Token校检未通过");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	/**
	 * 查询直播间和账户绑定状态
	 */
	public void getBindList(String appId,String time,String token,HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		//校检token
		boolean ret=this.ifAccess(appId, time, token);
		if(ret){
			ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
			List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(iLiveEnterprise.getEnterpriseId());
			String userIds="";
			if(manager!=null&&manager.size()>0){
				for(ILiveManager managers:manager){
					userIds+=managers.getUserId()+",";
				}
			}
			userIds=userIds.substring(0, userIds.length()-1);
			List<IliveSubRoom> subRoom=iLiveSubRoomMng.getSubRoomByIds(userIds);
			result.put("code", 1);
			result.put("data", subRoom);
			result.put("message", "查询成功");
		}else{
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "Token校检未通过");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	/**
	 * 查询账户Id
	 */
	public void getSubMsg(String appId,String time,String token,HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		//校检token
		boolean ret=this.ifAccess(appId, time, token);
		if(ret){
			ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
			List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(iLiveEnterprise.getEnterpriseId());
			List<AppSubBean> list=new ArrayList<AppSubBean>();
			if(manager!=null&&manager.size()>0){
				for(ILiveManager managers:manager){
					if(managers.getLevel()==null){
						managers.setLevel(0);
					}
					if(managers.getLevel()==3){
						AppSubBean bean=new AppSubBean();
						bean.setUserId(managers.getUserId());
						bean.setNailName(managers.getNailName());
						list.add(bean);
					}
				}
			}
			result.put("code", 1);
			result.put("data", list);
			result.put("message", "查询成功");
		}else{
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "Token校检未通过");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	/**
	 * 查询账户点播视频列表
	 */
	public void getMediaList(String appId,String time,String token,HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		//校检token
		boolean ret=this.ifAccess(appId, time, token);
		if(ret){
			try {
				ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
				List<ILiveMediaFile> listfile = iLiveMediaFileMng.getListByType(1, iLiveEnterprise.getEnterpriseId());
				result.put("code", 1);
				result.put("data", listfile);
				result.put("message", "查询成功");
			} catch (Exception e) {
				result.put("code", 0);
				result.put("data", new JSONObject());
				result.put("message", "查询失败");
			}
		}else{
			result.put("code", 0);
			result.put("data", new JSONObject());
			result.put("message", "Token校检未通过");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	/**
	 * 回看删除
	 */
	public void destroyVod(String appId,String time,String token,Long userId, Long fileId, HttpServletRequest request, HttpServletResponse response) {
		
		JSONObject resultJson = new JSONObject();
		//校检token
		boolean ret=this.ifAccess(appId, time, token);
		if(ret){
			ILiveEnterprise iLiveEnterprise= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
			List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(iLiveEnterprise.getEnterpriseId());
			if(manager!=null&&manager.size()>0){
				for(ILiveManager managers:manager){
					if(managers.getLevel()==null){
						managers.setLevel(0);
					}
					if(managers.getLevel()==0){
						try {
							if (managers.getUserId() == userId.longValue()) {
								ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
								if (mediaFile != null && mediaFile.getUserId().longValue() == managers.getUserId()) {
									iLiveMediaFileMng.deleteVedio(fileId);
									resultJson.put("code", SUCCESS_STATUS);
									resultJson.put("message", "删除成功");
									resultJson.put("data", new JSONObject());
									ResponseUtils.renderJson(request, response, resultJson.toString());
									return;
								} else {
									resultJson.put("code", ERROR_STATUS);
									resultJson.put("message", "非本人文件,禁止删除");
									resultJson.put("data", new JSONObject());
									ResponseUtils.renderJson(request, response, resultJson.toString());
									return;
								}
							} else {
								resultJson.put("code", ERROR_STATUS);
								resultJson.put("message", "非本人操作,禁止删除");
								resultJson.put("data", new JSONObject());
								ResponseUtils.renderJson(request, response, resultJson.toString());
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "删除失败");
							resultJson.put("data", new JSONObject());
							ResponseUtils.renderJson(request, response, resultJson.toString());
							return;
						}
					}
				}
			}
		}else{
			resultJson.put("code", 0);
			resultJson.put("data", new JSONObject());
			resultJson.put("message", "Token校检未通过");
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
public boolean checkIfIn(Integer roomId, Long userId){
	ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
	Integer level=iLiveManager.getLevel();
	if(level==null){
		level=0;
	}
	boolean ret=false;
	if(level==3){
		List<IliveSubRoom> list=iLiveSubRoomMng.selectILiveSubById(userId);
		for(IliveSubRoom subRoom:list){
			if(!roomId.equals(subRoom.getLiveRoomId())){
				ret=true;
			}else{
				ret=false;
			}
		}
	}
	boolean ret1=false;
	ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
	if(iliveRoom.getManagerId().equals(userId)){
		ret1=false;
	}else if(level==3&&ret){
		ret1=true;
	}
	return ret1;
}
/**
 * 获取房间访问记录
 * @param roomId
 * @param startTime
 * @param endTime
 * @param appId
 * @param time
 * @param token
 * @param request
 * @param response
 */
public void getRoomRecode(Integer roomId,String startTime,String endTime,String appId,String time,String token,HttpServletRequest request,HttpServletResponse response){
	JSONObject result = new JSONObject();
	//校检token
	boolean ret=this.ifAccess(appId, time, token);
	if(ret){
		Map<String, String> map = new HashMap<String, String>();
		map.put("roomId", roomId.toString());
		map.put("startTime", startTime);
		map.put("endTime",endTime);
		String infoUrl=ConfigUtils.get("statistics_roomRecode_url");
		String postJson;
		try {
			postJson = HttpUtils.sendPost(infoUrl, map, "UTF-8");
			JSONObject jsonObject = new JSONObject(postJson);
			Integer code=jsonObject.getInt("code");
			Object data=null;
			org.json.JSONArray array=new org.json.JSONArray();
			if(code==1){
				data=jsonObject.getJSONArray("data");
				org.json.JSONArray list=(org.json.JSONArray) data;
				for(int i=0;i<list.length();i++){
					JSONObject msgObject = new JSONObject(list.get(i).toString());
					String userId=msgObject.getString("userId");
					boolean userIdInt = StringPatternUtil.isInteger(userId);
					if(userId==null||"".equals(userId)||!userIdInt){
						userId="0";
					}
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.parseLong(userId));
					String zhxyuserId=null;
					if(iLiveManager==null){
						msgObject.put("thirdUserId", "");
					}else{
						zhxyuserId=iLiveManager.getZhxyuserId();
						if(zhxyuserId==null){
							msgObject.put("thirdUserId","");
						}else{
							msgObject.put("thirdUserId", zhxyuserId);
						}
					}
//					System.out.println(i+"==========:"+msgObject);
					array.put(msgObject);
				}
			}else{
				data=jsonObject.getString("data");
			}
			String message=jsonObject.getString("message");
			result.put("code", code);
			if(code==1){
				result.put("data", array);
			}else{
				result.put("data", data);
			}
			result.put("message", message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}else{
		result.put("code", 0);
		result.put("data", new JSONObject());
		result.put("message", "Token校检未通过");
	}
	ResponseUtils.renderJson(request, response, result.toString());
}

/**
 * 
 * 断点文件上传 1.先判断断点文件是否存在 2.存在直接流上传 3.不存在直接新创建一个文件 4.上传完成以后设置文件名称
 *
 */
@RequestMapping("/fileadd/appendUpload2temp.jspx")
public  void appendUpload2temp(String appId,String time,String token ,HttpServletRequest request,HttpServletResponse response) {
	
	 JSONObject json = new JSONObject();
	 //校检token
	boolean ret=this.ifAccess(appId, time, token);
	if(ret){
		Integer replayUpload=Integer.parseInt(request.getParameter("replayUpload"));
		if(replayUpload==1){
			PrintWriter print = null;
			try {
				request.setCharacterEncoding("utf-8");
				print = response.getWriter();
				String fileSize = request.getParameter("fileSize");
				long totalSize = Long.parseLong(fileSize) ;
				RandomAccessFile randomAccessfile = null;
				long currentFileLength = 0;// 记录当前文件大小，用于判断文件是否上传完成
				String currentFilePath = request.getSession().getServletContext().getRealPath("/temp");// 记录当前文件的绝对路径
				String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
				String temporaryFileName=request.getParameter("temporaryFileName");
				String []msg=temporaryFileName.split("_");
				Long userId=null;
				ILiveEnterprise manage= iLiveEnterpriseMng.getILiveEnterPriseByAppId(msg[1]);
				List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(manage.getEnterpriseId());
				if(manager!=null&&manager.size()>0){
					for(ILiveManager managers:manager){
						if(managers.getLevel()==null){
							managers.setLevel(0);
						}
						if(managers.getLevel()==0){
							userId=managers.getUserId();
						}
					}
				}
				String lastModifyTime =null;
				if(userId==null){
					lastModifyTime = msg[0]+"-";
				}else{
					lastModifyTime = msg[0]+"-"+userId;
				}
				
				File fileD = new File(currentFilePath+ "/" +fileName+"."+lastModifyTime);
				// 存在文件
				if(fileD.exists()){
					randomAccessfile = new RandomAccessFile(fileD, "rw");
				}
				else {
					// 不存在文件，根据文件标识创建文件
					randomAccessfile = new RandomAccessFile(currentFilePath+ "/" +fileName+"."+lastModifyTime, "rw");
				}
				// 开始文件传输
				InputStream in = request.getInputStream();
				randomAccessfile.seek(randomAccessfile.length());
				byte b[] = new byte[1024];
				int n;
				while ((n = in.read(b)) != -1) {
					randomAccessfile.write(b, 0, n);
				}
				
				currentFileLength = randomAccessfile.length();
				
				// 关闭文件
				closeRandomAccessFile(randomAccessfile);
				randomAccessfile = null;
				 JSONObject json1 = new JSONObject();
				 json1.put("currentFileLength", currentFileLength);
				//print.print(currentFileLength);
				 if (currentFileLength == totalSize){
					 String fileMD5=DigestUtils.md5Hex(new FileInputStream(fileD));
					 json1.put("fileMD5", fileMD5);
				 }else{
					 json1.put("fileMD5", "-1"); 
				 }
				json.put("code",1);
				json.put("data", json1);
		        json.put("message","断点续传中");
		       // ResponseUtils.renderJson(response, json.toString());
				
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("status", 0);
				json.put("data", new JSONObject());
			}
		}
		}else{
			json.put("code",0);
	        json.put("message","验证失败");
		}
	ResponseUtils.renderJson(response, json.toString());
}
/**
 * 
 * 断点文件上传 1.先判断断点文件是否存在 2.存在直接流上传 3.不存在直接新创建一个文件 4.上传完成以后设置文件名称
 *
 */
@RequestMapping("/fileadd/appendUpload2Server.jspx")
public  void appendUpload2Server(String appId,String time,String token ,HttpServletRequest request,HttpServletResponse response) {
	
	 JSONObject json = new JSONObject();
	 //校检token
	boolean ret=this.ifAccess(appId, time, token);
	if(ret){
			PrintWriter print = null;
			try {
				request.setCharacterEncoding("utf-8");
				print = response.getWriter();
				String fileSize = request.getParameter("fileSize");
				long totalSize = Long.parseLong(fileSize) ;
				RandomAccessFile randomAccessfile = null;
				long currentFileLength = 0;// 记录当前文件大小，用于判断文件是否上传完成
				String currentFilePath = request.getSession().getServletContext().getRealPath("/temp");// 记录当前文件的绝对路径
				String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
				String temporaryFileName=request.getParameter("temporaryFileName");
				String []msg=temporaryFileName.split("_");
				Long userId=null;
				String userName=null;
				Long enterpriseId=null;
				ILiveEnterprise manage= iLiveEnterpriseMng.getILiveEnterPriseByAppId(msg[1]);
				List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(manage.getEnterpriseId());
				if(manager!=null&&manager.size()>0){
					for(ILiveManager managers:manager){
						if(managers.getLevel()==null){
							managers.setLevel(0);
						}
						if(managers.getLevel()==0){
							userId=managers.getUserId();
							userName=managers.getUserName();
							enterpriseId=managers.getEnterpriseId();
						}
					}
				}
				String lastModifyTime =null;
				if(userId==null){
					lastModifyTime = msg[0]+"-";
				}else{
					lastModifyTime = msg[0]+"-"+userId;
				}
					File oldFile = new File(currentFilePath+ "/" +fileName+"."+lastModifyTime);
					File newFile = new File(currentFilePath+ "/" +fileName);
					if(!oldFile.exists()){
						return;//重命名文件不存在
					}
					if(newFile.exists()){// 如果存在形如test.txt的文件，则新的文件存储为test+当前时间戳.txt, 没处理不带扩展名的文件 
						String newName = fileName.substring(0,fileName.lastIndexOf("."))
								+System.currentTimeMillis()+"."
								+fileName.substring(fileName.lastIndexOf(".")+1);
						newFile = new File(currentFilePath+ "/" +newName);
					}
					if(!oldFile.renameTo(newFile)){
						oldFile.delete();
					}
					String filePath = FileUtils.getTimeFilePath(fileName);
					String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
					ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
					boolean result = false;
					if (accessMethod != null) {
						FileInputStream In = new FileInputStream(newFile);
						result = accessMethod.uploadFile(filePath, In);
					}
					if (newFile.exists()) {
						newFile.delete();
					}
					if (result) {
						
						Long fileId = 0L;
						//UserBean userBean = ILiveUtils.getUser(request);
						ILiveMediaFile selectFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
						if (selectFile != null) {
							selectFile.setFilePath(filePath);
							selectFile.setMediaInfoDealState(0);
							selectFile.setMediaInfoStatus(0);
							selectFile.setUserName(userName);
							selectFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
							iLiveMediaFileMng.updateMediaFile(selectFile);
						} else {
							selectFile = new ILiveMediaFile();
							selectFile.setMediaFileName(fileName.substring(0, fileName.lastIndexOf(".")));
							selectFile.setCreateStartTime(new Timestamp(System.currentTimeMillis()));
							selectFile.setCreateType(3);
							selectFile.setFileType(1);
							selectFile.setOnlineFlag(1);
							selectFile.setDelFlag(0);
							selectFile.setDuration(0);
							selectFile.setFileRate(0D);
							selectFile.setWidthHeight("");
							selectFile.setFileSize(0L);
							selectFile.setUserName(userName);
							selectFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
							selectFile.setCreateEndTime(new Timestamp(new Date().getTime()));
							//UserBean user = ILiveUtils.getUser(request);
							selectFile.setEnterpriseId(enterpriseId);
							selectFile.setMediaCreateTime(selectFile.getCreateStartTime());
							selectFile.setAllowComment(1);
							selectFile.setFilePath(filePath);
							selectFile.setUserId(userId);
							selectFile.setMediaInfoStatus(0);
							selectFile.setMediaInfoDealState(0);
							iLiveMediaFileMng.saveIliveMediaFile(selectFile);
						}
						String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
						+ accessMethod.getMountInfo().getBase_path() + filePath;
						List<ILiveMediaFile> list=iLiveMediaFileMng.getListByTypeAndName(fileName.substring(0, fileName.lastIndexOf(".")), 1, enterpriseId.intValue());
						if(list!=null&&list.size()>0){
							json.put("data", list.get(0));
						}else{
							json.put("data", new JSONObject());
						}
						json.put("status", 1);
//						json.put("filePath", filePath);
//						json.put("httpUrl", httpUrl);
						ResponseUtils.renderJson(response, json.toString());
						
						//上传文件以后调用转码服务进行转码
						ILiveConvertTask iLiveConvertTask = iLiveConvertTaskMng.createConvertTask(selectFile, accessMethod);
						iLiveConvertTaskMng.addConvertTask(iLiveConvertTask);
						//UserBean user = ILiveUtils.getUser(request);
						//workLogMng.save(new WorkLog(WorkLog.MODEL_UPLOADVIDEO, iliveRoom.getRoomId()+"", net.sf.json.JSONObject.fromObject(iliveRoom).toString(), WorkLog.MODEL_UPLOADVIDEO_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
						//将相应的内存占用通知计费系统
//						EnterpriseUtil.openEnterprise(user.getEnterpriseId(),EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,totalSize/1024L+"",user.getCertStatus());
						return;
					} else {
						json.put("status", 0);
						json.put("data", new JSONObject());
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
					
					
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("status", 0);
				json.put("data", new JSONObject());
				ResponseUtils.renderJson(response, json.toString());
			}
		
		}else{
			json.put("code",0);
	        json.put("message","验证失败");
	        ResponseUtils.renderJson(response, json.toString());
		}
}
/**
 * 获取已上传的文件大小
 * @param request
 * @param response
 */
@RequestMapping("/fileadd/getChunkedFileSize.jspx")
public void getChunkedFileSize(String appId,String time,String token ,HttpServletRequest request,HttpServletResponse response){
	 JSONObject json = new JSONObject();
	 //校检token
	boolean ret=this.ifAccess(appId, time, token);
	if(ret){
		//存储文件的路径，根据自己实际确定
	    String currentFilePath = request.getSession().getServletContext().getRealPath("/temp");
	    System.out.println(currentFilePath);
	    PrintWriter print = null;
	    try {
	    	JSONObject json1 = new JSONObject();
	        request.setCharacterEncoding("utf-8");
	        print = response.getWriter();
	        String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
	        String temporaryFileName=request.getParameter("temporaryFileName");
			String []msg=temporaryFileName.split("_");
			Long userId=null;
			ILiveEnterprise manage= iLiveEnterpriseMng.getILiveEnterPriseByAppId(msg[1]);
			List<ILiveManager> manager = iLiveManagerMng.getILiveManagerByEnterpriseId(manage.getEnterpriseId());
			if(manager!=null&&manager.size()>0){
				for(ILiveManager managers:manager){
					if(managers.getLevel()==null){
						managers.setLevel(0);
					}
					if(managers.getLevel()==0){
						userId=managers.getUserId();
					}
				}
			}
			String lastModifyTime =null;
			if(userId==null){
				lastModifyTime = msg[0]+"-";
			}else{
				lastModifyTime = msg[0]+"-"+userId;
			}
	        File file = new File(currentFilePath+fileName+"."+lastModifyTime);
	        if(file.exists()){
	           // print.print(file.length());
	            json1.put("fileSize", file.length());
	        }else{
	          //  print.print(-1);
	            json1.put("fileSize", -1); 
	        }
	        json.put("code",1);
	        json.put("message","获取已上传大小成功");
	        json.put("data",json1);
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        json.put("code",0);
	        json.put("message","获取已上传大小失败");
	        json.put("data",new JSONObject());
	    }
	}else{
		json.put("code",0);
        json.put("message","验证失败");
	}
	ResponseUtils.renderJson(response, json.toString());
    
}
/**
 * 构建临时文件
 *
 * @param tempFilePath
 * @param file
 * @return
 */
private File createTempFile(String tempFilePath, MultipartFile file) {
    long start = System.currentTimeMillis();
    File tempFile = new File(tempFilePath);
    if (null != tempFile && !tempFile.exists()) {
        tempFile.mkdirs();
    }
    try {
        file.transferTo(tempFile);
    } catch (Exception e) {
        e.printStackTrace();
    }
    long end = System.currentTimeMillis();
    logger.info("创建临时上传文件耗时：{} ms", end - start);
    return tempFile;
}
/**
 * 关闭随机访问文件
 * 
 * @param rfile
 */
public static void closeRandomAccessFile(RandomAccessFile rfile) {
    if (null != rfile) {
        try {
            rfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
}
