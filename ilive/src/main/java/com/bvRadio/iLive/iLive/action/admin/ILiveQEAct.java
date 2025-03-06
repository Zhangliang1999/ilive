package com.bvRadio.iLive.iLive.action.admin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.common.web.ResponseUtilsForC;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.constants.QEConstant;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.MountInfo;
import com.bvRadio.iLive.iLive.entity.ProgressStatusPoJo;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.QEMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.jwzt.common.AppTools;
import com.jwzt.common.DownloadURL;
import com.jwzt.common.Md5;
import net.sf.json.JSONObject;


@Controller
public class ILiveQEAct {
	/**
	 * 日志记录
	 */
	private static final Logger log = LoggerFactory.getLogger(ILiveQEAct.class);
	public static  Map<String,String> statusLocalStorage = new HashMap<String, String>();
	
	public static Map<String,UserBean> QEUserList = new HashMap<String, UserBean>();
	
	public static int nSeq = 1;

	
	@RequestMapping(value = "/cms/vod/servlet/ServiceXml")
	public void submitEditXmlServlet(HttpServletRequest request, HttpServletResponse response , String sNewsTitle , ModelMap map)
			throws IOException {
		
		try {
			String newsTitle = new String(sNewsTitle.getBytes("iso-8859-1"),"UTF-8");
			StringBuffer sRetMsg= new StringBuffer(1024);
		        sRetMsg.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n");
		        sRetMsg.append("<root><cms>\r\n");
		        sRetMsg.append("<newsHostNodeId>");
		        sRetMsg.append(0);
		        sRetMsg.append("</newsHostNodeId>\r\n");
		        
		        sRetMsg.append("<node_name>");
		        sRetMsg.append("");
		        sRetMsg.append("</node_name>\r\n");
		        
		        sRetMsg.append("<newsTitle>");
		        sRetMsg.append(newsTitle);
		        sRetMsg.append("</newsTitle>\r\n");
		        
		        sRetMsg.append("<newsPreTitle>");
		        sRetMsg.append("");
		        sRetMsg.append("</newsPreTitle>\r\n");
		        
		        sRetMsg.append("<newsSubTitle>");
		        sRetMsg.append("");
		        sRetMsg.append("</newsSubTitle>\r\n");
		        
		        sRetMsg.append("<newsAbstract>");
		        sRetMsg.append("");
		        sRetMsg.append("</newsAbstract>\r\n");
		       
		        sRetMsg.append("<newsContent>");
		        sRetMsg.append("");
		        sRetMsg.append("</newsContent>\r\n");
		        
		        sRetMsg.append("<SourceType>");
		        sRetMsg.append("");
		        sRetMsg.append("</SourceType>\r\n");
		        
		        sRetMsg.append("<SourceContent>");
		        sRetMsg.append("");
		        sRetMsg.append("</SourceContent>\r\n");
		        
		        sRetMsg.append("<isForce>");
		        sRetMsg.append("");
		        sRetMsg.append("</isForce>\r\n");
		        
		        sRetMsg.append("</cms></root>");
			
		        System.out.println(sRetMsg.toString());
				ResponseUtilsForC.renderXml(response, sRetMsg.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	@RequestMapping(value = "/EditXmlServlet")
	public String editXmlServlet(HttpServletRequest request, HttpServletResponse response , ModelMap map , String title)
			throws IOException {
		
		try {
			/**
			 * 请求登陆接口
			 */
			Document doc = null;
			doc = new SAXReader().read(new InputStreamReader(request.getInputStream(),"GBK"));
			System.out.println("doc:"+doc);
            if(doc!=null){
            	Element el=doc.getRootElement().element("cms");
            	String xmltitle = el.element("newsTitle").getText();
            	xmltitle = xmltitle.trim();
            	System.out.println("title:"+xmltitle);
            	map.addAttribute("title", xmltitle);
			}
			
		} catch (DocumentException e) {
			title = new String(title.getBytes("iso-8859-1"),"GBK");
			map.addAttribute("title", title);
			e.printStackTrace();
		}
		
		return "filelib/edit_video";
	}
	
	
	@RequestMapping(value = "/quickEditProgressQueryServlet")
	public void quickEditProgressQueryServlet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
				//进度跟踪流程,跟踪标识------------->taskUUID 标记一次任务
				String taskUUID = request.getParameter("taskUUID");
				System.out.println(request.getRequestURL());
				System.out.println("taskUUID:"+taskUUID);
				String retStr = "";
				String retStrStatus = "0";
				
				if(taskUUID != null) {
					//若是任務Id存在，现从本地缓存中获取，若不能取到，再从数据库中获取，
					retStr = statusLocalStorage.get(taskUUID);
					if(retStr == null) {
						//不存在的话，去库中取值
						ProgressStatusPoJo statusPoJo = qeMng.getUpdateTask(taskUUID);
						//库里再没有，说明确实是文件下载中，还没有存入
						if(statusPoJo==null) {
							retStr = "文件下载中" ;
							retStrStatus = "4";
						}else{
							retStrStatus = String.valueOf(statusPoJo.getStatusCode());
							if("1".equals(retStrStatus)){
								retStr = "完成" ;
							}
						}
						
						
					}else{
						if("".equals(retStr)) {
							retStr = "任务失败";
							retStrStatus = "1";
						}else{
							//存在如果成功则开始访问远程
							String json = getTaskInfoXml(taskUUID);
							if(null==json || "".equals(json)) {
								retStr = "后台异常";
							}

							JSONObject jsStr =  new JSONObject().fromObject(json);
							
							retStr = jsStr.getString("statusMsg");
							retStrStatus = jsStr.getString("status");
						}
					}
				}else{
					retStr = "taskUUID not exist ";
				}
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
				sb.append("<root>");
				sb.append("<isFinished>");
				sb.append(retStrStatus);
				sb.append("</isFinished>");
				sb.append("<statusMsg>");
				sb.append(retStr);
				sb.append("</statusMsg>");
				sb.append("</root>");
				//String xml = this.makeRetXml(retStr,retStrStatus);
				System.out.println(sb.toString());
				ResponseUtilsForC.renderXml(response, sb.toString());
			
	}
	
	
	@RequestMapping(value = "/QEGetUUIDServlet")
	public void getUUID(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
			String ret = "{\"uuid\":\"error\",\"auth\":\"error\"}";
			UserBean user = ILiveUtils.getUser(request);
			if( user != null){
				String uuid = UUID.randomUUID().toString();
				QEUserList.put(uuid, user);
				ret = "{\"uuid\":\""+uuid+"\",\"auth\":\""+getAuthString()+"\"}";
			}
			ResponseUtils.renderJson(response, ret);
			
	}
	
	@RequestMapping(value = "/QEPlayerServlet")
	public String playUUID(HttpServletRequest request, HttpServletResponse response , ModelMap map)
			throws IOException {
		
			String taskUUID = request.getParameter("taskUUID");
			System.out.println("taskUUID:"+taskUUID);
			ProgressStatusPoJo statusPoJo = qeMng.getUpdateTask(taskUUID);
			ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(statusPoJo.targetFileId);
			
			if (iLiveMediaFile != null) {
				Integer serverMountId = iLiveMediaFile.getServerMountId();
				ILiveServerAccessMethod serverAccess = accessMethodMng.getAccessMethodByMountId(serverMountId);
				MountInfo mountInfo = serverAccess.getMountInfo();
				String allPath = "http://" + serverAccess.getHttp_address() + ":" + serverAccess.getUmsport()
						+ mountInfo.getBase_path() + iLiveMediaFile.getFilePath();
				map.addAttribute("pushStreamAddr", allPath);
				
			}
			
			return "filelib/play_video";
			
	}
	
	
	@RequestMapping(value = "/QEOpenServlet")
	public void login(HttpServletRequest request, HttpServletResponse response , String auth , String uuid)
			throws IOException {
		
			UserBean user = QEUserList.get(uuid);

			String susername = user.getUserId();
			String spassword = uuid;
			String sipaddr = request.getServerName();
			String sport = String.valueOf(request.getServerPort());
			String sserverurl = request.getContextPath()+"/servlet/QELoginServlet";

			String ret = qeMng.makeServerXml(susername , spassword , sipaddr , sport , sserverurl);
			System.out.println(ret);
			ResponseUtilsForC.renderXml(response, ret);
		
	}
	
	
	@RequestMapping(value = "/QELoginServlet")
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
			
		String retXml = qeMng.makeFailedXml("login" , "请求错误");
		try
		{
			/*
			in = request.getInputStream();
			BufferedReader tBufferedReader =  new BufferedReader(new InputStreamReader(in,"GBK"));
			StringBuffer tStringBuffer = new StringBuffer();
			String sTempOneLine = new String("");
			while ((sTempOneLine = tBufferedReader.readLine()) != null){
				System.out.println("读取到数据："+ sTempOneLine);
				tStringBuffer.append(sTempOneLine);
			}
			ResponseUtils.renderXml(response, tStringBuffer.toString());
			*/
			Document doc = null;
			try {
				/**
				 * 请求登陆接口
				 */
				doc = new SAXReader().read(request.getInputStream());
			} catch (DocumentException e) {
				e.printStackTrace();
			}

			/**
			 * 解析xml 
			 */
			// 获取根元素
	        Element root = doc.getRootElement();
	        // 获取特定名称的子元素
	        String requestType = root.element("requestType").getTextTrim();
	        if(requestType.equals("login")){
	        	Element param = root.element("param");
	            //String userName = param.element("userName").getTextTrim();
	            String passWord = param.element("passWord").getTextTrim();
	            UserBean user = QEUserList.get(passWord);
	            if(user != null){
	            	String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	    			// 构建返回断点续传接口地址
	    			String uploadUrl = baseUrl + "/servlet/QEUploadServlet";
	    			// 构建本地上传路径路径
	    			String uploadPath = "/" +user.getUserId();
	    			// 构建文件列表接口
	    			String catalogueUrl = baseUrl + "/servlet/QESelectFile?uuid="+passWord;
	    			//	构建任务进行中任务进度查询接口
	    			String progressUrl = baseUrl + "/servlet/quickEditProgressQueryServlet?taskUUID=";
	    			// 返回任务完成后 查看输出时调用的视频播放界面
	    			String vodPlayUrl = baseUrl  + "/servlet/QEPlayerServlet?taskUUID=";
	    			//新闻栏目接口
	    			String newsEditURL = baseUrl + "/servlet/EditXmlServlet?function=show_news_editer&uuid=";
	    			//构建视频栏目接口
	    			String vodEditURL = baseUrl + "/servlet/QESelectFile";
	    			//把参数封装起来以xml的形式返回给C端工具
	    			retXml = qeMng.makeLoginXml(uploadUrl, uploadPath, catalogueUrl, newsEditURL, vodEditURL,progressUrl,vodPlayUrl);
	    			//request.getSession().setAttribute("QEUserInfo", iLiveManager);
	    			//QEUserList.put(uuid, iLiveManager);
	            }else{
	            	retXml = qeMng.makeFailedXml("login","用户名密码错误");
	            }
 
	        }

		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(retXml);
		ResponseUtilsForC.renderXml(response, retXml);
		
	}
	
	
	@RequestMapping(value = "/QEUploadServlet")
	public void upload(HttpServletRequest request, HttpServletResponse response){
		
		String ret = "";
		String rootPath = ConfigUtils.get("quickeditor_path");
		/**
		 * 本次请求的类型
		 */
		String requestType = request.getParameter("requestType");
		String Path = request.getParameter("Path");
		System.out.println("requestType:"+requestType);
		
		
		/**
		 * 文件上传的起始位置
		 */
		Integer FileBeginPos = 0;
		try {
			String FileBeginPosStr = request.getParameter("FileBeginPos");
			FileBeginPos = Integer.parseInt(FileBeginPosStr);
		} catch (NumberFormatException e1) {
			// e1.printStackTrace();
		}
		if (requestType != null) {
			if (requestType.equals("UploadFile")) {
				try{
					if (Path != null) {
						String realPath = rootPath + Path;
						System.out.println("临时文件保存路径："+realPath);
						File file = new File(realPath);
						if (!file.exists()) {
							file.createNewFile();
							InputStream in = request.getInputStream();
							FileOutputStream fos = new FileOutputStream(file, true);
							int ch = 0;
							byte[] b = new byte[1024];
							while ((ch = in.read(b)) != -1) {
								fos.write(b, 0, ch);
							}
							in.close();
							fos.flush();
							fos.close();
							long newFileSize = file.length();
							ret = qeMng.makeUploadFileSuccXml(newFileSize);
						} else {
							long fileSize = file.length();
							if (fileSize == 0) {
								file.delete();
								file.createNewFile();
								InputStream in = request.getInputStream();
								FileOutputStream fos = new FileOutputStream(file,
										true);
								int ch = 0;
								byte[] b = new byte[1024];
								while ((ch = in.read(b)) != -1) {
									fos.write(b, 0, ch);
								}
								long newFileSize = file.length();
								in.close();
								fos.flush();
								fos.close();
								ret = qeMng.makeUploadFileSuccXml(newFileSize);
							}
							if (fileSize == FileBeginPos) {
								InputStream in = request.getInputStream();
								FileOutputStream fos = new FileOutputStream(file,
										true);
								int ch = 0;
								byte[] b = new byte[1024];
								while ((ch = in.read(b)) != -1) {
									fos.write(b, 0, ch);
								}
								long newFileSize = file.length();
								ret = qeMng.makeUploadFileSuccXml(newFileSize);
							} else {
								ret = qeMng.makeFailedXml(requestType,
										"文件开始位置错误，请确认后上传");
							}
						}
					} else {
						ret = qeMng.makeFailedXml(requestType, "请确认文件路径存在");

					}
				}catch(Exception e){
					ret = qeMng.makeFailedXml(requestType, "请确认文件路径存在");
					e.printStackTrace();
				}
				

			}
		} else {
			Document doc = null;
			try {
				SAXReader sAXReader = new SAXReader();
				//sAXReader.setEncoding("GBK");
				Reader read = new InputStreamReader(request.getInputStream(),"GBK");
				doc = sAXReader.read(read);
			} catch (Exception e) {
				e.printStackTrace();
			}// 接受xml
			Element root = doc.getRootElement();
			String requestTypes = root.element("requestType").getTextTrim();
			System.out.println("requestType2:"+requestTypes);
			if (requestTypes.equals("GetFileSize")) {
				// 获取特定名称的子元素
				Element param = root.element("param");
				String sizeFilePath = param.element("Path").getTextTrim();
				String realDir = "";
				realDir = rootPath
						+ sizeFilePath.substring(0, sizeFilePath
								.lastIndexOf("/"));
				File dir = new File(realDir);
				// 如果文件夹不存在则创建
				if (!dir.exists() && !dir.isDirectory()) {
					dir.mkdirs();
				}
				String realPath = rootPath + sizeFilePath;
				File file = new File(realPath);
				if (!file.exists()) {
					ret = qeMng.makeFileSizeSuccXml(0);
				} else {
					long fileSize = file.length();
					ret = qeMng.makeFileSizeSuccXml(fileSize);
				}
			} else if (requestTypes.equals("FinishNotify")) {
				// 获取特定名称的子元素
				Element param = root.element("param");
				String finishFilePath = param.element("Path").getTextTrim();
				String fileTotSize = param.element("FileTotSize").getTextTrim();
				String realPath = rootPath + finishFilePath;
				File file = new File(realPath);
				if (!file.exists()) {
					ret = qeMng.makeFailedXml(requestTypes, "文件错误，上传失败");
				} else {
					long fileSize = file.length();
					if (fileSize == Long.parseLong(fileTotSize)) {
						ret = qeMng.makeFinishNotifySuccXml(fileSize);
					} else {
						ret = qeMng.makeFailedXml(requestTypes,
								"文件错误，上传失败");
					}
				}
			} else if (requestTypes.equals("uploadNotify")) {
				/*
				 * <userName></userName> <passWord></passWord> <!--md5 加密 -->
				 * <httpUploadURL>/<httpUploadURL> <httpUploadPath>/<httpUploadPath>
				 * <TaskUUID>uuid.xml</ TaskUUID ><!--上传成功的UUID,为UUID文件名-->
				 */
				// 获取特定名称的子元素
				
				
				
				Element param = root.element("param");
				String userName = param.element("userName").getTextTrim();
				String passWord = param.element("passWord").getTextTrim();
				String TaskUUID = param.element("TaskUUID").getTextTrim();
				
				System.out.println("任务完成收到的UUID是："+TaskUUID);
				
				UserBean user = QEUserList.get(passWord);
				
            
				// 首先需要判断任务是否存在
				ProgressStatusPoJo statusPoJo = qeMng.getUpdateTask(TaskUUID);
				if (statusPoJo == null) {
					
					//创建一个快编后的新的文件
					String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
					ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
							.getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
					String tempFileName = System.currentTimeMillis() + ".mp4";
					String filePath = FileUtils.getTimeFilePath(tempFileName);
					
					
					ILiveMediaFile newFile = new ILiveMediaFile();
					newFile.setMediaFileName("快编处理中");
					newFile.setCreateStartTime(new Timestamp(System.currentTimeMillis()));
					newFile.setCreateType(1);
					newFile.setFileType(1);
					newFile.setOnlineFlag(0);
					newFile.setDelFlag(0);
					newFile.setDuration(0);
					newFile.setFileRate(0D);
					newFile.setWidthHeight("");
					newFile.setFileSize(0L);
					newFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
					newFile.setCreateEndTime(new Timestamp(new Date().getTime()));
					newFile.setEnterpriseId(Long.valueOf(user.getEnterpriseId().toString()));
					newFile.setMediaCreateTime(newFile.getCreateStartTime());
					newFile.setAllowComment(1);
					newFile.setFilePath(filePath);
					newFile.setUserId(Long.valueOf(user.getUserId()));
					newFile.setMediaInfoStatus(1);
					newFile.setMediaInfoDealState(1);
							
					iLiveMediaFileMng.saveIliveMediaFile(newFile);
					
					statusPoJo = new ProgressStatusPoJo();
					statusPoJo.setTaskUUID(TaskUUID);
					statusPoJo.setStatusDesc( QEConstant.UPLOAD_FILE_QUICKEDIT);
					statusPoJo.setTargetFileId(newFile.getFileId());
					String path = accessMethod.getMountInfo().getFtp_path()+newFile.getFilePath();
					path = path.replaceAll("//", "/");
					statusPoJo.setTargetFtpPath(path);
					statusPoJo.setTargetFtpIp(accessMethod.getFtp_address_inner());
					statusPoJo.setTargetFtpPort(accessMethod.getFtpPort().toString());
					statusPoJo.setTargetFtpUsername(accessMethod.getFtp_user());
					statusPoJo.setTargetFtppassword(accessMethod.getFtp_pwd());
					
					Long addUpdateRet = qeMng.addUpdateTask(statusPoJo);
					
					System.out.println("生成的任务ID："+addUpdateRet);
					
					if (addUpdateRet>0) {
						//发送任务
						System.out.println("开始向快编服务发送任务：");
						createQuickNotifyXml(TaskUUID , userName);
						
						try {
							statusLocalStorage.put(TaskUUID,QEConstant.UPLOAD_FILE_QUICKEDIT);
							ret = qeMng.makeUploadNotifySuccXml();
						} catch (Exception e) {
							statusLocalStorage.put(TaskUUID, "");
							ret = qeMng.makeFailedXml("login","发送合成请求失败");
							e.printStackTrace();
						}
					} else {
						statusLocalStorage.put(	TaskUUID, "");
						ret = qeMng.makeFailedXml("login","记录存入数据库时发生异常");
					}
				} else {
					// 针对已经存在的任务，需要甄别下它目前是否被合成服务器合成中,若不在合成服务其中，则需要重新合成
					ret = qeMng.makeUploadNotifySuccXml();
				}
			} else if (requestTypes.equals("UploadFile_Merge")) {
				/**
				 * 合成多片文件
				 */
				// 获取xml信息
				try{
					Element param = root.element("param");
					String mergeFilePath = param.element("Path").getTextTrim();
					Integer partNum = Integer.parseInt(param.element("PartNum")
							.getTextTrim());
					String fileTotSize = param.element("FileTotSize").getTextTrim();
					String fileMD5 = param.element("FileMD5").getTextTrim();
					ArrayList<File> fileList = new ArrayList<File>();
					for (int i = 1; i <= partNum; i++) {
						if (!mergeFilePath.startsWith("/"))
							mergeFilePath = "/" + mergeFilePath;
						String tempFileName = rootPath + mergeFilePath;
						tempFileName = tempFileName.substring(0, tempFileName
								.indexOf("."))
								+ "_bvblock"
								+ i
								+ tempFileName.substring(tempFileName.indexOf("."));
						File file = new File(tempFileName);
						if (file.exists()) {
							fileList.add(file);
						} else {
							// 返回错误信息
							ret = qeMng.makeFailedXml(requestTypes,
									"文件错误，合并失败");
						}
					}
					String realPath = rootPath + mergeFilePath;
					File newFile = new File(realPath);
					if (!newFile.exists()) {
						newFile.createNewFile();
					} else {
						newFile.delete();
						newFile.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(newFile, true);
					for (File file : fileList) {

						FileInputStream fis = new FileInputStream(file);
						int ch = 0;
						byte[] b = new byte[1024];
						while ((ch = fis.read(b)) != -1) {
							fos.write(b, 0, ch);
						}
						fis.close();
						file.delete();

					}
					fos.flush();
					fos.close();
					long fileSize = newFile.length();
					ret = qeMng.makeMergeSuccXml(fileSize);
					// 合成完毕,处理xml，然后去调用合成服务器
				}catch(Exception e){
					ret = qeMng.makeFailedXml(requestTypes,
							"文件错误，合并失败");
					e.printStackTrace();
				}
				
			}
		}
		System.out.println("---------------返回结果------------");
		System.out.println(ret);
		System.out.println("--------------------------------");
		
		ResponseUtilsForC.renderXml(response , ret);

	}
	
	
	@RequestMapping(value = "/QEXmlOutServlet")
	public void download(HttpServletRequest request, HttpServletResponse response , String fileIds){
		
		
		if(fileIds.endsWith(","))fileIds.substring(0,fileIds.length()-1);
		String[] sIds = fileIds.split(",");
		Long[] ids = new Long[sIds.length];
		
		for(int i=0 ; i<sIds.length ; i++){
			ids[i] = new Long(sIds[i]);
		}
		
		List<AppMediaFile> fileList = new ArrayList();
		
		for(Long id : ids){
			
			ILiveMediaFile iLiveMediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(id);
			AppMediaFile appMediaFile = iLiveMediaFileMng.convertILiveMediaFile2AppMediaFile(iLiveMediaFile);
			//因为需要将切片地址切换为文件地址
			String playAddr = appMediaFile.getPlayAddr();
			playAddr = playAddr.substring(0,playAddr.indexOf("/tzwj_video.m3u8"));
			appMediaFile.setPlayAddr(playAddr);
			fileList.add(appMediaFile);
		}
		
		String ret = qeMng.makeDownloadXml(fileList);
		System.out.println(ret);
		ResponseUtilsForC.renderXml(response , ret);
		
	}
	
	@RequestMapping(value = "/QESelectFile")
	public String selectFile(HttpServletRequest request, HttpServletResponse response , String mediaFileName , Integer pageNo,
				Integer pageSize, ModelMap map,String uuid){
		try{
			Map<String, Object> sqlParam = new HashMap<>();
			UserBean user = QEUserList.get(uuid);
			if(user !=null){
				sqlParam.put("mediaFileName", mediaFileName);
				sqlParam.put("enterpriseId", Long.valueOf(user.getEnterpriseId().toString()));
				sqlParam.put("createType", 5);
				sqlParam.put("delFlag", new Integer(0));
				Pagination pagination = iLiveMediaFileMng.getMediaFilePage(sqlParam, pageNo, new Integer(10));
				map.addAttribute("liveMediaPage", pagination);
				map.addAttribute("mediaFileName", mediaFileName == null ? "" : mediaFileName);
				map.addAttribute("createType", 5);
				map.addAttribute("uuid", uuid);
				map.addAttribute("topActive", "2");
				map.addAttribute("leftActive", "1_1");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
			
		
		return "filelib/qe_video_mgr";
	}
	
	
	private void createQuickNotifyXml(String taskUUID , String userName ) {
		
		try{
			String QuickEditUrl = ConfigUtils.get("quickeditor_server_addr");
			// 得到document对象
			Document document = DocumentHelper.createDocument();
			// 添加根节点
			Element root = document.addElement("root");
			// 添加节点class,属性名为name与table 分别赋值
			Element responseType = root.addElement("xmlFtpPath");
			Element taskUUIDType = root.addElement("taskUUID");
			String ftpip = ConfigUtils.get("quickeditor_ftp_ip");
			String ftpport = ConfigUtils.get("quickeditor_ftp_port");
			String ftpusername = ConfigUtils.get("quickeditor_ftp_username");
			String ftppassword = ConfigUtils.get("quickeditor_ftp_password");

			String xmlPathText = "ftp://" + ftpusername + ":" + ftppassword
					+ "@" + ftpip+ ":" + ftpport + "/"+userName+"/"+ taskUUID+".xml";
			responseType.setText(xmlPathText);
			taskUUIDType.setText(taskUUID);
			String xml = document.asXML();
			System.out.println("发送给快编服务的数据为=========================" + xml);
			String retXml = DownloadURL.downloadUrlWithinputStream(QuickEditUrl,
					"post", "utf-8", new ByteArrayInputStream(xml.getBytes()));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private String getTaskInfoXml(String taskUUID ) {
		
		String ret = null;
		try{
			
			String QuickEditUrl = ConfigUtils.get("quickeditor_server_task")+"?taskUUID="+taskUUID;
			System.out.println("QuickEditUrl:"+QuickEditUrl);
			ret = DownloadURL.downloadUrl(QuickEditUrl,
					"POST", "utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	
	/**
	 * 获取认证字符串
	 * 
	 * @return
	 */
	private String getAuthString() {
		Calendar calendar = Calendar.getInstance();
		int nMonth = calendar.get(Calendar.MONTH) + 1;
		int nDay = calendar.get(Calendar.DAY_OF_MONTH);
		int nHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nMin = calendar.get(Calendar.MINUTE);
		int nSecond = calendar.get(Calendar.SECOND);

		long lTime = ((nMonth * 30) + nDay) * 24 * 3600 + nHour * 3600 + nMin * 60 + nSecond;
		// System.out.println("nMonth="+nMonth+" nDay="+nDay+" nHour="+nHour+" nMin="+nMin+" nSecond="+nSecond);
		// System.out.println("lTime="+lTime);
		String sTime = "" + lTime;

		nSeq = (nSeq + 1) % 10000;

		Md5 md5 = new Md5();
		StringBuffer sAuth = new StringBuffer();
		sAuth.append("auth=").append(AppTools.createRandomNum(4, 1)).append(sTime)
				.append(AppTools.createRandomNum(4, 1)).append("@").append(nSeq).append("@")
				.append(md5.getMD5ofStr(sTime + "jwzt" + nSeq)).append("");

		return sAuth.toString();
	}
		
	@Autowired
	private QEMng  qeMng;
	@Autowired
	private ILiveManagerMng iLiveManagerMng;
	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;
	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
}
