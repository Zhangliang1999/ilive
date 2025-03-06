package com.bvRadio.iLive.iLive.action.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;
import com.bvRadio.iLive.iLive.entity.ILiveSensitiveWord;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessagePraiseMng;
import com.bvRadio.iLive.iLive.manager.ILiveSensitiveWordMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;

@Controller
@RequestMapping(value="record")
public class ILiveEnrollRecordController{
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILiveMessagePraiseMng iLiveMessagePraiseMng;//点赞
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;	//人员管理
	
	@Autowired
	private ILiveMessageMng iLiveMessageMng;//消息管理
	
	//用户记录
	@RequestMapping(value="managerrecord.do")
	public String record(Model model, Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		String roomName = liveEvent.getLiveTitle();
		List<List<ILiveMessagePraise>> listMessage = new LinkedList<>();
		List<String[]> listString = new LinkedList<>();
		List<ILiveMessage> list = iLiveMessageMng.getList(roomId, Constants.LIVE_MSG_TYPE_LIVE, null, null, null, null,null, null, false, true, null,false);
		if (list == null) {
			list = new ArrayList<ILiveMessage>();
		}
		if (list!=null && !list.isEmpty()) {
			for(ILiveMessage iLiveMessage:list) {
				List<ILiveMessagePraise> praises = iLiveMessagePraiseMng.selectILiveMessagePraisByMsgId(iLiveMessage.getMsgId());
				if(praises!=null) {
					listMessage.add(praises);
				}
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println("listMessage: "+listMessage+"    "+listMessage.size());
		for(List<ILiveMessagePraise> list1:listMessage) {
			for(ILiveMessagePraise praise:list1) {
				// System.out.println(praise.getMsgId()+"   "+praise.getUserId()+"   "+format.format(praise.getCreateTime()));
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(praise.getUserId());
				String name = iLiveManager.getUserName();
				ILiveMessage iLiveMessage = iLiveMessageMng.findById(praise.getMsgId());
				String praiseName = iLiveMessage.getMsgContent();
				String[] strings = {roomName,praise.getUserId().toString(),name,praiseName,format.format(praise.getCreateTime())};
				listString.add(strings);
			}
		}
		List<String[]> resultList = new LinkedList<>();
		int praiseNum;
		int pageNum = 1;
		if (listString.size()==0) {
			praiseNum = 0;
			pageNum = 1;
		}else {
			praiseNum = listString.size();
			if(praiseNum>10) {
				resultList = listString.subList(0, 10);
				double temp = praiseNum;
				pageNum = (int) Math.ceil(temp/10);
			}else {
				resultList.addAll(listString);
				pageNum = 1;
			}
		}
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("praisenum",praiseNum);
		model.addAttribute("listString",resultList);
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("topActive", "1");
		model.addAttribute("leftActive", "6_2");
		return "liverecord/liverecord";
	}
	//获取分页数据
	@RequestMapping(value="getpage.do")
	public void recogetpagerd(Integer roomId,Integer pageNo,HttpServletResponse response) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		String roomName = liveEvent.getLiveTitle();
		List<List<ILiveMessagePraise>> listMessage = new LinkedList<>();
		List<String[]> listString = new LinkedList<>();
		List<ILiveMessage> list = iLiveMessageMng.getList(roomId, Constants.LIVE_MSG_TYPE_LIVE, null, null, null, null,null, null, false, true, null,false);
		if (list == null) {
			list = new ArrayList<ILiveMessage>();
		}
		for(ILiveMessage iLiveMessage:list) {
			List<ILiveMessagePraise> praises = iLiveMessagePraiseMng.selectILiveMessagePraisByMsgId(iLiveMessage.getMsgId());
			if(praises!=null) {
				listMessage.add(praises);
			}
		}	
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(List<ILiveMessagePraise> list1:listMessage) {
			for(ILiveMessagePraise praise:list1) {
				// System.out.println(praise.getMsgId()+"   "+praise.getUserId()+"   "+format.format(praise.getCreateTime()));
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(praise.getUserId());
				String name = iLiveManager.getUserName();
				ILiveMessage iLiveMessage = iLiveMessageMng.findById(praise.getMsgId());
				String praiseName = iLiveMessage.getMsgContent();
				String[] strings = {roomName,praise.getUserId().toString(),name,praiseName,format.format(praise.getCreateTime())};
				listString.add(strings);
			}
		}
		List<String[]> resultList = new LinkedList<>();
		int praiseNum;
		int pageNum = 1;
		if (listString.size()==0) {
			praiseNum = 0;
			pageNum = 1;
		}else {
			praiseNum = listString.size();
			if(praiseNum>10) {
				if(praiseNum>pageNo+10) {
					resultList = listString.subList(pageNo, pageNo+10);
				}else {
					resultList = listString.subList(pageNo, praiseNum);
				}
				double temp = praiseNum;
				pageNum = (int) Math.ceil(temp/10);
			}else {
				resultList.addAll(listString);
				pageNum = 1;
			}
		}
		JSONObject resultjson = new JSONObject();
		resultjson.put("data", resultList);
		ResponseUtils.renderJson(response,resultjson.toString());
	}
}
