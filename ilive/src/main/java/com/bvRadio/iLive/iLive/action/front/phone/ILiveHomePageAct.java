package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.Element;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.cache.MessageCache;
import com.bvRadio.iLive.iLive.entity.ContentSelectPublish;
import com.bvRadio.iLive.iLive.entity.ILiveAppointment;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageStructurePublish;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveThematic;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveAppointmentMng;
import com.bvRadio.iLive.iLive.manager.ILiveContentSelectMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveThematicMng;
import com.bvRadio.iLive.iLive.manager.SentitivewordFilterMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;
import com.bvRadio.iLive.iLive.util.Dom4jUtil;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.util.SerializeUtil;
import com.bvRadio.iLive.iLive.util.TerminalUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.bvRadio.iLive.manager.manager.ILiveEnterpriseMng;
import com.jwzt.comm.StringUtils;

@Controller
@RequestMapping(value = "/homepage")
public class ILiveHomePageAct {

	@Autowired
	private ILiveContentSelectMng contentMng; // 选择显示的内容

	@Autowired
	private ILiveEnterpriseFansMng fansMng; // 粉丝管理
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng; // 获取企业信息
	@Autowired
	private ILiveLiveRoomMng roomMng; // 直播间
	@Autowired
	private ILiveAppointmentMng iLiveAppointmentMng; // 预约
	@Autowired
	private ILiveMediaFileMng mediaFileMng; // 选择 视频 文档 图片
	@Autowired
	private ILiveThematicMng iLiveThematicMng;// 专题
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	@Autowired
	private ILiveEventMng iLiveEventMng;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void buildMessage() {
		System.out.println("开始构建message.xmlToMessageCache");
		if (MessageCache.messageCache.isEmpty()) {
			try {
				ClassPathResource classPathResource = new ClassPathResource("message.xml");
				String absolutePath = classPathResource.getFile().getAbsolutePath();
				System.out.println("absolutePath=============>" + absolutePath);
				Document document = Dom4jUtil.getDocument(absolutePath);
				Element rootElement = document.getRootElement();
				Element element = rootElement.element("message");
				List<Element> elements = element.elements();
				for (Element ele : elements) {
					String name = ele.getName();
					String textTrim = ele.getTextTrim();
					MessageCache.messageCache.put(name, textTrim);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取企业信息
	 * 
	 * @param enterpriseId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "homepageenterprise.jspx")
	public void homepageenterprise(Integer enterpriseId, HttpServletResponse response, HttpServletRequest request) {
		if(JedisUtils.exists("homepageenterprise:"+enterpriseId)) {
			ResponseUtils.renderJson(request, response, JedisUtils.get("homepageenterprise:"+enterpriseId));
			return;
		}else {
			ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterpriseById(enterpriseId);
			//过滤企业敏感信息
			iLiveEnterprise.setIdCardImg("");
			iLiveEnterprise.setContactName("");
			iLiveEnterprise.setCity("");
			iLiveEnterprise.setArea("");
			iLiveEnterprise.setUserPhone("");
			iLiveEnterprise.setIdCardImg("");
			iLiveEnterprise.setContactPhone("");	
			
			JSONObject resultJson = new JSONObject();
			if (iLiveEnterprise == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取企业信息失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			} else {
				// 获取企业信息
				try {
					int num = fansMng.getFansNum(enterpriseId);
					iLiveEnterprise.setFansNum(num);
					JSONObject json = new JSONObject(iLiveEnterprise);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "获取企业信息成功");
					resultJson.put("data", json);
					ResponseUtils.renderJson(request, response, resultJson.toString());
					JedisUtils.set("homepageenterprise:"+enterpriseId, resultJson.toString(), 300);
				} catch (Exception e) {
					e.printStackTrace();
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "获取企业信息失败");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, new JSONObject().toString());
				}
			}
		}
		
	}
	@RequestMapping("/searchEnterpriseMsg.jspx")
	public void searchMsg(Integer enterpriseId,Integer certStatus,HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId+"");
			certStatus = certStatus==null?0:certStatus;
			map.put("certStatus", certStatus+"");
			String url =ConfigUtils.get("enterprise_function");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			System.out.println("企业ID："+enterpriseId);
			System.out.println("更新企业套餐："+postJson);
			resultJson.put("data", postJson);
			resultJson.put("code", 1);
			resultJson.put("msg", "企业信息获取成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultJson.put("code", 0);
			resultJson.put("msg", "企业信息获取失败");
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 获取结构信息
	 * 
	 * @param enterpriseId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "homepagestructure.jspx")
	public void homepagestructure(Integer enterpriseId, HttpServletResponse response, HttpServletRequest request) {
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterpriseById(enterpriseId);
		JSONObject resultJson = new JSONObject();
		if (iLiveEnterprise == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取结构信息失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} else {
			// 获取企业信息
			try {
				List<ILiveHomepageStructurePublish> list = contentMng.getPublishStructureByEnterprise(enterpriseId);
				JSONArray jsonArray = JSONArray.fromObject(list);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取结构信息成功");
				resultJson.put("data", jsonArray);
				ResponseUtils.renderJson(request, response, resultJson.toString());
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取结构信息失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, new JSONObject().toString());
			}
		}
	}

	/**
	 * 获取内容信息
	 * 
	 * @param enterpriseId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "homepagecontent.jspx")
	public void homepagecontent(Integer enterpriseId, HttpServletResponse response, HttpServletRequest request) {
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterpriseById(enterpriseId);
		JSONObject resultJson = new JSONObject();
		if (iLiveEnterprise == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取内容信息失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		} else {
			// 获取内容信息
			try {
				// 获取已发布的信息
				List<ContentSelectPublish> list = contentMng.getPublishContentByEnterprise(enterpriseId);
				// 获取显示内容结构信息
				ILiveHomepageStructurePublish structure4 = contentMng.getPublishStructureById(4, enterpriseId);
				ILiveHomepageStructurePublish structure6 = contentMng.getPublishStructureById(6, enterpriseId);
				int policy4;
				int policy6;
				int num4;
				int num6;
				int contenttype4;//1
				int contenttype6;//1
				// 如果为空 设置默认值
				if (structure4 == null) {
					num4 = 2;
					policy4 = 1;
					contenttype4 = 1;
				} else {
					if (structure4.getShowNum() == null) {
						num4 = 2;
					} else {
						num4 = structure4.getShowNum();
					}
					if (structure4.getPolicy() == null) {
						policy4 = 1;
					} else {
						policy4 = structure4.getPolicy();
					}
					if (structure4.getShowContentType() == null) {
						contenttype4 = 1;
					} else {
						contenttype4 = structure4.getShowContentType();
					}
				}
				if (structure6 == null) {
					num6 = 2;
					policy6 = 1;
					contenttype6 = 1;
				} else {
					if (structure6.getShowNum() == null) {
						num6 = 2;
					} else {
						num6 = structure6.getShowNum();
					}
					if (structure6.getPolicy() == null) {
						policy6 = 1;
					} else {
						policy6 = structure6.getPolicy();
					}
					if (structure6.getShowContentType() == null) {
						contenttype6 = 1;
					} else {
						contenttype6 = structure6.getShowContentType();
					}
				}
				int mark4 = 0, mark6 = 0;

				// 遍历已发布信息
				Iterator<ContentSelectPublish> iterator = list.iterator();
				while (iterator.hasNext()) {
					ContentSelectPublish contentSelect = iterator.next();
					// 如果是直播类型，设置直播间状态 设置直播地址
					if ((contentSelect.getStructureId() == 4 || contentSelect.getStructureId() == 6)
							&& contentSelect.getContentType() != null && contentSelect.getContentType() == 1) {
						// 获取直播间状态
						ILiveLiveRoom room = roomMng.findById(contentSelect.getContentId());
						contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
						// 获取观看地址
						ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
								.getAccessMethodBySeverGroupId(room.getServerGroupId());
						String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/live.html?roomId="
								+ room.getRoomId();
						contentSelect.setContentUrl(liveAddr);
					} else if ((contentSelect.getStructureId() == 4 || contentSelect.getStructureId() == 6)
							&& contentSelect.getContentType() != null && contentSelect.getContentType() == 2) {
						// 获取回看地址
						ILiveMediaFile file = mediaFileMng
								.selectILiveMediaFileByFileId((long) contentSelect.getContentId());
						// ILiveLiveRoom room =
						// roomMng.findById(file.getLiveRoomId());
						ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
								.getAccessMethodByMountId(file.getServerMountId());
						String mediavedioAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
								+ "/review.html?roomId=" + (file.getLiveRoomId() == null ? 0 : file.getLiveRoomId())
								+ "&fileId=" + file.getFileId();
						contentSelect.setContentUrl(mediavedioAddr);
					}else if ((contentSelect.getStructureId() == 4 || contentSelect.getStructureId() == 6)
							&& contentSelect.getContentType() != null && contentSelect.getContentType() == 3) {
						// 获取专题地址
						
						ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
								.getAccessMethodByMountId(100);
						String mediavedioAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/topic/topic.html?id="+contentSelect.getContentId();
						contentSelect.setContentUrl(mediavedioAddr);
					}

					// 设置结构数量，如果数量多于应显示的则把多于的删除掉
					if (contentSelect.getStructureId() == 4) {
						if (policy4 == 1) {
							mark4++;
							if (mark4 > num4) {
								iterator.remove();
							}
						} else if (policy4 == 2) {
							iterator.remove();
						}
					}
					if (contentSelect.getStructureId() == 6) {
						if (policy6 == 1) {
							mark6++;
							if (mark6 > num6) {
								iterator.remove();
							}
						} else if (policy6 == 2) {
							iterator.remove();
						}
					}
				}

				// 自动选择内容
				if (policy4 == 2) {
					List<ContentSelectPublish> listcontent = new LinkedList<>();
					// 直播间
					if (contenttype4 == 1) {
						List<ILiveLiveRoom> listroom4 = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId, num4);
						if (listroom4 != null && !listroom4.isEmpty()) {
							for (ILiveLiveRoom room : listroom4) {
								ContentSelectPublish contentSelect = new ContentSelectPublish();
								contentSelect.setContentId(room.getRoomId());
								contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
								contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
								contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
								contentSelect.setContentType(1);
								contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
								contentSelect.setStructureId(4);

								// 获取观看地址
								ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
										.getAccessMethodBySeverGroupId(room.getServerGroupId());
								String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
										+ "/live.html?roomId=" + room.getRoomId();
								contentSelect.setContentUrl(liveAddr);

								listcontent.add(contentSelect);
							}
						}

						// 回看
					} else if (contenttype4 == 2) {
						List<ILiveMediaFile> listfile = mediaFileMng.getListByType(1, enterpriseId);
						if (listfile != null && !listfile.isEmpty()) {
							for (ILiveMediaFile file : listfile) {
								ContentSelectPublish contentSelect = new ContentSelectPublish();
								contentSelect.setContentId(file.getFileId().intValue());
								contentSelect.setContentName(file.getMediaFileName());
								contentSelect.setContentImg(file.getFileCover());
								contentSelect.setContentBrief(file.getMediaFileDesc());
								contentSelect.setContentType(2);
								contentSelect.setStructureId(4);

								// 获取专题地址
								// ILiveLiveRoom room =
								// roomMng.findById(file.getLiveRoomId());
								ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
										.getAccessMethodByMountId(file.getServerMountId());
								String mediavedioAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
										+ "/review.html?roomId="
										+ (file.getLiveRoomId() == null ? 0 : file.getLiveRoomId()) + "&fileId="
										+ file.getFileId();
								contentSelect.setContentUrl(mediavedioAddr);
								listcontent.add(contentSelect);
							}
						}

						// 专题
					} else if (contenttype4 == 3) {
						// 专题列表
						List<ILiveThematic> listthematic = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
						if (listthematic != null && !listthematic.isEmpty()) {
							for (ILiveThematic thematic : listthematic) {
								ContentSelectPublish contentSelect = new ContentSelectPublish();
								contentSelect.setContentId(thematic.getThematicId().intValue());
								contentSelect.setContentName(thematic.getThematicName());
								contentSelect.setContentImg(thematic.getThematicImage());
								contentSelect.setContentBrief(thematic.getThematicDesc());
								contentSelect.setContentType(3);
								contentSelect.setStructureId(4);
								listcontent.add(contentSelect);
							}
						}

					}
					list.addAll(listcontent);
				}
				// 同上 判断另一个结构是否为自动选择的
				if (policy6 == 2) {
					List<ContentSelectPublish> listcontent = new LinkedList<>();
					if (contenttype6 == 1) {
						List<ILiveLiveRoom> listroom4 = roomMng.findRoomListPassByEnterpriseAndSize(enterpriseId, num4);
						if (listroom4 != null && !listroom4.isEmpty()) {
							for (ILiveLiveRoom room : listroom4) {
								ContentSelectPublish contentSelect = new ContentSelectPublish();
								contentSelect.setContentId(room.getRoomId());
								contentSelect.setContentName(room.getLiveEvent().getLiveTitle());
								contentSelect.setContentImg(room.getLiveEvent().getConverAddr());
								contentSelect.setContentBrief(room.getLiveEvent().getLiveDesc());
								contentSelect.setContentType(1);
								contentSelect.setLiveStatus(room.getLiveEvent().getLiveStatus());
								contentSelect.setStructureId(6);
								// 获取观看地址
								ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
										.getAccessMethodBySeverGroupId(room.getServerGroupId());
								String liveAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
										+ "/live.html?roomId=" + room.getRoomId();
								contentSelect.setContentUrl(liveAddr);

								listcontent.add(contentSelect);
							}
						}
					} else if (contenttype6 == 2) {
						List<ILiveMediaFile> listfile = mediaFileMng.getListByType(1, enterpriseId);
						if (listfile != null && !listfile.isEmpty()) {
							for (ILiveMediaFile file : listfile) {
								ContentSelectPublish contentSelect = new ContentSelectPublish();
								contentSelect.setContentId(file.getFileId().intValue());
								contentSelect.setContentName(file.getMediaFileName());
								contentSelect.setContentImg(file.getFileCover());
								contentSelect.setContentBrief(file.getMediaFileDesc());
								contentSelect.setContentType(2);
								contentSelect.setStructureId(6);
								// 获取专题地址
								ILiveServerAccessMethod accessMethodBySeverGroupId = accessMethodMng
										.getAccessMethodByMountId(file.getServerMountId());
								String mediavedioAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone"
										+ "/review.html?roomId="
										+ (file.getLiveRoomId() == null ? 0 : file.getLiveRoomId()) + "&fileId="
										+ file.getFileId();
								// ILiveLiveRoom room =
								// roomMng.findById(file.getLiveRoomId());
								// ILiveServerAccessMethod
								// accessMethodBySeverGroupId = accessMethodMng
								// .getAccessMethodBySeverGroupId(room.getServerGroupId());
								// String mediavedioAddr =
								// accessMethodBySeverGroupId.getH5HttpUrl() +
								// "/phone"
								// + "/review.html?roomId=" +
								// file.getLiveRoomId() + "&fileId=" +
								// file.getFileId();
								contentSelect.setContentUrl(mediavedioAddr);
								listcontent.add(contentSelect);
							}
						}

					} else if (contenttype6 == 3) {
						// 专题列表
						List<ILiveThematic> listthematic = iLiveThematicMng.getListByEnterpriseId(enterpriseId);
						if (listthematic != null && !listthematic.isEmpty()) {
							for (ILiveThematic thematic : listthematic) {
								ContentSelectPublish contentSelect = new ContentSelectPublish();
								contentSelect.setContentId(thematic.getThematicId().intValue());
								contentSelect.setContentName(thematic.getThematicName());
								contentSelect.setContentImg(thematic.getThematicImage());
								contentSelect.setContentBrief(thematic.getThematicDesc());
								contentSelect.setContentType(3);
								contentSelect.setStructureId(6);
								listcontent.add(contentSelect);
							}
						}
					}
					list.addAll(listcontent);
				}
				JSONArray jsonArray = JSONArray.fromObject(list);
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取内容信息成功");
				resultJson.put("data", jsonArray);
				ResponseUtils.renderJson(request, response, resultJson.toString());
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取内容信息失败");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, new JSONObject().toString());
			}
		}
	}

	/**
	 * 保存预约信息
	 * 
	 * @param userid
	 * @param roomId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "saveAppointment.jspx")
	public void saveAppointment(String userId, Integer roomId, String terminalType, HttpServletResponse response,
			HttpServletRequest request) {
		if (userId == null) {
			UserBean userBean = ILiveUtils.getAppUser(request);
			userId = userBean.getUserId();
		}
		JSONObject resultJson = new JSONObject();
		if (userId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
		} else {
			try {
				JedisUtils.del("isAppointment:"+roomId);
				ILiveLiveRoom iliveRoom=null;
				
				if(JedisUtils.exists(("roomInfo:"+roomId).getBytes())) {
					iliveRoom= SerializeUtil.getObjectRoom(roomId);
				}else {
					iliveRoom = roomMng.getIliveRoom(roomId);
					JedisUtils.setByte(("roomInfo:"+roomId).getBytes(), SerializeUtil.serialize(iliveRoom), 300);
				}
				ILiveEvent event = iliveRoom.getLiveEvent();
				ILiveAppointment temp = iLiveAppointmentMng.getAppointmentByUseridAndEventid(userId,
						event.getLiveEventId());
				if (temp == null) {
					ILiveAppointment saveILiveAppointment = iLiveAppointmentMng.saveILiveAppointment(userId, roomId);
					resultJson.put("code", SUCCESS_STATUS);
					if (TerminalUtil.checkApp(terminalType)) {
						String reservation_app = MessageCache.messageCache.get("reservation_app");
						if (!StringUtils.isEmpty(reservation_app)) {
							resultJson.put("message", reservation_app);
						} else {
							resultJson.put("message", "预约成功");
						}
					} else if (TerminalUtil.checkH5(terminalType)) {
						String reservation_h5 = MessageCache.messageCache.get("reservation_h5");
						if (!StringUtils.isEmpty(reservation_h5)) {
							resultJson.put("message", reservation_h5);
						} else {
							resultJson.put("message", "预约成功");
						}
					} else if (TerminalUtil.checkPc(terminalType)) {
						String reservation_pc = MessageCache.messageCache.get("reservation_pc");
						if (!StringUtils.isEmpty(reservation_pc)) {
							resultJson.put("message", reservation_pc);
						} else {
							resultJson.put("message", "预约成功");
						}
					} else {
						resultJson.put("message", "预约成功");
					}
					ILiveAppointment appointmentVo = new ILiveAppointment();
					BeanUtilsExt.copyProperties(appointmentVo, saveILiveAppointment);
					appointmentVo.setMark(0);
					resultJson.put("data", new JSONObject(saveILiveAppointment));
					//表示本次操作为预约
					resultJson.put("status", 1);
				} else {
					iLiveAppointmentMng.cancelAppointment(userId, roomId);
					ILiveAppointment appointmentVo = new ILiveAppointment();
					BeanUtilsExt.copyProperties(appointmentVo, temp);
					appointmentVo.setMark(1);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "取消预约成功");
					resultJson.put("data", new JSONObject(appointmentVo));
					//表示本次操作为取消预约
					resultJson.put("status", 2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "操作失败");
				resultJson.put("data", new JSONObject());
				resultJson.put("status", 0);
			}
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	}

	/**
	 * 获取是否预约
	 * 
	 * @param userid
	 * @param roomId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "isAppointment.jspx")
	public void isAppointment(String userId, Integer roomId, HttpServletResponse response, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		if (userId == null) {
			UserBean userBean = ILiveUtils.getAppUser(request);
			if(userBean==null){
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "用户不存在");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
			}else{
				userId = userBean.getUserId();
			}
		}

		
		if (userId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
		} else {
			try {
				if(JedisUtils.exists("isAppointment:"+roomId)) {
					
					ResponseUtils.renderJson(request, response, JedisUtils.get("isAppointment:"+roomId));
					return;
				}else {
					ILiveLiveRoom iliveRoom = roomMng.getIliveRoom(roomId);
					if (iliveRoom == null) {
						resultJson.put("code", SUCCESS_STATUS);
						resultJson.put("message", "直播间不存在");
						resultJson.put("data", new JSONObject());
						JedisUtils.set("isAppointment:"+roomId, resultJson.toString(), 300);
					} else {
						ILiveEvent event = iliveRoom.getLiveEvent();
						ILiveAppointment temp = iLiveAppointmentMng.getAppointmentByUseridAndEventid(userId,
								event.getLiveEventId());
						if (temp == null) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("isAppointment", 0);
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "查询成功");
							resultJson.put("data", jsonObject);
							JedisUtils.set("isAppointment:"+roomId, resultJson.toString(), 300);
						} else {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("isAppointment", 1);
							resultJson.put("code", SUCCESS_STATUS);
							resultJson.put("message", "查询成功");
							resultJson.put("data", jsonObject);
							JedisUtils.set("isAppointment:"+roomId, resultJson.toString(), 300);
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "操作失败");
				resultJson.put("data", new JSONObject());
			}
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
	}

	/**
	 * 获取预约房间列表
	 * @param userid
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getApponitmentList.jspx")
	public void getApponitmentList(String userId, Integer pageSize, Integer pageNo, HttpServletRequest request,
			HttpServletResponse response) {
		if (userId == null) {
			UserBean userBean = ILiveUtils.getAppUser(request);
			userId = userBean.getUserId();
		}
		JSONObject resultJson = new JSONObject();
		if (userId == null) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "用户不存在");
			resultJson.put("data", new JSONObject());
		} else {
			try {
				List<ILiveAppointment> list;
				if (pageSize != null && pageNo != null) {
					list = iLiveAppointmentMng.getAppointmentList(userId, pageNo, pageSize);
				} else {
					list = iLiveAppointmentMng.getAppointmentList(userId);
				}
				Iterator<ILiveAppointment> iterator = list.iterator();
				while (iterator.hasNext()) {
					ILiveAppointment appointment = iterator.next();
					ILiveEvent event = iLiveEventMng.selectILiveEventByID(appointment.getLiveEventId());
					if (event == null || event.getLiveStatus() == 3) {
						iterator.remove();
					}
				}
				List<JSONObject> listjson = new LinkedList<>();
				for (ILiveAppointment roomap : list) {
					ILiveLiveRoom room = roomMng.findByroomId(roomap.getRoomId());
					if (room != null) {
						JSONObject jsonObject = room.putPlayNewLiveInJson(room);
						listjson.add(jsonObject);
					}
				}
				resultJson.put("code", SUCCESS_STATUS);
				resultJson.put("message", "获取列表成功");
				resultJson.put("data", listjson);
			} catch (Exception e) {
				e.printStackTrace();
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "获取列表失败");
				resultJson.put("data", new JSONObject());
			}
			ResponseUtils.renderJson(request, response, resultJson.toString());
		}
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
	@RequestMapping(value = "/testsentitive.jspx")
	public void testsentitive(HttpServletRequest request, HttpServletResponse response, String text) {
		Set<String> sensitiveWord = mng.getSensitiveWord(text);
		Iterator<String> iterable = sensitiveWord.iterator();
		while (iterable.hasNext()) {
			System.out.println(iterable.next());
		}
	}
}
