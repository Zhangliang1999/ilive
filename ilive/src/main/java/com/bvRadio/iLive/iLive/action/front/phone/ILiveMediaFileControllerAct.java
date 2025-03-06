package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.jwzt.comm.StringUtils;

/**
 * @author administrator 观看文件控制端
 * 管理端接口
 */
@Controller
@RequestMapping(value = "/livephone/room/vod")
public class ILiveMediaFileControllerAct {

	Logger logger = LoggerFactory.getLogger(ILiveMediaFileControllerAct.class);

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveMediaFileCommentsMng commentsMng;

	@Autowired
	private ILiveFileAuthenticationMng iLiveFileAuthenticationMng;
	
	
	@Autowired
	private ILiveVideoHistoryMng iLiveVideoHistoryMng;
	
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	
	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;
	/**
	 * 我的回看列表
	 */
	@RequestMapping(value = "files.jspx")
	public void getMyLivePlayList(Long userId, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			UserBean userBean = ILiveUtils.getAppUser(request);
			if (null == userId) {
				try {
					userId = Long.parseLong(userBean.getUserId());
				} catch (Exception e) {
				}
			}
			Integer enterpriseId = userBean.getEnterpriseId();
			if (enterpriseId == null || enterpriseId.equals(0)) {
				ILiveManager iLiveManager = iLiveManagerMng.selectILiveManagerById(userId);
				try {
					enterpriseId = iLiveManager.getEnterpriseId().intValue();
				} catch (Exception e) {
					enterpriseId = iLiveManager.getEnterPrise().getEnterpriseId();
				}
			}
//			ILiveEnterprise iLiveEnterprise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
//			Integer certStatus = iLiveEnterprise.getCertStatus();
//			boolean b = EnterpriseUtil.selectIfEn(enterpriseId, EnterpriseCache.ENTERPRISE_FUNCTION_liveRecording,certStatus);
//			if(b){
//				resultJson.put("code", ERROR_STATUS);
//				resultJson.put("message", EnterpriseCache.ENTERPRISE_FUNCTION_liveRecording_Content);
//				ResponseUtils.renderJson(request, response, resultJson.toString());
//				return;
//			}
			if (userBean != null && userId.equals(Long.parseLong(userBean.getUserId()))) {
				List<AppMediaFile> fileList = iLiveMediaFileMng.getPersonalFileList(userId, pageNo == null ? 1 : pageNo,
						pageSize == null ? 20 : pageSize,request);
				if (fileList != null && !fileList.isEmpty()) {
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "获取列表成功");
					resultJson.put("data", fileList);
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "操作人不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		resultJson.put("code", SUCCESS_STATUS);
		resultJson.put("message", "获取列表成功");
		resultJson.put("data", new JSONArray());
		ResponseUtils.renderJson(request, response, resultJson.toString());
		return;
	}
	
	
	/**
	 * 获取回看列表
	 * @param pageNo
	 * @param pageSize
	 * @param roomId
	 * @param httpServletResponse
	 * @param httpServletRequest
	 */
	@RequestMapping("/getrecordlist.jspx")
	public void getMediaRecordList(Integer pageNo, Integer pageSize, Integer roomId,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		JSONObject resultJson = new JSONObject();
		Map<String, Object> sqlParam = new HashMap<>();
		if (roomId == null) {
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "信息获取成功");
			resultJson.put("data", new JSONArray());
			ResponseUtils.renderJson(httpServletRequest, httpServletResponse, resultJson.toString());
			return;
		}
		try {
			sqlParam.put("roomId", roomId);
			List<AppMediaFile> mediaFilePageByRoom = iLiveVideoHistoryMng.getMediaFilePageByRoom(sqlParam,
					pageNo == null ? 1 : pageNo, pageSize == null ? 15 : pageSize, roomId);
			JSONArray jArr = new JSONArray();
			if (mediaFilePageByRoom != null && !mediaFilePageByRoom.isEmpty()) {
				JSONObject jo = null;
				for (AppMediaFile mediaFile : mediaFilePageByRoom) {
					jo = new JSONObject(mediaFile);
					jArr.put(jo);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "信息获取成功");
			resultJson.put("data", jArr);
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "信息获取失败");
			resultJson.put("data", new JSONObject());
			e.printStackTrace();
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(httpServletRequest, httpServletResponse, resultJson.toString());
	}

	/**
	 * 视频上架与下架 onlineState 0 为下架 1为上架
	 */
	@RequestMapping(value = "online.jspx")
	public void operatorVodOnlineState(Long userId, Long fileId, Integer onlineState, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			if (onlineState == null || fileId == null || userId == null) {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "请求参数不合法");
				resultJson.put("data", new JSONObject());
			}
			UserBean userBean = ILiveUtils.getAppUser(request);
			if (userBean != null && userId.equals(Long.parseLong(userBean.getUserId()))) {
				ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
				if (mediaFile != null && mediaFile.getUserId().equals(userId)) {
					mediaFile.setOnlineFlag(onlineState);
					iLiveMediaFileMng.updateEventFileOnlineFlag(mediaFile);
					resultJson.put("code", SUCCESS_STATUS);
					if (onlineState.intValue() == 0) {
						resultJson.put("message", "下架成功");
					} else {
						resultJson.put("message", "上架成功");
					}
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "无法操作该文件");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "操作人不合法");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "操作发生异常");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			logger.error(e.toString());
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 回看删除
	 */
	@RequestMapping(value = "destroy.jspx")
	public void destroyVod(Long userId, Long fileId, HttpServletRequest request, HttpServletResponse response) {
		UserBean appUser = ILiveUtils.getAppUser(request);
		long parseLong = Long.parseLong(appUser.getUserId());
		JSONObject resultJson = new JSONObject();
		try {
			if (parseLong == userId.longValue()) {
				ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
				if (mediaFile != null && mediaFile.getUserId().longValue() == parseLong) {
					iLiveMediaFileMng.deleteVedio(fileId);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "删除成功");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "非本人文件,禁止删除");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "非本人操作,禁止删除");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "删除失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}

	}

