package com.jwzt.statistic.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

import com.jwzt.statistic.entity.base.BaseWorkingLog;
/**
 * 工作日志
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class WorkingLog extends BaseWorkingLog{
	
	public JSONObject postJson(JSONObject jsonObject){
		if(jsonObject==null){
			jsonObject = new JSONObject();
		}
		String id = getId();
		if(id==null){
			jsonObject.put("id", "");
		}else{
			jsonObject.put("id", id);
		}
		Integer systemId = getSystemId();
		if(systemId==null){
			jsonObject.put("systemId", 0);
		}else{
			jsonObject.put("systemId", systemId);
		}
		Integer modelId = getModelId();
		if(modelId==null){
			jsonObject.put("modelId", 0);
		}else{
			jsonObject.put("modelId", modelId);
		}
		String contentId = getContentId();
		if(contentId==null){
			jsonObject.put("contentId", "0");
		}else{
			jsonObject.put("contentId", contentId);
		}
		String content = getContent();
		if(content==null){
			jsonObject.put("content", "");
		}else{
			jsonObject.put("content", content);
		}
		String contentName = getContentName();
		if(contentName==null){
			jsonObject.put("contentName", "");
		}else{
			jsonObject.put("contentName", contentName);
		}
		Integer userId = getUserId();
		if(userId==null){
			jsonObject.put("userId", 0);
		}else{
			jsonObject.put("userId", userId);
		}
		String userName = getUserName();
		if(userName==null){
			jsonObject.put("userName", "");
		}else{
			jsonObject.put("userName", userName);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp createTime = getCreateTime();
		if(createTime==null){
			jsonObject.put("createTime", "");
		}else{
			jsonObject.put("createTime", format.format(createTime));
		}
		String terminal = getTerminal();
		if(terminal==null){
			jsonObject.put("terminal", "");
		}else{
			jsonObject.put("terminal", terminal);
		}
		return jsonObject;
	}

}
