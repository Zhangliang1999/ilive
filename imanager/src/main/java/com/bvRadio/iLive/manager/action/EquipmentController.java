package com.bvRadio.iLive.manager.action;

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
import com.bvRadio.iLive.iLive.manager.EquipmentOrderService;
import com.bvRadio.iLive.iLive.manager.EquipmentService;
import com.bvRadio.iLive.iLive.util.ResultUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="equipment")
public class EquipmentController {

	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private EquipmentOrderService equipmentOrderService;
	
	/**
	 * 工单列表  
	 * @param model
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="equlplist")
	public String equlpselllist(Model model,Integer type,Integer pageNo,Integer pageSize) {
		
		Pagination page = equipmentOrderService.getAllPageByType(type,pageNo==null?1:pageNo,10);
		
		model.addAttribute("type", type);
		model.addAttribute("page", page);
		model.addAttribute("topActive", "7");
		switch(type) {
			case 1:model.addAttribute("leftActive", "2");break;
			case 2:model.addAttribute("leftActive", "1");break;
			case 3:model.addAttribute("leftActive", "3");break;
		}
		return "equip/equlplist";
	}
	
	/**
	 * 设备库存
	 * @param model
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="equlpment")
	public String equlpment(Model model,Integer rentOrSell,Integer isShangjia,String name,Integer pageNo,Integer pageSize) {
		
		Pagination page = equipmentService.getAllPageByRentOrSell(rentOrSell, isShangjia, name, pageNo==null?1:pageNo, 10);
		
		model.addAttribute("rentOrSell", rentOrSell);
		model.addAttribute("isShangjia", isShangjia);
		model.addAttribute("name", name);
		model.addAttribute("page", page);
		model.addAttribute("topActive", "7");
		switch(rentOrSell) {
		case 1:model.addAttribute("leftActive", "5");break;
		case 2:model.addAttribute("leftActive", "4");break;
		}
		return "equip/equlpment";
	}
	
	/**
	 * 工单详情
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="detail")
	public String equipDetail(Long id,Model model) {
		EquipmentOrder equiporder = equipmentOrderService.getById(id);
		Equipment equipment = new Equipment();
		if (equiporder.getEquipmentId()!=null) {
			equipment = equipmentService.getById(equiporder.getEquipmentId());
			if (equipment == null) {
				equipment = new Equipment();
			}
		}
		model.addAttribute("equiporder", equiporder);
		model.addAttribute("equipment", equipment);
		model.addAttribute("topActive", "7");
		switch(equiporder.getType()) {
			case 1:
				model.addAttribute("leftActive", "2");
				return "equip/equipdetail";
			case 2:
				model.addAttribute("leftActive", "1");
				return "equip/equipdetail";
			case 3:
				model.addAttribute("leftActive", "3");
				return "equip/serverdetail";
		}
		
		return "";
	}
	
	
	/**
	 * 关闭、处理工单 
	 * @param id
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="closeOrder",method=RequestMethod.POST)
	public void closeOrder(Long id,Integer status,HttpServletResponse response) {
		JSONObject result;
		try {
			EquipmentOrder equipment = equipmentOrderService.getById(id);
			equipment.setStatus(status);
			equipmentOrderService.update(equipment);
			result = ResultUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			result = ResultUtils.error();
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 添加工单
	 * @param equipmentOrder
	 * @param response
	 */
	@RequestMapping(value="ordersave",method=RequestMethod.POST)
	public void subimtOrder(EquipmentOrder equipmentOrder,HttpServletResponse response) {
		JSONObject result;
		try {
			equipmentOrderService.save(equipmentOrder);
			result = ResultUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			result = ResultUtils.error();
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 添加库存设备页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toaddEquip")
	public String toaddEquip(Model model,Integer rentOrSell,Long id) {
		Equipment equipment = null;
		if(id==null) {
			equipment = new Equipment();
		}else {
			equipment = equipmentService.getById(id);
		}
		
		model.addAttribute("equipment", equipment);
		model.addAttribute("rentOrSell", rentOrSell);
		model.addAttribute("topActive", "7");
		switch(rentOrSell) {
		case 1:model.addAttribute("leftActive", "5");break;
		case 2:model.addAttribute("leftActive", "4");break;
		}
		return "equip/addEquip";
	}
	
	/**
	 * 创建和修改库存
	 * @param equipment
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="addEquip")
	public void addEquip(Equipment equipment,HttpServletResponse response) {
		JSONObject result;
		try {
			System.out.println(equipment.getId());
			if(equipment.getId()==null||equipment.getId()==0) {
				equipmentService.save(equipment);
			}else {
				Equipment equipget = equipmentService.getById(equipment.getId());
				equipget.setImg(equipment.getImg());
				equipget.setName(equipment.getName());
				equipget.setPrice(equipment.getPrice());
				equipget.setDescript(equipment.getDescript());
				equipmentService.update(equipget);
			}
			result = ResultUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			result = ResultUtils.error();
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 创建和修改库存
	 * @param equipment
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="sxjia")
	public void sxjia(Long id,Integer isShangjia,HttpServletResponse response) {
		JSONObject result;
		try {
			Equipment equipget = equipmentService.getById(id);
			equipget.setIsShangjia(isShangjia==0?1:0);
			equipmentService.update(equipget);
			result = ResultUtils.success();
		} catch (Exception e) {
			e.printStackTrace();
			result = ResultUtils.error();
		}
		ResponseUtils.renderJson(response, result.toString());
	}
}
