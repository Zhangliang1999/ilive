package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveVoteActivity;
import com.bvRadio.iLive.iLive.entity.ILiveVotePeople;
import com.bvRadio.iLive.iLive.entity.ILiveVoteRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveVotePeopleMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteRoomMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="vote")
public class ILiveVotePhoneController {

	@Autowired
	private ILiveVoteActivityMng iLiveVoteActivityMng;	//投票活动
	
	@Autowired
	private ILiveVotePeopleMng iLiveVotePeopleMng;		//用户投票记录
	
	@Autowired
	private ILiveVoteRoomMng iLiveVoteRoomMng;
	
	@Autowired
	private ILiveLiveRoomMng iliveRoomMng;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	
	/**
	 * 获取直播间已开启的和没有开启过的投票活动
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getVoteList.jspx")
	public void getSwitchList(Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			ILiveLiveRoom room = iliveRoomMng.findById(roomId);
			List<ILiveVoteActivity> list = null;
			
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.valueOf(user.getUserId()));

			
			//查询子账户是否具有图片查看全部
			boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_voteActivity);
			
			if (iLiveManager.getLevel()!=null&&iLiveManager.getLevel()!=0&&!per) {
				list = iLiveVoteActivityMng.getH5VoteListByUserId(Long.valueOf(user.getUserId()));
			}
			
			if (room!=null&&room.getEnterpriseId()!=null) {
				list = iLiveVoteActivityMng.getH5VoteListByEnterpriseId(room.getEnterpriseId());
			}
			
			ILiveVoteRoom iLiveVoteRoom = iLiveVoteRoomMng.getStartByRoomId(roomId);
			if (iLiveVoteRoom!=null) {
				Long voteId = iLiveVoteRoom.getVoteId();
				if (list!=null&&list.size()>0) {
					Iterator<ILiveVoteActivity> iterator = list.iterator();
					while (iterator.hasNext()) {
						ILiveVoteActivity iLiveVoteActivity = (ILiveVoteActivity) iterator.next();
						if (iLiveVoteActivity.getId().equals(voteId)) {
							iLiveVoteActivity.setIsSwitch(1);
						}
					}
				}
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONArray jsonArray = new JSONArray();
			if(list!=null&&list.size()>0) {
				for(ILiveVoteActivity activity:list) {
					JSONObject obj = JSONObject.fromObject(activity);
					obj.put("startTime", format.format(activity.getStartTime()));
					obj.put("endTime", format.format(activity.getEndTime()));
					jsonArray.add(obj);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取投票列表成功");
			resultJson.put("data",jsonArray.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取投票列表失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 根据投票活动id和开启关闭标识修改
	 * @param voteId
	 * @param isSwitch
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="switchVote.jspx")
	public void switchVote(Integer roomId,Long voteId,Integer isSwitch,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONObject res = new JSONObject();
			ILiveVoteActivity activity = iLiveVoteActivityMng.getById(voteId);
			activity.setIsSwitch(isSwitch);
			if(isSwitch == 1) {
				activity.setIsBeforeSwitch(1);
			}
			iLiveVoteActivityMng.update(activity);
			
			ILiveVoteRoom iLiveVoteRoom = iLiveVoteRoomMng.selectByRoomIdAndVoteId(roomId, voteId);
			if (iLiveVoteRoom==null) {
				iLiveVoteRoom = new ILiveVoteRoom();
				iLiveVoteRoom.setRoomId(roomId);
				iLiveVoteRoom.setIsOpen(isSwitch);
				iLiveVoteRoom.setVoteId(voteId);
				iLiveVoteRoomMng.save(iLiveVoteRoom);
			}else {
				iLiveVoteRoom.setIsOpen(isSwitch);
				iLiveVoteRoomMng.update(iLiveVoteRoom);
			}
			
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(6);
			if("open".equals(ConfigUtils.get("redis_service"))) {
				iLiveMessage.setMsgId(Long.parseLong("-"+roomId+""+roomId));
				
				JedisUtils.delObject("msg:"+iLiveMessage.getMsgId());
				JedisUtils.setByte(("msg:"+iLiveMessage.getMsgId()).getBytes(), SerializeUtil.serialize(iLiveMessage), 0);
				Set<String> userIdList =JedisUtils.getSet("userIdList"+roomId);
				if(userIdList!=null&&userIdList.size()!=0) {
					for(String userId:userIdList) {
						boolean flag=true;
						while (flag) {
							String requestionIdString=UUID.randomUUID().toString();
							if(JedisUtils.tryGetDistributedLock(userId+"lock", requestionIdString, 1)) {
								JedisUtils.listAdd(roomId+":"+userId, iLiveMessage.getMsgId()+"");
								flag=false;
								JedisUtils.releaseDistributedLock(userId+"lock", requestionIdString);
							}else {
								try {
									Thread.sleep(100);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						}
					}
				}
				
			}else {
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
			resultJson.put("message", "修改成功");
			resultJson.put("data",res.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 获取直播间已开启投票活动
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getVote.jspx")
	public void getSwitch(Integer roomId,Long userId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveVoteActivity activity = null;
			//ILiveLiveRoom room = iliveRoomMng.findById(roomId);
//			if (room!=null&&room.getEnterpriseId()!=null) {
//				activity = iLiveVoteActivityMng.getH5Vote2(room.getEnterpriseId());
//			}
			
			boolean startVote = iLiveVoteRoomMng.isStartVote(roomId);
			if (startVote) {
				ILiveVoteRoom iLiveVoteRoom = iLiveVoteRoomMng.getStartByRoomId(roomId);
				activity = iLiveVoteActivityMng.getById(iLiveVoteRoom.getVoteId());
			}
			
			JSONObject res = JSONObject.fromObject(activity);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = format.format(activity.getStartTime());
			String endTime = format.format(activity.getEndTime());
			res.put("startTime", startTime);
			res.put("endTime", endTime);
			
			//该用户是否投过票
			List<ILiveVotePeople> list = iLiveVotePeopleMng.getListByUserId(userId, activity.getId());
			
			if(list.isEmpty()) {
				Date date = new Date();
				if(date.getTime()>activity.getStartTime().getTime()&&date.getTime()<activity.getEndTime().getTime()) {
					//进行中
					resultJson.put("voteStatus", 0);
				}else if(date.getTime()<activity.getStartTime().getTime()) {
					//未开始
					resultJson.put("voteStatus", 1);
				}else if(date.getTime()>activity.getEndTime().getTime()) {
					//抽奖结束
					resultJson.put("voteStatus", 2);
				}
			}else {
				//该用户已参与过投票
				resultJson.put("voteStatus", 3);
			}
			
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取投票信息成功");
			resultJson.put("data",res.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取投票信息失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 添加一条投票记录
	 * @param userId
	 * @param problemAnswers
	 * @param voteId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="addVote.jspx")
	public void vote(Long userId,String problemAnswers,Long voteId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveVoteActivityMng.vote(voteId,userId,problemAnswers);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "投票成功");
			resultJson.put("data", new JSONObject());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "投票失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 获取投票结果  各选项选择人数和百分比
	 * @param voteId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getResult.jspx")
	public void getResult(Long voteId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveVoteActivity result = iLiveVoteActivityMng.getResult(voteId);
			JSONObject res = JSONObject.fromObject(result);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "投票成功");
			resultJson.put("data", res.toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "投票失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
}
