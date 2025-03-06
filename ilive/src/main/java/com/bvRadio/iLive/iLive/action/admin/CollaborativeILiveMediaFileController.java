package com.bvRadio.iLive.iLive.action.admin;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.entity.*;
import com.bvRadio.iLive.iLive.manager.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.util.SafeTyChainPasswdUtil;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "/collaborative/mediafile")
public class CollaborativeILiveMediaFileController {

	private static final String HTTP_PROTOCAL = "http://";

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间

	@Autowired
	private ILiveMediaFileCommentsMng iLiveMediaFileCommentsMng;

	@Autowired
	private ILiveFileAuthenticationMng iLiveFileAuthenticationMng;

	@Autowired
	private ILiveFileWhiteBillMng iLiveFileWhiteBillMng;

	@Autowired
	private ILiveEnterpriseMemberMng iLiveEnterpriseMemberMng;
	@Autowired
	private ILiveMediaFileRelatedMng mediaFileRelatedMng;
	 @Autowired 
	    private ILiveSubRoomMng iLiveSubRoomMng;
	    @Autowired
		private ILiveSubLevelMng iLiveSubLevelMng;
	@Autowired
	private WorkLogMng workLogMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@RequestMapping("/entervedio")
	public String enterVedio(Integer createType, String mediaFileName, String liveRoomId, Integer pageNo,
			Integer pageSize, ModelMap map, HttpServletRequest request) {
		Map<String, Object> sqlParam = new HashMap<>();
		if (liveRoomId == null || liveRoomId.equals("")) {
			liveRoomId = "0";
		}
		UserBean user = ILiveUtils.getUser(request);
		sqlParam.put("mediaFileName", mediaFileName);
		Integer level = user.getLevel();
		level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
		//查询子账户是否有点播视频查看全部权限
		boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_recall);
		if(level.equals(ILiveManager.USER_LEVEL_SUB)&&!per){
			sqlParam.put("userId", user.getUserId());
		}else{
			sqlParam.put("enterpriseId", (long) user.getEnterpriseId());
		}
		createType = createType == null ? 5 : createType;
		sqlParam.put("createType", createType);
		sqlParam.put("delFlag", new Integer(0));
		//通过userid获取roomids
				String roomIds="";
				List<IliveSubRoom> roomFileList = iLiveSubRoomMng.selectILiveSubById(Long.parseLong(user.getUserId()));
				if(roomFileList.size()>0) {
					if(roomFileList.size()==1) {
						roomIds="("+roomFileList.get(0).getLiveRoomId()+")";
					}else if(roomFileList.size()>1){
					for(int i=0;i<roomFileList.size();i++) {
						if(i==0) {
							roomIds="("+roomFileList.get(0).getLiveRoomId()+",";
						}else if(i==roomFileList.size()-1) {
							roomIds=roomIds+roomFileList.get(roomFileList.size()-1).getLiveRoomId()+")";
						}else {
							roomIds=roomIds+roomFileList.get(i).getLiveRoomId()+",";
						}
					}
					}
				}
				
