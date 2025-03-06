package com.bvRadio.iLive.iLive.action.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveStreamFile;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveStreamFileMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "streamfile")
public class ILiveStreamFileController {

	@Autowired
	private ILiveStreamFileMng iLiveStreamFileMng;
	/**
	 * 流管理列表
	 * @param streamName
	 * @param model
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="streamlist.do")
	public String streamlist(String streamName,Model model,Integer pageNo,Integer pageSize,HttpServletRequest request) {
		try {
			if(streamName!=null){
				streamName = URLDecoder.decode(streamName,"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		UserBean user = ILiveUtils.getUser(request);
		Pagination page = iLiveStreamFileMng.getPage(streamName,user.getEnterpriseId(), pageNo==null?1:pageNo, 19);
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("page", page);
		model.addAttribute("SerchName", streamName);
		model.addAttribute("leftActive", "5_1");
		model.addAttribute("topActive", "2");
		model.addAttribute("serverGroupId", serverGroupId);
		return "streamFile/streamfilelist";
	}
	
	private Integer selectServerGroup() {
		return 100;
	}
	
	/**
	 * 创建流页面
	 * @return
	 */
	@RequestMapping(value="createstream.do")
	public String createStream(Model model, Long id) {
		ILiveStreamFile iLiveStreamFile;
		if(id!=null) {
			iLiveStreamFile = iLiveStreamFileMng.getById(id);
		}else {
			iLiveStreamFile = new ILiveStreamFile();
		}
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("iLiveStreamFile", iLiveStreamFile);
		model.addAttribute("leftActive", "5_1");
		model.addAttribute("topActive", "2");
		model.addAttribute("serverGroupId", serverGroupId);
		return "streamFile/createstream";
	}
	
	/**
	 * 创建/修改流
	 * @param iLiveStreamFile
	 */
	@ResponseBody
	@RequestMapping(value="saveStream.do",method = RequestMethod.POST)
	public void savePrize(ILiveStreamFile iLiveStreamFile,HttpServletResponse response,HttpServletRequest request) {
		JSONObject res = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			iLiveStreamFile.setUserId(Long.parseLong(user.getUserId()));
			iLiveStreamFile.setEnterpriseId(user.getEnterpriseId());
			if(iLiveStreamFile.getId()==null){
				iLiveStreamFileMng.save(iLiveStreamFile);
			}else{
				iLiveStreamFileMng.update(iLiveStreamFile);
			}
			res.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", 2);
		}
		ResponseUtils.renderJson(response, res.toString());
	}
	
	
	/**
	 * 删除流
	 * @param response
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value="delete.do")
	public void deleteStream(HttpServletResponse response,Long id) {
		JSONObject result = new JSONObject();
		try {
			iLiveStreamFileMng.delete(id);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
}
