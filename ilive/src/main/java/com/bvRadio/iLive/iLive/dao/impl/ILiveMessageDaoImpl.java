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
	public Pagination getPage(Integer liveRoomId, Integer liveMsgType, Integer msgType, Date startTime, Date endTime,
			String senderName, String msgContent, Integer status, Boolean deleted, Boolean checked, Integer orderBy,
			int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		if (null != liveRoomId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", liveRoomId);
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
	public List<ILiveMessage> getList(Integer liveRoomId, Integer liveMsgType, Integer msgType, Date startTime,
			Date endTime, String senderName, String msgContent, Integer status, Boolean deleted, Boolean checked,
			Integer orderBy,Boolean emptyAll) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		if (null != liveRoomId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", liveRoomId);
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
		if(emptyAll!=null){
			finder.append(" and bean.emptyAll = :emptyAll");
			finder.setParam("emptyAll", emptyAll);
		}else{
			
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
	public List<ILiveMessage> getList(Integer liveRoomId, Integer liveMsgType, Long startId, Integer size) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		if (null != liveRoomId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", liveRoomId);
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
	@SuppressWarnings("unchecked")
	public List<ILiveMessage> getListForWeb(Integer liveRoomId, Integer liveMsgType, Integer status, Boolean deleted,
			Boolean checked, Long startId, Integer size) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		if (null != liveRoomId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", liveRoomId);
		}
		if (null != liveMsgType) {
			finder.append(" and bean.liveMsgType = :liveMsgType");
			finder.setParam("liveMsgType", liveMsgType);
		}
		if (null != startId) {
			finder.append(" and bean.msgId > :startId");
			finder.setParam("startId", startId);
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
		finder.append(" order by bean.msgId desc");
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

	public int countMessageNum(Integer liveRoomId, Integer liveMsgType, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from ILiveMessage bean");
		finder.append(" where 1=1");
		finder.append(" and bean.deleted!=:deleted").setParam("deleted", true);
		if (null != liveRoomId) {
			finder.append(" and bean.liveRoomId = :liveRoomId");
			finder.setParam("liveRoomId", liveRoomId);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveMessage> selectILiveMessageMngByEventIdAndType(Long liveEventId, Integer liveMsgType,
			Integer pageNo , Integer count , Integer check) {
		int startRow = (pageNo - 1) * 10;
		String sql = "SELECT * FROM ilive_message WHERE live_id = " + liveEventId + " AND live_msg_type = "
				+ liveMsgType + " AND is_checked = "+check+" ORDER BY create_time DESC LIMIT " + startRow + ","+count;
		Query query = this.getSession().createSQLQuery(sql).addEntity(ILiveMessage.class);
		List<ILiveMessage> list = query.list();
		return list;
	}
	
	@Override
	public int getNumByEventIdAndType(Long liveEventId, Integer liveMsgType , boolean check) {
		String hql = "select count(bean) from ILiveMessage bean where bean.live.liveEventId = ? and bean.liveMsgType = ? and checked=? ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, liveEventId);
		query.setParameter(1, liveMsgType);
		query.setParameter(2, check);
		int count = ((Long) query.uniqueResult()).intValue();
		return count;
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
	public Pagination getQuestionAndAnwer(Long userId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(
				" from ILiveMessage where liveMsgType=3 and senderId=:senderId and deleted=0 order by createTime desc ");
		finder.setParam("senderId", userId);
		Pagination find = this.find(finder, pageNo, pageSize);
		return find;
	}

}