		sqlParam.put("roomIds", roomIds);
		Pagination pagination = iLiveMediaFileMng.getMediaFilePage(sqlParam, pageNo, new Integer(10));
		Hashtable<Integer, Hashtable<String, String>> enterpriseStrMap = EnterpriseCache.getEnterpriseStrMap();
		Hashtable<String, String> strMap = enterpriseStrMap.get(user.getEnterpriseId());
		double usedSpace = Long.parseLong(strMap.get(EnterpriseCache.ENTERPRISE_USE_Store) == null ? "0" : strMap.get(EnterpriseCache.ENTERPRISE_USE_Store))/1024/1024d;
		Long totalSpace = Long.parseLong(strMap.get(EnterpriseCache.ENTERPRISE_Store) == null ? "0" : strMap.get(EnterpriseCache.ENTERPRISE_Store))/1024/1024;
		List<Integer> enterpriseId = new ArrayList<Integer>();
		enterpriseId.add(user.getEnterpriseId());
		List<ILiveEnterprise> enterPriseByIds = iLiveEnterpriseMng.getEnterPriseByIds(enterpriseId);
		if(enterPriseByIds != null && enterPriseByIds.size() == 1 && enterPriseByIds.get(0).getOpenAreaStatus() != null){
			request.getSession().setAttribute("openAreaStatus",enterPriseByIds.get(0).getOpenAreaStatus() == 1 ? 1 : 0);
		}else{
			request.getSession().setAttribute("openAreaStatus",0);
		}
		map.addAttribute("liveMediaPage", pagination);
		map.addAttribute("mediaFileName", mediaFileName == null ? "" : mediaFileName);
		map.addAttribute("createType", createType);
        HttpSession session = request.getSession();
        session.setAttribute("remainderSpace", Double.valueOf(String.format("%.2f",totalSpace - usedSpace)));
        session.setAttribute("totalSpace", totalSpace);
//      map.addAttribute("remainderSpace", Double.valueOf(String.format("%.2f",totalSpace - usedSpace)));
//		map.addAttribute("totalSpace", totalSpace);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "1_1");
		return "filelib/video_mgr";
	}

	@RequestMapping("/updatemediaInfo")
	public String upateMediaInfo(Long id, String mediaFileName, String mediaFileDesc, String fileCover,
			HttpServletRequest request, Integer onlineFlag, HttpServletResponse response, ModelMap map) {
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(id);
		iLiveMediaFile.setFileCover(fileCover);
		iLiveMediaFile.setMediaFileName(mediaFileName);
		iLiveMediaFile.setMediaFileDesc(mediaFileDesc);
		iLiveMediaFile.setOnlineFlag(onlineFlag);
		iLiveMediaFileMng.updateMediaFile(iLiveMediaFile);
		return "redirect:enterfile/detail.do?fileId=" + id;
	}

	@RequestMapping("/entermediaedit.do")
	public String enterMediaEdit(Long id, String menuType, HttpServletRequest request, HttpServletResponse response,
			ModelMap map) {
		// menuType代表菜单类型， 1 视频管理 ,2 直播收录 ,3 文档管理,4 图片管理
		map.addAttribute("menuType", menuType);
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(id);
		if (iLiveMediaFile != null) {
			Integer serverMountId = iLiveMediaFile.getServerMountId();
			ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
			MountInfo mountInfo = serverAccess.getMountInfo();
			String allPath = HTTP_PROTOCAL + serverAccess.getHttp_address() + ":" + serverAccess.getUmsport()
					+ mountInfo.getBase_path() + iLiveMediaFile.getFilePath();
			Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
			allPath = String.format("%s?ut=%s&us=%s&sign=%s", allPath, generatorEncoderPwd.get("timestamp"),
					generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
			iLiveMediaFile.setFilePath(allPath);
			int roomId = iLiveMediaFile.getLiveRoomId();
			long fileId = iLiveMediaFile.getFileId();
			ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
			ILiveServerAccessMethod accessMethodBySeverGroupId = iLiveServerAccessMethodMng
					.getAccessMethodBySeverGroupId(iliveRoom.getServerGroupId());
			String mediavedioAddr = accessMethodBySeverGroupId.getH5HttpUrl() + "/phone" + "/review.html?roomId="
					+ roomId + "&fileId=" + fileId;
			map.addAttribute("mediavedioAddr", mediavedioAddr);
		}
		map.addAttribute("iLiveMediaFile", iLiveMediaFile);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "1_1");
		return "medialib/mediaedit";
	}

	@RequestMapping("/enterliverecord")
	public String enterLiveRecord(String mediaName, Integer pageNo, Integer pageSize, ModelMap map,
			HttpServletRequest request) {
		Map<String, Object> sqlParam = new HashMap<>();
		UserBean user = ILiveUtils.getUser(request);
		sqlParam.put("mediaFileName", mediaName);
		sqlParam.put("enterpriseId", (long) user.getEnterpriseId());
		sqlParam.put("createType", 0);
		sqlParam.put("delFlag", new Integer(0));
		Pagination pagination = iLiveMediaFileMng.getMediaFilePage(sqlParam, pageNo, new Integer(10));
		map.addAttribute("liveMediaPage", pagination);
		map.addAttribute("mediaFileName", mediaName == null ? "" : mediaName);
		map.addAttribute("createType", 0);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "1_1");
		return "filelib/record_mgr";
	}

	@RequestMapping("/enterdocument")
	public String enterDocument(String mediaName, Integer pageNo, Integer pageSize, ModelMap map,
			HttpServletRequest request) {
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("userId", ILiveUtils.getUser(request).getUserId());
		sqlParam.put("mediaFileName", mediaName);
		sqlParam.put("fileType", new Integer(2));
		sqlParam.put("delFlag", new Integer(1));
		Pagination pagination = iLiveMediaFileMng.getMediaFilePage(sqlParam, pageNo, new Integer(10));
		map.addAttribute("liveMediaPage", pagination);
		map.addAttribute("mediaName", mediaName);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "3_1");
		return "medialib/document";
	}

	@RequestMapping("/enterpicture")
	public String enterPicture(String mediaName, Integer pageNo, Integer pageSize, ModelMap map,
			HttpServletRequest request) {
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("userId", ILiveUtils.getUser(request).getUserId());
		sqlParam.put("mediaFileName", mediaName);
		sqlParam.put("fileType", new Integer(3));
		sqlParam.put("delFlag", new Integer(1));
		Pagination pagination = iLiveMediaFileMng.getMediaFilePage(sqlParam, pageNo, new Integer(10));
		map.addAttribute("pagination", pagination);
		map.addAttribute("mediaName", mediaName);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "4_1");
		return "medialib/picture";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/enterfile/detail.do")
	public String enterFileDetail(Long fileId, ModelMap map, HttpServletRequest request) {
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaForWeb(fileId);
		if (iLiveMediaFile != null) {
			map.addAttribute("menuType", iLiveMediaFile.getCreateType());
			Integer serverMountId = iLiveMediaFile.getServerMountId();
			ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
			MountInfo mountInfo = serverAccess.getMountInfo();
			String allPath = HTTP_PROTOCAL + serverAccess.getHttp_address() + ":" + serverAccess.getUmsport()
					+ mountInfo.getBase_path() + iLiveMediaFile.getFilePath();
			iLiveMediaFile.setFilePath(allPath);
			Integer roomId = iLiveMediaFile.getLiveRoomId();
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
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "1_1");
		return "filelib/video_detail";

	}

	@RequestMapping(value = "/enterfile/select.do")
	public String selectFile(Integer roomId, Long fileId, ModelMap map) {
		map.addAttribute("roomId", roomId);
		map.addAttribute("fileId", fileId);
		return "filelib/select";
	}

	@RequestMapping(value = "/upload.do")
	public String uploadFilePage(ModelMap map,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		map.put("userId", user.getUserId());
		return "filelib/upload";
	}
	/**
	 * 数据美化功能
	 * 
	 * @param roomId
	 * @param baseNum
	 * @param multiple
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "datadisplaybeautifyEditfile.do")
	public void iLiveConfigDatadisplayBeautifyEdit(Long fileId, Integer baseNum, Integer multiple,
			Integer openDataBeautifySwitch , HttpServletRequest request,HttpServletResponse response) {
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaForWeb(fileId);
		
		if (openDataBeautifySwitch == 1) {
			iLiveMediaFile.setBaseNum(baseNum);
			iLiveMediaFile.setMultiple(multiple);
			
			iLiveMediaFile.setOpenDataBeautifySwitch(1);
		} else {
			iLiveMediaFile.setOpenDataBeautifySwitch(0);
		}
		iLiveMediaFileMng.updateMediaFile(iLiveMediaFile);
		
		JSONObject resultJson = new JSONObject();
		resultJson.put("code", 1);
		resultJson.put("message", "设置成功");
		ResponseUtils.renderJson(request, response, resultJson.toString());
		
		//return "";
	}

}