	/**
	 * 修改回看文件
	 */
	@RequestMapping(value = "updateinfo.jspx")
	public void updateMediaInfo(AppMediaFile appFile, Long userId, HttpServletRequest request,
			HttpServletResponse response) {
		UserBean appUser = ILiveUtils.getAppUser(request);
		long parseLong = Long.parseLong(appUser.getUserId());
		JSONObject resultJson = new JSONObject();
		try {
			if (parseLong == userId.longValue()) {
				ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(appFile.getFileId());
				if (mediaFile != null && mediaFile.getUserId().longValue() == parseLong) {
					this.convertAppFile2MediaFile(appFile, mediaFile);
					resultJson.put("code", SUCCESS_STATUS);
					resultJson.put("message", "修改成功");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				} else {
					resultJson.put("code", ERROR_STATUS);
					resultJson.put("message", "非本人文件,禁止修改");
					resultJson.put("data", new JSONObject());
					ResponseUtils.renderJson(request, response, resultJson.toString());
					return;
				}
			} else {
				resultJson.put("code", ERROR_STATUS);
				resultJson.put("message", "非本人操作,禁止修改");
				resultJson.put("data", new JSONObject());
				ResponseUtils.renderJson(request, response, resultJson.toString());
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "修改失败");
			resultJson.put("data", new JSONObject());
			ResponseUtils.renderJson(request, response, resultJson.toString());
			return;
		}
	}

	/**
	 * 获取文件信息 暂不用 从文件列表中获取
	 */
	private void convertAppFile2MediaFile(AppMediaFile appFile, ILiveMediaFile file) {

		/**
		 * 文件名
		 */
		if (!StringUtils.isEmpty(appFile.getFileName())) {
			file.setMediaFileName(appFile.getFileName());
		}

		/**
		 * 文件描述
		 */
		if (!StringUtils.isEmpty(appFile.getFileDesc())) {
			file.setMediaFileDesc(appFile.getFileDesc());
		}

		/**
		 * 文件截图
		 */
		if (!StringUtils.isEmpty(appFile.getFileImg())) {
			file.setFileCover(appFile.getFileImg());
		}

		if (appFile.getAllowComment() != null) {
			file.setAllowComment(appFile.getAllowComment());
		}

		if (appFile.getOnlineState() != null) {
			file.setOnlineFlag(appFile.getOnlineState());
		}

		iLiveMediaFileMng.updateMediaFile(file);
		/**
		 * 鉴权方式
		 */
		Integer authType = appFile.getAuthType();
		if (authType != null) {
			ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
					.getFileAuthenticationByFileId(file.getFileId());
			if (fileAuth == null) {
				fileAuth = new ILiveFileAuthentication();
				fileAuth.setAuthType(authType);
				fileAuth.setFileId(appFile.getFileId());
				if (!StringUtils.isEmpty(appFile.getAuthPassword())) {
					fileAuth.setViewPassword(appFile.getAuthPassword());
				}
				iLiveFileAuthenticationMng.addFileAuth(fileAuth);
			} else {
				fileAuth.setAuthType(authType);
				fileAuth.setNeedLogin(appFile.getNeedLogin()==null?0:appFile.getNeedLogin());
				if (!StringUtils.isEmpty(appFile.getAuthPassword())) {
					fileAuth.setViewPassword(appFile.getAuthPassword());
				}
				iLiveFileAuthenticationMng.updateFileAuth(fileAuth);
			}
		}
	}

	/**
	 * 获取评论内容
	 */
	/**
	 * 推送评论列表向用户
	 * 
	 * @param fileId
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getcomments.jspx")
	public void pushCommentsList2User(Long fileId, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			Pagination fileComments = commentsMng.getFileComments(fileId, pageNo == null ? 1 : pageNo,
					pageSize == null ? 10 : pageSize);
			List<ILiveMediaFileComments> list = (List<ILiveMediaFileComments>) fileComments.getList();
			List<JSONObject> jsonobjs = new ArrayList<>();
			if (list != null && !list.isEmpty()) {
				for (ILiveMediaFileComments comments : list) {
					JSONObject jobj = comments.toSimpleJsonObj();
					jsonobjs.add(jobj);
				}
			}
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取评论成功");
			resultJson.put("data", jsonobjs);
			resultJson.put("totalPage", fileComments.getTotalPage());
			resultJson.put("totalCount", fileComments.getTotalCount());;
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取评论失败");
			resultJson.put("data", new JSONObject());
			logger.error(e.toString());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}

}
