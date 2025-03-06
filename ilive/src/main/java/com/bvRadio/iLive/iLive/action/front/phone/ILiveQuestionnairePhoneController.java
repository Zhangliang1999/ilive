package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivity;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivityStatistic;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireOption;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireProblem;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnairePeopleMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireStatisticMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="questionnaire")
public class ILiveQuestionnairePhoneController {

	@Autowired
	private ILiveQuestionnaireActivityMng iLiveQuestionnaireActivityMng;	//投票活动
	
	@Autowired
	private ILiveQuestionnairePeopleMng iLiveQuestionnairePeopleMng;		//用户投票记录
	
	@Autowired
	private ILiveQuestionnaireRoomMng iLiveQuestionnaireRoomMng;
	
	@Autowired
	private ILiveLiveRoomMng iliveRoomMng;
	
	@Autowired
	private ILiveQuestionnaireStatisticMng iLiveQuestionnaireStatisticMng; //用户作答或浏览记录
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	
	/**
	 * 获取直播间已开启的和没有开启过的问卷活动
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getQuestionnaireList.jspx")
	public void getSwitchList(Integer roomId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			ILiveLiveRoom room = iliveRoomMng.findById(roomId);
			List<ILiveQuestionnaireActivity> list = null;
			
//			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.valueOf(user.getUserId()));

			
//			//查询子账户是否具有图片查看全部
//			boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_QuestionnaireActivity);
//			
//			if (iLiveManager.getLevel()!=null&&iLiveManager.getLevel()!=0&&!per) {
//				list = iLiveQuestionnaireActivityMng.getH5QuestionnaireListByUserId(Long.valueOf(user.getUserId()));
//			}
			
			if (room!=null&&room.getEnterpriseId()!=null) {
				list = iLiveQuestionnaireActivityMng.getH5QuestionnaireListByEnterpriseId(room.getEnterpriseId());
			}
			
			ILiveQuestionnaireRoom iLiveQuestionnaireRoom = iLiveQuestionnaireRoomMng.getStartByRoomId(roomId);
			if (iLiveQuestionnaireRoom!=null) {
				Long QuestionnaireId = iLiveQuestionnaireRoom.getQuestionnaireId();
				if (list!=null&&list.size()>0) {
					Iterator<ILiveQuestionnaireActivity> iterator = list.iterator();
					while (iterator.hasNext()) {
						ILiveQuestionnaireActivity iLiveQuestionnaireActivity = (ILiveQuestionnaireActivity) iterator.next();
						if (iLiveQuestionnaireActivity.getId().equals(QuestionnaireId)) {
							iLiveQuestionnaireActivity.setIsSwitch(1);
						}
					}
				}
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONArray jsonArray = new JSONArray();
			if(list!=null&&list.size()>0) {
				for(ILiveQuestionnaireActivity activity:list) {
					JSONObject obj = JSONObject.fromObject(activity);
					obj.put("startTime", format.format(activity.getStartTime()));
					obj.put("endTime", format.format(activity.getEndTime()));
					jsonArray.add(obj);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取问卷列表成功");
			resultJson.put("data",jsonArray.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取问卷列表失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 根据问卷活动id和开启关闭标识修改
	 * @param QuestionnaireId
	 * @param isSwitch
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="switchQuestionnaire.jspx")
	public void switchQuestionnaire(Integer roomId,Long QuestionnaireId,Integer isSwitch,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try { 
			JSONObject res = new JSONObject();
			ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
			activity.setIsSwitch(isSwitch);
			if(isSwitch == 1) {
				activity.setIsBeforeSwitch(1);
			}
			iLiveQuestionnaireActivityMng.update(activity);
			
			ILiveQuestionnaireRoom iLiveQuestionnaireRoom = iLiveQuestionnaireRoomMng.selectByRoomIdAndQuestionnaireId(roomId, QuestionnaireId);
			if (iLiveQuestionnaireRoom==null) {
				iLiveQuestionnaireRoom = new ILiveQuestionnaireRoom();
				iLiveQuestionnaireRoom.setRoomId(roomId);
				iLiveQuestionnaireRoom.setIsOpen(isSwitch);
				iLiveQuestionnaireRoom.setQuestionnaireId(QuestionnaireId);
				iLiveQuestionnaireRoomMng.save(iLiveQuestionnaireRoom);
			}else {
				iLiveQuestionnaireRoom.setIsOpen(isSwitch);
				iLiveQuestionnaireRoomMng.update(iLiveQuestionnaireRoom);
			}
			
			ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(roomId);
			Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(19);
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
	 * 获取直播间已开启问卷活动
	 * @param roomId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getQuestionnaire.jspx")
	public void getSwitch(Integer roomId,Long userId,String mobile,HttpServletRequest request,HttpServletResponse response,Model model) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getAppUser(request);
//		if(userId==null){
//			if(user==null){
//				resultJson.put("code", 401 );
//				resultJson.put("message", "用户未登录");
//				resultJson.put("data", new JSONObject());
//			}else{
//				userId=Long.parseLong(user.getUserId());
//			}
//		}
		try {
			ILiveQuestionnaireActivity activity = null;
			//ILiveLiveRoom room = iliveRoomMng.findById(roomId);
//			if (room!=null&&room.getEnterpriseId()!=null) {
//				activity = iLiveQuestionnaireActivityMng.getH5Questionnaire2(room.getEnterpriseId());
//			}
			
			boolean startQuestionnaire = iLiveQuestionnaireRoomMng.isStartQuestionnaire(roomId);
			if (startQuestionnaire) {
				ILiveQuestionnaireRoom iLiveQuestionnaireRoom = iLiveQuestionnaireRoomMng.getStartByRoomId(roomId);
				activity = iLiveQuestionnaireActivityMng.getById(iLiveQuestionnaireRoom.getQuestionnaireId());
			}
			
			JSONObject res = JSONObject.fromObject(activity);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = format.format(activity.getStartTime());
			String endTime = format.format(activity.getEndTime());
			res.put("startTime", startTime);
			res.put("endTime", endTime);
			
			//该用户是否答过卷
			List<ILiveQuestionnairePeople> list = iLiveQuestionnairePeopleMng.getListByUserId(userId,mobile,activity.getId());
			
			if(list.isEmpty()) {
				Date date = new Date();
				if(date.getTime()>activity.getStartTime().getTime()&&date.getTime()<activity.getEndTime().getTime()) {
					//进行中
					resultJson.put("QuestionnaireStatus", 0);
				}else if(date.getTime()<activity.getStartTime().getTime()) {
					//未开始
					resultJson.put("QuestionnaireStatus", 1);
				}else if(date.getTime()>activity.getEndTime().getTime()) {
					//抽奖结束
					resultJson.put("QuestionnaireStatus", 2);
				}
			}else {
				//该用户已参与过问卷
				resultJson.put("QuestionnaireStatus", 3);
			}
			model.addAttribute("activity", activity);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取问卷信息成功");
			resultJson.put("data",res.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取问卷信息失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 添加一条答卷记录
	 * @param userId
	 * @param problemAnswers
	 * @param QuestionnaireId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="addQuestionnaire.jspx")
	public void Questionnaire(Long userId,String problemAnswers,String questionAnswers,Long QuestionnaireId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveQuestionnaireActivityMng.Questionnaire(QuestionnaireId,userId,problemAnswers,questionAnswers);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "答卷成功");
			resultJson.put("data", new JSONObject());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "答卷失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 添加一条答卷记录
	 * @param userId
	 * @param problemAnswers
	 * @param QuestionnaireId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="saveQuestionnaireAnswer.jspx")
	public void saveQuestionnaireAnswer(Long userId,Long questionnaireId,String IdCard,String mobile,String strList,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getAppUser(request);
		if(userId==null){
			if(user==null){
				resultJson.put("code", 401 );
				resultJson.put("message", "用户未登录");
				resultJson.put("data", new JSONObject());
			}else{
				userId=Long.parseLong(user.getUserId());
			}
		}
		ILiveManager manager=iLiveManagerMng.selectILiveManagerById(userId);
		if(manager!=null){
			if(mobile==null){
				mobile=manager.getMobile();
			}
		}
		try {
			//答卷记录加一
			ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(questionnaireId);
			activity.setNumber(activity.getNumber()+1);
			iLiveQuestionnaireActivityMng.update(activity);
			//记录当前答卷提交时间
			ILiveQuestionnaireActivityStatistic statistic= iLiveQuestionnaireStatisticMng.getListByUserId(userId, questionnaireId);
			if(statistic==null){
				//添加记录
				ILiveQuestionnaireActivityStatistic jilu=new ILiveQuestionnaireActivityStatistic();
				Long id=iLiveQuestionnaireStatisticMng.maxId();
				jilu.setId(id);
				jilu.setQuestionnaireId(questionnaireId);
				jilu.setUserId(userId);
				jilu.setStartTime(new Timestamp(System.currentTimeMillis()-3600000));
				jilu.setEndTime(new Timestamp(System.currentTimeMillis()));
				iLiveQuestionnaireStatisticMng.save(jilu);
			}else{
				statistic.setEndTime(new Timestamp(System.currentTimeMillis()));
				iLiveQuestionnaireStatisticMng.update(statistic);
			}
			JSONArray jsonArray = JSONArray.fromObject(strList);
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				
					JSONArray arrOption = obj.getJSONArray("list");
					for(int j=0;j<arrOption.size();j++) {
						JSONObject objOption = arrOption.getJSONObject(j);
						if(obj.getInt("style")!=3){
						String []options=objOption.getString("checkdId").split("_");
						for(String option : options){
							ILiveQuestionnairePeople iLiveQuestionnairePeople=new ILiveQuestionnairePeople();
							iLiveQuestionnairePeople.setQuestionnaireId(questionnaireId);
							iLiveQuestionnairePeople.setQuestionnaireProblemId(obj.getLong("problemId"));
							iLiveQuestionnairePeople.setIdCard(IdCard);
							iLiveQuestionnairePeople.setMobile(mobile);
							iLiveQuestionnairePeople.setUserName(manager.getUserName()==null?mobile:manager.getUserName());
							iLiveQuestionnairePeople.setQuestionnaireOptionId(Long.parseLong(option));
							iLiveQuestionnairePeople.setUserId(userId);
							iLiveQuestionnairePeopleMng.save(iLiveQuestionnairePeople);
						}
					}else{
						ILiveQuestionnairePeople iLiveQuestionnairePeople=new ILiveQuestionnairePeople();
						iLiveQuestionnairePeople.setQuestionnaireId(questionnaireId);
						iLiveQuestionnairePeople.setQuestionnaireProblemId(obj.getLong("problemId"));
						iLiveQuestionnairePeople.setIdCard(IdCard);
						iLiveQuestionnairePeople.setMobile(mobile);
						iLiveQuestionnairePeople.setUserName(manager.getUserName()==null?mobile:manager.getUserName());
						String answer=objOption.getString("answer");
						answer=new String(answer.trim().getBytes("ISO-8859-1"), "UTF-8"); 
						iLiveQuestionnairePeople.setAnswer(answer);
						iLiveQuestionnairePeople.setUserId(userId);
						iLiveQuestionnairePeopleMng.save(iLiveQuestionnairePeople);
					}
				}
				
				
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "答卷成功");
			resultJson.put("data", new JSONObject());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "答卷失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 开始答卷或者进入浏览时调用
	 */
	@RequestMapping(value="startQuestionnaire.jspx")
	public void startQuestionnaire(Integer roomId,Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getAppUser(request);
		if(userId==null){
			if(user==null){
				resultJson.put("code", 401 );
				resultJson.put("message", "用户未登录");
				resultJson.put("data", new JSONObject());
			}else{
				userId=Long.parseLong(user.getUserId());
			}
		}
		//roomId=4111;
		try {
			ILiveQuestionnaireActivity activity = null;
			ILiveQuestionnaireRoom iLiveQuestionnaireRoom =null;
			boolean startQuestionnaire = iLiveQuestionnaireRoomMng.isStartQuestionnaire(roomId);
			if (startQuestionnaire) {
				iLiveQuestionnaireRoom = iLiveQuestionnaireRoomMng.getStartByRoomId(roomId);
				activity = iLiveQuestionnaireActivityMng.getById(iLiveQuestionnaireRoom.getQuestionnaireId());
			}
			//浏览记录+1
//			ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
			if(activity.getLookNumber()==null){
				activity.setLookNumber(0);
			}
			activity.setLookNumber(activity.getLookNumber()+1);
			iLiveQuestionnaireActivityMng.update(activity);
			//查询该用户原先是否浏览过
			ILiveQuestionnaireActivityStatistic statistic=iLiveQuestionnaireStatisticMng.getListByUserId(userId,iLiveQuestionnaireRoom.getQuestionnaireId());
			if(statistic==null){
				//添加记录
				ILiveQuestionnaireActivityStatistic jilu=new ILiveQuestionnaireActivityStatistic();
				Long id=iLiveQuestionnaireStatisticMng.maxId();
				jilu.setId(id);
				jilu.setQuestionnaireId(iLiveQuestionnaireRoom.getQuestionnaireId());
				jilu.setUserId(userId);
				jilu.setStartTime(new Timestamp(System.currentTimeMillis()));
				iLiveQuestionnaireStatisticMng.save(jilu);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "答卷开始成功");
				resultJson.put("data", new JSONObject());
			}else{
				statistic.setStartTime(new Timestamp(System.currentTimeMillis()));
				iLiveQuestionnaireStatisticMng.update(statistic);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "答卷开始成功");
				resultJson.put("data", new JSONObject());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "答卷开始失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	
	/**
	 * 获取问卷结果  各选项选择人数和百分比以及问答题的相关人员作答答案
	 * @param QuestionnaireId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="getResult.jspx")
	public void getResult(Long QuestionnaireId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveQuestionnaireActivity result = iLiveQuestionnaireActivityMng.getResult(QuestionnaireId);
			JSONObject res = JSONObject.fromObject(result);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "问卷结果获取成功");
			resultJson.put("data", res.toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "问卷结果获取失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
}
