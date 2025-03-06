package com.bvRadio.iLive.iLive.action.front.finan;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILivePlayRewards;
import com.bvRadio.iLive.iLive.entity.ILiveUserPlayRewards;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePlayRewardsMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserPlayRewardsMng;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
/**
 * 打赏
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="rewards")
public class ILiveRoomPlayRewardsController {
	
	@Autowired
	private ILiveUserPlayRewardsMng iLiveUserPlayRewardsMng;
	
	@Autowired
	private ILivePlayRewardsMng iLivePlayRewardsMng;
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;//直播间
	@Autowired
	private ILiveManagerMng iLiveManagerMng;//用户
	
	private static final Logger log = LoggerFactory.getLogger(ILiveRoomPlayRewardsController.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 获取直播间打赏设置
	 * @param roomId
	 * @param terminalType
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/selectPlayRewards.jspx",method=RequestMethod.GET)
	public void selectPlayRewardsList(Integer roomId,String terminalType, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			if(roomId==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "直播间标示错误");
				return;
			}
			ILivePlayRewards iLivePlayRewards = iLivePlayRewardsMng.selectILivePlayRewardsById(roomId);
			if(iLivePlayRewards==null){
				iLivePlayRewards = new ILivePlayRewards();
				iLivePlayRewards.setRoomId(roomId);
				iLivePlayRewards.setRewardsSwitch(ILivePlayRewards.ROOM_REWARDS_SWITCH_OFF);
				iLivePlayRewards.setRewardsTitleType(ILivePlayRewards.ROOM_REWARDS_TITLE_Language);
				iLivePlayRewardsMng.addILivePlayRewards(iLivePlayRewards);
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取打赏信息成功");
			resultJson.put("data", iLivePlayRewards.putJSONObject());
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取打赏信息失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 打赏
	 * @param playRewardsAmount
	 * @param userId
	 * @param roomId
	 * @param terminalType
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/addPlayRewards.jspx",method=RequestMethod.GET)
	public void addPlayRewards(String playRewardsAmount,Long userId,Integer roomId,String terminalType, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			Double.parseDouble(playRewardsAmount);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "输入打赏金额不规范");
			ResponseUtils.renderJson(response, resultJson.toString());
			return;
		}
		try {
			if(roomId==null||userId==null||playRewardsAmount==null||playRewardsAmount.equals("")){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "数据错误");
				return;
			}
			ILivePlayRewards iLivePlayRewards = iLivePlayRewardsMng.selectILivePlayRewardsById(roomId);
			Double minAmount = 0.00; 
			try {
				String minRewardsAmount = iLivePlayRewards.getMinRewardsAmount()==null?"0":iLivePlayRewards.getMinRewardsAmount();
				minAmount = Double.parseDouble(minRewardsAmount);
			} catch (Exception e) {
				minAmount=0.00;
			}
			Double maxAmount = 0.00; 
			try {
				String maxRewardsAmount = iLivePlayRewards.getMaxRewardsAmount()==null?"0":iLivePlayRewards.getMaxRewardsAmount();
				maxAmount = Double.parseDouble(maxRewardsAmount);
			} catch (Exception e) {
				maxAmount=0.00;
			}
			double parseDouble = Double.parseDouble(playRewardsAmount);
			if(parseDouble>minAmount&&parseDouble<maxAmount){
				ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
				ILiveUserPlayRewards iLiveUserPlayRewards = new ILiveUserPlayRewards();
				ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
				iLiveUserPlayRewards.setEvenId(liveEvent.getLiveEventId());
				iLiveUserPlayRewards.setiLivePlayRewards(iLivePlayRewards);
				iLiveUserPlayRewards.setPlayRewardsAmount(playRewardsAmount);
				iLiveUserPlayRewards.setPlayRewardsTime(Timestamp.valueOf(format.format(new Date())));
				iLiveUserPlayRewards.setRoomId(roomId);
				iLiveUserPlayRewards.setRoomTitle(liveEvent.getLiveTitle());
				ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
				iLiveUserPlayRewards.setUserId(userId);
				iLiveUserPlayRewards.setUserName(iLiveManager.getNailName());
				iLiveUserPlayRewardsMng.addILiveUserPlayRewards(iLiveUserPlayRewards);
				iLiveUserPlayRewards.setPlayRewardsOperation(ILiveUserPlayRewards.ROOM_PLAY_REWARDS);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "打赏成功");
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
				ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
				if(concurrentHashMap!=null){
					Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
					ILiveMessage iLiveMessage = new ILiveMessage();
					iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_PLAY_REWARDS);
					iLiveMessage.setiLiveUserPlayRewards(iLiveUserPlayRewards);
					while (userIterator.hasNext()) {
						String key = userIterator.next();
						UserBean user = concurrentHashMap.get(key);
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
					}
				}
			}else{
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "打赏金额超出打赏范围");
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "打赏失败");
			log.error("sendMessage出错", e);
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 修改数据
	 * @param roomId 直播间ID
	 * @param rewardsSwitch 是否开启状态
	 * @param rewardsTitleType 宣传类型
	 * @param promotionalLanguage 宣传语
	 * @param promotionalImage 宣传图
	 * @param minRewardsAmount 最小金额
	 * @param maxRewardsAmount 最大金额
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/update/playRewards.jspx")
	public void updateILivePlayRewards(Integer roomId,Integer rewardsSwitch,
			Integer rewardsTitleType,String promotionalLanguage,String promotionalImage,
			String minRewardsAmount,String maxRewardsAmount,HttpServletRequest request, HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			Double.parseDouble(maxRewardsAmount);
			Double.parseDouble(minRewardsAmount);
		} catch (NumberFormatException e1) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "输入金额不规范");
			ResponseUtils.renderJson(request,response, resultJson.toString());
			return;
		}
		try {
			if(Double.parseDouble(maxRewardsAmount)<Double.parseDouble(minRewardsAmount)){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "最大金额必须大于最小金额");
				ResponseUtils.renderJson(request,response, resultJson.toString());
			}else{
				ILivePlayRewards iLivePlayRewards = new ILivePlayRewards();
				iLivePlayRewards.setRoomId(roomId);
				iLivePlayRewards.setRewardsSwitch(rewardsSwitch==null?ILivePlayRewards.ROOM_REWARDS_SWITCH_OFF:rewardsSwitch);
				iLivePlayRewards.setRewardsTitleType(rewardsTitleType==null?ILivePlayRewards.ROOM_REWARDS_TITLE_Language:rewardsTitleType);
				promotionalLanguage =promotionalLanguage==null?"":promotionalLanguage;
				promotionalLanguage = URLDecoder.decode(promotionalLanguage, "utf-8");
				iLivePlayRewards.setPromotionalLanguage(promotionalLanguage);
				iLivePlayRewards.setPromotionalImage(promotionalImage);
				iLivePlayRewards.setMinRewardsAmount(minRewardsAmount);
				iLivePlayRewards.setMaxRewardsAmount(maxRewardsAmount);
				iLivePlayRewardsMng.updateILivePlayRewards(iLivePlayRewards);
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
				ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
				if(concurrentHashMap!=null){
					Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
					ILiveMessage iLiveMessage = new ILiveMessage();
					iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_PLAY_REWARDS);
					ILiveUserPlayRewards iLiveUserPlayRewards = new ILiveUserPlayRewards();
					iLiveUserPlayRewards.setiLivePlayRewards(iLivePlayRewards);
					iLiveUserPlayRewards.setPlayRewardsOperation(ILiveUserPlayRewards.ROOM_PLAY_REWARDS_SETUP);
					iLiveMessage.setiLiveUserPlayRewards(iLiveUserPlayRewards);
					while (userIterator.hasNext()) {
						String key = userIterator.next();
						UserBean user = concurrentHashMap.get(key);
						List<ILiveMessage> msgList = user.getMsgList();
						if (msgList == null) {
							msgList = new ArrayList<ILiveMessage>();
						}
						msgList.add(iLiveMessage);
					}
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "成功");
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
}
