package com.bvRadio.iLive.iLive.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.InviteCard;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.InviteCardMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONObject;

@RequestMapping(value="invite")
public class InviteController {

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private InviteCardMng inviteCardMng; 	//邀请卡
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	@Autowired
	private WorkLogMng workLogMng;
	
	/**
	 * 设置邀请卡页面
	 * @param model
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value="set")
	public String invite(Model model,Integer roomId) {
		ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		InviteCard inviteCard = inviteCardMng.getByroomId(roomId);
		if(inviteCard == null) {
			inviteCard = new InviteCard();
			inviteCard.setLiveTitle(liveEvent.getLiveTitle());
			inviteCard.setRoomId(roomId);
			inviteCard.setStartTime(liveEvent.getLiveStartTime());
			inviteCard.setIsOpen(0);
			inviteCard.setSponser(liveEvent.getHostName());
			inviteCard.setIsPublish(0);
			inviteCardMng.save(inviteCard);
		}
		Integer serverGroupId = this.selectServerGroup();
		try {
			ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
					.getAccessMethodBySeverGroupId(iLiveLiveRoom.getServerGroupId());
			String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + roomId;
			model.addAttribute("liveAddr", liveAddr);
		}catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("inviteCard", inviteCard);
		model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
		model.addAttribute("leftActive", "2_10");
		model.addAttribute("topActive", "1");
		return "liveconfig/invite";
	}
	private Integer selectServerGroup() {
		return 100;
	}
	
	/**
	 * 保存或者修改邀请卡
	 * @param inviteCard
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="update.do")
	public void save(InviteCard inviteCard,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			inviteCardMng.update(inviteCard);
			workLogMng.save(new WorkLog(WorkLog.MODEL_EDITINVITE, inviteCard.getRoomId()+"", net.sf.json.JSONObject.fromObject(inviteCard).toString(), WorkLog.MODEL_EDITINVITE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping(value="publish")
	public void publish(Integer roomId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			InviteCard inviteCard = inviteCardMng.getByroomId(roomId);
			inviteCard.setIsPublish(1);
			inviteCardMng.update(inviteCard);
			result.put("status", 1);
		}catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
}
