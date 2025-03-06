package com.bvRadio.iLive.iLive.entity;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILivePlayRewards;

@SuppressWarnings("serial")
public class ILivePlayRewards extends BaseILivePlayRewards {
	/**
	 * 打赏功能开关 0 关闭 
	 */
	public static final Integer ROOM_REWARDS_SWITCH_OFF=0;
	/**
	 * 打赏功能开关  1 开启 
	 */
	public static final Integer ROOM_REWARDS_SWITCH_ON=1;
	/**
	 * 打赏宣传标题 类型   0 宣传语  
	 */
	public static final Integer ROOM_REWARDS_TITLE_Language=0;
	/**
	 * 打赏宣传标题 类型     1 宣传图
	 */
	public static final Integer ROOM_REWARDS_TITLE_IMAGE=1;
		
	public JSONObject putJSONObject(){
		JSONObject jsonObject = new JSONObject();
		Integer roomId = getRoomId();
		jsonObject.put("roomId", roomId==null?0:roomId);
		Integer rewardsTitleType = getRewardsTitleType();
		jsonObject.put("rewardsTitleType", rewardsTitleType==null?ROOM_REWARDS_TITLE_Language:rewardsTitleType);
		String promotionalLanguage = getPromotionalLanguage();
		jsonObject.put("promotionalLanguage", promotionalLanguage==null?"欢迎打赏":promotionalLanguage);
		String promotionalImage = getPromotionalImage();
		jsonObject.put("promotionalImage", promotionalImage==null?"":promotionalImage);
		Integer rewardsSwitch = getRewardsSwitch();
		jsonObject.put("rewardsSwitch", rewardsSwitch==null?ROOM_REWARDS_SWITCH_OFF:rewardsSwitch);
		String minRewardsAmount = getMinRewardsAmount();
		jsonObject.put("minRewardsAmount", minRewardsAmount==null?"0.00":minRewardsAmount);
		String maxRewardsAmount = getMaxRewardsAmount();
		jsonObject.put("maxRewardsAmount", maxRewardsAmount==null?"0.00":maxRewardsAmount);
		return jsonObject;
	}

}
