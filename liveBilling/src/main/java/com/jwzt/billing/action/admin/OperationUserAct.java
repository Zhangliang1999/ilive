package com.jwzt.billing.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jwzt.billing.entity.bo.OperationUser;
import com.jwzt.billing.manager.OperationUserMng;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class OperationUserAct {

	@RequestMapping(value = "/operationUser/list", method = RequestMethod.GET)
	public String list(ModelMap mp, HttpServletRequest request, Integer pageNo) {
		List<OperationUser> list = null;
		try {
			list = operationUserMng.listByParams();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RenderJsonUtils.addSuccess(mp, list);
		return "renderJson";
	}

	@Autowired
	private OperationUserMng operationUserMng;
}
