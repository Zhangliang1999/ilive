package com.bvRadio.iLive.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import com.bvRadio.iLive.sms.ILiveSmgServerCenter;

public class sendMsgTest {
public static void main(String[] args) {
	Map<String, Object> sendParam = new HashMap<>();
	List<Object> paramslist=new ArrayList<Object>();
	final String string = "您预约的直播:发送短信测试,将于2019-02-21 15:31:00开始直播,观看地址:http://zb.tv189.com/pc/play.html?roomId=5776";
	paramslist.add(string);
	sendParam.put("biztype", 246634);
	sendParam.put("params",paramslist);
	List<String> phonelist=new ArrayList<String>();
	phonelist.add("18510328589");
	try {
		Map<String, Object> sendMsg = ILiveSmgServerCenter.sendMsgNew(phonelist, sendParam);
		JSONObject json = new JSONObject(sendMsg);
		System.out.println("json========="+json);
		
	} catch (Exception e) {
		
		e.printStackTrace();
	}
}
}
