package com.bvRadio.iLive.manager.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveSensitiveWord;
import com.bvRadio.iLive.iLive.manager.ILiveSensitiveWordMng;

@RequestMapping(value="/platform")
public class ILivePlatformController {

	@Autowired
	private ILiveSensitiveWordMng iLiveSensitiveWordMng;
	
	//进入敏感词页面
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/platform.do")
	public String platform(Model model) {
		Pagination page = iLiveSensitiveWordMng.getPage("", true, 1,110);
		List<ILiveSensitiveWord> list = (List<ILiveSensitiveWord>) page.getList();
		int pagenum = page.getTotalPage();
		System.out.println(pagenum);
		model.addAttribute("pagenum", pagenum);
		model.addAttribute("list", list);
		model.addAttribute("topActive", "6");
		model.addAttribute("leftActive", "3");
		return "platform/sensitive";
	}
	
	//添加敏感词
	@RequestMapping(value="/addplatform.do")
	public void addplatform(ILiveSensitiveWord iLiveSensitiveWord,HttpServletResponse response) {
		ILiveSensitiveWord sensitiveWord = iLiveSensitiveWordMng.save(iLiveSensitiveWord);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", sensitiveWord);
		ResponseUtils.renderJson(response, jsonObject.toString());
	}
	
	
	//删除敏感词
	@RequestMapping(value="/remove.do")
	public void remove(Integer id,HttpServletResponse response) {
		ILiveSensitiveWord sensitiveWord = iLiveSensitiveWordMng.deleteById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", sensitiveWord);
		ResponseUtils.renderJson(response, jsonObject.toString());
	}
	
	//根据类型分类搜索
	@RequestMapping(value="/searchtype.do")
	public void searchtype(Integer type,HttpServletResponse response) {
		Pagination page = iLiveSensitiveWordMng.getPageByType(type, true, 1, 110);
		List<ILiveSensitiveWord>  list = (List<ILiveSensitiveWord>) page.getList();
		int pageSize = page.getTotalPage();
		JSONObject result = new JSONObject();
		System.out.println(pageSize);
		result.put("pageSize", pageSize);
		result.put("data", list);
		ResponseUtils.renderJson(response, result.toString());
	}
	
	//根据名称搜索
	@RequestMapping(value="/searchname.do")
	public void searchname(String sensitiveName,HttpServletResponse response) {
		Pagination page = iLiveSensitiveWordMng.getPage(sensitiveName, true, 1, 110);
		List<ILiveSensitiveWord>  list = (List<ILiveSensitiveWord>) page.getList();
		int pageSize = page.getTotalPage();
		JSONObject result = new JSONObject();
		result.put("pageSize", pageSize);
		result.put("data", list);
		ResponseUtils.renderJson(response, result.toString());
	}
	
	//分页搜索
	@RequestMapping(value="/getPage.do")
	public void getPage(Integer type,String sensitiveName,Integer pageNo,HttpServletResponse response) {
		System.out.println(type);
		System.out.println(sensitiveName);
		Pagination page = iLiveSensitiveWordMng.getPageByTypeAndName(type,sensitiveName,true,pageNo,110);
		List<ILiveSensitiveWord>  list = (List<ILiveSensitiveWord>) page.getList();
		int pageSize = page.getTotalPage();
		JSONObject result = new JSONObject();
		result.put("pageSize", pageSize);
		result.put("data", list);
		ResponseUtils.renderJson(response, result.toString());
	}
}
