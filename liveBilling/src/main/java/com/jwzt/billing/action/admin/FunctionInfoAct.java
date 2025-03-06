package com.jwzt.billing.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jwzt.billing.entity.FunctionInfo;
import com.jwzt.billing.manager.FunctionInfoMng;
import com.jwzt.common.web.springmvc.RenderJsonUtils;

@Controller
public class FunctionInfoAct {

	@RequestMapping(value = "/function/list", method = RequestMethod.GET)
	public String page(ModelMap mp, HttpServletRequest request) {
		List<FunctionInfo> list = functionInfoMng.listByParams(-1, true);
		RenderJsonUtils.addSuccess(mp, list);
		return "renderJson";
	}

	@Autowired
	private FunctionInfoMng functionInfoMng;
}
