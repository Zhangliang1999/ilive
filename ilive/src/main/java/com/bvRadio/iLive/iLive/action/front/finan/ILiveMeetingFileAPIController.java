package com.bvRadio.iLive.iLive.action.front.finan;

import com.alibaba.fastjson.JSON;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.*;
import com.bvRadio.iLive.iLive.manager.*;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.Md5Util;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import com.google.common.collect.Sets;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("meetingFile/")
public class ILiveMeetingFileAPIController {

    private static final String[] FILE_TYPE = {"avi","mp4","jpeg","jpg","png","xls","xlsx","doc","docx","ppt","pptx"};
    private static Logger logger = LoggerFactory.getLogger(ILiveMeetingFileAPIController.class);
    @Autowired
    private ILiveMeetingFileMng iLiveMeetingFileMng;

    @Autowired
    private ILiveUserMeetRoleMng meetRoleMng;

    @Autowired
    private ILiveManagerMng iLiveManagerMng;

    @Autowired
    private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

    @Autowired
    private ILiveLiveRoomMng iLiveLiveRoomMng;// 直播间

    @Autowired
    private ILiveConvertTaskMng iLiveConvertTaskMng;

    @Autowired
    private MeetingInvitationInfoMng meetingInvitationInfoMng;

    @Autowired
    private ILiveMeetRequestMng iLiveMeetRequestMng;

    @Autowired
    private ILiveEnterpriseMng iLiveEnterpriseMng;
    @Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

    /**
     * 会议文件列表页面
     * @param userId
     * @param roomId
     * @param keyword
     * @param fileType
     * @param pageNo
     * @param pageSize
     */
    @RequestMapping("page.jspx")
    public void getMeetingFileByPage(String userId,
                                     Integer roomId,
                                     String keyword,
                                     String fileType,
                                     Integer pageNo,
                                     Integer pageSize,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        JSONObject json = new JSONObject();
        if(userId == null || roomId == null){
            json.put("code",0);
            json.put("message","参数不能为空！");
            ResponseUtils.renderJsonNoJsonp(request,response,json.toString());
            return;
        }
        ILiveUserMeetRole user = meetRoleMng.getByUserMeetId(roomId, userId);
        ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
        if(checkParams(json,user,iliveRoom)){
            try {
                Map<String,Object> params = new HashMap<>();
                params.put("roomId",roomId);
                params.put("fileName",keyword == null ? "" : keyword);
                params.put("pageNo",pageNo == null ? 1 : pageNo);
                params.put("pageSize",pageSize == null ? 10 : pageSize);
                if(StringUtils.isNotBlank(fileType)){
                    params.put("fileType",fileType);
                }
                Pagination pagination = iLiveMeetingFileMng.selectMeetingFileListByPage(params);
                JSONObject result = new JSONObject();
                result.put("count",pagination.getTotalCount());
                result.put("list",pagination.getList());
                json.put("code",1);
                json.put("message","会议文件列表数据");
                json.put("data",result);
            }catch (Exception e){
                json.put("code",0);
                json.put("message","查询异常");
            }
        }
        
        ResponseUtils.renderJsonNoJsonp(request,response,json.toString());
    }

