package com.bvRadio.iLive.iLive.action.front.vo;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.ILiveComments;
import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;

public class MessageNumberPraiseVo {
	/**
	 * 消息ID
	 */
	private Long msgId;
	/**
	 * 点赞数
	 */
	private Long praiseNumber;
	/**
	 * 评论
	 */
	private List<ILiveComments>  commentsList;
	/**
	 * 点赞人
	 */
	private List<ILiveMessagePraise> praiseList;
	public MessageNumberPraiseVo() {
		super();
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public Long getPraiseNumber() {
		return praiseNumber;
	}

	public void setPraiseNumber(Long praiseNumber) {
		this.praiseNumber = praiseNumber;
	}

	public List<ILiveComments> getCommentsList() {
		return commentsList;
	}

	public void setCommentsList(List<ILiveComments> commentsList) {
		this.commentsList = commentsList;
	}
	
	public List<ILiveMessagePraise> getPraiseList() {
		return praiseList;
	}

	public void setPraiseList(List<ILiveMessagePraise> praiseList) {
		this.praiseList = praiseList;
	}

	public JSONObject putMessageJson(JSONObject msgJson) throws JSONException {
		if (null == msgJson) {
			msgJson = new JSONObject();
		}
		Long id = getMsgId();
		if(id!=null){
			msgJson.put("msgId", id);
			Long number = getPraiseNumber();
			if(number!=null){
				msgJson.put("praiseNumber", number);
			}else{
				msgJson.put("praiseNumber", 0);
			}
			List<ILiveComments> list = getCommentsList();
			if(list!=null){
				msgJson.put("commentsList", JSONArray.fromObject(list));
			}else{
				msgJson.put("commentsList", new JSONArray());
			}
			List<ILiveMessagePraise> praises = getPraiseList();
			if(praises!=null){
				msgJson.put("praiseList", JSONArray.fromObject(praises));
			}else{
				msgJson.put("praiseList", new JSONArray());
			}
		}
		return msgJson;
	}

}
