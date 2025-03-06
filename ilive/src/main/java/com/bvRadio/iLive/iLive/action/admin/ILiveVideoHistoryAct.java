package com.bvRadio.iLive.iLive.action.admin;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;
import com.bvRadio.iLive.iLive.entity.ILiveFileWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveHistoryVideo;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileRelated;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.MountInfo;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileWhiteBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileRelatedMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "history")
public class ILiveVideoHistoryAct {

	private static final String HTTP_PROTOCAL = "http://";

	@Autowired
	private ILiveVideoHistoryMng iLiveVideoHistoryMng;

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	ILiveServerAccessMethodMng accessMethodMng;

	@Autowired
	private WorkLogMng workLogMng;
	
	@Autowired
	private ILiveMediaFileCommentsMng iLiveMediaFileCommentsMng;
	
	@Autowired
	private ILiveFileAuthenticationMng iLiveFileAuthenticationMng;
	
	@Autowired
	private ILiveEnterpriseMemberMng iLiveEnterpriseMemberMng;
	
	@Autowired
	private ILiveFileWhiteBillMng iLiveFileWhiteBillMng;
	
	@Autowired
	private ILiveMediaFileRelatedMng mediaFileRelatedMng;
	
	@RequestMapping(value = "/video/list.do")
	public String gotoHistoryList(Integer roomId, Integer pageNo, Integer pageSize, HttpServletRequest request,
			ModelMap modelMap) {
		ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
		UserBean user = ILiveUtils.getUser(request);
		if (iliveRoom.getManagerId().equals(Long.parseLong(user.getUserId()))
				|| iliveRoom.getEnterpriseId().equals(user.getEnterpriseId())) {
			Pagination pagination = iLiveVideoHistoryMng.getHistoryList(roomId, pageNo == null ? 1 : pageNo,
					pageSize == null ? 0 : 15);
			modelMap.addAttribute("pagination", pagination);
			modelMap.addAttribute("totalPage", pagination.getTotalPage());
			modelMap.addAttribute("leftActive", "2_11");
			modelMap.addAttribute("topActive", "1");
			modelMap.addAttribute("iLiveLiveRoom", iliveRoom);
			return "videohistory/list";
		} else {
			return "nopower";
		}
	}

	@RequestMapping(value = "/video/detail.do")
	public String detail(Long fileId, Integer roomId, ModelMap map, HttpServletResponse response,HttpServletRequest request) {
		/*ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaForWeb(fileId);
		ILiveHistoryVideo viewHistoryByFileId = iLiveVideoHistoryMng.getViewHistoryByFileId(fileId, roomId);
		if (iLiveMediaFile != null && viewHistoryByFileId != null) {
			map.addAttribute("menuType", iLiveMediaFile.getCreateType());
			Integer serverMountId = iLiveMediaFile.getServerMountId();
			ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
			MountInfo mountInfo = serverAccess.getMountInfo();
			String allPath = HTTP_PROTOCAL + serverAccess.getHttp_address() + ":" + serverAccess.getUmsport()
					+ mountInfo.getBase_path() + iLiveMediaFile.getFilePath();
			iLiveMediaFile.setFilePath(allPath);
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			String mediavedioAddr = serverAccess.getH5HttpUrl() + "/phone" + "/review.html?roomId=" + roomId
					+ "&fileId=" + fileId;
			map.addAttribute("iLiveLiveRoom", iliveRoom);
			map.addAttribute("mediavedioAddr", mediavedioAddr);
		}
		map.addAttribute("iLiveMediaFile", iLiveMediaFile);
		map.addAttribute("leftActive", "5_1");
		map.addAttribute("topActive", "1");*/
		
		
		
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaForWeb(fileId);
		ILiveLiveRoom iliveRoom = null;
		if (iLiveMediaFile != null) {
			map.addAttribute("menuType", iLiveMediaFile.getCreateType());
			Integer serverMountId = iLiveMediaFile.getServerMountId();
			ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
			MountInfo mountInfo = serverAccess.getMountInfo();
			String allPath = HTTP_PROTOCAL + serverAccess.getHttp_address() + ":" + serverAccess.getUmsport()
					+ mountInfo.getBase_path() + iLiveMediaFile.getFilePath();
			iLiveMediaFile.setFilePath(allPath);
			//Integer roomId = iLiveMediaFile.getLiveRoomId();
			iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			String mediavedioAddr = serverAccess.getH5HttpUrl() + "/phone" + "/review.html?roomId="
					+ (roomId == null ? 0 : roomId) + "&fileId=" + fileId;
			map.addAttribute("mediavedioAddr", mediavedioAddr);
		}
		Pagination pagination = iLiveMediaFileCommentsMng.selectILiveMediaFileCommentsPage(fileId, 1, 20, null, null);
		List<ILiveMediaFileComments> list = (List<ILiveMediaFileComments>) pagination.getList();
		if (list != null) {
			for (ILiveMediaFileComments comment : list) {
				String comments = comment.getComments();
				String replaceKeyToImg = ExpressionUtil.replaceKeyToImg(comments == null ? "" : comments);
				comment.setComments(replaceKeyToImg);
			}
		}
		ILiveFileAuthentication iLiveFileAuthentication = iLiveFileAuthenticationMng
				.getFileAuthenticationByFileId(fileId);
		if (iLiveFileAuthentication == null) {
			iLiveFileAuthentication = new ILiveFileAuthentication();
			iLiveFileAuthentication.setAuthType(1);
		}
		Integer authType = iLiveFileAuthentication.getAuthType();
		if (authType == null) {
			authType = 1;
		}
		String viewPassword = iLiveFileAuthentication.getViewPassword();
		if (viewPassword == null) {
			viewPassword = "";
		}
		Double viewMoney = iLiveFileAuthentication.getViewMoney();
		String welcomeMsg = iLiveFileAuthentication.getWelcomeMsg();
		Integer openFCodeSwitch = iLiveFileAuthentication.getOpenFCodeSwitch();

		UserBean user = ILiveUtils.getUser(request);
		Pagination page = iLiveEnterpriseMemberMng.getPage(null, 1, 1000, user.getEnterpriseId());
		List<ILiveEnterpriseWhiteBill> enterprisewhiteList = (List<ILiveEnterpriseWhiteBill>) page.getList();
		List<ILiveFileWhiteBill> fileWhiteList = iLiveFileWhiteBillMng.getFileWhiteBills(fileId);

		List<ILiveMediaFileRelated> relatedFileList = mediaFileRelatedMng.listByParams(fileId);
		map.addAttribute("relatedFileList", relatedFileList);
		map.addAttribute("enterprisewhiteList", enterprisewhiteList);
		map.addAttribute("fileWhiteList", fileWhiteList);
		map.addAttribute("authType", authType);
		map.addAttribute("viewPassword", viewPassword);
		map.addAttribute("viewMoney", viewMoney);
		map.addAttribute("openFCodeSwitch", openFCodeSwitch);
		map.addAttribute("welcomeMsg", welcomeMsg);
		map.addAttribute("commentsList", list);
		map.addAttribute("iLiveMediaFile", iLiveMediaFile);
		map.addAttribute("commentPageTotal", pagination.getTotalPage());
		map.addAttribute("iLiveLiveRoom", iliveRoom);
		map.addAttribute("topActive", "1");
		map.addAttribute("leftActive", "5_1");
		return "videohistory/video_detail";
	}

