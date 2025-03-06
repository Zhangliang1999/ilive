package com.bvRadio.iLive.iLive.manager;

import com.alibaba.fastjson.JSONObject;
import com.bvRadio.iLive.iLive.entity.MeetingInvitationInfo;

public interface MeetingInvitationInfoMng {
    MeetingInvitationInfo selectInstanceByRoomId(Integer roomId);

    MeetingInvitationInfo selectInstanceById(Integer id);

    void saveOrUpdateInstance(MeetingInvitationInfo info);
}
