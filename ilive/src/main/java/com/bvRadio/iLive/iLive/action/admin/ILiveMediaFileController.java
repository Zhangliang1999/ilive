package com.bvRadio.iLive.iLive.action.admin;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.ILiveConvertTask;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;
import com.bvRadio.iLive.iLive.entity.ILiveFileDoc;
import com.bvRadio.iLive.iLive.entity.ILiveFileWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileRelated;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileShareConfig;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;
import com.bvRadio.iLive.iLive.entity.MountInfo;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.entity.vo.ILiveShareInfoVo;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveConvertTaskMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMemberMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFCodeMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationRecordMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileDocMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileWhiteBillMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileRelatedMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileShareConfigMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubRoomMng;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.SafeTyChainPasswdUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Controller
@RequestMapping(value = "mediafile")
public class ILiveMediaFileController {

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
    private ILiveFileAuthenticationRecordMng iLiveFileAuthenticationRecordMng;
	@Autowired
	private ILiveFileWhiteBillMng iLiveFileWhiteBillMng;

	@Autowired
	private ILiveEnterpriseMemberMng iLiveEnterpriseMemberMng;
	@Autowired
	private ILiveMediaFileRelatedMng mediaFileRelatedMng;
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveMediaFileShareConfigMng iLiveMediaFileShareConfigMng;
	@Autowired
	private ILiveConvertTaskMng iLiveConvertTaskMng;
	@Autowired
	private WorkLogMng workLogMng;
    @Autowired 
    private ILiveSubRoomMng iLiveSubRoomMng;
    @Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
    @Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;
    @Autowired
	private ILiveFileDocMng fileDocMng;
    @Autowired
	private ILiveFCodeMng iLiveFCodeMng;
    @Autowired
	private DocumentManagerMng documentManage;	//文档库
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
		map.addAttribute("liveMediaPage", pagination);
		map.addAttribute("totalPage", pagination.getTotalPage());
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(user.getEnterpriseId());
		Integer certStatus = iLiveEnterprise.getCertStatus();
		boolean b = EnterpriseUtil.selectIfEn(user.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_stripRemoval,certStatus);
		if(b){
			map.addAttribute("enterpriseType", 0);
			map.addAttribute("enterpriseContent", EnterpriseCache.ENTERPRISE_FUNCTION_stripRemoval_Content);
		}else{
			map.addAttribute("enterpriseType", 1);
			map.addAttribute("enterpriseContent", "");
		}
//		boolean b1 = EnterpriseUtil.selectIfEn(user.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_CorporateHome,certStatus);
//		boolean b2 = EnterpriseUtil.selectIfEn(user.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_CorporateHomePublicnumber,certStatus);
//		if(b1&&b2){
//			map.addAttribute("CorporateHome", 0);
//		}else{
//			map.addAttribute("CorporateHome", 1);
//		}
//		boolean b3 = EnterpriseUtil.selectIfEn(user.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_TopicPage,certStatus);
//		boolean b4 = EnterpriseUtil.selectIfEn(user.getEnterpriseId(), EnterpriseCache.ENTERPRISE_FUNCTION_TopicPagePublicnumber,certStatus);
//		if(b3&&b4){
//			map.addAttribute("TopicPage", 0);
//		}else{
//			map.addAttribute("TopicPage", 1);
//		}
//		if(b1&&b2&&b3&&b4) {
//			map.addAttribute("CorporateTopicPage", 0);
//		}else {
//			map.addAttribute("CorporateTopicPage", 1);
//		}
		map.addAttribute("mediaFileName", mediaFileName == null ? "" : mediaFileName);
		map.addAttribute("createType", createType);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "1_1");
		return "filelib/video_mgr";
	}

	@RequestMapping("/delete")
	public void deleteVedio(Long id, HttpServletResponse response) {
		iLiveMediaFileMng.deleteVedio(id);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
	}
	@RequestMapping("/search")
	public void searchMsg(HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		Integer enterpriseId=user.getEnterpriseId();
		Integer certStatus=user.getCertStatus();
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/rest/enterprisefile.do")
	public void getAppMediaFile(Integer createType, String mediaFileName, String liveRoomId,String search, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		Map<String, Object> sqlParam = new HashMap<>();
		if (liveRoomId == null || liveRoomId.equals("")) {
			liveRoomId = "0";
		}
		UserBean user = ILiveUtils.getUser(request);
		sqlParam.put("mediaFileName", mediaFileName);
		Integer level = user.getLevel();
		level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
//		Long userid=null;
		if(level.equals(ILiveManager.USER_LEVEL_SUB)){
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
		Pagination pagination = iLiveMediaFileMng.getAppMediaFile(sqlParam, pageNo, new Integer(10));
		List<AppMediaFile> mediaFileList = (List<AppMediaFile>) pagination.getList();
		for (AppMediaFile appMediaFile : mediaFileList) {
			
			ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
					.getFileAuthenticationByFileId(appMediaFile.getFileId());
			if (fileAuth == null) {
				appMediaFile.setAuthType(1);
			} else {
				appMediaFile.setAuthType(fileAuth.getAuthType() == null ? 1 : fileAuth.getAuthType());
			}
			
		}
		resultJson.put("data", mediaFileList);
		resultJson.put("pageSize", pagination.getPageSize());
		resultJson.put("totalPage", pagination.getTotalPage());
		ResponseUtils.renderJson(response, resultJson.toString());

	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/rest/enterprisefile1.do")
	public void getAppMediaFile1(Integer createType, String mediaFileName,Long fileId, String liveRoomId,String search, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		Map<String, Object> sqlParam = new HashMap<>();
		if (liveRoomId == null || liveRoomId.equals("")) {
			liveRoomId = "0";
		}
		UserBean user = ILiveUtils.getUser(request);
		sqlParam.put("mediaFileName", mediaFileName);
		sqlParam.put("fileId", fileId);
		Integer level = user.getLevel();
		level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
//		Long userid=null;
		if(level.equals(ILiveManager.USER_LEVEL_SUB)){
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
		Pagination pagination = iLiveMediaFileMng.getAppMediaFile(sqlParam, pageNo, new Integer(10));
		List<AppMediaFile> mediaFileList = (List<AppMediaFile>) pagination.getList();
		for (AppMediaFile appMediaFile : mediaFileList) {
			
			ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
					.getFileAuthenticationByFileId(appMediaFile.getFileId());
			if (fileAuth == null) {
				appMediaFile.setAuthType(1);
			} else {
				appMediaFile.setAuthType(fileAuth.getAuthType() == null ? 1 : fileAuth.getAuthType());
			}
			
		}
		resultJson.put("data", mediaFileList);
		resultJson.put("pageSize", pagination.getPageSize());
		resultJson.put("totalPage", pagination.getTotalPage());
		ResponseUtils.renderJson(response, resultJson.toString());

	}
	/**
	 * 群体删除
	 * 
	 * @param ids
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping("/deletes.do")
	public void deleteVedios(@RequestParam(value = "ids[]") Long[] ids, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		try {
			ILiveMediaFile[] mediaFiles = iLiveMediaFileMng.deleteVediosByIds(ids);
			if (null != mediaFiles) {
				for (ILiveMediaFile iLiveMediaFile : mediaFiles) {
					if (null != iLiveMediaFile) {
						Long enterpriseId = iLiveMediaFile.getEnterpriseId();
						Long fileSize = 0L-iLiveMediaFile.getFileSize();
						EnterpriseUtil.openEnterprise(enterpriseId.intValue(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,
								fileSize + "", null);
					}
				}
			}
			UserBean user = ILiveUtils.getUser(request);
			StringBuilder sBuilder = new StringBuilder();
			for(int i=0;i<ids.length;i++) {
				if (i==0) {
					sBuilder.append(ids[i]);
				}else {
					sBuilder.append(","+ids[i]);
				}
			}
			workLogMng.save(new WorkLog(WorkLog.MODEL_DELVIDEO, sBuilder.toString(), "", WorkLog.MODEL_DELVIDEO_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
			ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
		} catch (Exception e) {
			ResponseUtils.renderJson(response, "{\"status\":\"error\"}");
		}
	}
	/**
	 * 恢复永久
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping("/recover.do")
	public void recoverVedio(Long id, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		try {
			ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(id);
			if (null != mediaFile) {
				if(ILiveMediaFile.MEDIA_TYPE_Temporary.equals(mediaFile.getIsMediaType())) {
					mediaFile.setIsMediaType(ILiveMediaFile.MEDIA_TYPE_Store);
					iLiveMediaFileMng.updateMediaFile(mediaFile);
				}
				//永久恢复的时候占用存储
				Long enterpriseId = mediaFile.getEnterpriseId();
				Long fileSize = mediaFile.getFileSize();
				EnterpriseUtil.openEnterprise(enterpriseId.intValue(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,
						fileSize + "", null);
			}
			ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
		} catch (Exception e) {
			ResponseUtils.renderJson(response, "{\"status\":\"error\"}");
		}
	}

	/**
	 * 集体上线或下线
	 * 
	 * @param ids
	 * @param state
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping("/updatelinestate.do")
	public void upateVediosLineState(@RequestParam(value = "ids[]") Long[] ids, Integer state,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		iLiveMediaFileMng.updateVediosLineStateByIds(ids, state);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
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
		//2019-6-3 新加需求改动
		//如果分享表中有相应的数据，修改相关信息
		List<ILiveMediaFileShareConfig> shareConfigList = iLiveMediaFileShareConfigMng.getShareConfig(id.intValue());
		if(!shareConfigList.isEmpty()){
			for(ILiveMediaFileShareConfig share: shareConfigList){
				if(share.getIfEdit()==null||share.getIfEdit()==0){
					if(share.getShareType().equals(ILiveMediaFileShareConfig.FRIEND_CIRCLE)){
						share.setMediaFileDesc(mediaFileDesc);
						share.setMediaFileName(mediaFileName);
						share.setShareImg(fileCover);
						iLiveMediaFileShareConfigMng.updateShare(share);
					}else if(share.getShareType().equals(ILiveMediaFileShareConfig.FRIEND_SINGLE)){
						share.setMediaFileDesc(mediaFileDesc);
						share.setMediaFileName(mediaFileName);
						share.setShareImg(fileCover);
						iLiveMediaFileShareConfigMng.updateShare(share);
					}
				}
			}
		}
		return "redirect:enterfile/detail.do?fileId=" + id;
	}
	@RequestMapping("/updatemediafilePath")
	public void updatemediafilePath(Long id, String mediaFilePath, 
			HttpServletRequest request,  HttpServletResponse response) {
		JSONObject json = new JSONObject();
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(id);
		try {
			iLiveMediaFile.setFilePath(mediaFilePath);
			iLiveMediaFileMng.updateMediaFile(iLiveMediaFile);
			//上传文件以后调用转码服务进行转码
			String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
			ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
					.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
			ILiveConvertTask iLiveConvertTask = iLiveConvertTaskMng.createConvertTask(iLiveMediaFile, accessMethod);
			iLiveConvertTaskMng.addConvertTask(iLiveConvertTask);
			json.put("code", 1);
			
		} catch (Exception e) {
			json.put("code", 0);
			e.printStackTrace();
		}
		ResponseUtils.renderJson(request,response, json.toString());
	}
	@RequestMapping(value = "infochange.do")
	public void iLiveConfigSpreadShareUpdate(ILiveShareInfoVo iLiveShareInfoVo,Integer fileId, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveMediaFileShareConfigMng.updateConfigShare(iLiveShareInfoVo);
			List<ILiveMediaFileShareConfig> list=iLiveMediaFileShareConfigMng.getShareConfig(fileId);
			for(ILiveMediaFileShareConfig share:list){
				share.setIfEdit(1);
				iLiveMediaFileShareConfigMng.updateShare(share);
			}
			resultJson.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", -1);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
	
	@RequestMapping(value = "/share/updatestatus")
	public void updateShareState(Integer fileId, Integer openStatus, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		List<ILiveMediaFileShareConfig> shareConfigs = iLiveMediaFileShareConfigMng.getShareConfig(fileId);
		Map<String, Object> map = new HashMap<>();
		try {
			if (shareConfigs != null && !shareConfigs.isEmpty()) {
				for (ILiveMediaFileShareConfig share : shareConfigs) {
					share.setOpenStatus(openStatus);
					iLiveMediaFileShareConfigMng.updateShare(share);
				}
			}
			resultJson.put("code", 1);
		} catch (Exception e) {
			resultJson.put("code", 0);
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, resultJson.toString());
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
		Integer level = user.getLevel();
		level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
		if(level.equals(ILiveManager.USER_LEVEL_SUB)){
			sqlParam.put("userId", user.getUserId());
		}else{
			sqlParam.put("enterpriseId", (long) user.getEnterpriseId());
		}
		sqlParam.put("createType", 0);
		sqlParam.put("delFlag", new Integer(0));
		Pagination pagination = iLiveMediaFileMng.getMediaFilePage(sqlParam, pageNo, new Integer(10));
		map.addAttribute("liveMediaPage", pagination);
		map.addAttribute("totalPage", pagination.getTotalPage());
		map.addAttribute("mediaFileName", mediaName == null ? "" : mediaName);
		map.addAttribute("createType", 0);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "2_1");
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
	public String enterFileDetail(Long fileId, ModelMap map, HttpServletRequest request,Integer creatType,String searchCode) {
		UserBean user = ILiveUtils.getUser(request);
		ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaForWeb(fileId);
		String mediavedioAddr="";
		if (iLiveMediaFile != null) {
			if(!iLiveMediaFile.getEnterpriseId().equals(user.getEnterpriseId().longValue())) {
				return "redirect:/admin/login.do";
			}
			map.addAttribute("menuType", iLiveMediaFile.getCreateType());
			Integer serverMountId = iLiveMediaFile.getServerMountId();
			ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
			MountInfo mountInfo = serverAccess.getMountInfo();
			String allPath = HTTP_PROTOCAL + serverAccess.getHttp_address() + ":" + serverAccess.getUmsport()
					+ mountInfo.getBase_path() + iLiveMediaFile.getFilePath();
			iLiveMediaFile.setFilePath(allPath);
			Integer roomId = iLiveMediaFile.getLiveRoomId();
			mediavedioAddr = serverAccess.getH5HttpUrl() + "/phone" + "/review.html?roomId="
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
		Integer needLogin = iLiveFileAuthentication.getNeedLogin();
		if (needLogin == null) {
			needLogin = 0;
		}
		Double viewMoney = iLiveFileAuthentication.getViewMoney();
		String welcomeMsg = iLiveFileAuthentication.getWelcomeMsg();
		Integer openFCodeSwitch = iLiveFileAuthentication.getOpenFCodeSwitch();
        String outLinkUrl=iLiveFileAuthentication.getOutLinkUrl();
		Integer fopenStatus=iLiveFileAuthentication.getFopenStatus();
		Pagination page = iLiveEnterpriseMemberMng.getPage(null, 1, 1000, user.getEnterpriseId());
		List<ILiveEnterpriseWhiteBill> enterprisewhiteList = (List<ILiveEnterpriseWhiteBill>) page.getList();
		List<ILiveFileWhiteBill> fileWhiteList = iLiveFileWhiteBillMng.getFileWhiteBills(fileId);

		List<ILiveMediaFileRelated> relatedFileList = mediaFileRelatedMng.listByParams(fileId);
		List<ILiveFileDoc> fileDocList =fileDocMng.getListById(fileId);
		map.addAttribute("fileDocList", fileDocList);
		map.addAttribute("relatedFileList", relatedFileList);
		map.addAttribute("enterprisewhiteList", enterprisewhiteList);
		map.addAttribute("fileWhiteList", fileWhiteList);
		map.addAttribute("authType", authType);
		map.addAttribute("needLogin", needLogin);
		map.addAttribute("viewPassword", viewPassword);
		map.addAttribute("viewMoney", viewMoney);
		map.addAttribute("openFCodeSwitch", openFCodeSwitch);
		map.addAttribute("welcomeMsg", welcomeMsg);
		map.addAttribute("outLinkUrl", outLinkUrl);
		map.addAttribute("fopenStatus", fopenStatus);
		map.addAttribute("commentsList", list);
		map.addAttribute("iLiveMediaFile", iLiveMediaFile);
		map.addAttribute("commentPageTotal", pagination.getTotalPage());
		//新加入点播分享的内容
		List<ILiveMediaFileShareConfig> shareConfigList = iLiveMediaFileShareConfigMng.getShareConfig(fileId.intValue());
		if (shareConfigList == null || shareConfigList.isEmpty()) {
			ILiveMediaFileShareConfig shareConfigBean = new ILiveMediaFileShareConfig();
			shareConfigBean.setMediaFileDesc(iLiveMediaFile.getMediaFileDesc());
			shareConfigBean.setMediaFileName(iLiveMediaFile.getMediaFileName());
			shareConfigBean.setFileId(fileId.intValue());
			shareConfigBean.setShareUrl(mediavedioAddr);
			Integer enterpriseId = user.getEnterpriseId();
			ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
			String enterpriseImg = iLiveEnterPrise.getEnterpriseImg();
			if (enterpriseImg == null) {
				shareConfigBean.setShareImg(ConfigUtils.get("shareOtherConfig"));
			} else {
				shareConfigBean.setShareImg(iLiveMediaFile.getFileCover());
			}
			shareConfigList = iLiveMediaFileShareConfigMng.addIliveMediaFileShareConfig(shareConfigBean);
		}
		Integer openStatus = 0;
		if (!shareConfigList.isEmpty()) {
			for (ILiveMediaFileShareConfig share : shareConfigList) {
				openStatus = share.getOpenStatus();
				if (share.getShareType().equals(ILiveMediaFileShareConfig.FRIEND_SINGLE)) {
					// 朋友
					map.addAttribute("friendSingle", share);
				} else if (share.getShareType().equals(ILiveMediaFileShareConfig.FRIEND_CIRCLE)) {
					// 朋友圈
					map.addAttribute("friendCircle", share);
					
				}
			}
		}
		//新加入F码观看授权的相关东西
		//未使用
				List<ILiveFCode> unuselist = iLiveFCodeMng.listByParams(null, null, 1, null, fileId, null, null);
				map.addAttribute("unUseFCodeNum", unuselist.size()>0?unuselist.size():0);
				//已绑定
				List<ILiveFCode> useinglist = iLiveFCodeMng.listByParams(null, null, 2, null, fileId, null, null);
				map.addAttribute("useingFCodeNum", useinglist.size()>0?useinglist.size():0);
				//已失效
				List<ILiveFCode> outuselist = iLiveFCodeMng.listByParams(null, null, 3, null, fileId, null, null);
				map.addAttribute("outUseFCodeNum", outuselist.size()>0?outuselist.size():0);
				//全部
				List<ILiveFCode> totallist = iLiveFCodeMng.listByParams(null, null, null, null, fileId, null, null);
				map.addAttribute("totalFCodeNum", totallist.size()>0?totallist.size():0);
				//f码数据列表
				Integer type=1;
				if(creatType!=null){
					if(creatType==5){
						creatType=null;
					}
				}
				if("".equals(searchCode)&&searchCode!=null){
					searchCode=null;
				}
		Pagination pagination1 = iLiveFCodeMng.getBeanBySearchCode(null, fileId,type,creatType,searchCode,1, 20);
		//Pagination pagination = iLiveFCodeMng.getBeanByCode(roomId, null,1,1, 20);
		map.addAttribute("pager", pagination1);
		//添加是否开启开发者权限
		ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(user.getEnterpriseId());
		map.addAttribute("isDeveloper", iLiveEnterprise.getIsDeveloper()==null?0:iLiveEnterprise.getIsDeveloper());
		map.addAttribute("openStatus", openStatus);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "1_1");
		return "filelib/video_detail";

	}
	
	@RequestMapping(value = "/enterfile/vrPlayerXml.do")
	public String vrPlayerXml(Long fileId, ModelMap map, HttpServletRequest request) {
//		UserBean user = ILiveUtils.getUser(request);
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
		map.addAttribute("iLiveMediaFile", iLiveMediaFile);
		return "filelib/vrPlayerXml";
	}

	@RequestMapping(value = "/enterfile/select.do")
	public String selectFile(Integer roomId, Long fileId, ModelMap map) {
		map.addAttribute("roomId", roomId);
		map.addAttribute("fileId", fileId);
		return "filelib/select";
	}

	@RequestMapping(value = "/enterfile/saveRelatedMediaFile.do")
	public void saveRelatedMediaFile(Long fileId, @RequestParam(value = "ids[]") Long[] ids, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			List<ILiveMediaFileRelated> list = mediaFileRelatedMng.save(fileId, ids);
			if (null != list) {
				List<ILiveMediaFile> rList=new ArrayList<ILiveMediaFile>();
				for (ILiveMediaFileRelated iLiveMediaFileRelated : list) {
					rList.add(iLiveMediaFileRelated.getRelatedMediaFile());
					iLiveMediaFileRelated.setRelatedMediaFile(null);
				}
				resultJson.put("code", 1);
				resultJson.put("data", JSONArray.fromObject(list));
				for (ILiveMediaFile iLiveMediaFile : rList) {
					iLiveMediaFile.setiLiveMediaFileCommentsSet(null);
				}
				resultJson.put("data1", JSONArray.fromObject(rList));
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

	@RequestMapping(value = "/enterfile/updateRelatedMediaFileOrder.do")
	public void updateRelatedMediaFileOrder(String id, Double orderNum, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveMediaFileRelated mediaFileRelated = new ILiveMediaFileRelated();
			mediaFileRelated.setId(id);
			mediaFileRelated.setOrderNum(orderNum);
			mediaFileRelatedMng.update(mediaFileRelated);
			resultJson.put("code", 1);
			ResponseUtils.renderJson(response, resultJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 0);
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}

	@RequestMapping(value = "/enterfile/deleteRelatedMediaFile.do")
	public void deleteRelatedMediaFile(String id, HttpServletRequest request, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			mediaFileRelatedMng.deleteById(id) ;
			resultJson.put("code", 1);
			ResponseUtils.renderJson(response, resultJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 0);
			ResponseUtils.renderJson(response, resultJson.toString());
		}
	}

	@RequestMapping(value = "/upload.do")
	public String uploadFilePage(Integer vr, ModelMap map,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		map.put("vr", vr);
		map.put("userId", user.getUserId());
		return "filelib/upload";
	}

	@RequestMapping(value = "/media_auth/change.do")
	public void updateMediaAuth(Long fileId, Integer authType, String viewPassword, String welcomeMsg,String fwelcomeMsg, Double viewMoney,
			Integer openF,Integer openFS,Integer needLogin, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			ILiveFileAuthentication fileAuthentication = iLiveFileAuthenticationMng
					.getFileAuthenticationByFileId(fileId);
			welcomeMsg=new String(welcomeMsg.trim().getBytes("ISO-8859-1"), "UTF-8"); 
			fwelcomeMsg=new String(fwelcomeMsg.trim().getBytes("ISO-8859-1"), "UTF-8"); 
			if (fileAuthentication == null) {
				// 新增
				fileAuthentication = new ILiveFileAuthentication();
				fileAuthentication.setAuthType(authType);
				fileAuthentication.setFileId(fileId);
				if (authType != null && authType.intValue() == 1) {
					fileAuthentication.setNeedLogin(needLogin);
					
				} else
				if (authType != null && authType.intValue() == 2) {
					fileAuthentication.setViewPassword(viewPassword);
					fileAuthentication.setWelcomeMsg(welcomeMsg);
					fileAuthentication.setNeedLogin(needLogin);
				} else if (authType != null && authType.intValue() == 3) {
					fileAuthentication.setWelcomeMsg(welcomeMsg);
					fileAuthentication.setViewMoney(viewMoney);
					if (null != openF) {
						fileAuthentication.setOpenFCodeSwitch(1);
					} else {
						fileAuthentication.setOpenFCodeSwitch(0);
					}
				}else if(authType != null && authType.intValue() == 6){
					fileAuthentication.setAuthType(authType);
					fileAuthentication.setWelcomeMsg(fwelcomeMsg);
					if(openFS!=null){
						fileAuthentication.setFopenStatus(1);
					}else{
						fileAuthentication.setFopenStatus(0);
					}
					fileAuthentication.setNeedLogin(1);
				}else if(authType != null && authType.intValue() == 6){
					fileAuthentication.setAuthType(authType);
					fileAuthentication.setNeedLogin(1);
				}
				iLiveFileAuthenticationMng.addFileAuth(fileAuthentication);
			} else {
				// 修改
				fileAuthentication.setAuthType(authType);
				if (authType != null && authType.intValue() == 1) {
					fileAuthentication.setNeedLogin(needLogin);
					
				} else
				if (authType != null && authType.intValue() == 2) {
					fileAuthentication.setViewPassword(viewPassword);
					fileAuthentication.setWelcomeMsg(welcomeMsg);
					fileAuthentication.setNeedLogin(needLogin);
				}else if(authType != null && authType.intValue() == 3){
					fileAuthentication.setWelcomeMsg(welcomeMsg);
					fileAuthentication.setViewMoney(viewMoney);
					if (null != openF) {
						fileAuthentication.setOpenFCodeSwitch(1);
					} else {
						fileAuthentication.setOpenFCodeSwitch(0);
					}
				}else if(authType != null && authType.intValue() == 6){
					fileAuthentication.setAuthType(authType);
					fileAuthentication.setWelcomeMsg(fwelcomeMsg);
					if(openFS!=null){
						fileAuthentication.setFopenStatus(1);
					}else{
						fileAuthentication.setFopenStatus(0);
					}
					fileAuthentication.setNeedLogin(1);
				}
				iLiveFileAuthenticationMng.updateFileAuth(fileAuthentication);
				iLiveFileAuthenticationRecordMng.deleteFileAuthenticationRecordByFileId(fileId);
			}
			resultJson.put("code", 1);
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", 0);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	@RequestMapping(value = "/whiteadd.do")
	public void viewWhiteBill(String[] phoneNums, Long fileId, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		if (phoneNums == null) {
			jsonObject.put("status", "0");
			jsonObject.put("message", "没有选择条目");
		} else {
			jsonObject.put("status", "1");
			jsonObject.put("message", "操作成功");
			iLiveFileWhiteBillMng.rebuildFileWhite(phoneNums, fileId);
			ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng.getFileAuthenticationByFileId(fileId);
			if (fileAuth == null) {
				fileAuth = new ILiveFileAuthentication();
				fileAuth.setAuthType(4);
				fileAuth.setFileId(fileId);
				iLiveFileAuthenticationMng.addFileAuth(fileAuth);
			} else {
				fileAuth.setAuthType(4);
				iLiveFileAuthenticationMng.updateFileAuth(fileAuth);
			}

			// List<ILiveViewWhiteBill> distinctUserPhone =
			// iLiveFileWhiteBillMng.distinctUserPhone(phoneNums,fileId);
			//
			// // viewWhiteMng.distinctUserPhone(phoneNums, iLiveEventId);
			// if (distinctUserPhone != null && !distinctUserPhone.isEmpty()) {
			// for (ILiveViewWhiteBill bill : distinctUserPhone) {
			// viewWhiteMng.saveIliveViewWhiteBill(bill);
			// iLiveFileWhiteBillMng.saveILiveViewWhiteBill();
			// }
			// }
			// ILiveLiveRoom iliveRoom = iLiveRoomMng.getIliveRoom(roomId);
			// ILiveEvent liveEvent = iliveRoom.getLiveEvent();
			// // 白名单观看
			// liveEvent.setViewAuthorized(4);
			// iLiveEventMng.updateILiveEvent(liveEvent);
			// iLiveViewAuthBillMng.deleteLiveViewAuthBillByLiveEventId(liveEvent.getLiveEventId());
			ResponseUtils.renderJson(response, jsonObject.toString());
		}
	}
	
	/**
	 * 白名单列表
	 */
	@RequestMapping(value = "whitelist.do")
	public void whiteListRouter(String queryNum, Integer pageNo, Integer pageSize,Long fileId,
			HttpServletRequest request,HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			
			Pagination page = iLiveFileWhiteBillMng.getPage(queryNum, pageNo, 20, fileId);
			resultJson.put("status", 1001);
	    	resultJson.put("page", page);
		} catch (Exception e) {
			resultJson.put("status", 1002);
	    	
		}
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}
	/**
	 * 白名单清除
	 */
	@RequestMapping(value = "delAll.do")
	public void delAll( HttpServletRequest request,HttpServletResponse response,Long fileId) {
		JSONObject resultJson = new JSONObject();
		try {
			
			iLiveFileWhiteBillMng.clearViewWhiteBill(fileId);
				resultJson.put("status", 1001);
		} catch (Exception e) {
			resultJson.put("status", 1002);
		}
		
			
			
			ResponseUtils.renderJson(request,response, resultJson.toString());
		
	}
	
	/**
	 * 添加白名单
	 */
	@RequestMapping(value = "addwhite.do")
	public void addWhiteListRouter(Model model, ILiveFileWhiteBill bill, Long fileId, 
			HttpServletRequest request,HttpServletResponse response ) {
		JSONObject resultJson = new JSONObject();
		try {
			
			List<ILiveFileWhiteBill> list=iLiveFileWhiteBillMng.getAllViewWhiteBilll(bill.getPhoneNum(),fileId);
			if(list!=null&&list.size()!=0) {
				iLiveFileWhiteBillMng.deleteById(list.get(0).getBillId());
			}
			Long billIdEnd = iLiveFieldIdManagerMng.getNextId("ilive_file_whitebill", "bill_id", 1);
			Long firstIdEnd = billIdEnd - 1;
			
				
				bill.setBillId(firstIdEnd);
				bill.setFileId(fileId);
				bill.setExportTime(new Timestamp(System.currentTimeMillis()));
				iLiveFileWhiteBillMng.save(bill);
			
			
			resultJson.put("status", 1001);
		} catch (Exception e) {
			resultJson.put("status", 1002);
		}
		
		
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "deletewhite.do")
	public void deleteWhite(Long whitebillId, 
			HttpServletRequest request,HttpServletResponse response ) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveFileWhiteBillMng.deleteById(whitebillId);
			resultJson.put("status", 1001);
		} catch (Exception e) {
			resultJson.put("status", 1002);
		}
		
		ResponseUtils.renderJson(request,response, resultJson.toString());
	}

	/**
	 * 白名单导出
	 * 
	 * @return
	 */
	@RequestMapping(value="excelexport.do")
	@ResponseBody
	public void excel(Long fileId,String queryNum,HttpServletRequest request,HttpServletResponse response) {
		List<ILiveFileWhiteBill> list =iLiveFileWhiteBillMng.getAllViewWhiteBilll(queryNum, fileId);
		String[] title = {"视频ID","用户","姓名"};
		List<String[]> excelBody = new LinkedList<>();
		for(ILiveFileWhiteBill user:list) {
			String[] strings = new String[3];
			strings[0] =String.valueOf(user.getFileId());
			strings[1] =String.valueOf(user.getPhoneNum());
			strings[2] =String.valueOf(user.getUserName());
			
			excelBody.add(strings);
		}
		HSSFWorkbook workbook = excelExport(title,excelBody);
		try {
			this.setResponse(response,"回看白名单用户.xls");
			OutputStream os = response.getOutputStream();
	        workbook.write(os);
	        os.flush();
	        os.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("/o_uploadFile.do")
	public void upload(@RequestParam MultipartFile file, String fileType, Integer width, Integer height,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Integer serverGroupId,Long fileId) {
		JSONObject json = new JSONObject();
		try {
			if (null != fileType && Constants.UPLOAD_FILE_TYPE_Excel.equalsIgnoreCase(fileType)) {
				String fileName = file.getOriginalFilename();
				String tempFileName = System.currentTimeMillis() + "."
						+ fileName.substring(fileName.lastIndexOf(".") + 1);
				String realPath = request.getSession().getServletContext().getRealPath("/temp");
				File tempFile = createTempFile(realPath + "/" + tempFileName, file);
				
				if (fileName.endsWith("xls")) {
					this.readXls(new FileInputStream(tempFile));
				} else {
					int count = 0;
					try {
						List<Object[]> readXlsx = this.readXlsx(tempFile.getAbsolutePath());
						
						boolean hasError = false;
						for (Object[] objArr : readXlsx) {
							count++;
							if (count == 1) {
								// 跳过第一行
								continue;
							}
							for (int i = 0; i < objArr.length; i++) {
								Object object = objArr[i];
								if (i == 0) {
									String phoneNum = (String) object;
									String regex = "[0-9]*";
									if (phoneNum.length() != 11) {
										hasError = true;
										break;
									} else {
										Pattern p = Pattern.compile(regex);
										Matcher m = p.matcher(phoneNum);
										boolean isMatch = m.matches();
										if (!isMatch) {
											hasError = true;
											break;
										}
									}
								}
							}
						}
						if (hasError) {
							json.put("status", 2);
							json.put("msg", "第"+(count-1)+"行导入出错");
						} else {
							UserBean user = ILiveUtils.getUser(request);
							//通知计费系统企业消耗
							EnterpriseUtil.openEnterprise(user.getEnterpriseId(),EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,file.getSize()/1024L+"",user.getCertStatus());
							iLiveFileWhiteBillMng.batchInsertUser(readXlsx, user,fileId);
							json.put("status", 1);
						}
						tempFile.delete();
					} catch (Exception e) {
						json.put("status", 0);
						json.put("msg", "第"+count+"行导入出错");
					}
					
					ResponseUtils.renderJson(response, json.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 构建临时文件
	 * 
	 * @param tempFilePath
	 * @param file
	 * @return
	 */
	private File createTempFile(String tempFilePath, MultipartFile file) {
		long start = System.currentTimeMillis();
		File tempFile = new File(tempFilePath);
		if (null != tempFile && !tempFile.exists()) {
			tempFile.mkdirs();
		}
		try {
			file.transferTo(tempFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		
		return tempFile;
	}
	private List<Object[]> readXlsx(String fileName) throws IOException {
		// String fileName = "D:\\excel\\xlsx_test.xlsx";
		List<Object[]> list = new ArrayList<>();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileName);
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow == null) {
					continue;
				}
				// 循环列Cell
				Object[] objArr = new Object[xssfRow.getLastCellNum()];
				for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
					XSSFCell xssfCell = xssfRow.getCell(cellNum);
					if (xssfCell == null) {
						continue;
					}
					objArr[cellNum] = getValue(xssfCell);
					// // System.out.print(" " + getValue(xssfCell));
				}
				list.add(objArr);
				// // System.out.println();
			}
		}
		return list;
	}

	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfCell) {
		if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
			return NumberToTextConverter.toText(xssfCell.getNumericCellValue());
		} else {
			return String.valueOf(xssfCell.getStringCellValue());
		}
	}

	private void readXls(InputStream is) throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				// 循环列Cell
				for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
					HSSFCell hssfCell = hssfRow.getCell(cellNum);
					if (hssfCell == null) {
						continue;
					}
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return NumberToTextConverter.toText(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	// @RequestMapping(value = "/saveMedia")
	// private void saveMedia(String str) {
	//
	// PoiUtils poiUtils = new PoiUtils();
	// List<ILiveMediaFile> list = poiUtils.Main();
	//
	// if (list.size() > 0) {
	// for (int i = 0; i < list.size(); i++) {
	// ILiveMediaFile iLiveMediaFile = list.get(i);
	// iLiveMediaFile.setMediaCreateTime(new Timestamp(new Date().getTime()));
	// /*
	// * iLiveMediaFile.setLiveRoomId(liveRoomId);
	// * iLiveMediaFile.setLiveEventId(liveEventId);
	// * iLiveMediaFile.setEnterpriseId(enterpriseId);
	// * iLiveMediaFile.setUserId(userId);
	// */
	// iLiveMediaFile.setCreateType(0);
	// iLiveMediaFile.setFileType(1);
	// iLiveMediaFile.setOnlineFlag(1);
	// iLiveMediaFile.setDelFlag(0);
	// iLiveMediaFile.setDuration(0);
	// iLiveMediaFile.setFileRate(0D);
	// iLiveMediaFile.setWidthHeight("");
	// iLiveMediaFile.setFileSize(0l);
	// iLiveMediaFile.setServerMountId(100);
	// iLiveMediaFile.setCreateEndTime(new Timestamp(new Date().getTime()));
	// iLiveMediaFile.setMediaCreateTime(iLiveMediaFile.getCreateStartTime());
	// iLiveMediaFile.setFileExtension("mp4");
	// iLiveMediaFile.setAllowComment(1);
	// iLiveMediaFile.setMediaInfoStatus(1);
	// iLiveMediaFile.setMediaInfoDealState(1);
	// iLiveMediaFileMng.saveIliveMediaFile(iLiveMediaFile);
	// }
	// }
	//
	// }

	@SuppressWarnings("unchecked")
	@RequestMapping("freshComments")
	public void freshComments(Long fileId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Pagination pagination = iLiveMediaFileCommentsMng.selectILiveMediaFileCommentsPage(fileId, 1, 20, null, null);
			List<ILiveMediaFileComments> list = (List<ILiveMediaFileComments>) pagination.getList();
			if (list != null) {
				for (ILiveMediaFileComments comment : list) {
					String comments = comment.getComments();
					String replaceKeyToImg = ExpressionUtil.replaceKeyToImg(comments == null ? "" : comments);
					comment.setComments(replaceKeyToImg);
					
					ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(comment.getUserId());
					if (iLiveManager!=null) {
						comment.setUserMobile(iLiveManager.getMobile());
					}
					
				}
			}
			result.put("code", 0);
			result.put("msg", "获取评论成功");
			result.put("data", list);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 1);
			result.put("msg", "获取评论失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 导出
	 */
	@RequestMapping(value = "/enterfile/export.do")
	public void export(Long fileId,String search, ModelMap map, HttpServletRequest request,HttpServletResponse response) {
		Pagination pagination = iLiveMediaFileCommentsMng.selectILiveMediaFileCommentsPage(fileId, 1, 50000, null, search);
		List<ILiveMediaFileComments> list = (List<ILiveMediaFileComments>) pagination.getList();
		if (list != null) {
			for (ILiveMediaFileComments comment : list) {
				String comments = comment.getComments();
				String replaceKeyToImg = ExpressionUtil.replaceKeyToImg(comments == null ? "" : comments);
				comment.setComments(replaceKeyToImg);
			}
		}
		
		String[] title = {"用户名","评论内容","评论时间"};
		List<String[]> excelBody = new LinkedList<>();
		for(ILiveMediaFileComments comment:list) {
			String[] strings = new String[3];
			strings[0] =String.valueOf(comment.getCommentsUser());
			strings[1] =String.valueOf(comment.getComments());
			strings[2] =String.valueOf(comment.getCreateTime());
			
			excelBody.add(strings);
		}
		HSSFWorkbook workbook = excelExport(title,excelBody);
		try {
			this.setResponse(response,"互动.xls");
			OutputStream os = response.getOutputStream();
	        workbook.write(os);
	        os.flush();
	        os.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
		fileName = new String(fileName.getBytes(),"ISO8859-1");
		response.setContentType("application/octet-stream;charset=ISO8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
	}
	//excel导出
		public HSSFWorkbook excelExport(String[] title,List<String[]> body) {
			//1、创建HSSFWorkbook文件对应一个Excel文件
			HSSFWorkbook workbook = new HSSFWorkbook();
			//2、创建一个sheet
			HSSFSheet sheet = workbook.createSheet();
			//3、在sheet中添加表头第0行
			HSSFRow rowHead = sheet.createRow(0);
			//4、创建单元格 并设置表头格式
			HSSFCellStyle style = workbook.createCellStyle();
			//style.setAlignment(HSSFCellStyle.ALIGN_CENTER);	//创建一个居中格式
			
			//声明对象
			HSSFCell cell = null;
			
			//创建标题
			for(int i = 0;i<title.length;i++) {
				cell = rowHead.createCell(i);
				cell.setCellValue(title[i]);
				cell.setCellStyle(style);
			}
			
			//添加内容
			if (body.size()>0) {
				for (int i = 0; i < body.size(); i++) {
					String[] s = body.get(i);
					HSSFRow rowBody = sheet.createRow(i+1);
					for(int j=0;j<s.length;j++) {
						HSSFCell cell2 = rowBody.createCell(j);
						cell2.setCellValue(s[j]);
					}
				}
			}
			
			return workbook;
		}
		
		/**
		 * 选择文档列表
		 */
		@RequestMapping(value = "/enterfile/docList.do")
		public void docList(String name,Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response) {
			JSONObject result = new JSONObject();
			
			try {
				UserBean user = ILiveUtils.getUser(request);
				Pagination page = new Pagination( pageNo==null?1:pageNo, 10, 0, new ArrayList<>());
				Integer level = user.getLevel();
				level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
				//查询子账户是否具有图片查看全部
				boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_document);
				if(level.equals(ILiveManager.USER_LEVEL_SUB)&&!per){
					long userId = Long.parseLong(user.getUserId());
					page = documentManage.getCollaborativePage(name, pageNo==null?1:pageNo, 10, userId);
				}else{
					Integer enterpriseId = user.getEnterpriseId();
					page = documentManage.getPage(name,enterpriseId, pageNo==null?1:pageNo, 10);
				}
				result.put("code", 1);
				result.put("msg", "获取文档列表成功");
				result.put("data", page);
			} catch (Exception e) {
				result.put("code", 0);
				result.put("msg", "获取文档列表失败");
				result.put("data", null);
			}
			ResponseUtils.renderJson(response, result.toString());
		} 
		/**
		 * 确认选择
		 */
		@RequestMapping(value = "/enterfile/addDoc.do")
		public void addDoc(String ids,Long fileId,HttpServletRequest request,HttpServletResponse response) {
			JSONObject result = new JSONObject();
			
			try {
				String[] idList =ids.split(",");
				for (String id : idList) {
					ILiveFileDoc fileDoc1=fileDocMng.getByFileIdDocId(fileId,Long.parseLong(id));
					if(fileDoc1==null) {
						ILiveFileDoc fileDoc =new ILiveFileDoc();
						fileDoc.setFileId(fileId);
						fileDoc.setDocument(documentManage.getById(Long.parseLong(id)));
						fileDocMng.save(fileDoc);
					}
					
				}
				List<ILiveFileDoc> fileDocList =fileDocMng.getListById(fileId);
				result.put("code", 1);
				result.put("fileDocList", fileDocList);
				result.put("msg", "添加成功");
			} catch (Exception e) {
				result.put("code", 0);
				result.put("msg", "添加失败");
			}
			ResponseUtils.renderJson(response, result.toString());
		}
		/**
		 * 是否允许下载
		 */
		@RequestMapping(value = "/enterfile/isDown.do")
		public void isDown(Long docId,Integer state,HttpServletRequest request,HttpServletResponse response) {
			JSONObject result = new JSONObject();
			
			try {
				DocumentManager documentManager= documentManage.getById(docId);
				documentManager.setIsAllow(state);
				documentManage.update(documentManager);
				result.put("code", 1);
				result.put("msg", "修改成功");
			} catch (Exception e) {
				result.put("code", 0);
				result.put("msg", "修改失败");
			}
			ResponseUtils.renderJson(response, result.toString());
		}
		/**
		 * 删除
		 */
		@RequestMapping(value = "/enterfile/deleteDoc.do")
		public void deleteDoc(Long fileDocId,HttpServletRequest request,HttpServletResponse response) {
			JSONObject result = new JSONObject();
			
			try {
				fileDocMng.delete(fileDocId);
				result.put("code", 1);
				result.put("msg", "修改成功");
			} catch (Exception e) {
				result.put("code", 0);
				result.put("msg", "修改失败");
			}
			ResponseUtils.renderJson(response, result.toString());
		}
		/**
		 * 开启文档直播
		 */
		@RequestMapping(value = "/enterfile/isFileDoc.do")
		public void isFileDoc(Long fileId,Integer state,HttpServletRequest request,HttpServletResponse response) {
			JSONObject result = new JSONObject();
			
			try {
				ILiveMediaFile selectILiveMediaFile	=iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
				selectILiveMediaFile.setIsFileDoc(state);
				iLiveMediaFileMng.updateMediaFile(selectILiveMediaFile);
				result.put("code", 1);
				result.put("msg", "修改成功");
			} catch (Exception e) {
				result.put("code", 0);
				result.put("msg", "修改失败");
			}
			ResponseUtils.renderJson(response, result.toString());
		}
		/**
		 * 评论置顶
		 */
		@RequestMapping(value = "/enterfile/topFlag.do")
		public void topFlag(Long commentId,Integer state,HttpServletRequest request,HttpServletResponse response) {
			JSONObject result = new JSONObject();
			
			try { 
				ILiveMediaFileComments comments =iLiveMediaFileCommentsMng.getFileCommentById(commentId);
				comments.setTopFlagNum(state);
				Timestamp now = new Timestamp(new Date().getTime());
				comments.setTopTime(now);
				iLiveMediaFileCommentsMng.update(comments);
				result.put("code", 1);
				result.put("msg", "修改成功");
			} catch (Exception e) {
				result.put("code", 0);
				result.put("msg", "修改失败");
			}
			ResponseUtils.renderJson(response, result.toString());
		}
		
	
}