    /**
     * 功能：查询会议邀请函_PC
     * @param meet
     * @param response
     * @param request
     */
    @RequestMapping("selectMeetingInvitationDiy.jspx")
    public void selectMeetingInvitationDiy(ILiveMeetRequest meet,HttpServletResponse response,HttpServletRequest request){
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        Integer roomId = meet.getRoomId();
        if(roomId == null){
            result.put("code",400);
            result.put("message","roomId不能为空");
            ResponseUtils.renderJson(request,response,request.toString());
            return;
        }
        ILiveMeetRequest meetRequest = null;
        ILiveMeetRequest studentByMeetId = iLiveMeetRequestMng.getStudentByMeetId(roomId);
        ILiveMeetRequest hostByMeetId = iLiveMeetRequestMng.getHostByMeetId(roomId);
        if(studentByMeetId != null && studentByMeetId.getId().substring(0,6).equals(meet.getId())){
            meetRequest = studentByMeetId;
        }else if(hostByMeetId != null && hostByMeetId.getId().substring(0,6).equals(meet.getId())){
            meetRequest = hostByMeetId;
        }
        if(meetRequest == null){
            result.put("code",400);
            result.put("message","id不正确");
            ResponseUtils.renderJson(request,response,request.toString());
            return;
        }
        try{
            MeetingInvitationInfo meetingInvitationInfo = meetingInvitationInfoMng.selectInstanceByRoomId(roomId);
            ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
            ILiveEvent liveEvent = iliveRoom.getLiveEvent();
            ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
            //会议邀请函信息
            data.put("liveStartTime",liveEvent.getLiveStartTime());
            data.put("liveEndTime",liveEvent.getLiveEndTime());
            data.put("liveTitle",liveEvent.getLiveTitle());
            data.put("roomId",roomId);
            data.put("enterpriseName",iLiveEnterPrise.getEnterpriseName());
            data.put("password",meetRequest.getPassword());
            data.put("loginUrl",meetRequest.getLoginUrl());
            data.put("logo",meetingInvitationInfo.getLogo());
            data.put("backImage",meetingInvitationInfo.getBackImage());
            data.put("otherImages",meetingInvitationInfo.getOtherImages());
            data.put("processFlag",meetingInvitationInfo.getProcessFlag());
            result.put("code",1);
            result.put("data",data);
        }catch (Exception e) {
            e.printStackTrace();
            result.put("code",500);
            result.put("message","查询错误！");
        }
        ResponseUtils.renderJson(request,response,result.toString());
    }
    /**
     * 功能：查询会议邀请函_APP
     * @param roomId
     * @param type
     * @param response
     * @param request
     */
    @RequestMapping("selectMeetingInvitation.jspx")
    public void selectMeetingInvitationDiy(Integer roomId,Integer type,HttpServletResponse response,HttpServletRequest request){
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        if(roomId == null){
            result.put("code",400);
            result.put("message","roomId不能为空");
            ResponseUtils.renderJson(request,response,request.toString());
            return;
        }
        ILiveMeetRequest meetRequest = null;
        ILiveMeetRequest studentByMeetId = iLiveMeetRequestMng.getStudentByMeetId(roomId);
        ILiveMeetRequest hostByMeetId = iLiveMeetRequestMng.getHostByMeetId(roomId);
        if(type != null && type.intValue() == 1){
            meetRequest = hostByMeetId;
        }else{
            meetRequest = studentByMeetId;
        }
        try{
            MeetingInvitationInfo meetingInvitationInfo = meetingInvitationInfoMng.selectInstanceByRoomId(roomId);
            ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
            ILiveEvent liveEvent = iliveRoom.getLiveEvent();
            ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(iliveRoom.getEnterpriseId());
            //会议邀请函信息
            data.put("liveStartTime",liveEvent.getLiveStartTime());
            data.put("liveTitle",liveEvent.getLiveTitle());
            data.put("roomId",roomId);
            data.put("enterpriseName",iLiveEnterPrise.getEnterpriseName());
            data.put("password",meetRequest.getPassword());
            data.put("loginUrl",meetRequest.getLoginUrl());
            data.put("logo",meetingInvitationInfo.getLogo());
            data.put("backImage",meetingInvitationInfo.getBackImage());
            data.put("otherImages",meetingInvitationInfo.getOtherImages());
            data.put("processFlag",meetingInvitationInfo.getProcessFlag());
            if(type != null && type.intValue() == 1){
                data.put("shareUrl",ConfigUtils.get("meet_invitation_info_url")+"?roomId="+roomId+"&id="+meetRequest.getId().substring(0,6));
            }else{
                data.put("shareUrl",ConfigUtils.get("meet_invitation_info_url")+"?roomId="+roomId+"&id="+meetRequest.getId().substring(0,6));
            }
            result.put("code",1);
            result.put("data",data);
        }catch (Exception e) {
            e.printStackTrace();
            result.put("code",500);
            result.put("message","查询错误！");
        }
        ResponseUtils.renderJson(request,response,result.toString());
    }
    /**
     * 删除指定文件
     * @param fileId
     * @param userId
     * @param roomId
     * @param request
     * @param response
     */
    @RequestMapping("delete.jspx")
    public void deleteMeetingFileByFileId(Long fileId ,String userId,Integer roomId, HttpServletRequest request, HttpServletResponse response){
        JSONObject modelMap = new JSONObject();
        if(userId == null || roomId == null){
            modelMap.put("code",0);
            modelMap.put("message","参数不能为空！");
            ResponseUtils.renderJsonNoJsonp(request,response,modelMap.toString());
            return;
        }
        IliveMeetingFile file = null;
        ILiveUserMeetRole user = meetRoleMng.getByUserMeetId(roomId, userId);
        ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
        if(checkParams(modelMap,user,iliveRoom) && (file = checkFileExist(modelMap,fileId)) != null){
            if(user.getRoleType() == 3 && !userId.equals(file.getCreateId())){
                modelMap.put("code",0);
                modelMap.put("message","不能删除别人上传的文件");
            }else if(roomId.intValue() != file.getLiveRoomId().intValue()){
                modelMap.put("code",0);
                modelMap.put("message","目标文件不在当前会议下");
            }else{
                file.setDelFlag(1);
                file.setUpdateId(userId);
                iLiveMeetingFileMng.updateMeetingFile(file);
                modelMap.put("code",1);
                modelMap.put("message","删除成功");
            }
        }
        ResponseUtils.renderJsonNoJsonp(request,response, modelMap.toString());
    }

