package com.bvRadio.iLive.iLive.entity;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveMediaFileComments;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;

public class ILiveMediaFileComments extends BaseILiveMediaFileComments {

	/**
	 * 构造简单bean
	 * 
	 * @return
	 */
	public JSONObject toSimpleJsonObj() {
		JSONObject jobj = new JSONObject();
		jobj.put("commentsId", this.getCommentsId());
		jobj.put("commentsUser", this.getCommentsUser());
		jobj.put("comments", ExpressionUtil.replaceKeyToImg(this.getComments()));
		return jobj;
	}

}
