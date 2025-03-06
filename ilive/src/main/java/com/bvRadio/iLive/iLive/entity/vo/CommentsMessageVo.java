package com.bvRadio.iLive.iLive.entity.vo;

import java.util.ArrayList;
import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveComments;

public class CommentsMessageVo {
	/**
	 * 话题ID
	 */
	private Long msgId;
	/**
	 * 评论集
	 */
	private List<ILiveComments> list = new ArrayList<ILiveComments>();
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public List<ILiveComments> getList() {
		return list;
	}
	public void setList(List<ILiveComments> list) {
		this.list = list;
	}
	public CommentsMessageVo(Long msgId, List<ILiveComments> list) {
		super();
		this.msgId = msgId;
		this.list = list;
	}
	public CommentsMessageVo() {
		super();
	}
	
}
