package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.UPLOAD_FILE_TYPE_IMAGE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.bvRadio.iLive.iLive.entity.*;
import com.bvRadio.iLive.iLive.manager.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.Constants;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.PictureUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;


@Controller
@RequestMapping("file")
public class FileUploadController {
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILiveEnterpriseMemberMng memberMng;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	
	@Autowired
	private ILiveConvertTaskMng iLiveConvertTaskMng;

    @Autowired
    private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间

    @Autowired
    private ILiveMeetingFileMng iLiveMeetingFileMng;

	@Autowired
	private WorkLogMng workLogMng;

	@RequestMapping(value = "uploadAndSaveFile.do")
	public void uploadAndSave(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException {
		JSONObject json = new JSONObject();
		String fileName = file.getOriginalFilename();
		String tempFileName = System.currentTimeMillis() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
		String realPath = request.getSession().getServletContext().getRealPath("/temp");
		File tempFile = createTempFile(realPath + "/" + tempFileName, file);
		String filePath = FileUtils.getTimeFilePath(tempFileName);
		String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
		ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
		boolean result = false;
		if (accessMethod != null) {
			FileInputStream in = new FileInputStream(tempFile);
			result = accessMethod.uploadFile(filePath, in);
		}
		if (tempFile.exists()) {
			tempFile.delete();
		}
		if (result) {
			String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
					+ accessMethod.getMountInfo().getBase_path() + filePath;
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

	@RequestMapping("/getILiveServerAccessMethod.do")
	public void getILiveServerAccessMethod(HttpServletResponse response){
		String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
		ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
		ResponseUtils.renderJson(response,JSON.toJSONString(accessMethod));
	}

    @RequestMapping(value = "uploadMeetingFile.do")
    public void uploadMeetingFile(@RequestParam(value = "file") MultipartFile file,Integer roomId, HttpServletRequest request,
                                  HttpServletResponse response){
	    JSONObject json = new JSONObject();
	    try{
            String fileName = file.getOriginalFilename();
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
			String[] rule = {"avi","mp4","jpeg","jpg","png","xls","xlsx","doc","docx","ppt","pptx"};
			boolean flag = false;
			for (String s : rule) {
				if (fileExtension.toLowerCase().endsWith(s)){
					flag = true;
				}
			}
			if(!flag){
				json.put("code", 2);
				json.put("msg", "仅支持word、ppt、Excel、mp4、avi、jpg、png类型文件");
				ResponseUtils.renderJson(response, json.toString());
			}else{
				String tempFileName = System.currentTimeMillis() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
				String realPath = request.getSession().getServletContext().getRealPath("/temp");
				File tempFile = createTempFile(realPath + "/" + tempFileName, file);
				String filePath = FileUtils.getTimeFilePath(tempFileName);
				String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
				ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
				boolean result = false;
				if (accessMethod != null) {
					FileInputStream in = new FileInputStream(tempFile);
					result = accessMethod.uploadFile(filePath, in);
				}
				if (tempFile.exists()) {
					tempFile.delete();
				}
				if (result) {
					String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
							+ accessMethod.getMountInfo().getBase_path() + filePath.substring(1);
					UserBean user = ILiveUtils.getUser(request);
					ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
					IliveMeetingFile meetingFile = new IliveMeetingFile();
					meetingFile.setFilePath(filePath);
					meetingFile.setFileUrl(httpUrl);
					meetingFile.setEnterpriseId(user.getEnterpriseId().longValue());
					meetingFile.setFileId(System.currentTimeMillis());
					meetingFile.setMediaFileName(file.getOriginalFilename());
					meetingFile.setCreateId(user.getUserId());
					meetingFile.setMediaCreateTime(new Timestamp(new Date().getTime()));
					meetingFile.setFileExtension(fileExtension);
					meetingFile.setLiveRoomId(iliveRoom.getRoomId());
					meetingFile.setLiveEventId(iliveRoom.getLiveEvent() == null ? null:iliveRoom.getLiveEvent().getLiveEventId());
					meetingFile.setDelFlag(0);
					meetingFile.setFileSize(file.getSize()/1024);
					if(StringUtils.isNotBlank(fileExtension)){
						fileExtension = fileExtension.toLowerCase();
					}else{
						fileExtension = " ";
					}
					StringBuilder path = new StringBuilder(request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
					if(fileExtension.endsWith("avi") || fileExtension.endsWith("mp4")){//视频文件
						meetingFile.setFileType(1);
						meetingFile.setFileCover(path.append("/tysx/img/video.png").toString());
					}else if(fileExtension.endsWith("jpg") || fileExtension.endsWith("jpeg") || fileExtension.endsWith("png")){//图片
						meetingFile.setFileType(3);
					}else{//文档
						meetingFile.setFileType(2);
						if(fileExtension.endsWith("ppt") || fileExtension.endsWith("pptx")){
							meetingFile.setFileCover(path.append("/tysx/img/pptx.png").toString());
						}else if(fileExtension.endsWith("doc") || fileExtension.endsWith("docx")){
							meetingFile.setFileCover(path.append("/tysx/img/word.png").toString());
						}else if(fileExtension.endsWith("xls") || fileExtension.endsWith("xlsx")){
							meetingFile.setFileCover(path.append("/tysx/img/excel.png").toString());
						}
					}
					iLiveMeetingFileMng.saveILiveMeetingFileMng(meetingFile);
					if(fileExtension.endsWith("avi") || fileExtension.endsWith("mp4")){
						ILiveConvertTask iLiveConvertTask = iLiveConvertTaskMng.createConvertTask(meetingFile, accessMethod);
						iLiveConvertTaskMng.addConvertTask(iLiveConvertTask);
					}
					json.put("code", 0);
					json.put("msg", "文件上传成功");
					json.put("filePath", filePath);
					json.put("httpUrl", httpUrl);
					ResponseUtils.renderJson(response, json.toString());
				} else {
					json.put("code", 1);
					json.put("msg", "文件上传失败");
					ResponseUtils.renderJson(response, json.toString());
				}
			}
        }catch (FileNotFoundException e){
	        e.printStackTrace();
        }
    }
    @RequestMapping("/o_uploadFile_logo.do")
	public void uploadImgLogo(@RequestParam MultipartFile file, String fileType, Integer width, Integer height,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Integer serverGroupId,Long iLiveEventId) {
		JSONObject json = new JSONObject();
		try {
			if(!FileUtils.isAllowedFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1))) {
				json.put("status", -1);
				json.put("msg", "不支持的文件类型");
				ResponseUtils.renderJson(response, json.toString());
				return;
			}else {
				if (null != fileType && UPLOAD_FILE_TYPE_IMAGE.equalsIgnoreCase(fileType)) {
					String contentType = file.getContentType();
					if (contentType.indexOf("image") == -1) {
						json.put("status", 0);
						json.put("filePath", "");
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
					UserBean user = ILiveUtils.getUser(request);
					String fileName = file.getOriginalFilename();
					String tempFileName = System.currentTimeMillis() + "."
							+ fileName.substring(fileName.lastIndexOf(".") + 1);
					String realPath = request.getSession().getServletContext().getRealPath("/temp");
					File tempFile = createTempFile(realPath + "/" + tempFileName, file);
					
					BufferedImage bi = null;
			        try {
			        bi = ImageIO.read(tempFile);
			        } catch (Exception e) {
			        e.printStackTrace();
			        }
			        int widthCheck = bi.getWidth(); // 像素
			        int heightCheck = bi.getHeight(); // 像素
			        if (widthCheck<480&&heightCheck<360) {
						
					}else {
						json.put("status", 0);
						json.put("filePath", "");
						json.put("msg", "图片分辨率过大");
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
					String filePath = FileUtils.getTimeFilePath(tempFileName);
					String filePathPrefix = filePath.substring(0, filePath.lastIndexOf("/") + 1);
					String thumbFilePath = null;
					File generatorThumb = null;
					if (width != null && height != null) {
						generatorThumb = PictureUtils.INSTANCE.generatorThumb(tempFile, height, width);
						thumbFilePath = filePathPrefix + generatorThumb.getName();
					}
					ILiveUploadServer iLiveUploadServer = iLiveUploadServerMng.getDefaultServer();
					boolean result = false;
					if (iLiveUploadServer != null) {
						FileInputStream in = new FileInputStream(tempFile);
						result = iLiveUploadServer.upload(filePath, in);
						if (thumbFilePath != null) {
							result = iLiveUploadServer.upload(thumbFilePath, new FileInputStream(generatorThumb));
						}
					}
					if (tempFile.exists()) {
						tempFile.delete();
					}
					if (result) {
						if (thumbFilePath != null) {
							String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath()
									+ thumbFilePath;
							json.put("status", 1);
							json.put("filePath", filePath);
							json.put("httpUrl", httpUrl);
						} else {
							String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath() + filePath;
							json.put("status", 1);
							json.put("filePath", filePath);
							json.put("httpUrl", httpUrl);
						}
						//通知计费系统企业消耗
						EnterpriseUtil.openEnterprise(user.getEnterpriseId(),EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,file.getSize()/1024L+"",user.getCertStatus());
						ResponseUtils.renderJson(response, json.toString());
						return;
					} else {
						json.put("status", 0);
						json.put("filePath", filePath);
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
				} 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/o_uploadFile.do")
	public void upload(@RequestParam MultipartFile file, String fileType, Integer width, Integer height,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Integer serverGroupId,Long iLiveEventId) {
		JSONObject json = new JSONObject();
		try {
			if(!FileUtils.isAllowedFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1))) {
				json.put("status", -1);
				json.put("msg", "不支持的文件类型");
				ResponseUtils.renderJson(response, json.toString());
				return;
			}else {
				if (null != fileType && UPLOAD_FILE_TYPE_IMAGE.equalsIgnoreCase(fileType)) {
					String contentType = file.getContentType();
					if (contentType.indexOf("image") == -1) {
						json.put("status", 0);
						json.put("filePath", "");
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
					UserBean user = ILiveUtils.getUser(request);
					String fileName = file.getOriginalFilename();
					String tempFileName = System.currentTimeMillis() + "."
							+ fileName.substring(fileName.lastIndexOf(".") + 1);
					String realPath = request.getSession().getServletContext().getRealPath("/temp");
					File tempFile = createTempFile(realPath + "/" + tempFileName, file);
					String filePath = FileUtils.getTimeFilePath(tempFileName);
					String filePathPrefix = filePath.substring(0, filePath.lastIndexOf("/") + 1);
					String thumbFilePath = null;
					File generatorThumb = null;
					if (width != null && height != null) {
						generatorThumb = PictureUtils.INSTANCE.generatorThumb(tempFile, height, width);
						thumbFilePath = filePathPrefix + generatorThumb.getName();
					}
					ILiveUploadServer iLiveUploadServer = iLiveUploadServerMng.getDefaultServer();
					boolean result = false;
					if (iLiveUploadServer != null) {
						FileInputStream in = new FileInputStream(tempFile);
						result = iLiveUploadServer.upload(filePath, in);
						if (thumbFilePath != null) {
							result = iLiveUploadServer.upload(thumbFilePath, new FileInputStream(generatorThumb));
						}
					}
					if (tempFile.exists()) {
						tempFile.delete();
					}
					if (result) {
						if (thumbFilePath != null) {
							String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath()
									+ thumbFilePath;
							json.put("status", 1);
							json.put("filePath", filePath);
							json.put("httpUrl", httpUrl);
						} else {
							String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath() + filePath;
							json.put("status", 1);
							json.put("filePath", filePath);
							json.put("httpUrl", httpUrl);
						}
						//通知计费系统企业消耗
						EnterpriseUtil.openEnterprise(user.getEnterpriseId(),EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,file.getSize()/1024L+"",user.getCertStatus());
						ResponseUtils.renderJson(response, json.toString());
						return;
					} else {
						json.put("status", 0);
						json.put("filePath", filePath);
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
				} else if (null != fileType && Constants.UPLOAD_FILE_TYPE_VIDEO.equalsIgnoreCase(fileType)) {
					UserBean user = ILiveUtils.getUser(request);
					boolean ret=EnterpriseUtil.openEnterprise(user.getEnterpriseId(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,file.getSize()/1024L+"",user.getCertStatus());
					if(ret) {
						json.put("status", ERROR_STATUS);
						json.put("code", ERROR_STATUS);
						json.put("message", "无可用存储空间");
						json.put("data", new JSONObject());
						ResponseUtils.renderJson(request, response, json.toString());
						return;
					}
					String fileName = file.getOriginalFilename();
					String tempFileName = System.currentTimeMillis() + "."
							+ fileName.substring(fileName.lastIndexOf(".") + 1);
					String realPath = request.getSession().getServletContext().getRealPath("/temp");
					File tempFile = createTempFile(realPath + "/" + tempFileName, file);
					String filePath = FileUtils.getTimeFilePath(tempFileName);
					String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
					ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
					boolean result = false;
					if (accessMethod != null) {
						FileInputStream in = new FileInputStream(tempFile);
						result = accessMethod.uploadFile(filePath, in);
					}
					if (tempFile.exists()) {
						tempFile.delete();
					}
					if (result) {
						String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
								+ accessMethod.getMountInfo().getBase_path() + filePath;
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
				} else if (null != fileType && Constants.UPLOAD_FILE_TYPE_Excel.equalsIgnoreCase(fileType)) {
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
								memberMng.batchInsertUser(readXlsx, user,iLiveEventId);
								json.put("status", 1);
							}
							tempFile.delete();
						} catch (Exception e) {
							json.put("status", 0);
							json.put("msg", "第"+count+"行导入出错");
						}
						
						ResponseUtils.renderJson(response, json.toString());
					}
				}else if(null != fileType && fileType.equalsIgnoreCase("doc")){
					
					String fileName = file.getOriginalFilename();
					String tempFileName = System.currentTimeMillis() + "."
							+ fileName.substring(fileName.lastIndexOf(".") + 1);
					String realPath = request.getSession().getServletContext().getRealPath("/temp");
					File tempFile = createTempFile(realPath + "/" + tempFileName, file);
					String filePath = FileUtils.getTimeFilePath(tempFileName);
					String filePathPrefix = filePath.substring(0, filePath.lastIndexOf("/") + 1);
					String thumbFilePath = null;
					File generatorThumb = null;
					if (width != null && height != null) {
						generatorThumb = PictureUtils.INSTANCE.generatorThumb(tempFile, height, width);
						thumbFilePath = filePathPrefix + generatorThumb.getName();
					}
					ILiveUploadServer iLiveUploadServer = iLiveUploadServerMng.getDefaultServer();
					boolean result = false;
					if (iLiveUploadServer != null) {
						FileInputStream in = new FileInputStream(tempFile);
						result = iLiveUploadServer.upload(filePath, in);
						if (thumbFilePath != null) {
							result = iLiveUploadServer.upload(thumbFilePath, new FileInputStream(generatorThumb));
						}
					}
					if (tempFile.exists()) {
						tempFile.delete();
					}
					if (result) {
						if (thumbFilePath != null) {
							String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath()
									+ thumbFilePath;
							json.put("status", 1);
							json.put("filePath", filePath);
							json.put("httpUrl", httpUrl);
						} else {
							String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath() + filePath;
							json.put("status", 1);
							json.put("filePath", filePath);
							json.put("httpUrl", httpUrl);
						}
						//通知计费系统企业消耗
						UserBean user = ILiveUtils.getUser(request);
						EnterpriseUtil.openEnterprise(user.getEnterpriseId(),EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,file.getSize()/1024L+"",user.getCertStatus());
						ResponseUtils.renderJson(response, json.toString());
						return;
					} else {
						json.put("status", 0);
						json.put("filePath", filePath);
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 获取已上传的文件大小
     * @param request
     * @param response
     */
	@RequestMapping("/fileadd/getChunkedFileSize.do")
    public void getChunkedFileSize(HttpServletRequest request,HttpServletResponse response){
        //存储文件的路径，根据自己实际确定
        String currentFilePath = request.getSession().getServletContext().getRealPath("/temp");
        System.out.println(currentFilePath);
        PrintWriter print = null;
        try {
            request.setCharacterEncoding("utf-8");
            print = response.getWriter();
            String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
            String lastModifyTime = request.getParameter("lastModifyTime");
            File file = new File(currentFilePath+fileName+"."+lastModifyTime);
            if(file.exists()){
                print.print(file.length());
            }else{
                print.print(-1);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
	/**
     * 删除错误文件
     * @param request
     * @param response
     */
	@RequestMapping("/fileadd/deleteErrorFile.do")
    public void deleteErrorFile(HttpServletRequest request,HttpServletResponse response){
        //存储文件的路径，根据自己实际确定
        String currentFilePath = request.getSession().getServletContext().getRealPath("/temp");
        System.out.println(currentFilePath);
        PrintWriter print = null;
        try {
            request.setCharacterEncoding("utf-8");
            print = response.getWriter();
            String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
            String lastModifyTime = request.getParameter("lastModifyTime");
            File file = new File(currentFilePath+"/"+fileName+"."+lastModifyTime);
            if(file.exists()){
                file.delete();
            }else{
                print.print(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
	/**
     * 获取能否上传文件
     * @param request
     * @param response
     */
	@RequestMapping("/fileadd/getIfUpload.do")
    public void getIfUpload(Long fileSize,HttpServletRequest request,HttpServletResponse response){
		JSONObject resultJson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		Integer enterprisId=user.getEnterpriseId();
		Integer cerstus=user.getCertStatus();
		fileSize=fileSize/1024L;
		boolean ret=EnterpriseUtil.getStoreIfCan(enterprisId,EnterpriseUtil.ENTERPRISE_USE_TYPE_Store.toString(),cerstus,fileSize);
	
		if(!ret) {
			resultJson.put("status", 1);
			resultJson.put("code", 1);
			resultJson.put("message", "成功");
			
		}else {
			resultJson.put("status", ERROR_STATUS);
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "无可用存储空间");
		}
		resultJson.put("data", new JSONObject());
		ResponseUtils.renderJson(request, response, resultJson.toString());
		return;
        
    }
	/**
     * 
     * 断点文件上传 1.先判断断点文件是否存在 2.存在直接流上传 3.不存在直接新创建一个文件 4.上传完成以后设置文件名称
     *
     */
	@RequestMapping("/fileadd/appendUpload2Server.do")
    public  void appendUpload2Server(HttpServletRequest request,HttpServletResponse response) {
		 PrintWriter print = null;
	        try {
	            request.setCharacterEncoding("utf-8");
	            print = response.getWriter();
	            String fileSize = request.getParameter("fileSize");
	            long totalSize = Long.parseLong(fileSize) ;
	            RandomAccessFile randomAccessfile = null;
	            long currentFileLength = 0;// 记录当前文件大小，用于判断文件是否上传完成
	            String currentFilePath = request.getSession().getServletContext().getRealPath("/temp");// 记录当前文件的绝对路径
	            String fileName = new String(request.getParameter("fileName").getBytes("ISO-8859-1"),"UTF-8");
	            String lastModifyTime = request.getParameter("lastModifyTime");
	            File fileD = new File(currentFilePath+ "/" +fileName+"."+lastModifyTime);
	            // 存在文件
	            if(fileD.exists()){
	                randomAccessfile = new RandomAccessFile(fileD, "rw");
	            }
	             else {
	                // 不存在文件，根据文件标识创建文件
	                randomAccessfile = new RandomAccessFile(currentFilePath+ "/" +fileName+"."+lastModifyTime, "rw");
	            }
	                    // 开始文件传输
	                InputStream in = request.getInputStream();
	                randomAccessfile.seek(randomAccessfile.length());
	                byte b[] = new byte[1024];
	                int n;
	                while ((n = in.read(b)) != -1) {
	                    randomAccessfile.write(b, 0, n);
	                }

	            currentFileLength = randomAccessfile.length();

	            // 关闭文件
	            closeRandomAccessFile(randomAccessfile);
	            randomAccessfile = null;
	            
	            
	            print.print(currentFileLength);
	            // 整个文件上传完成,修改文件后缀
	            if (currentFileLength == totalSize) {
	                    File oldFile = new File(currentFilePath+ "/" +fileName+"."+lastModifyTime);
	                    File newFile = new File(currentFilePath+ "/" +fileName);
	                    if(!oldFile.exists()){
	                        return;//重命名文件不存在
	                    }
	                    if(newFile.exists()){// 如果存在形如test.txt的文件，则新的文件存储为test+当前时间戳.txt, 没处理不带扩展名的文件 
	                        String newName = fileName.substring(0,fileName.lastIndexOf("."))
	                                +System.currentTimeMillis()+"."
	                                +fileName.substring(fileName.lastIndexOf(".")+1);
	                        newFile = new File(currentFilePath+ "/" +newName);
	                    }
	                    if(!oldFile.renameTo(newFile)){
	                        oldFile.delete();
	                    }
	                    String filePath = FileUtils.getTimeFilePath(fileName);
						String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
						ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
								.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
						boolean result = false;
						if (accessMethod != null) {
							FileInputStream In = new FileInputStream(newFile);
							result = accessMethod.uploadFile(filePath, In);
						}
						if (newFile.exists()) {
							newFile.delete();
						}
						if (result) {
							
							Long fileId = 0L;
							UserBean userBean = ILiveUtils.getUser(request);
							ILiveMediaFile selectFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
							if (selectFile != null) {
								selectFile.setFilePath(filePath);
								selectFile.setMediaInfoDealState(0);
								selectFile.setMediaInfoStatus(0);
								selectFile.setUserName(userBean.getUsername());
								selectFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
								iLiveMediaFileMng.updateMediaFile(selectFile);
							} else {
								selectFile = new ILiveMediaFile();
								selectFile.setMediaFileName(fileName.substring(0, fileName.lastIndexOf(".")));
								selectFile.setCreateStartTime(new Timestamp(System.currentTimeMillis()));
								selectFile.setCreateType(3);
								selectFile.setFileType(1);
								selectFile.setOnlineFlag(1);
								selectFile.setDelFlag(0);
								selectFile.setDuration(0);
								selectFile.setFileRate(0D);
								selectFile.setWidthHeight("");
								selectFile.setFileSize(0L);
								selectFile.setUserName(userBean.getUsername());
								selectFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
								selectFile.setCreateEndTime(new Timestamp(new Date().getTime()));
								UserBean user = ILiveUtils.getUser(request);
								selectFile.setEnterpriseId((long) user.getEnterpriseId());
								selectFile.setMediaCreateTime(selectFile.getCreateStartTime());
								selectFile.setAllowComment(1);
								selectFile.setFilePath(filePath);
								selectFile.setUserId(Long.parseLong(user.getUserId()));
								selectFile.setMediaInfoStatus(0);
								selectFile.setMediaInfoDealState(0);
								iLiveMediaFileMng.saveIliveMediaFile(selectFile);
							}
							String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
									+ accessMethod.getMountInfo().getBase_path() + filePath;
							/*json.put("status", 1);
							json.put("filePath", filePath);
							json.put("httpUrl", httpUrl);
							ResponseUtils.renderJson(response, json.toString());*/
							
							//上传文件以后调用转码服务进行转码
							ILiveConvertTask iLiveConvertTask = iLiveConvertTaskMng.createConvertTask(selectFile, accessMethod);
							iLiveConvertTaskMng.addConvertTask(iLiveConvertTask);
							UserBean user = ILiveUtils.getUser(request);
							//workLogMng.save(new WorkLog(WorkLog.MODEL_UPLOADVIDEO, iliveRoom.getRoomId()+"", net.sf.json.JSONObject.fromObject(iliveRoom).toString(), WorkLog.MODEL_UPLOADVIDEO_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
							//将相应的内存占用通知计费系统
//							EnterpriseUtil.openEnterprise(user.getEnterpriseId(),EnterpriseUtil.ENTERPRISE_USE_TYPE_Store,totalSize/1024L+"",user.getCertStatus());
							return;
						} else {
							/*json.put("status", 0);
							json.put("filePath", filePath);
							ResponseUtils.renderJson(response, json.toString());*/
							return;
						}
	                    
	                     
	            }
	            
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }

	@RequestMapping("/fileadd/o_uploadFile.do")
	public void uploadAndReplace(@RequestParam MultipartFile file, Long fileId, String fileType, Integer width,
			Integer height,Boolean vrFile, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		JSONObject json = new JSONObject();
		try {
			if (null != fileType && Constants.UPLOAD_FILE_TYPE_VIDEO.equalsIgnoreCase(fileType)) {
				
					String contentType = file.getContentType();
					if (contentType.indexOf("video") == -1) {
						json.put("status", 0);
						json.put("filePath", "");
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
					
					String fileName = file.getOriginalFilename();
					String tempFileName = System.currentTimeMillis() + "."
							+ fileName.substring(fileName.lastIndexOf(".") + 1);
					String realPath = request.getSession().getServletContext().getRealPath("/temp");
					File tempFile = createTempFile(realPath + "/" + tempFileName, file);
					String filePath = FileUtils.getTimeFilePath(tempFileName);
					String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
					ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
					boolean result = false;
					if (accessMethod != null) {
						FileInputStream in = new FileInputStream(tempFile);
						result = accessMethod.uploadFile(filePath, in);
					}
					if (tempFile.exists()) {
						tempFile.delete();
					}
					if (result) {
						if (fileId == null) {
							fileId = 0L;
						}
						UserBean userBean = ILiveUtils.getUser(request);
						ILiveMediaFile selectFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(fileId);
						if (selectFile != null) {
							selectFile.setFilePath(filePath);
							selectFile.setMediaInfoDealState(0);
							selectFile.setMediaInfoStatus(0);
							selectFile.setUserName(userBean.getUsername());
							selectFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
							iLiveMediaFileMng.updateMediaFile(selectFile);
						} else {
							selectFile = new ILiveMediaFile();
							selectFile.setMediaFileName(fileName.substring(0, fileName.lastIndexOf(".")));
							selectFile.setCreateStartTime(new Timestamp(System.currentTimeMillis()));
							selectFile.setCreateType(3);
							selectFile.setFileType(1);
							selectFile.setOnlineFlag(1);
							selectFile.setDelFlag(0);
							selectFile.setDuration(0);
							selectFile.setFileRate(0D);
							selectFile.setWidthHeight("");
							selectFile.setFileSize(0L);
							selectFile.setUserName(userBean.getUsername());
							selectFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
							selectFile.setCreateEndTime(new Timestamp(new Date().getTime()));
							UserBean user = ILiveUtils.getUser(request);
							selectFile.setEnterpriseId((long) user.getEnterpriseId());
							selectFile.setMediaCreateTime(selectFile.getCreateStartTime());
							selectFile.setAllowComment(1);
							selectFile.setFilePath(filePath);
							selectFile.setUserId(Long.parseLong(user.getUserId()));
							selectFile.setMediaInfoStatus(0);
							selectFile.setMediaInfoDealState(0);
							selectFile.setVrFile(vrFile);
							iLiveMediaFileMng.saveIliveMediaFile(selectFile);
						}
						String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
								+ accessMethod.getMountInfo().getBase_path() + filePath;
						json.put("status", 1);
						json.put("filePath", filePath);
						json.put("httpUrl", httpUrl);
						ResponseUtils.renderJson(response, json.toString());
						
						//上传文件以后调用转码服务进行转码
						ILiveConvertTask iLiveConvertTask = iLiveConvertTaskMng.createConvertTask(selectFile, accessMethod);
						iLiveConvertTaskMng.addConvertTask(iLiveConvertTask);
						UserBean user = ILiveUtils.getUser(request);
						//workLogMng.save(new WorkLog(WorkLog.MODEL_UPLOADVIDEO, iliveRoom.getRoomId()+"", net.sf.json.JSONObject.fromObject(iliveRoom).toString(), WorkLog.MODEL_UPLOADVIDEO_NAME, Long.parseLong(user.getUserId()), user.getUsername(), ""));
						
						return;
					} else {
						json.put("status", 0);
						json.put("filePath", filePath);
						ResponseUtils.renderJson(response, json.toString());
						return;
					}
				
				
				
			} else if (null != fileType && Constants.UPLOAD_FILE_TYPE_Excel.equalsIgnoreCase(fileType)) {
				String fileName = file.getOriginalFilename();
				String tempFileName = System.currentTimeMillis() + "."
						+ fileName.substring(fileName.lastIndexOf(".") + 1);
				String realPath = request.getSession().getServletContext().getRealPath("/temp");
				File tempFile = createTempFile(realPath + "/" + tempFileName, file);
				if (fileName.endsWith("xls")) {
					this.readXls(new FileInputStream(tempFile));
				} else {
					try {
						List<Object[]> readXlsx = this.readXlsx(tempFile.getAbsolutePath());
						UserBean user = ILiveUtils.getUser(request);
						memberMng.batchInsertUser(readXlsx, user,null);
						tempFile.delete();
						json.put("status", 1);
					} catch (Exception e) {
						json.put("status", 0);
					}
					ResponseUtils.renderJson(response, json.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
     * 关闭随机访问文件
     * 
     * @param rfile
     */
    public static void closeRandomAccessFile(RandomAccessFile rfile) {
        if (null != rfile) {
            try {
                rfile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

	@ResponseBody
	@RequestMapping(value = "/o_uploadImgFile.do",produces = MediaType.TEXT_PLAIN_VALUE +";charset=utf-8")
	public String uploadImg(@RequestParam MultipartFile imgFile, String fileType, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Integer serverGroupId) {
		JSONObject json = new JSONObject();
		try {
			String fileName = imgFile.getOriginalFilename();
			String tempFileName = System.currentTimeMillis() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
			String realPath = request.getSession().getServletContext().getRealPath("/temp");
			File tempFile = createTempFile(realPath + "/" + tempFileName, imgFile);
			String filePath = FileUtils.getTimeFilePath(tempFileName);
			ILiveUploadServer iLiveUploadServer = iLiveUploadServerMng.getDefaultServer();
			boolean result = false;
			if (iLiveUploadServer != null) {
				FileInputStream in = new FileInputStream(tempFile);
				result = iLiveUploadServer.upload(filePath, in);
			}
			if (tempFile.exists()) {
				tempFile.delete();
			}
			if (result) {
				String httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath() + filePath;
				json.put("error", 0);
				//json.put("filePath", filePath);
				json.put("url", httpUrl);
				//ResponseUtils.renderJson(response, json.toString());
				return json.toString();
			} else {
				json.put("error", 1);
				json.put("message", filePath);
				//json.put("filePath", filePath);
				//ResponseUtils.renderJson(response, json.toString());
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@RequestMapping(value = "/upload_slime.do")
	public void uploadSlimeFile(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response, String fileType,Integer roomId) {
		JSONObject json = new JSONObject();
		try {
			if (null != fileType && Constants.UPLOAD_FILE_TYPE_VIDEO.equalsIgnoreCase(fileType)) {
				String contentType = file.getContentType();
				if (contentType.indexOf("video") == -1) {
					json.put("status", 0);
					json.put("filePath", "");
					ResponseUtils.renderJson(response, json.toString());
					return;
				}
				String fileName = file.getOriginalFilename();
				String tempFileName = System.currentTimeMillis() + "."
						+ fileName.substring(fileName.lastIndexOf(".") + 1);
				String realPath = request.getSession().getServletContext().getRealPath("/temp");
				File tempFile = createTempFile(realPath + "/" + tempFileName, file);
				String filePath = FileUtils.getTimeFilePath(tempFileName);
				//String defaultVodServerGroupId = ConfigUtils.get("defaultLiveVodServerGroupId");
				String defaultVodServerGroupId = ConfigUtils.get("defaultLiveServerGroupId");
				ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
						.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
				
				//获取配置的ftp信息
				accessMethod.setFtp_address(ConfigUtils.get("backupvideo_ftp_address"));
				accessMethod.setFtp_user(ConfigUtils.get("backupvideo_ftp_username"));
				accessMethod.setFtp_pwd(ConfigUtils.get("backupvideo_ftp_password"));
				accessMethod.setFtpPort(Integer.valueOf(ConfigUtils.get("backupvideo_ftp_port")));
				accessMethod.getMountInfo().setFtp_path(ConfigUtils.get("backupvideo_ftppath"));
				
				
				
				boolean result = false;
				if (accessMethod != null) {
					FileInputStream in = new FileInputStream(tempFile);
					result = accessMethod.uploadFile(filePath, in);
				}
				if (tempFile.exists()) {
					tempFile.delete();
				}
				UserBean userBean = ILiveUtils.getUser(request);
				if (result) {
					String basePath = ConfigUtils.get("backupvideo_ftp_basepath");
//					String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
//							+ accessMethod.getMountInfo().getBase_path() + filePath;
					//String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
						//	+ basePath+ filePath;
					String httpUrl = "http://" + accessMethod.getOrgLiveHttpUrl() + ":" + accessMethod.getUmsport()
						+ basePath+ filePath;
					System.out.println(accessMethod.getHttp_address());
					System.out.println(accessMethod.getUmsport());
					System.out.println(accessMethod.getMountInfo().getBase_path());
					System.out.println(filePath);
					ILiveMediaFile selectFile = new ILiveMediaFile();
					selectFile.setMediaFileName(fileName);
					selectFile.setCreateStartTime(new Timestamp(System.currentTimeMillis()));
					selectFile.setCreateType(4);
					selectFile.setFileType(1);
					selectFile.setOnlineFlag(0);
					selectFile.setDelFlag(0);
					selectFile.setDuration(0);
					selectFile.setFileRate(0D);
					selectFile.setWidthHeight("");
					selectFile.setFileSize(0L);
					selectFile.setUserName(userBean.getUsername());
					selectFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
					selectFile.setCreateEndTime(new Timestamp(new Date().getTime()));
					UserBean user = ILiveUtils.getUser(request);
					selectFile.setEnterpriseId((long) user.getEnterpriseId());
					selectFile.setMediaCreateTime(selectFile.getCreateStartTime());
					selectFile.setAllowComment(1);
					selectFile.setFilePath(filePath);
					selectFile.setUserId(Long.parseLong(user.getUserId()));
					selectFile.setMediaInfoStatus(0);
					selectFile.setMediaInfoDealState(0);
					Long saveIliveMediaFileId = iLiveMediaFileMng.saveIliveMediaFile(selectFile);
					
					
					json.put("status", 1);
					json.put("filePath", filePath);
					json.put("httpUrl", httpUrl);
					System.out.println(httpUrl);
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
		log.info("创建临时上传文件耗时：{} ms", end - start);
		return tempFile;
	}

}
