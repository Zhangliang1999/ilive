package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveGuests;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.manager.ILiveGuestsMng;

/**
 * 嘉宾管理
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="guests")
public class ILiveGuestsController {
	
	@Autowired
	private ILiveGuestsMng iLiveGuestsMng;//嘉宾
	
	/**
	 * 添加嘉宾
	 * @param iLiveGuests
	 */
	@RequestMapping(value="/addGuests.do",method=RequestMethod.POST)
	public void addILiveGuests(String guestsName,String guestsImage,Integer roomId,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveGuests iLiveGuests = new ILiveGuests();
			iLiveGuests.setGuestsName(guestsName);
			iLiveGuests.setGuestsImage(guestsImage);
			ILiveLiveRoom iLiveLiveRoom = new ILiveLiveRoom();
			iLiveLiveRoom.setRoomId(roomId);
			iLiveGuests.setiLiveLiveRoom(iLiveLiveRoom);
			iLiveGuests.setIsDelete(false);
			iLiveGuests = iLiveGuestsMng.addILiveGuest(iLiveGuests);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
			resultJson.put("iLiveGuests", iLiveGuests.putMessageInJson(null));
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	@RequestMapping(value="/deleteGuests.do",method=RequestMethod.POST)
	public void deleteILiveGuests(Long guestsId,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			iLiveGuestsMng.deleteILiveGuests(guestsId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (JSONException e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
}
