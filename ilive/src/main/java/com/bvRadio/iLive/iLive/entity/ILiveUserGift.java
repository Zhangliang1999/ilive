package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveUserGift;

@SuppressWarnings("serial")
public class ILiveUserGift extends BaseILiveUserGift {
	/**
	 * 送礼物操作
	 */
	public static final Integer ILIVE_GIFT_ADD = 0;
	/**
	 * 礼物列表发生改变操作
	 */
	public static final Integer ILIVE_GIFT_Operation=1;
	/**
	 * 礼物操作   0 送  1 操作
	 */
	private Integer giftOperation;
	
	
	public Integer getGiftOperation() {
		return giftOperation;
	}
	public void setGiftOperation(Integer giftOperation) {
		this.giftOperation = giftOperation;
	}
	public JSONObject putGiftJson(JSONObject msgJson){
		if (null == msgJson) {
			msgJson = new JSONObject();
		}
		Integer giftOperation2 = getGiftOperation();
		if(giftOperation2==null){
			msgJson.put("giftOperation", 0);
		}else{
			msgJson.put("giftOperation", giftOperation);
		}
		Long id = getId();
		if(id==null){
			msgJson.put("id", 0);
		}else{
			msgJson.put("id", id);
		}
		Long evenId = getEvenId();
		if(evenId==null){
			msgJson.put("evenId", 0);
		}else{
			msgJson.put("evenId", evenId);
		}
		Long giftId = getGiftId();
		if(giftId==null){
			msgJson.put("giftId", 0);
		}else{
			msgJson.put("giftId", giftId);
		}
		String giftName = getGiftName();
		if(giftName==null){
			msgJson.put("giftName", "");
		}else{
			msgJson.put("giftName", giftName);
		}
		Integer giftNumber = getGiftNumber();
		if(giftNumber==null){
			msgJson.put("giftNumber", 0);
		}else{
			msgJson.put("giftNumber", giftNumber);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp giveTime = getGiveTime();
		if(giveTime==null){
			msgJson.put("giveTime", format.format(new Date()));
		}else{
			msgJson.put("giveTime", format.format(giveTime));
		}
		Integer roomId = getRoomId();
		if(roomId==null){
			msgJson.put("roomId", 0);
		}else{
			msgJson.put("roomId", roomId);
		}
		String roomTitle = getRoomTitle();
		if(roomTitle==null){
			msgJson.put("roomTitle", "");
		}else{
			msgJson.put("roomTitle", roomTitle);
		}
		String giftPrice = getGiftPrice();
		if(giftPrice==null){
			msgJson.put("giftPrice", "0.00");
		}else{
			msgJson.put("giftPrice", giftPrice);
		}
		Long userId = getUserId();
		if(userId==null){
			msgJson.put("userId", 0);
		}else{
			msgJson.put("userId", userId);
		}
		String userName = getUserName();
		if(userName==null){
			msgJson.put("userName", "");
		}else{
			msgJson.put("userName", userName);
		}
		String giftImage = getGiftImage();
		if(giftImage==null){
			msgJson.put("giftImage", "");
		}else{
			msgJson.put("giftImage", giftImage);
		}
		return msgJson;
	}
}
