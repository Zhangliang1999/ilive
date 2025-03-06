package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveMessage;
import com.bvRadio.iLive.iLive.entity.vo.ILiveEventVo;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;

@SuppressWarnings("serial")
public class ILiveMessage extends BaseILiveMessage implements java.io.Serializable {
	/**
	 * 操作类型：页面设置
	 */
	public static final Integer ROOM_TYPE_WEB_CONFIG = 0;
	/**
	 * 操作类型：消息管理
	 */
	public static final Integer ROOM_TYPE_MESSAGE = 1;
	/**
	 * 操作类型：话题
	 */
	public static final Integer ROOM_TYPE_TOPIC = 2;
	/**
	 * 操作类型：直播间删除
	 */
	public static final Integer ROOM_TYPE_ROOM_DELETE = 3;
	/**
	 * 操作类型：广告
	 */
	public static final Integer ROOM_TYPE_AD = 4;
	/**
	 * 操作类型：抽奖
	 */
	public static final Integer ROOM_TYPE_LOTTERY = 5;
	/**
	 * 操作类型：投票
	 */
	public static final Integer ROOM_TYPE_VOTE = 6;
	/**
	 * 操作类型：礼物
	 */
	public static final Integer ROOM_TYPE_GIFT = 7;
	/**
	 * 操作类型：连麦
	 */
	public static final Integer ROOM_TYPE_MICROPHONE = 8;
	/**
	 * 打赏
	 */
	public static final Integer ROOM_TYPE_PLAY_REWARDS = 9;
	/**
	 * 观看人进入直播间
	 */
	public static final Integer ROOM_TYPE_USER = 10;
	/**
	 * 文档直播内容
	 */
	public static final Integer ROOM_TYPE_DOCUMENT = 11;
	/**
	 * 连接类型：请求
	 */
	public static final Integer CONNECT_TYPE_ACCESS = 1;
	/**
	 * 连接类型：接受
	 */
	public static final Integer CONNECT_TYPE_ACCEPT= 2;
	/**
	 * 连接类型：拒绝
	 */
	public static final Integer CONNECT_TYPE_REFUSE = 3;
	
	/**
	 * 连接类型：签到
	 */
	public static final Integer CONNECT_TYPE_SIGN = 15;
	
	/**
	 * 连接类型：红包
	 */
	public static final Integer CONNECT_TYPE_PACKET = 16;
	/**
	 * 连接类型：评论
	 */
	public static final Integer CONNECT_TYPE_COMMONT = 17;
	
	/**
	 * 
	 */
	public static final Integer COMMENT_AUTO_CHECK = 18;
	/**
	 * 问卷
	 */
	public static final Integer ROOM_TYPE__Questionnaire = 19;
	private boolean update = false;
	/**
	 * 是否被清空
	 */
	private boolean deleteAll = false;
	/**
	 * 操纵类型 0 页面设置 1 消息管理
	 */
	private Integer roomType;
	
	private Integer connectType;
	
	/**
	 * 操作记录
	 */
	private ILiveEventVo iLiveEventVo;
	/**
	 * 礼物收取记录
	 */
	private ILiveUserGift iLiveUserGift;
	/**
	 * 打赏记录
	 */
	private ILiveUserPlayRewards iLiveUserPlayRewards;
	/**
	 * 欢迎语
	 */
	private String welcomeLanguage;
	/**
	 * 当前文档页码
	 */
	private Integer documentPageNO;
	/**
	 * 文档ID
	 */
	private Long documentId;
	/**
	 * 文档是否允许用户下载：0 关闭  1 开启
	 */
	private Integer documentDownload;
	/**
	 * 手动翻页是否开启：0 关闭  1 开启
	 */
	private Integer documentManual;
	
	public Integer getDocumentPageNO() {
		return documentPageNO;
	}

	public void setDocumentPageNO(Integer documentPageNO) {
		this.documentPageNO = documentPageNO;
	}

	public String getWelcomeLanguage() {
		return welcomeLanguage;
	}

	public void setWelcomeLanguage(String welcomeLanguage) {
		this.welcomeLanguage = welcomeLanguage;
	}

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

	public ILiveUserPlayRewards getiLiveUserPlayRewards() {
		return iLiveUserPlayRewards;
	}

