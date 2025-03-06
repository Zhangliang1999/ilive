package com.bvRadio.iLive.iLive.entity;

import static com.bvRadio.iLive.iLive.Constants.MSG_TYPE_IMG;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveMessage;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;

@SuppressWarnings("serial")
public class ILiveMessage extends BaseILiveMessage implements java.io.Serializable {
	private boolean update = false;
	/**
	 * 是否被清空   
	 */
	private boolean deleteAll = false;
	/**
	 * 操纵类型
	 * 0   页面设置     1 消息管理
	 */
	private Integer roomType;
	/**
	 * 操作记录
	 */
	private ILiveEventVo iLiveEventVo;
	public boolean getUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isDeleteAll() {
		return deleteAll;
	}

	public void setDeleteAll(boolean deleteAll) {
		this.deleteAll = deleteAll;
	}
	public Integer getRoomType() {
		return roomType;
	}

	public void setRoomType(Integer roomType) {
		this.roomType = roomType;
	}

	public ILiveEventVo getiLiveEventVo() {
		return iLiveEventVo;
	}

	public void setiLiveEventVo(ILiveEventVo iLiveEventVo) {
		this.iLiveEventVo = iLiveEventVo;
	}

	public List<String> getImages() {
		List<String> images = null;
		if (null != getMsgType() && getMsgType() == MSG_TYPE_IMG) {
			String content = getMsgContent();
			String[] imagesArray = content.split(",");
			if (null != imagesArray) {
				images = new ArrayList<String>();
				for (String image : imagesArray) {
					if (!StringUtils.isBlank(image)) {
						images.add(image);
					}
				}
			}
		}
		return images;
	}

	public String getContent() {
		String content = getMsgContent();
		if (!StringUtils.isBlank(content)) {
			content = ExpressionUtil.replaceKeyToImg(content);
		}
		return content;
	}

	public String getOrginContent() {
		String content = getMsgOrginContent();
		if (!StringUtils.isBlank(content)) {
			content = ExpressionUtil.replaceKeyToImg(content);
		}
		return content;
	}

