package com.bvRadio.iLive.iLive.action.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.core.web.WebErrors;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiyformName;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.BBSDiyformDataMng;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiyformNameService;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.util.ExcelUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class BBSDiyformAct {

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;
	
	@Autowired
	private WorkLogMng workLogMng;
	
	@Autowired
	private BBSDiyformNameService diyformNameService;

	@RequestMapping(value = "form/statics.do")
	public String statics(Integer roomId,Integer mark,Date startTime, Date endTime, Integer pageNo, Integer pageSize, ModelMap model) {
		ILiveLiveRoom findById = iLiveRoomMng.findById(roomId);
		
		List<ILiveEvent> listEvent = iLiveEventMng.getListEvent(roomId);
		ILiveEvent liveEvent =listEvent.get(0);
		Integer formId = liveEvent.getFormId();
		Pagination page;		
		Integer num = 0;
		pageSize = pageSize == null ? 20 : pageSize;
		pageNo = pageNo == null ? 1 : pageNo;
		List<String[]> resultList = new ArrayList<>();
		
		BBSDiyform diyfrom = bbsDiyformMng.getDiyfromById(formId);
		if (diyfrom==null) {
			pageSize = pageSize == null ? 20 : pageSize;
			pageNo = pageNo == null ? 1 : pageNo;
			List<BBSDiymodel> listByDiyformModel = bbsDiymodelMng.getListByDiyformId(formId);
			Pagination distinctDiyformUserPage = formDataMng.distinctDiyformSubmitId(formId, startTime, endTime, pageNo, pageSize);
			Integer[] questionIds = new Integer[listByDiyformModel.size()];
			for (int i = 0; i < questionIds.length; i++) {
				questionIds[i] = listByDiyformModel.get(i).getDiymodelId();
			}
			// 表格头部
			String[] headList = new String[listByDiyformModel.size() + 1];
			int size = headList.length;
			for (int i = 0; i < size; i++) {
				if (i == 0) {
					headList[i] = "无记录";
				} else {
					headList[i] = listByDiyformModel.get(i - 1).getDiymodelTitle();
				}
			}
			if (distinctDiyformUserPage != null) {
				num = distinctDiyformUserPage.getTotalCount();
				// 报名总人数
				List<Long> distinctDiyformUser = (List<Long>) distinctDiyformUserPage.getList();
				if (null != distinctDiyformUser) {
					for (Long submitId : distinctDiyformUser) {
						String[] formStatic = formDataMng.getFormStatic(submitId, questionIds);
						if (formStatic != null) {
							resultList.add(formStatic);
						}
					}
				}

			}
			List<BBSDiymodel> list4Show = new LinkedList<>();
			model.addAttribute("headList", headList);
			page = new Pagination(pageNo, pageSize, num, resultList);
			model.addAttribute("page", page);
			model.addAttribute("totalPage", page.getTotalPage());
			model.addAttribute("roomId", roomId);
			model.addAttribute("iLiveLiveRoom", findById);
			model.addAttribute("startTime", startTime);
			model.addAttribute("listEvent", listEvent);
			model.addAttribute("endTime", endTime);
			model.addAttribute("topActive", "1");
			model.addAttribute("leftActive", "4_9");
			model.addAttribute("list4Show", list4Show);
			model.addAttribute("nowMark", null);
			return "diyform/statics";
		}
		
		if (mark == null) {
			mark = diyfrom.getMark();
		}
		List<BBSDiymodel> listByDiyformModel = bbsDiymodelMng.getListByDiyformIdAndMark(formId,mark);
		List<BBSDiyformName> listFormName= diyformNameService.getListByFormId(formId);
		//供前端选择修改次数
		List<BBSDiymodel> list4Show = new LinkedList<>();
		Integer nowMark = 0;
		if (diyfrom.getMark()==null) {
			BBSDiymodel bbsDiymodel = listByDiyformModel.get(0);
			bbsDiymodel.setUpdateMark(0);
			list4Show.add(bbsDiymodel);
		}else {
			for(int i=0;i<=diyfrom.getMark();i++) {
				BBSDiymodel m = bbsDiymodelMng.getAnyModelByFormIdAndMark(formId, i);
				list4Show.add(m);
				nowMark = i;
			}
		}
		if (mark!=null) {
			nowMark = mark;
		}
		
		Pagination distinctDiyformUserPage = formDataMng.distinctDiyformSubmitId(formId, startTime, endTime, pageNo, pageSize);
		Integer[] questionIds = new Integer[listByDiyformModel.size()];
		for (int i = 0; i < questionIds.length; i++) {
			questionIds[i] = listByDiyformModel.get(i).getDiymodelId();
		}
		// 表格头部
		String[] headList = new String[listByDiyformModel.size() + 1];
		int size = headList.length;
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				headList[i] = "提交时间";
			} else {
				headList[i] = listByDiyformModel.get(i - 1).getDiymodelTitle();
			}
		}
		if (distinctDiyformUserPage != null) {
			num = distinctDiyformUserPage.getTotalCount();
			// 报名总人数
			List<Long> distinctDiyformUser = (List<Long>) distinctDiyformUserPage.getList();
			if (null != distinctDiyformUser) {
				for (Long submitId : distinctDiyformUser) {
					String[] formStatic = formDataMng.getFormStatic(submitId, questionIds);
					if (formStatic != null) {
						resultList.add(formStatic);
					}
				}
			}

		}
		model.addAttribute("headList", headList);
		page = new Pagination(pageNo, pageSize, num, resultList);
		
		model.addAttribute("liveEvent", liveEvent);
		model.addAttribute("iLiveLiveRoom", findById);
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("roomId", roomId);
		model.addAttribute("nowMark", nowMark);
		model.addAttribute("startTime", startTime);
		model.addAttribute("listEvent", listEvent);
		model.addAttribute("list4Show", list4Show);
		model.addAttribute("listFormName", listFormName);
		model.addAttribute("endTime", endTime);
		model.addAttribute("topActive", "1");
		model.addAttribute("leftActive", "4_9");
		return "diyform/statics";
	}

	@RequestMapping(value = "form/statics_export.do")
	public void staticsExport(Integer roomId, Date startTime, Date endTime, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		ILiveLiveRoom findById = iLiveRoomMng.findById(roomId);
		ILiveEvent liveEvent = findById.getLiveEvent();
		Integer formId = liveEvent.getFormId();
		Pagination page;
		if (formId == null || formId == 0) {
		} else {
			List<String[]> resultList = new ArrayList<>();
			List<BBSDiymodel> listByDiyformModel = bbsDiymodelMng.getListByDiyformId(formId);
			List<Long> distinctDiyformUserList = formDataMng.distinctDiyformSubmitId(formId, startTime, endTime);
			Integer[] questionIds = new Integer[listByDiyformModel.size()];
			for (int i = 0; i < questionIds.length; i++) {
				questionIds[i] = listByDiyformModel.get(i).getDiymodelId();
			}
			// 表格头部
			String[] headList = new String[listByDiyformModel.size() + 1];
			int size = headList.length;
			for (int i = 0; i < size; i++) {
				if (i == 0) {
					headList[i] = "提交时间";
				} else {
					headList[i] = listByDiyformModel.get(i - 1).getDiymodelTitle();
				}
			}
			if (distinctDiyformUserList != null) {
				// 报名总人数
				if (null != distinctDiyformUserList) {
					for (Long submitId : distinctDiyformUserList) {
						String[] formStatic = formDataMng.getFormStatic(submitId, questionIds);
						if (formStatic != null) {
							resultList.add(formStatic);
						}
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileName = "报名记录_" + sdf.format(new Date());
			// excel填充数据
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ExcelUtils
						.createWorkBook(ExcelUtils.createExcelRecordFromArray(resultList, headList), headList, headList)
						.write(os);
				ResponseUtils.downloadHandler(fileName, os, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	@RequestMapping("/diyform/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		List<BBSDiymodel> list = bbsDiymodelMng.getList();
		model.addAttribute("list", list);
		return "diyform/list";
	}

	@RequestMapping("/diyform/v_add.do")
	public String add(HttpServletRequest request, Integer roomId, ModelMap model) {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		model.addAttribute("iLiveLiveRoom", iliveRoom);
		model.addAttribute("leftActive", "2_4");
		model.addAttribute("topActive", "1");
		return "diyform/add";
	}

	@RequestMapping("/diyform/v_edit.do")
	public String edit(Integer roomId, HttpServletRequest request, ModelMap model) {
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		model.addAttribute("iLiveLiveRoom", iliveRoom);
		model.addAttribute("leftActive", "2_4");
		model.addAttribute("topActive", "1");
		Integer formId = iliveRoom.getLiveEvent().getFormId();
		if (formId == null) {
			return "diyform/add";
		} else {
			BBSDiyform diyform = bbsDiyformMng.findById(formId);
			if (diyform != null) {
				model.addAttribute("bbsDiyform", diyform);
				model.addAttribute("bbsDiymodelList", bbsDiymodelMng.getListByDiyformId(diyform.getDiyformId()));
				return "diyform/edit";
			} else {
				return "diyform/add";
			}
		}

	}

	@RequestMapping("/diyform/o_save.do")
	public void save(String resultJson, String formType, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		System.out.println("报名表单");
		System.out.println(resultJson);
		BBSDiyform bbsDiyform = new BBSDiyform();
		bbsDiyform.setFormType(formType);
		List<BBSDiymodel> bbsDiymodelList = null;
		BBSDiyform diyform = null;
		UserBean user = ILiveUtils.getUser(request);
		try {
			JSONObject json = JSONObject.fromObject(resultJson);
			if ("1".equals(formType)) {
				String diyformName = json.getString("diyformName");
				String activename = json.getString("activename");
				if (diyformName==null || !diyformName.equals("")) {
					diyformName = activename;
				}
				String startTime = json.getString("startTime");
				if (StringUtils.isNotBlank(startTime)) {
					bbsDiyform.setVoteStartTime(Timestamp.valueOf(startTime));
				}
				String endTime = json.getString("endTime");
				if (StringUtils.isNotBlank(endTime)) {
					bbsDiyform.setVoteEndTime(Timestamp.valueOf(endTime));
				}
				String voteCount = json.getString("maxOption");
				String voteResult = json.getString("voteResult");
				bbsDiyform.setDiyformName(diyformName);
				if (StringUtils.isNumeric("1234567890")) {
					bbsDiyform.setVoteCount(Integer.parseInt(voteCount));
				}
				if (StringUtils.isNotBlank(voteResult) && !"undefined".equals(voteResult) && "1".equals(voteResult)) {
					bbsDiyform.setAllowSeeResult(1);
				} else {
					bbsDiyform.setAllowSeeResult(0);
				}
			}
			String diyformName = json.getString("diyformName");
			String activename = json.getString("activename");
			if (diyformName==null || !diyformName.equals("")) {
				diyformName = activename;
			}
			bbsDiyform.setDiyformName(diyformName);
			bbsDiymodelList = new ArrayList<BBSDiymodel>();
			String liveEventId = json.getString("liveEventId");
			if (liveEventId != null) {
				JSONArray fieldData = json.getJSONArray("fieldData");
				for (int i = 0; i < fieldData.size(); i++) {
					try {
						JSONObject fieldJson = fieldData.getJSONObject(i);
						BBSDiymodel bbsDiymodel = parseJson2BBSDiymodel(fieldJson);
						bbsDiymodelList.add(bbsDiymodel);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				ILiveEvent liveEvent = iLiveEventMng.selectILiveEventByID(Long.parseLong(liveEventId));
				bbsDiyform.setRoomId(liveEvent.getRoomId());
				diyform = bbsDiyformMng.save(bbsDiyform, bbsDiymodelList);
				liveEvent.setFormId(diyform.getDiyformId());
				iLiveEventMng.updateILiveEvent(liveEvent);
				
				workLogMng.save(new WorkLog(WorkLog.MODEL_ENLIST, liveEvent.getLiveEventId()+"", net.sf.json.JSONObject.fromObject(liveEvent).toString(), WorkLog.MODEL_ENLIST_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, "{\"status\":\"success\",\"diyformId\":" + diyform.getDiyformId() + "}");
	}

	@RequestMapping("/diyform/o_update.do")
	public void update(String resultJson, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		System.out.println("修改报名表单");
		System.out.println(resultJson);
		try {
			JSONObject json = JSONObject.fromObject(resultJson);
			String fieldCount = (String) json.get("fieldCount");
			String diyformId = json.getString("diyformId");
			UserBean user = ILiveUtils.getUser(request);
			if (Integer.parseInt(fieldCount) > 0) {
				/*BBSDiyform bbsDiyform = new BBSDiyform();
				bbsDiyform.setDiyformId(Integer.parseInt(diyformId));*/
				List<BBSDiymodel> bbsDiymodelList = new ArrayList<BBSDiymodel>();
				JSONArray fieldData = json.getJSONArray("fieldData");
				for (int i = 0; i < fieldData.size(); i++) {
					try {
						JSONObject fieldJson = fieldData.getJSONObject(i);
						BBSDiymodel bbsDiymodel = parseJson2BBSDiymodel(fieldJson);
						try {
							String diymodelId = fieldJson.getString("diymodelId");
							bbsDiymodel.setDiymodelId(Integer.parseInt(diymodelId));
						} catch (Exception e) {
						}
						bbsDiymodelList.add(bbsDiymodel);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				BBSDiyform bbsDiyform = bbsDiyformMng.getDiyfromById(Integer.parseInt(diyformId));
				String diyformName = json.getString("diyformName");
				String activename = json.getString("activename");
				if (diyformName==null || !diyformName.equals("")) {
					diyformName = activename;
				}
				bbsDiyform.setDiyformName(diyformName);
				BBSDiyform diyform = bbsDiyformMng.update(bbsDiyform, bbsDiymodelList);
				try {
					JsonConfig config = new JsonConfig();
					config.setExcludes(new String[]{"iLiveManager"});//除去iLiveManager属性
					workLogMng.save(new WorkLog(WorkLog.MODEL_ENLIST, diyform.getDiyformId()+"", net.sf.json.JSONObject.fromObject(diyform, config).toString(), WorkLog.MODEL_ENLIST_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ResponseUtils.renderJson(response,
						"{\"status\":\"success\",\"diyformId\":" + diyform.getDiyformId() + "}");
			} else {
				ResponseUtils.renderJson(response, "{\"status\":\"success\",\"diyformId\":" + diyformId + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String delete(Integer[] ids, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		return list(request, model);
	}

	private BBSDiymodel parseJson2BBSDiymodel(JSONObject fieldJson) {
		BBSDiymodel bbsDiymodel = new BBSDiymodel();
		String fieldType;
		try {
			fieldType = fieldJson.getString("fieldType");
			bbsDiymodel.setDiymodelType(Short.parseShort(fieldType));
			if (null != fieldType && "1".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String defValue;
				try {
					defValue = fieldJson.getString("defValue");
				} catch (JSONException e) {
					defValue = "";
				}
				String optValue;
				try {
					optValue = fieldJson.getString("optValue");
				} catch (JSONException e) {
					optValue = "";
				}
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setDefValue(defValue);
				bbsDiymodel.setOptValue(optValue);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (Exception e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				try {

					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "2".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String defValue;
				try {
					defValue = fieldJson.getString("defValue");
				} catch (JSONException e) {
					defValue = "";
				}
				String optValue;
				try {
					optValue = fieldJson.getString("optValue");
				} catch (JSONException e) {
					optValue = "";
				}
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setDefValue(defValue);
				bbsDiymodel.setOptValue(optValue);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "3".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String defValue;
				try {
					defValue = fieldJson.getString("defValue");
				} catch (JSONException e) {
					defValue = "";
				}
				String optValue;
				try {
					optValue = fieldJson.getString("optValue");
				} catch (JSONException e) {
					optValue = "";
				}
				String optImgUrl;
				try {
					optImgUrl = fieldJson.getString("optImgUrl");
				} catch (JSONException e) {
					optImgUrl = "";
				}
				String helpTxt = "";
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));

				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setDefValue(defValue);
				bbsDiymodel.setOptValue(optValue);
				bbsDiymodel.setOptImgUrl(optImgUrl);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "4".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String defValue;
				try {
					defValue = fieldJson.getString("defValue");
				} catch (JSONException e) {
					defValue = "";
				}
				String optValue;
				try {
					optValue = fieldJson.getString("optValue");
				} catch (JSONException e) {
					optValue = "";
				}
				String optImgUrl;
				try {
					optImgUrl = fieldJson.getString("optImgUrl");
				} catch (JSONException e) {
					optImgUrl = "";
				}
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition = "";
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setDefValue(defValue);
				bbsDiymodel.setOptValue(optValue);
				bbsDiymodel.setOptImgUrl(optImgUrl);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "5".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String textSize;
				try {
					textSize = fieldJson.getString("textSize");
				} catch (JSONException e) {
					textSize = "0";
				}
				String defValue;
				try {
					defValue = fieldJson.getString("defValue");
				} catch (JSONException e) {
					defValue = "";
				}
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setTextSize(Integer.parseInt(textSize));
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setDefValue(defValue);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "6".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String textSize;
				try {
					textSize = fieldJson.getString("textSize");
				} catch (JSONException e) {
					textSize = "0";
				}
				String defValue;
				try {
					defValue = fieldJson.getString("defValue");
				} catch (JSONException e) {
					defValue = "";
				}
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition = "";
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order;
				try {
					order = fieldJson.getString("order");
				} catch (JSONException e) {
					order = "0";
				}
				String areaRows;
				try {
					areaRows = fieldJson.getString("areaRows");
				} catch (JSONException e) {
					areaRows = "0";
				}
				String areaCols;
				try {
					areaCols = fieldJson.getString("areaCols");
				} catch (JSONException e) {
					areaCols = "0";
				}
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setTextSize(Integer.parseInt(textSize));
				bbsDiymodel.setAreaRows(Integer.parseInt(areaRows));
				bbsDiymodel.setAreaCols(Integer.parseInt(areaCols));
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setDefValue(defValue);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "7".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "8".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String defValue;
				try {
					defValue = fieldJson.getString("defValue");
				} catch (JSONException e) {
					defValue = "";
				}
				String optValue;
				try {
					optValue = fieldJson.getString("optValue");
				} catch (JSONException e) {
					optValue = "";
				}
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setDefValue(defValue);
				bbsDiymodel.setOptValue(optValue);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "9".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "10".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "11".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				try {
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "12".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
				}
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "13".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "14".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition;
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				try {
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			} else if (null != fieldType && "27".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition = "";
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				Integer needMsgValid = 0;
				try {
					needMsgValid = fieldJson.getInt("needMsgValid");
					bbsDiymodel.setNeedMsgValid(needMsgValid);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				String defValue = fieldJson.getString("defValue");
				bbsDiymodel.setDefValue(defValue);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));

			} else if (null != fieldType && "37".equals(fieldType)) {
				String fieldTitle = fieldJson.getString("fieldTitle");
				String fieldKey = fieldJson.getString("fieldKey");
				String helpTxt;
				try {
					helpTxt = fieldJson.getString("helpTxt");
				} catch (JSONException e) {
					helpTxt = "";
				}
				String helpPosition = "";
				try {
					helpPosition = fieldJson.getString("helpPosition");
				} catch (JSONException e) {
					helpPosition = "4";
				}
				String order = fieldJson.getString("order");
				bbsDiymodel.setDiymodelTitle(fieldTitle);
				bbsDiymodel.setDiymodelKey(fieldKey);
				bbsDiymodel.setHelpTxt(helpTxt);
				String defValue = fieldJson.getString("defValue");
				bbsDiymodel.setDefValue(defValue);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
				} catch (NumberFormatException e) {
					bbsDiymodel.setNeedAnswer(0);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bbsDiymodel;
	}

	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = new WebErrors();
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = new WebErrors();
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BBSDiyform entity = bbsDiyformMng.findById(id);
		if (errors.ifNotExist(entity, BBSDiyform.class, id)) {
			return true;
		}
		return false;
	}

	@Autowired
	private BBSDiymodelMng bbsDiymodelMng;
	@Autowired
	private BBSDiyformMng bbsDiyformMng;

	@Autowired
	private BBSDiyformDataMng formDataMng;
}