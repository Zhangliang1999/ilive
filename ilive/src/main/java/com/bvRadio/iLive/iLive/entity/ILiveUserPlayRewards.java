package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveUserPlayRewards;
/**
 * 打赏记录
 * @author YanXL
 *
 */
@SuppressWarnings("serial")
public class ILiveUserPlayRewards extends BaseILiveUserPlayRewards {
	/**
	 * 打赏操作  0 打赏设置
	 */
	public static final Integer ROOM_PLAY_REWARDS_SETUP=0;
	/**
	 * 打赏操作  1 打赏
	 */
	public static final Integer ROOM_PLAY_REWARDS=1;
	/**
	 * 打赏操作  0 打赏设置     1 打赏
	 */
	private Integer playRewardsOperation;
	/**
	 * 打赏设置
	 */
	private ILivePlayRewards iLivePlayRewards;

	public ILivePlayRewards getiLivePlayRewards() {
		return iLivePlayRewards;
	}
	public Integer getPlayRewardsOperation() {
		return playRewardsOperation;
	}
	public void setPlayRewardsOperation(Integer playRewardsOperation) {
		this.playRewardsOperation = playRewardsOperation;
	}
	public void setiLivePlayRewards(ILivePlayRewards iLivePlayRewards) {
		this.iLivePlayRewards = iLivePlayRewards;
	}
	
	public JSONObject putJSONObject(){
		JSONObject jsonObject = new JSONObject();
		Integer playRewardsOperation2 = getPlayRewardsOperation();
		jsonObject.put("playRewardsOperation", playRewardsOperation2==null?ROOM_PLAY_REWARDS_SETUP:playRewardsOperation2);
		Integer roomId = getRoomId();
		jsonObject.put("roomId", roomId==null?0:roomId);
		Long evenId = getEvenId();
		jsonObject.put("evenId", evenId==null?0:evenId);
		Long id = getId();
		jsonObject.put("id", id==null?0:id);
		ILivePlayRewards iLivePlayRewards = getiLivePlayRewards();
		jsonObject.put("iLivePlayRewards", iLivePlayRewards==null?new JSONObject():iLivePlayRewards.putJSONObject());
		String playRewardsAmount = getPlayRewardsAmount();
		jsonObject.put("playRewardsAmount", playRewardsAmount==null?"0.00":playRewardsAmount);
		Timestamp playRewardsTime = getPlayRewardsTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonObject.put("playRewardsTime", playRewardsTime==null?format.format(new Date()):format.format(playRewardsTime));
		String roomTitle = getRoomTitle();
		jsonObject.put("roomTitle", roomTitle==null?"":roomTitle);
		Long userId = getUserId();
		jsonObject.put("userId", userId==null?0:userId);
		String userName = getUserName();
		jsonObject.put("userName", userName==null?"":userName);
		return jsonObject;
	}
}
