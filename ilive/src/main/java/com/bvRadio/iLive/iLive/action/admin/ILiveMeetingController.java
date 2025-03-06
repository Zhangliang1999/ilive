package com.bvRadio.iLive.iLive.action.admin;

import com.alibaba.fastjson.JSONObject;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.IliveMeetingFile;
import com.bvRadio.iLive.iLive.entity.MeetingInvitationInfo;
import com.bvRadio.iLive.iLive.manager.ILiveMeetingFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserMeetRoleMng;
import com.bvRadio.iLive.iLive.manager.MeetingInvitationInfoMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("meeting/")
public class ILiveMeetingController {

    @Autowired
    private ILiveMeetingFileMng iLiveMeetingFileMng;

    @Autowired
    private ILiveUserMeetRoleMng meetRoleMng;

    @Autowired
    private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

    @Autowired
    private MeetingInvitationInfoMng meetingInvitationInfoMng;

    @RequestMapping("toUpload.do")
    public ModelAndView toMeetingFileUploadPage(ModelAndView modelAndView, Integer roomId){
        ModelMap map = modelAndView.getModelMap();
        ILiveLiveRoom ilive = new ILiveLiveRoom();
        ilive.setRoomId(roomId);
        map.put("iLiveLiveRoom",ilive);
        map.put("roomId",roomId);
        map.put("leftActive","8_2");
        map.put("topActive","1");
        modelAndView.setViewName("meet/file_upload");
        return modelAndView;
    }

    @RequestMapping("meetInviteDiy.do")
    public ModelAndView invitationPC(ModelAndView modelAndView, Integer roomId){
        ModelMap map = modelAndView.getModelMap();
        ILiveLiveRoom ilive = new ILiveLiveRoom();
        ilive.setRoomId(roomId);
        MeetingInvitationInfo result = meetingInvitationInfoMng.selectInstanceByRoomId(roomId);
        map.put("data",result == null ? new MeetingInvitationInfo() : result);
        map.put("roomId",roomId);
        map.put("leftActive","8_3");
        map.put("topActive","1");
        map.put("iLiveLiveRoom",ilive);
        modelAndView.setViewName("liveroom/meetInviteDiy");
        return modelAndView;
    }

    @RequestMapping(value = "restoreDefaultPhoto.do")
    public void restoreDefaultPhoto(MeetingInvitationInfo invitationInfo, HttpServletResponse response){
        JSONObject result = new JSONObject();
        result.put("status",1);
        result.put("code",1);
        result.put("message","恢复成功！");
        try {
            MeetingInvitationInfo info = meetingInvitationInfoMng.selectInstanceByRoomId(invitationInfo.getRoomId());
            if ("logo".equals(invitationInfo.getImage())) {
                info.setLogo(null);
            } else if ("backImage".equals(invitationInfo.getImage())) {
                info.setBackImage(null);
            }
            meetingInvitationInfoMng.saveOrUpdateInstance(info);
        }catch(Exception e){
            result.put("status",0);
            result.put("code",0);
            result.put("message","更新异常！");
        }
        ResponseUtils.renderJson(response,result.toJSONString());
    }

    /**
     * 功能：保存会议邀请函图片
     * @param invitationInfo
     * @param response
     * @param file
     */
    @RequestMapping(value = "saveOrUpdatePhotos.do",method = RequestMethod.POST)
    public void saveOrUpdatePhotos(MeetingInvitationInfo invitationInfo, HttpServletResponse response, @RequestParam MultipartFile file){
        JSONObject result = new JSONObject();
        result.put("status",1);
        result.put("code",1);
        try{
            if(file != null && file.getSize() > 0){
                //上传图片
                String fileName = file.getOriginalFilename();
                String tempFileName = System.currentTimeMillis() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
                String filePath = FileUtils.getTimeFilePath(tempFileName);
                String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
                ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
                        .getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
                boolean b = accessMethod.uploadFile(filePath, file.getInputStream());
                if(!b){
                    throw new Exception("上传图片失败！");
                }
                String newPhoto = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
                        + accessMethod.getMountInfo().getBase_path() + filePath;
                MeetingInvitationInfo info = meetingInvitationInfoMng.selectInstanceByRoomId(invitationInfo.getRoomId());
                //判断上传图片是logo、backImage、processImage中的哪一个
                if("logo".equals(invitationInfo.getImage())){
                    info.setLogo(newPhoto);
                }else if("backImage".equals(invitationInfo.getImage())){
                    info.setBackImage(newPhoto);
                }else{
                    if(StringUtils.isNotBlank(info.getProcessImage())){
                        info.setProcessImage(info.getProcessImage()+","+newPhoto);
                    }else{
                        info.setProcessImage(newPhoto);
                    }
                }
                meetingInvitationInfoMng.saveOrUpdateInstance(info);
                List<String> otherImages = new ArrayList<>();
                if(StringUtils.isNotBlank(info.getProcessImage())){
                    for (String item : info.getProcessImage().split(",")) {
                        otherImages.add(item);
                    }
                }
                info.setOtherImages(otherImages);
                result.put("data",info);
            }else{
                result.put("status",400);
                result.put("code",400);
                result.put("message","文件不能为空");
            }
        }catch (Exception e){
            result.put("code",500);
            result.put("status",500);
            result.put("message","上传失败");
            e.printStackTrace();
        }
        ResponseUtils.renderJson(response,result.toJSONString());
    }

