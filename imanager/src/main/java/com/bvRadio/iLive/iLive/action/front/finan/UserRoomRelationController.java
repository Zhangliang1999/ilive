package com.bvRadio.iLive.iLive.action.front.finan;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.UserRoomRelation;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.UserRoomRelationMng;
import com.google.gson.Gson;

@Controller
public class UserRoomRelationController {
	@Autowired
	private UserRoomRelationMng userRoomRelationMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	
	@RequestMapping("/oss/getRoomId")
	public @ResponseBody String getUserRoomRelationByUserId(Integer userId){
		List<UserRoomRelation> findByUid = userRoomRelationMng.findByUid(userId);
		String roomID="0";
		if(findByUid!=null){
			if(findByUid.size()>0){
				UserRoomRelation userRoomRelation = findByUid.get(0);
				roomID = String.valueOf(userRoomRelation.getRoomId());
				System.out.println("接收到用户ID："+userId+"  查询到集合大小："+findByUid.size());
			}
		}
		System.out.println("接收到用户ID："+userId+"  查询到roomID："+roomID);
		return roomID;
	}
	@RequestMapping("/oss/getAllRoomId")
	public  void getUserRoomRelationAll(HttpServletResponse response){
		List<ILiveLiveRoom> findRoomList = iLiveLiveRoomMng.findRoomList();
		System.out.println("接收到房间号数量："+findRoomList.size());
		Gson gson = new Gson();
		String json = gson.toJson(findRoomList);
		System.out.println("ILIVE-接收到数据："+json);
		ResponseUtils.renderJson(response, json);
	}
}
