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
		Long guestsId = getGuestsId();
		if(guestsId!=null){
			msgJson.put("guestsId", guestsId);
		}else{
			msgJson.put("guestsId", 0);
		}
		String guestsName = getGuestsName();
		if(guestsName!=null){
			msgJson.put("guestsName", guestsName);
		}else{
			msgJson.put("guestsName", "");
		}
		String guestsImage = getGuestsImage();
		if(guestsImage!=null){
			msgJson.put("guestsImage", guestsImage);
		}else{
			msgJson.put("guestsImage", "");
		}
		Boolean isDelete = getIsDelete();
		if(isDelete){
			msgJson.put("isDelete", 1);
		}else{
			msgJson.put("isDelete", 0);
		}
		return msgJson;
	}
	
	

}
