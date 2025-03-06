package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Repository
public class ILiveLiveRoomDaoImpl extends HibernateBaseDao<ILiveLiveRoom, Integer> implements ILiveLiveRoomDao {
	public Pagination getPage(Integer id, String roomName, Integer roomId, String urlAddr, String pullAddr,
			Integer isOpened, Timestamp createTime, String userId, String userName, int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveLiveRoom bean");
		finder.append(" where 1=1");
		if (null != id) {
			finder.append(" and bean.id = :id");
			finder.setParam("id", id);
		}
		if (!StringUtils.isBlank(roomName)) {
			finder.append(" and bean.roomName = :roomName");
			finder.setParam("roomName", "%" + roomName + "%");
		}
		if (null != roomId) {
			finder.append(" and bean.roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		if (!StringUtils.isBlank(urlAddr)) {
			finder.append(" and bean.urlAddr = :urlAddr");
			finder.setParam("urlAddr", "%" + urlAddr + "%");
		}
		if (!StringUtils.isBlank(pullAddr)) {
			finder.append(" and bean.pullAddr = :pullAddr");
			finder.setParam("pullAddr", "%" + pullAddr + "%");
		}
		if (null != isOpened) {
			finder.append(" and bean.isOpened = :isOpened");
			finder.setParam("isOpened", isOpened);
		}
		if (!StringUtils.isBlank(userId)) {
			finder.append(" and bean.userId = :userId");
			finder.setParam("userId", "%" + userId + "%");
		}
		if (!StringUtils.isBlank(userName)) {
			finder.append(" and bean.userName = :userName");
			finder.setParam("userName", "%" + userName + "%");
		}
		finder.append(" order by bean.createTime desc");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<ILiveLiveRoom> findByNumber(String roomName) {
		Finder finder = Finder.create("select bean from ILiveLiveRoom bean");
		finder.append(" where 1=1");
		if (!StringUtils.isBlank(roomName)) {
			finder.append(" and bean.roomName like:roomName");
			finder.setParam("roomName", "%" + roomName + "%");
		}
		return find(finder);
	}

	public ILiveLiveRoom findById(Integer id) {
		ILiveLiveRoom entity = get(id);
		return entity;
	}

	public ILiveLiveRoom save(ILiveLiveRoom bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveLiveRoom bean) {
		getSession().update(bean);
	}

	public ILiveLiveRoom deleteById(Integer id) {
		ILiveLiveRoom entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	public ILiveLiveRoom findByRoomId(Integer roomId) {
		Finder finder = Finder.create("select bean from ILiveLiveRoom bean");
		finder.append(" where 1=1");
		if (null != roomId) {
			finder.append(" and bean.roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		List find1 = find(finder);
		if (find1.isEmpty()) {
			return null;
		} else {
			return (ILiveLiveRoom) find1.get(0);
		}
	}

	@Override
	public List<ILiveLiveRoom> findRoomList() {
		Finder finder = Finder.create(
				"select bean from ILiveLiveRoom bean WHERE bean.openStatus=:openStatus  order by bean.createTime desc");
		finder.setParam("openStatus", 1);
		return find(finder);
	}

	@Override
	protected Class<ILiveLiveRoom> getEntityClass() {
		return ILiveLiveRoom.class;
	}

	@Override
	public Pagination getPager(String roomName, Integer liveStatus, Integer pageNo, Integer pageSize, Long managerId) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		if (liveStatus != null) {
			finder.append(" and room.liveEvent.liveStatus =:liveStatus");
			finder.setParam("liveStatus", liveStatus);
			finder.append(" and room.openStatus =:openStatus");
			finder.setParam("openStatus", 1);
		}
		if (roomName != null) {
			// 如果是数字
			boolean roomNameInt = StringPatternUtil.isInteger(roomName);
			if (roomNameInt) {
				finder.append(" and room.roomId like :roomId");
				finder.setParam("roomId", "%" + roomName + "%");
			} else {
				// 如果是文字
				finder.append(" and room.liveEvent.liveTitle like :liveTitle");
				finder.setParam("liveTitle", "%" + roomName + "%");
			}
		}
		finder.append(" and room.managerId =:managerId");
		finder.setParam("managerId", managerId);
		finder.append(" and room.deleteStatus =:deleteStatus");
		finder.setParam("deleteStatus", 0);
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public void updateILiveRoom(ILiveLiveRoom liveRoom) {
		this.update(liveRoom);
	}

	@Override
	public boolean saveRoom(ILiveLiveRoom liveRoom) {
		this.getSession().save(liveRoom);
		return true;
	}

	@Override
	public ILiveLiveRoom getILiveRoom(Integer liveRoomId) {
		return this.get(liveRoomId);
	}

	@Override
	public Pagination getPager(Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		String enterpriseStr = ConfigUtils.get("defaultEnterpriseId");
		if (keyWord != null) {
			boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
			if (roomNameInt) {
				finder.append(" and cast(room.roomId as string) like :roomId");
				finder.setParam("roomId", "%" + keyWord + "%");
			} else {
				finder.append(" and room.liveEvent.liveTitle like :liveTitle");
				finder.setParam("liveTitle", "%" + keyWord + "%");
			}
			finder.append(" and room.deleteStatus =:deleteStatus");
			finder.setParam("deleteStatus", 0);
			finder.append(" and room.enterpriseId !=:enterpriseId");
			finder.setParam("enterpriseId", Integer.parseInt(enterpriseStr));

			finder.append(" or ( room.liveEvent.hostName like :hostName");
			finder.setParam("hostName", "%" + keyWord + "%");
			finder.append(" and room.deleteStatus =:deleteStatus");
			finder.setParam("deleteStatus", 0);
			finder.append(" and room.enterpriseId !=:enterpriseId )");
			finder.setParam("enterpriseId", Integer.parseInt(enterpriseStr));
		} else {
			finder.append(" and room.deleteStatus =:deleteStatus");
			finder.setParam("deleteStatus", 0);
			finder.append(" and room.enterpriseId !=:enterpriseId )");
			finder.setParam("enterpriseId", Integer.parseInt(enterpriseStr));
		}
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Pagination getPagerForOperator(Long userId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where 1=1 ");
		if (userId != null) {
			finder.append(" and room.managerId =:managerId");
			finder.setParam("managerId", userId);
		}
		finder.append(" and room.deleteStatus =:deleteStatus");
		finder.setParam("deleteStatus", 0);
		finder.append(" order by room.createTime desc ");
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLiveRoom> findRoomListByEnterprise(Integer enterpriseId) {
		Finder finder = Finder.create(
				"select bean from ILiveLiveRoom bean WHERE bean.openStatus=:openStatus and bean.enterpriseId = :enterpriseId  order by bean.createTime desc");
		finder.setParam("openStatus", 1);
		finder.setParam("enterpriseId", enterpriseId);
		return find(finder);
	}

	@Override
	public Pagination getPager(Integer formroomtype, Integer formexamine, Integer formlivestate, Integer formroomstate,
			String queryName, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select bean from ILiveLiveRoom bean");
		finder.append(" where 1=1");
		if (null != formroomtype) {
			finder.append(" and bean.useState = :useState");
			finder.setParam("useState", formroomtype);
		}
		if (formexamine!=null) {
			finder.append(" and bean.liveEvent.liveStatus = :formexamine");
			finder.setParam("formexamine", formexamine);
		}
		if (formlivestate!=null) {
			finder.append(" and bean.liveEvent.liveStatus = :formlivestate");
			finder.setParam("formlivestate", formlivestate);
		}
		if (formroomstate!=null) {
			switch(formroomstate) {
			case 0:
				finder.append(" and bean.openStatus = :openStatus");
				finder.setParam("openStatus", formroomstate);
				break;
			case 1:
				finder.append(" and bean.openStatus = :openStatus");
				finder.setParam("openStatus", formroomstate);
				break;
			case 2:
				finder.append(" and bean.deleteStatus = :deleteStatus");
				finder.setParam("deleteStatus", 1);
				break;
			case 3:
				finder.append(" and bean.disabled = :disabled");
				finder.setParam("disabled", 1);
				break;
			}
		}
		if(queryName!=null) {
			finder.append(" and (bean.liveEvent.liveTitle like :query or bean.liveEvent.hostName like :query) ");
			finder.setParam("query", "%"+queryName+"%");
		}
		finder.append(" order by bean.createTime desc");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Pagination getPagerNew(List<Object>roomIds,Integer liveStatus,Integer pageNo, Integer pageSize){
		Finder finder = Finder.create("select room from ILiveLiveRoom room where deleteStatus = 0 and liveEvent.liveStatus = :liveStatus and room.roomId in(:roomIds)");
		finder.append(" order by room.createTime desc ");
		finder.setParam("liveStatus", liveStatus);
		finder.setParamList("roomIds", roomIds);
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public int queryNumbyState(Integer state) {
		StringBuilder hql = new StringBuilder("select count(*) from ILiveLiveRoom");
		if(state!=null) {
			hql.append(" where liveEvent.liveStatus = "+state+" ");
		}
		Query query = this.getSession().createQuery(hql.toString());
		if((Long)query.uniqueResult() == null) {
			return 0;
		}
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

	@Override
	public void updateDisabledById(Integer roomId,Integer disabled) {
		ILiveLiveRoom iLiveLiveRoom = this.get(roomId);
		iLiveLiveRoom.setDisabled(disabled);
		this.getSession().save(iLiveLiveRoom);
	}

	@Override
	public void updateLiveStatusById(Integer roomId, Integer liveStatus) {
		ILiveLiveRoom iLiveLiveRoom = this.get(roomId);
		ILiveEvent liveEvent = iLiveLiveRoom.getLiveEvent();
		liveEvent.setLiveStatus(liveStatus);
		iLiveLiveRoom.setLiveEvent(liveEvent);
		this.getSession().save(iLiveLiveRoom);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLiveRoom> getRoomListByliveStatus(Integer liveStatus) {
		String hql = "from ILiveLiveRoom where liveEvent.liveStatus = :liveStatus";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("liveStatus", liveStatus);
		List<ILiveLiveRoom> list = query.list();
		return list;
	}

	@Override
	public Pagination getPager(Integer liveStatus,Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("select room from ILiveLiveRoom room where deleteStatus = 0 and liveEvent.liveStatus = :liveStatus");
		finder.append(" order by room.createTime desc ");
		finder.setParam("liveStatus", liveStatus);
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}


}