package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ContentPolicy;
import com.bvRadio.iLive.iLive.entity.ContentSelect;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveThematic;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveContentSelectMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveThematicMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;

import net.sf.json.JSONArray;

/**
 * 专题管理
 * 
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value = "thematic")
public class ILiveThematicController {

	@Autowired
	private ILiveThematicMng iLiveThematicMng;// 专题

	@Autowired
	private ILiveLiveRoomMng roomMng; // 直播间

	@Autowired
	private ILiveContentSelectMng contentMng; // 选择显示的内容

	@Autowired
	private ILiveMediaFileMng mediaFileMng; // 选择 视频 文档 图片
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng; // 获取企业信息
	@Autowired
	private ILiveEnterpriseFansMng iLiveEnterpriseFansMng; // 获取企业信息

	/**
	 * 进入专题管理列表页
	 * 
	 * @param pageNo
	 *            页码
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/into/list.do")
	public String intoILiveThematic(Integer pageNo, ModelMap model) {
		Pagination pagination = iLiveThematicMng.selectILiveThematicPage(cpn(pageNo), 7, false);
		model.addAttribute("pagination", pagination);
		model.addAttribute("leftActive", "2_1");
		model.addAttribute("topActive", "4");
		return "homepage/thematic_list";
	}

	/**
	 * 新增数据
	 * 
	 * @param thematicName
	 *            专题名称
	 * @param thematicDesc
	 *            专题描述
	 * @param thematicImage
	 *            图片路径
	 * @param templateId
	 *            模板ID
	 * @param response
	 */
	@RequestMapping(value = "/save.do")
	public void saveILiveThematic(String thematicName, String thematicDesc, String thematicImage, Integer templateId,
			HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			UserBean userBean = ILiveUtils.getUser(request);
			if (userBean == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户不存在");
			}
			ILiveThematic iLiveThematic = new ILiveThematic();
			iLiveThematic.setThematicName(thematicName);
			iLiveThematic.setThematicDesc(thematicDesc);
			iLiveThematic.setThematicImage(thematicImage);
			iLiveThematic.setTemplateId(templateId);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			iLiveThematic.setCreateTime(Timestamp.valueOf(format.format(new Date())));
			iLiveThematic.setIsDelete(false);
			iLiveThematic.setCreateName(userBean.getUsername());
			iLiveThematic.setIsChecked(1);
			iLiveThematicMng.addILiveThematic(iLiveThematic);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 删除专题
	 * 
	 * @param thematicId
	 *            专题ID
	 * @param pageNo
	 *            页码
	 * @return
	 */
	@RequestMapping(value = "/delete.do")
	public String deleteILiveThematicList(Long thematicId, Integer pageNo) {
		iLiveThematicMng.deleteILiveThematicByIsDelete(thematicId, true);
		return "redirect:/admin/thematic/into/list.do?pageNo=" + pageNo;
	}

	/**
	 * 修改状态
	 * 
	 * @param thematicId
	 *            主键ID
	 * @param isChecked
	 *            状态
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/checked/update.do")
	public void updateILiveThematicByChecked(Long thematicId, Integer isChecked, HttpServletResponse response,
			HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveThematic iLiveThematic = iLiveThematicMng.selectIliveThematicById(thematicId);
			iLiveThematic.setIsChecked(isChecked);
			iLiveThematicMng.updateILiveThematic(iLiveThematic);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 主页管理
	 */
	@RequestMapping(value = "thematicmain.do")
	public String thematicmain(Model model, Integer pageNo, Integer contentType,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		
		
		// 直播间列表
		List<ILiveLiveRoom> list1 = roomMng.findRoomListByEnterprise(enterpriseId);
		// 视频列表
		List<ILiveMediaFile> list2 = mediaFileMng.getListByType(1,enterpriseId);
		// 专题列表
		List<ILiveThematic> list3 = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
		// 获取一行一条数据的条数
		List<ContentSelect> list5 = contentMng.getListByShows(3);
		// 获取一行两条数据的条数
		List<ContentSelect> list6 = contentMng.getListByShows(5);

		ContentPolicy policy3 = contentMng.getPolicyByShows(3,enterpriseId);
		if (policy3 == null) {
			policy3 = new ContentPolicy();
			policy3.setNum(2);
			policy3.setPolicy(1);
		}
		ContentPolicy policy5 = contentMng.getPolicyByShows(5,enterpriseId);
		if (policy5 == null) {
			policy5 = new ContentPolicy();
			policy5.setNum(2);
			policy5.setPolicy(1);
		}

		model.addAttribute("enterpriseId", enterpriseId);
		model.addAttribute("list1", list1);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("list5", list5);
		model.addAttribute("list6", list6);
		model.addAttribute("policy3", policy3);
		model.addAttribute("policy5", policy5);
		model.addAttribute("topActive", "4");
		model.addAttribute("leftActive", "1_1");
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		return "homepage/tmain";
	}

	/**
	 * 保存已选择的内容
	 */
	@ResponseBody
	@RequestMapping(value = "saveselect.do")
	public String saveselect(ContentSelect content) {
		Integer id = contentMng.saveselect(content);
		return id + "";
	}

	/**
	 * 顺序调整
	 */
	@ResponseBody
	@RequestMapping(value = "saveindex.do")
	public String saveindex(String obj, HttpServletResponse response,Integer enterpriseId) {
		JSONArray jarr = JSONArray.fromObject(obj);
		net.sf.json.JSONObject jobj;
		List<Map<String, String>> list = new ArrayList<>();
		if (jarr.size() > 0) {
			for (int i = 0; i < jarr.size(); i++) {
				Map<String, String> map = new HashMap<>();
				jobj = jarr.getJSONObject(i);
				map.put("id", (String) jobj.get("id"));
				map.put("index", jobj.get("index").toString());
				list.add(map);
			}
			for (Map<String, String> m : list) {
				System.out.println("id            " + m.get("id"));
				System.out.println("index            " + m.get("index"));
			}
			contentMng.saveindex(list);
		}
		response.setCharacterEncoding("utf-8");
		List<ContentSelect> list1 = contentMng.getListByType(1);
		JSONArray resarr = JSONArray.fromObject(list1);
		return resarr.toString();
	}

	/**
	 * 删除已选择的内容
	 */
	@ResponseBody
	@RequestMapping(value = "removeselect.do")
	public String removeselect(Integer id) {
		contentMng.removeselect(id);
		return "";
	}

	/**
	 * 更新内容策略
	 */
	@ResponseBody
	@RequestMapping(value = "updatePolicy.do")
	public String updatePolicy(ContentPolicy policy) {
		System.out.println(policy.getId());
		contentMng.updatePolicy(policy);
		return "";
	}

	/**
	 * 更新内容策略
	 */
	@ResponseBody
	@RequestMapping(value = "savepolicy.do")
	public String savepolicy(ContentPolicy policy) {
		contentMng.savepolicy(policy);
		return "";
	}

	/**
	 * 更新内容策略
	 */
	@RequestMapping(value = "getshow.do")
	public void getoneshow(Integer shows, HttpServletResponse response,Integer enterpriseId) {
		List<ContentSelect> list = contentMng.getListByShowsAndEnid(shows,enterpriseId);
		JSONArray json = JSONArray.fromObject(list);
		ResponseUtils.renderJson(response, json.toString());
	}

	/**
	 * 分页显示
	 */
	@ResponseBody
	@RequestMapping(value = "page.do")
	public String page(Integer shows, Integer pageNo,Integer enterpriseId) {
		List<ContentSelect> list = contentMng.getListByShows(shows, pageNo,enterpriseId);
		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}

	/**
	 * 获取企业信息
	 */
	@ResponseBody
	@RequestMapping(value = "getEnterprise.do")
	public void getEnterprise(HttpServletResponse response,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		ILiveEnterprise iliveEnterprise = iLiveEnterpriseMng.getILiveEnterpriseById(enterpriseId);
		int num = iLiveEnterpriseFansMng.getFansNum(enterpriseId);
		Map<String, Object> map = new HashMap<>();
		map.put("iliveEnterprise", iliveEnterprise);
		map.put("num", num);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(map);
		ResponseUtils.renderJson(response, json.toString());
	}
	/**
	 * 获取发布策略和内容数量
	 */
	@ResponseBody
	@RequestMapping(value = "getpolicy.do")
	public String getpolicy(Integer shows,Integer enterpriseId) {
		ContentPolicy policy = contentMng.getPolicyByShows(shows,enterpriseId);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(policy);
		return json.toString();
	}

	/**
	 * 获取第二行标题
	 */
	@RequestMapping(value = "gettitle2.do")
	public void gettitle2(Integer shows, HttpServletResponse response,Integer enterpriseId) {
		ContentPolicy policy = contentMng.getPolicyByShows(2, enterpriseId);
		if (policy == null) {
			ResponseUtils.renderHtml(response, "");
		} else {
			ResponseUtils.renderHtml(response, policy.getTitle2());
		}
	}

	/**
	 * 获取第一行背景图片
	 */
	@RequestMapping(value = "gettitle4.do")
	public void gettitle4(Integer shows, HttpServletResponse response,Integer enterpriseId) {
		ContentPolicy policy = contentMng.getPolicyByShows(shows, enterpriseId);
		if (policy == null) {
			ResponseUtils.renderJson(response, "");
		} else {
			JSONArray json = JSONArray.fromObject(policy);
			ResponseUtils.renderJson(response, json.toString());
		}
	}

	/**
	 * 保存第二行标题
	 */
	@ResponseBody
	@RequestMapping(value = "savetitle2.do")
	public void savetitle2(String title, Integer shows,Integer enterpriseId) {
		ContentPolicy policy = contentMng.getPolicyByShows(shows, enterpriseId);
		if (policy == null) {
			contentMng.saveTitle2(title, shows,enterpriseId);
		} else {
			contentMng.updateTitle2(title, shows,enterpriseId);
		}
	}

	/**
	 * 保存第四行内容
	 */
	@ResponseBody
	@RequestMapping(value = "savelink.do")
	public void savelink(String link, String linkName,Integer enterpriseId,Integer shows) {
		System.out.println(linkName);
		ContentPolicy policy = contentMng.getPolicyByShows(shows, enterpriseId);
		if (policy == null) {
			contentMng.saveLink(link, linkName,enterpriseId,shows);
		} else {
			contentMng.updateLink(link, linkName,enterpriseId,shows);
		}
	}

	/**
	 * 保存与替换第一行图片
	 */
	@ResponseBody
	@RequestMapping(value = "replaceimg.do")
	public void replaceimg(String imgurl,Integer enterpriseId) {
		System.out.println(imgurl);
		ContentPolicy policy = contentMng.getPolicyByShows(1, enterpriseId);
		if (policy == null) {
			contentMng.saveImg(imgurl,enterpriseId);
		} else {
			contentMng.updateImg(imgurl,enterpriseId);
		}
	}

	/**
	 * 删除第四行链接
	 */
	@ResponseBody
	@RequestMapping(value = "deletelink.do")
	public void deletelink(Integer enterpriseId,Integer shows) {
		contentMng.deletelink(enterpriseId,shows);
	}

	/**
	 * 删除第一行背景图片
	 */
	@ResponseBody
	@RequestMapping(value = "deleteimg.do")
	public void deleteimg(Integer enterpriseId) {
		contentMng.deleteimg(enterpriseId);
	}

	/**
	 * 预览 1 获取第三行和第五行数据
	 */
	@RequestMapping(value = "yulan.do")
	public void yulan(Integer shows, HttpServletResponse response,Integer enterpriseId) {
		// 获取一行一条数据的条数
		List<ContentSelect> list3;
		// 获取一行两条数据的条数
		List<ContentSelect> list5;
		// 获取第二行标题
		ContentPolicy policy3 = contentMng.getPolicyByShows(3,enterpriseId);
		if (policy3 != null) {
			list3 = contentMng.getNumByShows(3, policy3.getNum(),enterpriseId);
		} else {
			list3 = contentMng.getNumByShows(3,2,enterpriseId);
		}
		ContentPolicy policy5 = contentMng.getPolicyByShows(5,enterpriseId);
		if (policy5 != null) {
			list5 = contentMng.getNumByShows(5, policy5.getNum(),enterpriseId);
		} else {
			list5 = contentMng.getNumByShows(5, 2,enterpriseId);
		}
		list3.addAll(list5);
		JSONArray json = JSONArray.fromObject(list3);
		ResponseUtils.renderJson(response, json.toString());
	}

	/**
	 * 预览 2 获取第一行第二行第四行内容信息
	 */
	@RequestMapping(value = "yulan2.do")
	public void yulan2(Integer shows, HttpServletResponse response,Integer enterpriseId) {
		// 获取一行一条数据的条数
		List<ContentPolicy> list = new LinkedList<>();
		ContentPolicy policy1 = contentMng.getPolicyByShows(1,enterpriseId);
		ContentPolicy policy2 = contentMng.getPolicyByShows(2,enterpriseId);
		ContentPolicy policy4 = contentMng.getPolicyByShows(4,enterpriseId);
		if (policy1 != null) {
			list.add(policy1);
		}
		if (policy2 != null) {
			list.add(policy2);
		}
		if (policy4 != null) {
			list.add(policy4);
		}
		JSONArray json = JSONArray.fromObject(list);
		ResponseUtils.renderJson(response, json.toString());
	}

	private Integer selectServerGroup() {
		return 100;
	}
	
	/**
	 * 自动选择内容
	 */
	@RequestMapping(value="autogetshow")
	public void autogetshow(HttpServletResponse response,Integer shows) {
		List<ILiveLiveRoom> list1 = roomMng.findRoomList();
		List<ContentSelect> list = new LinkedList<>();
		if(list1.size()>0) {
			ContentSelect temp = null;
			for(ILiveLiveRoom room:list1) {
				temp = new ContentSelect();
				temp.setShows(shows);
				temp.setContentId(room.getRoomId());
				temp.setContentName(room.getLiveEvent().getLiveTitle());
				temp.setContentBrief(room.getLiveEvent().getLiveDesc());
				temp.setContentImg(room.getLiveEvent().getLogoUrl());
				list.add(temp);
			}
		}
		JSONArray json = JSONArray.fromObject(list);
		ResponseUtils.renderJson(response, json.toString());
	}
	
	
	
}
