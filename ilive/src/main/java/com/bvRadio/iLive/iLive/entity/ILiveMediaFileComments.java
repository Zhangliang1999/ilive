package com.bvRadio.iLive.iLive.entity;

import java.util.Date;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveMediaFileComments;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class ILiveMediaFileComments extends BaseILiveMediaFileComments {

	/**
	 * 构造简单bean
	 * 
	 * @return
	 */
	public JSONObject toSimpleJsonObj() {
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jobj = new JSONObject();
		jobj.put("commentsId", this.getCommentsId());
		jobj.put("commentsUser", this.getCommentsUser());
		jobj.put("userImg", this.getUserImg()==null?ConfigUtils.get("defaultTerminalUserImg"):this.getUserImg());
		jobj.put("appComments", this.getComments() == null ? "" : this.getComments());
		jobj.put("comments", ExpressionUtil.replaceKeyToImg(this.getComments() == null ? "" : this.getComments()));
		jobj.put("createTime", this.getCreateTime()==null?sf.format(new Date()):sf.format(this.getCreateTime()));
		jobj.put("topFlagNum", this.getTopFlagNum());
		jobj.put("sendType", this.getSendType());
		return jobj;
	}

	
	
	public JSONObject toWebSimpleJsonObj() {
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jobj = new JSONObject();
		jobj.put("commentsId", this.getCommentsId());
		jobj.put("commentsUser", this.getCommentsUser());
		jobj.put("userImg", this.getUserImg()==null?ConfigUtils.get("defaultTerminalUserImg"):this.getUserImg());
		jobj.put("appComments", this.getComments() == null ? "" : this.getComments());
		jobj.put("comments", ExpressionUtil.replaceKeyToImg(this.getComments() == null ? "" : this.getComments()));
		jobj.put("createTime", this.getCreateTime()==null?sf.format(new Date()):sf.format(this.getCreateTime()));
		jobj.put("checkState", this.getCheckState());
		jobj.put("topFlagNum", this.getTopFlagNum());
		jobj.put("sendType", this.getSendType());
		jobj.put("userId", this.getUserId());
		return jobj;
	}

}
