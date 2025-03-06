package com.bvRadio.iLive.iLive.action.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;

@Controller
@RequestMapping(value="enterprise")
public class ILiveEnterpriseController {
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	
	
	/**
	 * 新增企业
	 * @return
	 */
	@RequestMapping(value="/save.do")
	public String addEnterprise(ILiveEnterprise enterprise) {
		boolean saveILiveEnterprise = iLiveEnterpriseMng.saveILiveEnterprise(enterprise);
		if(saveILiveEnterprise) {
			return "redirect:/admin/room/list.do";
		}
		return null;
	}
	
	

}