	@SuppressWarnings("static-access")
	public JSONObject putMessageInJson(JSONObject msgJson) throws JSONException {
		if (null == msgJson) {
			msgJson = new JSONObject();
		}
		Long msgId = getMsgId();
		Integer liveMsgType = getLiveMsgType();
		String senderName = getSenderName();
		String senderImage = getSenderImage();
		Integer serviceType = getServiceType();
		String extra = getExtra();
		com.alibaba.fastjson.JSONObject jObj = null;
		if(extra!=null&&!extra.equals("")){
			 jObj = new com.alibaba.fastjson.JSONObject().parseObject(extra);
		}
		Integer senderLevel = getSenderLevel();
		String content = getMsgContent();
		List<String> images = getImages();
		Integer msgType = getMsgType();
		String fontColor = getFontColor();
		boolean deleted = getDeleted();
		boolean update = getUpdate();
		boolean checked = getChecked();
		Timestamp createTime = getCreateTime();
		Integer width = getWidth();
		Integer height = getHeight();
		msgJson.put("serviceType", serviceType);
		msgJson.put("extra", jObj);
		msgJson.put("msgId", msgId);
		msgJson.put("liveMsgType", liveMsgType);
		if (deleted) {
			msgJson.put("deleted", 1);
		} else {
			msgJson.put("deleted", 0);
		}
		if (update) {
			msgJson.put("update", 1);
		} else {
			msgJson.put("update", 0);
		}
		if (checked) {
			msgJson.put("checked", 1);
		} else {
			msgJson.put("checked", 0);
		}
		
		if(deleteAll){
			msgJson.put("deleteAll", 1);
		}else{
			msgJson.put("deleteAll", 0);
		}
		if (!StringUtils.isBlank(senderName)) {
			msgJson.put("senderName", senderName);
		} else {
			msgJson.put("senderName", "");
		}
		if (!StringUtils.isBlank(senderImage)) {
			msgJson.put("senderImage", senderImage);
		} else {
			msgJson.put("senderImage", "");
		}
		if (null != senderLevel) {
			msgJson.put("senderLevel", senderLevel);
		} else {
			msgJson.put("senderLevel", -1);
		}
		if (!StringUtils.isBlank(content)) {
			msgJson.put("content", content);
		} else {
			msgJson.put("content", "");
		}
		msgJson.put("images", images);
		msgJson.put("msgType", msgType);
		if (!StringUtils.isBlank(fontColor)) {
			msgJson.put("fontColor", fontColor);
		} else {
			msgJson.put("fontColor", "");
		}
		if (null != width) {
			msgJson.put("width", width);
		} else {
			msgJson.put("width", 0);
		}
		if (null != height) {
			msgJson.put("height", height);
		} else {
			msgJson.put("height", 0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			msgJson.put("createTime", sdf.format(createTime));
		} catch (Exception e) {
			msgJson.put("createTime", "");
		}
		Integer top = getTop();
		if (null != top) {
			msgJson.put("top", top);
		} else {
			msgJson.put("top", 0);
		}
		Integer opType = getOpType(); 
		if(opType!=null){
			msgJson.put("opType", opType);
		}else{
			msgJson.put("opType", 11);
		}
		Long senderId = getSenderId();
		if(senderId!=null){
			msgJson.put("senderId", senderId);
		}else{
			msgJson.put("senderId", 0);
		}
		Integer senderType = getSenderType();
		if(senderType!=null){
			msgJson.put("senderType", senderType);
		}else{
			msgJson.put("senderType", 0);
		}
		
		Integer replyType = getReplyType();
		if(replyType!=null){
			msgJson.put("replyType", replyType);
		}else{
			msgJson.put("replyType", 0);
		}
		String replyContent = getReplyContent();
		if(replyContent!=null){
			msgJson.put("replyContent", replyContent);
		}else{
			msgJson.put("replyContent", "");
		}
		Integer roomType = getRoomType();
		if(roomType!=null){
			msgJson.put("roomType", roomType);
		}else{
			msgJson.put("roomType", 0);
		}
		String replyName = getReplyName();
		if(replyName!=null){
			msgJson.put("replyName", replyName);
		}else{
			msgJson.put("replyName", "admin");
		}
		Long praiseNumber = getPraiseNumber();
		if(praiseNumber!=null){
			msgJson.put("praiseNumber", praiseNumber);
		}else{
			msgJson.put("praiseNumber", 0);
		}
		ILiveEventVo iLiveEventVo = getiLiveEventVo();
		if(iLiveEventVo!=null){
			msgJson.put("iLiveEventVo", new com.alibaba.fastjson.JSONObject().toJSONString(iLiveEventVo));
		}else{
			iLiveEventVo = new ILiveEventVo();
			msgJson.put("iLiveEventVo", new com.alibaba.fastjson.JSONObject().toJSONString(iLiveEventVo));
		}
		return msgJson;
	}

	public ILiveMessage() {
		super();
	}

	public ILiveMessage(Long msgId, ILiveEvent live, ILiveIpAddress iLiveIpAddress) {
		super(msgId, live, iLiveIpAddress);
	}

	public ILiveMessage(Long msgId, ILiveEvent live, ILiveIpAddress iLiveIpAddress, String senderName,
			String msgOrginContent, String msgContent, Integer msgType, Timestamp createTime,
			Integer liveMsgType, String iMEI,Integer serviceType,String extra,Integer top,Integer opType, Long senderId,Integer senderType,Integer replyType,String replyContent,String replyName,Long praiseNumber) {
		super(msgId, live, iLiveIpAddress, senderName, msgOrginContent, msgContent, msgType, createTime,
				liveMsgType, iMEI, serviceType, extra, top, opType, senderId,senderType,replyType,replyContent,replyName,praiseNumber);
	}

}
