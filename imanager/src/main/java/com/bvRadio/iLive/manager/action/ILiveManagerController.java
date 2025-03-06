package com.bvRadio.iLive.manager.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.bvRadio.iLive.iLive.entity.*;
import com.bvRadio.iLive.iLive.manager.*;
import com.jwzt.live4g.mgr.UmsMountInfo;
import com.jwzt.ums.api.UmsApi;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.manager.entity.ILiveRemEnterprise;
import com.bvRadio.iLive.manager.manager.FankongMng;
import com.bvRadio.iLive.manager.manager.ILiveRemEnterpriseMng;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value="managercontrol")
public class ILiveManagerController {
	private static final String HTTP_PROTOCAL = "http://";
	
	@Autowired
	private FankongMng fankongMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	
	@Autowired
	private ILiveMediaFileMng ilivemediaMng;
	
	@Autowired
	private ILiveViewSetRecordMng iLiveViewSetRecordMng;
	
	@Autowired
	private ILiveEnterpriseMng enterpriseMng;

	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	@Autowired
	private ILiveEventMng eventMng;
	
	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;
	@Autowired
	private ILiveRemEnterpriseMng remEnterpriseMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	//直播管控
	@RequestMapping(value="managercontrol.do")
	public String managercontrol(@RequestParam(value="pageNo",required=false,defaultValue="1") int pageNo,@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			Model model,Integer formroomtype,Integer formexamine,Integer formlivestate,Integer formroomstate,String formqueryName,HttpServletRequest request) {
		UserBean userBean = ILiveUtils.getUser(request);
		Integer enterpriseId = userBean.getEnterpriseId();
		Pagination page = iLiveLiveRoomMng.getPager(formroomtype,formexamine,formlivestate,formroomstate,formqueryName,pageNo, pageSize);
		@SuppressWarnings("unchecked")
		List<ILiveLiveRoom> list = (List<ILiveLiveRoom>) page.getList();
		for(ILiveLiveRoom room:list) {
			if(room.getEnterpriseId()!=null) {
				ILiveEnterprise iLiveEnterPrise = enterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
				if(iLiveEnterPrise!=null && iLiveEnterPrise.getEnterpriseName()!=null) {
					room.setEnterpriseName(iLiveEnterPrise.getEnterpriseName());
				}
			}
		}
		//获取直播间数量
		int livenum = iLiveLiveRoomMng.queryNumbyState(null);
		int livewaitnum = iLiveLiveRoomMng.queryNumbyState(4);
		int roomlivenum = iLiveLiveRoomMng.queryNumbyState(1);
		int liveadvannum = iLiveLiveRoomMng.queryNumbyState(0);
		model.addAttribute("livenum", livenum);
		model.addAttribute("livewaitnum", livewaitnum);
		model.addAttribute("roomlivenum", roomlivenum);
		model.addAttribute("liveadvannum", liveadvannum);
		model.addAttribute("formroomtype", formroomtype);
		model.addAttribute("formexamine", formexamine);
		model.addAttribute("formlivestate", formlivestate);
		model.addAttribute("formroomstate", formroomstate);
		model.addAttribute("queryName", formqueryName);
		model.addAttribute("page",page);
		model.addAttribute("topActive","1");
		model.addAttribute("leftActive","2");
		return "managercontrol/ilivecontrol";
	}
	
