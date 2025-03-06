package com.bvRadio.iLive.manager.action;

import static com.bvRadio.iLive.iLive.Constants.UPLOAD_FILE_TYPE_IMAGE;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.util.FileUtils;

@Controller
@RequestMapping("file")
public class FileUploadController {
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@RequestMapping("/o_uploadFile.do")
	public void upload(@RequestParam MultipartFile file, String fileType, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Integer serverGroupId) {
		JSONObject json = new JSONObject();
		try {
			if (null != fileType && UPLOAD_FILE_TYPE_IMAGE.equalsIgnoreCase(fileType)) {
				String fileName = file.getOriginalFilename();
				String tempFileName = System.currentTimeMillis() + "."
						+ fileName.substring(fileName.lastIndexOf(".") + 1);
				String realPath = request.getSession().getServletContext().getRealPath("/temp");
				System.out.println("realPath=========================================" + realPath);
				File tempFile = createTempFile(realPath + "/" + tempFileName, file);
				System.out.println("tempFileName=========================================" + tempFileName);
				System.out.println("ext=========================================" + "");
				String filePath = FileUtils.getTimeFilePath(tempFileName);
				ILiveUploadServer iLiveUploadServer = iLiveUploadServerMng.getDefaultServer();
				boolean result = false;
				if (iLiveUploadServer != null) {
					FileInputStream in = new FileInputStream(tempFile);
					result = iLiveUploadServer.upload(filePath, in);
				}
				if (result) {
					String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath() + filePath;
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
			} else if (null != fileType && Constants.UPLOAD_FILE_TYPE_VIDEO.equalsIgnoreCase(fileType)) {
				String fileName = file.getOriginalFilename();
				String tempFileName = System.currentTimeMillis() + "."
						+ fileName.substring(fileName.lastIndexOf(".") + 1);
				System.out.println("tempFileName=========================================" + tempFileName);
				String realPath = request.getSession().getServletContext().getRealPath("/temp");
				System.out.println("realPath=========================================" + realPath);
				File tempFile = createTempFile(realPath + "/" + tempFileName, file);
				String filePath = FileUtils.getTimeFilePath(tempFileName);
				ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng.getAccessMethodBySeverGroupId(102);
				boolean result = false;
				if (accessMethod != null) {
					FileInputStream in = new FileInputStream(tempFile);
					result = accessMethod.uploadFile(filePath, in);
				}
				if (result) {
					String httpUrl = accessMethod.getH5HttpUrl() + accessMethod.getMountInfo().getFtp_path() + filePath;
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
			e.printStackTrace();
		}
	}

	@RequestMapping("/o_uploadImgFile.do")
	public void uploadImg(@RequestParam MultipartFile imgFile, String fileType, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Integer serverGroupId) {
		JSONObject json = new JSONObject();
		try {
			String fileName = imgFile.getOriginalFilename();
			String tempFileName = System.currentTimeMillis() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
			String realPath = request.getSession().getServletContext().getRealPath("/temp");
			System.out.println("realPath=========================================" + realPath);
			File tempFile = createTempFile(realPath + "/" + tempFileName, imgFile);
			System.out.println("tempFileName=========================================" + tempFileName);
			System.out.println("ext=========================================" + "");
			String filePath = FileUtils.getTimeFilePath(tempFileName);
			ILiveUploadServer iLiveUploadServer = iLiveUploadServerMng.getDefaultServer();
			boolean result = false;
			if (iLiveUploadServer != null) {
				FileInputStream in = new FileInputStream(tempFile);
				result = iLiveUploadServer.upload(filePath, in);
			}
			if (result) {
				String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath() + filePath;
				json.put("error", 0);
				json.put("filePath", filePath);
				json.put("url", httpUrl);
				ResponseUtils.renderJson(response, json.toString());
				return;
			} else {
				json.put("error", 1);
				json.put("filePath", filePath);
				ResponseUtils.renderJson(response, json.toString());
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		log.info("创建临时上传文件耗时：{} ms", end - start);
		return tempFile;
	}

}
