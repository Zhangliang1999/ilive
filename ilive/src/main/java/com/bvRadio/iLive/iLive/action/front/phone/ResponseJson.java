package com.bvRadio.iLive.iLive.action.front.phone;

import com.alibaba.fastjson.JSONObject;


public class ResponseJson{
	//成功有返回参数的Json
	public String CreateJson(Object vo){
		JSONObject json= new JSONObject();
		json.put("Code", 200);
		json.put("message", "成功");
		json.put("data", vo);
		return json.toString();
	}
	//失败
	public String CreateErrorJson(String error){
		JSONObject json= new JSONObject();
		json.put("Code", 500);
		json.put("message", "失败");
		json.put("data", error);
		return json.toString();
	}
	//成功无返回 参数的json
	public String CreateNoDataJson(){
		JSONObject json= new JSONObject();
		json.put("Code", 200);
		json.put("message", "成功");
		return json.toString();
	}
}

