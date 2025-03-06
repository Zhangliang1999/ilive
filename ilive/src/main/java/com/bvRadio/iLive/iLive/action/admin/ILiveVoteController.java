package com.bvRadio.iLive.iLive.action.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import com.bvRadio.iLive.iLive.entity.ILiveVoteActivity;
import com.bvRadio.iLive.iLive.entity.ILiveVoteOption;
import com.bvRadio.iLive.iLive.entity.ILiveVoteProblem;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveVoteOptionMng;
import com.bvRadio.iLive.iLive.manager.ILiveVotePeopleMng;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value="vote")
public class ILiveVoteController {
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间
	
	@Autowired
	private ILiveVoteActivityMng iLiveVoteActivityMng;	//投票活动
	
	@Autowired
	private ILiveVoteOptionMng iLiveVoteOptionMng;		//投票问题选项
	
	@Autowired
	private ILiveVotePeopleMng iLiveVotePeopleMng;		//用户投票记录
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	
	/**
	 * 投票列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="votelist.do")
	public String votelist(String votename,Integer pageNo,Model model,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		Pagination page = iLiveVoteActivityMng.getpageByUserId(Long.valueOf(user.getUserId()),votename, pageNo==null?1:pageNo, 10);;
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(Long.valueOf(user.getUserId()));
		boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_voteActivity);
		if (iLiveManager.getLevel()!=null&&iLiveManager.getLevel()!=0&&!per) {
			page = iLiveVoteActivityMng.getpageByUserId(Long.valueOf(user.getUserId()),votename, pageNo==null?1:pageNo, 10);
		}else {
			page = iLiveVoteActivityMng.getPageByEnterpriseId(enterpriseId, votename, pageNo==null?1:pageNo, 10);
		}
		
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page == null?0:page.getTotalPage());
		model.addAttribute("votename", votename);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_3");
		return "vote/votelist";
	}
	
	/**
	 * 投票创建页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="tocreatevote.do")
	public String createvote(Long voteId,Model model,Integer detail) {
		ILiveVoteActivity activity;
		Integer isEdid = 0;
		if (voteId==null) {
			activity = new ILiveVoteActivity();
		}else {
			activity = iLiveVoteActivityMng.getById(voteId);
			isEdid = 1;
		}
		//是否为修改   0为新增   1为修改
		model.addAttribute("isEdid", isEdid);
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("activity", activity);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_3");
		if(detail ==null) {
			model.addAttribute("detail", "0");
		}else {
			model.addAttribute("detail", "1");
		}
		return "vote/createvote";
	}
	private Integer selectServerGroup() {
		return 100;
	}
	/**
	 * 投票结果统计页面
	 * @param enterpriseId
	 * @param model
	 * @param voteId
	 * @return
	 */
	@RequestMapping(value="voteresult.do")
	public String voteresult(Integer enterpriseId,Long voteId,Model model) {
		ILiveVoteActivity activity = iLiveVoteActivityMng.getById(voteId);
		
		model.addAttribute("activity", activity);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "4_3");
		return "vote/voteresult";
	}
	
	/**
	 * 新增一个投票活动
	 * @param iLiveVoteActivity
	 * @param strList
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="createvote.do")
	public void addvote(ILiveVoteActivity iLiveVoteActivity,String strList,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			Integer enterpriseId = user.getEnterpriseId();
			iLiveVoteActivity.setEnterpriseId(enterpriseId);
			iLiveVoteActivity.setUserId(Long.valueOf(user.getUserId()));
			iLiveVoteActivityMng.createVote(iLiveVoteActivity,strList);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	/**
	 * 修改一个投票活动
	 * @param voteId
	 * @param iLiveVoteActivity
	 * @param strList
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="editvote.do")
	public void editvote(Long voteId,ILiveVoteActivity iLiveVoteActivity,String strList,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			iLiveVoteActivityMng.updateVote(iLiveVoteActivity,strList);
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
	@RequestMapping(value="isStartVote.do")
	public void isStartVote(Integer enterpriseId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveVoteActivity activity = iLiveVoteActivityMng.getActivityByEnterpriseId(enterpriseId);
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
	public void editSwitch(Long voteId,Integer isSwitch,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveVoteActivity activity = iLiveVoteActivityMng.getById(voteId);
			activity.setIsSwitch(isSwitch);
			if (isSwitch==1) {
				activity.setIsBeforeSwitch(1);
			}
			iLiveVoteActivityMng.update(activity);
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
	 * @param voteId
	 * @param problemId
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="getOptionByPRoblemId.do")
	public void getOptionByPRoblemId(Long voteId,Long problemId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Integer number = iLiveVotePeopleMng.getAllPeopleByVoteId(voteId);
			List<ILiveVoteOption> list = iLiveVoteOptionMng.getListByProblemId(problemId);
			for(ILiveVoteOption option:list) {
				Integer optionNum = iLiveVotePeopleMng.getPeopleByOptionId(option.getId());
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
			ILiveVoteActivity activity = iLiveVoteActivityMng.getById(id);
			Timestamp now = new Timestamp(new Date().getTime());
			activity.setIsEnd(1);
			activity.setEndTime(now);
			activity.setIsSwitch(0);
			iLiveVoteActivityMng.update(activity);
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
	@RequestMapping(value="getvote.do")
	public void getvote(Long id,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			result.put("status", "1");
			ILiveVoteActivity activity = iLiveVoteActivityMng.getById(id);
			JSONObject res = JSONObject.fromObject(activity);
			result.put("data", res);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "2");
			
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@ResponseBody
	@RequestMapping(value="export")
	public void export(Long voteId,HttpServletRequest request,HttpServletResponse response) {
		ILiveVoteActivity activity = iLiveVoteActivityMng.getById(voteId);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow head = sheet.createRow(0);
		head.createCell(0).setCellValue("投票名称");
		head.createCell(1).setCellValue(activity.getVoteName());
		HSSFRow timehead = sheet.createRow(1);
		timehead.createCell(0).setCellValue("开始与结束时间");
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timehead.createCell(1).setCellValue(sdf.format(activity.getStartTime()));
		timehead.createCell(2).setCellValue(sdf.format(activity.getEndTime()));
		HSSFRow numPeople = sheet.createRow(2);
		numPeople.createCell(0).setCellValue("总参与人数");
		numPeople.createCell(1).setCellValue(activity.getNumber());
		
		List<ILiveVoteProblem> list = activity.getList();
		if (list!=null&&!list.isEmpty()) {
			for(int i=0;i<list.size();i++) {
				ILiveVoteProblem iLiveVoteProblem = list.get(i);
				HSSFRow row =null;
				if(i!=0) {
					row = sheet.createRow(3+i+list.get(i-1).getListOption().size());
				}else {
					row = sheet.createRow(3+i);
				}
				
				row.createCell(0).setCellValue("问题名称：");
				row.createCell(1).setCellValue(iLiveVoteProblem.getProblemName());
				List<ILiveVoteOption> listOption = iLiveVoteProblem.getListOption();
				if (listOption!=null&&!listOption.isEmpty()) {
					for(int j=0;j<listOption.size();j++) {
						ILiveVoteOption option = listOption.get(j);
						if(i==0) {
							HSSFRow optionrow = sheet.createRow(3+i+1+j);
							optionrow.createCell(1).setCellValue(option.getContent());
							optionrow.createCell(2).setCellValue(option.getNum());
						}else {
							HSSFRow optionrow = sheet.createRow(3+i+1+j+list.get(i-1).getListOption().size());
							optionrow.createCell(1).setCellValue(option.getContent());
							optionrow.createCell(2).setCellValue(option.getNum());
						}
						
					}
				}
			}
		}
		try {
			this.setResponse(response,activity.getVoteName()+".xls");
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
	public void setResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
		fileName = new String(fileName.getBytes(),"ISO8859-1");
		response.setContentType("application/octet-stream;charset=ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
	}
}
