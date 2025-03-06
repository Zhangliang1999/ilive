package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ContentSelect;
import com.bvRadio.iLive.iLive.entity.ContentSelectPublish;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageLink;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructure;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructurePublish;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveThematic;
import com.bvRadio.iLive.iLive.entity.Topic;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.ILiveContentSelectMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveHomepageLinkMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveThematicMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.manager.TopicMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
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
	private TopicMng topicMng;			//专题
	
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
	@Autowired
	private ILiveHomepageLinkMng iLiveHomepageLinkMng; // 获取主页链接信息
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	@Autowired
	private WorkLogMng workLogMng;

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
		model.addAttribute("leftActive", "5_1");
		model.addAttribute("topActive", "2");
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
	 * 专题详情
	 */
	@RequestMapping(value="detailThematic.do")
	public String detailThematic(Long thematicId,Model model) {
		ILiveThematic iLiveThematic = iLiveThematicMng.selectIliveThematicById(thematicId);
		model.addAttribute("iLiveThematic",iLiveThematic);
		if(iLiveThematic.getTemplateId()==1) {
			return "homepage/thematicManager1";
		}else if(iLiveThematic.getTemplateId()==2){
			return "homepage/thematicManager2";
		}
		return "homepage/thematicManager1";
	}
	
	
	/**
	 * 主页管理
	 */
	@RequestMapping(value = "thematicmain.do")
	public String thematicmain(Model model, Integer pageNo, Integer contentType,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		
		// 直播间列表
		List<ILiveLiveRoom> list1 = roomMng.findRoomListPassByEnterprise(enterpriseId);
		// 视频列表
		List<ILiveMediaFile> list2 = mediaFileMng.getListByType(1,enterpriseId);
		// 专题列表
		//List<ILiveThematic> list3 = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
		List<Topic> list3 = topicMng.getByEnterpriseId(enterpriseId);
		
		//链接列表
		List<ILiveHomepageLink> list4 = iLiveHomepageLinkMng.getListByEnterpriseId(enterpriseId);

		//更新未发布的表
		List<ContentSelectPublish> listpc = contentMng.getPublishContentByEnterprise(enterpriseId);
		contentMng.deleteContentByEnterprise(enterpriseId);
		for(ContentSelectPublish contentSelectPublish:listpc) {
			ContentSelect con = new ContentSelect();
			con.setId(contentSelectPublish.getId());
			con.setEnterpriseId(contentSelectPublish.getEnterpriseId());
			con.setStructureId(contentSelectPublish.getStructureId());
			con.setContentType(contentSelectPublish.getContentType());
			con.setContentId(contentSelectPublish.getContentId());
			con.setContentName(contentSelectPublish.getContentName());
			con.setContentImg(contentSelectPublish.getContentImg());
			con.setContentBrief(contentSelectPublish.getContentBrief());
			con.setIndexs(contentSelectPublish.getIndexs());
			con.setLiveStatus(contentSelectPublish.getLiveStatus());
			con.setUrlName(contentSelectPublish.getUrlName());
			con.setContentUrl(contentSelectPublish.getContentUrl());
			if(con!=null) {
				contentMng.saveselectfrompublish(con);
			}
		}
		List<ILiveHomepageStructurePublish> listps = contentMng.getPublishStructureByEnterprise(enterpriseId);
		contentMng.deleteStructure(enterpriseId);
		System.out.println(listps+"   "+listps.size());
		if(listps==null || listps.size()==0 || listps.isEmpty()) {
			initStructure(enterpriseId);
		}else {
			for(ILiveHomepageStructurePublish iLiveHomepageStructure:listps) {
				ILiveHomepageStructure structure = new ILiveHomepageStructure();
				structure.setStructureId(iLiveHomepageStructure.getStructureId());
				structure.setType(iLiveHomepageStructure.getType());
				structure.setEnterpriseId(iLiveHomepageStructure.getEnterpriseId());
				structure.setOrders(iLiveHomepageStructure.getOrders());
				structure.setPolicy(iLiveHomepageStructure.getPolicy());
				structure.setShowContentType(iLiveHomepageStructure.getShowContentType());
				structure.setShowNum(iLiveHomepageStructure.getShowNum());
				if(structure!=null) {
					contentMng.saveStructure(structure);
				}
			}
		}
       //新加主页分享地址
		String defaultEnterpriseServerId = ConfigUtils.get("defaultEnterpriseServerId");
		ILiveServerAccessMethod serverGroup = accessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultEnterpriseServerId));
		String homePageUrl = serverGroup.getH5HttpUrl() + "/home/index.html?enterpriseId=" + enterpriseId;
		model.addAttribute("homePageUrl", homePageUrl);
		model.addAttribute("enterpriseId", enterpriseId);
		model.addAttribute("list1", list1);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("list4", list4);
		model.addAttribute("topActive", "2");
		model.addAttribute("leftActive", "5_1");
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		return "homepage/tmain";
	}

	/**
	 * 保存已选择的内容
	 */
	@ResponseBody
	@RequestMapping(value = "saveselect.do")
	public void saveselect(Integer contentId,Integer contentType,Integer enterpriseId,Integer structureId,HttpServletResponse response) {
		ContentSelect content = new ContentSelect();
		content.setContentId(contentId);
		content.setContentType(contentType);
		content.setEnterpriseId(enterpriseId);
		content.setStructureId(structureId);
		String img = "/ilive/tysx/img/u1207.jpg";
		if(contentType == 1) {
			ILiveLiveRoom room = roomMng.findById(contentId);
			if (room!=null) {
				ILiveEvent liveEvent = room.getLiveEvent();
				if(liveEvent!=null) {
					content.setContentName(liveEvent.getLiveTitle());
					if(liveEvent.getConverAddr()==null||liveEvent.getConverAddr().equals("")||liveEvent.getConverAddr().equals("null")) {
						content.setContentImg(img);
					}else {
						content.setContentImg(liveEvent.getConverAddr());
					}
					content.setContentBrief(liveEvent.getLiveDesc());
				}
			}
		}else if(contentType == 2) {
			long fileId = (long)contentId;
			ILiveMediaFile file = mediaFileMng.selectILiveMediaFileByFileId(fileId);
			if (file!=null) {
				content.setContentName(file.getMediaFileName());
				if(file.getFileCover()==null||file.getFileCover().equals("")||file.getFileCover().equals("null")) {
					content.setContentImg(img);
				}else {
					content.setContentImg(file.getFileCover());
				}
				content.setContentBrief(file.getMediaFileDesc());
			}
		}else if (contentType == 3) {
			long thematicId = (long)contentId;
			//ILiveThematic thematic = iLiveThematicMng.selectIliveThematicById(thematicId);
			Topic topic = topicMng.getById(thematicId);
			if(topic!=null) {
				content.setContentName(topic.getUserName());
				if (topic.getBanner()==null||topic.getBanner().equals("")||topic.getBanner().equals("null")) {
					content.setContentImg(img);
				}else {
					content.setContentImg(topic.getBanner());
				}
				content.setContentName(topic.getName());
				content.setContentBrief(topic.getDescript());
			}
		}else if(contentType == 4){
			long id = (long)contentId;
			ILiveHomepageLink link = iLiveHomepageLinkMng.getById(id);
			if(link!=null) {
				content.setContentName(link.getLinkName());
				if (link.getLinkImg()==null||link.getLinkImg().equals("")||link.getLinkImg().equals("null")) {
					content.setContentImg(img);
				}else {
					content.setContentImg(link.getLinkImg());
				}
				content.setContentUrl(link.getLink());
				content.setContentBrief(link.getLinkDesc());
			}
			
		}
		
		Integer id = contentMng.saveselect(content);
		content.setId(id);
		JSONObject resultJson = new JSONObject(content);
		ResponseUtils.renderJson(response, resultJson.toString());
	}
