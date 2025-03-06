package com.bvRadio.iLive.iLive.action.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.util.Base64;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.Topic;
import com.bvRadio.iLive.iLive.entity.TopicInnerContent;
import com.bvRadio.iLive.iLive.entity.TopicInnerType;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.manager.TopicInnerTypeMng;
import com.bvRadio.iLive.iLive.manager.TopicMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="topic")
public class TopicController {

	@Autowired
	private TopicMng topicMng;	//专题
	
	@Autowired
	private TopicInnerTypeMng topicInnerTypeMng;	//专题类型
	
	@Autowired
	private ILiveLiveRoomMng roomMng; // 直播间
	
	@Autowired
	private ILiveMediaFileMng mediaFileMng; // 选择 视频 文档 图片
	
	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;
	
	/*@Autowired
	private WorkLogMng workLogMng;*/
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	/**
	 * 专题列表页
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(Model model,String name,Integer pageNo,Integer pageSize,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Pagination page = topicMng.getPage(Long.parseLong(user.getUserId()), name, pageNo==null?1:pageNo, 10);

		model.addAttribute("userId", user.getUserId());
		model.addAttribute("pageNum", page.getTotalPage());
		model.addAttribute("list", page.getList());
		model.addAttribute("leftActive", "5_2");
		model.addAttribute("topActive", "2");
		return "topic/list";
	}
	
	/**
	 * 跳转至新增专题页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="tocreate",method=RequestMethod.GET)
	public String tocreate(Model model) {
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("leftActive", "5_2");
		model.addAttribute("topActive", "2");
		return "topic/create";
	}
	
	private Integer selectServerGroup() {
		return 100;
	}
	

	/**
	 * 新增一条专题
	 * @param topic
	 * @param str
	 * @param response
	 */
	@RequestMapping(value="insertopic")
	public void insertTopic(Topic topic,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			UserBean user = ILiveUtils.getUser(request);
			topic.setUserId(Long.parseLong(user.getUserId()));
			topic.setEnterpriseId(user.getEnterpriseId());
			Long id = topicMng.save(topic);
			//workLogMng.save(new WorkLog(WorkLog.MODEL_TOPIC, id+"", net.sf.json.JSONObject.fromObject(topic).toString(), WorkLog.MODEL_TOPIC_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			result.put("status", 1);
		} catch (Exception e) {
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 基本设置页面
	 * @param id
	 * @param model
	 */
	@RequestMapping(value="tobasicEdit")
	public String basicEdit(Long id,Model model) {
		Topic topic = topicMng.getById(id);
		String topic_prefix_doamin = ConfigUtils.get("topic_prefix_doamin");
		try {
			topic.setDomain(topic_prefix_doamin+topic.getId());
			System.out.println(topic_prefix_doamin);
			topicMng.update(topic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("topic", topic);
		model.addAttribute("leftActive", "5_2");
		model.addAttribute("topActive", "2");
		return "topic/basicEdit";
	}
	
	/**
	 * 发布、上下架
	 * @param id
	 * @param type 1为发布  2为上架  3为下架
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="pubTopic")
	public void pubTopic(Long id,Integer type,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Topic topic = topicMng.getById(id);
			switch (type) {
				case 1:
					topic.setIsPublish(1);
					break;
				case 2:
					topic.setIsPut(1);
					break;
				case 3:
					topic.setIsPut(0);
					break;
			}
			topicMng.update(topic);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 修改专题基本设置
	 * @param topic
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="editTopic",method=RequestMethod.POST)
	public void edittopic(Topic topic,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Topic edit = topicMng.getById(topic.getId());
			edit.setName(topic.getName());
			edit.setLogo(topic.getLogo());
			edit.setBanner(topic.getBanner());
			edit.setDescript(topic.getDescript());
			topicMng.update(edit);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 删除专题
	 * @param topic
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="deleteTopic",method=RequestMethod.POST)
	public void deleteTopic(Long id,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			topicMng.delete(id);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 专题详情页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="toeditTopic")
	public String detail(Model model,Long id,HttpServletRequest request) {
		Topic topic = topicMng.getById(id);
		HttpSession session = request.getSession();
		session.setAttribute("topic_"+id, topic);
		Integer serverGroupId = this.selectServerGroup();
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId = user.getEnterpriseId();
		// 直播间列表
		List<ILiveLiveRoom> list12 = roomMng.findRoomListPassByEnterprise(enterpriseId);
		List<ILiveLiveRoom> list1 = new ArrayList<>();
		for(ILiveLiveRoom r:list12) {
			ILiveLiveRoom r1 = roomMng.findById(r.getRoomId());
			list1.add(r1);
		}
		// 视频列表
		List<ILiveMediaFile> list2 = mediaFileMng.getListByType(1,enterpriseId);
		// 专题列表
		List<Topic> list3 = topicMng.getByEnterpriseId(enterpriseId);
		
		model.addAttribute("list1", list1);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("topic", topic);
		model.addAttribute("leftActive", "5_2");
		model.addAttribute("topActive", "2");
		return "topic/detail";
	}
	
	/**
	 * 从session中获取单个专题信息
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="getTopic")
	public void getTopic(Long id,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			result.put("data", topic);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 从session中获取单个专题类型信息
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="getType")
	public void getType(Long id,Long typeId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			Iterator<TopicInnerType> iterator = topic.getListType().iterator();
			while (iterator.hasNext()) {
				TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
				if (topicInnerType.getId().equals(typeId)) {
					result.put("data", topicInnerType);
					break;
				}
			}
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 从session中保存单个专题类型信息
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="saveType")
	public void saveType(Long id,Long typeId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			Iterator<TopicInnerType> iterator = topic.getListType().iterator();
			while (iterator.hasNext()) {
				TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
				if (topicInnerType.getId().equals(typeId)) {
					topicInnerTypeMng.updateSessionType(topicInnerType);
					result.put("status", 1);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 保存标题信息
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="saveTypeTitle")
	public void saveTypeTitle(Long id,Long typeId,Integer identify,String color,String fontColor,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			Iterator<TopicInnerType> iterator = topic.getListType().iterator();
			while (iterator.hasNext()) {
				TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
				if (topicInnerType.getId().equals(typeId)) {
					if(topicInnerType.getContentList().get(0)!=null) {
						topicInnerType.getContentList().get(0).setBackgroundColor(color);
						topicInnerType.getContentList().get(0).setFontColor(fontColor);;
						result.put("status", 1);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 保存文本信息
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="saveTypeText")
	public void saveTypeText(Long id,Long typeId,Integer identify,TopicInnerContent content,Integer paddingSize
			,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			List<TopicInnerType> listType = topic.getListType();
			if(typeId == null) {
				TopicInnerType type = topicInnerTypeMng.createTextType(topic, content);
				listType.add(type);
			}else {
				Iterator<TopicInnerType> iterator = listType.iterator();
				while (iterator.hasNext()) {
					TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
					if (topicInnerType.getId().equals(typeId)) {
						List<TopicInnerContent> contentList = topicInnerType.getContentList();
						TopicInnerContent con = contentList.get(0);
						if(con!=null) {
							con.setName(content.getName());
							con.setFontColor(content.getFontColor());
							con.setFontSize(content.getFontSize());
							con.setAlign(content.getAlign());
							con.setTilt(content.getTilt());
							con.setOverStriking(content.getOverStriking());
							con.setPaddingSize(paddingSize);
							result.put("status", 1);
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 保存单个图片信息
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="saveTypeImg")
	public void saveTypeImg(Long id,Long typeId,Integer identify,String backgroundUrl,String contenturl,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			if(typeId==null) {
				TopicInnerType createOneImgType = topicInnerTypeMng.createOneImgType(topic, backgroundUrl);
				List<TopicInnerType> listType = topic.getListType();
				listType.add(createOneImgType);
			}else {
				Iterator<TopicInnerType> iterator = topic.getListType().iterator();
				while (iterator.hasNext()) {
					TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
					if (topicInnerType.getId().equals(typeId)) {
						List<TopicInnerContent> contentList = topicInnerType.getContentList();
						TopicInnerContent con = contentList.get(0);
						if(con!=null) {
							con.setBackgroundUrl(backgroundUrl);
							con.setContentUrl(contenturl);
							result.put("status", 1);
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 保存多个图片信息
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="saveTypeManyImg")
	public void saveTypeManyImg(Long id,Long typeId,Integer identify,String jsonarr,Integer num,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			if(typeId == null) {
				TopicInnerType createManyImgType = topicInnerTypeMng.createManyImgType(topic,jsonarr,num);
				List<TopicInnerType> listType = topic.getListType();
				listType.add(createManyImgType);
			}else {
				Iterator<TopicInnerType> iterator = topic.getListType().iterator();
				while (iterator.hasNext()) {
					TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
					if (topicInnerType.getId().equals(typeId)) {
						topicInnerType.setNum(num);
						List<TopicInnerContent> contentList = topicInnerType.getContentList();
						contentList.clear();
						JSONArray jsonArray = JSONArray.fromObject(jsonarr);
						for(int i=0;i<jsonArray.size();i++) {
							JSONObject obj = jsonArray.getJSONObject(i);
							TopicInnerContent content = new TopicInnerContent();
							content.setBackgroundUrl(obj.getString("backgroundUrl"));
							content.setName(obj.getString("name"));
							content.setContentType(obj.getInt("type"));
							content.setContentUrl(obj.getString("contentUrl"));
							content.setContentId(obj.getInt("id"));
							contentList.add(content);
						}
						break;
					}
				}
			}
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 保存单列多列信息
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="saveTypeColumn")
	public void saveTypeColumn(Long id,Long typeId,Integer identify,String str,Integer mark,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			if(typeId==null) {
				TopicInnerType createColumnType = topicInnerTypeMng.createColumnType(topic,str,mark);
				List<TopicInnerType> listType = topic.getListType();
				listType.add(createColumnType);
			}else {
				Iterator<TopicInnerType> iterator = topic.getListType().iterator();
				while (iterator.hasNext()) {
					TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
					if (topicInnerType.getId().equals(typeId)) {
						List<TopicInnerContent> contentList = topicInnerType.getContentList();
						contentList.clear();
						JSONArray jsonArray = JSONArray.fromObject(str);
						for(int i=0;i<jsonArray.size();i++) {
							JSONObject obj = jsonArray.getJSONObject(i);
							TopicInnerContent content = new TopicInnerContent();
							content.setBackgroundUrl(obj.getString("background"));
							content.setName(obj.getString("name"));
							content.setContentType(obj.getInt("type"));
							content.setContentUrl(obj.getString("contentUrl"));
							content.setContentId(obj.getInt("id"));
							contentList.add(content);
						}
						break;
					}
				}
			}
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 保存专题信息至数据库
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="saveAllPage")
	public void saveAllPage(Long topicId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+topicId);
			topicMng.sessionToDB(topic);
			Topic byId = topicMng.getById(topicId);
			session.setAttribute("topic", byId);
			//worksave(request,byId,topicId);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 添加一个内部结构
	 * @param topicInnerType
	 * @param response
	 */
	@RequestMapping(value="saveInnerType")
	public void saveInnerType(Long topicId,Integer type,HttpServletResponse response,HttpServletRequest request) {
		JSONObject josnObject = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+topicId);
			TopicInnerContent content = new TopicInnerContent();
			TopicInnerType innerType = null;
			if(type == 1) {
				content.setName("新增文字");
				innerType = topicInnerTypeMng.createTextType(topic, content);
			}else if(type == 2) {
				innerType = topicInnerTypeMng.createOneImgType(topic, "");
			}else if (type == 3) {
				innerType = topicInnerTypeMng.createManyImgType(topic, new JSONArray().toString(), 0);
			}else if(type == 4){
				innerType = topicInnerTypeMng.createColumnType(topic,  new JSONArray().toString(), 1);
			}else if(type == 5) {
				innerType = topicInnerTypeMng.createColumnType(topic,  new JSONArray().toString(), 2);
			}
			
			topic.getListType().add(innerType);
			josnObject.put("typeId", innerType.getId());
			josnObject.put("status", 1);
		} catch (Exception e) {
			josnObject.put("status", 2);
		}
		ResponseUtils.renderJson(response, josnObject.toString());
	}
	
	/**
	 * 删除一个内部结构
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="deleteOneType",method=RequestMethod.POST)
	public void deleteInner(Long id,Long typeId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			if (typeId!=null) {
				HttpSession session = request.getSession();
				Topic topic = (Topic) session.getAttribute("topic_"+id);
				Iterator<TopicInnerType> iterator = topic.getListType().iterator();
				while (iterator.hasNext()) {
					TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
					if (topicInnerType.getId().equals(typeId)) {
						iterator.remove();
					}
				}
			}
			result.put("status",1 );
		} catch (Exception e) {
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	
	/**
	 * 保存已选择的内容
	 */
	@ResponseBody
	@RequestMapping(value = "saveselect.do")
	public void saveselect(Integer contentId,Integer contentType,HttpServletResponse response) {
		TopicInnerContent content = new TopicInnerContent();
		if(contentType == 1) {
			ILiveLiveRoom room = roomMng.findById(contentId);
			if (room!=null) {
				ILiveEvent liveEvent = room.getLiveEvent();
				if(liveEvent!=null) {
					ILiveServerAccessMethod accessMethodBySeverGroupId = null;
					try {
						accessMethodBySeverGroupId = accessMethodMng
								.getAccessMethodBySeverGroupId(room.getServerGroupId());
					} catch (Exception e) {
						e.printStackTrace();
					}
					content.setName(liveEvent.getLiveTitle());
					if(liveEvent.getConverAddr()==null||liveEvent.getConverAddr().equals("")||liveEvent.getConverAddr().equals("null")) {
						content.setBackgroundUrl("");
					}else {
						content.setBackgroundUrl(liveEvent.getConverAddr());
					}
					content.setDescript(liveEvent.getLiveDesc());
					String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId=" + contentId;
					content.setContentUrl(liveAddr);
				}
			}
		}else if(contentType == 2) {
			long fileId = (long)contentId;
			ILiveMediaFile file = mediaFileMng.selectILiveMediaFileByFileId(fileId);
			if (file!=null) {
				content.setName(file.getMediaFileName());
				if(file.getFileCover()==null||file.getFileCover().equals("")||file.getFileCover().equals("null")) {
					content.setBackgroundUrl("");
				}else {
					content.setBackgroundUrl(file.getFileCover());
				}
				//content.setBackgroundUrl(file.get);
				content.setDescript(file.getMediaFileDesc());
				
				Integer serverMountId = file.getServerMountId();
				ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
				String mediavedioAddr = serverAccess.getH5HttpUrl() + "/phone" + "/review.html?roomId="
						+ (file.getLiveRoomId() == null ? 0 : file.getLiveRoomId()) + "&fileId=" + fileId;
				content.setContentUrl(mediavedioAddr);
				
			}
		}else if (contentType == 3) {
			long thematicId = (long)contentId;
			Topic thematic = topicMng.getById(thematicId);
			if(thematic!=null) {
				content.setName(thematic.getName());
				if (thematic.getLogo()==null||thematic.getLogo().equals("")||thematic.getLogo().equals("null")) {
					content.setBackgroundUrl("");
				}else {
					content.setBackgroundUrl(thematic.getLogo());
				}
				content.setBackgroundUrl(thematic.getDescript());
				content.setContentUrl(thematic.getDomain());
			}
		}
		
		JSONObject resultJson = JSONObject.fromObject(content);
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	/**
	 * 获取列表信息
	 * @param pageSize
	 * @param pageNo
	 * @param userId
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="getList")
	public void getList(Integer pageSize,Integer pageNo,Long userId,String name,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Pagination page = topicMng.getPage(userId, name, pageNo, pageSize);
			result.put("data", page.getList());
			result.put("pageNum", page.getTotalPage());
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 类型排序
	 * @param str
	 * @param response
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="refreshOrder")
	public void refreshOrder(Long id,String str,HttpServletResponse response,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			JSONArray array = JSONArray.fromObject(str);
			List<TopicInnerType> newList = new ArrayList<>();
			for(int i=0;i<array.size();i++) {
				JSONObject object = array.getJSONObject(i);
				Long typeId = object.getLong("dataId");
				Iterator<TopicInnerType> iterator = topic.getListType().iterator();
				while (iterator.hasNext()) {
					TopicInnerType topicInnerType = (TopicInnerType) iterator.next();
					if (typeId.equals(topicInnerType.getId())) {
						topicInnerType.setOrderN(i);
						newList.add(topicInnerType);
						iterator.remove();
						break;
					}
				}
			}
			topic.setListType(newList);
			
			result.put("status", 1);
			result.put("message", "排序成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
			result.put("message", "排序失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 单列双列排序
	 * @param str
	 * @param typeId
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="sortOneColumn",method=RequestMethod.POST)
	public void sortOneColumn(Long id,String str,Long typeId,HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			topicMng.sortColumnType(topic,str,typeId);
			result.put("status", 1);
			result.put("message", "排序成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
			result.put("message", "排序失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 多图排序
	 * @param str
	 * @param typeId
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="sortManyImg",method=RequestMethod.POST)
	public void sortManyImg(Long id,String str,Long typeId,Integer num,HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			HttpSession session = request.getSession();
			Topic topic = (Topic) session.getAttribute("topic_"+id);
			topicMng.sortManyImg(topic,str,typeId,num);
			result.put("status", 1);
			result.put("message", "排序成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
			result.put("message", "排序失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	@RequestMapping(value="base64upload.do")
	public void base64upload(String str,HttpServletRequest request,HttpServletResponse response) {
		net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
		try {
			String string = str.replace("data:image/jpeg;base64,", "");
			//String string=str;
			System.out.println(string);
			String fileName = "1234.jpeg";
			//File tempFile = new File("E://test",fileName);  
			File tempFile = new File(fileName);  
			//File tempFile = File.createTempFile("1234", ".jpeg",new File("E://test"));
			FileOutputStream out=new FileOutputStream(tempFile);  
			@SuppressWarnings("resource")
			BufferedOutputStream Bout=new BufferedOutputStream(out);  
			byte[] bs = Base64.decodeFast(string);
			Bout.write(bs);
			String tempFileName = System.currentTimeMillis() + "."
					+ fileName.substring(fileName.lastIndexOf(".") + 1);
			String filePath = FileUtils.getTimeFilePath(tempFileName);
			ILiveUploadServer iLiveUploadServer = iLiveUploadServerMng.getDefaultServer();
			boolean result = false;
			if (iLiveUploadServer != null) {
				FileInputStream in = new FileInputStream(tempFile);
				result = iLiveUploadServer.upload(filePath, in);
			}
			System.out.println(tempFile);
			if (tempFile.exists()) {
				System.out.println("临时文件存在");
				tempFile.delete();
				System.out.println("临时文件删除");
			}
			String httpUrl = "";
			if (result) {
				httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath() + filePath;
				System.out.println("2: "+httpUrl);
			}
			jsonObject.put("code", "1");
			jsonObject.put("dataurl",httpUrl );
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "2");
		}
		ResponseUtils.renderJson(response, jsonObject.toString());
	}
	
	private void delete(File file) {
		//判断文件是否存在
		if(file.exists()) {
			//判断是否是文件
			if (file.isFile()) {
				file.delete();
			}else {
				System.out.println("要删除的文件不存在");
			}
		}
	}
	
	/**
	 * 专题修改记录日志
	 * @param request
	 * @param id
	 */
	private void worksave(HttpServletRequest request,Topic topic,Long id) {
		UserBean user = ILiveUtils.getUser(request);
		//workLogMng.save(new WorkLog(WorkLog.MODEL_TOPIC, id+"", net.sf.json.JSONObject.fromObject(topic).toString(), WorkLog.MODEL_TOPIC_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
	}
	
	
}