    /**
     * 删除会议室下所有文件
     * @param request
     * @param response
     * @param roomId
     * @param userId
     * @param fileType
     */
    @RequestMapping(value = "deleteAllFiles.jspx")
    public void deleteMeetingAllFiles(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Integer roomId,
                                      String userId,
                                      Integer fileType){
        JSONObject modelMap = new JSONObject();
        if(userId == null || roomId == null){
            modelMap.put("code",0);
            modelMap.put("message","参数不能为空！");
            ResponseUtils.renderJsonNoJsonp(request,response,modelMap.toString());
            return;
        }
        ILiveUserMeetRole user = meetRoleMng.getByUserMeetId(roomId, userId);
        ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
        if(checkParams(modelMap,user,iliveRoom)){
            if(user.getRoleType() == 3){
                modelMap.put("code",413);
                modelMap.put("message","用户权限不足");
            }else{
                iLiveMeetingFileMng.deleteMeetingAllFilesByType(roomId,fileType);
                modelMap.put("code",1);
                modelMap.put("msg","删除成功");
            }
        }
        ResponseUtils.renderJsonNoJsonp(request,response,modelMap.toString());
    }
    
    /**
     * 文件上传api
     * @param roomId  Integer
     * @param userId  Long
     * @param fileName String
     * @param fileSize  (KB)
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "uploadFileAPI.jspx")
    public void uploadFileAPI(@RequestParam(value = "file") MultipartFile file, String appId,  String fileName, Long time,String token ,HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        String key =Md5Util.encode(time+"&TV189&"+ConfigUtils.get("meet_Secret"));
    	if(key.equals(token)) {
        if(appId == null ){
            json.put("code",0);
            json.put("message","appId参数不能为空！");
            ResponseUtils.renderJsonNoJsonp(request,response,json.toString());
            return;
        }
        ServletInputStream inputStream = null;
        ILiveEnterprise manage= iLiveEnterpriseMng.getILiveEnterPriseByAppId(appId);
        if(manage==null){
        	json.put("code", AutoLogin.CODE_ERROR);
        	json.put("data", new JSONObject());
        	json.put("msg", "无此appId！");
    		ResponseUtils.renderJson(request, response, json.toString());
    		return;
    	}
            try {
                logger.info("创建临时文件");
                String tempFileName = System.currentTimeMillis() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
                String realPath = request.getSession().getServletContext().getRealPath("/temp");
                File tempFile = createTempFile(realPath + "/" + tempFileName, file);
                logger.info("临时文件创建完成：boolean="+tempFile.exists()+" size:"+tempFile.length());
                String filePath = FileUtils.getTimeFilePath(tempFileName);
                String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
                ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
                        .getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
                boolean result = false;
                if (accessMethod != null) {
                    FileInputStream in = new FileInputStream(tempFile);
                    logger.info("ftp文件上传：filePath="+filePath+" inputStream:"+in.toString());
                    result = accessMethod.uploadFile(filePath, in);
                }
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                if(result){
                    
                    ILiveMediaFile  selectFile = new ILiveMediaFile();
                   
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
    				selectFile.setUserName(manage.getUserPhone());
    				selectFile.setServerMountId(accessMethod.getMountInfo().getServer_mount_id());
    				selectFile.setCreateEndTime(new Timestamp(new Date().getTime()));
    				
    				selectFile.setEnterpriseId((long) manage.getEnterpriseId());
    				selectFile.setMediaCreateTime(selectFile.getCreateStartTime());
    				selectFile.setAllowComment(1);
    				selectFile.setFilePath(filePath);
    				selectFile.setUserId(manage.getUserId());
    				selectFile.setMediaInfoStatus(0);
    				selectFile.setMediaInfoDealState(0);
    				Long saveIliveMediaFileId=  iLiveMediaFileMng.saveIliveMediaFile(selectFile);
                    
    				String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
    				+ accessMethod.getMountInfo().getBase_path() + filePath;
    		
    					ILiveConvertTask iLiveConvertTask = iLiveConvertTaskMng.createConvertTask(selectFile, accessMethod);
    					
                        if(iLiveConvertTask != null){
                            iLiveConvertTaskMng.addConvertTask(iLiveConvertTask);
                        }else{
                            logger.info("添加转码任务失败："+ JSON.toJSONString(selectFile));
                        }
                        try {
    						Thread.sleep(500);
    					} catch (InterruptedException e) {
    						
    						e.printStackTrace();
    					}
                    json.put("code",1);
                    json.put("message","上传成功");
                    json2.put("fileId",saveIliveMediaFileId);
                    json2.put("fileSize", selectFile.getFileSize());
                    json2.put("fileName", fileName);
                    json2.put("fileCover", selectFile.getFileCover());
                    json2.put("fileUrl", httpUrl);
                    json2.put("playAddr", ConfigUtils.get("free_login_review")+"/phone/review.html?roomId=0&fileId="+saveIliveMediaFileId);
                    json.put("data", json2);
                }else{
                    json.put("code",0);
                    json.put("message","上传失败");
                }
            } catch (IOException e) {
                json.put("code",0);
                json.put("message","上传失败");
                e.printStackTrace();
            }
    	}else {
    		json.put("code",0);
            json.put("message","验证失败");
    	}
        ResponseUtils.renderJsonNoJsonp(request,response,json.toString());
    }


    /**
     * 会议文件上传
     * @param roomId  Integer
     * @param userId  Long
     * @param fileName String
     * @param fileSize  (KB)
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "uploadMeetingFile.jspx")
    public void uploadMeetingFile(@RequestParam(value = "file") MultipartFile file, Integer roomId, String userId, String fileName, Long fileSize, HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONObject();
        if(userId == null || roomId == null){
            json.put("code",0);
            json.put("message","参数不能为空！");
            ResponseUtils.renderJsonNoJsonp(request,response,json.toString());
            return;
        }
        ServletInputStream inputStream = null;
        ILiveUserMeetRole user = meetRoleMng.getByUserMeetId(roomId, userId);
        ILiveLiveRoom iliveRoom = iLiveLiveRoomMng.getIliveRoom(roomId);
        if(validateFileType(json,fileName,fileSize) && checkParams(json,user,iliveRoom)){
            try {
                logger.info("创建临时文件");
                String tempFileName = System.currentTimeMillis() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
                String realPath = request.getSession().getServletContext().getRealPath("/temp");
                File tempFile = createTempFile(realPath + "/" + tempFileName, file);
                logger.info("临时文件创建完成：boolean="+tempFile.exists()+" size:"+tempFile.length());
                String filePath = FileUtils.getTimeFilePath(tempFileName);
                String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
                ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
                        .getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
                boolean result = false;
                if (accessMethod != null) {
                    FileInputStream in = new FileInputStream(tempFile);
                    logger.info("ftp文件上传：filePath="+filePath+" inputStream:"+in.toString());
                    result = accessMethod.uploadFile(filePath, in);
                }
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                if(result){
                    String httpUrl = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
                            + accessMethod.getMountInfo().getBase_path() + filePath.substring(1);
                    IliveMeetingFile meetingFile = new IliveMeetingFile();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    meetingFile.setEnterpriseId(iliveRoom.getEnterpriseId().longValue());
                    meetingFile.setFilePath(filePath);
                    meetingFile.setFileUrl(httpUrl);
                    meetingFile.setFileId(System.currentTimeMillis());
                    meetingFile.setMediaFileName(fileName);
                    meetingFile.setCreateId(userId);
                    meetingFile.setMediaCreateTime(new Timestamp(new Date().getTime()));
                    meetingFile.setFileExtension(fileExtension);
                    meetingFile.setLiveRoomId(roomId);
                    meetingFile.setLiveEventId(iliveRoom.getLiveEvent() == null ? null:iliveRoom.getLiveEvent().getLiveEventId());
                    meetingFile.setDelFlag(0);
                    meetingFile.setFileSize(fileSize);
                    fileExtension = fileExtension.toLowerCase();
                    StringBuilder path = new StringBuilder(ConfigUtils.get("ilive_url") + request.getContextPath());
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
                        if(iLiveConvertTask != null){
                            iLiveConvertTaskMng.addConvertTask(iLiveConvertTask);
                        }else{
                            logger.info("添加转码任务失败："+ JSON.toJSONString(meetingFile));
                        }
                    }
                    json.put("code",1);
                    json.put("message","上传成功");
                }else{
                    json.put("code",0);
                    json.put("message","上传失败");
                }
            } catch (IOException e) {
                json.put("code",0);
                json.put("message","上传失败");
                e.printStackTrace();
            }
        }
        ResponseUtils.renderJsonNoJsonp(request,response,json.toString());
    }

    private boolean checkParams(JSONObject json,Object user,ILiveLiveRoom iliveRoom){
        if(user == null){
            json.put("code",413);
            json.put("message","用户id不合法！");
            return false;
        }else if(iliveRoom == null){
            json.put("code",0);
            json.put("message","当前会议室不存在！");
            return false;
        }
        return true;
    }

    private IliveMeetingFile checkFileExist(JSONObject json,Long fileId){
        IliveMeetingFile file = null;
        if(fileId == null){
            json.put("code",0);
            json.put("message","参数不能为空!");
        }else {
            try {
                file = iLiveMeetingFileMng.selectMeetingFileListById(fileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(file == null){
                json.put("code",0);
                json.put("msg","删除文件不存在");
            }
        }
        return file;
    }

    private boolean validateFileType(JSONObject json,String fileName,Long fileSize){
        if(StringUtils.isBlank(fileName)){
            json.put("code",0);
            json.put("message","文件名称不能为空");
            return false;
        }
        for (String s : FILE_TYPE) {
            if (fileName.toLowerCase().endsWith(s)){
                return true;
            }
        }
        json.put("code",0);
        json.put("message","仅支持word、ppt、Excel、mp4、avi、jpg、png类型文件");
        return false;
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
        logger.info("创建临时上传文件耗时：{} ms", end - start);
        return tempFile;
    }
}
