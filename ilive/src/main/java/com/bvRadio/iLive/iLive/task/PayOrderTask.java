package com.bvRadio.iLive.iLive.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.bvRadio.iLive.iLive.action.util.PayOrderUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthenticationRecord;
import com.bvRadio.iLive.iLive.entity.ILiveGift;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILivePayOrder;
import com.bvRadio.iLive.iLive.entity.ILivePlayRewards;
import com.bvRadio.iLive.iLive.entity.ILiveUserGift;
import com.bvRadio.iLive.iLive.entity.ILiveUserPlayRewards;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationRecordMng;
import com.bvRadio.iLive.iLive.manager.ILiveGiftMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePlayRewardsMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserGiftMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserPlayRewardsMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewAuthBillMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.PostMan;

public class PayOrderTask extends TimerTask{
	ExecutorService newFixedThreadPool = null;

	public PayOrderTask() {
		newFixedThreadPool = Executors.newFixedThreadPool(10);
	}
	@Override
	public void run() {
		Hashtable<Integer, List<ILivePayOrder>> orderListMap = PayOrderUtil.getOrderListMap();
		if (!orderListMap.isEmpty()) {
			Iterator<Integer> roomIds = orderListMap.keySet().iterator();
			while (roomIds.hasNext()) {
				Integer roomId = roomIds.next();
				List<ILivePayOrder> list = orderListMap.get(roomId);
				if (list == null) {
					list = new ArrayList<ILivePayOrder>();
				}
				if (!list.isEmpty()) {
					System.out.println("====订单数量============"+list.size());
					this.checkUserMsg(list, roomId);
					orderListMap.put(roomId, new ArrayList<ILivePayOrder>());
				}	
			}
		}
		
	}
	private static final Logger log = LoggerFactory.getLogger(AuditMessageTimer.class);

