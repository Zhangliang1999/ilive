package com.bvRadio.iLive.iLive.action.admin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value="pagedecorate")
public class ILivePageDecorateController {
	
	@Autowired
	private ILiveEventMng iLiveEventMng;// 场次管理

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILivePageDecorateMng pageDecorateMng;// 页面装修
	
	//移动端页面显示
	@RequestMapping(value="mobilepage.do")
	public String mobile_page(Model model,Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		String pageRichContent = "";
		List<PageDecorate> list = pageDecorateMng.getList(liveEvent.getLiveEventId());
		if(list!=null && !list.isEmpty()) {
			for(PageDecorate page : list ) {
				if(page.getMenuType()==4) {
					pageRichContent = page.getRichContent();
					break;
				}
			}
		}
		model.addAttribute("pageRichContent",pageRichContent);
		model.addAttribute("list",list);
		model.addAttribute("leftActive", "3_1");
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("topActive", "1");
		return "pagedecorate/mobile_page";
	}
	
	//PC端页面显示
	@RequestMapping(value="pcpage.do")
	public String pc_page(Model model,Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		List<PageDecorate> list = pageDecorateMng.getList(liveEvent.getLiveEventId());
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
		model.addAttribute("leftActive", "3_3");
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("serverGroupId",serverGroupId);
		model.addAttribute("topActive", "1");
		return "pagedecorate/live_leader";
	}

	//修改直播引导页
	@ResponseBody
	@RequestMapping(value="liveleaderedit.do")
	public String liveleaderedit(Integer roomId,Integer openGuideSwitch,String guideAddr) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		if(openGuideSwitch == 1) {
			liveEvent.setOpenGuideSwitch(1);
			liveEvent.setGuideAddr(guideAddr);
		}else {
			liveEvent.setOpenGuideSwitch(0);
		}
		iLiveEventMng.updateILiveEvent(liveEvent);
		
		return "";
	}
	
	//删除操作
	@ResponseBody
	@RequestMapping(value="mobilepagedelete.do")
	public String mobile_page_delete(Long liveEventId,Integer menuType,Integer menuOrder,Long id,Integer noind,String jsonobj) {
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
		}
		return "";
	}
	
	//添加操作
	@ResponseBody
	@RequestMapping(value="addmobilepage.do")
	public String add_mobile_page(PageDecorate page,String objjson) {
		switch(page.getMenuType()) {
		case 1:page.setHrefValue("twzb");break;		//话题
		case 2:page.setHrefValue("lthd");break;		//聊天互动
		case 3:page.setHrefValue("wd");break;		//在线问答
		case 4:page.setHrefValue("zbjj");break;		//直播简介
		case 5:page.setHrefValue("splb");break;		//视频列表
		case 6:page.setHrefValue("wdzb");break;		//文档直播
		}
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
		System.out.println(list);
		int id = pageDecorateMng.addPageDecorate(page,list);
		return id+"";
	}
	
	//修改操作
	@ResponseBody
	@RequestMapping(value="editmobilepage.do")
	public String edit_mobile_page(PageDecorate page,Integer oldType,Integer oldOrder,String richContent,Long id,String jsonobj) {
		System.out.println(page.getRichContent()+"   "+richContent);
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
		}
		return "";
	}
	
	private Integer selectServerGroup() {
		return 100;
	}
	
}
