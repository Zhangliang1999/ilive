package com.bvRadio.iLive.iLive.action.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.core.web.WebErrors;
import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.manager.BBSDiyformMng;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Controller
public class BBSDiyformAct {

	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;

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
		// WebErrors errors = validateEdit(roomId, request);
		// if (errors.hasErrors()) {
		// return errors.showErrorPage(model);
		// }
		ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
		model.addAttribute("iLiveLiveRoom", iliveRoom);
		model.addAttribute("leftActive", "2_4");
		model.addAttribute("topActive", "1");
		BBSDiyform diyform = bbsDiyformMng.findById(iliveRoom.getLiveEvent().getFormId());
		if (diyform != null) {
			model.addAttribute("bbsDiyform", diyform);
			model.addAttribute("bbsDiymodelList", bbsDiymodelMng.getListByDiyformId(diyform.getDiyformId()));
			return "diyform/edit";
		} else {
			return "diyform/add";
		}
	}

	@RequestMapping("/diyform/o_save.do")
	public void save(String resultJson, String formType,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//		System.out.println(resultJson);
		BBSDiyform bbsDiyform = new BBSDiyform();
		bbsDiyform.setFormType(formType);
		List<BBSDiymodel> bbsDiymodelList = null;
		BBSDiyform diyform = null;
		try {
			JSONObject json = JSONObject.fromObject(resultJson);
			if("1".equals(formType)){
				String diyformName = json.getString("diyformName");
				String startTime = json.getString("startTime");
				if(StringUtils.isNotBlank(startTime)){
					bbsDiyform.setVoteStartTime(Timestamp.valueOf(startTime));
				}
				String endTime = json.getString("endTime");
				if(StringUtils.isNotBlank(endTime)){
					bbsDiyform.setVoteEndTime(Timestamp.valueOf(endTime));				
				}
				String voteCount = json.getString("maxOption");
				String voteResult = json.getString("voteResult");
				bbsDiyform.setDiyformName(diyformName);
				if(StringUtils.isNumeric("1234567890")){
					bbsDiyform.setVoteCount(Integer.parseInt(voteCount));				
				}
				if(StringUtils.isNotBlank(voteResult)&&!"undefined".equals(voteResult)&&"1".equals(voteResult)){
					bbsDiyform.setAllowSeeResult(1);				
				}else{
					bbsDiyform.setAllowSeeResult(0);								
				}
			}
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
				diyform = bbsDiyformMng.save(bbsDiyform, bbsDiymodelList);
				liveEvent.setFormId(diyform.getDiyformId());
				iLiveEventMng.updateILiveEvent(liveEvent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, "{\"status\":\"success\",\"diyformId\":" + diyform.getDiyformId() + "}");
	}

	@RequestMapping("/diyform/o_update.do")
	public void update(String resultJson, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		System.out.println(resultJson);
		try {
			JSONObject json = JSONObject.fromObject(resultJson);
			String fieldCount = (String) json.get("fieldCount");
			String diyformId = json.getString("diyformId");
			if (Integer.parseInt(fieldCount) > 0) {
				BBSDiyform bbsDiyform = new BBSDiyform();
				bbsDiyform.setDiyformId(Integer.parseInt(diyformId));
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
				BBSDiyform diyform = bbsDiyformMng.update(bbsDiyform, bbsDiymodelList);
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
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
				String helpPosition;
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
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
				String needAnswer = fieldJson.getString("needAnswer");
				bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
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
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
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
				String defValue = fieldJson.getString("defValue");
				bbsDiymodel.setDefValue(defValue);
				try {
					String needAnswer = fieldJson.getString("needAnswer");
					bbsDiymodel.setNeedAnswer(Integer.parseInt(needAnswer));
					bbsDiymodel.setHelpPosition(Integer.parseInt(helpPosition));
				} catch (NumberFormatException e) {
					bbsDiymodel.setHelpPosition(4);
				}
				bbsDiymodel.setDiyOrder(Integer.parseInt(order));

			} else if (null != fieldType && "37".equals(fieldType)) {

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
}