	private void checkUserMsg(final List<ILivePayOrder> list, final Integer roomId) {
		newFixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
				boolean ret = true;
				while (ret) {
					if (list.size() > 0) {
						List<ILivePayOrder> orders = new ArrayList<ILivePayOrder>();
						for (ILivePayOrder iLivePayOrder : list) {
							Integer orderType = iLivePayOrder.getOrderType();
							Integer orderUp = iLivePayOrder.getOrderUp();
							if(orderUp.equals(ILivePayOrder.ORDER_UP_YES)){
								ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
								//流水记录
								Integer roomId = iLivePayOrder.getRoomId();
								Long amount = iLivePayOrder.getAmount()==null?0:iLivePayOrder.getAmount();
								ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
								ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
								ILiveEnterprise enterprise = liveEvent.getEnterprise();
								Integer enterpriseId = enterprise.getEnterpriseId();
								String enterpriseName = enterprise.getEnterpriseName();
								ILiveManagerMng iLiveManagerMng = (ILiveManagerMng) applicationContext.getBean("iLiveManagerMng");
								ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(iLivePayOrder.getUserId());
								String params = "flowPrice="+Double.valueOf(amount/100+"."+amount%100)+"&flowType="+iLivePayOrder.getOrderType()+"&userId="+iLivePayOrder.getUserId()+
										"&userName="+iLivePayOrder.getUserName()+"&enterpriseId="+enterpriseId+"&enterpriseName="+enterpriseName+"&roomId="+roomId+"&evenId="+iLivePayOrder.getEvenId()+
										"&flowDesc="+iLivePayOrder.getPayDesc()+"&flagId="+iLivePayOrder.getContentId()+"&mobile="+iLiveManager.getMobile();
								String url = ConfigUtils.get("liveBilling_revenue_flow");
								try {
									String postJson = PostMan.downloadUrl(url+"?"+params);
									System.out.println("流水记录结果:"+postJson);
									JSONObject jsonObject = new JSONObject(postJson);
									Integer code = (Integer) jsonObject.get("code");
									if(code==1){
										orders.add(iLivePayOrder);
									}
								} catch (JSONException e) {
									System.out.println("================记录流水失败。。。。。。");
								}
							}else{
								if(orderType==ILivePayOrder.ORDER_TYPE_GIFT){
									//礼物
									String payDesc = iLivePayOrder.getPayDesc();
									String[] split = payDesc.split("*");
									Integer giftNumber = 1;
									if(split.length==2){
										try {
											giftNumber = Integer.valueOf(split[1]);
										} catch (Exception e) {
											giftNumber = 1;
										}
									}
									boolean b = saveILiveUserGift(iLivePayOrder.getRoomId(), iLivePayOrder.getUserId(), iLivePayOrder.getContentId(), giftNumber,applicationContext);
									if(b){
										iLivePayOrder.setOrderUp(ILivePayOrder.ORDER_UP_YES);
										ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
										//流水记录
										Integer roomId = iLivePayOrder.getRoomId();
										Long amount = iLivePayOrder.getAmount()==null?0:iLivePayOrder.getAmount();
										ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
										ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
										ILiveEnterprise enterprise = liveEvent.getEnterprise();
										Integer enterpriseId = enterprise.getEnterpriseId();
										String enterpriseName = enterprise.getEnterpriseName();
										ILiveManagerMng iLiveManagerMng = (ILiveManagerMng) applicationContext.getBean("iLiveManagerMng");
										ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(iLivePayOrder.getUserId());
										String params = "flowPrice="+Double.valueOf(amount/100+"."+amount%100)+"&flowType="+iLivePayOrder.getOrderType()+"&userId="+iLivePayOrder.getUserId()+
												"&userName="+iLivePayOrder.getUserName()+"&enterpriseId="+enterpriseId+"&enterpriseName="+enterpriseName+"&roomId="+roomId+"&evenId="+iLivePayOrder.getEvenId()+
												"&flowDesc="+iLivePayOrder.getPayDesc()+"&flagId="+iLivePayOrder.getContentId()+"&mobile="+iLiveManager.getMobile();
										String url = ConfigUtils.get("liveBilling_revenue_flow");
										try {
											String postJson = PostMan.downloadUrl(url+"?"+params);
											System.out.println("流水记录结果:"+postJson);
											JSONObject jsonObject = new JSONObject(postJson);
											Integer code = (Integer) jsonObject.get("code");
											if(code==1){
												orders.add(iLivePayOrder);
											}
										} catch (JSONException e) {
											System.out.println("================记录流水失败。。。。。。");
										}
									}
								}else if(orderType==ILivePayOrder.ORDER_TYPE_PLAY_REWARDS){
									//打赏
									boolean b = addPlayRewards(String.valueOf(iLivePayOrder.getAmount()*100), iLivePayOrder.getUserId(), iLivePayOrder.getRoomId(),applicationContext);
									if(b){
										iLivePayOrder.setOrderUp(ILivePayOrder.ORDER_UP_YES);
										ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
										//流水记录
										Integer roomId = iLivePayOrder.getRoomId();
										Long amount = iLivePayOrder.getAmount()==null?0:iLivePayOrder.getAmount();
										ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
										ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
										ILiveEnterprise enterprise = liveEvent.getEnterprise();
										Integer enterpriseId = enterprise.getEnterpriseId();
										String enterpriseName = enterprise.getEnterpriseName();
										ILiveManagerMng iLiveManagerMng = (ILiveManagerMng) applicationContext.getBean("iLiveManagerMng");
										ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(iLivePayOrder.getUserId());
										String params = "flowPrice="+Double.valueOf(amount/100+"."+amount%100)+"&flowType="+iLivePayOrder.getOrderType()+"&userId="+iLivePayOrder.getUserId()+
												"&userName="+iLivePayOrder.getUserName()+"&enterpriseId="+enterpriseId+"&enterpriseName="+enterpriseName+"&roomId="+roomId+"&evenId="+iLivePayOrder.getEvenId()+
												"&flowDesc="+iLivePayOrder.getPayDesc()+"&flagId="+iLivePayOrder.getContentId()+"&mobile="+iLiveManager.getMobile();
										String url = ConfigUtils.get("liveBilling_revenue_flow");
										try {
											String postJson = PostMan.downloadUrl(url+"?"+params);
											System.out.println("流水记录结果:"+postJson);
											JSONObject jsonObject = new JSONObject(postJson);
											Integer code = (Integer) jsonObject.get("code");
											if(code==1){
												orders.add(iLivePayOrder);
											}
										} catch (JSONException e) {
											System.out.println("================记录流水失败。。。。。。");
										}
									}
								}else if (ILivePayOrder.ORDER_TYPE_LIVE.equals(orderType)) {
									boolean b = saveILiveViewAuthBill(iLivePayOrder,applicationContext);
									if(b){
										iLivePayOrder.setOrderUp(ILivePayOrder.ORDER_UP_YES);
										ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
										//流水记录
										Integer roomId = iLivePayOrder.getRoomId();
										Long amount = iLivePayOrder.getAmount()==null?0:iLivePayOrder.getAmount();
										ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
										ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
										ILiveEnterprise enterprise = liveEvent.getEnterprise();
										Integer enterpriseId = enterprise.getEnterpriseId();
										String enterpriseName = enterprise.getEnterpriseName();
										ILiveManagerMng iLiveManagerMng = (ILiveManagerMng) applicationContext.getBean("iLiveManagerMng");
										ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(iLivePayOrder.getUserId());
										String params = "flowPrice="+Double.valueOf(amount/100+"."+amount%100)+"&flowType="+iLivePayOrder.getOrderType()+"&userId="+iLivePayOrder.getUserId()+
												"&userName="+iLivePayOrder.getUserName()+"&enterpriseId="+enterpriseId+"&enterpriseName="+enterpriseName+"&roomId="+roomId+"&evenId="+iLivePayOrder.getEvenId()+
												"&flowDesc="+iLivePayOrder.getPayDesc()+"&flagId="+iLivePayOrder.getContentId()+"&mobile="+iLiveManager.getMobile();
										String url = ConfigUtils.get("liveBilling_revenue_flow");
										try {
											String postJson = PostMan.downloadUrl(url+"?"+params);
											System.out.println("流水记录结果:"+postJson);
											JSONObject jsonObject = new JSONObject(postJson);
											Integer code = (Integer) jsonObject.get("code");
											if(code==1){
												orders.add(iLivePayOrder);
											}
										} catch (JSONException e) {
											System.out.println("================记录流水失败。。。。。。");
										}
									}
								} else if (ILivePayOrder.ORDER_TYPE_MEDIA.equals(orderType)) {
									System.out.println("================点播观看");
									boolean b = saveILiveFileAuthentication(iLivePayOrder, applicationContext);
									if(b){
										iLivePayOrder.setOrderUp(ILivePayOrder.ORDER_UP_YES);
										ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
										//流水记录
										Integer roomId = iLivePayOrder.getRoomId();
										Long amount = iLivePayOrder.getAmount()==null?0:iLivePayOrder.getAmount();
										ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
										ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
										ILiveEnterprise enterprise = liveEvent.getEnterprise();
										Integer enterpriseId = enterprise.getEnterpriseId();
										String enterpriseName = enterprise.getEnterpriseName();
										ILiveManagerMng iLiveManagerMng = (ILiveManagerMng) applicationContext.getBean("iLiveManagerMng");
										ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(iLivePayOrder.getUserId());
										String params = "flowPrice="+Double.valueOf(amount/100+"."+amount%100)+"&flowType="+iLivePayOrder.getOrderType()+"&userId="+iLivePayOrder.getUserId()+
												"&userName="+iLivePayOrder.getUserName()+"&enterpriseId="+enterpriseId+"&enterpriseName="+enterpriseName+"&roomId="+roomId+"&evenId="+iLivePayOrder.getEvenId()+
												"&flowDesc="+iLivePayOrder.getPayDesc()+"&flagId="+iLivePayOrder.getContentId()+"&mobile="+iLiveManager.getMobile();
										String url = ConfigUtils.get("liveBilling_revenue_flow");
										try {
											String postJson = PostMan.downloadUrl(url+"?"+params);
											System.out.println("流水记录结果:"+postJson);
											JSONObject jsonObject = new JSONObject(postJson);
											Integer code = (Integer) jsonObject.get("code");
											if(code==1){
												orders.add(iLivePayOrder);
											}
										} catch (JSONException e) {
											System.out.println("================记录流水失败。。。。。。");
										}
									}else{
										System.out.println("================点播观看 失败");
									}
								}
							}
						}
						for (ILivePayOrder iLivePayOrder : orders) {
							list.remove(iLivePayOrder);
						}
					}else{
						ret = false;
					}
				}
			}
		});
	}
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public boolean saveILiveViewAuthBill(ILivePayOrder iLivePayOrder,ApplicationContext applicationContext){
		boolean ret = false; 
		try {
			ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
			ILiveViewAuthBillMng iLiveViewAuthBillMng = (ILiveViewAuthBillMng) applicationContext.getBean("iLiveViewAuthBillMng");
			Integer roomId = iLivePayOrder.getRoomId();
			ILiveLiveRoom liveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent liveEvent = liveRoom.getLiveEvent();
			Long liveEventId = liveEvent.getLiveEventId();
			Long userId = iLivePayOrder.getUserId();
			ILiveViewAuthBill authbill = new ILiveViewAuthBill();
			authbill.setAuthPassTime(new Timestamp(System.currentTimeMillis()));
			authbill.setAuthType(ILiveViewAuthBill.AUTH_TYPE_PAY);
			authbill.setDeleteStatus(0);
			authbill.setLiveEventId(liveEventId);
			authbill.setLiveRoomId(roomId);
			authbill.setUserId(String.valueOf(userId));
			iLiveViewAuthBillMng.addILiveViewAuthBill(authbill);
			ret = true;
		} catch (BeansException e) {
			ret = false; 
			log.error("付费观看", e);
		}
		return ret;
	}
	public boolean saveILiveFileAuthentication(ILivePayOrder iLivePayOrder,ApplicationContext applicationContext){
		boolean ret = false; 
		try {
			ILiveFileAuthenticationRecordMng iLiveFileAuthenticationRecordMng = (ILiveFileAuthenticationRecordMng) applicationContext.getBean("ILiveFileAuthenticationMng");
			Long contentId = iLivePayOrder.getContentId();
			Long userId = iLivePayOrder.getUserId();
			ILiveFileAuthenticationRecord fileAuthenticationRecord = new ILiveFileAuthenticationRecord();
			fileAuthenticationRecord.setAuthPassTime(new Timestamp(System.currentTimeMillis()));
			fileAuthenticationRecord.setAuthType(ILiveFileAuthenticationRecord.AUTH_TYPE_PAY);
			fileAuthenticationRecord.setDeleteStatus(0);
			fileAuthenticationRecord.setFileId(contentId);
			fileAuthenticationRecord.setUserId(userId);
			iLiveFileAuthenticationRecordMng.addILiveViewAuthBill(fileAuthenticationRecord);
			ret = true;
		} catch (BeansException e) {
			ret = false; 
			log.error("付费观看", e);
		}
		return ret;
	}
	/**
	 * 礼物
	 * @param roomId
	 * @param userId
	 * @param giftId
	 * @param giftNumber
	 * @param applicationContext
	 * @return
	 */
    public boolean saveILiveUserGift(Integer roomId,Long userId,Long giftId,Integer giftNumber,ApplicationContext applicationContext){
    	boolean ret =false;
		try {
			ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
			ILiveGiftMng iLiveGiftMng = (ILiveGiftMng) applicationContext.getBean("iLiveGiftMng");
			ILiveManagerMng iLiveManagerMng = (ILiveManagerMng) applicationContext.getBean("iLiveManagerMng");
			ILiveUserGiftMng iLiveUserGiftMng = (ILiveUserGiftMng) applicationContext.getBean("iLiveUserGiftMng");
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveGift iLiveGift = iLiveGiftMng.selectIliveGiftByGiftId(giftId);
			ILiveUserGift iLiveUserGift = new ILiveUserGift();
			ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
			iLiveUserGift.setEvenId(liveEvent.getLiveEventId());
			iLiveUserGift.setGiftId(giftId);
			iLiveUserGift.setGiftImage(iLiveGift.getGiftImage());
			iLiveUserGift.setGiftName(iLiveGift.getGiftName());
			iLiveUserGift.setGiftNumber(giftNumber);
			iLiveUserGift.setGiftPrice(iLiveGift.getGiftPrice());
			iLiveUserGift.setGiveTime(Timestamp.valueOf(format.format(new Date())));
			iLiveUserGift.setRoomId(roomId);
			iLiveUserGift.setRoomTitle(liveEvent.getLiveTitle());
			ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
			iLiveUserGift.setUserId(userId);
			iLiveUserGift.setUserName(iLiveManager.getNailName());
			iLiveUserGiftMng.addILiveUserGift(iLiveUserGift);
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			if(concurrentHashMap!=null){
				Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
				ILiveMessage iLiveMessage = new ILiveMessage();
				iLiveMessage.setRoomType(ILiveMessage.ROOM_TYPE_GIFT);
				iLiveUserGift.setGiftOperation(ILiveUserGift.ILIVE_GIFT_ADD);
				iLiveMessage.setiLiveUserGift(iLiveUserGift);
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
			ret = true;
		} catch (Exception e) {
			ret = false;
			log.error("礼物发送错误", e);
		}
		return ret;
	}
    /**
     * 打赏
     * @param playRewardsAmount
     * @param userId
     * @param roomId
     * @param applicationContext
     * @return
     */
    public boolean addPlayRewards(String playRewardsAmount,Long userId,Integer roomId,ApplicationContext applicationContext) {
    	boolean ret = false;
		try {
			Double.parseDouble(playRewardsAmount);
		} catch (Exception e) {
			log.error("打赏金额错误", e);
			System.out.println("playRewardsAmount："+playRewardsAmount);
			ret = false;
		}
		try {
			ILivePlayRewardsMng iLivePlayRewardsMng = (ILivePlayRewardsMng) applicationContext.getBean("iLivePlayRewardsMng");
			ILiveLiveRoomMng iLiveLiveRoomMng = (ILiveLiveRoomMng) applicationContext.getBean("iLiveLiveRoomMng");
			ILiveManagerMng iLiveManagerMng = (ILiveManagerMng) applicationContext.getBean("iLiveManagerMng");
			ILiveUserPlayRewardsMng iLiveUserPlayRewardsMng = (ILiveUserPlayRewardsMng) applicationContext.getBean("iLiveUserPlayRewardsMng");
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
				ret = true;
			}else{
				ret = false;
				log.error("打赏金额超出范围");
			}
		} catch (Exception e) {
			log.error("打赏错误", e);
			ret = false;
		}
		return ret;
	}
    
}
