package com.bvRadio.iLive.iLive.action.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.MountInfo;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Controller
@RequestMapping(value = "mediafile")
public class ILiveMediaFileController {

	private static final String HTTP_PROTOCAL = "http://";

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;

	@RequestMapping("/entervedio")
	public String enterVedio(String mediaName, Integer pageNo, Integer pageSize, ModelMap map,
			HttpServletRequest request) {
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("userId", ILiveUtils.getUser(request).getUserId());
		sqlParam.put("mediaFileName", mediaName);
		sqlParam.put("fileType", new Integer(1));
		sqlParam.put("delFlag", new Integer(1));
		Pagination pagination = iLiveMediaFileMng.getMediaFilePage(sqlParam, pageNo, new Integer(10));
		map.addAttribute("liveMediaPage", pagination);
		map.addAttribute("mediaName", mediaName);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "1_1");
		return "medialib/vedio";
	}

	@RequestMapping("/delete")
	public void deleteVedio(Long id, HttpServletResponse response) {
		iLiveMediaFileMng.deleteVedio(id);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
	}

	@RequestMapping("/deletes")
	public void deleteVedios(@RequestParam(value = "ids[]") Long[] ids, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		iLiveMediaFileMng.deleteVediosByIds(ids);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
	}

	@RequestMapping("/updatelinestate")
	public void upateVediosLineState(@RequestParam(value = "ids[]") Long[] ids, Integer state,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		iLiveMediaFileMng.updateVediosLineStateByIds(ids, state);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
	}

	@RequestMapping("/updatemediaInfo")
	public void upateMediaInfo(Long id, String mediaFileName, String mediaFileDesc, HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		ILiveMediaFile iLiveMediaFile = new ILiveMediaFile();
		iLiveMediaFile.setFileId(id);
		iLiveMediaFile.setMediaFileName(mediaFileName);
		iLiveMediaFile.setMediaFileDesc(mediaFileDesc);
		iLiveMediaFileMng.updateMediaFileById(iLiveMediaFile);
		ResponseUtils.renderJson(response, "{\"status\":\"success\"}");
	}

	@RequestMapping("/entermediaedit")
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
			iLiveMediaFile.setFilePath(allPath);
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
		sqlParam.put("mediaFileName", mediaName);
		sqlParam.put("userId", ILiveUtils.getUser(request).getUserId());
		sqlParam.put("createType", new Integer(0));
		sqlParam.put("delFlag", new Integer(1));
		Pagination pagination = iLiveMediaFileMng.getMediaFilePage(sqlParam, pageNo, new Integer(6));
		int fileNumber = 0;
		String totalFileSize = iLiveMediaFileMng
				.selectLiveRecordTotalSize(Long.parseLong(ILiveUtils.getUser(request).getUserId()), mediaName);
		if (pagination != null) {
			fileNumber = pagination.getTotalCount();
		}
		map.addAttribute("pagination", pagination);
		map.addAttribute("mediaName", mediaName);
		map.addAttribute("fileNumber", fileNumber);
		map.addAttribute("totalFileSize", totalFileSize);
		map.addAttribute("topActive", "2");
		map.addAttribute("leftActive", "2_1");
		return "medialib/record";
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

}
