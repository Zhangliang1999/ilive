package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.iLive.dao.ILiveLiveDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;

@Repository
public class ILiveLiveDaoImpl extends HibernateBaseDao<ILiveEvent, Integer> implements ILiveLiveDao {
	public Pagination getPage(Integer channelId, String liveName, Integer status, Boolean deleted,
			int pageNo, int pageSize,Integer liveType,Integer userId) {
		Finder finder = Finder.create("select bean from ILiveEvent bean");
		finder.append(" where 1=1");
		if (null != channelId) {
			finder.append(" and bean.channelId = :channelId");
			finder.setParam("channelId", channelId);
		}
		if (!StringUtils.isBlank(liveName)) {
			finder.append(" and bean.liveName like :liveName");
			finder.setParam("liveName", "%" + liveName + "%");
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != deleted) {
			finder.append(" and bean.deleted = :deleted");
			finder.setParam("deleted", deleted);
		}
		if (null != liveType) {
			finder.append(" and bean.liveType = :liveType");
			finder.setParam("liveType", liveType);
		}
		if (null != userId) {
			finder.append(" and bean.userId = :userId");
			finder.setParam("userId", userId);
		}
		finder.append(" order by bean.createTime desc");
		return find(finder, pageNo, pageSize);
	}
	public Pagination getPageL(Integer channelId, String creatorName,String liveName, Integer status, Boolean deleted,
			int pageNo, int pageSize,Integer userId) {
		Finder finder = Finder.create("select bean from ILiveEvent bean");
		finder.append(" where 1=1");
		if (null != channelId) {
			finder.append(" and bean.channelId = :channelId");
			finder.setParam("channelId", channelId);
		}
		if (!StringUtils.isBlank(creatorName)) {
			finder.append(" and bean.creatorName = :creatorName");
			finder.setParam("creatorName",  creatorName );
		}
		if (!StringUtils.isBlank(liveName)) {
			finder.append(" and bean.liveName like :liveName");
			finder.setParam("liveName", "%" + liveName + "%");
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != deleted) {
			finder.append(" and bean.deleted = :deleted");
			finder.setParam("deleted", deleted);
		}
		if (null != userId) {
			finder.append(" and bean.userId = :userId");
			finder.setParam("userId", userId);
		}
		finder.append(" order by bean.createTime desc");
		return find(finder, pageNo, pageSize);
	}
	@SuppressWarnings("unchecked")
	public List<ILiveEvent> getList(Integer channelId, String liveName, Integer status, Boolean deleted) {
		Finder finder = Finder.create("select bean from ILiveEvent bean");
		finder.append(" where 1=1");
		if (null != channelId) {
			finder.append(" and bean.channelId = :channelId");
			finder.setParam("channelId", channelId);
		}
		if (!StringUtils.isBlank(liveName)) {
			finder.append(" and bean.liveName like :liveName");
			finder.setParam("liveName", "%" + liveName + "%");
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != deleted) {
			finder.append(" and bean.deleted = :deleted");
			finder.setParam("deleted", deleted);
		}
		
		finder.append(" order by bean.createTime desc");
		return find(finder);
	}

	@SuppressWarnings("unchecked")
	public List<ILiveEvent> getListB(Integer liveType,Integer channelId, String creatorName,String lianmai,String liveName, Integer status, Boolean deleted) {
		Finder finder = Finder.create("select bean from ILiveEvent bean");
		finder.append(" where 1=1");
		if (null != liveType) {
			finder.append(" and bean.liveType =:liveType");
			finder.setParam("liveType", liveType);
		}
		if (null != channelId) {
			finder.append(" and bean.channelId = :channelId");
			finder.setParam("channelId", channelId);
		}
		if (!StringUtils.isBlank(creatorName)) {
			finder.append(" and bean.creatorName = :creatorName");
			finder.setParam("creatorName",   creatorName );
		}
		if (!StringUtils.isBlank(lianmai)) {
			boolean islianmai = false;
			if("1".equals(lianmai)){
				islianmai = true;
			}else if("0".equals(lianmai)){
				islianmai = false;
			}
			finder.append(" and bean.lianmai = :lianmai");
			finder.setParam("lianmai",   islianmai );
		}
		if (!StringUtils.isBlank(liveName)) {
			finder.append(" and bean.liveName like :liveName");
			finder.setParam("liveName", "%" + liveName + "%");
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != deleted) {
			finder.append(" and bean.deleted = :deleted");
			finder.setParam("deleted", deleted);
		}
		finder.append(" order by bean.createTime desc");
		return find(finder);
	}
	
	public ILiveEvent findById(Integer id) {
		ILiveEvent entity = get(id);
		return entity;
	}
	
	public List<ILiveEvent> findByRoomId(Integer roomId) {
		Finder finder = Finder.create("select bean from ILiveEvent bean");
		finder.append(" where 1=1");
		if (null != roomId) {
			finder.append(" and bean.roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		return find(finder);
	}

	public ILiveEvent save(ILiveEvent bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveEvent bean) {
		getSession().update(bean);
	}

	public ILiveEvent deleteById(Integer id) {
		ILiveEvent entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ILiveEvent> getEntityClass() {
		return ILiveEvent.class;
	}

}