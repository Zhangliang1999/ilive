package com.bvRadio.iLive.manager.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveAppointment;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.manager.ILiveAppointmentMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.manager.entity.vo.LiveVideoViewRecord;
import com.bvRadio.iLive.manager.entity.vo.LoginRecord;

import net.sf.json.JSONArray;

@RequestMapping(value="userrecord")
public class UserRecordController {

	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@Autowired
	private ILiveEnterpriseFansMng enterpriseFansMng;
	
	@Autowired
	private ILiveMessageMng iLiveMessageMng;
	
	@Autowired
	private ILiveMediaFileCommentsMng iLiveMediaFileCommentsMng;
	
	/**
	 * 用户列表
	 * @param iLiveManager
	 * @param pageSize
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value="alluser")
	public String getUserList(ILiveManager iLiveManager,String nailName, Integer pageSize,Integer pageNo,Model model) {
		
		String nailName1=iLiveManager.getNailName();
		if(nailName1!=null){
			try {
				nailName1=new String(nailName1.trim().getBytes("ISO-8859-1"), "UTF-8");
				iLiveManager.setNailName(nailName1);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		Pagination userRecord = iLiveManagerMng.getUserRecord(iLiveManager,pageNo,pageSize);
		
		model.addAttribute("page", userRecord);
		model.addAttribute("iLiveManager", iLiveManager);
		model.addAttribute("topActive", "8");
		model.addAttribute("leftActive", "1");
		model.addAttribute("isBlack", "0");
		return "userecord/userRecordList";
	}
	
	/**
	 * 黑名单用户列表
	 * @param iLiveManager
	 * @param pageSize
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value="alluserblack")
	public String alluserblack(ILiveManager iLiveManager,Integer pageSize,Integer pageNo,Model model) {
		iLiveManager.setIsAllBlack(1);
		Pagination userRecord = iLiveManagerMng.getUserRecord(iLiveManager,pageNo,pageSize);
		
		model.addAttribute("page", userRecord);
		model.addAttribute("iLiveManager", iLiveManager);
		model.addAttribute("topActive", "8");
		model.addAttribute("leftActive", "2");
		model.addAttribute("isBlack", "1");
		return "userecord/userRecordList";
	}
	
	/**
	 * 跳转至修改页面
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("detail")
	public String toEdit(Long userId,Model model,Integer isBlack) {
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		model.addAttribute("iLiveManager", iLiveManager);
		model.addAttribute("topActive", "8");
		if (isBlack==0) {
			model.addAttribute("leftActive", "1");
		}else if (isBlack==1) {
			model.addAttribute("leftActive", "2");
		}
		model.addAttribute("isBlack",isBlack);
		return "userecord/editDetail";
	}
	
	
	@RequestMapping("exportData")
	public void exportData(String uuid,Model model,HttpServletResponse response) {
		List<ILiveManager> list = managerMap.get(uuid);
		managerMap.remove(uuid);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("用户列表");
		HSSFRow rowHead = sheet.createRow(0);
		rowHead.createCell(0).setCellValue("用户ID");
		rowHead.createCell(1).setCellValue("用户账号");
		rowHead.createCell(2).setCellValue("用户昵称");
		rowHead.createCell(3).setCellValue("注册时间");
		rowHead.createCell(4).setCellValue("注册来源");
		rowHead.createCell(5).setCellValue("最后一次登录IP");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (list!=null) {
			for(int i=0;i<list.size();i++) {
				ILiveManager user = list.get(i);
				HSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(user.getUserId());
				row.createCell(1).setCellValue(user.getMobile());
				row.createCell(2).setCellValue(user.getNailName());
				row.createCell(3).setCellValue(format.format(user.getCreateTime()));
				if (user.getRegisterSource()==null || user.getRegisterSource()==1) {
					row.createCell(4).setCellValue("网页");
				}else if(user.getRegisterSource()==2){
					row.createCell(4).setCellValue("微信");
				}else if(user.getRegisterSource()==3){
					row.createCell(4).setCellValue("APP");
				}
				row.createCell(5).setCellValue(user.getLastIP()==null?"":user.getLastIP());
			}
		}
		try {
			this.setResponse(response, "用户列表.xls");
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
	private void setResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
		fileName = new String(fileName.getBytes(),"ISO8859-1");
		response.setContentType("application/octet-stream;charset=ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
	}
	
	private ConcurrentHashMap<String, List<ILiveManager>> managerMap = new ConcurrentHashMap<>(); 
	
	@RequestMapping(value="getList")
	public void getList(ILiveManager iLiveManager,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			List<ILiveManager> list = iLiveManagerMng.getUserRecordList(iLiveManager);
			UUID randomUUID = UUID.randomUUID();
			System.out.println(randomUUID.toString());
			managerMap.put(randomUUID.toString(), list);
			result.put("code", 1);
			result.put("msg", "成功");
			result.put("uuid", randomUUID.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 0);
			result.put("msg", "失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 设置备注
	 * @param response
	 * @param str
	 * @param beizhu
	 */
	@RequestMapping(value="setbeizhu")
	public void setbeizhu(HttpServletResponse response,String str,String beizhu) {
		JSONObject result = new JSONObject();
		try {
			List<ILiveManager> managers = new ArrayList<>();
			JSONArray array = JSONArray.fromObject(str);
			for(int i=0;i<array.size();i++) {
				net.sf.json.JSONObject jsonObject = array.getJSONObject(i);
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(jsonObject.getLong("id"));
				iLiveManager.setBeizhu(beizhu);
				managers.add(iLiveManager);
			}
			
			iLiveManagerMng.batchUpdate(managers);
			result.put("code", 1);
			result.put("msg", "设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 0);
			result.put("msg", "设置失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}

	/**
	 * 拉黑单个用户
	 * @param userId
	 * @param response
	 */
	@RequestMapping(value="letBlack")
	public void letBlack(Long userId,Integer status,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
			iLiveManager.setIsAllBlack(status==null?1:status);
			iLiveManagerMng.updateLiveManager(iLiveManager);
			result.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 拉黑多个用户
	 * @param userId
	 * @param response
	 */
	@RequestMapping(value="letBatchBlack")
	public void letBatchBlack(String userIds,Integer status,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			JSONArray jsonArray = JSONArray.fromObject(userIds);
			List<ILiveManager> list = new ArrayList<>();
			for (int i = 0; i < jsonArray.size(); i++) {
				net.sf.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
				ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(jsonObject.getLong("id"));
				iLiveManager.setIsAllBlack(status==null?1:status);
				list.add(iLiveManager);
				iLiveManagerMng.batchUpdate(list);
			}
			result.put("msg", "操作成功");
			result.put("code", "0");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "操作失败");
			result.put("code", "1");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 提交编辑
	 * @param userId
	 * @param beizhu
	 * @param nailName
	 * @param userPwd
	 * @param response
	 */
	@RequestMapping(value="subset")
	public void subset(Long userId,String beizhu,String nailName,
			String userPwd,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
			iLiveManager.setBeizhu(beizhu);
			iLiveManager.setNailName(nailName);
			iLiveManager.setUserPwd(userPwd);
			iLiveManagerMng.updateLiveManager(iLiveManager);
			result.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 基本资料
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="uDetail")
	public String uDetail(Long userId,Model model) {
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		model.addAttribute("iLiveManager", iLiveManager);
		return "userecord/icontent/uDetail";
	}
	
	
	@Autowired
	private ILiveAppointmentMng iLiveAppointmentMng; // 预约
	
	/**
	 * 预约列表
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="appointList")
	public String appointList(Long userId,Integer roomId,Model model,Integer pageNo,Integer pageSize) {
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		Pagination page = iLiveAppointmentMng.getAppointmentPage(userId+"",roomId, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
		
		model.addAttribute("iLiveManager", iLiveManager);
		model.addAttribute("roomId", roomId);
		model.addAttribute("page", page);
		return "userecord/icontent/appointList";
	}
	
	
	@RequestMapping(value="exportAppoList")
	public void getAppoList(String userId,Integer roomId,HttpServletResponse response) {
		List<ILiveAppointment> list = iLiveAppointmentMng.getAppointmentListByUserIdAndRoomId(userId,roomId);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("预约列表");
		HSSFRow rowHeader = sheet.createRow(0);
		rowHeader.createCell(0).setCellValue("直播id");
		rowHeader.createCell(1).setCellValue("直播开始时间");
		rowHeader.createCell(2).setCellValue("预约时间");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (list!=null) {
			for(int i=0;i<list.size();i++) {
				ILiveAppointment iLiveAppointment = list.get(i);
				HSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(iLiveAppointment.getRoomId());
				row.createCell(1).setCellValue(format.format(iLiveAppointment.getStartTime()));
				row.createCell(2).setCellValue(format.format(iLiveAppointment.getCreateTime()));
			}
		}
		try {
			this.setResponse(response, "预约列表.xls");
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
	@RequestMapping(value="exportFolllow")
	public void exportFolllow(Long userId,Integer roomId,HttpServletResponse response) {
		List<ILiveEnterpriseFans> list = enterpriseFansMng.getListByUserId(userId);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("关注列表");
		HSSFRow rowHeader = sheet.createRow(0);
		rowHeader.createCell(0).setCellValue("关注商户id");
		rowHeader.createCell(1).setCellValue("商户名称");
		rowHeader.createCell(2).setCellValue("关注时间");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (list!=null) {
			for(int i=0;i<list.size();i++) {
				ILiveEnterpriseFans iLiveEnterpriseFans = list.get(i);
				HSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(iLiveEnterpriseFans.getEnterpriseId());
				row.createCell(1).setCellValue(iLiveEnterpriseFans.getEnterpriseId());
				row.createCell(2).setCellValue(format.format(iLiveEnterpriseFans.getConcernTime()));
			}
		}
		try {
			this.setResponse(response, "关注列表.xls");
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
	
	/**
	 * 关注商户列表
	 * @param userId
	 * @param enterpriseId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="followEnt")
	public String followEnt(Long userId,Integer pageNo,Integer pageSize,Model model) {
		Pagination page = enterpriseFansMng.getPageByUserId(userId,pageNo,pageSize);
		model.addAttribute("page", page);
		model.addAttribute("userId", userId);
		return "userecord/icontent/followEnt";
	}
	
	/**
	 * 直播评论列表
	 * @param userId
	 * @param roomId
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value="liveMsglist")
	public String liveMsglist(Long userId,Integer roomId,String keyword,Integer pageNo,Integer pageSize,Model model) {
		Long start = new Date().getTime(); 
		Pagination page = iLiveMessageMng.getPageRecordByUser(userId,roomId,keyword,pageNo,pageSize);
		Long end = new Date().getTime(); 
		System.out.println("查询直播评论总共用时："+(end-start)+"毫秒");
		model.addAttribute("page", page);
		model.addAttribute("userId", userId);
		model.addAttribute("roomId", roomId);
		model.addAttribute("keyword", keyword);
		return "userecord/icontent/liveMsglist";
	}
	
	@RequestMapping(value="delLiveMsg")
	public void delLiveMsg(Long msgId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveMessage message = iLiveMessageMng.findById(msgId);
			message.setDeleted(true);
			iLiveMessageMng.update(message);
			result.put("code", "0");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	private final ConcurrentHashMap<String, List<ILiveMessage>> liveMsgMap = new ConcurrentHashMap<>();
	
	@RequestMapping(value="getliveMsgList")
	public void getliveMsgList(Long userId,Integer roomId,String keyword,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			List<ILiveMessage> list = iLiveMessageMng.getListRecordByUser(userId,roomId,keyword);
			UUID uuid = UUID.randomUUID();
			liveMsgMap.put(uuid.toString(), list);
			result.put("code", 1);
			result.put("uuid", uuid);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 0);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping(value="exportLiveMsg")
	public void exportLiveMsg(String uuid,HttpServletResponse response) {
		List<ILiveMessage> list = liveMsgMap.get(uuid);
		liveMsgMap.remove(uuid);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("用户列表");
		HSSFRow rowHead = sheet.createRow(0);
		rowHead.createCell(0).setCellValue("直播ID");
		rowHead.createCell(1).setCellValue("评论内容");
		rowHead.createCell(2).setCellValue("评论时间");
		rowHead.createCell(3).setCellValue("状态");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (list!=null) {
			for(int i=0;i<list.size();i++) {
				ILiveMessage iLiveMessage = list.get(i);
				HSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(iLiveMessage.getLiveRoomId());
				row.createCell(1).setCellValue(iLiveMessage.getMsgContent());
				row.createCell(2).setCellValue(format.format(iLiveMessage.getCreateTime()));
				row.createCell(3).setCellValue(iLiveMessage.getStatus()!=null&&iLiveMessage.getStatus()==1?"删除":"正常");
			}
		}
		try {
			this.setResponse(response, "评论列表.xls");
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
	
	/**
	 * 回看评论
	 * @param userId
	 * @param fileId
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value="fileCommentsList")
	public String fileCommentsList(Long userId,Long fileId,String keyword,Integer pageNo,Integer pageSize,Model model) {
		Pagination page = iLiveMediaFileCommentsMng.getPageByUserId(userId,fileId,keyword,pageNo,pageSize);
		model.addAttribute("page",page);
		model.addAttribute("userId",userId);
		model.addAttribute("fileId",fileId);
		model.addAttribute("keyword",keyword);
		return "userecord/icontent/mediaMsglist";
	}
	
	@RequestMapping(value="delVideoMsg")
	public void delVideoMsg(Long commentsId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			ILiveMediaFileComments fileComment = iLiveMediaFileCommentsMng.getFileCommentById(commentsId);
			fileComment.setDelFlag(1);
			iLiveMediaFileCommentsMng.update(fileComment);
			result.put("code", "0");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	private final ConcurrentHashMap<String, List<ILiveMediaFileComments>> mediaMsgMap = 
			new ConcurrentHashMap<>();
	
	@RequestMapping(value="getMediaMsgList")
	public void getMediaMsgList(Long userId,Long fileId,String keyword,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			List<ILiveMediaFileComments> list = iLiveMediaFileCommentsMng.getListByUserId(userId,fileId,keyword);
			UUID uuid = UUID.randomUUID();
			mediaMsgMap.put(uuid.toString(), list);
			result.put("code", 1);
			result.put("uuid", uuid);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 0);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping(value="exportMediaMsg")
	public void exportMediaMsg(String uuid,HttpServletResponse response) {
		List<ILiveMediaFileComments> list = mediaMsgMap.get(uuid);
		mediaMsgMap.remove(uuid);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("用户列表");
		HSSFRow rowHead = sheet.createRow(0);
		rowHead.createCell(0).setCellValue("回看ID");
		rowHead.createCell(1).setCellValue("评论内容");
		rowHead.createCell(2).setCellValue("评论时间");
		rowHead.createCell(3).setCellValue("状态");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (list!=null) {
			for(int i=0;i<list.size();i++) {
				ILiveMediaFileComments mediaFileComments = list.get(i);
				HSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(mediaFileComments.getiLiveMediaFile().getFileId());
				row.createCell(1).setCellValue(mediaFileComments.getComments());
				row.createCell(2).setCellValue(format.format(mediaFileComments.getCreateTime()));
				row.createCell(3).setCellValue(mediaFileComments.getDelFlag()!=null&&mediaFileComments.getDelFlag()==1?"删除":"正常");
			}
		}
		try {
			this.setResponse(response, "评论列表.xls");
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
	
	/**
	 * 登录列表
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="loginList")
	public String loginList(Long userId,Integer pageNo,Integer pageSize,Model model) {
		pageNo = pageNo==null?1:pageNo;
		pageSize = pageSize==null?10:pageSize;
		String getLogUrl = ConfigUtils.get("log_url");
		/*String saveLogUrl = "http://" + getLogUrl + "/logServer/service/log/list";
		Map<String, String> params = new HashMap<>();
		params.put("userId", userId+"");
		params.put("pageNo", pageNo+"");
		params.put("pageSize", pageSize+"");
		params.put("systemId", "100");
		params.put("modelId", "128");
		String sendPost;
		try {
			sendPost = HttpUtils.sendPost(saveLogUrl, params, "utf-8");
			System.out.println(sendPost);
			net.sf.json.JSONObject result = net.sf.json.JSONObject.fromObject(sendPost);
			net.sf.json.JSONObject data = result.getJSONObject("data");
			int totalCount = data.getInt("totalCount");
			JSONArray jsonArray = data.getJSONArray("list");
			List<LoginRecord> list = new ArrayList<>();
			for(int i=0;i<jsonArray.size();i++) {
				net.sf.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
				System.out.println(jsonObject);
				LoginRecord loginRecord = new LoginRecord();
				loginRecord.setCreateTime(jsonObject.getString("createTime"));
				
				net.sf.json.JSONObject content = jsonObject.getJSONObject("content");
				try {
					loginRecord.setIpArea(content.getString("lastRegion"));
				}catch (Exception e) {
					e.printStackTrace();
					loginRecord.setIpArea("暂无");
				}
				try {
					loginRecord.setIpAddress(content.getString("lastIP"));
				}catch (Exception e) {
					e.printStackTrace();
					loginRecord.setIpAddress("暂无记录");
				}
				list.add(loginRecord);
			}
			Pagination page = new Pagination(pageNo, pageSize, totalCount, list);
			model.addAttribute("page", page);
			model.addAttribute("userId", userId);*/
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		String url2 = "http://172.18.250.170/statistic/service/ip/getByUserId?userId="+userId+"&pageNo="+pageNo+"&pageSize="+pageSize;
		try {
			String sendGet = HttpUtils.sendGet(url2, "utf-8");
			if (sendGet!=null&&!sendGet.equals("")) {
				net.sf.json.JSONObject result = net.sf.json.JSONObject.fromObject(sendGet);
				net.sf.json.JSONObject data = result.getJSONObject("data");
				int totalCount = data.getInt("totalCount");
				JSONArray jsonArray = data.getJSONArray("list");
				List<LoginRecord> list = new ArrayList<>();
				for(int i=0;i<jsonArray.size();i++) {
					net.sf.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
					System.out.println(jsonObject);
					LoginRecord loginRecord = new LoginRecord();
					loginRecord.setCreateTime(jsonObject.getString("createTime"));
					
					net.sf.json.JSONObject content = jsonObject.getJSONObject("content");
					try {
						loginRecord.setIpArea(content.getString("lastRegion"));
					}catch (Exception e) {
						e.printStackTrace();
						loginRecord.setIpArea("暂无");
					}
					try {
						loginRecord.setIpAddress(content.getString("lastIP"));
					}catch (Exception e) {
						e.printStackTrace();
						loginRecord.setIpAddress("暂无记录");
					}
					list.add(loginRecord);
				}
				Pagination page = new Pagination(pageNo, pageSize, totalCount, list);
				model.addAttribute("page", page);
				model.addAttribute("userId", userId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "userecord/icontent/loginList";
	}
	
	/**
	 * 导出登录列表
	 * @param userId
	 * @param response
	 */
	@RequestMapping(value="exportLogList")
	public void exportLogList(Long userId,HttpServletResponse response) {
		
		//获取登录列表
		String getLogUrl = ConfigUtils.get("log_url");
		String saveLogUrl = "http://" + getLogUrl + "/logServer/service/log/list";
		Map<String, String> params = new HashMap<>();
		params.put("userId", userId+"");
		params.put("systemId", "100");
		params.put("modelId", "128");
		String sendPost;
		List<LoginRecord> list = new ArrayList<>();
		try {
			sendPost = HttpUtils.sendPost(saveLogUrl, params, "utf-8");
			System.out.println(sendPost);
			net.sf.json.JSONObject result = net.sf.json.JSONObject.fromObject(sendPost);
			net.sf.json.JSONObject data = result.getJSONObject("data");
			int totalCount = data.getInt("totalCount");
			JSONArray jsonArray = data.getJSONArray("list");
			for(int i=0;i<jsonArray.size();i++) {
				net.sf.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
				LoginRecord loginRecord = new LoginRecord();
				loginRecord.setCreateTime(jsonObject.getString("createTime"));
				list.add(loginRecord);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("登录列表");
		HSSFRow rowHead = sheet.createRow(0);
		rowHead.createCell(0).setCellValue("登录ip");
		rowHead.createCell(1).setCellValue("登录ip地点");
		rowHead.createCell(2).setCellValue("登录时间");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (list!=null) {
			for(int i=0;i<list.size();i++) {
				LoginRecord loginRecord = list.get(i);
				HSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(format.format(loginRecord.getCreateTime()));
				row.createCell(1).setCellValue(format.format(loginRecord.getCreateTime()));
				row.createCell(2).setCellValue(format.format(loginRecord.getCreateTime()));
			}
		}
		try {
			this.setResponse(response, "登录列表.xls");
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
	
	/**
	 * 直播观看列表
	 * @param userId
	 * @param roomId
	 * @param pageNo
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value="liveviewlist")
	public String liveviewlist(Long userId,Integer roomId,Integer pageNo,Integer pageSize,Model model) {
		//type=1直播   2回看
		int type = 1;
		pageNo = pageNo==null?1:pageNo;
		pageSize = pageSize==null?10:pageSize;
		Pagination page = null;
		//统计地址
		String getStatisticUrl = ConfigUtils.get("statistic_log");
		String searchApi = null;
		if (roomId!=null) {
			searchApi = getStatisticUrl + "/service/live/getLiveViewList?userId="+userId+"&roomId="+roomId+"&pageNo="+pageNo+"&pageSize="+pageSize+"&type="+type+"";
		}else {
			searchApi = getStatisticUrl + "/service/live/getLiveViewList?userId="+userId+"&pageNo="+pageNo+"&pageSize="+pageSize+"&type="+type+"";
		}
		try {
			String response = HttpUtils.sendGet(searchApi,"utf-8");
			System.out.println(response);
			com.alibaba.fastjson.JSONObject parse = (com.alibaba.fastjson.JSONObject) JSON.parse(response);
			String code = parse.getString("code");
			List<LiveVideoViewRecord> list = new ArrayList<>();
			if (code.equals("0")) {
				String data = parse.getString("data");
				System.out.println(data);
				com.alibaba.fastjson.JSONArray parseArray = JSON.parseArray(data);
				for(int i=0;i<parseArray.size();i++) {
					com.alibaba.fastjson.JSONObject obj = parseArray.getJSONObject(i);
					LiveVideoViewRecord record = new LiveVideoViewRecord();
					record.setId(obj.getString("id"));
					record.setRoomId(obj.getInteger("roomId"));
					record.setStartTime(obj.getString("startTime"));
					record.setEndTime(obj.getString("endTime"));
					record.setDuration(obj.getFloat("duration"));
					record.setDurationStr(obj.getString("durationStr"));
					list.add(record);
				}
				page = new Pagination(pageNo, pageSize, parse.getIntValue("totalCount"), list);
				model.addAttribute("code", "0");
				model.addAttribute("page", page);
			}else {
				page = new Pagination(pageNo, pageSize, 0, list);
				model.addAttribute("code", "1");
			}
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("code", "1");
		}
		
		model.addAttribute("userId", userId);
		model.addAttribute("roomId", roomId);
		return "userecord/icontent/liveviewlist";
	}
	
	/**
	 * 回看观看列表
	 * @param userId
	 * @param roomId
	 * @param pageNo
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value="videoviewlist")
	public String videoviewlist(Long userId,Long videoId,Integer pageNo,Integer pageSize,Model model) {
		//type=1直播   2回看
		int type = 2;
		pageNo = pageNo==null?1:pageNo;
		pageSize = pageSize==null?10:pageSize;
		Pagination page = null;
		//统计地址
		String getStatisticUrl = ConfigUtils.get("statistic_log");
		String searchApi = null;
		if (videoId!=null) {
			searchApi = getStatisticUrl + "/service/live/getLiveViewList?userId="+userId+"&videoId="+videoId+"&pageNo="+pageNo+"&pageSize="+pageSize+"&type="+type+"";
		}else {
			searchApi = getStatisticUrl + "/service/live/getLiveViewList?userId="+userId+"&pageNo="+pageNo+"&pageSize="+pageSize+"&type="+type+"";
		}
		try {
			String response = HttpUtils.sendGet(searchApi,"utf-8");
			com.alibaba.fastjson.JSONObject parse = (com.alibaba.fastjson.JSONObject) JSON.parse(response);
			String code = parse.getString("code");
			List<LiveVideoViewRecord> list = new ArrayList<>();
			if (code.equals("0")) {
				String data = parse.getString("data");
				com.alibaba.fastjson.JSONArray parseArray = JSON.parseArray(data);
				for(int i=0;i<parseArray.size();i++) {
					com.alibaba.fastjson.JSONObject obj = parseArray.getJSONObject(i);
					LiveVideoViewRecord record = new LiveVideoViewRecord();
					record.setId(obj.getString("id"));
					record.setVideoId(obj.getLong("videoId"));
					record.setStartTime(obj.getString("startTime"));
					list.add(record);
				}
				page = new Pagination(pageNo, pageSize, parse.getIntValue("totalCount"), list);
				model.addAttribute("code", "0");
				model.addAttribute("page", page);
			}else {
				page = new Pagination(pageNo, pageSize, 0, list);
				model.addAttribute("code", "1");
			}
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("code", "1");
		}
		
		model.addAttribute("userId", userId);
		model.addAttribute("videoId", videoId);
		return "userecord/icontent/videoviewlist";
	}
	
	@RequestMapping(value="exportLiveViewData")
	public void exportLiveViewData(Long userId,Integer roomId,HttpServletResponse response1) {
		try {
			//type=1直播   2回看
			int type = 1;
			//统计地址
			String getStatisticUrl = ConfigUtils.get("statistic_log");
			String searchApi = null;
			if (roomId!=null) {
				searchApi = getStatisticUrl + "/service/live/getLiveViewList4Export?userId="+userId+"&roomId="+roomId+"&type="+type+"";
			}else {
				searchApi = getStatisticUrl + "/service/live/getLiveViewList4Export?userId="+userId+"&type="+type+"";
			}
			String response = HttpUtils.sendGet(searchApi,"utf-8");
			com.alibaba.fastjson.JSONObject parse = (com.alibaba.fastjson.JSONObject) JSON.parse(response);
			String code = parse.getString("code");
			//要导出的list
			List<LiveVideoViewRecord> list = new ArrayList<>();
			if (code.equals("0")) {
				String data = parse.getString("data");
				com.alibaba.fastjson.JSONArray parseArray = JSON.parseArray(data);
				
				//创建excel表
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("直播观看列表");
				HSSFRow rowHeader = sheet.createRow(0);
				rowHeader.createCell(0).setCellValue("直播id");
				rowHeader.createCell(1).setCellValue("开始观看时间");
				rowHeader.createCell(2).setCellValue("结束观看时间");
				rowHeader.createCell(3).setCellValue("持续时间");
				
				for(int i=0;i<parseArray.size();i++) {
					HSSFRow row = sheet.createRow(i+1);
					com.alibaba.fastjson.JSONObject obj = parseArray.getJSONObject(i);
					
					row.createCell(0).setCellValue(obj.getLong("roomId"));
					row.createCell(1).setCellValue(obj.getString("startTime"));
					row.createCell(2).setCellValue(obj.getString("endTime")==null?"":obj.getString("endTime"));
					row.createCell(3).setCellValue(obj.getString("durationStr")==null?"":obj.getString("durationStr"));
				}
				
				try {
					this.setResponse(response1, "直播观看列表.xls");
					OutputStream os = response1.getOutputStream();
					workbook.write(os);
			        os.flush();
			        os.close();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				//TODO
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="exportVideoViewData")
	public void exportVideoViewData(Long userId,Long videoId,HttpServletResponse response1) {
		try {
			//type=1直播   2回看
			int type = 2;
			//统计地址
			String getStatisticUrl = ConfigUtils.get("statistic_log");
			String searchApi = null;
			if (videoId!=null) {
				searchApi = getStatisticUrl + "/service/live/getLiveViewList4Export?userId="+userId+"&videoId="+videoId+"&type="+type+"";
			}else {
				searchApi = getStatisticUrl + "/service/live/getLiveViewList4Export?userId="+userId+"&type="+type+"";
			}
			String response = HttpUtils.sendGet(searchApi,"utf-8");
			com.alibaba.fastjson.JSONObject parse = (com.alibaba.fastjson.JSONObject) JSON.parse(response);
			String code = parse.getString("code");
			//要导出的list
			List<LiveVideoViewRecord> list = new ArrayList<>();
			if (code.equals("0")) {
				String data = parse.getString("data");
				com.alibaba.fastjson.JSONArray parseArray = JSON.parseArray(data);
				
				//创建excel表
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("回看观看列表");
				HSSFRow rowHeader = sheet.createRow(0);
				rowHeader.createCell(0).setCellValue("回看id");
				rowHeader.createCell(1).setCellValue("观看时间");
				
				for(int i=0;i<parseArray.size();i++) {
					HSSFRow row = sheet.createRow(i+1);
					com.alibaba.fastjson.JSONObject obj = parseArray.getJSONObject(i);
					
					row.createCell(0).setCellValue(obj.getLong("videoId"));
					row.createCell(1).setCellValue(obj.getString("startTime"));
				}
				
				try {
					this.setResponse(response1, "回看观看列表.xls");
					OutputStream os = response1.getOutputStream();
					workbook.write(os);
			        os.flush();
			        os.close();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				//TODO
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
