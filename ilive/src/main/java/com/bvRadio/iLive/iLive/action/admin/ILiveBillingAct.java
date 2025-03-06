package com.bvRadio.iLive.iLive.action.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.page.SimplePage;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveFCodeMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.util.ExcelUtils;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Controller
@RequestMapping(value = "billing")
public class ILiveBillingAct {
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	@Autowired
	private ILiveFCodeMng iLiveFCodeMng;

	/**
	 * F码设置
	 * 
	 * @return
	 */
	@RequestMapping(value = "list.do")
	public String list(Model model, Integer roomId, Long fileId, String startTime, String endTime, Integer pageNo,
			Date fStartBindTime, Date fEndBindTime, Integer tab) {
		String listUrl = ConfigUtils.get("billing_flow_page_url");
		if (null != fileId) {
			listUrl = StringUtils.replace(listUrl, ":flagId", String.valueOf(fileId));
			listUrl = StringUtils.replace(listUrl, ":flowType", String.valueOf(4));
		} else if (null != roomId) {
			listUrl = StringUtils.replace(listUrl, ":flagId", String.valueOf(roomId));
			listUrl = StringUtils.replace(listUrl, ":flowType", String.valueOf(3));
		} else {
			return null;
		}
		if (null != startTime) {
			listUrl = StringUtils.replace(listUrl, ":startTime", startTime);
		} else {
			listUrl = StringUtils.replace(listUrl, ":startTime", "");
		}
		if (null != endTime) {
			listUrl = StringUtils.replace(listUrl, ":endTime", endTime);
		} else {
			listUrl = StringUtils.replace(listUrl, ":endTime", "");
		}
		if (null != pageNo) {
			if(null == tab || tab.equals(1)) {
				listUrl = StringUtils.replace(listUrl, ":pageNo", String.valueOf(pageNo));
			}else {
				listUrl = StringUtils.replace(listUrl, ":pageNo", String.valueOf(1));
			}
		} else {
			listUrl = StringUtils.replace(listUrl, ":pageNo", "");
		}
		try {
			String listResult = HttpUtils.sendGet(listUrl, "utf-8");
			JSONObject jsonObject = new JSONObject(listResult);
			if (jsonObject.has("data")) {
				String dataJson = jsonObject.get("data").toString();
				model.addAttribute("pageJson", dataJson);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("roomId", roomId);
		model.addAttribute("fileId", fileId);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("fStartBindTime", fStartBindTime);
		model.addAttribute("fEndBindTime", fEndBindTime);
		if (null == tab) {
			model.addAttribute("tabActive", "1");
		} else if (tab.equals(1)) {
			model.addAttribute("tabActive", "1");
		} else if (tab.equals(2)) {
			model.addAttribute("tabActive", "2");
		}
		Pagination fcodePage;
		if (null != tab && tab.equals(2)) {
			fcodePage = iLiveFCodeMng.pageByParams(null, null, ILiveFCode.STATUS_USED, roomId, fileId, fStartBindTime,
					fEndBindTime, SimplePage.cpn(pageNo), 20);
		} else {
			fcodePage = iLiveFCodeMng.pageByParams(null, null, ILiveFCode.STATUS_USED, roomId, fileId, fStartBindTime,
					fEndBindTime, 1, 20);
		}
		model.addAttribute("fcodePage", fcodePage);
		if (null != fileId) {
			model.addAttribute("topActive", "2");
			model.addAttribute("leftActive", "1");
		} else if (null != roomId) {
			ILiveLiveRoom iLiveLiveRoom = iLiveLiveRoomMng.findById(roomId);
			ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
			model.addAttribute("liveEvent", liveEvent);
			model.addAttribute("iLiveLiveRoom", iLiveLiveRoom);
			model.addAttribute("topActive", "1");
			model.addAttribute("leftActive", "2_3");
		}
		return "billing/page";
	}

	@RequestMapping(value = "exportBilling.do")
	public void exportBilling(Integer roomId, Long fileId, String startTime, String endTime, HttpServletResponse response) {
		String listUrl = ConfigUtils.get("billing_flow_list_url");
		if (null != fileId) {
			listUrl = StringUtils.replace(listUrl, ":flagId", String.valueOf(fileId));
			listUrl = StringUtils.replace(listUrl, ":flowType", String.valueOf(4));
		} else if (null != roomId) {
			listUrl = StringUtils.replace(listUrl, ":flagId", String.valueOf(roomId));
			listUrl = StringUtils.replace(listUrl, ":flowType", String.valueOf(3));
		} else {
			return;
		}
		if (null != startTime) {
			listUrl = StringUtils.replace(listUrl, ":startTime", startTime);
		} else {
			listUrl = StringUtils.replace(listUrl, ":startTime", "");
		}
		if (null != endTime) {
			listUrl = StringUtils.replace(listUrl, ":endTime", endTime);
		} else {
			listUrl = StringUtils.replace(listUrl, ":endTime", "");
		}
		try {
			String listResult = HttpUtils.sendGet(listUrl, "utf-8");
			JSONObject jsonObject = new JSONObject(listResult);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			if (jsonObject.has("data")) {
				JSONArray dataJsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < dataJsonArray.length(); i++) {
					try {
						JSONObject beanJson = dataJsonArray.getJSONObject(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("userId", beanJson.get("userId"));
						map.put("userName", beanJson.get("userName"));
						map.put("flowPrice", beanJson.get("flowPrice"));
						map.put("mobile", beanJson.get("mobile"));
						map.put("flowTime", beanJson.get("flowTime"));
						dataList.add(map);
					} catch (Exception e) {
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileName = "付费观看记录_" + sdf.format(new Date());
			String[] keys = { "userId", "userName", "flowPrice", "mobile", "flowTime" };
			String[] columnNames = { "付费人ID", "付费人昵称", "支付金额", "付费绑定手机号", "支付时间" };
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList , keys), keys, columnNames)
			.write(os);
			ResponseUtils.downloadHandler(fileName, os, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "exportFcode.do")
	public void exportFcode(Integer roomId, Long fileId, Date fStartBindTime, Date fEndBindTime, HttpServletResponse response) {
		try {
			List<ILiveFCode> list = iLiveFCodeMng.listByParams(null, null, ILiveFCode.STATUS_USED, roomId, fileId, fStartBindTime,
					fEndBindTime);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				try {
					ILiveFCode bean = list.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					Long bindUserId = bean.getBindUserId();
					map.put("bindUserId", bindUserId);
					ILiveManager bindUser = bean.getBindUser();
					if (null != bindUser) {
						String userName = bindUser.getUserName();
						String mobile = bindUser.getMobile();
						map.put("userName", userName);
						map.put("mobile", mobile);
					}
					String code = bean.getCode();
					map.put("code", code);
					String codeName = bean.getCodeName();
					map.put("codeName", codeName);
					Timestamp bindTime = bean.getBindTime();
					map.put("bindTime", bindTime);
					dataList.add(map);
				} catch (Exception e) {
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileName = "邀请码观看记录_" + sdf.format(new Date());
			String[] keys = { "bindUserId", "userName", "code", "codeName", "mobile", "bindTime" };
			String[] columnNames = { "激活人ID", "激活人昵称", "邀请码", "邀请码所属", "绑定手机号", "激活时间" };
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList , keys), keys, columnNames)
			.write(os);
			ResponseUtils.downloadHandler(fileName, os, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
