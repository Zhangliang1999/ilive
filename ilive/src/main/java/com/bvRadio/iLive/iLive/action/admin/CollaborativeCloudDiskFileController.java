package com.bvRadio.iLive.iLive.action.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.*;
import com.bvRadio.iLive.iLive.manager.*;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.StringUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("collaborative/cloudDisk/")
public class CollaborativeCloudDiskFileController {

    private static Logger log = LoggerFactory.getLogger(CollaborativeCloudDiskFileController.class);
    @Autowired
    private ILiveMediaFileMng iLiveMediaFileMng;
    @Autowired
    private ILiveSubLevelMng iLiveSubLevelMng;
    @Autowired
    private ILiveSubRoomMng iLiveSubRoomMng;
    @Autowired
    private ILiveEnterpriseMng iLiveEnterpriseMng;
    @Autowired
    private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

    @RequestMapping("fileList.do")
    public ModelAndView cloudDiskPage(ModelAndView modelAndView) {
        ModelMap map = modelAndView.getModelMap();
        map.put("leftActive", "2_0");
        map.put("topActive", "2");
        modelAndView.setViewName("cloudDisk/fileList");
        return modelAndView;
    }

    @RequestMapping("page.do")
    public void fileList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "fileName", required = false) String fileName, Integer pageNo, Integer pageSize) {
        Map<String, Object> sqlParam = new HashMap<String, Object>();
        sqlParam.put("isSync", 1);
        UserBean user = ILiveUtils.getUser(request);
        Integer level = user.getLevel();
        level = level == null ? ILiveManager.USER_LEVEL_ADMIN : level;
        //查询子账户是否有点播视频查看全部权限
        boolean per = iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_recall);
        if (level.equals(ILiveManager.USER_LEVEL_SUB) && !per) {
            sqlParam.put("userId", user.getUserId());
        } else {
            sqlParam.put("enterpriseId", (long) user.getEnterpriseId());
        }
        //通过userid获取roomids
        String roomIds = "";
        List<IliveSubRoom> roomFileList = iLiveSubRoomMng.selectILiveSubById(Long.parseLong(user.getUserId()));
        if (roomFileList.size() > 0) {
            if (roomFileList.size() == 1) {
                roomIds = "(" + roomFileList.get(0).getLiveRoomId() + ")";
            } else if (roomFileList.size() > 1) {
                for (int i = 0; i < roomFileList.size(); i++) {
                    if (i == 0) {
                        roomIds = "(" + roomFileList.get(0).getLiveRoomId() + ",";
                    } else if (i == roomFileList.size() - 1) {
                        roomIds = roomIds + roomFileList.get(roomFileList.size() - 1).getLiveRoomId() + ")";
                    } else {
                        roomIds = roomIds + roomFileList.get(i).getLiveRoomId() + ",";
                    }
                }
            }
        }
        sqlParam.put("roomIds", roomIds);
        if (StringUtils.isNotBlank(fileName)) {
            sqlParam.put("mediaFileName", fileName);
        }
        Pagination mediaFilePage = iLiveMediaFileMng.getMediaFilePage1(sqlParam, pageNo, pageSize);
        //获取企业云盘空间信息
        String url = ConfigUtils.get("cloud_disk_backups_address") + "/enterprise/selectPackageInformation.do";
        Map<String, String> params = new HashMap<>();
        params.put("enterpriseId", user.getEnterpriseId().toString());
        JSONObject json = new JSONObject();
        try {
            String result = HttpUtils.sendPost(url, params, "UTF-8");
            if (StringUtils.isNotBlank(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                json.put("totalSpace", jsonObject.get("totalSpace"));
                json.put("usedSpace", jsonObject.get("usedSpace"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mediaFilePage.getTotalCount() > 0) {
            json.put("data", mediaFilePage.getList());
            json.put("count", mediaFilePage.getTotalCount());
            json.put("msg", "分页列表");
            json.put("code", 0);
            json.put("fileName", fileName);
            json.put("cloudDiskUrl", ConfigUtils.get("cloud_disk_backups_address"));
        } else {
            json.put("data", new ArrayList<String>());
            json.put("count", 0);
            json.put("msg", "暂无数据");
            json.put("code", 1);
            json.put("fileName", fileName);
            json.put("cloudDiskUrl", ConfigUtils.get("cloud_disk_backups_address"));
        }
        if (!json.containsKey("totalSpace")) {
            json.put("totalSpace", 0);
        }
        if (!json.containsKey("usedSpace")) {
            json.put("usedSpace", 0);
        }
        ResponseUtils.renderJson(response, json.toJSONString());
    }

    @RequestMapping("validateSpace.do")
    public void validateSpace(HttpServletResponse response,
                              Integer enterpriseId,
                              @RequestParam(value = "cloudDiskFileId[]") String[] cloudDiskFileId) {
        JSONObject json = new JSONObject();
        if (enterpriseId == null || cloudDiskFileId.length == 0) {
            json.put("code", 400);
            json.put("message", "cloudDiskFileId和enterpriseId不能为空！");
        } else {
            ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
            if (iLiveEnterPrise == null) {
                json.put("code", 400);
                json.put("message", "enterpriseId不存在对应的企业！");
            } else {
                List<ILiveMediaFile> list = iLiveMediaFileMng.selectILiveMediaFileBycloudDiskFileIds(cloudDiskFileId);
                if (list == null || list.size() == 0) {
                    json.put("code", 400);
                    json.put("message", "cloudDiskFileId不存在对应的云盘文件！");
                } else {
                    //查询企业剩余空间
                    try {
                        //更新企业缓存
                        EnterpriseUtil.selectEnterpriseCache(iLiveEnterPrise.getEnterpriseId(), iLiveEnterPrise.getCertStatus());
                        Hashtable<Integer, Hashtable<String, String>> enterpriseStrMap = EnterpriseCache.getEnterpriseStrMap();
                        Hashtable<String, String> strMap = enterpriseStrMap.get(iLiveEnterPrise.getEnterpriseId());
                        //剩余空间大小（KB）
                        long reminderSpace = Long.parseLong(strMap.get(EnterpriseCache.ENTERPRISE_Store)) - Long.parseLong(strMap.get(EnterpriseCache.ENTERPRISE_USE_Store));
                        //需要空间大小（KB）
                        long needSpace = 0;
                        for (ILiveMediaFile file : list) {
                            needSpace += file.getFileSize();
                        }
                        if (reminderSpace > needSpace && needSpace > 0) {
                            //通知计费系统计费
                            //通知计费系统企业消耗
                            boolean b = EnterpriseUtil.openEnterprise(iLiveEnterPrise.getEnterpriseId(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Store, needSpace + "", iLiveEnterPrise.getCertStatus());
                            log.info("通知企业计费发送消息返回结果：" + b);
                            json.put("code", 0);
                        } else {
                            json.put("code", 400);
                            json.put("message", "您的在线媒资库储存空间不足，无法转入本次选择的文件！");
                        }
                    } catch (Exception e) {
                        json.put("code", 400);
                        json.put("message", "获取企业空间失败！");
                    }

                }
            }
        }
        ResponseUtils.renderJson(response, json.toJSONString());
    }

    @RequestMapping("formartOnlineMedia.do")
    public void formartOnlineMedia(@RequestParam(value = "cloudDiskFileId[]", required = false) String[] cloudDiskFileId
            , Integer enterpriseId
            , HttpServletResponse response) {
        String url = ConfigUtils.get("cloud_disk_backups_address") + "/media/formartOnlineMedia.do";
        Map<String, String> params = new HashMap<>();
        params.put("enterpriseId", enterpriseId == null ? "" : enterpriseId.toString());
        if (cloudDiskFileId != null || cloudDiskFileId.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String item : cloudDiskFileId) {
                builder.append(",").append(item);
            }
            params.put("cloudDiskFileIds", builder.substring(1));
        } else {
            params.put("cloudDiskFileIds", "");
        }
        String defaultVodServerGroupId = ConfigUtils.get("defaultVodServerGroupId");
        ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
                .getAccessMethodBySeverGroupId(Integer.parseInt(defaultVodServerGroupId));
        params.put("accessMethod", JSON.toJSONString(accessMethod));
        String result = null;
        try {
            result = HttpUtils.sendPost(url, params, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseUtils.renderJson(response, result);
    }

    @RequestMapping("getMediaUrlById.do")
    public void getMediaUrlById(String cloudDiskFileId, Integer enterpriseId, HttpServletResponse response) {
        String url = ConfigUtils.get("cloud_disk_backups_address") + "/media/getMediaUrlById.do";
        Map<String, String> params = new HashMap<>();
        params.put("enterpriseId", enterpriseId == null ? "" : enterpriseId.toString());
        params.put("cloudDiskFileId", cloudDiskFileId);
        String post = null;
        try {
            post = HttpUtils.sendPost(url, params, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseUtils.renderJson(response, post);
    }

    @RequestMapping("deleteCloudDiskFiles.do")
    public void deleteCloudDiskFiles(@RequestParam(value = "fileId[]",required = false) String[] fileId,Integer enterpriseId, HttpServletResponse response) {
        String url = ConfigUtils.get("cloud_disk_backups_address") + "/media/deleteCloudDiskFiles.do";
        Map<String, String> params = new HashMap<>();
        params.put("enterpriseId", enterpriseId == null ? "" : enterpriseId.toString());
        if (fileId != null || fileId.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String item : fileId) {
                builder.append(",").append(item);
            }
            params.put("fileIds", builder.substring(1));
        } else {
            params.put("fileIds", "");
        }
        String post = null;
        try {
            post = HttpUtils.sendPost(url, params, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseUtils.renderJson(response, post);

    }
}