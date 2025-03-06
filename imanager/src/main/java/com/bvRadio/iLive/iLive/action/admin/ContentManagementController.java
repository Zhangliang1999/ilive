package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;

/**
 * 内容管理  栏目
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="content")
public class ContentManagementController {
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;//直播间
	
	@Autowired
	private ILiveEventMng iLiveEventMng;//场次
	
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;//场次文件
	/**
	 * 场次管理
	 * @param pageNo 页码
	 * @param roomId 直播间ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/events.do")
	public  String contentEventList(Integer pageNo,Integer roomId,ModelMap model){
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		Long liveEventId = iliveRoom.getLiveEvent().getLiveEventId();
		Pagination pagination= iLiveEventMng.selectILiveEventPage(roomId,liveEventId,cpn(pageNo), 8);
		model.addAttribute("pagination", pagination);
		model.addAttribute("iLiveLiveRoom", iliveRoom);
		model.addAttribute("fileNumber", 1);//节目录制
		model.addAttribute("fileSize", 10);//存储空间
		model.addAttribute("leftActive", "5_1");
		model.addAttribute("topActive", "1");
		return "livecontent/event_list";
	}
	/**
	 * 根据场次ID删除场次
	 * @param liveEventId
	 * @param roomId
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value="/delete.do")
	public String deleteEventList(Long liveEventId,Integer roomId,Integer pageNo){
		iLiveEventMng.updateILiveEventByIsDelete(liveEventId,true);
		return "redirect:/admin/content/events.do?roomId="+roomId+"&pageNo="+pageNo;
	}
//进入场次=======================================================================================================	
	/**
	 * 直播场次管理
	 * @param pageNo 页码
	 * @param roomId 直播间ID
	 * @param liveEventId 场次
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/events/file.do")
	public  String contentEventFileList(Integer pageNo,Integer roomId,Long liveEventId,ModelMap model){
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		Pagination pagination= iLiveMediaFileMng.selectILiveMediaFilePage(liveEventId,cpn(pageNo), 8,0,1);
		ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
		model.addAttribute("pagination", pagination);
		model.addAttribute("iLiveLiveRoom", iliveRoom);
		model.addAttribute("iLiveEvent", iLiveEvent);
		model.addAttribute("leftActive", "5_1");
		model.addAttribute("topActive", "1");
		return "livecontent/event_content_list";
	}
	/**
	 * 根据文件ID删除数据信息
	 * @param fileId 文件ID
	 * @param liveEventId 场次ID
	 * @param roomId 直播间ID
	 * @param pageNo 页码
	 * @return
	 */
	@RequestMapping(value="/events/delete.do")
	public String deleteEventFileList(Long fileId,Long liveEventId,Integer roomId,Integer pageNo){
		iLiveMediaFileMng.deleteVedio(fileId);
		return "redirect:/admin/content/events/file.do?roomId="+roomId+"&pageNo="+pageNo+"&liveEventId="+liveEventId;
	}
	
	/**
	 * 修改上线状态
	 * @param fileId 文件ID
	 * @param onlineFlag 上线状态
	 * @param response
	 */
	@RequestMapping(value="/events/online.do")
	public void updateEventFileOnlineFlag(Long fileId,Integer onlineFlag,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMediaFile file = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
			file.setOnlineFlag(onlineFlag);
			iLiveMediaFileMng.updateEventFileOnlineFlag(file);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 编辑视频详情
	 * @param roomId 直播间ID
	 * @param fileId 文件ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/events/details.do")
	public  String contentEventFileList(Integer roomId,Long fileId,ModelMap model){
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		model.addAttribute("iLiveLiveRoom", iliveRoom);
		model.addAttribute("iLiveMediaFile", iLiveMediaFile);
		model.addAttribute("leftActive", "5_1");
		model.addAttribute("topActive", "1");
		return "livecontent/file_details";
	}
	/**
	 * 修改信息
	 * @param fileId 文件ID 
	 * @param mediaFileName 媒资名称
	 * @param mediaFileDesc 媒资描述
	 * @param fileCover 封面图
	 * @param response
	 */
	@RequestMapping(value="/events/update.do")
	public void updateFile(Long fileId,String mediaFileName,String mediaFileDesc,String fileCover,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMediaFile file = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
			file.setMediaFileName(mediaFileName);
			file.setMediaFileDesc(mediaFileDesc);
			file.setFileCover(fileCover);
			iLiveMediaFileMng.updateEventFileOnlineFlag(file);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
//修改场次权限============================================================================================================================================	
	/**
	 * 免费观看权限
	 * @param liveEventId 场次ID
	 * @param viewAuthorized 权限值
	 * @param response
	 */
	@RequestMapping(value="/events/permissions/free.do")
	public void updateILiveEventPermissionsFree(Long liveEventId,Integer viewAuthorized,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
			iLiveEvent.setViewAuthorized(viewAuthorized);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	/**
	 * 密码观看
	 * @param liveEventId 场次ID
	 * @param viewAuthorized 权限值
	 * @param viewPassword 观看密码
	 * @param welcomeMsg 欢迎语
	 * @param response
	 */
	@RequestMapping(value="/events/permissions/password.do")
	public void updateILiveEventPermissionsPassword(Long liveEventId,Integer viewAuthorized,String viewPassword,String welcomeMsg,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		try {
			ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(liveEventId);
			iLiveEvent.setViewAuthorized(viewAuthorized);
			iLiveEvent.setViewPassword(viewPassword);
			iLiveEvent.setWelcomeMsg(welcomeMsg);
			iLiveEventMng.updateILiveEvent(iLiveEvent);
			resultJson.put("status", SUCCESS_STATUS);
			resultJson.put("message", "成功");
		} catch (Exception e) {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("message", "失败");
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
//精品图============================================================================================================================	
	/**
	 * 根据直播间ID 获取直播间图片
	 * @param pageNo 页码
	 * @param roomId 直播间ID
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/image/list.do")
	public  String contentImageFileList(Integer pageNo,Integer roomId,ModelMap model){
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		Pagination pagination= iLiveMediaFileMng.selectILiveMediaFilePageByRoomId(roomId,cpn(pageNo), 8,0,3);
		List<ILiveMediaFile> list = (List<ILiveMediaFile>) pagination.getList();
		for (ILiveMediaFile iLiveMediaFile : list) {
			String fileCover = iLiveMediaFile.getFileCover();
			if(fileCover!=null){
				String imageType = fileCover.substring(fileCover.lastIndexOf(".") + 1);
					iLiveMediaFile.setImageType(imageType);
			}
		}
		model.addAttribute("pagination", pagination);
		model.addAttribute("iLiveLiveRoom", iliveRoom);
		model.addAttribute("leftActive", "5_2");
		model.addAttribute("topActive", "1");
		return "livecontent/image_list";
	}
	/**
	 * 删除图片
	 * @param fileId 文件ID
	 * @param pageNo 页码
	 * @param roomId 直播间ID
	 * @return
	 */
	@RequestMapping(value="/image/delete.do")
	public  String deleteContentImageFile(Long fileId,Integer pageNo,Integer roomId){
		iLiveMediaFileMng.deleteVedio(fileId);
		return "redirect:/admin/content/image/list.do?roomId="+roomId+"&pageNo="+pageNo;
	}
	
	
}
