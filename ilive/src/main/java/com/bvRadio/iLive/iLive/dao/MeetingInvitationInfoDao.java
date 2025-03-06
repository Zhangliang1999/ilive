package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.MeetingInvitationInfo;

import java.util.List;

public interface MeetingInvitationInfoDao {
    MeetingInvitationInfo selectInstanceByRoomId(Integer roomId);

    MeetingInvitationInfo selectInstanceById(Integer id);

    void saveOrUpdateInstance(MeetingInvitationInfo info);
}