//	/**
//	 * 保存已选择的内容
//	 */
//	@ResponseBody
//	@RequestMapping(value = "saveselect.do")
//	public String saveselect(ContentSelect content) {
//		Integer id = contentMng.saveselect(content);
//		return id + "";
//	}

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
	@RequestMapping(value = "savepolicy.do")
	public String savepolicy(Integer structureId,Integer showNum,Integer enterpriseId,Integer policy) {
		contentMng.updateStructureNum(showNum,policy,structureId,enterpriseId);
		return "";
	}
	
	/**
	 * 更新自动选择的类型
	 */
	@ResponseBody
	@RequestMapping(value = "updaeStructureContentType.do")
	public String updaeStructureContentType(Integer structureId,Integer enterpriseId,Integer showContentType) {
		contentMng.updaeStructureContentType(showContentType,structureId,enterpriseId);
		return "";
	}
	
	/**
	 * 更新自动选择的类型并根据类型获取资源
	 */
	@RequestMapping(value = "updaeStructureContentTypeAndGetList.do")
	public void updaeStructureContentTypeAndGetList(Integer structureId,Integer enterpriseId,Integer showContentType,Integer num,HttpServletResponse response) {
		contentMng.deleteContentByEnterpriseIdAndStructure(enterpriseId, structureId);
		contentMng.updaeStructureContentType(showContentType,structureId,enterpriseId);
		JSONArray jsonArray ;
		List<ContentSelect> listcontent = new LinkedList<>();
		if(num == null) {
			num = 2;
		}
		if(showContentType == 1) {
			//直播间列表
			List<ILiveLiveRoom> list = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId,num);
			for(ILiveLiveRoom room:list) {
				ContentSelect contentSelect = new ContentSelect();
				contentSelect.setContentId(room.getRoomId());
				contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
				contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
				contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
				contentSelect.setContentType(1);
				contentSelect.setEnterpriseId(enterpriseId);
				contentSelect.setStructureId(structureId);
				contentMng.saveselect(contentSelect);
				listcontent.add(contentSelect);
			}
			jsonArray = JSONArray.fromObject(listcontent);
			ResponseUtils.renderJson(response, jsonArray.toString());
		}else if(showContentType == 2) {
			// 视频列表
			List<ILiveMediaFile> list = mediaFileMng.getListByTypeAndSize(1,enterpriseId,num);
			for(ILiveMediaFile file:list) {
				ContentSelect contentSelect = new ContentSelect();
				contentSelect.setContentId(file.getFileId().intValue());
				contentSelect.setContentName(file.getMediaFileName());
				contentSelect.setContentImg(file.getFileCover());
				contentSelect.setContentBrief(file.getMediaFileDesc());
				contentSelect.setContentType(2);
				contentSelect.setEnterpriseId(enterpriseId);
				contentSelect.setStructureId(structureId);
				contentMng.saveselect(contentSelect);
				listcontent.add(contentSelect);
			}
			jsonArray = JSONArray.fromObject(listcontent);
			ResponseUtils.renderJson(response, jsonArray.toString());
		}else if(showContentType == 3){
			// 专题列表
			//List<ILiveThematic> list = iLiveThematicMng.getListByEnterpriseIdAndSize(enterpriseId,num);
			List<Topic> list = topicMng.getByEnterpriseId(enterpriseId);
			
			for(Topic topic:list) {
				ContentSelect contentSelect = new ContentSelect();
				contentSelect.setContentId(topic.getId().intValue());
				contentSelect.setContentName(topic.getName());
				contentSelect.setContentImg(topic.getBanner());
				contentSelect.setContentBrief(topic.getDescript());
				contentSelect.setContentType(3);
				contentSelect.setEnterpriseId(enterpriseId);
				contentSelect.setStructureId(structureId);
				contentMng.saveselect(contentSelect);
				listcontent.add(contentSelect);
			}
			jsonArray = JSONArray.fromObject(listcontent);
			ResponseUtils.renderJson(response, jsonArray.toString());
		}
	}

	/**
	 * 更新内容策略
	 */
	@RequestMapping(value = "getshow.do")
	public void getoneshow(Integer structureId, HttpServletResponse response,Integer enterpriseId,Integer pageNo) {
		List<ContentSelect> list = contentMng.getContentByEnterpriseAndStructure(enterpriseId,structureId);
		JSONArray json = JSONArray.fromObject(list);
		ResponseUtils.renderJson(response, json.toString());
	}
	
	/**
	 * 更新内容分页
	 */
	@RequestMapping(value = "getshowPage.do")
	public void getshowPage(Integer structureId, HttpServletResponse response,Integer enterpriseId,Integer pageNo) {
		//List<ContentSelect> list = contentMng.getContentByEnterpriseAndStructure(enterpriseId,structureId);
		Pagination pagination = contentMng.getPageContentByEnterpriseAndStructure(10,pageNo==null?1:pageNo,enterpriseId,structureId);
		@SuppressWarnings("unchecked")
		List<ContentSelect> list = (List<ContentSelect>) pagination.getList();
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
	public String getpolicy(Integer structureId,Integer enterpriseId) {
		ILiveHomepageStructure structure = contentMng.getStructureById(structureId,enterpriseId);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(structure);
		return json.toString();
	}

	/**
	 * 保存第二行标题  保存分割线标题内容
	 */
	@ResponseBody
	@RequestMapping(value = "savecontentName.do")
	public void savecontentName(String contentName,Integer enterpriseId,Integer structureId) {
		List<ContentSelect> conten = contentMng.getContentByEnterpriseAndStructure(enterpriseId,structureId);
		if (conten.size() == 0) {
			contentMng.savecontentName(contentName, enterpriseId,structureId);
		} else {
			contentMng.updatecontentName(contentName, enterpriseId,structureId);
		}
	}

	/**
	 * 保存第四行内容
	 */
	@ResponseBody
	@RequestMapping(value = "savelink.do")
	public void savelink(String contentUrl, String urlName,Integer enterpriseId,Integer structureId) {
		List<ContentSelect> conten = contentMng.getContentByEnterpriseAndStructure(enterpriseId,structureId);
		if (conten.size() == 0) {
			contentMng.saveContetnLink(contentUrl, urlName,enterpriseId,structureId);
		} else {
			contentMng.updateContetnLink(contentUrl, urlName,enterpriseId,structureId);
		}
	}

	/**
	 * 保存与替换第一行图片
	 */
	@ResponseBody
	@RequestMapping(value = "replaceimg.do")
	public void replaceimg(String imgurl,Integer enterpriseId,Integer structureId) {
		List<ContentSelect> conten = contentMng.getContentByEnterpriseAndStructure(enterpriseId,structureId);
		if (conten.size() == 0) {
			contentMng.saveImg(imgurl,enterpriseId,structureId);
		} else {
			contentMng.updateImg(imgurl,enterpriseId,structureId);
		}
	}
	/**
	 *根据企业id和结构id获取内容
	 */
	@RequestMapping(value = "getContentSelect.do")
	public void getContentSelect(Integer enterpriseId,Integer structureId,HttpServletResponse response) {
		List<ContentSelect> conten = contentMng.getContentByEnterpriseAndStructure(enterpriseId,structureId);
		ResponseUtils.renderJson(response,JSONArray.fromObject(conten).toString());
	}

	/**
	 * 删除第四行链接
	 */
	@ResponseBody
	@RequestMapping(value = "deletelink.do")
	public void deletelink(Integer enterpriseId,Integer structureId) {
		contentMng.deletelink(enterpriseId,structureId);
	}

	/**
	 * 删除第一行背景图片
	 */
	@ResponseBody
	@RequestMapping(value = "deleteimg.do")
	public void deleteimg(Integer enterpriseId,Integer structureId) {
		contentMng.updateImg("",enterpriseId,structureId);
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
	
	/**
	 * 预览 获取所有内容
	 */
	@RequestMapping(value = "yulanbyenterprise.do")
	public void yulanbyenterprise( HttpServletResponse response,Integer enterpriseId) {
		List<ContentSelect> list = contentMng.getContentByEnterprise(enterpriseId);
		ILiveHomepageStructure structure4 = contentMng.getStructureById(4,enterpriseId);
		ILiveHomepageStructure structure6 = contentMng.getStructureById(6,enterpriseId);
		int policy4;
		int policy6;
		int num4;
		int num6;
		int contenttype4;
		int contenttype6;
		if(structure4 == null) {
			num4 = 1;
			policy4 = 1;
			contenttype4 = 1;
		}else {
			if(structure4.getShowNum()==null) {
				num4 = 1;
			}else {
				num4 = structure4.getShowNum();
			}
			if(structure4.getPolicy()==null) {
				policy4 = 1;
			}else {
				policy4 = structure4.getPolicy();
			}
			if(structure4.getShowContentType()==null) {
				contenttype4 = 1;
			}else {
				contenttype4 = structure4.getShowContentType();
			}
		}
		if(structure6 == null) {
			num6 = 2;
			policy6 = 1;
			contenttype6 = 1;
		}else {
			if (structure6.getShowNum()==null) {
				num6 = 2;
			}else {
				num6 =structure6.getShowNum();
			}
			if (structure6.getPolicy()==null) {
				policy6 = 1;
			}else {
				policy6 = structure6.getPolicy();
			}
			if(structure6.getShowContentType()==null) {
				contenttype6 = 1;
			}else {
				contenttype6 = structure6.getShowContentType();
			}
		}
		int mark4 = 0,mark6 = 0;
		Iterator<ContentSelect> iterator = list.iterator();
		while (iterator.hasNext()) {
			ContentSelect contentSelect = iterator.next();
			if(contentSelect.getContentType()!=null && contentSelect.getContentType() == 1) {
				ILiveLiveRoom room = roomMng.findById(contentSelect.getContentId()); 
				contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
			}
			if(contentSelect.getStructureId() == 4) {
				if(policy4 == 1) {
					mark4 ++;
					if(mark4>num4) {
						iterator.remove();
					}
				}else if(policy4 == 2) {
					iterator.remove();
				}
			}
			if(contentSelect.getStructureId() == 6) {
				if(policy6 == 1) {
					mark6 ++;
					if(mark6>num6) {
						iterator.remove();
					}
				}else if(policy6 == 2) {
					iterator.remove();
				}
			}
		}
		if(policy4 == 2) {
			List<ContentSelect> listcontent = new LinkedList<>();
			if (contenttype4 == 1) {
				List<ILiveLiveRoom> listroom4 = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId, num4);
				for(ILiveLiveRoom room:listroom4) {
					ContentSelect contentSelect = new ContentSelect();
					contentSelect.setContentId(room.getRoomId());
					contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
					contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
					contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
					contentSelect.setContentType(1);
					contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}else if(contenttype4 == 2) {
				List<ILiveMediaFile> listfile = mediaFileMng.getListByType(1,enterpriseId);
				for(ILiveMediaFile file:listfile) {
					ContentSelect contentSelect = new ContentSelect();
					contentSelect.setContentId(file.getFileId().intValue());
					contentSelect.setContentName(file.getMediaFileName());
					contentSelect.setContentImg(file.getFileCover());
					contentSelect.setContentBrief(file.getMediaFileDesc());
					contentSelect.setContentType(2);
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}else if(contenttype4 == 3) {
				// 专题列表
				//List<ILiveThematic> listthematic = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
				List<Topic> listtopic = topicMng.getByEnterpriseId(enterpriseId);
				for(Topic topic:listtopic) {
					ContentSelect contentSelect = new ContentSelect();
					contentSelect.setContentId(topic.getId().intValue());
					contentSelect.setContentName(topic.getName());
					contentSelect.setContentImg(topic.getBanner());
					contentSelect.setContentBrief(topic.getDescript());
					contentSelect.setContentType(3);
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}
			list.addAll(listcontent);
		}
		if(policy6 == 2) {
			List<ContentSelect> listcontent = new LinkedList<>();
			if (contenttype6 == 1) {
				List<ILiveLiveRoom> listroom4 = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId, num4);
				for(ILiveLiveRoom room:listroom4) {
					ContentSelect contentSelect = new ContentSelect();
					contentSelect.setContentId(room.getRoomId());
					contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
					contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
					contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
					contentSelect.setContentType(1);
					contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}else if(contenttype6 == 2) {
				List<ILiveMediaFile> listfile = mediaFileMng.getListByType(1,enterpriseId);
				for(ILiveMediaFile file:listfile) {
					ContentSelect contentSelect = new ContentSelect();
					contentSelect.setContentId(file.getFileId().intValue());
					contentSelect.setContentName(file.getMediaFileName());
					contentSelect.setContentImg(file.getFileCover());
					contentSelect.setContentBrief(file.getMediaFileDesc());
					contentSelect.setContentType(2);
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}else if(contenttype6 == 3) {
				// 专题列表
				//List<ILiveThematic> listthematic = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
				List<Topic> listtopic = topicMng.getByEnterpriseId(enterpriseId);
				for(Topic topic:listtopic) {
					ContentSelect contentSelect = new ContentSelect();
					contentSelect.setContentId(topic.getId().intValue());
					contentSelect.setContentName(topic.getName());
					contentSelect.setContentImg(topic.getBanner());
					contentSelect.setContentBrief(topic.getDescript());
					contentSelect.setContentType(3);
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}
			list.addAll(listcontent);
		}
		/*
		List<ContentSelect> list = contentMng.getContentByEnterprise(enterpriseId);
		ILiveHomepageStructure structure4 = contentMng.getStructureById(4);
		ILiveHomepageStructure structure6 = contentMng.getStructureById(6);
		int num4;
		int num6;
		if(structure4 == null) {
			num4 = 2;
		}else {
			num4 = structure4.getShowNum();
		}
		if(structure6 == null) {
			num6 = 2;
		}else {
			num6 = structure6.getShowNum();
		}
		int mark4 = 0,mark6 = 0;
		Iterator<ContentSelect> iterator = list.iterator();
		while (iterator.hasNext()) {
			ContentSelect contentSelect = iterator.next();
			if(contentSelect.getContentType()!=null && contentSelect.getContentType() == 1) {
				ILiveLiveRoom room = roomMng.findById(contentSelect.getContentId()); 
				contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
				String url = roomMng.getH5PlayUrlByRoom(room);
				contentSelect.setContentUrl(url);
			}
			if(contentSelect.getStructureId() == 4) {
				mark4 ++;
				if(mark4>num4) {
					iterator.remove();
				}
			}
			if(contentSelect.getStructureId() == 6) {
				mark6 ++;
				if(mark6>num6) {
					iterator.remove();
				}
			}
		}*/
		JSONArray jsonArray = JSONArray.fromObject(list);
		ResponseUtils.renderJson(response, jsonArray.toString());
	}
	/**
	 * 发布并获取发布内容
	 */
	@RequestMapping(value = "publishcontentandget.do")
	public void publishcontentandget(HttpServletResponse response,Integer enterpriseId,HttpServletRequest request) {
		List<ContentSelect> listnotpush = contentMng.getContentByEnterprise(enterpriseId);
		List<ILiveHomepageStructure> listnopublishStructure = contentMng.getStructureByEnterprise(enterpriseId);
		//更新发布信息
		contentMng.deletePublishContent(enterpriseId);
		for(ContentSelect contentSelect:listnotpush) {
			contentMng.savePublishContent(contentSelect);
		}
		//更新结构信息
		contentMng.deletePublishStructure(enterpriseId);
		for(ILiveHomepageStructure iLiveHomepageStructure:listnopublishStructure) {
			contentMng.savePublishStructure(iLiveHomepageStructure);
		}
		
		List<ContentSelectPublish> list = contentMng.getPublishContentByEnterprise(enterpriseId);
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i).getContentName());
		}
		ILiveHomepageStructurePublish structure4 = contentMng.getPublishStructureById(4,enterpriseId);
		ILiveHomepageStructurePublish structure6 = contentMng.getPublishStructureById(6,enterpriseId);
		int policy4;
		int policy6;
		int num4;
		int num6;
		int contenttype4;
		int contenttype6;
		if(structure4 == null) {
			num4 = 1;
			policy4 = 1;
			contenttype4 = 1;
		}else {
			if(structure4.getShowNum()==null) {
				num4 = 1;
			}else {
				num4 = structure4.getShowNum();
			}
			if(structure4.getPolicy()==null) {
				policy4 = 1;
			}else {
				policy4 = structure4.getPolicy();
			}
			if(structure4.getShowContentType()==null) {
				contenttype4 = 1;
			}else {
				contenttype4 = structure4.getShowContentType();
			}
		}
		if(structure6 == null) {
			num6 = 2;
			policy6 = 1;
			contenttype6 = 1;
		}else {
			if (structure6.getShowNum()==null) {
				num6 = 2;
			}else {
				num6 =structure6.getShowNum();
			}
			if (structure6.getPolicy()==null) {
				policy6 = 1;
			}else {
				policy6 = structure6.getPolicy();
			}
			if(structure6.getShowContentType()==null) {
				contenttype6 = 1;
			}else {
				contenttype6 = structure6.getShowContentType();
			}
		}
		int mark4 = 0,mark6 = 0;
		Iterator<ContentSelectPublish> iterator = list.iterator();
		while (iterator.hasNext()) {
			ContentSelectPublish contentSelect = iterator.next();
			if(contentSelect.getContentType()!=null && contentSelect.getContentType() == 1) {
				ILiveLiveRoom room = roomMng.findById(contentSelect.getContentId()); 
				contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
			}
			if(contentSelect.getStructureId() == 4) {
				if(policy4 == 1) {
					mark4 ++;
					if(mark4>num4) {
						iterator.remove();
					}
				}else if(policy4 == 2) {
					iterator.remove();
				}
			}
			if(contentSelect.getStructureId() == 6) {
				if(policy6 == 1) {
					mark6 ++;
					if(mark6>num6) {
						iterator.remove();
					}
				}else if(policy6 == 2) {
					iterator.remove();
				}
			}
		}
		if(policy4 == 2) {
			List<ContentSelectPublish> listcontent = new LinkedList<>();
			if (contenttype4 == 1) {
				List<ILiveLiveRoom> listroom4 = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId, num4);
				for(ILiveLiveRoom room:listroom4) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(room.getRoomId());
					contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
					contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
					contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
					contentSelect.setContentType(1);
					contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}else if(contenttype4 == 2) {
				List<ILiveMediaFile> listfile = mediaFileMng.getListByType(1,enterpriseId);
				for(ILiveMediaFile file:listfile) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(file.getFileId().intValue());
					contentSelect.setContentName(file.getMediaFileName());
					contentSelect.setContentImg(file.getFileCover());
					contentSelect.setContentBrief(file.getMediaFileDesc());
					contentSelect.setContentType(2);
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}else if(contenttype4 == 3) {
				// 专题列表
				//List<ILiveThematic> listthematic = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
				List<Topic> topiclist = topicMng.getByEnterpriseId(enterpriseId);
				for(Topic topic:topiclist) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(topic.getId().intValue());
					contentSelect.setContentName(topic.getName());
					contentSelect.setContentImg(topic.getBanner());
					contentSelect.setContentBrief(topic.getDescript());
					contentSelect.setContentType(3);
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}
			list.addAll(listcontent);
		}
		if(policy6 == 2) {
			List<ContentSelectPublish> listcontent = new LinkedList<>();
			if (contenttype6 == 1) {
				List<ILiveLiveRoom> listroom4 = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId, num4);
				for(ILiveLiveRoom room:listroom4) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(room.getRoomId());
					contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
					contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
					contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
					contentSelect.setContentType(1);
					contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}else if(contenttype6 == 2) {
				List<ILiveMediaFile> listfile = mediaFileMng.getListByType(1,enterpriseId);
				for(ILiveMediaFile file:listfile) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(file.getFileId().intValue());
					contentSelect.setContentName(file.getMediaFileName());
					contentSelect.setContentImg(file.getFileCover());
					contentSelect.setContentBrief(file.getMediaFileDesc());
					contentSelect.setContentType(2);
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}else if(contenttype6 == 3) {
				// 专题列表
				//List<ILiveThematic> listthematic = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
				List<Topic> listtop = topicMng.getByEnterpriseId(enterpriseId);
				for(Topic topic:listtop) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(topic.getId().intValue());
					contentSelect.setContentName(topic.getName());
					contentSelect.setContentImg(topic.getBanner());
					contentSelect.setContentBrief(topic.getDescript());
					contentSelect.setContentType(3);
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}
			list.addAll(listcontent);
		}
		JSONArray jsonArray = JSONArray.fromObject(list);
		UserBean user = ILiveUtils.getUser(request);
		workLogMng.save(new WorkLog(WorkLog.MODEL_HOMEPAGE, enterpriseId+"", jsonArray.toString(), WorkLog.MODEL_HOMEPAGE_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
		
		ResponseUtils.renderJson(response, jsonArray.toString());
	}
	/**
	 * 获取发布内容
	 */
	@RequestMapping(value = "initpublishcontentandget.do")
	public void initpublishcontentandget( HttpServletResponse response,Integer enterpriseId) {
		List<ContentSelectPublish> list = contentMng.getPublishContentByEnterprise(enterpriseId);
		ILiveHomepageStructurePublish structure4 = contentMng.getPublishStructureById(4,enterpriseId);
		ILiveHomepageStructurePublish structure6 = contentMng.getPublishStructureById(6,enterpriseId);
		int policy4;
		int policy6;
		int num4;
		int num6;
		int contenttype4;
		int contenttype6;
		if(structure4 == null) {
			num4 = 2;
			policy4 = 1;
			contenttype4 = 1;
		}else {
			if(structure4.getShowNum()==null) {
				num4 = 2;
			}else {
				num4 = structure4.getShowNum();
			}
			if(structure4.getPolicy()==null) {
				policy4 = 1;
			}else {
				policy4 = structure4.getPolicy();
			}
			if(structure4.getShowContentType()==null) {
				contenttype4 = 1;
			}else {
				contenttype4 = structure4.getShowContentType();
			}
		}
		if(structure6 == null) {
			num6 = 2;
			policy6 = 1;
			contenttype6 = 1;
		}else {
			if (structure6.getShowNum()==null) {
				num6 = 2;
			}else {
				num6 =structure6.getShowNum();
			}
			if (structure6.getPolicy()==null) {
				policy6 = 1;
			}else {
				policy6 = structure6.getPolicy();
			}
			if(structure6.getShowContentType()==null) {
				contenttype6 = 1;
			}else {
				contenttype6 = structure6.getShowContentType();
			}
		}
		int mark4 = 0,mark6 = 0;
		Iterator<ContentSelectPublish> iterator = list.iterator();
		while (iterator.hasNext()) {
			ContentSelectPublish contentSelect = iterator.next();
			if(contentSelect.getContentType()!=null && contentSelect.getContentType() == 1) {
				ILiveLiveRoom room = roomMng.findById(contentSelect.getContentId()); 
				contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
			}
			if(contentSelect.getStructureId() == 4) {
				if(policy4 == 1) {
					mark4 ++;
					if(mark4>num4) {
						iterator.remove();
					}
				}else if(policy4 == 2) {
					iterator.remove();
				}
			}
			if(contentSelect.getStructureId() == 6) {
				if(policy6 == 1) {
					mark6 ++;
					if(mark6>num6) {
						iterator.remove();
					}
				}else if(policy6 == 2) {
					iterator.remove();
				}
			}
		}
		if(policy4 == 2) {
			List<ContentSelectPublish> listcontent = new LinkedList<>();
			if (contenttype4 == 1) {
				List<ILiveLiveRoom> listroom4 = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId, num4);
				for(ILiveLiveRoom room:listroom4) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(room.getRoomId());
					contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
					contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
					contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
					contentSelect.setContentType(1);
					contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}else if(contenttype4 == 2) {
				List<ILiveMediaFile> listfile = mediaFileMng.getListByType(1,enterpriseId);
				for(ILiveMediaFile file:listfile) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(file.getFileId().intValue());
					contentSelect.setContentName(file.getMediaFileName());
					contentSelect.setContentImg(file.getFileCover());
					contentSelect.setContentBrief(file.getMediaFileDesc());
					contentSelect.setContentType(2);
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}else if(contenttype4 == 3) {
				// 专题列表
				//List<ILiveThematic> listthematic = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
				List<Topic> listtop = topicMng.getByEnterpriseId(enterpriseId);
				for(Topic topic:listtop) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(topic.getId().intValue());
					contentSelect.setContentName(topic.getName());
					contentSelect.setContentImg(topic.getBanner());
					contentSelect.setContentBrief(topic.getDescript());
					contentSelect.setContentType(3);
					contentSelect.setStructureId(4);
					listcontent.add(contentSelect);
				}
			}
			list.addAll(listcontent);
		}
		if(policy6 == 2) {
			List<ContentSelectPublish> listcontent = new LinkedList<>();
			if (contenttype6 == 1) {
				List<ILiveLiveRoom> listroom4 = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId, num4);
				for(ILiveLiveRoom room:listroom4) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(room.getRoomId());
					contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
					contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
					contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
					contentSelect.setContentType(1);
					contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}else if(contenttype6 == 2) {
				List<ILiveMediaFile> listfile = mediaFileMng.getListByType(1,enterpriseId);
				for(ILiveMediaFile file:listfile) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(file.getFileId().intValue());
					contentSelect.setContentName(file.getMediaFileName());
					contentSelect.setContentImg(file.getFileCover());
					contentSelect.setContentBrief(file.getMediaFileDesc());
					contentSelect.setContentType(2);
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}else if(contenttype6 == 3) {
				// 专题列表
				//List<ILiveThematic> listthematic = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
				List<Topic> topiclist = topicMng.getByEnterpriseId(enterpriseId);
				for(Topic topic:topiclist) {
					ContentSelectPublish contentSelect = new ContentSelectPublish();
					contentSelect.setContentId(topic.getId().intValue());
					contentSelect.setContentName(topic.getName());
					contentSelect.setContentImg(topic.getBanner());
					contentSelect.setContentBrief(topic.getDescript());
					contentSelect.setContentType(3);
					contentSelect.setStructureId(6);
					listcontent.add(contentSelect);
				}
			}
			list.addAll(listcontent);
		}
		/*
		List<ContentSelectPublish> list = contentMng.getPublishContentByEnterprise(enterpriseId);
		ILiveHomepageStructurePublish structure4 = contentMng.getPublishStructureById(4);
		ILiveHomepageStructurePublish structure6 = contentMng.getPublishStructureById(6);
		int num4;
		int num6;
		if(structure4 == null) {
			num4 = 2;
		}else {
			num4 = structure4.getShowNum();
		}
		if(structure6 == null) {
			num6 = 2;
		}else {
			num6 = structure6.getShowNum();
		}
		int mark4 = 0,mark6 = 0;
		Iterator<ContentSelectPublish> iterator = list.iterator();
		while (iterator.hasNext()) {
			ContentSelectPublish contentSelect = iterator.next();
			if(contentSelect.getContentType()!=null && contentSelect.getContentType() == 1) {
				ILiveLiveRoom room = roomMng.findById(contentSelect.getContentId()); 
				contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
				String url = roomMng.getH5PlayUrlByRoom(room);
				contentSelect.setContentUrl(url);
			}
			if(contentSelect.getStructureId() == 4) {
				mark4 ++;
				if(mark4>num4) {
					iterator.remove();
				}
			}
			if(contentSelect.getStructureId() == 6) {
				mark6 ++;
				if(mark6>num6) {
					iterator.remove();
				}
			}
		}*/
		JSONArray jsonArray = JSONArray.fromObject(list);
		ResponseUtils.renderJson(response, jsonArray.toString());
	}
	//初始化结构
	public void initStructure(Integer enterpriseId) {
		
		//1
		ILiveHomepageStructure structure1 = new ILiveHomepageStructure();
		structure1.setEnterpriseId(enterpriseId);
		structure1.setType(1);
		structure1.setOrders(1);
		structure1.setStructureId(1);
		contentMng.saveHomepageStructure(structure1);
		//2
		ILiveHomepageStructure structure2 = new ILiveHomepageStructure();
		structure2.setEnterpriseId(enterpriseId);
		structure2.setType(2);
		structure2.setOrders(2);
		structure2.setStructureId(2);
		contentMng.saveHomepageStructure(structure2);
		//3
		ILiveHomepageStructure structure3 = new ILiveHomepageStructure();
		structure3.setEnterpriseId(enterpriseId);
		structure3.setType(3);
		structure3.setOrders(3);
		structure3.setStructureId(3);
		contentMng.saveHomepageStructure(structure3);
		//4
		ILiveHomepageStructure structure4 = new ILiveHomepageStructure();
		structure4.setEnterpriseId(enterpriseId);
		structure4.setType(4);
		structure4.setOrders(4);
		structure4.setPolicy(1);
		structure4.setShowNum(1);
		structure4.setStructureId(4);
		contentMng.saveHomepageStructure(structure4);
		//5
		ILiveHomepageStructure structure5 = new ILiveHomepageStructure();
		structure5.setEnterpriseId(enterpriseId);
		structure5.setType(3);
		structure5.setOrders(5);
		structure5.setStructureId(5);
		contentMng.saveHomepageStructure(structure5);
		//6
		ILiveHomepageStructure structure6 = new ILiveHomepageStructure();
		structure6.setEnterpriseId(enterpriseId);
		structure6.setType(5);
		structure6.setOrders(6);
		structure6.setPolicy(1);
		structure6.setShowNum(2);
		structure6.setStructureId(6);
		contentMng.saveHomepageStructure(structure6);
	}
	
	@Autowired
	private SentitivewordFilterMng mng;
	
	/**
	 * 测试敏感词
	 * 
	 * @param userid
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/test.do")
	public void testsentitive(HttpServletRequest request, HttpServletResponse response) {
	}
	
	/**
	 * 添加一个链接
	 * @param link
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="addLianjie.do",method=RequestMethod.POST)
	public void addLianjie(ILiveHomepageLink link,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			ILiveHomepageLink l = iLiveHomepageLinkMng.save(link);
			result.put("status", 1);
			result.put("data", net.sf.json.JSONObject.fromObject(l));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 修改名称
	 * @param id
	 * @param name
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="editName.do",method=RequestMethod.POST)
	public void editName(Integer id,String name,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			ContentSelect content = contentMng.getContentById(id);
			content.setContentName(name);
			contentMng.updateContent(content);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 修改链接
	 * @param id
	 * @param name
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="editUrl.do",method=RequestMethod.POST)
	public void editUrl(Integer id,String url,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			ContentSelect content = contentMng.getContentById(id);
			content.setContentUrl(url);
			contentMng.updateContent(content);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@ResponseBody
	@RequestMapping(value="searchbytypeandname.do",method=RequestMethod.POST)
	public void searchbytypeandname(String name,Integer identype,Integer enterpriseId,HttpServletResponse response) {
		net.sf.json.JSONObject result = new net.sf.json.JSONObject();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			switch (identype) {
			case 1:
				List<ILiveLiveRoom> list1 = roomMng.findByEnterpriseIdAndName(name,enterpriseId);
				JSONArray array1 = new JSONArray();
				for(ILiveLiveRoom live:list1) {
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("roomId", live.getRoomId());
					jsonObject.put("liveTitle", live.getLiveEvent().getLiveTitle());
					jsonObject.put("createPerson", live.getCreatePerson());
					jsonObject.put("createTime", format.format(live.getCreateTime()));
					array1.add(jsonObject);
				}
				result.put("data", array1);
				break;
			case 2:
				List<ILiveMediaFile> list2 = mediaFileMng.getListByTypeAndName(name,1,enterpriseId);
				JSONArray array2 = new JSONArray();
				for(ILiveMediaFile file:list2) {
					net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
					jsonObject.put("fileId", file.getFileId());
					jsonObject.put("mediaFileName", file.getMediaFileName());
					jsonObject.put("mediaCreateTime", format.format(file.getMediaCreateTime()));
					array2.add(jsonObject);
				}
				result.put("data", array2);
				break;
			case 3:
				// 专题列表
				//List<ILiveThematic> list3 = iLiveThematicMng.getListByEnterpriseIdAndName(naem,enterpriseId);
				//result.put("data", list3);
				break;
			}
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 1);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
}
