package com.bvRadio.iLive.iLive.entity;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BeasILiveGuests;
/**
 * 嘉宾
 * @author YanXL
 *
 */
public class ILiveGuests extends BeasILiveGuests implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JSONObject putMessageInJson(JSONObject msgJson) {
		if (null == msgJson) {
			msgJson = new JSONObject();
		}
		Integer roomId = getRoomId();
		if(roomId!=null){
			msgJson.put("roomId", roomId);
		}else{
			msgJson.put("roomId", 0);
		}
		String guestsName = getGuestsName();
		if(guestsName!=null){
			msgJson.put("guestsName", guestsName);
		}else{
			msgJson.put("guestsName", "主持人");
		}
		String guestsLabel = getGuestsLabel();
		if(guestsLabel!=null){
			msgJson.put("guestsLabel", guestsLabel);
		}else{
			msgJson.put("guestsLabel", "直播方");
		}
		return msgJson;
	}
	
	

}