	//直播间详情
	@RequestMapping(value="roomdetial.do")
	public String roomdetial(Integer roomId,Model model) {
		ILiveLiveRoom iliveroom = iLiveLiveRoomMng.findById(roomId);
		if(iliveroom.getEnterpriseId()!=null) {
			ILiveEnterprise iLiveEnterPrise = enterpriseMng.getILiveEnterPrise(iliveroom.getEnterpriseId());
			if(iLiveEnterPrise!=null && iLiveEnterPrise.getEnterpriseName()!=null) {
				iliveroom.setEnterpriseName(iLiveEnterPrise.getEnterpriseName());
			}
		}
		/**
		 * 获取直播间推流地址
		 */
		ILiveServerAccessMethod accessMethodBySeverGroupId = null;
		try {
			accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(iliveroom.getServerGroupId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
		
		Pagination record = iLiveViewSetRecordMng.getPage(roomId,1,5);
		
		//获取直播间所有场次
		Pagination liveeventPage = eventMng.getPageByRoomId(roomId,10,1);
		@SuppressWarnings("unchecked")
		List<ILiveEvent> listevent = (List<ILiveEvent>) liveeventPage.getList();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for(ILiveEvent event:listevent) {
			if (event.getRealStartTime()!=null &&event.getRealEndTime()!=null) {
				long start = event.getRealStartTime().getTime();
				long end = event.getRealEndTime().getTime();
				long longtime = (end -start)/1000;
				event.setHours((int)(longtime/60));
				event.setMinutes((int)(longtime%60));
				event.setStartDate(format.format(event.getLiveStartTime()));
			}
		}
		Pagination fkpage = fankongMng.getPage(roomId, null, null, 1, 10);
		int pageNum = record.getTotalPage();
		int pageNumEvent = liveeventPage.getTotalPage();
		model.addAttribute("recordlist", record.getList());
		model.addAttribute("listevent", listevent);
		model.addAttribute("listeventsize", liveeventPage.getTotalCount());
		model.addAttribute("liveAddr", liveAddr);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageNumEvent", pageNumEvent);
		model.addAttribute("iliveroom", iliveroom);
		model.addAttribute("fkpage", fkpage.getList());
		model.addAttribute("fkpageNum", fkpage.getTotalPage());
		model.addAttribute("topActive","1");
		model.addAttribute("leftActive","2");
		return "managercontrol/detailsView";
	}
	
	//直播场次分页
	@RequestMapping(value="getPageEvent")
	public void getEventPage(Integer roomId,Integer pageNo,HttpServletResponse response) {
		//获取直播间所有场次
		Pagination liveeventPage = eventMng.getPageByRoomId(roomId,10,pageNo);
		List<ILiveEvent> listevent = (List<ILiveEvent>) liveeventPage.getList();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for(ILiveEvent event:listevent) {
			long start = event.getLiveStartTime().getTime();
			long end = event.getLiveEndTime().getTime();
			long longtime = (end -start)/1000;
			event.setHours((int)(longtime/60));
			event.setHours((int)(longtime%60));
			event.setStartDate(format.format(event.getLiveStartTime()));
		}
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(listevent);
		ResponseUtils.renderJson(response, array.toString());
	}
	
	//观看授权分页
	@RequestMapping(value="getRecordPage.do")
	public void getRecordPage(Integer roomId,Integer pageNo,HttpServletResponse response) {
		Pagination record = iLiveViewSetRecordMng.getPage(roomId,pageNo,5);
		List<ILiveViewSetRecord> list = (List<ILiveViewSetRecord>) record.getList();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		for(ILiveViewSetRecord recorddd:list) {
			recorddd.setDate(format.format(recorddd.getUpdateTime()).toString());
			System.out.println(recorddd.toString());
		}
		System.out.println(list);
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(list);
		ResponseUtils.renderJson(response, array.toString());
	}
	
	//直播间禁用修改
	@ResponseBody
	@RequestMapping(value="disabledf.do",method=RequestMethod.POST)
	public String disabled(Integer roomId,Integer disabled) {
		iLiveLiveRoomMng.updateDisabledById(roomId,disabled);
		return String.valueOf(disabled);
	}
	
	//直播间审核通过
	@ResponseBody
	@RequestMapping(value="/{id}/examine.do",method=RequestMethod.POST)
	public String examine(@PathVariable("id")Integer roomId) {
		iLiveLiveRoomMng.updateLiveStatusById(roomId,0);
		return "修改成功";
	}
	
	//多个直播间审核通过
	@ResponseBody
	@RequestMapping(value="/{state}/iliveroomexampass.do",method=RequestMethod.POST)
	//state  0为审核通过  6为审核不通过
	public String iliveroomexampass(String ids,@PathVariable("state") Integer state) {
		System.out.println(ids);
		JSONArray jsonArray = JSONArray.fromObject(ids);
		Integer[] idArr = new Integer[jsonArray.size()];
		for(int i=0;i<jsonArray.size();i++) {
			int roomId = jsonArray.getJSONObject(i).getInt("id");
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			if(iliveRoom.getLiveEvent().getLiveStatus() == 4) {
				iLiveLiveRoomMng.updateLiveStatusById(roomId,state);
			}
		}
		//iLiveLiveRoomMng.updateLiveStatusById(roomId,5);
		return "修改成功";
	}
	
	//资源管控列表页
	@RequestMapping(value="resourcecontrol.do")
	public String resourcecontrol(Model model,Integer pageSize,Integer pageNo,Integer formmediatype,Integer formmediastate,
			Timestamp formstartTime,Timestamp formendTime,String formsearch) {
		Pagination page = ilivemediaMng.getPager(formmediatype,formmediastate,formstartTime,formendTime,formsearch,1, pageNo, 10);
		
		
		//List<ILiveMediaFile> listByType = ilivemediaMng.getListByType(1, 33);
		//model.addAttribute("allnum", listByType.size());
		int allNum = ilivemediaMng.getNum(1,null);
		model.addAttribute("allNum", allNum);
		model.addAttribute("formmediatype", formmediatype);
		model.addAttribute("formmediastate", formmediastate);
		model.addAttribute("formsearch", formsearch);
		model.addAttribute("page", page);
		model.addAttribute("topActive","1");
		model.addAttribute("leftActive","3");
		return "managercontrol/medisresource";
	}
	//资源管控详情页
	@RequestMapping(value="mediadetail.do")
	public String mediadetail(Model model,Long fileId) {
		ILiveMediaFile file = ilivemediaMng.selectILiveMediaFileByFileId(fileId);
		Integer liveRoomId = file.getLiveRoomId();
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.findById(liveRoomId);
		
		Integer serverMountId = file.getServerMountId();
		ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
		MountInfo mountInfo = serverAccess.getMountInfo();
		String allPath = HTTP_PROTOCAL + serverAccess.getHttp_address() + ":" + serverAccess.getUmsport()
				+ mountInfo.getBase_path() + file.getFilePath();
		file.setFilePath(allPath);
		
		ILiveServerAccessMethod accessMethodBySeverGroupId = iLiveServerAccessMethodMng
				.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
		String mediavedioAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/review.html?roomId="
				+ liveRoomId + "&fileId=" + fileId;
		
		model.addAttribute("mediavedioAddr", mediavedioAddr);
		model.addAttribute("file", file);
		model.addAttribute("topActive","1");
		model.addAttribute("leftActive","3");
		return "managercontrol/mediadetail";
	}
	
	//媒体文件状态修改
	@ResponseBody
	@RequestMapping(value="/{id}/{state}/setfilestate.do",method=RequestMethod.POST)
	public String setfilestate(@PathVariable("id") Long fileId,@PathVariable("state") Integer onlineFlag) {
		ILiveMediaFile selectILiveMediaFileByFileId = ilivemediaMng.selectILiveMediaFileByFileId(fileId);
		selectILiveMediaFileByFileId.setOnlineFlag(onlineFlag);
		ilivemediaMng.updateMediaFileById(selectILiveMediaFileByFileId);
		return "修改成功";
	}
	
	//多个媒体文件审核
	@ResponseBody
	@RequestMapping(value="/{state}/fileexampass.do",method=RequestMethod.POST)
	//state  0为审核通过  1为审核不通过
	public String fileexampass(String ids,@PathVariable("state") Integer state) {
		System.out.println(ids);
		JSONArray jsonArray = JSONArray.fromObject(ids);
		Integer[] idArr = new Integer[jsonArray.size()];
		for(int i=0;i<jsonArray.size();i++) {
			int roomId = jsonArray.getJSONObject(i).getInt("id");
		}
		
		return "修改成功";
	}
	
	private final static String RTMP_PROTOACAL = "rtmp://";
	
	org.slf4j.Logger logger = LoggerFactory.getLogger(ILiveManagerController.class);
	
	//矩阵监控
	@RequestMapping(value="/monitor.do")
	public String monitor(Model model,Integer formroomtype,
			Integer formexamine,Integer formlivestate,
			Integer formroomstate,String formqueryName) {
		//List<ILiveLiveRoom> iliveroom = iLiveLiveRoomMng.getRoomListByliveStatus(1);
		//System.out.println(iliveroom.size());
		String pushStreamAddr = null;
		UmsApi api = new UmsApi(ConfigUtils.get("ums_api"),ConfigUtils.get("ums_port"));
//		UmsApi api = new UmsApi("source.live01.zb.tv189.com","1935");
		List<UmsMountInfo> umsMountName = api.getUmsMountName();
		List<Object> roomIds = new ArrayList<>();
		for (UmsMountInfo item : umsMountName) {
			if(StringUtils.isNotBlank(item.getMountName())){
				String mountName = item.getMountName();
				Pattern pattern = Pattern.compile("\\d+");
				Matcher matcher = pattern.matcher(mountName.substring(0, mountName.indexOf("_")));
				if(matcher.find()){
					roomIds.add(Integer.parseInt(matcher.group()));
				}
			}
		}
		if(roomIds.size() > 0){
			Pagination page = iLiveLiveRoomMng.getPagerNew(roomIds,1,1, 9);
			@SuppressWarnings("unchecked")
			List<ILiveLiveRoom> iliveroom = (List<ILiveLiveRoom>) page.getList();
			System.out.println(page.getTotalCount());
			model.addAttribute("pageNo", 1);
			model.addAttribute("pushStreamAddr", pushStreamAddr);
			model.addAttribute("maxPage", page.getTotalPage());
			model.addAttribute("list", iliveroom);
			return "managercontrol/monitor";
		}else{
			@SuppressWarnings("unchecked")
			List<ILiveLiveRoom> iliveroom = Collections.emptyList();
			model.addAttribute("pageNo", 1);
			model.addAttribute("pushStreamAddr", pushStreamAddr);
			model.addAttribute("maxPage",1);
			model.addAttribute("list", iliveroom);
			return "managercontrol/monitor";
		}

	}

	@RequestMapping(value="/getPage.do")
	public void mString(Model model,Integer pageNo,HttpServletResponse response) {
		UmsApi api = new UmsApi(ConfigUtils.get("ums_api"),ConfigUtils.get("ums_port"));
		List<UmsMountInfo> umsMountName = api.getUmsMountName();
		List<Object> roomIds = new ArrayList<>();
		for (UmsMountInfo item : umsMountName) {
			if(StringUtils.isNotBlank(item.getMountName())){
				String mountName = item.getMountName();
				Pattern pattern = Pattern.compile("\\d+");
				Matcher matcher = pattern.matcher(mountName.substring(0, mountName.indexOf("_")));
				if(matcher.find()){
					roomIds.add(Integer.parseInt(matcher.group()));
				}
			}
		}
//		Pagination page = iLiveLiveRoomMng.getPager(1,pageNo, 9);
		Pagination page = iLiveLiveRoomMng.getPagerNew(roomIds,1,pageNo, 9);
		@SuppressWarnings("unchecked")
		List<ILiveLiveRoom> iliveroom = (List<ILiveLiveRoom>) page.getList();
		Iterator<ILiveLiveRoom> iterator = iliveroom.iterator();
		net.sf.json.JSONArray array = new JSONArray();
		//for(ILiveLiveRoom room:iliveroom){
		while (iterator.hasNext()) {
			ILiveLiveRoom room = iterator.next();
			ILiveEnterprise iLiveEnterPrise = null;
			if(room.getEnterpriseId()==null) {
				iLiveEnterPrise = enterpriseMng.getILiveEnterPrise(10);
			}else {
				iLiveEnterPrise = enterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
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
	
	
	/**
	 * 忽略操作
	 * @param str
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="ignore")
	public void ignore(String str,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			fankongMng.ignoreMany(str);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 2);
			result.put("message", "操作失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 查询
	 * @param type1
	 * @param type2
	 * @param startTime
	 * @param endTime
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="searchfk")
	public void searchfk(Integer roomId,Integer type1,Integer type2,Timestamp startTime,Timestamp endTime,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			System.out.println(type1+"  "+type2+"  "+startTime+"   "+endTime);
			Pagination page = fankongMng.getPage(roomId, startTime, endTime, type1, type2, 1, 10);
			result.put("code", "1");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "2");
			result.put("message", "查询失败");
		}
		ResponseUtils.renderHtml(response, result.toString());
	}
	
	
	@RequestMapping(value="getpattern")
	public String pattern(Integer roomId,Model model) {
		ILiveLiveRoom room = iLiveLiveRoomMng.getIliveRoom(roomId);
		if(room != null && room.getManagerId() != null){
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(room.getManagerId());
			if(iLiveManager != null){
				room.setPhone(iLiveManager.getMobile());
			}
		}
		ILiveEnterprise iLiveEnterPrise = enterpriseMng.getILiveEnterPrise(room.getEnterpriseId());
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
		return "managercontrol/patternVideo";
	}
	
	@RequestMapping(value="jinbo")
	public void jinbo(Integer roomId,Integer mark,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveLiveRoom room = iLiveLiveRoomMng.getIliveRoom(roomId);
			room.setDisabled(mark);
			iLiveLiveRoomMng.update(room);
			try {
				if (mark == 1 && room.getLiveEvent()!=null && room.getLiveEvent().getLiveStatus()==1) {
					String api = ConfigUtils.get("live_api");
					String url = api+"/liveevent/live/stop.jspx";
					Map<String, String> map = new HashMap<>();
					map.put("roomId", roomId+"");
					map.put("playType", "1");
					String sendPost = HttpUtils.sendPost(url,map ,"UTF-8");
					System.out.println(sendPost);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put("code", 0);
			result.put("msg", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 1);
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderHtml(response, result.toString());
	}
	/**
	 * 
	 * @param roomId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="recommendEp")
	public String recommendEp(String name,Model model) {
		List<ILiveRemEnterprise> list =remEnterpriseMng.getPage(name);
		model.addAttribute("list", list);
		model.addAttribute("name", name);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "7");
		return "managercontrol/recommendEp";
	}
	
	@RequestMapping(value="remDelete")
	public void remDelete(Long id,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			
			remEnterpriseMng.del(id);
			result.put("code", 0);
			result.put("msg", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 1);
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderHtml(response, result.toString());
	}
	@RequestMapping(value="setTopNum")
	public void setTopNum(Long id,Integer state,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveRemEnterprise ob1=	remEnterpriseMng.get(id);
			ob1.setTopNum(state);
			remEnterpriseMng.update(ob1);
			result.put("code", 0);
			result.put("msg", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 1);
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderHtml(response, result.toString());
	}
	@RequestMapping(value="moveEnterprise")
	public void moveEnterprise(Long id,Integer state,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			//上移
			if(state==1) {
			Integer index=null;
			List<ILiveRemEnterprise> list =remEnterpriseMng.getPage(null);
			for (int i = 0; i < list.size(); i++) {
				if(id.equals(list.get(i).getId())) {
					index=i-1;
					break;
				}
			}
			if(index>=0) {
				ILiveRemEnterprise ob1=	remEnterpriseMng.get(id);
				ILiveRemEnterprise ob2=	list.get(index);
				Long temp=null;
				temp=ob1.getOrderNum();
				ob1.setOrderNum(ob2.getOrderNum());
				ob2.setOrderNum(temp);
				remEnterpriseMng.update(ob1);
				remEnterpriseMng.update(ob2);
			}
			
		    }else {
		    	Integer index=null;
				List<ILiveRemEnterprise> list =remEnterpriseMng.getPage(null);
				for (int i = 0; i < list.size(); i++) {
					if(id.equals(list.get(i).getId())) {
						index=i+1;
						break;
					}
				}
				if(index<=list.size()) {
					ILiveRemEnterprise ob1=	remEnterpriseMng.get(id);
					ILiveRemEnterprise ob2=	list.get(index);
					Long temp=null;
					temp=ob1.getOrderNum();
					ob1.setOrderNum(ob2.getOrderNum());
					ob2.setOrderNum(temp);
					remEnterpriseMng.update(ob1);
					remEnterpriseMng.update(ob2);
				}
		    	
		    }
			result.put("code", 0);
			result.put("msg", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 1);
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderHtml(response, result.toString());
	}
	
	@RequestMapping(value="selectRemmendEnterprise")
	public void selectRemmendEnterprise(HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			List<ILiveRemEnterprise> list =remEnterpriseMng.getPage(null);
			result.put("code", 0);
			result.put("msg", "修改成功");
			result.put("data", JSONArray.fromObject(list));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 1);
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderHtml(response, result.toString());
	}
	
	/**
	 * 添加推荐企业
	 * 
	 */
	@RequestMapping(value="addrementerprise")
	public String addrementerprise(Integer[] newsIds,HttpServletResponse response) {
		
		for (Integer id : newsIds) {
			if(remEnterpriseMng.getByEnterpriseId(id)) {
				ILiveEnterprise iLiveEnterPrise = enterpriseMng.getILiveEnterPrise(id);
				ILiveRemEnterprise remEnterprise=new ILiveRemEnterprise();
				remEnterprise.setEnterprise(iLiveEnterPrise);
				remEnterprise.setTopNum(0);
				remEnterpriseMng.saveEnterprise(remEnterprise);
			}
			
		}
		
		return "redirect:recommendEp.do";
	}
	
	@RequestMapping(value="dataList")
	public void dataList(HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			List<ILiveRemEnterprise> list = remEnterpriseMng.getprivacyPage(null);
			result.put("list", list);
			result.put("code", 1);
			
		} catch (Exception e) {
			result.put("code", 0);
		}
		
		ResponseUtils.renderJson(response, result.toString());
	}
	/**
	 * 企业是否开启关注
	 * @param response
	 */
	@RequestMapping(value="isEnterpriseRem")
	public void isEnterpriseRem(HttpServletRequest request,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			String string=	IOUtils.toString(request.getInputStream(), "utf-8");
			net.sf.json.JSONObject json_result = net.sf.json.JSONObject.fromObject(string);
			Integer enterpriseId=Integer.parseInt(json_result.get("enterpriseId").toString());
			ILiveRemEnterprise remEnterprise =	remEnterpriseMng.getIsEnterpriseRem(enterpriseId);
			if(remEnterprise!=null) {
				if(remEnterprise.getTopNum()==0) {
					result.put("data", 0);
				}else {
					result.put("data", 1);
				}
				
			}else {
				result.put("data", 1);
			}
			
			result.put("code", 1);
			
		} catch (Exception e) {
			result.put("code", 0);
		}
		
		ResponseUtils.renderJson(response, result.toString());
	}

	@RequestMapping(value="platformMessageManager")
	public String platformMessageManager(Model model) {
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "8");
		return "platform/appMessage";
	}

	@RequestMapping(value="createNewPlatformMessage")
	public void createNewPlatformMessage(HttpServletRequest request,HttpServletResponse response){
		Map<String, String[]>  readOnlyMap = request.getParameterMap();
		Map<String,String> map = new HashMap<>();
		for (String key : readOnlyMap.keySet()) {
			map.put(key,readOnlyMap.get(key)[0]);
		}
		String params = JSON.toJSONString(readOnlyMap);
		String createMessageUrl = ConfigUtils.get("live_api") + "/messageBox/createNewMessageTemporary.jspx";
		try{
			String result = HttpUtils.sendPost(createMessageUrl, URLEncoder.encode(params,"UTF-8"), "UTF-8");
			ResponseUtils.renderJson(response,result);
		}catch (IOException io){
			logger.debug("新建消息通知异常，消息参数："+params);
			io.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("code",400);
		json.put("message","保存失败");
		ResponseUtils.renderJson(response,json.toString());
	}

}
