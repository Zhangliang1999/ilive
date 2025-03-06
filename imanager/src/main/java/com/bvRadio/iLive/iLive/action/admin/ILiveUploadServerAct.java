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
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.web.WebErrors;

@Controller
public class ILiveUploadServerAct {
	private static final Logger log = LoggerFactory.getLogger(ILiveUploadServerAct.class);

	@RequestMapping("/uploadServer/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		List<ILiveUploadServer> uploadServerList = uploadServerMng.getList();
		model.addAttribute("uploadServerList", uploadServerList);
		model.addAttribute("leftActive", "3_2");
		model.addAttribute("topActive", "1");
		return "uploadServer/list";
	}

	@RequestMapping("/uploadServer/v_add.do")
	public String add(ModelMap model) {
		model.addAttribute("topActive", "1");
		return "uploadServer/add";
	}

	@RequestMapping("/uploadServer/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("uploadServer", uploadServerMng.findById(id));
		model.addAttribute("topActive", "1");
		return "uploadServer/edit";
	}

	@RequestMapping("/uploadServer/o_save.do")
	public void save(ILiveUploadServer bean, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		bean = uploadServerMng.save(bean);
		String result = "<script>window.parent.location.reload(true);</script>";
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/uploadServer/o_update.do")
	public void update(ILiveUploadServer bean, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		bean = uploadServerMng.update(bean);
		String result = "<script>window.parent.location.reload(true);</script>";
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/uploadServer/o_deleteAll.do")
	public @ResponseBody String delete(String ids, HttpServletRequest request, ModelMap model) {
		String[] split = ids.split(",");
		ILiveUploadServer[] beans = uploadServerMng.deleteByIds(split);
		for (ILiveUploadServer bean : beans) {
			log.info("delete ILiveUploadServer id={}", bean.getId());
		}
		return "success";
	}
	@RequestMapping("/uploadServer/o_delete.do")
	public @ResponseBody String delete(Integer id, HttpServletRequest request, ModelMap model) {
		uploadServerMng.deleteById(id);
		return "success";
	}

	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = new WebErrors();
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}


	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		ILiveUploadServer entity = uploadServerMng.findById(id);
		if (errors.ifNotExist(entity, ILiveUploadServer.class, id)) {
			return true;
		}
		return false;
	}

	@Autowired
	private ILiveUploadServerMng uploadServerMng;

}