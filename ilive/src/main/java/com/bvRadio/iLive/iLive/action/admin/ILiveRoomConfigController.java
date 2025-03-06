package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.InputSource;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveAPIGateWayRouter;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.ILiveViewSetRecord;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.entity.vo.ILiveShareInfoVo;
import com.bvRadio.iLive.iLive.manager.ILiveAPIGateWayRouterMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFCodeMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomShareConfigMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewSetRecordMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewWhiteBillMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.util.ConvertThread;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
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
	private ILiveEnterpriseMemberMng iLiveEnterpriseMemberMng;

	@Autowired
	private ILiveViewWhiteBillMng iLiveViewWhiteBillMng;

	@Autowired
	private ILiveViewAuthBillMng iLiveViewAuthBillMng;

	@Autowired
	private ILiveViewSetRecordMng iLiveViewSetRecordMng; // 观看授权记录

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILiveRoomShareConfigMng iLiveRoomShareConfigMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	
	@Autowired
	private WorkLogMng workLogMng;
	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;
	@Autowired
	private ILiveFCodeMng iLiveFCodeMng;
	@Autowired
	private ILiveAPIGateWayRouterMng iLiveAPIGateWayRouterMng;
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	private final static String RTMP_PROTOACAL = "rtmp://";

	@RequestMapping(value = "/enterprise/whitelist.do")
	public String iLiveWhiteList() {

		return null;
	}

	/**
	 * logo设置页面跳转
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "logoset.do")
	public String iLiveConfigLogoSet(Model model, Integer roomId, HttpServletRequest request) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		Integer serverGroupId = this.selectServerGroup();
		//查询是否有播放画面台标自定义权限
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		Integer certStatus = iLiveEnterprise.getCertStatus();
		boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_PlayTableMark,certStatus);
		if(b){
			model.addAttribute("PlayTableMark", 0);
		}else{
			model.addAttribute("PlayTableMark", 1);
		}
		model.addAttribute("leftActive", "2_2");
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("topActive", "1");
		return "liveconfig/LOGOsetConver";
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
	public String iLiveConfigLogoSetEdit(String logoUrl, Integer roomId, Integer openLogoSwitch, Integer logoPosition,HttpServletRequest request) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		UserBean user = ILiveUtils.getUser(request);
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
		workLogMng.save(new WorkLog(WorkLog.MODEL_EDITLOGO, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITLOGO_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
		return "";
	}
	/**
	 * logo设置页面222
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "logosetEditConver.do")
	public String iLiveConfigLogoSetEdit2(Integer state,String logoUrl, Integer roomId, Integer openLogoSwitch, Integer logoPosition,Integer width,Integer height,HttpServletRequest request) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		UserBean user = ILiveUtils.getUser(request);
		final String converTaskLogo=ConfigUtils.get("conver_task_logo");
		iLiveLiveRoom.setConverLogoFtp(logoUrl);
		if(iLiveLiveRoom.getConvertTaskId()!=null&&iLiveLiveRoom.getConvertTaskId()>0) {
			try {
				String data1=HttpUtils.sendStr(converTaskLogo+"/api/task/"+iLiveLiveRoom.getConvertTaskId()+"/stop", "PUT", "");
				
	            	ConvertThread thread =new ConvertThread(roomId, iLiveLiveRoomMng, iLiveLiveRoom.getConvertTaskId());
	            	thread.run();
		           
		           
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
		if (openLogoSwitch == 1) {
			iLiveLiveRoom.setOpenLogoSwitch(1);
			iLiveLiveRoom.setLogoPosition(logoPosition);
			
			String left="0";
			String top="0";
			if(width==null) {
				width=124;
			}
			if(height==null) {
				height=33;
			}
			switch (logoPosition) {
			case 1:
				iLiveLiveRoom.setLogoClass("leftTop");
				
				break;
			case 2:
				iLiveLiveRoom.setLogoClass("rightTop");
				left=(1280-width)+"";
				break;
			case 3:
				iLiveLiveRoom.setLogoClass("leftBottom");
				top=(720-height)+"";
				break;
			case 4:
				iLiveLiveRoom.setLogoClass("rightBottom");
				left=(1280-width)+"";
				top=(720-height)+"";
				break;
			}
			try {
			String imgUrl="";
			if(logoUrl==null||"".equals(logoUrl)) {
				imgUrl="/mnt/data/remote/default_mount_storage/logo/defaultLogo.png";
			}else {
				iLiveLiveRoom.setConverLogoFtp(logoUrl);
				ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
				
				logoUrl=logoUrl.substring(logoUrl.indexOf("//")+2);
				logoUrl=logoUrl.substring(logoUrl.indexOf("/"));
				String xml = createJDOM(uploadServer.getConvertTaskAddr()+logoUrl);
				
				String data=HttpUtils.sendStr(converTaskLogo+"/api/material/downloadFile", "POST", xml);
				//创建一个新的字符串
		        StringReader read = new StringReader(data);
		        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		        InputSource source = new InputSource(read);
		        //创建一个新的SAXBuilder
		        SAXBuilder sb = new SAXBuilder();
		      //通过输入源构造一个Document
	            Document doc = sb.build(source);
				 //取的根元素
	            Element root = doc.getRootElement();
	            
	            if(root.getChild("code").getText().equals("0")) {
	            	imgUrl=root.getChild("body").getChild("localUri").getText();
	            }
			}
			
			
			
			
				
	            iLiveLiveRoom.setConvertLogo(imgUrl);
	            iLiveLiveRoom.setLogoWidth(Integer.parseInt(top));
	            iLiveLiveRoom.setLogoHight(Integer.parseInt(left));
	          //创建一个SAXBuilder对象
	              SAXBuilder saxBuilder = new SAXBuilder();            
	            //读取prop.xml资源
	              Document Doc = saxBuilder.build(request.getSession().getServletContext().getRealPath("/temp")+"/xml/4.xml");
	            //获取根元素(prop)
	            Element Root = Doc.getRootElement();
	            
	            Element name=  Root.getChild("name");
	            name.setText(liveEvent.getLiveTitle());
	            Element uri=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("uri");
	           
	            uri.setText(imgUrl);
	            Element rtmpuri=  Root.getChild("inputs").getChild("network").getChild("uri");
	            ILiveServerAccessMethod accessMethodBySeverGroupId = null;
	    		try {
	    			accessMethodBySeverGroupId = accessMethodMng
	    					.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    		String pushStreamAddr = RTMP_PROTOACAL + accessMethodBySeverGroupId.getOrgLiveHttpUrl() + ":"
	    				+ accessMethodBySeverGroupId.getUmsport() + "/live/live" + roomId + "_tzwj_5000k";
	    		
	            String[] addr= pushStreamAddr.split("_");
	            rtmpuri.setText(addr[0]+"o_"+addr[1]+"_"+addr[2]);
	            Element Top=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("top");
	            Top.setText(top);
	            Element Left=  Root.getChild("streams").getChild("stream").getChild("h264").getChild("preprocessor").getChild("logo").getChild("left");
	            Left.setText(left);
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
				if(state==0) {
					String str = byteRsp.toString("utf-8");
		            String data1=HttpUtils.sendStr(converTaskLogo+"/api/task", "POST", str);
		            Integer taskId=getTaskId(data1);
		            String res  =HttpUtils.sendStr(converTaskLogo+"/api/task/"+taskId+"/start", "PUT", "");
		            if(taskId!=null) {
		            	iLiveLiveRoom.setConvertTaskId(taskId);
		            }
				}else {
					
				}
				
	            
			} catch (JDOMException | IOException e) {
				
				e.printStackTrace();
			}
			
			
			
		} else {
			iLiveLiveRoom.setOpenLogoSwitch(0);
		}
		
			iLiveLiveRoomMng.update(iLiveLiveRoom);
		
		
		//workLogMng.save(new WorkLog(WorkLog.MODEL_EDITLOGO, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITLOGO_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
		return "";
	}
	public static String createJDOM(String imgUrl){
		try {
			// 创建一个根节点
			Element rootElement = new Element("material");
			
			Document doc = new Document(rootElement);
			
			// 在根节点下创建第一个子节点
			Element rootOneElement = new Element("uri");
			rootOneElement.setText(imgUrl);
			// 在根节点下创建第二个子节点
			Element rootTwoElement = new Element("extension");
			rootTwoElement.setText("png");
			doc.getRootElement().addContent(rootOneElement);
			doc.getRootElement().addContent(rootTwoElement);
			//格式化输出xml文件字符串
			Format format = Format.getCompactFormat();
			format.setEncoding("UTF-8");
			//这行保证输出后的xml的格式
			format.setIndent(" ");
			// 创建xml输出流操作类
			XMLOutputter xmlout = new XMLOutputter(format);
			ByteArrayOutputStream byteRsp = new ByteArrayOutputStream();
			xmlout.output(doc, byteRsp);
			String str = byteRsp.toString("utf-8");
	        return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
	public void iLiveConfigCountdownSetEdit(Model model, Integer roomId, Timestamp time, String countdownTitle,
			Integer openCountdownSwitch,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		UserBean user = ILiveUtils.getUser(request);
		
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if (openCountdownSwitch == 1) {
			liveEvent.setLiveStartTime(time);
			liveEvent.setCountdownTitle(countdownTitle);
			liveEvent.setOpenCountdownSwitch(1);
		} else {
			liveEvent.setOpenCountdownSwitch(0);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		
		workLogMng.save(new WorkLog(WorkLog.MODEL_COUNTDOWN, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_COUNTDOWN_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "修改成功");
		ResponseUtils.renderJson(request, response, resultJson.toString());
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
		
		List<ILiveAPIGateWayRouter> routerList = iLiveAPIGateWayRouterMng.getRouterList();
		String apiRouterUrl = "";
		if (routerList != null && !routerList.isEmpty()) {
			ILiveAPIGateWayRouter iLiveAPIGateWayRouter = routerList.get(0);
			apiRouterUrl = iLiveAPIGateWayRouter.getRouterUrl();
		}
		model.addAttribute("apiRouterUrl", apiRouterUrl);
		
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
	public void iLiveConfigDatadisplayBeautifyEdit(Integer roomId, Integer baseNum, Integer multiple,
			Integer openDataBeautifySwitch , HttpServletRequest request,HttpServletResponse response) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if (openDataBeautifySwitch == 1) {
			liveEvent.setBaseNum(baseNum);
			liveEvent.setMultiple(multiple);
			Long currentNum = 0L;
			if (liveEvent.getCurrentNum() == null) {
				currentNum = 0L;
				liveEvent.setCurrentNum(0L);
			} else {
				currentNum = liveEvent.getCurrentNum();
				currentNum = currentNum == null ? 0 : currentNum;
			}
			baseNum = baseNum == null ? 0 : baseNum;
			Long showNum = (long) baseNum + currentNum * baseNum;
			liveEvent.setShowNum(showNum);
			liveEvent.setOpenDataBeautifySwitch(1);
		} else {
			liveEvent.setOpenDataBeautifySwitch(0);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		ApplicationCache.getOnlineNumber().remove(liveEvent.getRoomId());
		JSONObject resultJson = new JSONObject();
		resultJson.put("code", 1);
		resultJson.put("message", "设置成功");
		ResponseUtils.renderJson(request, response, resultJson.toString());
		
		//return "";
	}

	/**
	 * 推广分享
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "spreadshare.do")
	public String iLiveConfigSpreadShare(Model model, Integer roomId, HttpServletRequest request) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		UserBean user = ILiveUtils.getUser(request);
		if (iLiveLiveRoom != null) {
			ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
			ILiveServerAccessMethod accessMethodBySever = iLiveServerAccessMethodMng
					.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
			String liveUrl = accessMethodBySever.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
			model.addAttribute("liveUrl", liveUrl);
			List<ILiveRoomShareConfig> shareConfigList = iLiveRoomShareConfigMng.getShareConfig(roomId);
			if (shareConfigList == null || shareConfigList.isEmpty()) {
				ILiveRoomShareConfig shareConfigBean = new ILiveRoomShareConfig();
				shareConfigBean.setLiveDesc(iLiveLiveRoom.getLiveEvent().getLiveTitle());
				shareConfigBean.setLiveTitle(iLiveLiveRoom.getLiveEvent().getLiveTitle());
				shareConfigBean.setRoomId(roomId);
				shareConfigBean.setShareUrl(liveUrl);
				Integer enterpriseId = user.getEnterpriseId();
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
				String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
				if (enterpriseImg == null) {
					shareConfigBean.setLiveImg(ConfigUtils.get("shareOtherConfig"));
				} else {
					shareConfigBean.setLiveImg(enterpriseImg);
				}
				shareConfigList = iLiveRoomShareConfigMng.addIliveRoomShareConfig(shareConfigBean);
			}
			Integer openStatus = 0;
			if (!shareConfigList.isEmpty()) {
				for (ILiveRoomShareConfig share : shareConfigList) {
					openStatus = share.getOpenStatus();
					if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_SINGLE)) {
						// 朋友
						model.addAttribute("friendSingle", share);
					} else if (share.getShareType().equals(ILiveRoomShareConfig.FRIEND_CIRCLE)) {
						// 朋友圈
						model.addAttribute("friendCircle", share);
					}
				}
			}
			workLogMng.save(new WorkLog(WorkLog.MODEL_SHARE, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_SHARE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			model.addAttribute("openStatus", openStatus);
			model.addAttribute("leftActive", "2_8");
			model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
			model.addAttribute("liveEvent", liveEvent);
			model.addAttribute("topActive", "1");
		}
		return "liveconfig/spread_share";
	}

	@RequestMapping(value = "/share/updatestatus")
	public void updateShareState(Integer roomId, Integer openStatus, HttpServletResponse response) {
		List<ILiveRoomShareConfig> shareConfigs = iLiveRoomShareConfigMng.getShareConfig(roomId);
		Map<String, Object> map = new HashMap<>();
		try {
			if (shareConfigs != null && !shareConfigs.isEmpty()) {
				for (ILiveRoomShareConfig share : shareConfigs) {
					share.setOpenStatus(openStatus);
					iLiveRoomShareConfigMng.updateShare(share);
				}
			}
			map.put("code", 1);
		} catch (Exception e) {
			map.put("code", 0);
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, new JSONObject(map).toString());
	}

	@RequestMapping(value = "infochange.do")
	public void iLiveConfigSpreadShareUpdate(ILiveShareInfoVo iLiveShareInfoVo,Integer roomId, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		JedisUtils.del("shareinfo:"+roomId);
		try {
			iLiveRoomShareConfigMng.updateConfigShare(iLiveShareInfoVo);
			resultJson.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", -1);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 观看限制
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "livelim.do")
	public String iLiveLim(Integer roomId, Model model, HttpServletRequest request,Integer creatType,String searchCode) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		UserBean user = ILiveUtils.getUser(request);
		if (user.getEnterpriseId().equals(ConfigUtils.get("defaultEnterpriseId"))) {
			model.addAttribute("enterprisewhiteList", new ArrayList<>());
			model.addAttribute("viewWhiteList", new ArrayList<>());
		} else {
			Pagination page = iLiveEnterpriseMemberMng.getPage(null, 1, 1000, user.getEnterpriseId());
			List<ILiveEnterpriseWhiteBill> enterprisewhiteList = (List<ILiveEnterpriseWhiteBill>) page.getList();
			List<ILiveViewWhiteBill> viewWhiteList = iLiveViewWhiteBillMng
					.getAllViewWhiteBill(liveEvent.getLiveEventId());
			model.addAttribute("enterprisewhiteList", enterprisewhiteList);
			model.addAttribute("viewWhiteList", viewWhiteList);
		}
		//添加F码使用情况统计 
		//未使用
		List<ILiveFCode> unuselist = iLiveFCodeMng.listByParams(null, null, 1, roomId, null, null, null);
		model.addAttribute("unUseFCodeNum", unuselist.size()>0?unuselist.size():0);
		//已绑定
		List<ILiveFCode> useinglist = iLiveFCodeMng.listByParams(null, null, 2, roomId, null, null, null);
		model.addAttribute("useingFCodeNum", useinglist.size()>0?useinglist.size():0);
		//已失效
		List<ILiveFCode> outuselist = iLiveFCodeMng.listByParams(null, null, 3, roomId, null, null, null);
		model.addAttribute("outUseFCodeNum", outuselist.size()>0?outuselist.size():0);
		//全部
		List<ILiveFCode> totallist = iLiveFCodeMng.listByParams(null, null, null, roomId, null, null, null);
		model.addAttribute("totalFCodeNum", totallist.size()>0?totallist.size():0);
		//f码数据列表
		Integer type=1;
		if(creatType!=null){
			if(creatType==5){
				creatType=null;
			}
		}
		if("".equals(searchCode)&&searchCode!=null){
			searchCode=null;
		}
		Pagination pagination = iLiveFCodeMng.getBeanBySearchCode(roomId, null,type,creatType,searchCode,1, 20);
		//Pagination pagination = iLiveFCodeMng.getBeanByCode(roomId, null,1,1, 20);
		//添加是否开启开发者权限
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(user.getEnterpriseId());
		model.addAttribute("isDeveloper", iLiveEnterprise.getIsDeveloper()==null?0:iLiveEnterprise.getIsDeveloper());
		model.addAttribute("pager", pagination);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("leftActive", "2_3");
		model.addAttribute("user", user);
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
	public void freeLogin(Integer roomId,Integer freeneedLogin, Model model, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
		try {
			ILiveEvent liveEvent = liveRoom.getLiveEvent();
			if(freeneedLogin==null) {
				if(liveEvent.getNeedLogin()!=null&&liveEvent.getViewAuthorized()==1) {
					freeneedLogin=liveEvent.getNeedLogin();
				}else {
				freeneedLogin=0;
				}
			}
			liveEvent.setViewAuthorized(1);
			if(freeneedLogin==1) {
				liveEvent.setNeedLogin(1);;
			}else {
				liveEvent.setNeedLogin(0);
			}
			iLiveEventMng.updateILiveEvent(liveEvent);
			iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());

			// 添加观看授权操作记录
			ILiveViewSetRecord record = new ILiveViewSetRecord();
			UserBean user = ILiveUtils.getUser(request);
			record.setViewAuthorized(1);
			record.setUserName(user.getUsername());
			record.setRoomId(roomId);
			iLiveViewSetRecordMng.saveRecord(record);
			
			workLogMng.save(new WorkLog(WorkLog.MODEL_EDITLOGO, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITLOGO_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			
			
			model.addAttribute("iLiveLiveRoom", liveRoom);
			model.addAttribute("roomId", roomId);
			model.addAttribute("liveEvent", liveEvent);
			model.addAttribute("leftActive", "2_3");
			model.addAttribute("topActive", "1");
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
//		return "redirect:/admin/config/livelim.do";
	}
	/**
	 * 免费观看
	 * 
	 * @param roomId
	 * @param is_login
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "thirdLogin.do")
	public void thirdLogin(Integer roomId, Model model, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
		try {
			ILiveEvent liveEvent = liveRoom.getLiveEvent();
			liveEvent.setViewAuthorized(7);
			liveEvent.setNeedLogin(1);
			iLiveEventMng.updateILiveEvent(liveEvent);
			iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());
			// 添加观看授权操作记录
			ILiveViewSetRecord record = new ILiveViewSetRecord();
			UserBean user = ILiveUtils.getUser(request);
			record.setViewAuthorized(7);
			record.setUserName(user.getUsername());
			record.setRoomId(roomId);
			iLiveViewSetRecordMng.saveRecord(record);
			workLogMng.save(new WorkLog(WorkLog.MODEL_EDITLOGO, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITLOGO_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			
			
			model.addAttribute("iLiveLiveRoom", liveRoom);
			model.addAttribute("roomId", roomId);
			model.addAttribute("liveEvent", liveEvent);
			model.addAttribute("leftActive", "2_3");
			model.addAttribute("topActive", "1");
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
//		return "redirect:/admin/config/livelim.do";
	}
	/**
	 * 登录观看
	 * 
	 * @param roomId
	 * @param is_login
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "Login.do")
	public String Login(Integer roomId, Model model, HttpServletRequest request) {
		ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = liveRoom.getLiveEvent();
		liveEvent.setViewAuthorized(5);
		iLiveEventMng.updateILiveEvent(liveEvent);
		iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());

		// 添加观看授权操作记录
		ILiveViewSetRecord record = new ILiveViewSetRecord();
		UserBean user = ILiveUtils.getUser(request);
		record.setViewAuthorized(5);
		record.setUserName(user.getUsername());
		record.setRoomId(roomId);
		iLiveViewSetRecordMng.saveRecord(record);
		workLogMng.save(new WorkLog(WorkLog.MODEL_EDITPOWER, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITPOWER_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
		model.addAttribute("iLiveLiveRoom", liveRoom);
		model.addAttribute("roomId", roomId);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("leftActive", "2_3");
		model.addAttribute("topActive", "1");
		return "redirect:/admin/config/livelim.do";
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
	public void passwordLogin(Integer roomId,Integer passneedLogin, String viewPassword, String welcomeMsg, Model model,
			HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		if (viewPassword.length() == 4) {
			try {
				ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
				ILiveEvent liveEvent = liveRoom.getLiveEvent();
				if(passneedLogin==null) {
					if(liveEvent.getNeedLogin()!=null&&liveEvent.getViewAuthorized()==2) {
						passneedLogin=liveEvent.getNeedLogin();
					}else {
						passneedLogin=0;
					}
				}
				liveEvent.setViewAuthorized(2);
				if(passneedLogin==2||passneedLogin==1) {
					liveEvent.setNeedLogin(1);
				}else {
					liveEvent.setNeedLogin(0);
				}
				liveEvent.setViewPassword(viewPassword);
				liveEvent.setWelcomeMsg(welcomeMsg);
				iLiveEventMng.updateILiveEvent(liveEvent);
				iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());
				// 添加观看授权操作记录
				ILiveViewSetRecord record = new ILiveViewSetRecord();
				UserBean user = ILiveUtils.getUser(request);
				record.setViewAuthorized(2);
				record.setViewPassword(viewPassword);
				record.setWelcome(welcomeMsg);
				record.setUserName(user.getUsername());
				record.setRoomId(roomId);
				iLiveViewSetRecordMng.saveRecord(record);
				workLogMng.save(new WorkLog(WorkLog.MODEL_EDITPOWER, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITPOWER_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
				model.addAttribute("iLiveLiveRoom", liveRoom);
				model.addAttribute("roomId", roomId);
				model.addAttribute("liveEvent", liveEvent);
				model.addAttribute("leftActive", "2_3");
				model.addAttribute("topActive", "1");
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "操作成功");
			} catch (Exception e) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "操作失败");
			}
		} else {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "密码格式错误");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
//		return "redirect:/admin/config/livelim.do";
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
	public void pagView(Model model, Integer roomId, Double viewMoney, String welcomeMsg, Integer openFCodeSwitch,
			Integer is_openF, HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent liveEvent = liveRoom.getLiveEvent();
			liveEvent.setViewAuthorized(3);
			liveEvent.setViewMoney(viewMoney);
			liveEvent.setWelcomeMsg(welcomeMsg);
			if (is_openF != null) {
				openFCodeSwitch = 1;
				liveEvent.setOpenFCodeSwitch(openFCodeSwitch);
			}else {
				openFCodeSwitch = 0;
				liveEvent.setOpenFCodeSwitch(openFCodeSwitch);
			}
			iLiveEventMng.updateILiveEvent(liveEvent);
			iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());

			// 添加观看授权操作记录
			ILiveViewSetRecord record = new ILiveViewSetRecord();
			UserBean user = ILiveUtils.getUser(request);
			record.setViewAuthorized(3);
			record.setPayAmount(viewMoney);
			record.setWelcome(welcomeMsg);
			record.setUserName(user.getUsername());
			record.setRoomId(roomId);
			iLiveViewSetRecordMng.saveRecord(record);
			
			workLogMng.save(new WorkLog(WorkLog.MODEL_EDITPOWER, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITPOWER_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			
			model.addAttribute("iLiveLiveRoom", liveRoom);
			model.addAttribute("roomId", roomId);
			model.addAttribute("liveEvent", liveEvent);
			model.addAttribute("leftActive", "2_3");
			model.addAttribute("topActive", "1");
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
//		return "redirect:/admin/config/livelim.do";
	}


	/**
	 * 白名单观看
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "whiteList.do")
	public void whiteList(Model model, Integer roomId, HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent liveEvent = liveRoom.getLiveEvent();
			liveEvent.setViewAuthorized(4);

			iLiveEventMng.updateILiveEvent(liveEvent);
			iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());
			// 添加观看授权操作记录
			ILiveViewSetRecord record = new ILiveViewSetRecord();
			UserBean user = ILiveUtils.getUser(request);
			record.setViewAuthorized(4);
			record.setUserName(user.getUsername());
			record.setRoomId(roomId);
			iLiveViewSetRecordMng.saveRecord(record);
			
			workLogMng.save(new WorkLog(WorkLog.MODEL_EDITPOWER, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITPOWER_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			
			model.addAttribute("iLiveLiveRoom", liveRoom);
			model.addAttribute("roomId", roomId);
			model.addAttribute("liveEvent", liveEvent);
			model.addAttribute("topActive", "1");
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
		}
		
		ResponseUtils.renderJson(request, response, resultJson.toString());
//		return "redirect:/admin/config/livelim.do";
	}
	/**
	 * 观看码观看
	 * 
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "FCodeList.do")
	public void FCodeList(Model model, Integer roomId,String fcodeMsg,String outLinkUrl, HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
			JedisUtils.delObject("roomInfo:"+roomId);
		}
		try {
			ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent liveEvent = liveRoom.getLiveEvent();
			liveEvent.setViewAuthorized(6);
			liveEvent.setNeedLogin(1);
			liveEvent.setWelcomeMsg(fcodeMsg);
			liveEvent.setOutLinkUrl(outLinkUrl);
			iLiveEventMng.updateILiveEvent(liveEvent);
			iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());
			// 添加观看授权操作记录
			ILiveViewSetRecord record = new ILiveViewSetRecord();
			UserBean user = ILiveUtils.getUser(request);
			record.setViewAuthorized(6);
			record.setUserName(user.getUsername());
			record.setRoomId(roomId);
			iLiveViewSetRecordMng.saveRecord(record);
			
			workLogMng.save(new WorkLog(WorkLog.MODEL_EDITPOWER, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_EDITPOWER_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			
			model.addAttribute("iLiveLiveRoom", liveRoom);
			model.addAttribute("roomId", roomId);
			model.addAttribute("liveEvent", liveEvent);
			model.addAttribute("topActive", "1");
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "操作成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作失败");
		}
		
		ResponseUtils.renderJson(request, response, resultJson.toString());
//		return "redirect:/admin/config/livelim.do";
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
