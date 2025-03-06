package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;

public interface ILiveLiveDao {
	public Pagination getPage(Integer channelId, String liveName, Integer status, Boolean deleted,
			int pageNo, int pageSize,Integer liveType,Integer userId);

	public Pagination getPageL(Integer channelId,String creatorName, String liveName, Integer status, Boolean deleted,
			int pageNo, int pageSize, Integer userId);
	
	public List<ILiveEvent> getList(Integer channelId, String liveName, Integer status, Boolean deleted);

	public List<ILiveEvent> getListB(Integer liveType,Integer channelId,String creatorName,String lianmai, String liveName, Integer status, Boolean deleted);
	
	public ILiveEvent findById(Integer id);
	
	public List<ILiveEvent> findByRoomId(Integer roomId);

	public ILiveEvent save(ILiveEvent bean);

	public ILiveEvent deleteById(Integer id);

	public void update(ILiveEvent bean);

	public ILiveEvent updateByUpdater(Updater<ILiveEvent> updater);

}