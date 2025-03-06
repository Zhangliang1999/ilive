package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.xml.sax.InputSource;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.constants.ILivePlayCtrType;
import com.bvRadio.iLive.iLive.constants.ILivePlayStatusConstant;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFileDoc;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivity;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivityStatistic;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireRoom;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.ILiveUserMeetRole;
import com.bvRadio.iLive.iLive.entity.ILiveVoteActivity;
import com.bvRadio.iLive.iLive.entity.ILiveVotePeople;
import com.bvRadio.iLive.iLive.entity.ILiveVoteRoom;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileDocMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMeetRequestMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnairePeopleMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireStatisticMng;
import com.bvRadio.iLive.iLive.manager.ILiveRandomRecordTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomStaticsMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserMeetRoleMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveVotePeopleMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteRoomMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.service.Notify;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.ConvertThread;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.ILiveUMSMessageUtil;
import com.bvRadio.iLive.iLive.util.IPUtils;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.util.QrCodeUtils;
import com.bvRadio.iLive.iLive.util.RoomNoticeUtil;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.util.StringUtil;
import com.bvRadio.iLive.iLive.util.TwoDimensionCode;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUserViewStatics;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.iLive.web.PostMan;
import com.google.gson.Gson;
import com.jwzt.ums.api.UmsApi;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/liveevent")
public class ILiveEventApiController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String HTTP_PROTOCAL = "http://";

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILiveRoomStaticsMng iLiveRoomStaticsMng;

	@Autowired
	private ILiveRandomRecordTaskMng iLiveRandomRecordTaskMng;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveVideoHistoryMng iLiveVideoHisotryMng;

	@Autowired
	private ILivePageDecorateMng pageDecorateMng;

	@Autowired
	private BBSDiyformMng bbsDiyformMng;
	@Autowired
	private ILiveQuestionnaireActivityMng iLiveQuestionnaireActivityMng;
	@Autowired
	private ILiveQuestionnaireRoomMng iLiveQuestionnaireRoomMng;
	@Autowired
	private ILiveQuestionnairePeopleMng iLiveQuestionnairePeopleMng;
	@Autowired
	private ILiveQuestionnaireStatisticMng iLiveQuestionnaireStatisticMng;
	@Autowired
	private ILiveVoteActivityMng iLiveVoteActivityMng;	//投票活动
	
	@Autowired
	private ILiveVotePeopleMng iLiveVotePeopleMng;		//用户投票记录
	
	@Autowired
	private ILiveVoteRoomMng iLiveVoteRoomMng;
	@Autowired
	private BBSDiymodelMng bbsDiymodelMng;
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	@Autowired
	private ILiveFileDocMng fileDocMng;
	@Autowired
	private DocumentManagerMng documentManagerMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private Notify notify;
	@Autowired
	private ILiveViewWhiteBillMng viewWhiteMng;
	@Autowired
	private ILiveUserMeetRoleMng meetRoleMng;
	@Autowired
	private ILiveMeetRequestMng  meetRequestMng;
	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;
	@Autowired
	private SentitivewordFilterMng sentitivewordFilterMng;
	private final static String RTMP_PROTOACAL = "rtmp://";
	@RequestMapping(value = "live/start.jspx")
	public void livestart(String terminalType,Integer playType, Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		UserBean userBean = ILiveUtils.getUser(request);
		System.out.println("查询房间信息===========："+iliveRoom);
		System.out.println("获取session用户信息===========："+userBean);
		//判断是否还有直播时长
		boolean ret=EnterpriseUtil.getIfCan(iliveRoom.getEnterpriseId(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration.toString(), userBean.getCertStatus());
		//判断是否账号过期
		Integer days=null;
		try {
			
			 days=EnterpriseUtil.checkValiteTime(iliveRoom.getEnterpriseId(), userBean.getCertStatus());
			 System.out.println("查询账号是否到期！"+days);
			
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(days==null){
			days=1;
		}
		if(ret||days.equals(0)) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "无可用直播时长");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
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
		    				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		    		
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
					resultJson.put("message", "创建logo转码任务失败");
					e.printStackTrace();
				}
			}
			
		}else {
			if(iliveRoom.getConvertTaskId()!=null&&iliveRoom.getConvertTaskId()>0) {

				try {
					String data1=HttpUtils.sendStr(converTaskLogo+"/api/task/"+iliveRoom.getConvertTaskId()+"/stop", "PUT", "");
					
		            	ConvertThread thread =new ConvertThread(roomId, iLiveRoomMng, iliveRoom.getConvertTaskId());
		            	thread.run();
			           
			           
				} catch (Exception e) {
					resultJson.put("message", "删除logo转码任务失败");
					e.printStackTrace();
				}
			}
			
		}
		
		
		
		// 根据直播状态
		if (liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			try {
				//notifyUms(roomId);
				System.out.println("通知直播间垫片");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (playType != null) {
				// 拉流直播
				if (playType.equals(ILivePlayCtrType.POOL_LIVE)) {
					// 先判断直播状态，需要是未开始或者已经结束了的
					iliveRoom.getLiveEvent().setPlayType(ILivePlayCtrType.POOL_LIVE);
				}
				// 列表直播
				else if (playType.equals(ILivePlayCtrType.LIST_LIVE)) {
					iliveRoom.getLiveEvent().setPlayType(ILivePlayCtrType.LIST_LIVE);
				} else {
					/**
					 * 获取直播间推流地址
					 */
					ILiveServerAccessMethod accessMethodBySeverGroupId = null;
					try {
						accessMethodBySeverGroupId = iLiveServerAccessMethodMng
								.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
						
						try {
							//发送通知
							notify.start(roomId, accessMethodBySeverGroupId.toString());
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("发送通知失败");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iliveRoom);
					} catch (Exception e1) {
						resultJson.put("status", ERROR_STATUS);
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "流服务器交互失败");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						e1.printStackTrace();
						return;
					}
					// 正常向流媒体服务器推流操作
					startPlayAfter(playType, null, request, iliveRoom, accessMethodBySeverGroupId);
					resultJson.put("status", SUCCESS_STATUS);
					resultJson.put("message", "操作成功");
				}
			} else {
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iliveRoom);
				} catch (Exception e1) {
					resultJson.put("status", ERROR_STATUS);
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "流服务器交互失败");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					e1.printStackTrace();
					return;
				}
				ILiveEvent liveEvent = iliveRoom.getLiveEvent();
				liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
				liveEvent.setLiveStartTime(new Timestamp(System.currentTimeMillis()));
				liveEvent.setRealStartTime(new Timestamp(System.currentTimeMillis()));
				iLiveEventMng.updateILiveEvent(liveEvent);
				iliveRoom.setLiveEvent(liveEvent);
				String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
				iliveRoom.setHlsAddr(pushStreamAddr);
				RoomNoticeUtil.nptice(iliveRoom);
				resultJson.put("status", SUCCESS_STATUS);
				resultJson.put("message", "操作成功");
			}
		} else {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合开始标准");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());

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
	@RequestMapping(value = "live/stoptest.jspx")
	public void liveStopTest() {
		boolean ret=EnterpriseUtil.openEnterprise(670,EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration,"7200",4);
		System.out.println("更新直播时长结果："+ret);
	}
	
	@RequestMapping(value = "live/stop.jspx")
	public void livestop(String terminalType,Integer playType, final Integer roomId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		String converTaskLogo=ConfigUtils.get("conver_task_logo");
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)
				|| liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
			System.out.println("直播状态"+liveStatus);
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合结束标准");
		} else {
			ILiveServerAccessMethod accessMethodBySeverGroupId = null;
			try {
				accessMethodBySeverGroupId = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (iliveRoom.getLiveEvent().getPlayType().equals(ILivePlayCtrType.POOL_LIVE)) {
					ILiveUMSMessageUtil.INSTANCE.closePullStream(accessMethodBySeverGroupId, iliveRoom);
				} else {
					ILiveUMSMessageUtil.INSTANCE.stopPlay(accessMethodBySeverGroupId, iliveRoom);
				}
			} catch (Exception e1) {
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "流服务器交互失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				e1.printStackTrace();
				return;
			}
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			liveEvent.setLiveEndTime(timestamp);
			liveEvent.setRealEndTime(timestamp);
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			
			RoomNoticeUtil.nptice(iliveRoom);
			iLiveRoomMng.stopTask(liveEvent.getLiveEventId());
			iliveRoom.setIsNowInsert(0);
			iLiveRoomMng.update(iliveRoom);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			//更新企业直播时长
			Integer enterpriseId=iliveRoom.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise1 = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			Integer certStatus = null;
			if(iLiveEnterPrise1!=null){
				certStatus = iLiveEnterPrise1.getCertStatus();
			}
			//判断是否有暂停过，若有，从暂停的开始时间开始计算
			Timestamp lastStartTime=null;
			if(liveEvent.getLastPauseStartTime()!=null){
				lastStartTime=liveEvent.getLastPauseStartTime();
			}else{
				lastStartTime=liveEvent.getRealStartTime();
			}
			Long useValue=(liveEvent.getRealEndTime().getTime()-lastStartTime.getTime())/1000;
			
			EnterpriseUtil.openEnterprise(enterpriseId,EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration,useValue.toString(),certStatus);
			
			
			//结束直播通知服务器
			try {
				//发送通知
				notify.end(roomId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("发送通知失败");
			}
			
			
			// 正常向流媒体服务器推流操作
			String realIpAddr = IPUtils.getRealIpAddr(request);
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
			iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
			ILiveUserViewStatics.INSTANCE.stopLive(iliveRoom, realIpAddr);
			final Integer serverGroupId = iliveRoom.getServerGroupId();
			final Long userIdLong = iliveRoom.getManagerId();
			final ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(liveEvent.getLiveEventId(),
					userIdLong, ILivePlayStatusConstant.PLAY_ING);
			if (ILivePlayStatusConstant.PAUSE_ING != liveEvent.getLiveStatus()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
									.getAccessMethodBySeverGroupId(serverGroupId);
							String postUrl = HTTP_PROTOCAL + serverAccessMethod.getHttp_address() + ":"
									+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
							String mountName = "live" + roomId;
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
										ILiveServerAccessMethod vodAccessMethod = iLiveServerAccessMethodMng
												.getAccessMethodBySeverGroupId(vodGroupId);
										liveMediaFile.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
										liveMediaFile.setCreateType(0);
										liveMediaFile.setDuration(length);
										liveMediaFile.setFileType(1);
										liveMediaFile.setOnlineFlag(1);
										//查询是否还有
										// 通过用户拿到企业
										liveMediaFile.setEnterpriseId((long) iliveRoom.getEnterpriseId());
										liveMediaFile.setUserId(userIdLong);
										liveMediaFile.setMediaInfoStatus(0);
										liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
										liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
										liveMediaFile.setDelFlag(0);
										liveMediaFile.setAllowComment(1);
										liveMediaFile.setMediaInfoDealState(0);
										//根据userid获取username
										ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userIdLong);
										liveMediaFile.setUserName(iLiveManager.getUserName());
										try {
											Thread.sleep(15000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										//录制
										Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
										DocumentManager doc= documentManagerMng.getById(iliveRoom.getLiveEvent().getDocumentId());
										if(doc!=null) {
											ILiveFileDoc fileDoc =new ILiveFileDoc();
											fileDoc.setFileId(saveIliveMediaFile);
											fileDoc.setDocument(doc);
											fileDocMng.save(fileDoc);
										}
										
										iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile, userIdLong);
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
											ILiveServerAccessMethod vodAccessMethod = iLiveServerAccessMethodMng
													.getAccessMethodBySeverGroupId(vodGroupId);
											liveMediaFile
													.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
											liveMediaFile.setCreateType(0);
											liveMediaFile.setDuration(length);
											liveMediaFile.setFileType(1);
											liveMediaFile.setOnlineFlag(1);
											// 通过用户拿到企业
											liveMediaFile.setEnterpriseId((long) iliveRoom.getEnterpriseId());
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
											iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile, userIdLong);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
		
		try {
			String data1=HttpUtils.sendStr(converTaskLogo+"/api/task/"+iliveRoom.getConvertTaskId()+"/stop", "PUT", "");
			
            	ConvertThread thread =new ConvertThread(roomId, iLiveRoomMng, iliveRoom.getConvertTaskId());
            	thread.run();
	           
	           
		} catch (Exception e) {
			resultJson.put("message", "删除logo转码任务失败");
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/live/pause.jspx")
	public void livepause(String terminalType,Integer playType, final Integer roomId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		if (!liveStatus.equals(ILivePlayStatusConstant.PLAY_ING)) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合暂停标准");
		} else {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PAUSE_ING);
			liveEvent.setLastPauseStopTime(new Timestamp(System.currentTimeMillis()));
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
			//与计费系统交互计算时长
			Timestamp lastStartTime=null;
			if(liveEvent.getLastPauseStartTime()!=null){
				lastStartTime=liveEvent.getLastPauseStartTime();
			}else{
				lastStartTime=liveEvent.getRealStartTime();
			}
			final Long userIdLong = iliveRoom.getManagerId();
			final Integer enterpriseId = iliveRoom.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise1 = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			Integer certStatus = null;
			if(iLiveEnterPrise1!=null){
				certStatus = iLiveEnterPrise1.getCertStatus();
			}
			Long useValue=(liveEvent.getLastPauseStopTime().getTime()-lastStartTime.getTime())/1000;
			
			EnterpriseUtil.openEnterprise(enterpriseId,EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration,useValue.toString(),certStatus);
			
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					Integer serverGroupId = iliveRoom.getServerGroupId();
					ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
					String postUrl = HTTP_PROTOCAL + serverAccessMethod.getHttp_address() + ":"
							+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
					String mountName = "live" + roomId;
					int length = (int) (System.currentTimeMillis()
							- iliveRoom.getLiveEvent().getRecordStartTime().getTime()) / 1000;
					int vodGroupId = Integer.parseInt(ConfigUtils.get("defaultVodServerGroupId"));
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
							ILiveMediaFile liveMediaFile = new ILiveMediaFile();
							liveMediaFile.setMediaFileName(iliveRoom.getLiveEvent().getLiveTitle() + "-"
									+ new SimpleDateFormat("yyyyMMddHHmmss")
											.format(iliveRoom.getLiveEvent().getRecordStartTime()));
							liveMediaFile.setFilePath(relativePath);
							liveMediaFile.setCreateStartTime(iliveRoom.getLiveEvent().getRecordStartTime());
							liveMediaFile.setMediaCreateTime(new Timestamp(System.currentTimeMillis()));
							liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
							ILiveServerAccessMethod serverAccessMethodVod = iLiveServerAccessMethodMng
									.getAccessMethodBySeverGroupId(vodGroupId);
							liveMediaFile.setServerMountId(serverAccessMethodVod.getMountInfo().getServer_mount_id());
							liveMediaFile.setCreateType(0);
							liveMediaFile.setDuration(length);
							liveMediaFile.setFileType(1);
							liveMediaFile.setOnlineFlag(1);
							liveMediaFile.setMediaInfoDealState(0);
							// 通过用户拿到企业
							liveMediaFile.setEnterpriseId((long) enterpriseId);
							liveMediaFile.setMediaInfoStatus(0);
							liveMediaFile.setUserId(userIdLong);
							liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
							liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
							liveMediaFile.setDelFlag(0);
							liveMediaFile.setAllowComment(1);
							try {
								Thread.sleep(15000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
							iLiveVideoHisotryMng.saveVideoHistory(roomId, saveIliveMediaFile, userIdLong);
						}
					}
				}
			}).start();
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	@RequestMapping(value = "/live/continue.jspx")
	public void livecontinue(String terminalType,Integer playType, final Integer roomId, HttpServletRequest request,
			HttpServletResponse response,boolean isPush,String pullUrl) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		final	ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		Integer Status=iliveRoom.getLiveEvent().getLiveStatus();
		UserBean userBean = ILiveUtils.getUser(request);
		System.out.println("查询房间信息===========："+iliveRoom);
		System.out.println("获取session用户信息===========："+userBean);
		//判断是否还有直播时长
				boolean ret=EnterpriseUtil.getIfCan(iliveRoom.getEnterpriseId(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration.toString(), userBean.getCertStatus());
				System.out.println("直播时长查询结果："+ret);
				//判断是否账号过期
				Integer days=null;
				try {
					 days=EnterpriseUtil.checkValiteTime(iliveRoom.getEnterpriseId(), userBean.getCertStatus());
					 System.out.println("查询账号是否到期！"+days);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(days==null){
					days=1;
				}
				if(ret||days.equals(0)) {
					resultJson.put("status", ERROR_STATUS);
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "无可用直播时长");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
		
		final	String converTaskLogo=ConfigUtils.get("conver_task_logo");
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
		    				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
		    		
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
					resultJson.put("message", "创建logo转码任务失败");
					e.printStackTrace();
				}
			}
			
		}else {
			if(iliveRoom.getConvertTaskId()!=null&&iliveRoom.getConvertTaskId()>0) {

				try {
					String data1=HttpUtils.sendStr(converTaskLogo+"/api/task/"+iliveRoom.getConvertTaskId()+"/stop", "PUT", "");
					
					//创建一个新的字符串
			        StringReader read = new StringReader(data1);
			        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			        InputSource source = new InputSource(read);
			        //创建一个新的SAXBuilder
			        SAXBuilder sb = new SAXBuilder();
			      //通过输入源构造一个Document
		            Document doc = sb.build(source);
		            //取的根元素
		            Element root = doc.getRootElement();
		            final Integer id =iliveRoom.getConvertTaskId();
		            if("success".equals(root.getName())) {
		            	iliveRoom.setConvertTaskId(-1);
		            	
		            }
		            new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(10000);
								String data=HttpUtils.doDelete("", converTaskLogo+"/api/task/"+id);
								iLiveRoomMng.update(iliveRoom);
							} catch (Exception e) {
								
								e.printStackTrace();
							}
							
						}
		            	
		            }).start();
		            
				} catch (Exception e) {
					resultJson.put("message", "删除logo转码任务失败");
					e.printStackTrace();
				} 
			}
			
		}
		
		
		
		
		
		
		
		
		
		
		Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
		Integer serverGroupId = iliveRoom.getServerGroupId();
		/**
		 * 获取直播间推流地址
		 */
		ILiveServerAccessMethod accessMethodBySeverGroupId = null;
		try {
			accessMethodBySeverGroupId = iLiveServerAccessMethodMng.getAccessMethodBySeverGroupId(serverGroupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 直播结束的继续直播
		if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)) {
			try {
				if (isPush) {
					boolean startPlay = ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iliveRoom);
				}else {
					ILiveUMSMessageUtil.INSTANCE.pullStream(pullUrl, accessMethodBySeverGroupId, iliveRoom);
				}
				
			} catch (Exception e1) {
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "流服务器交互失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				e1.printStackTrace();
				return;
			}
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			long oldLiveEventId = liveEvent.getLiveEventId();
			ILiveEvent iLiveEventNew = new ILiveEvent();
			try {
				BeanUtilsExt.copyProperties(iLiveEventNew, liveEvent);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			iLiveEventNew.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
			iLiveEventNew.setRealStartTime(new Timestamp(System.currentTimeMillis()));
			List<PageDecorate> pageRecordList = pageDecorateMng.getList(iliveRoom.getLiveEvent().getLiveEventId());
			iLiveEventNew.setPageRecordList(pageRecordList);
			Timestamp startTmp = new Timestamp(System.currentTimeMillis());
			iLiveEventNew.setLiveStartTime(startTmp);
			iLiveEventNew.setLiveEndTime(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
			iLiveEventNew.setRecordStartTime(startTmp);
			iLiveEventNew.setRealStartTime(startTmp);
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
			viewWhiteMng.updateEventId(oldLiveEventId,saveIliveMng);
			
			
			iLiveEventNew.setLiveEventId(saveIliveMng);
			iLiveEventNew.setFormId(formId);
			iliveRoom.setLiveEvent(iLiveEventNew);
			iLiveRoomMng.update(iliveRoom);
			// 正常向流媒体服务器推流操作
			String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live"
					+ iliveRoom.getRoomId() + "/5000k/tzwj_video.m3u8";
			iliveRoom.setHlsAddr(pushStreamAddr);
			// RoomNoticeUtil.nptice(iliveRoom);
			startPlayAfter(playType, null, request, iliveRoom, accessMethodBySeverGroupId);
			// dealRoomStatic(iliveRoom);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		}
		// 暂停结束后的继续直播
		else if (liveStatus.equals(ILivePlayStatusConstant.PAUSE_ING)) {
			ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
			liveEvent.setLastPauseStartTime(new Timestamp(System.currentTimeMillis()));
			liveEvent.setRecordStartTime(new Timestamp(System.currentTimeMillis()));
			iLiveEventMng.updateILiveEvent(liveEvent);
			iliveRoom.setLiveEvent(liveEvent);
			String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + ":"
					+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "/5000k/tzwj_video.m3u8";
			iliveRoom.setHlsAddr(pushStreamAddr);
			RoomNoticeUtil.nptice(iliveRoom);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		} else {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "直播状态不符合继续直播标准");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

	public boolean startPlayAfter(Integer playType, String pullAddr, HttpServletRequest request,
			ILiveLiveRoom iliveRoom, ILiveServerAccessMethod accessMethodBySeverGroupId) {
		// 正常向流媒体服务器推流操作
		String realIpAddr = IPUtils.getRealIpAddr(request);
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
		iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
		ILiveUserViewStatics.INSTANCE.startLive(iliveRoom, realIpAddr, "pc");
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
		liveEvent.setRecordStartTime(new Timestamp(System.currentTimeMillis()));
		liveEvent.setRealStartTime(new Timestamp(System.currentTimeMillis()));
		liveEvent.setPlayType(playType);
		if (pullAddr != null) {
			liveEvent.setPollStreamAddr(pullAddr);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		iliveRoom.setLiveEvent(liveEvent);
		String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live"
				+ iliveRoom.getRoomId() + "/5000k/tzwj_video.m3u8";
		iliveRoom.setHlsAddr(pushStreamAddr);
		RoomNoticeUtil.nptice(iliveRoom);
		dealRoomStatic(iliveRoom);
		return true;
	}

	/**
	 * 
	 * @param iliveRoom
	 */
	public void dealRoomStatic(ILiveLiveRoom iliveRoom) {
		try {
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			Long nowNum = 0L;
			if (userListMap == null) {
			} else {
				ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(iliveRoom.getRoomId());
				if (concurrentHashMap != null && !concurrentHashMap.isEmpty()) {
					nowNum = (long) concurrentHashMap.size();
				}
			}
			ILiveEvent iLiveEventNew = iliveRoom.getLiveEvent();
			iLiveRoomStaticsMng.initRoom(iLiveEventNew.getLiveEventId(), nowNum);
			ApplicationCache.LiveEventUserCache.put(iLiveEventNew.getLiveEventId(), nowNum);
			ApplicationCache.getOnlineNumber().remove(iliveRoom.getRoomId());
			iLiveRoomMng.startTask(iliveRoom.getLiveEvent().getLiveEventId(), iliveRoom.getRoomId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
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
	@RequestMapping(value = "close.jspx")
	public void iLiveRoomClose(String terminalType,Integer roomId, HttpServletRequest request, ModelMap modelMap,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		try {
			// 选择直播服务器组
			ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
			if (iliveRoom == null) {
				jsonObject.put("code", 0);
				jsonObject.put("message", "删除失败,直播间不存在");
				ResponseUtils.renderJson(request, response, jsonObject.toString());
				return;
			}

			if (iliveRoom.getLiveEvent().getLiveStatus().intValue() == ILivePlayStatusConstant.PAUSE_ING.intValue()) {
				jsonObject.put("code", 0);
				jsonObject.put("message", "删除失败,直播间正在直播,请先结束");
				ResponseUtils.renderJson(request, response, jsonObject.toString());
				return;
			}
			iliveRoom.setOpenStatus(0);
			iLiveRoomMng.update(iliveRoom);
			jsonObject.put("code", "1");
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
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", 0);
		}
		ResponseUtils.renderJson(request, response, jsonObject.toString());
	}

	/**
	 * 拉流检测
	 */
	@RequestMapping(value = "live/pullstream.jspx")
	public void pullStream(String terminalType,Integer roomId, String pullAddr, Integer playType, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		System.out.println("拉流直播開始"); 
		try {
			if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
				JedisUtils.delObject("roomInfo:"+roomId);
			}
			ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
			Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
			UserBean userBean = ILiveUtils.getUser(request);
			System.out.println("认证状态：====="+userBean.getCertStatus());
			//判断是否还有直播时长
			boolean ret=EnterpriseUtil.getIfCan(iliveRoom.getEnterpriseId(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration.toString(), userBean.getCertStatus());
			//判断是否账号过期
			Integer days=null;
			try {
				
				 days=EnterpriseUtil.checkValiteTime(iliveRoom.getEnterpriseId(), userBean.getCertStatus());
				 System.out.println("查询账号是否到期！"+days);
				
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 if(days==null){
				 days=1; 
			 }
			
			if (iliveRoom == null) {
				resultJson.put("message", "直播间不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}else if(ret||days.equals(0)) {
				resultJson.put("status", ERROR_STATUS);
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "无可用直播时长");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				Integer enterpriseId = iliveRoom.getEnterpriseId();
				ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
				Integer certStatus = iLiveEnterprise.getCertStatus();
				System.err.println("拉流直播開始************");
				if(playType==1){
					//设备直播
					boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_equipmentLive,certStatus);
					if(b){
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_equipmentLive_Content);
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				}else if(playType==2){
					//拉流 
//					boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_lahueLive,certStatus);
					boolean b=false;
					if(b){
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_lahueLive_Content);
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				}else if(playType==5){
					//手机
					boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_cellPhone,certStatus);
					if(b){
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_cellPhone_Content);
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				}else if(playType==7){
					//垫片
					boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_Gasket,certStatus);
					if(b){
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_Gasket_Content);
						ResponseUtils.renderJson(request, response, resultJson.toString());
						return;
					}
				}
				System.err.println("拉流直播開始************判斷playType結束");
				if (liveStatus.equals(ILivePlayStatusConstant.PLAY_ING)) {
					resultJson.put("status", ERROR_STATUS);
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "直播中,不能启动拉流直播,请先结束直播");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				} else if (liveStatus.equals(ILivePlayStatusConstant.PAUSE_ING)) {
					resultJson.put("status", ERROR_STATUS);
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "暂停中,不能启动拉流直播,请先结束直播");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
				System.err.println("拉流直播開始************直播中暫停中判斷完畢");
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					Integer serverGroupId = iliveRoom.getServerGroupId();
					accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(serverGroupId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("開始判斷***********************");
				if (liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
					try {
						System.out.println("預告狀態");
						ILiveUMSMessageUtil.INSTANCE.pullStream(pullAddr, accessMethodBySeverGroupId, iliveRoom);
					} catch (Exception e1) {
						resultJson.put("status", ERROR_STATUS);
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "流服务器交互失败");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						e1.printStackTrace();
						return;
					}
					System.out.println("預告狀態：發送消息到統計******");
					startPlayAfter(playType, pullAddr, request, iliveRoom, accessMethodBySeverGroupId);
				} else if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)) {
					try {
						System.out.println("結束狀態");
						ILiveUMSMessageUtil.INSTANCE.pullStream(pullAddr, accessMethodBySeverGroupId, iliveRoom);
					} catch (Exception e1) {
						resultJson.put("status", ERROR_STATUS);
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "流服务器交互失败");
						resultJson.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, resultJson.toString());
						e1.printStackTrace();
						return;
					}
					System.out.println("結束狀態：發送消息到統計******");
					startPlayAfter(playType, pullAddr, request, iliveRoom, accessMethodBySeverGroupId);
					// 继续直播
					ILiveEvent liveEvent = iliveRoom.getLiveEvent();
					long oldLiveEventId = liveEvent.getLiveEventId();
					ILiveEvent iLiveEventNew = new ILiveEvent();
					try {
						BeanUtilsExt.copyProperties(iLiveEventNew, liveEvent);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					iLiveEventNew.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
					iLiveEventNew.setRealStartTime(new Timestamp(System.currentTimeMillis()));
					List<PageDecorate> pageRecordList = pageDecorateMng
							.getList(iliveRoom.getLiveEvent().getLiveEventId());
					iLiveEventNew.setPageRecordList(pageRecordList);
					Timestamp startTmp = new Timestamp(System.currentTimeMillis());
					iLiveEventNew.setLiveStartTime(startTmp);
					iLiveEventNew.setLiveEndTime(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
					iLiveEventNew.setRecordStartTime(startTmp);
					iLiveEventNew.setRealStartTime(startTmp);
					iLiveEventNew.setPlayType(playType);
					iLiveEventNew.setPollStreamAddr(pullAddr);
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
					iliveRoom.setLiveEvent(iLiveEventNew);
					iLiveRoomMng.update(iliveRoom);
					String pushStreamAddr = HTTP_PROTOCAL + accessMethodBySeverGroupId.getHttp_address() + "/live/live"
							+ iliveRoom.getRoomId() + "/5000k/tzwj_video.m3u8";
					iliveRoom.setHlsAddr(pushStreamAddr);
					RoomNoticeUtil.nptice(iliveRoom);
					dealRoomStatic(iliveRoom);
					resultJson.put("status", SUCCESS_STATUS);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "拉流直播成功");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "流服务器交互失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
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
	@RequestMapping(value = "delete.jspx")
	public void iLiveRoomDelete(String terminalType,Integer roomId, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		try {
			// 选择直播服务器组
			ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
			if (iliveRoom == null) {
				jsonObj.put("code", 0);
				jsonObj.put("message", "删除失败,直播间不存在");
				ResponseUtils.renderJson(request, response, jsonObj.toString());
				return;
			}
			if (iliveRoom.getLiveEvent().getLiveStatus().intValue() == ILivePlayStatusConstant.PAUSE_ING.intValue()) {
				jsonObj.put("code", 0);
				jsonObj.put("message", "删除失败,直播间正在直播,请先结束");
				ResponseUtils.renderJson(request, response, jsonObj.toString());
				return;
			}
			iliveRoom.setDeleteStatus(1);
			iLiveRoomMng.update(iliveRoom);
			jsonObj.put("code", 1);
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
		} catch (Exception e) {
			e.printStackTrace();
			jsonObj.put("code", 0);
			jsonObj.put("message", "删除失败");
		}
		ResponseUtils.renderJson(request, response, jsonObj.toString());
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
	
	@RequestMapping("notifyUms.jspx")
	public void notifyUmsApi(HttpServletRequest request,HttpServletResponse response,Integer roomId) {
		JSONObject result = new JSONObject();
		try {
			notifyUms(roomId);
			result.put("code", 1);
			result.put("msg", "通知成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 0);
			result.put("msg", "通知失败");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	
	private void notifyUms(Integer roomId) {
		try {
			ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
			//直播间开了垫片
			if (iliveRoom!=null&&iliveRoom.getOpenSlimModel()!=null&&iliveRoom.getOpenSlimModel()==1) {
				
				ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
				accessMethod.setFtp_address(ConfigUtils.get("backupvideo_ftp_address"));
				accessMethod.setFtp_user(ConfigUtils.get("backupvideo_ftp_username"));
				accessMethod.setFtp_pwd(ConfigUtils.get("backupvideo_ftp_password"));
				accessMethod.setFtpPort(Integer.valueOf(ConfigUtils.get("backupvideo_ftp_port")));
				accessMethod.getMountInfo().setFtp_path(ConfigUtils.get("backupvideo_ftppath"));
				
				UmsApi umsApi = new UmsApi(accessMethod.getOrgLiveHttpUrl()
						, String.valueOf(accessMethod.getUmsport()));
				String mountName = "live" + roomId + "_tzwj_5000k";
				
				
				System.out.println("通知垫片");
				umsApi.closeMount("live", mountName);
				umsApi.setPublishAutoBackup("live", mountName, 
						"/data/vod/backupvideo"+iliveRoom.getRelateSlimFileLocalUrl(), 
						"0", 
						false);
				System.out.println("通知垫片");
			}
		} catch (Exception e) {
			logger.error("ums垫片失败");
		}
	}
	
	/**
	 * 插播垫片
	 * @param roomId
	 * @param isNow
	 * @param delay
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="nowinsert.jspx")
	public void nowinsert(Integer roomId,Boolean isNow,Integer delay,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			notifyUmsInsert(roomId,isNow==null?true:isNow,delay==null?0:delay);
			result.put("code", 1);
			result.put("msg", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 0);
			result.put("msg", "失败");
		}
		ResponseUtils.renderJson(request, response, result.toString());
	}
	
	
	private void notifyUmsInsert(Integer roomId,boolean isNow,Integer second) {
		try {
			ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
			ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			accessMethod.setFtp_address(ConfigUtils.get("backupvideo_ftp_address"));
			accessMethod.setFtp_user(ConfigUtils.get("backupvideo_ftp_username"));
			accessMethod.setFtp_pwd(ConfigUtils.get("backupvideo_ftp_password"));
			accessMethod.setFtpPort(Integer.valueOf(ConfigUtils.get("backupvideo_ftp_port")));
			accessMethod.getMountInfo().setFtp_path(ConfigUtils.get("backupvideo_ftppath"));
			
			UmsApi umsApi = new UmsApi(accessMethod.getOrgLiveHttpUrl()
					, String.valueOf(accessMethod.getUmsport()));
			
			String mountName = "live" + roomId + "_tzwj_5000k";
			//立刻插播垫片
			if (isNow) {
				if (iliveRoom!=null) {
					System.out.println("插播垫片");
					umsApi.setPublishAutoBackup("live", mountName, 
								"/data/vod/backupvideo"+iliveRoom.getRelateSlimFileLocalUrl(),
								iliveRoom.getLiveDelay()==null?"0":String.valueOf(iliveRoom.getLiveDelay()),
								true);
				}
			}else {
				System.out.println("取消插播垫片");
				umsApi.setPublishAutoBackup("live", mountName, 
							"/data/vod/backupvideo"+iliveRoom.getRelateSlimFileLocalUrl(),
							iliveRoom.getLiveDelay()==null?"0":String.valueOf(iliveRoom.getLiveDelay()),
							false);
			}
			//直播间开了垫片
		} catch (Exception e) {
			logger.error("ums垫片失败");
		}
	}
	
	/**
	 * 进入会议
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/getRoomInfo.jspx")
	public void getRoomInfo(Integer roomId,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		try {
		 ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(roomId);
		 ILiveMeetRequest  student =meetRequestMng.getStudentByMeetId(roomId);
		 if(room.getRoomType()==1) {
			 json.put("code", 1);
			 json.put("pwd", student.getPassword());
			 json.put("room", room);
		 }else {
			 json.put("code", 0);
			 json.put("room", room);
		 }
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", -1);
		}
		ResponseUtils.renderJson(request, response, json.toString());
		
	}
	/**
	 * 输入会议邀请码
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/enterRoomByPwd.jspx")
	public void enterRoomByPwd(Integer roomId,String enterPwd,String nickName,String userId,ModelMap model,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		
		HttpSession session =request.getSession();
		String meetUserId=null;
		try {
			nickName=java.net.URLDecoder.decode(nickName,"UTF-8");
			Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(nickName);
			Set<String> sensitiveWord2 = new HashSet<>();
			if (nickName!=null&&!nickName.equals("")) {
				sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(nickName);
			}
			if (sensitiveWord.size()!=0||sensitiveWord2.size()!=0) {
				json.put("code", ERROR_STATUS);
				json.put("message", "包含敏感词");
				if (sensitiveWord.size()!=0) {
					json.put("data", JSONArray.fromObject(sensitiveWord));
				}else if (sensitiveWord2.size()!=0) {
					json.put("data", JSONArray.fromObject(sensitiveWord2));
				}
				ResponseUtils.renderJson(request, response, json.toString());
				return;
			}
			ILiveUserMeetRole meetRole =null;
			
			if(userId==null||"".equals(userId)) {
				if(session.getAttribute("meetUserId")==null||"".equals(session.getAttribute("meetUserId"))) {
					meetUserId=UUID.randomUUID().toString().replace("-", "");
					session.setAttribute("meetUserId", meetUserId);
				}else {
					meetUserId=(String) session.getAttribute("meetUserId");
					meetRole=meetRoleMng.getByUserMeetId(roomId, meetUserId);
				}
			}else {
				meetRole=meetRoleMng.getByUserMeetId(roomId, userId);
			}
		 
		 ILiveMeetRequest  host =meetRequestMng.getHostByMeetId(roomId);
		 ILiveMeetRequest  student =meetRequestMng.getStudentByMeetId(roomId);
		 String tenantId= ConfigUtils.get("tenant_id");
		 String apiUrl= ConfigUtils.get("meet_url_enterMeet");
		 json.put("tenantId", tenantId);
		 json.put("apiUrl", apiUrl);
		 if(meetRole!=null) {
			 if(enterPwd.equals(host.getPassword())) {
				 meetRole.setNickName(nickName);
				 meetRole.setRoleType(1);
				 meetRoleMng.update(meetRole);
				 json.put("code", 1);
				 json.put("msg", "成功");
				 json.put("type", "host");
			 }else if(enterPwd.equals(student.getPassword())) {
				 meetRole.setNickName(nickName);
				 meetRole.setRoleType(3);
				 meetRoleMng.update(meetRole);
				 json.put("code", 1);
				 json.put("msg", "成功");
				 json.put("type", "participator");
			 }else {
				 json.put("code", 0);
				 json.put("msg", "参会码输入错误");
			 }
			 
			 
		 }else {
			 meetRole=new ILiveUserMeetRole();
			 if(enterPwd.equals(host.getPassword())) {
				 meetRole.setNickName(nickName);
				 meetRole.setRoleType(1);
				 meetRole.setRoomId(roomId);
				 if(userId==null||"".equals(userId)) {
					 meetRole.setUserId(meetUserId);
				 }else {
					 meetRole.setUserId(userId);
				 }
				 meetRoleMng.save(meetRole);
				 json.put("type", "host");
				 json.put("code", 1);
				 json.put("msg", "成功");
			 }else if(enterPwd.equals(student.getPassword())) {
				 meetRole.setNickName(nickName);
				 meetRole.setRoleType(3);
				 meetRole.setRoomId(roomId);
				 if(userId==null||"".equals(userId)) {
					 meetRole.setUserId(meetUserId);
				 }else {
					 meetRole.setUserId(userId);
				 }
				 
				 meetRoleMng.save(meetRole);
				 json.put("code", 1);
				 json.put("msg", "成功");
				 json.put("type", "participator");
			 }else {
				 json.put("code", 0);
				 json.put("msg", "参会码输入错误");
			 }
		 }
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", -1);
			 json.put("msg", "进入会议失败");
		}
		if(userId==null||"".equals(userId)) {
			 json.put("userId", meetUserId);
		 }else {
			 json.put("userId", userId);
		 }
		ResponseUtils.renderJson(request, response, json.toString());
		
	}
	/**
	 * 后台进入会议
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/hostintomeet.jspx")
	public void hostintomeet(Integer roomId,String nickName,String userId,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		try {
		 ILiveUserMeetRole meetRole =meetRoleMng.getByUserMeetId(roomId, userId);
		 nickName=java.net.URLDecoder.decode(nickName,"UTF-8");
		 Set<String> sensitiveWord = sentitivewordFilterMng.getSensitiveWord(nickName);
			Set<String> sensitiveWord2 = new HashSet<>();
			if (nickName!=null&&!nickName.equals("")) {
				sensitiveWord2 = sentitivewordFilterMng.getSensitiveWord(nickName);
			}
			if (sensitiveWord.size()!=0||sensitiveWord2.size()!=0) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "包含敏感词");
				if (sensitiveWord.size()!=0) {
					resultJson.put("data", JSONArray.fromObject(sensitiveWord));
				}else if (sensitiveWord2.size()!=0) {
					resultJson.put("data", JSONArray.fromObject(sensitiveWord2));
				}
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
		 if(meetRole!=null) {
				 meetRole.setNickName(nickName);
				 meetRole.setRoleType(2);
				 meetRoleMng.update(meetRole);
				
		 }else {
			     meetRole=new ILiveUserMeetRole();
				 meetRole.setNickName(nickName);
				 meetRole.setRoleType(2);
				 meetRole.setRoomId(roomId);
				 meetRole.setUserId(userId+"");
				 meetRoleMng.save(meetRole);
				
		 }
		    resultJson.put("code", SUCCESS_STATUS);
		    resultJson.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "进入失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 会议邀请码信息
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/meetRequest.jspx")
	public void meetRequest(Integer roomId,String userId,Integer type,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="34027994f458798fa0f87f81ff13aaed";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		if(key.equals(token)) {
			try {
				
				ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(roomId);
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				if(type==1) {
					ILiveMeetRequest  meetRequest =meetRequestMng.getHostByMeetId(roomId);
					
					String msg="您好，["+iLiveEnterPrise.getEnterpriseName()+"]邀请您以主持人身份参与"+f.format(room.getLiveEvent().getLiveStartTime())+"召开的【"+room.getLiveEvent().getLiveTitle()+"】。会议号："+room.getRoomId()+"，主持人密码："+meetRequest.getPassword()+"。参会地址："+ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+room.getRoomId();
					meetRequest.setMessage(msg);
					json.put("code", 1);
					json.put("message", "成功");
					json.put("data", meetRequest);
				}else {
					ILiveMeetRequest  meetRequest =meetRequestMng.getStudentByMeetId(roomId);
					String msg="您好，["+iLiveEnterPrise.getEnterpriseName()+"]邀请您参与"+f.format(room.getLiveEvent().getLiveStartTime())+"召开的【"+room.getLiveEvent().getLiveTitle()+"】。会议号："+room.getRoomId()+"，与会者密码："+meetRequest.getPassword()+"。参会地址："+ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+room.getRoomId();
					meetRequest.setMessage(msg);
					json.put("code", 1);
					json.put("message", "成功");
					json.put("data", meetRequest);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 0);
				json.put("message", "失败");
				json.put("data", new JSONObject());
			}
		}else {
			json.put("code", 0);
			json.put("message", "验证失败");
			json.put("data", new JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	/**
	 * 会议邀请码信息自服务
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/meetRequestIlive.jspx")
	public void meetRequestIlive(Integer roomId,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		
			try {
				
				ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(roomId);
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				
					ILiveMeetRequest  meetRequest =meetRequestMng.getHostByMeetId(roomId);
					
					String msg="您好，["+iLiveEnterPrise.getEnterpriseName()+"]邀请您以主持人身份参与"+f.format(room.getLiveEvent().getLiveStartTime())+"召开的【"+room.getLiveEvent().getLiveTitle()+"】。会议号："+room.getRoomId()+"，主持人密码："+meetRequest.getPassword()+"。参会地址："+ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+room.getRoomId();
					meetRequest.setMessage(msg);
					
					meetRequest.setRoomName(room.getLiveEvent().getLiveTitle());
					json.put("data1", meetRequest);
			
					ILiveMeetRequest  meetRequest2 =meetRequestMng.getStudentByMeetId(roomId);
					String msg1="您好，["+iLiveEnterPrise.getEnterpriseName()+"]邀请您参与"+f.format(room.getLiveEvent().getLiveStartTime())+"召开的【"+room.getLiveEvent().getLiveTitle()+"】。会议号："+room.getRoomId()+"，与会者密码："+meetRequest2.getPassword()+"。参会地址："+ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+room.getRoomId();
					meetRequest2.setMessage(msg1);
					meetRequest2.setRoomName(room.getLiveEvent().getLiveTitle());
					json.put("code", 1);
					json.put("message", "成功");
					json.put("data2", meetRequest2);
				
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 0);
				json.put("message", "失败");
				json.put("data", new JSONObject());
			}
		
		
		ResponseUtils.renderJson(request, response, json.toString());
		
	}
	/**
	 * 会议邀请码信息
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/meetRequestILive.jspx")
	public void meetRequestILive(Integer roomId,Integer type,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		
			try {
				
				ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(roomId);
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
				if(type==1) {
					ILiveMeetRequest  meetRequest =meetRequestMng.getHostByMeetId(roomId);
					json.put("code", 1);
					json.put("message", "成功");
					json.put("startTime", room.getLiveEvent().getLiveStartTime());
					json.put("name", iLiveEnterPrise.getEnterpriseName());
					json.put("data", meetRequest);
				}else {
					ILiveMeetRequest  meetRequest =meetRequestMng.getStudentByMeetId(roomId);
					json.put("code", 1);
					json.put("message", "成功");
					json.put("startTime", room.getLiveEvent().getLiveStartTime());
					json.put("name", iLiveEnterPrise.getEnterpriseName());
					json.put("data", meetRequest);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 0);
				json.put("message", "失败");
				json.put("data", new JSONObject());
			}
		
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	/**
	 * 会议转播信息
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/meetRelay.jspx")
	public void meetRelay(Integer roomId,String userId,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		net.sf.json.JSONObject json2 =new net.sf.json.JSONObject();
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		if(key.equals(token)) {
			try {
				ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(roomId);
				if(room.getMeetPushRoomId()!=null&&!"".equals(room.getMeetPushRoomId())) {
					ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
					ILiveLiveRoom roomRelay =iLiveRoomMng.getIliveRoom(room.getMeetPushRoomId());
					json.put("code", 1);
					json.put("message", "成功");
					json2.put("type", roomRelay.getLiveEvent().getViewAuthorized());
					json2.put("password", roomRelay.getLiveEvent().getViewPassword());
					json2.put("playUrl", ConfigUtils.get("meet_play_url")+"?roomId="+room.getMeetPushRoomId());
					String ext = "png";
					String tempFileName = System.currentTimeMillis() + "." + ext;
					String realPath = request.getSession().getServletContext().getRealPath("/temp");
					File tempFile = new File(realPath + "/" + tempFileName);
					//TwoDimensionCode.encoderQRCode(ConfigUtils.get("meet_play_url")+"?roomId="+room.getMeetPushRoomId(),  realPath + "/" + tempFileName,"jpg");
					String defaultLogoPath = ConfigUtils.get("default_logo_path");
					String logoPath = FileUtils.getRootPath(defaultLogoPath);
					QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/pc/play.html?roomId="+room.getMeetPushRoomId(), realPath + "/" + tempFileName, logoPath);
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
						
						json2.put("playImage", httpUrl);
					json2.put("needLogin", roomRelay.getLiveEvent().getNeedLogin());
					String msg="您好，["+iLiveEnterPrise.getEnterpriseName()+"]邀请您参与"+room.getLiveEvent().getLiveStartTime()+"召开的【"+room.getLiveEvent().getLiveTitle()+"】会议。";
					json2.put("message", msg);
					if(room.getLiveEvent().getViewAuthorized()==1) {
						json2.put("typeName", "公开观看");
					}else if(room.getLiveEvent().getViewAuthorized()==2) {
						json2.put("typeName", "密码观看");
					}else {
						json2.put("typeName", "白名单观看");
					}
					json.put("data", json2);
				}else {
					json.put("code", -1);
					json.put("message", "未设置转播房间");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 0);
				json.put("message", "失败");
			}
		}else {
			json.put("code", 0);
			json.put("message", "验证失败");
			json.put("data", new JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	/**
	 * 会议转播信息app
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/meetRelayApp.jspx")
	public void meetRelay(Integer roomId,String userId,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		net.sf.json.JSONObject json2 =new net.sf.json.JSONObject();
		
			try {
				ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(roomId);
				if(room.getMeetPushRoomId()!=null&&!"".equals(room.getMeetPushRoomId())) {
					ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
					ILiveLiveRoom roomRelay =iLiveRoomMng.getIliveRoom(room.getMeetPushRoomId());
					json.put("code", 1);
					json.put("message", "成功");
					json.put("roomRelayId", room.getMeetPushRoomId());
					json2.put("type", roomRelay.getLiveEvent().getViewAuthorized());
					json2.put("password", roomRelay.getLiveEvent().getViewPassword());
					json2.put("playUrl", ConfigUtils.get("meet_play_url")+"?roomId="+roomId);
					String ext = "png";
					String tempFileName = System.currentTimeMillis() + "." + ext;
					String realPath = request.getSession().getServletContext().getRealPath("/temp");
					File tempFile = new File(realPath + "/" + tempFileName);
					//TwoDimensionCode.encoderQRCode(ConfigUtils.get("meet_play_url")+"?roomId="+room.getMeetPushRoomId(),  realPath + "/" + tempFileName,"jpg");
					String defaultLogoPath = ConfigUtils.get("default_logo_path");
					String logoPath = FileUtils.getRootPath(defaultLogoPath);
					QrCodeUtils.createQRCodeWithFileLogo(ConfigUtils.get("meet_invitation_url")+"/pc/play.html?roomId="+room.getMeetPushRoomId(), realPath + "/" + tempFileName, logoPath);
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
						
						json2.put("playImage", httpUrl);
					json2.put("needLogin", roomRelay.getLiveEvent().getNeedLogin());
					String msg="您好，["+iLiveEnterPrise.getEnterpriseName()+"]邀请您参与"+room.getLiveEvent().getLiveStartTime()+"召开的【"+room.getLiveEvent().getLiveTitle()+"】会议。";
					json2.put("message", msg);
					if(room.getLiveEvent().getViewAuthorized()==1) {
						json2.put("typeName", "公开观看");
					}else if(room.getLiveEvent().getViewAuthorized()==2) {
						json2.put("typeName", "密码观看");
					}else {
						json2.put("typeName", "白名单观看");
					}
					json.put("data", json2);
				}else {
					json.put("code", -1);
					json.put("message", "未设置转播房间");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 0);
				json.put("message", "失败");
			}
		
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	/**
	 * 刷新密码
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/refreshPwd.jspx")
	public void refreshPwd(Integer roomId,String userId,Integer type,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		if(key.equals(token)) {
			
			ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(roomId);
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				
				String pwd=(int)((Math.random()*9+1)*100000)+"";
				if(type!=null&&type==1) {
					ILiveMeetRequest  meetRequest =meetRequestMng.getHostByMeetId(roomId);
					if(!pwd.equals(meetRequest.getPassword())) {
						meetRequest.setPassword(pwd);
					}
					meetRequestMng.update(meetRequest);
					List<ILiveUserMeetRole> role =meetRoleMng.getListByType(roomId,1);
					meetRoleMng.delete(role);
					String msg="您好，["+iLiveEnterPrise.getEnterpriseName()+"]邀请您以主持人身份参与"+f.format(room.getLiveEvent().getLiveStartTime())+"召开的【"+room.getLiveEvent().getLiveTitle()+"】。会议号："+room.getRoomId()+"，主持人密码："+meetRequest.getPassword()+"。参会地址："+ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+room.getRoomId();
					meetRequest.setMessage(msg);
					json.put("code", 1);
					json.put("message", "成功");
					json.put("data", meetRequest);
				}else if(type==2) {
					ILiveMeetRequest  meetRequest =meetRequestMng.getStudentByMeetId(roomId);
					if(!pwd.equals(meetRequest.getPassword())) {
						meetRequest.setPassword(pwd);
					}
					String msg="您好，["+iLiveEnterPrise.getEnterpriseName()+"]邀请您参与"+f.format(room.getLiveEvent().getLiveStartTime())+"召开的【"+room.getLiveEvent().getLiveTitle()+"】。会议号："+room.getRoomId()+"，与会者密码："+meetRequest.getPassword()+"。参会地址："+ConfigUtils.get("meet_invitation_url")+"/index.html?roomId="+room.getRoomId();
					
					meetRequestMng.update(meetRequest);
					meetRequest.setMessage(msg);
					List<ILiveUserMeetRole> role =meetRoleMng.getListByType(roomId,1);
					meetRoleMng.delete(role);
					json.put("code", 1);
					json.put("message", "成功");
					json.put("data", meetRequest);
				}else {
					json.put("code", 0);
					json.put("message", "失败");
					json.put("data", new JSONObject());
					ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
					return;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 0);
				json.put("message", "失败");
				json.put("data", new JSONObject());
			}
		}else {
			json.put("code", 0);
			json.put("message", "验证失败");
			json.put("data", new JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	/**
	 * 刷新密码
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/refreshPwdIlive.jspx")
	public void refreshPwdIlive(Integer roomId,HttpServletResponse response,HttpServletRequest request) {
		
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		
			try {
				
				String pwd=(int)((Math.random()*9+1)*100000)+"";
					ILiveMeetRequest  meetRequest =meetRequestMng.getHostByMeetId(roomId);
					if(!pwd.equals(meetRequest.getPassword())) {
						meetRequest.setPassword(pwd);
					}
					meetRequestMng.update(meetRequest);
					
					ILiveMeetRequest  meetRequest2 =meetRequestMng.getStudentByMeetId(roomId);
					String pwd2=(int)((Math.random()*9+1)*100000)+"";
					if(!pwd.equals(meetRequest2.getPassword())) {
						meetRequest2.setPassword(pwd2);
					}
					meetRequestMng.update(meetRequest2);
					
					json.put("code", 1);
					json.put("message", "成功");
				
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 0);
				json.put("message", "失败");
				json.put("data", new JSONObject());
			}
		
		
		ResponseUtils.renderJson(request, response, json.toString());
		
	}
	/**
	 * 获取默认转推地址
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/rtmpPushAddr.jspx")
	public void rtmpPushAddr(Integer roomId,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		net.sf.json.JSONObject json2 =new net.sf.json.JSONObject();
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		if(key.equals(token)) {
			try {
				ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(roomId);
				json.put("code", 1);
				json.put("message", "成功");
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = accessMethodMng
							.getAccessMethodBySeverGroupId(room.getServerGroupId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				String pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
						+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
				json2.put("rtmpPushAddr", pushStreamAddr);
				json2.put("ViewAddr",ConfigUtils.get("meet_play_url")+"?roomId="+roomId);
				json.put("data", json2);
				
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 0);
				json.put("message", "失败");
				json.put("data", new net.sf.json.JSONObject());
			}
		}else {
			json.put("code", 0);
			json.put("message", "验证失败");
			json.put("data", new net.sf.json.JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	
	/**
	 * 进入房间合法行校验
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/enterRoomCheck.jspx")
	public void enterRoomCheck(Integer roomId,String tenantId,String userId,String clientType,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		net.sf.json.JSONObject json2 =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="34027994f458798fa0f87f81ff13aaed";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)) {
			try {
				 ILiveUserMeetRole meetRole =meetRoleMng.getByUserMeetId(roomId, userId);
				 if(meetRole!=null) {
					 if(clientType.equals("host")) {
						 if(meetRole.getRoleType()==1) {
							json.put("code", 1);
							json.put("message", "成功");
							json2.put("ViewAddr","");
							json2.put("nickName", meetRole.getNickName());
							json.put("data", json2);
						 }else {
							 json.put("code", 413);
								json.put("message", "失败");
								json.put("data", new net.sf.json.JSONObject());
								
						 }
					 }
					 
					 if(clientType.equals("creater")) {
						 if(meetRole.getRoleType()==2) {
							json.put("code", 1);
							json.put("message", "成功");
							json2.put("ViewAddr","");
							json2.put("nickName", meetRole.getNickName());
							json.put("data", json2);
						 }else {
							 json.put("code", 413);
								json.put("message", "失败");
								json.put("data", new net.sf.json.JSONObject());
								
						 }
					 }
					 
					 if(clientType.equals("participator")) {
						 if(meetRole.getRoleType()==3) {
							json.put("code", 1);
							json.put("message", "成功");
							json2.put("ViewAddr","");
							json2.put("nickName", meetRole.getNickName());
							json.put("data", json2);
						 }else {
							 json.put("code", 413);
								json.put("message", "失败");
								json.put("data", new net.sf.json.JSONObject());
								
						 }
					 }
					 
					 
				 }else {
					    json.put("code", 413);
						json.put("message", "验证成功 用户信息获取失败");
						json.put("ViewAddr",""); 
						json.put("data", new net.sf.json.JSONObject());
				 }
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 413);
				json.put("message", "失败");
				json.put("data", new net.sf.json.JSONObject());
			}
		}else {
			json.put("code", 413);
			json.put("message", "验证失败");
			json.put("ViewAddr",""); 
			json.put("data", new net.sf.json.JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	/**
	 * 获取所有直播间信息
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/getAllRoomInfo.jspx")
	public void getAllRoomInfo(Integer roomId,Integer pageNo,Integer pageSize,Integer type,String keyword,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="34027994f458798fa0f87f81ff13aaed";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)) {
			try {
				if(keyword!=null) {
					keyword=java.net.URLDecoder.decode(keyword,"UTF-8");
				}
				
				ILiveLiveRoom room1=iLiveRoomMng.getIliveRoom(roomId);
				Pagination pager = iLiveRoomMng.getNoMeetPager(keyword,room1.getEnterpriseId(), type,  cpn(pageNo), pageSize);
				resultJson.put("code", 1);
				resultJson.put("message", "获取直播间列表成功");
				resultJson.put("pageNo", pager.getPageNo());
				resultJson.put("totalCount", pager.getTotalCount());
				
				List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) pager.getList();
				List<JSONObject> jsonList = new ArrayList<JSONObject>();
				if (list != null) {
					for (ILiveLiveRoom room : list) {
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
				resultJson.put("data", jsonList);
				
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", 413);
				resultJson.put("message", "失败");
				resultJson.put("data", new net.sf.json.JSONObject());
			}
		}else {
			resultJson.put("code", 413);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
		
	}
	/**
	 * 选择转推直播间
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/chosePushRoom.jspx")
	public void chosePushRoom(Integer eventId,Integer roomId,String userId,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="34027994f458798fa0f87f81ff13aaed";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)) {
			try {
				ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(eventId);
				room.setMeetPushRoomId(roomId);
				iLiveRoomMng.update(room);
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = accessMethodMng
							.getAccessMethodBySeverGroupId(room.getServerGroupId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ILiveLiveRoom roomMeet =iLiveRoomMng.getIliveRoom(roomId);
				String pushStreamAddr =null;
				if(roomMeet.getConvertTaskId()!=null&&roomMeet.getConvertTaskId()>0) {
					 pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
							+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "o_tzwj_5000k";
				}else {
					 pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
							+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
				}
				
				
				roomMeet.getLiveEvent().setPushStreamAddr(pushStreamAddr);
				json.put("code", 1);
				json.put("message", "设置转推直播间成功");
				Gson gson = new Gson();
				json.put("data", gson.toJson(roomMeet));
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 413);
				json.put("message", "失败");
				json.put("data", new net.sf.json.JSONObject());
			}
		}else {
			json.put("code", 413);
			json.put("message", "验证失败");
			json.put("ViewAddr",""); 
			json.put("data", new net.sf.json.JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	/**
	 * 选择转推直播间
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/getMeetRoomInfo.jspx")
	public void getMeetRoomInfo(Integer eventId,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject json =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="34027994f458798fa0f87f81ff13aaed";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)) {
			try {
				ILiveLiveRoom room=null;
				if(JedisUtils.exists("meetRoomInfo:"+eventId)) {
					room=SerializeUtil.getObjectMeet(eventId);
				}else {
					 room=iLiveRoomMng.getIliveRoom(eventId);
					 JedisUtils.setObject("meetRoomInfo:"+eventId, SerializeUtil.serialize(room), 0);
				}
				ILiveServerAccessMethod accessMethodBySeverGroupId = null;
				try {
					accessMethodBySeverGroupId = accessMethodMng
							.getAccessMethodBySeverGroupId(room.getServerGroupId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				String pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
						+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + eventId + "_tzwj_5000k";
				room.getLiveEvent().setPushStreamAddr(pushStreamAddr);
				json.put("code", 1);
				json.put("message", "获取房间信息成功");
				json.put("data", room);
				
			} catch (Exception e) {
				e.printStackTrace();
				json.put("code", 413);
				json.put("message", "失败");
				json.put("data", new net.sf.json.JSONObject());
			}
		}else {
			json.put("code", 413);
			json.put("message", "验证失败");
			json.put("ViewAddr",""); 
			json.put("data", new net.sf.json.JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, json.toString());
		
	}
	/**
	 * 会议通知接口
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/meetNotice.jspx")
	public void meetNotice(final Integer eventId,String code,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		final Integer pushRoomId =iLiveRoomMng.getIliveRoom(eventId).getMeetPushRoomId();
		if(key.equals(token)) {
			try {
				if("3001".equals(code)) {//转推开启
					JedisUtils.set("meetPushRoomStartTime:"+eventId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()), 0);
					final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(iLiveRoomMng.getIliveRoom(eventId).getMeetPushRoomId());
					
					Integer playType=1;
					// 根据直播状态
					
						try {
							//notifyUms(roomId);
							System.out.println("通知直播间垫片");
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (playType != null) {
							// 拉流直播
							if (playType.equals(ILivePlayCtrType.POOL_LIVE)) {
								// 先判断直播状态，需要是未开始或者已经结束了的
								iliveRoom.getLiveEvent().setPlayType(ILivePlayCtrType.POOL_LIVE);
							}
							// 列表直播
							else if (playType.equals(ILivePlayCtrType.LIST_LIVE)) {
								iliveRoom.getLiveEvent().setPlayType(ILivePlayCtrType.LIST_LIVE);
							} else {
								
								/**
								 * 获取直播间推流地址
								 */
								ILiveServerAccessMethod accessMethodBySeverGroupId = null;
							
							  try {
								  accessMethodBySeverGroupId = iLiveServerAccessMethodMng
							  .getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
							  
							  try { 
								  //发送通知 
								  notify.start(pushRoomId, accessMethodBySeverGroupId.toString());
							      
							    } catch (Exception e) { 
								  e.printStackTrace(); logger.error("发送通知失败"); 
								  }
							  
							  } catch (Exception e) {
								  e.printStackTrace(); 
							  }
							 
								try {
									ILiveUMSMessageUtil.INSTANCE.startPlay(accessMethodBySeverGroupId, iliveRoom);
									
								} catch (Exception e1) {
									resultJson.put("status", ERROR_STATUS);
									resultJson.put("code", ERROR_STATUS);
									resultJson.put("message", "流服务器交互失败");
									resultJson.put("data", new JSONObject());
									ResponseUtils.renderJson(request, response, resultJson.toString());
									e1.printStackTrace();
									return;
								}
								// 正常向流媒体服务器推流操作
								startPlayAfter(playType, null, request, iliveRoom, accessMethodBySeverGroupId);
								
								resultJson.put("status", SUCCESS_STATUS);
								resultJson.put("code", 1);
								resultJson.put("message", "操作成功");
							}
						} 
					
					
				}else if("3002".equals(code)) {//转推结束
					JedisUtils.del("meetPushRoomStartTime:"+eventId);
					final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(iLiveRoomMng.getIliveRoom(eventId).getMeetPushRoomId());
					Integer liveStatus = iliveRoom.getLiveEvent().getLiveStatus();
					if (liveStatus.equals(ILivePlayStatusConstant.PLAY_OVER)
							|| liveStatus.equals(ILivePlayStatusConstant.UN_START)) {
						System.out.println("直播状态"+liveStatus);
						resultJson.put("code", ERROR_STATUS);
						resultJson.put("message", "直播状态不符合结束标准");
					} else {
						ILiveServerAccessMethod accessMethodBySeverGroupId = null;
						try {
							accessMethodBySeverGroupId = iLiveServerAccessMethodMng
									.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							if (iliveRoom.getLiveEvent().getPlayType().equals(ILivePlayCtrType.POOL_LIVE)) {
								ILiveUMSMessageUtil.INSTANCE.closePullStream(accessMethodBySeverGroupId, iliveRoom);
							} else {
								ILiveUMSMessageUtil.INSTANCE.stopPlay(accessMethodBySeverGroupId, iliveRoom);
							}
						} catch (Exception e1) {
							resultJson.put("status", ERROR_STATUS);
							resultJson.put("code", ERROR_STATUS);
							resultJson.put("message", "流服务器交互失败");
							resultJson.put("data", new JSONObject());
							ResponseUtils.renderJson(request, response, resultJson.toString());
							e1.printStackTrace();
							return;
						}
						ILiveEvent liveEvent = iliveRoom.getLiveEvent();
						liveEvent.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						liveEvent.setLiveEndTime(timestamp);
						liveEvent.setRealEndTime(timestamp);
						iLiveEventMng.updateILiveEvent(liveEvent);
						iliveRoom.setLiveEvent(liveEvent);
						
						RoomNoticeUtil.nptice(iliveRoom);
						iLiveRoomMng.stopTask(liveEvent.getLiveEventId());
						iliveRoom.setIsNowInsert(0);
						iLiveRoomMng.update(iliveRoom);
						resultJson.put("status", SUCCESS_STATUS);
						resultJson.put("code", 1);
						resultJson.put("message", "操作成功");
						//更新企业直播时长
						Integer enterpriseId=iliveRoom.getEnterpriseId();
						ILiveEnterprise iLiveEnterPrise1 = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
						Integer certStatus = null;
						if(iLiveEnterPrise1!=null){
							certStatus = iLiveEnterPrise1.getCertStatus();
						}
						Long useValue=(liveEvent.getRealEndTime().getTime()-liveEvent.getRealStartTime().getTime())/1000;
						
						boolean ret=EnterpriseUtil.openEnterprise(enterpriseId,EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration,useValue.toString(),certStatus);
						
						
						
						  //结束直播通知服务器 
						try { 
							//发送通知 notify.end(pushRoomId); 
							} catch (Exception e) {
							 e.printStackTrace(); 
							 logger.error("发送通知失败"); 
						 }
						 
						
						
						// 正常向流媒体服务器推流操作
						String realIpAddr = IPUtils.getRealIpAddr(request);
						ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
						iliveRoom.getLiveEvent().setEnterprise(iLiveEnterPrise);
						ILiveUserViewStatics.INSTANCE.stopLive(iliveRoom, realIpAddr);
						final Integer serverGroupId = iliveRoom.getServerGroupId();
						final Long userIdLong = iliveRoom.getManagerId();
						final ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(liveEvent.getLiveEventId(),
								userIdLong, ILivePlayStatusConstant.PLAY_ING);
						if (ILivePlayStatusConstant.PAUSE_ING != liveEvent.getLiveStatus()) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
												.getAccessMethodBySeverGroupId(serverGroupId);
										String postUrl = HTTP_PROTOCAL + serverAccessMethod.getHttp_address() + ":"
												+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
										String mountName = "live" + pushRoomId;
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
													ILiveServerAccessMethod vodAccessMethod = iLiveServerAccessMethodMng
															.getAccessMethodBySeverGroupId(vodGroupId);
													liveMediaFile.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
													liveMediaFile.setCreateType(0);
													liveMediaFile.setDuration(length);
													liveMediaFile.setFileType(1);
													liveMediaFile.setOnlineFlag(1);
													//查询是否还有
													// 通过用户拿到企业
													liveMediaFile.setEnterpriseId((long) iliveRoom.getEnterpriseId());
													liveMediaFile.setUserId(userIdLong);
													liveMediaFile.setMediaInfoStatus(0);
													liveMediaFile.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
													liveMediaFile.setLiveRoomId(iliveRoom.getRoomId());
													liveMediaFile.setDelFlag(0);
													liveMediaFile.setAllowComment(1);
													liveMediaFile.setMediaInfoDealState(0);
													//根据userid获取username
													ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userIdLong);
													liveMediaFile.setUserName(iLiveManager.getUserName());
													try {
														Thread.sleep(15000);
													} catch (InterruptedException e) {
														e.printStackTrace();
													}
													//录制
													Long saveIliveMediaFile = iLiveMediaFileMng.saveIliveMediaFile(liveMediaFile);
													DocumentManager doc= documentManagerMng.getById(iliveRoom.getLiveEvent().getDocumentId());
													if(doc!=null) {
														ILiveFileDoc fileDoc =new ILiveFileDoc();
														fileDoc.setFileId(saveIliveMediaFile);
														fileDoc.setDocument(doc);
														fileDocMng.save(fileDoc);
													}
													
													iLiveVideoHisotryMng.saveVideoHistory(pushRoomId, saveIliveMediaFile, userIdLong);
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
														ILiveServerAccessMethod vodAccessMethod = iLiveServerAccessMethodMng
																.getAccessMethodBySeverGroupId(vodGroupId);
														liveMediaFile
																.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
														liveMediaFile.setCreateType(0);
														liveMediaFile.setDuration(length);
														liveMediaFile.setFileType(1);
														liveMediaFile.setOnlineFlag(1);
														// 通过用户拿到企业
														liveMediaFile.setEnterpriseId((long) iliveRoom.getEnterpriseId());
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
														iLiveVideoHisotryMng.saveVideoHistory(pushRoomId, saveIliveMediaFile, userIdLong);
													}
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
								}
							}).start();
						}
					}
				}else if("3003".equals(code)) {//录制开启
					JedisUtils.set("meetRecordStartTime:"+eventId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()), 0);
					
					ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(eventId);
					ILiveEvent event =iliveRoom.getLiveEvent();
					if(event.getRecordStartTime()==null) {
						event.setRecordStartTime(new Timestamp(System.currentTimeMillis()));
						iLiveEventMng.updateILiveEvent(event);
					}
					
						ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(
								iliveRoom.getLiveEvent().getLiveEventId(), iliveRoom.getManagerId(),
								ILivePlayStatusConstant.PLAY_ING);
						// 没有建立过任务
						if (task == null) {
							// 创建个任务
							task = new ILiveRandomRecordTask();
							task.setStartTime(System.currentTimeMillis());
							task.setPhoneNum("天翼直播");
							task.setLiveEventId(iliveRoom.getLiveEvent().getLiveEventId());
							task.setUserId(Long.parseLong(iliveRoom.getManagerId()+""));
							task.setRoomId(eventId);
							task.setPhoneNum("天翼直播");
							task.setLiveStatus(ILivePlayStatusConstant.PLAY_ING);
							iLiveRandomRecordTaskMng.saveTask(task);
							resultJson.put("code", 1);
							resultJson.put("message", "启动收录成功");
							resultJson.put("time", "0");
						} else {
							resultJson.put("code", 0);
							resultJson.put("message", "启动收录失败,已有任务正在收录中");
						}
					
				}else if("3004".equals(code)) {//录制关闭
					JedisUtils.del("meetRecordStartTime:"+eventId);
					final ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(eventId);
					final ILiveEvent liveEvent = iliveRoom.getLiveEvent();
					
					final ILiveRandomRecordTask task = iLiveRandomRecordTaskMng.getTaskByQuery(liveEvent.getLiveEventId(),
							Long.parseLong(iliveRoom.getManagerId()+""), ILivePlayStatusConstant.PLAY_ING);
					if (task != null) {
						task.setEndTime(System.currentTimeMillis());
						task.setLiveStatus(ILivePlayStatusConstant.PLAY_OVER);
						iLiveRandomRecordTaskMng.updateRecordTask(task);
						resultJson.put("code", 1);
						resultJson.put("message", "结束收录成功");
						new Thread(new Runnable() {
							@Override
							public void run() {
								ILiveServerAccessMethod serverAccessMethod = iLiveServerAccessMethodMng
										.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
								String postUrl = HTTP_PROTOCAL + serverAccessMethod.getOrgLiveHttpUrl() + ":"
										+ serverAccessMethod.getLivemsport() + "/livems/servlet/LiveServlet";
								String mountName = "live" + eventId;
								int vodGroupId = Integer.parseInt(ConfigUtils.get("defaultVodServerGroupId"));
								int length = (int) (System.currentTimeMillis() - task.getStartTime()) / 1000;
								if (liveEvent.getLiveStatus().intValue() == ILivePlayStatusConstant.PLAY_OVER) {
									length = (int) (liveEvent.getLiveEndTime().getTime() - task.getStartTime()) / 1000;
								}
								String common = "";
								try {
									common = "?function=RecordLive&mountName=" + mountName + "&startTime="
											+ URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
													.format(new Timestamp(task.getStartTime())), "UTF-8")
											+ "&length=" + length + "&destGroupId=" + vodGroupId;
								} catch (UnsupportedEncodingException e1) {
									e1.printStackTrace();
								}
								postUrl = postUrl + common;
								System.out.println("Connection refused" + postUrl);
								String downloadUrl = PostMan.downloadUrl(postUrl);
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
										ILiveServerAccessMethod vodAccessMethod = iLiveServerAccessMethodMng
												.getAccessMethodBySeverGroupId(vodGroupId);
										liveMediaFile.setServerMountId(vodAccessMethod.getMountInfo().getServer_mount_id());
										liveMediaFile.setCreateType(0);
										liveMediaFile.setDuration(length);
										liveMediaFile.setFileType(1);
										liveMediaFile.setOnlineFlag(1);
										// 通过用户拿到企业
										liveMediaFile.setEnterpriseId(Long.parseLong(iLiveManagerMng.getILiveManager(iliveRoom.getManagerId()).getEnterPrise().getEnterpriseId()+""));
										liveMediaFile.setUserId(Long.parseLong(iliveRoom.getManagerId()+""));
										liveMediaFile.setMediaInfoStatus(0);
										liveMediaFile.setLiveEventId(liveEvent.getLiveEventId());
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
										iLiveVideoHisotryMng.saveVideoHistory(eventId, saveIliveMediaFile,
												Long.parseLong(eventId+""));
									}
								}
							}
						}).start();
					} else {
						resultJson.put("code", 0);
						resultJson.put("message", "结束收录失败,未找到收录任务");
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", 0);
				resultJson.put("message", "失败");
				resultJson.put("data", new net.sf.json.JSONObject());
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
		
	}
	/**
	 * 获取所有直播间信息
	 * @param iliveRoom
	 */
	@RequestMapping(value="live/getMeetRoomStatus.jspx")
	public void getMeetRoomStatus(Integer eventId,Long time,String token,HttpServletResponse response,HttpServletRequest request) {
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)) {
			try {
				
				
				ILiveLiveRoom room=iLiveRoomMng.getIliveRoom(eventId);
				
				resultJson.put("code", 1);
				resultJson.put("message", "获取会议状态信息成功");
				ILiveRandomRecordTask taskByQuery = iLiveRandomRecordTaskMng.getTaskByQuery(
						room.getLiveEvent().getLiveEventId(), room.getManagerId(),
						ILivePlayStatusConstant.PLAY_ING);
				if (taskByQuery == null) {
					resultJson.put("recordStatus", ILivePlayStatusConstant.UN_START);
				} else {
					resultJson.put("recordStatus", ILivePlayStatusConstant.PLAY_ING);
					if(JedisUtils.exists("meetRecordStartTime:"+eventId)) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date d1 = df.parse(JedisUtils.get("meetRecordStartTime:"+eventId));
						long diff=new Date().getTime()-d1.getTime();
						resultJson.put("meetRecordStartTime", diff);
					}
				}
				if(room.getMeetPushRoomId()!=null) {
					ILiveEvent event =iLiveRoomMng.getIliveRoom(room.getMeetPushRoomId()).getLiveEvent();
					if(event.getLiveStatus()==1||event.getLiveStatus()==2) {
						resultJson.put("meetPushRoomStatus", ILivePlayStatusConstant.PLAY_ING);
						if(JedisUtils.exists("meetPushRoomStartTime:"+eventId)) {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date d1 = df.parse(JedisUtils.get("meetPushRoomStartTime:"+eventId));
							long diff=new Date().getTime()-d1.getTime();
							resultJson.put("meetPushRoomStartTime", diff);
						}
						
					}else {
						resultJson.put("meetPushRoomStatus", ILivePlayStatusConstant.UN_START);
					}
					ILiveServerAccessMethod accessMethodBySeverGroupId = null;
					try {
						accessMethodBySeverGroupId = accessMethodMng
								.getAccessMethodBySeverGroupId(room.getServerGroupId());
					} catch (Exception e) {
						e.printStackTrace();
					}
					ILiveLiveRoom roomMeet=iLiveRoomMng.getIliveRoom(room.getMeetPushRoomId());
					String pushStreamAddr =null;
					if(roomMeet.getConvertTaskId()!=null&&roomMeet.getConvertTaskId()>0) {
						 pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
								+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + room.getMeetPushRoomId() + "_tzwj_5000k";
					}else {
						 pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
								+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + room.getMeetPushRoomId() + "_tzwj_5000k";
					}
					
					
					roomMeet.getLiveEvent().setPushStreamAddr(pushStreamAddr);
					resultJson.put("data", roomMeet);
				}else {
					resultJson.put("meetPushRoomStatus", ILivePlayStatusConstant.UN_START);
					resultJson.put("data", new JSONObject());
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", 0);
				resultJson.put("message", "失败");
				resultJson.put("data", new net.sf.json.JSONObject());
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
		
	}
	@RequestMapping(value="live/getQuestionnaireList.jspx")
	public void getQuestionnaireList(Integer roomId,Long time,String token,HttpServletRequest request,HttpServletResponse response){
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)) {
			try {
				UserBean user = ILiveUtils.getUser(request);
				ILiveLiveRoom room = iLiveRoomMng.findById(roomId);
				List<ILiveQuestionnaireActivity> list = null;
				
//				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.valueOf(user.getUserId()));

				
//				//查询子账户是否具有图片查看全部
//				boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_QuestionnaireActivity);
//				
//				if (iLiveManager.getLevel()!=null&&iLiveManager.getLevel()!=0&&!per) {
//					list = iLiveQuestionnaireActivityMng.getH5QuestionnaireListByUserId(Long.valueOf(user.getUserId()));
//				}
				
				if (room!=null&&room.getEnterpriseId()!=null) {
					list = iLiveQuestionnaireActivityMng.getH5QuestionnaireListByEnterpriseId(room.getEnterpriseId());
				}
				
				ILiveQuestionnaireRoom iLiveQuestionnaireRoom = iLiveQuestionnaireRoomMng.getStartByRoomId(roomId);
				if (iLiveQuestionnaireRoom!=null) {
					Long QuestionnaireId = iLiveQuestionnaireRoom.getQuestionnaireId();
					if (list!=null&&list.size()>0) {
						Iterator<ILiveQuestionnaireActivity> iterator = list.iterator();
						while (iterator.hasNext()) {
							ILiveQuestionnaireActivity iLiveQuestionnaireActivity = (ILiveQuestionnaireActivity) iterator.next();
							if (iLiveQuestionnaireActivity.getId().equals(QuestionnaireId)) {
								iLiveQuestionnaireActivity.setIsSwitch(1);
							}
						}
					}
				}
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				JSONArray jsonArray = new JSONArray();
				if(list!=null&&list.size()>0) {
					for(ILiveQuestionnaireActivity activity:list) {
						net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(activity);
						obj.put("startTime", format.format(activity.getStartTime()));
						obj.put("endTime", format.format(activity.getEndTime()));
						jsonArray.add(obj);
					}
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取问卷列表成功");
				resultJson.put("data",jsonArray.toString() );
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取问卷列表失败");
				resultJson.put("data", new JSONObject());
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
	}
	@RequestMapping(value="live/checkMobile.jspx")
	public void checkMobile(Long questionnaireId,Long time,String token,HttpServletRequest request,HttpServletResponse response){
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)){
			ILiveQuestionnaireActivity activity =iLiveQuestionnaireActivityMng.getById(questionnaireId);
			Integer identity=activity.getIdentity();
			if(identity==1){
				resultJson.put("checkMobile", 1);
			}else{
				resultJson.put("checkMobile", 0);
			}
			resultJson.put("code", 1);
			resultJson.put("message", "获取成功");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
	}
	@RequestMapping(value="live/checkQuestionnaireMsg.jspx")
	public void checkQuestionnaireMsg(String mobile,String vpassword,String type,Long time,String token,HttpServletRequest request,HttpServletResponse response){
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)){
			try {
				Cache cacheInfo = CacheManager.getCacheInfo(CacheManager.mobile_token_ + type + "_" + mobile);
				if (cacheInfo == null|| cacheInfo.isExpired()) {
					resultJson.put("code", "1");
					resultJson.put("msg", "验证码已过期");
				}else if(!vpassword.equals(cacheInfo.getValue())){
					resultJson.put("code", "1");
					resultJson.put("msg", "验证码错误");
				}else {
					cacheInfo.setExpired(true);
					resultJson.put("code", "0");
					resultJson.put("msg", "验证成功");
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", "1");
				resultJson.put("msg", "验证出错");
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
	}
	@RequestMapping(value="live/getQuestionnaire.jspx")
	public void getQuestionnaire(Long questionnaireId,Integer roomId,Long userId,String mobile,Long time,String token,HttpServletRequest request,HttpServletResponse response){
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)){
			try {
				ILiveQuestionnaireActivity activity = null;
				//ILiveLiveRoom room = iliveRoomMng.findById(roomId);
//				if (room!=null&&room.getEnterpriseId()!=null) {
//					activity = iLiveQuestionnaireActivityMng.getH5Questionnaire2(room.getEnterpriseId());
//				}
				
				boolean startQuestionnaire = iLiveQuestionnaireRoomMng.isStartQuestionnaire(roomId);
				if (startQuestionnaire) {
					//ILiveQuestionnaireRoom iLiveQuestionnaireRoom = iLiveQuestionnaireRoomMng.getStartByRoomId(roomId);
					activity = iLiveQuestionnaireActivityMng.getById(questionnaireId);
				}
				
				net.sf.json.JSONObject res = net.sf.json.JSONObject.fromObject(activity);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startTime = format.format(activity.getStartTime());
				String endTime = format.format(activity.getEndTime());
				res.put("startTime", startTime);
				res.put("endTime", endTime);
				
				//该用户是否答过卷
				List<ILiveQuestionnairePeople> list = iLiveQuestionnairePeopleMng.getListByUserId(userId,mobile, activity.getId());
				
				if(list.isEmpty()) {
					Date date = new Date();
					if(date.getTime()>activity.getStartTime().getTime()&&date.getTime()<activity.getEndTime().getTime()) {
						//进行中
						resultJson.put("QuestionnaireStatus", 0);
					}else if(date.getTime()<activity.getStartTime().getTime()) {
						//未开始
						resultJson.put("QuestionnaireStatus", 1);
					}else if(date.getTime()>activity.getEndTime().getTime()) {
						//抽奖结束
						resultJson.put("QuestionnaireStatus", 2);
					}
				}else {
					//该用户已参与过问卷
					resultJson.put("QuestionnaireStatus", 3);
				}
				resultJson.put("activity", activity);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取问卷信息成功");
				resultJson.put("data",res.toString() );
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取问卷信息失败");
				resultJson.put("data", new JSONObject());
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
	}
	@RequestMapping(value="live/saveQuestionnaire.jspx")
	public void saveQuestionnaire(Long userId,Long questionnaireId,Long time,String token,String IdCard,String mobile,String strList,HttpServletRequest request,HttpServletResponse response){
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)){
			if(userId==null){
					resultJson.put("code", 401 );
					resultJson.put("message", "用户未登录");
					resultJson.put("data", new JSONObject());
			}
			ILiveManager manager=iLiveManagerMng.selectILiveManagerById(userId);
			if(manager!=null){
				if(mobile==null){
					mobile=manager.getMobile();
				}
			}
			try {
				//答卷记录加一
				ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(questionnaireId);
				activity.setNumber(activity.getNumber()+1);
				iLiveQuestionnaireActivityMng.update(activity);
				//记录当前答卷提交时间
				ILiveQuestionnaireActivityStatistic statistic= iLiveQuestionnaireStatisticMng.getListByUserId(userId, questionnaireId);
				if(statistic==null){
					//添加记录
					ILiveQuestionnaireActivityStatistic jilu=new ILiveQuestionnaireActivityStatistic();
					Long id=iLiveQuestionnaireStatisticMng.maxId();
					jilu.setId(id);
					jilu.setQuestionnaireId(questionnaireId);
					jilu.setUserId(userId);
					jilu.setStartTime(new Timestamp(System.currentTimeMillis()-3600000));
					jilu.setEndTime(new Timestamp(System.currentTimeMillis()));
					iLiveQuestionnaireStatisticMng.save(jilu);
				}else{
					statistic.setEndTime(new Timestamp(System.currentTimeMillis()));
					iLiveQuestionnaireStatisticMng.update(statistic);
				}
				JSONArray jsonArray = JSONArray.fromObject(strList);
				for(int i=0;i<jsonArray.size();i++) {
					net.sf.json.JSONObject obj = jsonArray.getJSONObject(i);
					
						JSONArray arrOption = obj.getJSONArray("list");
						for(int j=0;j<arrOption.size();j++) {
							net.sf.json.JSONObject objOption = arrOption.getJSONObject(j);
							if(obj.getInt("style")!=3){
							String []options=objOption.getString("checkdId").split("_");
							for(String option : options){
								ILiveQuestionnairePeople iLiveQuestionnairePeople=new ILiveQuestionnairePeople();
								iLiveQuestionnairePeople.setQuestionnaireId(questionnaireId);
								iLiveQuestionnairePeople.setQuestionnaireProblemId(obj.getLong("problemId"));
								iLiveQuestionnairePeople.setIdCard(IdCard);
								iLiveQuestionnairePeople.setMobile(mobile);
								iLiveQuestionnairePeople.setUserName(manager.getUserName()==null?mobile:manager.getUserName());
								iLiveQuestionnairePeople.setQuestionnaireOptionId(Long.parseLong(option));
								iLiveQuestionnairePeople.setUserId(userId);
								iLiveQuestionnairePeopleMng.save(iLiveQuestionnairePeople);
							}
						}else{
							ILiveQuestionnairePeople iLiveQuestionnairePeople=new ILiveQuestionnairePeople();
							iLiveQuestionnairePeople.setQuestionnaireId(questionnaireId);
							iLiveQuestionnairePeople.setQuestionnaireProblemId(obj.getLong("problemId"));
							iLiveQuestionnairePeople.setIdCard(IdCard);
							iLiveQuestionnairePeople.setMobile(mobile);
							iLiveQuestionnairePeople.setUserName(manager.getUserName()==null?mobile:manager.getUserName());
							String answer=objOption.getString("answer");
							answer=new String(answer.trim().getBytes("ISO-8859-1"), "UTF-8"); 
							iLiveQuestionnairePeople.setAnswer(answer);
							iLiveQuestionnairePeople.setUserId(userId);
							iLiveQuestionnairePeopleMng.save(iLiveQuestionnairePeople);
						}
					}
					
					
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "答卷成功");
				resultJson.put("data", new JSONObject());
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "答卷失败");
				resultJson.put("data", new JSONObject());
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
	}
	@RequestMapping(value="live/getVoteList.jspx")
	public void getVoteList(Integer roomId,Long time,String token,HttpServletRequest request,HttpServletResponse response){
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)){
			try {
				UserBean user = ILiveUtils.getUser(request);
				ILiveLiveRoom room = iLiveRoomMng.findById(roomId);
				List<ILiveVoteActivity> list = null;
				
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.valueOf(user.getUserId()));

				
				//查询子账户是否具有图片查看全部
//				boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_voteActivity);
//				
//				if (iLiveManager.getLevel()!=null&&iLiveManager.getLevel()!=0&&!per) {
//					list = iLiveVoteActivityMng.getH5VoteListByUserId(Long.valueOf(user.getUserId()));
//				}
				
				if (room!=null&&room.getEnterpriseId()!=null) {
					list = iLiveVoteActivityMng.getH5VoteListByEnterpriseId(room.getEnterpriseId());
				}
				
				ILiveVoteRoom iLiveVoteRoom = iLiveVoteRoomMng.getStartByRoomId(roomId);
				if (iLiveVoteRoom!=null) {
					Long voteId = iLiveVoteRoom.getVoteId();
					if (list!=null&&list.size()>0) {
						Iterator<ILiveVoteActivity> iterator = list.iterator();
						while (iterator.hasNext()) {
							ILiveVoteActivity iLiveVoteActivity = (ILiveVoteActivity) iterator.next();
							if (iLiveVoteActivity.getId().equals(voteId)) {
								iLiveVoteActivity.setIsSwitch(1);
							}
						}
					}
				}
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				JSONArray jsonArray = new JSONArray();
				if(list!=null&&list.size()>0) {
					for(ILiveVoteActivity activity:list) {
						net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(activity);
						obj.put("startTime", format.format(activity.getStartTime()));
						obj.put("endTime", format.format(activity.getEndTime()));
						jsonArray.add(obj);
					}
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取投票列表成功");
				resultJson.put("data",jsonArray.toString() );
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取投票列表失败");
				resultJson.put("data", new JSONObject());
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
	}
	@RequestMapping(value="live/getVote.jspx")
	public void getVote(Integer roomId,Long userId,Long time,String token,HttpServletRequest request,HttpServletResponse response){
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)){
			try {
				ILiveVoteActivity activity = null;
				//ILiveLiveRoom room = iliveRoomMng.findById(roomId);
//				if (room!=null&&room.getEnterpriseId()!=null) {
//					activity = iLiveVoteActivityMng.getH5Vote2(room.getEnterpriseId());
//				}
				
				boolean startVote = iLiveVoteRoomMng.isStartVote(roomId);
				if (startVote) {
					ILiveVoteRoom iLiveVoteRoom = iLiveVoteRoomMng.getStartByRoomId(roomId);
					activity = iLiveVoteActivityMng.getById(iLiveVoteRoom.getVoteId());
				}
				
				net.sf.json.JSONObject res = net.sf.json.JSONObject.fromObject(activity);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startTime = format.format(activity.getStartTime());
				String endTime = format.format(activity.getEndTime());
				res.put("startTime", startTime);
				res.put("endTime", endTime);
				
				//该用户是否投过票
				List<ILiveVotePeople> list = iLiveVotePeopleMng.getListByUserId(userId, activity.getId());
				
				if(list.isEmpty()) {
					Date date = new Date();
					if(date.getTime()>activity.getStartTime().getTime()&&date.getTime()<activity.getEndTime().getTime()) {
						//进行中
						resultJson.put("voteStatus", 0);
					}else if(date.getTime()<activity.getStartTime().getTime()) {
						//未开始
						resultJson.put("voteStatus", 1);
					}else if(date.getTime()>activity.getEndTime().getTime()) {
						//抽奖结束
						resultJson.put("voteStatus", 2);
					}
				}else {
					//该用户已参与过投票
					resultJson.put("voteStatus", 3);
				}
				
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取投票信息成功");
				resultJson.put("data",res.toString() );
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取投票信息失败");
				resultJson.put("data", new JSONObject());
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
	}
	@RequestMapping(value="live/addVote.jspx")
	public void addVote(Long userId,String problemAnswers,Long voteId,Long time,String token,HttpServletRequest request,HttpServletResponse response){
		net.sf.json.JSONObject resultJson =new net.sf.json.JSONObject();
		if(time==null) {
			time=Long.parseLong("1514779932");
			
		}
		if(token==null) {
			token="08e6f71e411123bdab251ba4d46ebb19";
		}
		String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
		
		if(key.equals(token)){
			try {
				iLiveVoteActivityMng.vote(voteId,userId,problemAnswers);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "投票成功");
				resultJson.put("data", new JSONObject());
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "投票失败");
				resultJson.put("data", new JSONObject());
			}
		}else {
			resultJson.put("code", 0);
			resultJson.put("message", "验证失败");
			resultJson.put("ViewAddr",""); 
			resultJson.put("data", new net.sf.json.JSONObject());
		}
		ResponseUtils.renderJsonNoJsonp(request, response, resultJson.toString());
	}
}
