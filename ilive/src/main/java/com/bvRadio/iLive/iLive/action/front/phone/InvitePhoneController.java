package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.InviteCard;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.InviteCardMng;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="invite")
public class InvitePhoneController {

	@Autowired
	private InviteCardMng inviteCardMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	@RequestMapping(value="getinvite.jspx")
	public void getInfo(Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			InviteCard byroomId = inviteCardMng.getByroomId(roomId);
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			JSONObject card = JSONObject.fromObject(byroomId);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = format.format(byroomId.getStartTime());
			card.put("startTime", startTime);
			try {
				ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
						.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
				String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
				byroomId.setLiveUrl(liveAddr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			resultJson.put("data",card);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
}
