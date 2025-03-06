package com.bvRadio.iLive.iLive.action.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bvRadio.iLive.iLive.entity.ILivePoint;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.manager.ILivePointMng;
import com.bvRadio.iLive.iLive.web.WebErrors;

@Controller
public class ILivePointAct {
	private static final Logger log = LoggerFactory.getLogger(ILivePointAct.class);

	@RequestMapping("/point/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		List<ILivePoint> pointList = iLivePointMng.getList();
		model.addAttribute("pointList", pointList);
		model.addAttribute("leftActive", "3_3");
		model.addAttribute("topActive", "1");
		return "point/list";
	}

	@RequestMapping("/point/v_add.do")
	public String add(ModelMap model) {
		return "point/add";
	}

	@RequestMapping("/point/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("point", iLivePointMng.findById(id));
		return "point/edit";
	}

	@RequestMapping("/point/o_save.do")
	public void save(ILivePoint bean, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		bean = iLivePointMng.save(bean);
		String result = "<script>window.parent.location.reload(true);</script>";
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/point/o_update.do")
	public void update(ILivePoint bean, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		bean = iLivePointMng.update(bean);
		String result = "<script>window.parent.location.reload(true);</script>";
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/point/o_delete.do")
	public String delete(@RequestParam(value="ids[]")Integer[] ids, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		ILivePoint [] beans = iLivePointMng.deleteByIds(ids);
		for (ILivePoint bean : beans) {
			log.info("delete ILivePoint id={}", bean.getPointId());
		}
		return list(request, model);
		
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
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		ILivePoint entity = iLivePointMng.findById(id);
		if (errors.ifNotExist(entity, ILiveUploadServer.class, id)) {
			return true;
		}
		return false;
	}

	

	@Autowired
	private ILivePointMng  iLivePointMng;

}