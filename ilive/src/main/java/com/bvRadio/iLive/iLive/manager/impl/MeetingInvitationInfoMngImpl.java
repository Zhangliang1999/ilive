package com.bvRadio.iLive.iLive.manager.impl;


import com.bvRadio.iLive.iLive.dao.MeetingInvitationInfoDao;
import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.MeetingInvitationInfo;
import com.bvRadio.iLive.iLive.manager.ILiveMeetRequestMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.manager.MeetingInvitationInfoMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.TwoDimensionCode;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MeetingInvitationInfoMngImpl implements MeetingInvitationInfoMng {

    @Autowired
    private MeetingInvitationInfoDao meetingInvitationInfoDao;

    @Autowired
    private ILiveMeetRequestMng iLiveMeetRequestMng;

    @Autowired
    private ILiveUploadServerMng iLiveUploadServerMng;

    @Override
    public MeetingInvitationInfo selectInstanceByRoomId(Integer roomId) {
        MeetingInvitationInfo infos = meetingInvitationInfoDao.selectInstanceByRoomId(roomId);
        if(infos != null && StringUtils.isNotBlank(infos.getProcessImage())){
            String[] split = infos.getProcessImage().split(",");
            List<String> otherImages = infos.getOtherImages();
            for (String itme : split) {
                otherImages.add(itme);
            }
        }
        if(infos == null){
            infos = new MeetingInvitationInfo();
            infos.setRoomId(roomId);
            infos.setProcessFlag(-1);
            ILiveMeetRequest meetRequest = null;
            //id取前六位是为了让生成的二维码图片更简单一点，防止图片过于密集
            for (int i = 0 ; i < 2 ; i++){
                if(i == 0){
                    meetRequest = iLiveMeetRequestMng.getHostByMeetId(roomId);
                    infos.setHostQR(ConfigUtils.get("meet_invitation_info_url")+"?roomId="+roomId+"&id="+meetRequest.getId().substring(0,6));
                }else{
                    meetRequest = iLiveMeetRequestMng.getStudentByMeetId(roomId);
                    infos.setParticipantQR(ConfigUtils.get("meet_invitation_info_url")+"?roomId="+roomId+"&id="+meetRequest.getId().substring(0,6));
                }
            }
            meetingInvitationInfoDao.saveOrUpdateInstance(infos);
        }

        return infos;
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingInvitationInfo selectInstanceById(Integer id) {
        return meetingInvitationInfoDao.selectInstanceById(id);
    }

    @Override
    public void saveOrUpdateInstance(MeetingInvitationInfo info) {
        meetingInvitationInfoDao.saveOrUpdateInstance(info);
    }
}
