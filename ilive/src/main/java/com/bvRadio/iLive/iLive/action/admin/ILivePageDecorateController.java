package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value="pagedecorate")
public class ILivePageDecorateController {
	
	@Autowired
	private ILiveEventMng iLiveEventMng;// 场次管理

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILivePageDecorateMng pageDecorateMng;// 页面装修
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	
	@Autowired
	private WorkLogMng workLogMng;
	
	//移动端页面显示
	@RequestMapping(value="mobilepage.do")
	public String mobile_page(Model model,Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		if(iLiveLiveRoom.getRelatedVideo()==null){
			iLiveLiveRoom.setRelatedVideo(1);
		}
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		String pageRichContent = "";
		String hkmc = "";
		List<PageDecorate> list = pageDecorateMng.getList(liveEvent.getLiveEventId());
		if(list!=null && !list.isEmpty()) {
			
			Iterator<PageDecorate> iterator = list.iterator();
			while (iterator.hasNext()) {
				PageDecorate page = iterator.next();
				if(page.getMenuType()==4) {
					//iLiveLiveRoom.setRelatedVideo(page.getRelatedVideo()==null?1:page.getRelatedVideo());
					hkmc=page.getMenuName1()==null?"相关视频":page.getMenuName1();
					pageRichContent = page.getRichContent();
				}
				if(page.getMenuType() == 3||page.getMenuType() == 5) {
					iterator.remove();
				}
			}
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
		String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
		model.addAttribute("pageRichContent",pageRichContent);
		model.addAttribute("list",list);
		model.addAttribute("hkmc",hkmc);
		model.addAttribute("liveAddr",liveAddr);
		model.addAttribute("leftActive", "3_1");
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("topActive", "1");
		return "pagedecorate/mobile_page";
	}
	
	//PC端页面显示
	@RequestMapping(value="pcpage.do")
	public String pc_page(Model model,Integer roomId,HttpServletRequest request) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		List<PageDecorate> list = pageDecorateMng.getList(liveEvent.getLiveEventId());
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
		Integer certStatus=iLiveEnterPrise.getCertStatus();
		//查询是否有直播自服务平台LOGO自定义权限
		boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_LiveSelfServiceLogo, certStatus);
		if(b){
			model.addAttribute("LiveSelfServiceLogo", 0);
		}else{
			model.addAttribute("LiveSelfServiceLogo", 1);
		}
		model.addAttribute("list",list);
		model.addAttribute("leftActive", "3_2");
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("topActive", "1");
		return "pagedecorate/pc_decorate";
	}
	
	//直播引导页
	@RequestMapping(value="liveleader.do")
	public String liveleader(Model model,Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		List<PageDecorate> list = pageDecorateMng.getList(liveEvent.getLiveEventId());
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("list",list);
		model.addAttribute("leftActive", "2_10");
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("serverGroupId",serverGroupId);
		model.addAttribute("topActive", "1");
		return "pagedecorate/live_leader";
	}

	//修改直播引导页
	@ResponseBody
	@RequestMapping(value="liveleaderedit.do")
	public void liveleaderedit(Integer roomId,Integer openGuideSwitch,String guideAddr,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if(openGuideSwitch == 1) {
			liveEvent.setOpenGuideSwitch(1);
			liveEvent.setGuideAddr(guideAddr);
		}else {
			liveEvent.setOpenGuideSwitch(0);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		UserBean user = ILiveUtils.getUser(request);
		workLogMng.save(new WorkLog(WorkLog.MODEL_GUIDE, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_GUIDE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "修改成功");
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	//删除操作
	@ResponseBody
	@RequestMapping(value="mobilepagedelete.do")
	public String mobile_page_delete(Long liveEventId,Integer menuType,Integer menuOrder,Long id,Integer noind,String jsonobj,HttpServletRequest request) {
		pageDecorateMng.removeById(id);
		if(noind == 0) {
			JSONArray jsonArray = JSONArray.fromObject(jsonobj);
			List<Map<String, Integer>> list = new LinkedList<>();
			if(jsonArray.size()>0) {
				net.sf.json.JSONObject jsonObject;
				for(int i =0;i<jsonArray.size();i++) {
					Map<String, Integer> map = new HashMap<>();
					jsonObject = jsonArray.getJSONObject(i);
					map.put("id", jsonObject.getInt("id"));
					map.put("order", jsonObject.getInt("order"));
					list.add(map);
				}
			}
			pageDecorateMng.setIndex(list);
			UserBean user = ILiveUtils.getUser(request);
			workLogMng.save(new WorkLog(WorkLog.MODEL_PAGEDECORATE, id+"","", WorkLog.MODEL_PAGEDECORATE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
		}
		return "";
	}
	
	//添加操作
	@ResponseBody
	@RequestMapping(value="addmobilepage.do")
	public String add_mobile_page(PageDecorate page,String objjson,HttpServletRequest request) {
		switch(page.getMenuType()) {
		case 1:page.setHrefValue("twzb");break;		//话题
		case 2:page.setHrefValue("lthd");break;		//聊天互动
		case 3:page.setHrefValue("wd");break;		//在线问答
		case 4:page.setHrefValue("zbjj");break;		//直播简介
		case 5:page.setHrefValue("splb");break;		//视频列表
		case 6:page.setHrefValue("wdzb");break;		//文档直播
		}
		
		PageDecorate pageDecorate = pageDecorateMng.getPageDecorateByEventIdAndType(page);
		if(pageDecorate == null) {
			JSONArray jsonArray = JSONArray.fromObject(objjson);
			List<Map<String, Integer>> list = new LinkedList<>();
			if(jsonArray.size()>0) {
				net.sf.json.JSONObject jsonObject;
				for(int i =0;i<jsonArray.size();i++) {
					Map<String, Integer> map = new HashMap<>();
					jsonObject = jsonArray.getJSONObject(i);
					map.put("id", jsonObject.getInt("id"));
					map.put("order", jsonObject.getInt("order"));
					list.add(map);
				}
			}
			// System.out.println(list);
			int id = pageDecorateMng.addPageDecorate(page,list);
			//根据eventId获取roomId
			ILiveEvent event = iLiveEventMng.selectILiveEventByID(page.getLiveEventId());
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(event.getRoomId());
			if(page.getRelatedVideo()==null){
				iLiveLiveRoom.setRelatedVideo(1);
			}else{
			iLiveLiveRoom.setRelatedVideo(page.getRelatedVideo());
			}
			iLiveLiveRoomMng.update(iLiveLiveRoom);
			UserBean user = ILiveUtils.getUser(request);
			workLogMng.save(new WorkLog(WorkLog.MODEL_PAGEDECORATE, id+"", net.sf.json.JSONObject.fromObject(page).toString(), WorkLog.MODEL_PAGEDECORATE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			return id+"";
		}else {
			return "";
		}
	}
	
	//修改操作
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping(value="editmobilepage.do")
	public String edit_mobile_page(PageDecorate page,Integer oldType,Integer oldOrder,String richContent,Long id,String jsonobj,HttpServletRequest request) {
		// System.out.println(page.getRichContent()+"   "+richContent);
		Long liveEventId = page.getLiveEventId();
		Integer menuType = page.getMenuType();
		
		switch(page.getMenuType()) {
		case 1:page.setHrefValue("twzb");break;		//话题
		case 2:page.setHrefValue("lthd");break;		//聊天互动
		case 3:page.setHrefValue("wd");break;		//在线问答
		case 4:page.setHrefValue("zbjj");break;		//直播简介
		case 5:page.setHrefValue("splb");break;		//视频列表
		case 6:page.setHrefValue("wdzb");break;		//文档直播
		}
		
		if(oldOrder == page.getMenuOrder()) {
			//顺序不变  直接更新
			pageDecorateMng.updatePageDecorate(page);
			
		}else {
			JSONArray jsonArray = JSONArray.fromObject(jsonobj);
			List<Map<String, Integer>> list = new LinkedList<>();
			if(jsonArray.size()>0) {
				net.sf.json.JSONObject jsonObject;
				for(int i =0;i<jsonArray.size();i++) {
					Map<String, Integer> map = new HashMap<>();
					jsonObject = jsonArray.getJSONObject(i);
					map.put("id", jsonObject.getInt("id"));
					map.put("order", jsonObject.getInt("order"));
					list.add(map);
				}
			}
			
			
			//更新并排序
			pageDecorateMng.updateAndSetIndex(page,list);
			UserBean user = ILiveUtils.getUser(request);
			workLogMng.save(new WorkLog(WorkLog.MODEL_PAGEDECORATE, id+"", net.sf.json.JSONObject.fromObject(page).toString(), WorkLog.MODEL_PAGEDECORATE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			
		}
		//根据eventId获取roomId
		ILiveEvent event = iLiveEventMng.selectILiveEventByID(page.getLiveEventId());
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(event.getRoomId());
		if(page.getRelatedVideo()==null){
			iLiveLiveRoom.setRelatedVideo(1);
		}else{
		iLiveLiveRoom.setRelatedVideo(page.getRelatedVideo());
		}
		iLiveLiveRoomMng.update(iLiveLiveRoom);
		return "";
	}
	
	private Integer selectServerGroup() {
		return 100;
	}
	
}
