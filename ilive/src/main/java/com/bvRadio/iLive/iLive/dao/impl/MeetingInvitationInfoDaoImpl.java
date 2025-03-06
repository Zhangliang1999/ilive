package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.MeetingInvitationInfoDao;
import com.bvRadio.iLive.iLive.entity.MeetingInvitationInfo;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MeetingInvitationInfoDaoImpl extends HibernateBaseDao<MeetingInvitationInfo,Integer> implements MeetingInvitationInfoDao {
    @Override
    protected Class<MeetingInvitationInfo> getEntityClass() {
        return MeetingInvitationInfo.class;
    }

    @Override
    public MeetingInvitationInfo selectInstanceByRoomId(Integer roomId) {
        String hql = "from MeetingInvitationInfo info where info.roomId = :roomId";
        Query query = getSession().createQuery(hql);
        query.setParameter("roomId",roomId);
        return (MeetingInvitationInfo)query.uniqueResult();
    }

    @Override
    public MeetingInvitationInfo selectInstanceById(Integer id) {
        return get(id);
    }

    @Override
    public void saveOrUpdateInstance(MeetingInvitationInfo info) {
        getSession().saveOrUpdate(info);
    }
}
