package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.iLive.dao.ILiveMessageDao;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;

@Repository
public class ILiveMessageDaoImpl extends HibernateBaseDao<ILiveMessage, Long> implements ILiveMessageDao {
	public Pagination getPage(Integer liveId, Integer liveMsgType, Integer msgType, Date startTime, Date endTime,
			String senderName, String msgContent, Integer status, Boolean deleted, Boolean checked, Integer orderBy,
			int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		if (null != liveId) {
			finder.append(" and bean.live.liveId = :liveId");
			finder.setParam("liveId", liveId);
		}
		if (null != liveMsgType) {
			finder.append(" and bean.liveMsgType = :liveMsgType");
			finder.setParam("liveMsgType", liveMsgType);
		}
		if (null != msgType) {
			finder.append(" and bean.msgType = :msgType");
			finder.setParam("msgType", msgType);
		}
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if (!StringUtils.isBlank(senderName)) {
			finder.append(" and bean.senderName like :senderName");
			finder.setParam("senderName", "%" + senderName + "%");
		}
		if (!StringUtils.isBlank(msgContent)) {
			finder.append(" and bean.msgContent like :msgContent");
			finder.setParam("msgContent", "%" + msgContent + "%");
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != deleted) {
			finder.append(" and bean.deleted = :deleted");
			finder.setParam("deleted", deleted);
		}
		if (null != checked) {
			finder.append(" and bean.checked = :checked");
			finder.setParam("checked", checked);
		}
		if (null != orderBy && orderBy.equals(1)) {
			finder.append(" order by bean.createTime desc");
		} else {
			finder.append(" order by bean.createTime desc");
		}
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Integer msgType, Date startTime,
			Date endTime, String senderName, String msgContent, Integer status, Boolean deleted, Boolean checked,
			Integer orderBy) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		if (null != liveId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", liveId);
		}
		if (null != liveMsgType) {
			finder.append(" and bean.liveMsgType = :liveMsgType");
			finder.setParam("liveMsgType", liveMsgType);
		}
		if (null != msgType) {
			finder.append(" and bean.msgType = :msgType");
			finder.setParam("msgType", msgType);
		}
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if (!StringUtils.isBlank(senderName)) {
			finder.append(" and bean.senderName like :senderName");
			finder.setParam("senderName", "%" + senderName + "%");
		}
		if (!StringUtils.isBlank(msgContent)) {
			finder.append(" and bean.msgContent like :msgContent");
			finder.setParam("msgContent", "%" + msgContent + "%");
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != deleted) {
			finder.append(" and bean.deleted = :deleted");
			finder.setParam("deleted", deleted);
		}
		if (null != checked) {
			finder.append(" and bean.checked = :checked");
			finder.setParam("checked", checked);
		}
		if (null != orderBy && orderBy.equals(1)) {
			finder.append(" order by bean.createTime desc");
		} else {
			finder.append(" order by bean.createTime asc");
		}
		if (null != orderBy && orderBy.equals(2)) {
			finder.append(" order by bean.senderLevel desc");
		} else {
			finder.append(" order by bean.senderLevel asc");
		}
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	public List<ILiveMessage> getList(Integer liveId, Integer liveMsgType, Long startId, Integer size) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		if (null != liveId) {
			finder.append(" and bean.live.liveId = :liveId");
			finder.setParam("liveId", liveId);
		}
		if (null != liveMsgType) {
			finder.append(" and bean.liveMsgType = :liveMsgType");
			finder.setParam("liveMsgType", liveMsgType);
		}
		if (null != startId) {
			finder.append(" and bean.msgId < :startId");
			finder.setParam("startId", startId);
		}
		finder.append(" and bean.deleted = :deleted");
		finder.setParam("deleted", false);
		finder.append(" order by bean.createTime desc");
		finder.setMaxResults(size);
		return find(finder);
	}

	public ILiveMessage findById(Long msgId) {
		ILiveMessage entity = get(msgId);
		return entity;
	}

	public ILiveMessage save(ILiveMessage bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveMessage bean) {
		getSession().update(bean);
	}

	public int countMessageNum(Integer liveId, Integer liveMsgType, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		finder.append(" and bean.deleted!=:deleted").setParam("deleted", true);
		if (null != liveId) {
			finder.append(" and bean.live.liveId = :liveId");
			finder.setParam("liveId", liveId);
		}
		if (null != liveMsgType) {
			finder.append(" and bean.liveMsgType = :liveMsgType");
			finder.setParam("liveMsgType", liveMsgType);
		}
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		return countQueryResult(finder);
	}

	public ILiveMessage deleteById(Long msgId) {
		ILiveMessage entity = super.get(msgId);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ILiveMessage> getEntityClass() {
		return ILiveMessage.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMessage> selectILiveMessageMngByEventId(Long liveEventId) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where bean.live.liveEventId = :liveEventId");
		finder.setParam("liveEventId", liveEventId);
		finder.append(" order by bean.createTime desc");
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMessage> selectILiveMessageMngByEventIdAndType(Long liveEventId, Integer liveMsgType,
			Integer pageNo) {
		int startRow = (pageNo - 1) * 10;
		String sql = "SELECT * FROM ilive_message WHERE live_id = " + liveEventId + " AND live_msg_type = "
				+ liveMsgType + " ORDER BY create_time LIMIT " + startRow + ",10";
		Query query = this.getSession().createSQLQuery(sql).addEntity(ILiveMessage.class);
		List<ILiveMessage> list = query.list();
		return list;
	}

	@Override
	public int getNumByEventIdAndType(Long liveEventId, Integer liveMsgType) {
		String hql = "select count(bean) from ILiveMessage bean where bean.live.liveEventId = ? and bean.liveMsgType = ?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, liveEventId);
		query.setParameter(1, liveMsgType);
		int count = ((Long) query.uniqueResult()).intValue();
		return count;
	}

	@Override
	public Pagination getPage(Integer roomId, Integer interactType, String searchContent, int cpn, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where bean.liveRoomId = :liveRoomId");
		finder.setParam("liveRoomId", roomId);
		finder.append(" and bean.liveMsgType = :liveMsgType");
		finder.setParam("liveMsgType", interactType);
		if (searchContent != null && !searchContent.trim().equals("")) {
			finder.append(" and bean.msgContent like :msgContent");
			finder.setParam("msgContent", "%" + searchContent + "%");
		}
		finder.append(" and bean.deleted = :deleted");
		finder.setParam("deleted", false);
		finder.append(" order by bean.createTime desc");
		return find(finder, cpn, pageSize);
	}

	@Override
	public Pagination getPageRecordByUser(Long userId, Integer roomId, String keyword, Integer pageNo,
			Integer pageSize) {
		Finder finder = getFinder(userId,roomId,keyword);
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}
	
	private Finder getFinder(Long userId, Integer roomId, String keyword) {
		Finder finder = Finder.create("from ILiveMessage");
		finder.append(" where senderId = :senderId");
		finder.setParam("senderId", userId);
		finder.append(" and msgType = 1");
		if (roomId!=null) {
			finder.append(" and liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", roomId);
		}
		if (keyword!=null) {
			finder.append(" and msgContent like :msgContent");
			finder.setParam("msgContent","%" + keyword+"%");
		}
		finder.append(" order by createTime desc");
		return finder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMessage> getListRecordByUser(Long userId, Integer roomId, String keyword) {
		Finder finder = getFinder(userId,roomId,keyword);
		return find(finder);
	}

}