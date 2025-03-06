package com.bvRadio.iLive.iLive.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveRollingAdvertising;

@SuppressWarnings("serial")
public class ILiveRollingAdvertising extends BaseILiveRollingAdvertising{
	
	public JSONObject putMessageInJson(JSONObject msgJson) throws JSONException {
		if(msgJson==null){
			msgJson = new JSONObject();
		}
		Integer roomId = getRoomId();
		if(roomId != null){
			msgJson.put("roomId", roomId);
		}else{
			msgJson.put("roomId", 0);
		}
		String content = getContent();
		if(content != null){
			msgJson.put("content", content);
		}else{
			msgJson.put("content", "");
		}
		Integer startType = getStartType();
		if(startType != null){
			msgJson.put("startType", startType);
		}else{
			msgJson.put("startType", 0);
		}
		return msgJson;
	}

}