	@RequestMapping(value = "/video/move.do")
	public void moveHistory(Integer direct, Integer roomId, Long historyId, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		if (direct == 1) {
			// 从大变小
			iLiveVideoHistoryMng.moveUp(historyId, roomId);
		} else if (direct == 0) {
			// 从小变大
			iLiveVideoHistoryMng.moveDown(historyId, roomId);
		}
		resultJson.put("code", 1);
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	@RequestMapping(value = "/video/delete.do")
	public void deleteHistory(Long historyId, HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		iLiveVideoHistoryMng.deleteHistory(historyId);
		
		UserBean user = ILiveUtils.getUser(request);
		workLogMng.save(new WorkLog(WorkLog.MODEL_REVIEW, historyId+"", "", WorkLog.MODEL_REVIEW_NAME_DEL, Long.parseLong(user.getUserId()), user.getUsername(), ""));
		resultJson.put("code", 1);
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	@RequestMapping(value = "/video/select.do")
	public String selectFile(Integer roomId, ModelMap map) {
		map.addAttribute("roomId", roomId);
		return "videohistory/select";
	}

	@RequestMapping(value = "/video/batchadd.do")
	public void batchAddHistory(Integer roomId, @RequestParam(value = "ids[]") Long[] ids, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean user = ILiveUtils.getUser(request);
        if(JedisUtils.exists("recordlist:"+roomId)) {
        	JedisUtils.del("recordlist:"+roomId);
		}
		JSONObject resultJson = new JSONObject();
		try {
			boolean ret = iLiveVideoHistoryMng.batchAddHistory(roomId, ids, Long.parseLong(user.getUserId()));
			if (ret) {
				resultJson.put("code", 1);
				workLogMng.save(new WorkLog(WorkLog.MODEL_REVIEW, roomId+"", ids.toString(), WorkLog.MODEL_REVIEW_NAME_ADD, Long.parseLong(user.getUserId()), user.getUsername(), ""));
				ResponseUtils.renderJson(response, resultJson.toString());
			} else {
				resultJson.put("code", 0);
				ResponseUtils.renderJson(response, resultJson.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 0);
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}
	@RequestMapping(value = "/video/test.do")
	public void test(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ExecutorService executorService = Executors.newCachedThreadPool();
			for(int i=0;i<100;i++) {
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						iLiveVideoHistoryMng.test();
					}
				});
			}
			resultJson.put("code", 1);
			ResponseUtils.renderJson(response, resultJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 0);
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}

}
