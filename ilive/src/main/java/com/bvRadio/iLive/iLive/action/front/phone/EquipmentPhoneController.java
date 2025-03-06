package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.EquipmentOrder;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.EquipmentOrderService;
import com.bvRadio.iLive.iLive.manager.EquipmentService;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;

import net.sf.json.JSONObject;

@RequestMapping(value="equipment")
@Controller
public class EquipmentPhoneController {

	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private EquipmentOrderService equipmentOrderService;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@RequestMapping(value="save.jspx")
	public void addEquipment(EquipmentOrder order,Long userId,String city,String params
			,HttpServletResponse response,HttpServletRequest request,Long equipmentId) throws UnsupportedEncodingException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		JSONObject resultJson = new JSONObject();
		System.out.println(order.toString());
		try {
			if(userId!=null&&!userId.equals("")) {
				ILiveManager user = iLiveManagerMng.getILiveManager(userId);
				order.setApplyAccount(user.getMobile());	
			}
			equipmentOrderService.save(order);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "添加工单成功");
			resultJson.put("data",new JSONObject());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "添加工单失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
}
