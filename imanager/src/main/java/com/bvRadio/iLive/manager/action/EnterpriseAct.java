package com.bvRadio.iLive.manager.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.manager.entity.ILiveCertTopic;
import com.bvRadio.iLive.manager.entity.WorkLog;
import com.bvRadio.iLive.manager.manager.ILiveCertTopicMng;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.manager.manager.WorkLogMng;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "enterprise")
public class EnterpriseAct {

	/**
	 * 认证中
	 */
	private static final Integer CERT_ING = 1;

	//
	private String success_message = "企业认证通过";

	@Autowired
	private ILiveEnterpriseMng enterpriseMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private ILiveCertTopicMng iLiveCertTopicMng;
	
	@Autowired
	private WorkLogMng workLogMng;
	
	@Autowired
	private ILiveEventMng iLiveEventMng;
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;

	/**
	 * 企业概览
	 * 
	 * @return
	 */
	@RequestMapping(value = "overview.do")
	public String overview(ModelMap modelMap, HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		System.out.println(user);
		modelMap.addAttribute("topActive", "3");
		modelMap.addAttribute("leftActive", "1");
		return "enterprise/overview";
	}

	/**
	 * 未审核企业列表
	 */
	@RequestMapping(value = "uncheck/list.do")
	public String unCheckEnterpriseList(ModelMap model, Integer pageNo, Integer pageSize, String enterpriseType,
			String content) {
		List<Integer> certStatusList = new ArrayList<>();
		certStatusList.add(CERT_ING);
		//certStatusList.add(CERT_FAIL);
		Pagination pagination = enterpriseMng.getILiveEnterprisesByCertStatusByList(certStatusList, enterpriseType,
				content, pageNo, pageSize, 1);

		if (enterpriseType == null || enterpriseType == "") {
			enterpriseType = "0";
		}
		if (content == null) {
			content = "";
		}
		model.addAttribute("enterpriseType", enterpriseType);
		model.addAttribute("content", content);
		model.addAttribute("pagination", pagination);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "1");
		return "enterprise/uncheck";
	}

	/**
	 * 未审核个人
	 */
	@RequestMapping(value = "uncheck/listperson.do")
	public String unCheckPersonList(ModelMap model, Integer pageNo, Integer pageSize, String enterpriseType,
			String content) {
		List<Integer> certStatusList = new ArrayList<>();
		certStatusList.add(CERT_ING);
		//certStatusList.add(CERT_FAIL);
		Pagination pagination = enterpriseMng.getILiveEnterprisesByCertStatusByList(certStatusList, enterpriseType,
				content, pageNo, pageSize, 0);

		if (enterpriseType == null || enterpriseType == "") {
			enterpriseType = "0";
		}
		if (content == null) {
			content = "";
		}
		model.addAttribute("enterpriseType", enterpriseType);
		model.addAttribute("content", content);
		model.addAttribute("pagination", pagination);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "2");
		return "enterprise/uncheckperson";
	}

	/**
	 * 企业管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "manager/list.do")
	public String manager(Model model, Integer pageNo, Integer pageSize, String content, String enterprisetype,
			ILiveEnterprise iLiveEnterprise) {
		if (iLiveEnterprise == null) {
			iLiveEnterprise = new ILiveEnterprise();
			iLiveEnterprise.setDisabled(-1);
			iLiveEnterprise.setStamp(-1);
			iLiveEnterprise.setEnterpriseType("-1");
		}
		Pagination pagination = enterpriseMng.getPage(iLiveEnterprise, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "3");
		model.addAttribute("content", content);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("iLiveEnterprise", iLiveEnterprise);
		return "enterprise/cert_manager";
	}
	/**
	 * 领航自动续购企业管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "manager/autolist.do")
	public String autoBuyList(Model model, Integer pageNo, Integer pageSize, String content, String enterprisetype,
			ILiveEnterprise iLiveEnterprise) {
		if (iLiveEnterprise == null) {
			iLiveEnterprise = new ILiveEnterprise();
			iLiveEnterprise.setDisabled(-1);
			iLiveEnterprise.setStamp(-1);
			iLiveEnterprise.setEnterpriseType("-1");
		}
		Pagination pagination = enterpriseMng.getautoPage(iLiveEnterprise, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "6");
		model.addAttribute("content", content);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("iLiveEnterprise", iLiveEnterprise);
		return "enterprise/auto_manager";
	}
	/**
	 * 企业认证 查看详细页面
	 * 
	 * @return
	 * 
	 */
	@RequestMapping(value = "certinfo.do")
	public String cert(Model model, Integer enterpriseId) {
		ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
		List<ILiveCertTopic> certTopicList = iLiveCertTopicMng.getCertTopicList(enterpriseId);
		model.addAttribute("enterprise", enterprise);
		model.addAttribute("certTopicList", certTopicList);
		model.addAttribute("topActive", "3");
		if (enterprise.getStamp() == 0) {
			// 个人
			model.addAttribute("leftActive", "2");
			return "enterprise/certperson";
		} else {
			// 企业详情
			model.addAttribute("leftActive", "1");
			return "enterprise/cert";
		}
	}

	/**
	 * 企业认证页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "ecert.do")
	public String ecert(Model model, String enterprisetype, Integer pageNo, String content) {
		int pass = 0;
		Pagination pagination = enterpriseMng.getPage(enterprisetype, content, pass, pageNo, 10);
		model.addAttribute("pagination", pagination);
		model.addAttribute("passtype", 0);
		model.addAttribute("topActive", "3");
		return "enterprise/ecert";
	}

	/**
	 * 企业审核
	 * 
	 * @return
	 */
	@RequestMapping(value = "validate.do")
	public void validate(Integer enterpriseId, Integer pass,Integer type, String message, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean user = ILiveUtils.getUser(request);
		JSONObject jobj = new JSONObject();
		try {
			ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
			enterprise.setCertStatus(pass);
			enterprise.setCertTime(new Timestamp(System.currentTimeMillis()));
			enterprise.setCheckPerson(user.getNickname());
			enterprise.setAuthUserId(Long.parseLong(user.getUserId()));
			enterprise.setAutoUserName(user.getNickname());
			enterprise.setEntype(type);
			List<ILiveManager> managerList = iLiveManagerMng
					.getILiveManagerByEnterpriseId(enterprise.getEnterpriseId());
			enterpriseMng.updateEnterpriseWithPerson(enterprise, managerList);
			ILiveCertTopic certTopic = new ILiveCertTopic();
			certTopic.setUserId(enterprise.getUserId());
			certTopic.setUserPhone(enterprise.getUserPhone());
			if (pass == 5) {
				// 增加审核未过信息加原因
				certTopic.setCommentTime(new Timestamp(System.currentTimeMillis()));
				certTopic.setEnterpriseId(enterpriseId);
				certTopic.setManagerId(Long.parseLong(ILiveUtils.getUser(request).getUserId()));
				certTopic.setManagerName(ILiveUtils.getUser(request).getUsername());
				certTopic.setTopicComment(message);
				certTopic.setCertStatus(pass);
				if (enterprise.getStamp() == 1) {
					workLogMng.save(new WorkLog(WorkLog.MODEL_ENTEXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_ENTEXAMINE_NAME_NOPASS, Long.parseLong(user.getUserId()),user.getUsername(), ""));
				}else {
					workLogMng.save(new WorkLog(WorkLog.MODEL_PEREXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_PEREXAMINE_NAME_NOPASS, Long.parseLong(user.getUserId()),user.getUsername(), ""));
				}
			} else {
				// 审核通过增加一条记录
				certTopic.setCommentTime(new Timestamp(System.currentTimeMillis()));
				certTopic.setEnterpriseId(enterpriseId);
				certTopic.setManagerId(Long.parseLong(ILiveUtils.getUser(request).getUserId()));
				certTopic.setManagerName(ILiveUtils.getUser(request).getNickname());
				certTopic.setTopicComment(success_message);
				certTopic.setCertStatus(pass);
				if (enterprise.getStamp() == 1) {
					workLogMng.save(new WorkLog(WorkLog.MODEL_ENTEXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_ENTEXAMINE_NAME_PASS, Long.parseLong(user.getUserId()),user.getUsername(), ""));
				}else {
					workLogMng.save(new WorkLog(WorkLog.MODEL_PEREXAMINE, certTopic.getEnterpriseId()+"", net.sf.json.JSONObject.fromObject(certTopic).toString(), WorkLog.MODEL_PEREXAMINE_NAME_PASS, Long.parseLong(user.getUserId()),user.getUsername(), ""));
				}
			}
			
			iLiveCertTopicMng.addCertTopic(certTopic);
			jobj.put("status", "1");
		} catch (Exception e) {
			jobj.put("status", "0");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, jobj.toString());
	}
	

	/**
	 * 删除申请信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "remove.do")
	public String remove(Integer enterpriseId) {
		enterpriseMng.del(enterpriseId);
		return "";
	}

	private static final Logger logger = Logger.getLogger("企业管理");

	// 企业管理详情页
	@RequestMapping(value = "detail.do")
	public String detail(Integer enterpriseId, Model model) {
		ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
		if (enterprise.getStamp() == null) {
			enterprise.setStamp(1);
		}
		if(enterprise.getDisabled()==null) {
			enterprise.setDisabled(0);
		}
		List<ILiveManager> list = iLiveManagerMng.getILiveManagerByEnterpriseId(enterpriseId);
		logger.info(list.size());
		Iterator<ILiveManager> iterator = list.iterator();
		while (iterator.hasNext()) {
			ILiveManager next = iterator.next();
			System.out.println(next.getUserId() + "   " + next.getUserName());
		}
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("enterprise", enterprise);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "3");
		model.addAttribute("list", list);
		model.addAttribute("num", list.size());
		model.addAttribute("first", list.get(0));
		return "enterprise/manager_detail";
	}
	private Integer selectServerGroup() {
		return 100;
	}

	// 获取用户信息
	@RequestMapping(value = "userdetail.do")
	public void userdetail(Long userId, HttpServletResponse response) {
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		ResponseUtils.renderJson(response, new JSONObject(iLiveManager).toString());
	}

	// 修改用户信息
	@RequestMapping(value = "saveUser.do", method = RequestMethod.POST)
	public void saveUser(Long userId, String nailName, String userImg, HttpServletResponse response) {
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		iLiveManager.setNailName(nailName);
		iLiveManager.setUserImg(userImg);
		iLiveManagerMng.saveIliveManager(iLiveManager);
	}
	
	// 禁用企业
	@RequestMapping(value = "forbidden.do", method = RequestMethod.POST)
	public void forbidden(Integer enterpriseId,Integer status, HttpServletResponse response) {
		JSONObject res = new JSONObject();
		try {
			enterpriseMng.forbidden(enterpriseId,status);
			res.put("status", 1);
			res.put("type", status);
			ResponseUtils.renderJson(response, res.toString());
		}catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
			res.put("type", status);
			ResponseUtils.renderJson(response, response.toString());
		}
	}
	
	/**
	 * 修改状态
	 * @param enterpriseId
	 * @param state
	 * @param response
	 */
	@RequestMapping(value="editdisable")
	public void editdisable(Integer enterpriseId,Integer state,String reason,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
			enterprise.setDisabled(state);
			List<ILiveManager> manager= iLiveManagerMng.getILiveManagerByEnterpriseId(enterpriseId);
			
			if (state == 1) {
				enterprise.setForbiddenReason(reason);
				System.out.println(reason);
				//将该企业所有的账户状态设为关闭
					for(int i=0;i<manager.size();i++) {
					manager.get(i).setSubType(0);
					iLiveManagerMng.updateLiveManager(manager.get(i));
				}
				
			}else {
				//将该企业所有的账户状态设为关闭
				for(int i=0;i<manager.size();i++) {
				manager.get(i).setSubType(1);
				iLiveManagerMng.updateLiveManager(manager.get(i));
			}
			}
			enterprise.setForbiddenUser(user.getUsername());
			enterpriseMng.update(enterprise);
			result.put("code", 1);
			result.put("msg", "修改成功");
			result.put("person", user.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 0);
			result.put("msg", "修改失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 详情
	 * @param model
	 * @param enterpriseId
	 * @return
	 */
	@RequestMapping(value = "detail2.do")
	public String editenterprise(Model model, Integer enterpriseId,HttpServletRequest request) {
		
		String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }
		
		ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
		if (enterprise.getStamp() == null) {
			enterprise.setStamp(1);
		}
		if(enterprise.getDisabled()==null) {
			enterprise.setDisabled(0);
		}
		
		List<ILiveManager> list = iLiveManagerMng.getILiveManagerByEnterpriseId(enterpriseId);
		Iterator<ILiveManager> iterator = list.iterator();
		while (iterator.hasNext()) {
			ILiveManager next = iterator.next();
			if (next.getLevel() == null ||next.getLevel() == 0) {
				enterprise.setPassword(next.getUserPwd());
				enterprise.setLastIP(next.getLastIP());
			}
		}
		
		//计算直播时长和次数
		Long timelong = 0l;
		int liveCount= 0;
		List<ILiveLiveRoom> roomList = iLiveLiveRoomMng.findRoomListByEnterprise(enterpriseId);
		for(ILiveLiveRoom room:roomList) {
			List<ILiveEvent> eventList = iLiveEventMng.findAllEventByRoomId(room.getRoomId());
			for(ILiveEvent event:eventList) {
				if (event.getRealEndTime()!=null && event.getRealStartTime()!=null) {
					timelong += event.getRealEndTime().getTime() - event.getRealStartTime().getTime();
					liveCount++;
				}
			}
		}
		//时
		long hour = timelong / (1000 * 60 * 60);
		
		long minute = (timelong % (1000 * 60 * 60))/(1000 * 60);
		enterprise.setLiveCount(liveCount);
		enterprise.setLiveTimeLong(hour+"小时"+minute+"分钟");
		
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("enterprise", enterprise);
		model.addAttribute("topActive", "3");
		model.addAttribute("leftActive", "3");
		model.addAttribute("list", list);
		model.addAttribute("num", list.size());
		model.addAttribute("first", list.size()>0?list.size():null);
		return "enterprise/manager_detail2_qiye";
	}
	
	/**
	 * 提交更改
	 * @param response
	 */
	@RequestMapping(value="editsub")
	public void editsub(HttpServletResponse response,HttpServletRequest request,ILiveEnterprise enterprise) {
		JSONObject result = new JSONObject();
		try {
			ILiveEnterprise enterprise2 = enterpriseMng.getILiveEnterpriseById(enterprise.getEnterpriseId());
			if (enterprise2!=null) {
				enterprise2.setContactName(enterprise.getContactName());
				enterprise2.setContactPhone(enterprise.getContactPhone());
				enterprise2.setEnterpriseInfo(enterprise.getEnterpriseInfo());
				enterprise2.setContactEmail(enterprise.getContactEmail());
				enterprise2.setRemark(enterprise.getRemark());
				enterprise2.setEnterpriseName(enterprise.getEnterpriseName());
				enterprise2.setEnterpriseRegNo(enterprise.getEnterpriseRegNo());
				enterprise2.setEnterpriseRegName(enterprise.getEnterpriseRegName());
				enterprise2.setPassword(enterprise.getPassword());
				enterprise2.setStamp(enterprise.getStamp());
				enterprise2.setEntype(enterprise.getEntype());
				enterprise2.setCertResource(enterprise.getCertResource());
				enterprise2.setEnterpriseImg(enterprise.getEnterpriseImg());
				UserBean user = ILiveUtils.getUser(request);
				enterprise2.setLastEditPerson(user.getUsername());
				enterprise2.setLastEditTime(new Timestamp(new Date().getTime()));
				enterpriseMng.update(enterprise2);
				
				List<ILiveManager> list = iLiveManagerMng.getILiveManagerByEnterpriseId(enterprise.getEnterpriseId());
				Iterator<ILiveManager> iterator = list.iterator();
				while (iterator.hasNext()) {
					ILiveManager manager = iterator.next();
					if (manager.getLevel() == null ||manager.getLevel() == 0) {
						manager.setUserPwd(enterprise.getPassword());
						iLiveManagerMng.updateLiveManager(manager);
					}
				}
				
				
			}
			result.put("code", 0);
			result.put("msg", "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 1);
			result.put("msg", "修改失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 重置密码
	 */
	@RequestMapping(value="resetpwd")
	public void resetpwd(Integer enterpriseId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			List<ILiveManager> list = iLiveManagerMng.getILiveManagerByEnterpriseId(enterpriseId);
			Iterator<ILiveManager> iterator = list.iterator();
			while (iterator.hasNext()) {
				ILiveManager manager = iterator.next();
				if (manager.getLevel() == null ||manager.getLevel() == 0) {
					manager.setUserPwd("000000");
					iLiveManagerMng.updateLiveManager(manager);
				}
			}
			ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
			UserBean user = ILiveUtils.getUser(request);
			enterprise.setLastEditPerson(user.getUsername());
			enterprise.setLastEditTime(new Timestamp(new Date().getTime()));
			enterpriseMng.update(enterprise);
			result.put("code", "1");
			result.put("msg", "重置成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("msg", "重置失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping("editdisablemany")
	public void editdisablemany(HttpServletResponse response,HttpServletRequest request,
			String str,Integer status,String reason) {
		JSONObject result = new JSONObject();
		try {
			System.out.println(str);
			JSONArray array = JSONArray.fromObject(str);
			List<ILiveEnterprise> list = new ArrayList<>();
			for(int i=0;i<array.size();i++) {
				net.sf.json.JSONObject obj = array.getJSONObject(i);
				Integer enterpriseId = obj.getInt("id");
				ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
				enterprise.setDisabled(status);
				List<ILiveManager> manager= iLiveManagerMng.getILiveManagerByEnterpriseId(enterpriseId);
				
				if (status == 1) {
					enterprise.setForbiddenReason(reason);
					System.out.println(reason);
					//将该企业所有的账户状态设为关闭
						for(int j=0;j<manager.size();j++) {
						manager.get(j).setSubType(0);
						iLiveManagerMng.updateLiveManager(manager.get(j));
					}
					
				}else {
					//将该企业所有的账户状态设为关闭
					for(int j=0;j<manager.size();j++) {
					manager.get(j).setSubType(1);
					iLiveManagerMng.updateLiveManager(manager.get(j));
				}
				}
				UserBean user = ILiveUtils.getUser(request);
				enterprise.setLastEditPerson(user.getUsername());
				enterprise.setForbiddenUser(user.getUsername());
				enterprise.setLastEditTime(new Timestamp(new Date().getTime()));
				list.add(enterprise);
				result.put("msg", "批量操作成功");
			}
			enterpriseMng.batchUpdate(list);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "批量操作成功");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 设置备注
	 * @param response
	 * @param str
	 * @param request
	 */
	@RequestMapping(value="setbeizhu")
	public void setbeizhu(HttpServletResponse response,String str,HttpServletRequest request,String beizhu) {
		JSONObject result = new JSONObject();
		try {
			List<ILiveEnterprise> list = new ArrayList<>();
			JSONArray array = JSONArray.fromObject(str);
			for(int i=0;i<array.size();i++) {
				net.sf.json.JSONObject obj = array.getJSONObject(i);
				Integer enterpriseId = obj.getInt("id");
				ILiveEnterprise enterprise = enterpriseMng.getILiveEnterpriseById(enterpriseId);
				enterprise.setRemark(beizhu);
				UserBean user = ILiveUtils.getUser(request);
				enterprise.setLastEditPerson(user.getUsername());
				enterprise.setForbiddenUser(user.getUsername());
				enterprise.setLastEditTime(new Timestamp(new Date().getTime()));
				list.add(enterprise);
			}
			enterpriseMng.batchUpdate(list);
			
			result.put("code", "1");
			result.put("msg", "设置备注成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("msg", "设置备注失败");
		}
		ResponseUtils.renderJson(response, result.toString());
		
	}
	
	@RequestMapping(value="exportData")
	public void exportData(String uuid,HttpServletRequest request,HttpServletResponse response) {
		List<ILiveEnterprise> list = hashMap.get(uuid);
		hashMap.remove(uuid);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("企业列表");
		HSSFRow rowHead = sheet.createRow(0);
		rowHead.createCell(0).setCellValue("企业ID");
		rowHead.createCell(1).setCellValue("企业名称");
		rowHead.createCell(2).setCellValue("营业执照注册号");
		rowHead.createCell(3).setCellValue("企业状态");
		rowHead.createCell(4).setCellValue("申请人手机号");
		rowHead.createCell(5).setCellValue("认证类型");
		rowHead.createCell(6).setCellValue("联系人");
		rowHead.createCell(7).setCellValue("企业类型");
		rowHead.createCell(8).setCellValue("注册时间");
		rowHead.createCell(9).setCellValue("认证时间");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:hh:ss");
		if (list!=null) {
			for(int i=0;i<list.size();i++) {
				ILiveEnterprise en = list.get(i);
				HSSFRow row = sheet.createRow(i+1);
				row.createCell(0).setCellValue(en.getEnterpriseId());
				row.createCell(1).setCellValue(en.getEnterpriseName());
				row.createCell(2).setCellValue(en.getEnterpriseRegNo());
				row.createCell(4).setCellValue(en.getUserPhone());
				row.createCell(5).setCellValue((en.getStamp()!=null&&en.getStamp()==0)?"个人":"企业");
				row.createCell(6).setCellValue(en.getContactName());
				String entype = "";
				if (en.getEntype()==null) {
					
				}else if (en.getEntype()==1){
					entype = "外部测试";
				}else if (en.getEntype()==2) {
					entype = "内部测试";
				}else if (en.getEntype()==3) {
					entype = "签约用户";
				}
				row.createCell(7).setCellValue(entype);
				row.createCell(8).setCellValue(en.getApplyTime()!=null?format.format(en.getApplyTime()):"");
				row.createCell(9).setCellValue(en.getCertTime()!=null?format.format(en.getCertTime()):"");
				row.createCell(3).setCellValue((en.getDisabled()!=null&&en.getDisabled()==1)?"禁用":"启用");
			}
		}
		try {
			this.setResponse(response, "企业信息.xls");
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
	
	private final ConcurrentHashMap<String, List<ILiveEnterprise>>  hashMap = new ConcurrentHashMap<>(); 
	
	/**
	 * 
	 */
	@RequestMapping(value="getList")
	public void getList(ILiveEnterprise iLiveEnterprise,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			if (iLiveEnterprise == null) {
				iLiveEnterprise = new ILiveEnterprise();
				iLiveEnterprise.setDisabled(-1);
				iLiveEnterprise.setStamp(-1);
				iLiveEnterprise.setEnterpriseType("-1");
			}
			List<ILiveEnterprise> list = enterpriseMng.getList(iLiveEnterprise);
			UUID randomUUID = UUID.randomUUID();
			System.out.println(randomUUID.toString());
			hashMap.put(randomUUID.toString(), list);
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
	
	@RequestMapping(value="startDev")
	public void startDev(Integer id,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			enterpriseMng.startDev(id);
			result.put("code", "0");
			result.put("msg", "开通成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping(value="closeDev")
	public void closeDev(Integer id,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			ILiveEnterprise iLiveEnterprise = enterpriseMng.getILiveEnterpriseById(id);
			iLiveEnterprise.setIsDeveloper(0);
			enterpriseMng.update(iLiveEnterprise);
			result.put("code", "0");
			result.put("msg", "关闭成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping(value="getEnt")
	public void getEnt(Integer id,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			ILiveEnterprise iLiveEnterprise = enterpriseMng.getILiveEnterpriseById(id);
			String appId = iLiveEnterprise.getAppId();
			String secret = iLiveEnterprise.getSecret();
			result.put("appId", appId);
			result.put("secret", secret);
			result.put("code", "0");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
			result.put("msg", "操作失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 
	 * 
	 */
	@RequestMapping(value="enterpriseList")
	public String enterpriseList(Model model) {
		
		return "managercontrol/enterpriseList";
	}
	
	/**
	 * 
	 * 
	 */
	@RequestMapping(value="dataList")
	public void dataList(Model model, Integer pageNo, Integer pageSize, ILiveEnterprise iLiveEnterprise,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			if (iLiveEnterprise == null) {
				iLiveEnterprise = new ILiveEnterprise();
				iLiveEnterprise.setDisabled(0);
				iLiveEnterprise.setStamp(-1);
				iLiveEnterprise.setEnterpriseType("-1");
			}else {
				iLiveEnterprise.setDisabled(0);
				iLiveEnterprise.setStamp(-1);
				iLiveEnterprise.setEnterpriseType("-1");
			}
			Pagination pagination = enterpriseMng.getPage(iLiveEnterprise, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
			result.put("pagination", pagination);
			result.put("code", 1);
			
		} catch (Exception e) {
			result.put("code", 0);
		}
		
		ResponseUtils.renderJson(response, result.toString());
	}
	
}
