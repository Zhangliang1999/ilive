package com.bvRadio.iLive.iLive.manager;

import java.text.ParseException;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ParamsBean;

public interface ILiveLiveMng {
	public Pagination getPage(Integer channelId, String liveName, Integer status, Boolean deleted,
			int pageNo, int pageSize,Integer liveType,Integer userId);
	
	public Pagination getPageL(Integer channelId,String creatorName, String liveName, Integer status, Boolean deleted,
			int pageNo, int pageSize,Integer userId);
	
	public List<ILiveEvent> getList(Integer channelId, String liveName, Integer status, Boolean deleted);

	public List<ILiveEvent> getListB(Integer liveType,Integer channelId,String creatorName,String lianmai,String liveName, Integer status, Boolean deleted);
	
	public ILiveEvent findById(Integer liveId);
	
	public ILiveEvent findByRoomId(Integer roomId);

	public ILiveEvent save(ILiveEvent bean);

	public ILiveEvent deleteById(Integer liveId);

	public ILiveEvent[] deleteByIds(Integer[] ids);

	public ILiveEvent onlineById(Integer liveId);

	public ILiveEvent[] onlineByIds(Integer[] ids);

	public ILiveEvent offlineById(Integer liveId);

	public ILiveEvent[] offlineByIds(Integer[] ids);

	public ILiveEvent update(ILiveEvent bean);

	public void addJoinNumById(Integer liveId);

	public void addMsgNumById(Integer liveId);

	public void substractMsgNumById(Integer liveId);

	public void addViewNumById(Integer liveId);
	
	public  String meetingcreate(List<ParamsBean> list,String url);

	public ILiveEvent RecordLive(Integer id,long startTime,long endTime) throws ParseException;

}
