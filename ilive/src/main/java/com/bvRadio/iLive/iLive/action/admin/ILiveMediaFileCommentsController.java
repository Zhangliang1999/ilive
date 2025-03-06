package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.common.page.SimplePage.cpn;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseTerminalUser;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseTerminalUserMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileCommentsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.util.ExpressionUtil;

@Controller
@RequestMapping(value = "file")
public class ILiveMediaFileCommentsController {

	@Autowired
	private ILiveMediaFileCommentsMng iLiveMediaFileCommentsMng;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveEnterpriseTerminalUserMng terminalUserMng;

	/**
	 * 获取视频分页评论
	 * 
	 * @param fileId
	 * @param pageNo
	 * @param map
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/media/comments.do")
	public String enterVedio(Long fileId, Integer pageNo, ModelMap map, HttpServletRequest request) {
		Pagination pagination = iLiveMediaFileCommentsMng.selectILiveMediaFileCommentsPage(fileId, cpn(pageNo), 20,
				null,null);
		List<ILiveMediaFileComments> list = (List<ILiveMediaFileComments>) pagination.getList();
		for (ILiveMediaFileComments iLiveMediaFileComments : list) {
			String comments = iLiveMediaFileComments.getComments();
			iLiveMediaFileComments.setComments(ExpressionUtil.replaceKeyToImg(comments));
		}
		pagination.setList(list);
		map.addAttribute("pagination", pagination);
		map.addAttribute("fileId", fileId);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "1_1");
		return "medialib/fileComments";
	}

	/**
	 * 开启评论 关闭评论
	 */
	@RequestMapping(value = "/comments/switch.do")
	public void fileCommentsSwitch(Long fileId, Integer allowComment, HttpServletRequest request,
			HttpServletResponse response) {
		ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		if (mediaFile != null) {
			mediaFile.setAllowComment(allowComment);
			iLiveMediaFileMng.updateMediaFile(mediaFile);
			ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
		} else {
			ResponseUtils.renderJson(response, "{\"status\":\"error\"}");
		}
	}
	
	
	/**
	 * 批量审核
	 */
	@RequestMapping(value = "/comments/batchcheck.do")
	public void batchCheck(@RequestParam(value = "ids[]") Long[] ids, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			iLiveMediaFileCommentsMng.batchCheck(ids);
			ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
		} catch (Exception e) {
			ResponseUtils.renderJson(response, "{\"status\":\"error\"}");
		}
	}
	
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/comments/batchdelete.do")
	public void batchDelete(@RequestParam(value = "ids[]") Long[] ids, Integer allowComment, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			iLiveMediaFileCommentsMng.batchDelete(ids);
			ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
		} catch (Exception e) {
			ResponseUtils.renderJson(response, "{\"status\":\"error\"}");
		}
	}
	
	/**
	 * 开启审核 关闭审核
	 */
	@RequestMapping(value = "/comments/check.do")
	public void checkStateSwitch(Long fileId, Integer autoCheckFlag, HttpServletRequest request,
			HttpServletResponse response) {
		ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
		if (mediaFile != null) {
			mediaFile.setAutoCheckFlag(autoCheckFlag);
			iLiveMediaFileMng.updateMediaFile(mediaFile);
			ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
		} else {
			ResponseUtils.renderJson(response, "{\"status\":\"error\"}");
		}
	}

	/**
	 * 获取视频分页评论
	 * 
	 * @param fileId
	 * @param pageNo
	 * @param map
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/rest/comments.do")
	public void enterVedio(Long fileId, Integer enterpriseId, String search,Integer pageNo, ModelMap map, HttpServletRequest request,
			Integer uncheckQueryFlag, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		Pagination pagination = iLiveMediaFileCommentsMng.selectILiveMediaFileCommentsPage(fileId, cpn(pageNo), 20,
				uncheckQueryFlag,search);
		List<ILiveMediaFileComments> list = (List<ILiveMediaFileComments>) pagination.getList();
		JSONArray jsonArray = new JSONArray();
		for (ILiveMediaFileComments iLiveMediaFileComments : list) {
			JSONObject simpleJsonObj = iLiveMediaFileComments.toWebSimpleJsonObj();
			ILiveEnterpriseTerminalUser terminalUserFind = terminalUserMng.getTerminalUser((Long)simpleJsonObj.get("userId") ,
					enterpriseId);
			if(terminalUserFind.getIsBlacklist()==1) {
				simpleJsonObj.put("isBlack", 1);
			}else {
				simpleJsonObj.put("isBlack", 0);
			}
			jsonArray.put(simpleJsonObj);
		}
		resultJson.put("currentPage", pagination.getPageNo());
		resultJson.put("data", jsonArray);
		resultJson.put("pageSize", pagination.getPageSize());
		resultJson.put("totalPage", pagination.getTotalPage());
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 禁言针对回看评论
	 * 
	 * @param fileId
	 * @param pageNo
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/rest/forbid.do")
	public void forbidUser(Long commentsId, Integer enterpriseId,Integer state, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		ILiveMediaFileComments fileCommentById = iLiveMediaFileCommentsMng.getFileCommentById(commentsId);
		try {
			if (fileCommentById != null) {
				Long userId = fileCommentById.getUserId();
				ILiveEnterpriseTerminalUser terminalUser = terminalUserMng.getTerminalUser(userId, enterpriseId);
				if(terminalUser!=null) {
					terminalUser.setIsBlacklist(state);
					terminalUserMng.updateTerminalUser(terminalUser);
				}
				resultJson.put("code", SUCCESS_STATUS);
			} else {
				resultJson.put("code", ERROR_STATUS);
			}
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 修改审核状态
	 * 
	 * @param commentsId
	 *            评论ID
	 * @param checkState
	 *            审核状态
	 * @param response
	 */
	@RequestMapping("/checkState/update.do")
	public void updateCheckState(Long commentsId, Integer checkState, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveMediaFileCommentsMng.updateCheckState(commentsId, checkState);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "审核成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "审核失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}

	/**
	 * 删除数据
	 * 
	 * @param commentsId
	 * @param response
	 */
	@RequestMapping("/deleteState/update.do")
	public void updateDeleteState(Long commentsId, HttpServletResponse response) {
		JSONObject resultJson = new JSONObject();
		try {
			iLiveMediaFileCommentsMng.updateDeleteState(commentsId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "删除成功");
		} catch (Exception e) {
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "删除失败");
			// System.out.println(e.toString());
		}
		ResponseUtils.renderJson(response, resultJson.toString());
	}
}
