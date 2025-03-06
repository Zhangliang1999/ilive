package com.bvRadio.iLive.iLive.action.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivity;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivityStatistic;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireOption;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireProblem;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.vo.ILiveWenJuanResult;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.util.ExcelUtils;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireOptionMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnairePeopleMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireProblemMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireStatisticMng;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.jwzt.comm.StringUtils;
import com.jwzt.common.StringUtil;

@Controller
@RequestMapping(value="questionnaire")
public class ILiveQuestionnaireController {
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILiveQuestionnaireActivityMng iLiveQuestionnaireActivityMng;	//投票活动
	@Autowired
	private ILiveQuestionnaireProblemMng iLiveQuestionnaireProblemMng;
	@Autowired
	private ILiveQuestionnaireOptionMng iLiveQuestionnaireOptionMng;		//投票问题选项
	
	@Autowired
	private ILiveQuestionnairePeopleMng iLiveQuestionnairePeopleMng;		//用户投票记录
	@Autowired
	private ILiveQuestionnaireStatisticMng iLiveQuestionnaireStatisticMng;
	
	
	
	/**
	 * 问卷列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="questionnairelist.do")
	public String Questionnairelist(String questionnairename,String StartTime,Integer pageNo,Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
//		Pagination page = iLiveQuestionnaireActivityMng.getpageByUserId(Long.valueOf(user.getUserId()),questionnairename, pageNo==null?1:pageNo, 10);;
//		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.valueOf(user.getUserId()));
//		boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_QuestionnaireActivity);
//		if (iLiveManager.getLevel()!=null&&iLiveManager.getLevel()!=0&&!per) {
//			page = iLiveQuestionnaireActivityMng.getpageByUserId(Long.valueOf(user.getUserId()),Questionnairename, pageNo==null?1:pageNo, 10);
//		}else {
		if(StringUtils.isEmpty(StartTime)){
			StartTime=null;
		}
		if(StartTime!=null){
			StartTime=StartTime+":00";
		}
		Pagination page = iLiveQuestionnaireActivityMng.getPageByEnterpriseId(enterpriseId, questionnairename,StartTime, pageNo==null?1:pageNo, 10);
//		}
		
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page == null?0:page.getTotalPage());
		model.addAttribute("questionnairename", questionnairename);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_4");
		return "questionnaire/questionnairelist";
	}
	/**
	 * 根据题号显示结果列表页面
	 */
	@RequestMapping(value="questionnairelistByProblemId.do")
	public String QuestionnairelistByproblemId(Long QuestionnaireId,Integer pageNo,Model model,HttpServletRequest request){
		Pagination page = iLiveQuestionnaireProblemMng.getByQuestionnaireId(QuestionnaireId,pageNo==null?1:pageNo, 10);
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page == null?0:page.getTotalPage());
		model.addAttribute("questionnaireId", QuestionnaireId);
		//获取用户的浏览记录
		ILiveQuestionnaireActivity active=iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
		Integer lookNumber=active.getLookNumber();//浏览量
		if(lookNumber==null){
			lookNumber=0;
		}
		Integer number=active.getNumber();//回收量
		if(number==null){
			number=0;
		}
		Double percent=0.0;
		if(lookNumber!=0){
			percent=(double) (number/lookNumber);
		}
		model.addAttribute("lookNumber", lookNumber);
		model.addAttribute("number", number);
		model.addAttribute("percent", percent);
		Long totalTime=0L;
		String average=null;
		List<ILiveQuestionnaireActivityStatistic> list=iLiveQuestionnaireStatisticMng.getListByQuestionnaireId(QuestionnaireId);
		if(list==null||list.isEmpty()){
			average="0秒";
		}else if(list!=null||list.size()>0){
			for(ILiveQuestionnaireActivityStatistic statistic : list){
				if(statistic.getEndTime()==null){
					statistic.setEndTime(active.getEndTime());
				}
				totalTime=totalTime+(statistic.getEndTime().getTime()-statistic.getStartTime().getTime())/1000;
			}
			average=convertTime(totalTime.intValue());
		}
		model.addAttribute("active", active);
		model.addAttribute("average", average);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_4");
		return "questionnaire/questionnaireproblemlist";	
	}
	
	/**
	 * 根据人员显示结果列表页面
	 */
	@RequestMapping(value="questionnairelistByPeople.do")
	public String QuestionnairelistByPeople(Long QuestionnaireId,Integer pageNo,Model model,HttpServletRequest request){
		Pagination page = iLiveQuestionnairePeopleMng.getByQuestionnaireId(QuestionnaireId,pageNo==null?1:pageNo, 10);
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page == null?0:page.getTotalPage());
		model.addAttribute("questionnaireId", QuestionnaireId);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_4");
		return "questionnaire/questionnairepeoplelist";	
	}
	/**
	 * 投票创建页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="tocreateQuestionnaire.do")
	public String createQuestionnaire(Long QuestionnaireId,Model model,Integer detail) {
		ILiveQuestionnaireActivity activity;
		Integer isEdid = 0;
		if (QuestionnaireId==null) {
			activity = new ILiveQuestionnaireActivity();
		}else {
			activity = iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
			isEdid = 1;
		}
		//是否为修改   0为新增   1为修改
		model.addAttribute("isEdid", isEdid);
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("activityList", activity.getList());
		model.addAttribute("activity", activity);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_4");
		if(detail ==null) {
			model.addAttribute("detail", "0");
		}else {
			model.addAttribute("detail", "1");
		}
		return "questionnaire/createQuestionnaire";
	}
	/**
	 * 获取问题列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="getQuestionnaireProblemsLists.do")
	public void getQuestionnaireProblemsLists(Long QuestionnaireId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
		JSONArray array=new JSONArray();
		array.add(activity.getList());
		result.put("QuestionnaireProblemsLists", array);
		
		ResponseUtils.renderJson(response, result.toString());
	}
	private Integer selectServerGroup() {
		return 100;
	}
	/**
	 * 投票结果统计页面(按照problemId)
	 * @param enterpriseId
	 * @param model
	 * @param QuestionnaireId
	 * @return
	 */
	@RequestMapping(value="questionnaireresult.do")
	public String Questionnaireresult(Integer enterpriseId,Long QuestionnaireId,Model model) {
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
		List<ILiveQuestionnaireProblem> problem=iLiveQuestionnaireProblemMng.getListByQuestionnaireId(QuestionnaireId);
		if(!problem.isEmpty()||problem!=null){
			Long minId=problem.get(0).getId();
			Long maxId=problem.get(problem.size()-1).getId();
			model.addAttribute("minId", minId);
			model.addAttribute("maxId", maxId);
		}
		model.addAttribute("problemId", problem.get(0).getId());
		model.addAttribute("activity", activity);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_4");
		return "questionnaire/questionnaireresult";
	}
	/**
	 * 投票结果统计页面(按照problemId)
	 * @param enterpriseId
	 * @param model
	 * @param QuestionnaireId
	 * @return
	 */
	@RequestMapping(value="getquestionnaireresult.do")
	public void getQuestionnaireresult(Long QuestionnaireId,Long problemId,Integer pageNo,HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		List<ILiveQuestionnaireProblem> problem = iLiveQuestionnaireProblemMng.getResultById(QuestionnaireId, problemId, pageNo==null?1:pageNo, 15);
		List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireOptionMng.getListByProblemId(problemId);
		for(ILiveQuestionnaireOption option:listOption) {
			Integer num = iLiveQuestionnairePeopleMng.getPeopleByOptionId(option.getId());
			option.setNum(num);
		}
		problem.get(0).setListOption(listOption);
		if(problem.get(0).getStyle()==3){
			Pagination pagination=iLiveQuestionnairePeopleMng.getByQuestionnaireId(QuestionnaireId, problemId, pageNo==null?1:pageNo, 15);
			List<ILiveQuestionnairePeople> listPeople=(List<ILiveQuestionnairePeople>) pagination.getList();
			problem.get(0).setListPeople(listPeople);
		}
		result.put("result", problem.get(0));
		ResponseUtils.renderJson(response, result.toString());
	}
	/**
	 * 投票结果统计页面(按照userId)
	 * @param enterpriseId
	 * @param model
	 * @param QuestionnaireId
	 * @return
	 */
	
	@RequestMapping(value="questionnairepeopleresult.do")
	public String Questionnaireresult1(Integer enterpriseId,Long QuestionnaireId,Long userId,Model model) {
		ILiveQuestionnaireActivity activity=iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
		List<ILiveWenJuanResult>  list = iLiveQuestionnaireActivityMng.getByuserId(QuestionnaireId,userId);
		List<ILiveQuestionnairePeople> listPeople=iLiveQuestionnairePeopleMng.getListByUserId(userId,"",QuestionnaireId);
		model.addAttribute("userId", userId);
		model.addAttribute("activity", activity);
		model.addAttribute("list", list);
		model.addAttribute("mobile", listPeople.get(0).getIdCard());
		model.addAttribute("idCard", listPeople.get(0).getIdCard());
		model.addAttribute("userName", listPeople.get(0).getUserName());
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_4");
		return "questionnaire/questionnaireresultBypeople";
	}
	/**
	 * 新增一个问卷活动
	 * @param iLiveQuestionnaireActivity
	 * @param strList
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="createQuestionnaire.do")
	public void addQuestionnaire(ILiveQuestionnaireActivity iLiveQuestionnaireActivity,String strList,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			Integer enterpriseId = user.getEnterpriseId();
			iLiveQuestionnaireActivity.setEnterpriseId(enterpriseId);
			iLiveQuestionnaireActivity.setUserId(Long.valueOf(user.getUserId()));
			iLiveQuestionnaireActivityMng.createQuestionnaire(iLiveQuestionnaireActivity,strList);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	/**
	 * 修改一个投票活动
	 * @param QuestionnaireId
	 * @param iLiveQuestionnaireActivity
	 * @param strList
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="editQuestionnaire.do")
	public void editQuestionnaire(Long QuestionnaireId,ILiveQuestionnaireActivity iLiveQuestionnaireActivity,String strList,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			iLiveQuestionnaireActivityMng.updateQuestionnaire(iLiveQuestionnaireActivity,strList);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 判断一个活动是否开始
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="isStartQuestionnaire.do")
	public void isStartQuestionnaire(Integer enterpriseId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getActivityByEnterpriseId(enterpriseId);
			if(activity==null) {
				result.put("status", 0);
			}else {
				result.put("status", 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 修改活动状态是否开启
	 */
	@ResponseBody
	@RequestMapping(value="editSwitch.do")
	public void editSwitch(Long QuestionnaireId,Integer isSwitch,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
			activity.setIsSwitch(isSwitch);
			if (isSwitch==1) {
				activity.setIsBeforeSwitch(1);
			}
			iLiveQuestionnaireActivityMng.update(activity);
			if(activity!=null){
				Integer enterpriseId = activity.getEnterpriseId();
				ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
						.getUserListMap();
				if(userListMap!=null) {
					ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(enterpriseId);
					if(concurrentHashMap!=null) {
						Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
						ILiveMessage iLiveMessage = new ILiveMessage();
						iLiveMessage.setRoomType(5);
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
				}
			}
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 根据问题id获取这个问题的选项
	 * @param QuestionnaireId
	 * @param problemId
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="getOptionByPRoblemId.do")
	public void getOptionByPRoblemId(Long QuestionnaireId,Long problemId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Integer number = iLiveQuestionnairePeopleMng.getAllPeopleByQuestionnaireId(QuestionnaireId);
			List<ILiveQuestionnaireOption> list = iLiveQuestionnaireOptionMng.getListByProblemId(problemId);
			for(ILiveQuestionnaireOption option:list) {
				Integer optionNum = iLiveQuestionnairePeopleMng.getPeopleByOptionId(option.getId());
				option.setNum(optionNum);
				if (option.getContent().length()>15) {
					option.setContent(option.getContent().substring(0, 14)+"...");
				}
			}
			JSONArray res = JSONArray.fromObject(list);
			result.put("status", 1);
			result.put("data", res);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
			result.put("status", new JSONObject());
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 使一个投票活动结束
	 * @param lottery
	 */
	@ResponseBody
	@RequestMapping(value="letend.do",method = RequestMethod.POST)
	public void letend(Long id,HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			res.put("status", 1);
			ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(id);
			Timestamp now = new Timestamp(new Date().getTime());
			activity.setIsEnd(1);
			activity.setEndTime(now);
			activity.setIsSwitch(0);
			iLiveQuestionnaireActivityMng.update(activity);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			res.put("date", format.format(new Date()));
			/*ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
			.getUserListMap();
			ConcurrentHashMap<String, UserBean> concurrentHashMap = userListMap.get(prize.getRoomId());
			Iterator<String> userIterator = concurrentHashMap.keySet().iterator();
			ILiveMessage iLiveMessage = new ILiveMessage();
			iLiveMessage.setRoomType(5);
			while (userIterator.hasNext()) {
				String key = userIterator.next();
				UserBean user = concurrentHashMap.get(key);
				List<ILiveMessage> msgList = user.getMsgList();
				if (msgList == null) {
					msgList = new ArrayList<ILiveMessage>();
				}
				msgList.add(iLiveMessage);
			}*/
		}catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
	/**
	 * 根据id获取投票活动信息
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="getQuestionnaire.do")
	public void getQuestionnaire(Long id,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			result.put("status", "1");
			ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(id);
			JSONObject res = JSONObject.fromObject(activity);
			result.put("data", res);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "2");
			
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@ResponseBody
	@RequestMapping(value="export.do")
	public void export(Long QuestionnaireId,HttpServletRequest request,HttpServletResponse response) {
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
		Map<Long, Object>  list1 = iLiveQuestionnaireActivityMng.getByquestionnaireId(QuestionnaireId);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow head = sheet.createRow(0);
		head.createCell(0).setCellValue("投票名称");
		head.createCell(1).setCellValue(activity.getQuestionnaireName());
		HSSFRow timehead = sheet.createRow(1);
		timehead.createCell(0).setCellValue("开始与结束时间");
		timehead.createCell(1).setCellValue(activity.getStartTime());
		timehead.createCell(2).setCellValue(activity.getEndTime());
		HSSFRow numPeople = sheet.createRow(2);
		numPeople.createCell(0).setCellValue("总参与人数");
		numPeople.createCell(1).setCellValue(activity.getNumber());
		
		List<ILiveQuestionnaireProblem> list = activity.getList();
		if (list!=null&&!list.isEmpty()) {
			for(int i=0;i<list.size();i++) {
				ILiveQuestionnaireProblem iLiveQuestionnaireProblem = list.get(i);
				HSSFRow row = sheet.createRow(3+i);
				row.createCell(0).setCellValue("问题名称：");
				row.createCell(1).setCellValue(iLiveQuestionnaireProblem.getProblemName());
				List<ILiveQuestionnaireOption> listOption = iLiveQuestionnaireProblem.getListOption();
				if (listOption!=null&&!listOption.isEmpty()) {
					for(int j=0;j<listOption.size();j++) {
						ILiveQuestionnaireOption option = listOption.get(j);
						HSSFRow optionrow = sheet.createRow(3+i+1+j);
						optionrow.createCell(1).setCellValue(option.getContent());
						optionrow.createCell(2).setCellValue(option.getNum());
					}
				}
			}
		}
		try {
			this.setResponse(response,activity.getQuestionnaireName()+".xls");
			OutputStream os = response.getOutputStream();
	        workbook.write(os);
	        os.flush();
	        os.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="export1.do")
	public void export1(Long QuestionnaireId,HttpServletRequest request,HttpServletResponse response) {
		try {
		ILiveQuestionnaireActivity activity = iLiveQuestionnaireActivityMng.getById(QuestionnaireId);
		Map<Long, Object>  list1 = iLiveQuestionnaireActivityMng.getByquestionnaireId(QuestionnaireId);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for(Long userId : list1.keySet()){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			List<ILiveWenJuanResult> result=(List<ILiveWenJuanResult>) list1.get(userId);
			for(int i=0;i<result.size();i++){
				map.put("problem"+(i+1), result.get(i).getQuestionnaireProblemAnswer());
			}
			dataList.add(map);
		}
		List<ILiveQuestionnaireProblem> problem=iLiveQuestionnaireProblemMng.getListByQuestionnaireId(QuestionnaireId);
		List key=new ArrayList<>();
		List columnName=new ArrayList<>();
		key.add("UserId");
		columnName.add('"'+"用户ID"+'"');
		for(int i=0;i<problem.size();i++){
			key.add("problem"+(i+1));
			columnName.add('"'+"问题"+(i+1)+"答案"+'"');	
		}
		String[] keys=(String[]) key.toArray(new String[key.size()]);
		String[] columnNames=(String[]) columnName.toArray(new String[columnName.size()]);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList , keys), keys, columnNames)
			.write(os);
			ResponseUtils.downloadHandler(activity.getQuestionnaireName(), os, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void setResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
		fileName = new String(fileName.getBytes(),"ISO8859-1");
		response.setContentType("application/octet-stream;charset=ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
	}
	
	/**
	 * 换算时间：秒-》 时:分:秒
	 * 
	 * @param duration
	 *            时间，单位为秒
	 * @return
	 */
	private String convertTime(Integer duration) {
		if (duration == null)
			return "00小时00分钟00秒";
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (duration < 60) {
			return "00分钟" + duration+"秒";
		} else {
			minute = duration / 60;
			if (minute < 60) {
				second = duration % 60;
				return  formatTime(minute) + "分钟" + formatTime(second)+"秒";
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99小时59分钟59秒";
				minute = minute % 60;
				second = duration - hour * 3600 - minute * 60;
				return formatTime(hour) + "小时" + formatTime(minute) + "分钟" + formatTime(second)+"秒";
			}
		}
	}
	/**
	 * 个位数时间加上0前缀，十位数不用加
	 * 
	 * @param time
	 *            时间
	 * @return
	 */
	private String formatTime(int time) {
		if (time >= 0 && time < 10) {
			return "0" + time;
		}
		return "" + time;
	}
}