	public void setiLiveUserPlayRewards(ILiveUserPlayRewards iLiveUserPlayRewards) {
		this.iLiveUserPlayRewards = iLiveUserPlayRewards;
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

	public ILiveUserGift getiLiveUserGift() {
		return iLiveUserGift;
	}

	public void setiLiveUserGift(ILiveUserGift iLiveUserGift) {
		this.iLiveUserGift = iLiveUserGift;
	}

	public Integer getConnectType() {
		return connectType;
	}

	public void setConnectType(Integer connectType) {
		this.connectType = connectType;
	}

	public String getWebContents(){
		String web = "";
		if(getMsgOrginContent()==null){
		}else{
			web=ExpressionUtil.replaceKeyToImg(getMsgOrginContent());
		}
		return web;
	}
	public List<String> getImagesList(){
		List<String> list = new ArrayList<String>();
		String images = getImages();
		if(images!=null){
			String[] split = images.split(",");
			for (int i = 0; i < split.length; i++) {
				list.add(split[i]);
			}
		}
		return list;
	}
	
	public String getCreateTimeTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp createTime = getCreateTime();
		System.out.println(createTime);
		if (createTime == null) {
			return "未知时间";
		}
		Date date = new Date(createTime.getTime());
		String time = format.format(date);
		return time;
		
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*Timestamp createTime = getCreateTime();
		Date date = new Date();
		String timer = "";
		if(createTime==null){
			timer="未知时间";
		}else{
			if(date.getTime()-createTime.getTime()<1000*60){
				//1分钟前
				timer = "1分钟前";
			}else if(date.getTime()-createTime.getTime()<1000*60*5){
				//5分钟前
				timer = "5分钟前";
			}else if(date.getTime()-createTime.getTime()<1000*60*10){
				//10分钟前
				timer = "10分钟前";
			}else if(date.getTime()-createTime.getTime()<1000*60*30){
				//30分钟前
				timer = "30分钟前";
			}else if(date.getTime()-createTime.getTime()<1000*60*60){
				//一小时前
				timer = "1小时前";
			}else{
				//更早
				timer = "更早";
			}
		}
		return timer;*/
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
		if (extra != null && !extra.equals("")) {
			jObj = new com.alibaba.fastjson.JSONObject().parseObject(extra);
		}
		Integer senderLevel = getSenderLevel();
		Integer isLiveSend= getIsLiveSend();
		String fontColor = getFontColor();
		boolean deleted = getDeleted();
		boolean update = getUpdate();
		boolean checked = getChecked();
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

		if (deleteAll) {
			msgJson.put("deleteAll", 1);
		} else {
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
		if (null != isLiveSend) {
			msgJson.put("isLiveSend", isLiveSend);
		} else {
			msgJson.put("isLiveSend", 0);
		}
		String content = getMsgContent();
		if (!StringUtils.isBlank(content)) {
			content = content.replaceAll("(\r\n|\r|\n|\n\r)", "");
			msgJson.put("content", content);
		} else {
			msgJson.put("content", "");
		}
		List<String> images = getImagesList();
		msgJson.put("images", images);
		Integer msgType = getMsgType();
		if(msgType!=null){
			msgJson.put("msgType", msgType);
		}else{
			msgJson.put("msgType", 0);
		}
		
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
		String createTime = getCreateTimeTime();
		try {
			msgJson.put("createTime", createTime);
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
		if (opType != null) {
			msgJson.put("opType", opType);
		} else {
			msgJson.put("opType", 10);
		}
		Long senderId = getSenderId();
		if (senderId != null) {
			msgJson.put("senderId", senderId);
		} else {
			msgJson.put("senderId", 0);
		}
		Integer senderType = getSenderType();
		if (senderType != null) {
			msgJson.put("senderType", senderType);
		} else {
			msgJson.put("senderType", 0);
		}

		Integer replyType = getReplyType();
		if (replyType != null) {
			msgJson.put("replyType", replyType);
		} else {
			msgJson.put("replyType", 0);
		}
		String replyContent = getReplyContent();
		if (replyContent != null) {
			replyContent = replyContent.replaceAll("(\r\n|\r|\n|\n\r)", "");
			msgJson.put("replyContent", replyContent);
		} else {
			msgJson.put("replyContent", "");
		}
		Integer roomType = getRoomType();
		if (roomType != null) {
			msgJson.put("roomType", roomType);
		} else {
			msgJson.put("roomType", 0);
		}
		String replyName = getReplyName();
		if (replyName != null) {
			msgJson.put("replyName", replyName);
		} else {
			msgJson.put("replyName", "admin");
		}
		Long praiseNumber = getPraiseNumber();
		if (praiseNumber != null) {
			msgJson.put("praiseNumber", praiseNumber);
		} else {
			msgJson.put("praiseNumber", 0);
		}
		
		String video = getVideos();
		if (video != null) {
			msgJson.put("video", video);
		} else {
			msgJson.put("video", "");
		}
		boolean b = getEmptyAll();
		if (b) {
			msgJson.put("emptyAll", 1);
		} else {
			msgJson.put("emptyAll", 0);
		}
		ILiveEventVo iLiveEventVo = getiLiveEventVo();
		if (iLiveEventVo != null) {
			msgJson.put("iLiveEventVo", iLiveEventVo.toJSONObject());
		} else {
			iLiveEventVo = new ILiveEventVo();
			msgJson.put("iLiveEventVo", new JSONObject());
		}
		String webContent = getWebContents();
		if (!StringUtils.isBlank(webContent)) {
			webContent = webContent.replaceAll("(\r\n|\r|\n|\n\r)", "");
			msgJson.put("webContent", webContent);
		} else {
			msgJson.put("webContent", "");
		}
		ILiveUserGift iLiveUserGift = getiLiveUserGift();
		if(iLiveUserGift!=null){
			msgJson.put("iLiveUserGift", iLiveUserGift.putGiftJson(null));
		}else{
			msgJson.put("iLiveUserGift", new JSONObject());
		}
		Integer connectType = getConnectType();
		if(null!=connectType) {
			msgJson.put("connectType", connectType);
		}else {
			msgJson.put("connectType", 0);
		}
		ILiveUserPlayRewards iLiveUserPlayRewards = getiLiveUserPlayRewards();
		if(iLiveUserPlayRewards!=null){
			msgJson.put("iLiveUserPlayRewards", iLiveUserPlayRewards.putJSONObject());
		}else{
			msgJson.put("iLiveUserPlayRewards", new JSONObject());
		}
		String welcomeLanguage2 = getWelcomeLanguage();
		if(welcomeLanguage2!=null){
			msgJson.put("welcomeLanguage", welcomeLanguage2);
		}else{
			msgJson.put("welcomeLanguage", "");
		}
		if(documentPageNO==null){
			msgJson.put("documentPageNO", 1);
		}else{
			msgJson.put("documentPageNO", documentPageNO);
		}
		msgJson.put("documentId", documentId==null?0:documentId);
		msgJson.put("documentManual", documentManual==null?0:documentManual);
		msgJson.put("documentDownload", documentDownload==null?0:documentDownload);
		return msgJson;
	}
	public ILiveMessage() {
		super();
	}

	public ILiveMessage(Long msgId, ILiveEvent live, ILiveIpAddress iLiveIpAddress) {
		super(msgId, live, iLiveIpAddress);
	}

	public ILiveMessage(Long msgId, ILiveEvent live, ILiveIpAddress iLiveIpAddress, String senderName,
			String msgOrginContent, String msgContent, Integer msgType, Timestamp createTime, Integer liveMsgType,
			String iMEI, Integer serviceType, String extra, Integer top, Integer opType, Long senderId,
			Integer senderType, Integer replyType, String replyContent, String replyName, Long praiseNumber,String images,String videos) {
		super(msgId, live, iLiveIpAddress, senderName, msgOrginContent, msgContent, msgType, createTime, liveMsgType,
				iMEI, serviceType, extra, top, opType, senderId, senderType, replyType, replyContent, replyName,
				praiseNumber, images, videos);
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Integer getDocumentDownload() {
		return documentDownload;
	}

	public void setDocumentDownload(Integer documentDownload) {
		this.documentDownload = documentDownload;
	}

	public Integer getDocumentManual() {
		return documentManual;
	}

	public void setDocumentManual(Integer documentManual) {
		this.documentManual = documentManual;
	}

}
