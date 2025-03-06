package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UPLOAD_FILE_TYPE_IMAGE;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.util.FileUtils;

@Controller
public class FileUploadAct {

	private static final Logger log = LoggerFactory.getLogger(FileUploadAct.class);

	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;

	@RequestMapping("/o_uploadFile.do")
	public void upload(@RequestParam("Filedata") CommonsMultipartFile file, String fileType, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Integer serverGroupId) {
		JSONObject json = new JSONObject();
		try {
			String ext = FileUtils.getFileExt(file.getOriginalFilename());
			String tempFileName = System.currentTimeMillis() + "." + ext;
			String realPath = request.getSession().getServletContext().getRealPath("/temp");
			File tempFile = createTempFile(realPath + "/" + tempFileName, file);
			String filePath = FileUtils.getTimeFilePath(tempFileName);
			if (null != fileType && UPLOAD_FILE_TYPE_IMAGE.equalsIgnoreCase(fileType)) {
				boolean result = false;
				ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
				if (uploadServer != null) {
					FileInputStream in = new FileInputStream(tempFile);
					result = uploadServer.upload(filePath, in);
				}
				String httpUrl = uploadServer.getHttpUrl() + uploadServer.getFtpPath() + "/" + filePath;
				if (result) {
					json.put("status", 1);
					json.put("filePath", filePath);
					json.put("httpUrl", httpUrl);
					ResponseUtils.renderJson(response, json.toString());
					return;
				} else {
					json.put("status", 0);
					json.put("filePath", filePath);
					ResponseUtils.renderJson(response, json.toString());
					return;
				}
			}
		} catch (Exception e) {
			log.error("upload file error!", e);
			try {
				json.put("status", ERROR_STATUS);
				json.put("message", "上传出错");
				ResponseUtils.renderJson(response, json.toString());
				return;
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	private File createTempFile(String tempFilePath, CommonsMultipartFile file) {
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
		log.info("创建临时上传文件耗时：{} ms", end - start);
		return tempFile;
	}

	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	// @Autowired
	// private ILiveUploadServerMng uploadServerMng;
}