    @RequestMapping("udpateInvitationProcessStatus.do")
    public void udpateInvitationProcessStatus(MeetingInvitationInfo invitationInfo, HttpServletResponse response){
        JSONObject result = new JSONObject();
        result.put("status",1);
        result.put("code",1);
        if(invitationInfo.getRoomId() == null || invitationInfo.getProcessFlag() == null){
            result.put("status",400);
            result.put("code",400);
            result.put("message","roomId或processFlag不能为空!");
        }else{
            try{
                MeetingInvitationInfo info = meetingInvitationInfoMng.selectInstanceByRoomId(invitationInfo.getRoomId());
                info.setProcessFlag(invitationInfo.getProcessFlag());
                meetingInvitationInfoMng.saveOrUpdateInstance(info);
            }catch (Exception e){
                result.put("status",500);
                result.put("code",500);
                result.put("message","更新出错");
                e.printStackTrace();
            }
        }
        ResponseUtils.renderJson(response,result.toJSONString());
    }

    @RequestMapping("deleteProcessImages.do")
    public void deleteProcessImages(MeetingInvitationInfo invitationInfo, HttpServletResponse response){
        JSONObject json = new JSONObject();
        if(StringUtils.isBlank(invitationInfo.getProcessImage()) || invitationInfo.getRoomId() == null){
            json.put("code",400);
            json.put("message","roomId或processImage不能为空.");
        }else{
            MeetingInvitationInfo info = meetingInvitationInfoMng.selectInstanceByRoomId(invitationInfo.getRoomId());
            StringBuilder process = new StringBuilder();
            for (String item : info.getOtherImages()) {
                if(invitationInfo.getProcessImage().equals(item)){
                    continue;
                }
                process.append(",").append(item);
            }
            if(process.length() > 0){
                info.setProcessImage(process.substring(1));
            }else{
                info.setProcessImage("");
            }
            meetingInvitationInfoMng.saveOrUpdateInstance(info);
            json.put("code",1);
        }
        ResponseUtils.renderJson(response,json.toJSONString());
    }

    /**
     * 分页列表
     * @param roomId
     * @param pageNo
     * @param pageSize
     * @param fileName
     * @param response
     */
    @RequestMapping(value = "page.do")
    public void selectMeetingFileListByPage(Integer roomId, Integer pageNo,Integer pageSize, String fileName,HttpServletResponse response){
        JSONObject json = new JSONObject();
        try {
            Map<String,Object> params = new HashMap<>();
            params.put("roomId",roomId);
            params.put("fileName",fileName);
            params.put("pageNo",pageNo == null ? 1: pageNo);
            params.put("pageSize",pageSize == null ? 10 : pageSize);
            Pagination pagination = iLiveMeetingFileMng.selectMeetingFileListByPage(params);
            List<?> list = pagination.getList();
            if(list == null || list.size() ==0){
                list = new ArrayList<>();
            }
            json.put("data", list);
            json.put("count", pagination.getTotalCount());
            json.put("msg", "分页列表");
            json.put("code", 0);
            json.put("fileName", fileName);
            json.put("roomId", roomId);
        } catch (Exception e) {
            json.put("data", new ArrayList<IliveMeetingFile>());
            json.put("count", 1);
            json.put("msg", "分页列表");
            json.put("code", 400);
            e.printStackTrace();
        }
        ResponseUtils.renderJson(response, json.toJSONString());
    }

    /**
     * 删除文件
     * @param fileId
     * @return
     */
    @RequestMapping("delete.do")
    public void deleteMeetingFileByFileId(@RequestParam(value = "fileId") Long fileId ,HttpServletRequest request, HttpServletResponse response){
        JSONObject modelMap = new JSONObject();
        modelMap.put("code",0);
        modelMap.put("msg","删除成功");
        try{
            IliveMeetingFile file = iLiveMeetingFileMng.selectMeetingFileListById(fileId);
            if(file == null){
                modelMap.put("code",1);
                modelMap.put("msg","删除文件不存在");
            }else{
                file.setDelFlag(1);
                iLiveMeetingFileMng.updateMeetingFile(file);
            }
        }catch (Exception e){
            modelMap.put("code",2);
            modelMap.put("msg","删除异常");
            e.printStackTrace();
        }
        ResponseUtils.renderJson(response, modelMap.toJSONString());
    }

    /**
     * 批量删除
     * @param fileIds
     * @return
     */
    @ResponseBody
    @RequestMapping("batchDelete.do")
    public void batchDeleteMeetingFiles(@RequestParam(value = "fileIds[]") List<Long> fileIds,HttpServletResponse response){
        JSONObject modelMap = new JSONObject();
        if(fileIds.size() < 1){
            modelMap.put("status",1);
            modelMap.put("msg","未选中任何文件！");
        }
        try{
            StringBuilder builder = new StringBuilder();
            for (Long id : fileIds) {
                builder.append(","+id);
            }
            iLiveMeetingFileMng.batchDeleteMeetingFiles(builder.substring(1));
            modelMap.put("status",0);
            modelMap.put("msg","删除成功！");
        }catch (Exception e){
            modelMap.put("status",1);
            modelMap.put("msg","删除异常！");
        }
        ResponseUtils.renderJson(response, modelMap.toJSONString());
    }

    /**
     * 下载文件
     */
    @ResponseBody
    @RequestMapping(value="download.do")
    public void download(HttpServletResponse response, Long fileId) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        ByteArrayOutputStream byteArrayOutputStream = null;
        ServletOutputStream outputStream = null;
        try {
            IliveMeetingFile file = iLiveMeetingFileMng.selectMeetingFileListById(fileId);
            URL url2 = new URL(file.getFileUrl());
            DataInputStream dataInputStream = new DataInputStream(url2.openStream());
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int length ;
            while((length = dataInputStream.read(bytes))!=-1) {
                byteArrayOutputStream.write(bytes, 0, length);
            }
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(file.getMediaFileName(), "utf-8"));
            response.setHeader("Content-Type","multipart/form-data");
            outputStream = response.getOutputStream();
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
