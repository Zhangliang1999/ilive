package com.bvRadio.iLive.iLive.action.admin;
import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UPLOAD_FILE_TYPE_IMAGE;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
/**
 * 图集上传
 * @author YanXL
 *
 */
@Controller
@RequestMapping(value="upload")
public class ImageUploadFileController {
	private static final Logger log = LoggerFactory.getLogger(ImageUploadFileController.class);
	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;
	/**
	 * @key 用户Id
	 * @value 地址集
	 */
	public static Hashtable<String, List<String>> imageUserMap = new Hashtable<String, List<String>>();
	/**
	 * 上传处理
	 * @param file 
	 * @param fileType
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/imageUpload.do")
	public void upload(@RequestParam CommonsMultipartFile file, String fileType, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			UserBean userBean = ILiveUtils.getUser(request);
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
					json.put("status", SUCCESS_STATUS);
					json.put("filePath", filePath);
					json.put("httpUrl", httpUrl);
					String userId = userBean.getUserId();
					List<String> list = imageUserMap.get(userId);
					if(list==null){
						list = new ArrayList<String>();
					}
					list.add(httpUrl);
					imageUserMap.put(userId, list);
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
	/**
	 * 获取上传图集
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/selectImage.do")
	public void selectImageList(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		try {
			UserBean userBean = ILiveUtils.getUser(request);
			String userId = userBean.getUserId();
			List<String> list = imageUserMap.get(userId);
			json.put("status", SUCCESS_STATUS);
			json.put("message", "获取图集成功");
			json.put("imageList", list);
			imageUserMap.remove(userId);
		}catch (Exception e){
			json.put("status", ERROR_STATUS);
			json.put("message", "获取图集失败");
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	/**
	 * 清除上传图集
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/deleteImage.do")
	public void deleteImageList(Long userId,HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		try {
			if(userId==null){
				UserBean userBean = ILiveUtils.getUser(request);
				userId = Long.parseLong(userBean.getUserId());
			}
			imageUserMap.remove(String.valueOf(userId));
			json.put("status", SUCCESS_STATUS);
			json.put("message", "清空图集成功");
		}catch (Exception e){
			json.put("status", ERROR_STATUS);
			json.put("message", "清空图集失败");
		}
		ResponseUtils.renderJson(response, json.toString());
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
}
