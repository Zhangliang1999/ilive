package com.bvRadio.iLive.iLive.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.Equipment;
import com.bvRadio.iLive.iLive.entity.EquipmentOrder;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.EquipmentOrderService;
import com.bvRadio.iLive.iLive.manager.EquipmentService;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.util.ResultUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONObject;

/**
 * 设备  工单
 * @author Wei
 *
 */
@Controller
@RequestMapping(value="equipment")
public class EquipmentController {

	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private EquipmentOrderService equipmentOrderService;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	/**
	 * 设备信息列表
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="equiplist")
	public String equipmentList(Integer rentOrSell,String name,Integer pageNo,Integer pageSize,HttpServletRequest request,Model model) {
		UserBean user = ILiveUtils.getUser(request);
		Pagination page = equipmentService.getPage(Long.parseLong(user.getUserId()),name, pageNo==null?1:pageNo, 10,rentOrSell);
		
		model.addAttribute("page",page);
		model.addAttribute("list",page.getList());
		model.addAttribute("pageNum",page.getTotalPage());
		model.addAttribute("topActive", "6");
		if(rentOrSell==2) {
			System.out.println("rentOrSell======="+rentOrSell);
			model.addAttribute("leftActive","1_2");
			return "equip/equiplist";
		}else {
			model.addAttribute("leftActive","1_3");
			return "equip/equiplist2";
		}
	}
	
	/**
	 * 我的设备工单
	 * @param status
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="orderList")
	public String orderList(Integer status,Integer type,Integer pageNo,Integer pageSize,HttpServletRequest request,Model model) {
		UserBean user = ILiveUtils.getUser(request);
		Long userId =Long.parseLong(user.getUserId());
		Pagination page = equipmentOrderService.getPage(userId,status, type, pageNo==null?1:pageNo, 10);
		model.addAttribute("status",status==null?0:status);
		model.addAttribute("type",type==null?0:type);
		model.addAttribute("page",page);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "1_4");
		return "equip/workorderlist";
	}
	
	/**
	 * 工单详情
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="detail")
	public String orderDetail(Model model,Long id) {
		EquipmentOrder equipmentOrder = equipmentOrderService.getById(id);
		Equipment equipment = equipmentService.getById(equipmentOrder.getEquipmentId());
		model.addAttribute("equipmentOrder", equipmentOrder);
		model.addAttribute("equipment", equipment);
		model.addAttribute("leftActive", "10");
		switch (equipmentOrder.getType()) {
		case 1:
			return "equip/addequipdetail";
		case 2:
			return "equip/addequipselldetail";
		case 3:
			return "equip/liveserverdetail";
		}
		return "";
	}
	
	/**
	 * 关闭工单
	 * @param id
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="closeOrder",method=RequestMethod.POST)
	public void closeOrder(Long id,HttpServletResponse response) {
		JSONObject result;
		try {
			EquipmentOrder equipment = equipmentOrderService.getById(id);
			equipment.setStatus(2);
			equipmentOrderService.update(equipment);
			result = ResultUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			result = ResultUtils.error();
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 添加工单页面
	 * @param equipmentOrder
	 * @param response
	 */
	@RequestMapping(value="toordersave")
	public String tosubimtOrder(Long id,Integer rentOrSell,Model model) {
		Equipment equipment = equipmentService.getById(id);
		model.addAttribute("equipment", equipment);
		model.addAttribute("rentOrSell", rentOrSell);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", rentOrSell==1?'8':'9');
		return rentOrSell==1?"equip/addequip":"equip/addequipsell";
	}
	
	/**
	 * 直播服务页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toliveserver")
	public String liveservice(Model model) {
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "1_1");
		return "equip/liveserver";
	}
	
	/**
	 * 添加工单
	 * @param equipmentOrder
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="save",method=RequestMethod.POST)
	public void add(EquipmentOrder equipmentOrder,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result;
		try {
			UserBean user = ILiveUtils.getUser(request);
			Long userId =Long.parseLong(user.getUserId());
			ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
			equipmentOrder.setUserId(userId);
			equipmentOrder.setApplyAccount(iLiveManager.getMobile());
			equipmentOrderService.save(equipmentOrder);
			result = ResultUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			result = ResultUtils.error();
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 删除工单
	 * @param equipmentOrder
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public void delete(Long id,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result;
		try {
			EquipmentOrder order = equipmentOrderService.getById(id);
			order.setIsDel(1);
			equipmentOrderService.update(order);
			result = ResultUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			result = ResultUtils.error();
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
}
