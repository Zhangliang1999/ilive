package com.bvRadio.iLive.iLive.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.LiveEventsVo;
import com.bvRadio.iLive.iLive.dao.ILiveEventDao;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileDao;
import com.bvRadio.iLive.iLive.dao.ILiveRoomStaticsDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveRoomStatics;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomStaticsMng;
import com.bvRadio.iLive.iLive.util.JedisUtils;
import com.bvRadio.iLive.iLive.web.ApplicationCache;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

@Service
@Transactional
public class ILiveEventMngImpl implements ILiveEventMng {

	@Autowired
	private ILiveEventDao iLiveEventDao;

	@Autowired
	private ILiveLiveRoomDao iLiveLiveRoomDao;

	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;

	@Autowired
	private ILiveMediaFileDao iLiveMediaFileDao;// 视频

	@Autowired
	private ILiveRoomStaticsDao iLiveRoomStaticsDao;

	@Autowired
	private ILiveRoomStaticsMng iLiveRoomStaticsMng;

	@Override
	public void putLiveEventUserCache(Long eventId) {
		try {
			Long orgNum=0L;
			
			if(JedisUtils.exists("onlineNumber:"+eventId)) {
				try {
					orgNum=Long.parseLong(JedisUtils.get("onlineNumber:"+eventId));
				} catch (Exception e) {
					ILiveRoomStatics roomStaticsByEventId = iLiveRoomStaticsDao.getRoomStaticsByEventId(eventId);
					if (roomStaticsByEventId == null) {
					} else {
						orgNum = roomStaticsByEventId.getShowNum();
						JedisUtils.set("onlineNumber:"+eventId, (orgNum+1)+"", 300);
					}
				}
				
			}else {
				ILiveRoomStatics roomStaticsByEventId = iLiveRoomStaticsDao.getRoomStaticsByEventId(eventId);
				Long showNum = 0L;
				if (roomStaticsByEventId == null) {
				} else {
					showNum = roomStaticsByEventId.getShowNum();
				}
				orgNum = showNum;
				
			}
			
			Long finalNum = orgNum + 1;
			JedisUtils.del("onlineNumber:"+eventId);
			JedisUtils.set("onlineNumber:"+eventId, finalNum+"", 300);
			ApplicationCache.LiveEventUserCache.put(eventId, finalNum);
			System.out.println(eventId + "####################开始加入人数##############" + finalNum);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationCache.LiveEventUserCache.put(eventId, 0L);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveEvent selectILiveEventByID(Long liveEventId) {
		ILiveEvent iLiveEvent = iLiveEventDao.selectILiveEventByID(liveEventId);
		return iLiveEvent;
	}

	@Override
	public boolean saveIliveMng(ILiveEvent iLiveEvent, Integer iliveRoomId) {
		Long nextId = filedIdMng.getNextId("ilive_event", "live_event_id", 1);
		iLiveEvent.setLiveEventId(nextId);
		iLiveEvent.setIsNotify(0);
		Long liveEventId = iLiveEventDao.saveIliveMng(iLiveEvent);
		iLiveEvent.setLiveEventId(liveEventId);
		ILiveLiveRoom liveRoom = iLiveLiveRoomDao.findById(iliveRoomId);
		liveRoom.setLiveEvent(iLiveEvent);
		iLiveLiveRoomDao.updateILiveRoom(liveRoom);
		Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
		roomListMap.put(liveRoom.getRoomId(), iLiveEvent.getAutoCheckSecond());
		return true;
	}

	@Override
	public Long saveIliveMng(ILiveEvent iLiveEvent) {
		Long nextId = filedIdMng.getNextId("ilive_event", "live_event_id", 1);
		iLiveEvent.setLiveEventId(nextId);
		iLiveEvent.setIsNotify(0);
		Long saveIliveMng = iLiveEventDao.saveIliveMng(iLiveEvent);
		Hashtable<Integer, Integer> roomListMap = ApplicationCache.getRoomListMap();
		roomListMap.put(iLiveEvent.getRoomId(), iLiveEvent.getAutoCheckSecond());
		return saveIliveMng;
	}

	@Override
	public void updateILiveEvent(ILiveEvent dbEvent) {
		iLiveEventDao.updateILiveEvent(dbEvent);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination selectILiveEventPage(Integer roomId, Long liveEventId, Integer pageNo, Integer pageSize) {
		Pagination pagination = new Pagination(pageNo, pageSize, 0);
		try {
			pagination = iLiveEventDao.selectILiveEventPage(roomId, liveEventId, pageNo, pageSize);
			List<ILiveEvent> list = (List<ILiveEvent>) pagination.getList();
			for (ILiveEvent iLiveEvent : list) {
				Long eventId = iLiveEvent.getLiveEventId();
				List<ILiveMediaFile> files = iLiveMediaFileDao.selectILiveMediaFileListByEventId(eventId);
				iLiveEvent.setFileSize(files.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	public void updateILiveEventByIsDelete(Long liveEventId, boolean isDelete) {
		try {
			iLiveEventDao.updateILiveEventByIsDelete(liveEventId, isDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateILiveEventByCommentsAllow(Long evenId, Integer commentsAllow) {
		try {
			iLiveEventDao.updateILiveEventByCommentsAllow(evenId, commentsAllow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateILiveEventByCommentsAudit(Long evenId, Integer commentsAudit) {
		try {
			iLiveEventDao.updateILiveEventByCommentsAudit(evenId, commentsAudit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveEvent> getLiveEventByStartTime(Integer time) {
		return iLiveEventDao.getLiveEventByStartTime(time);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<LiveEventsVo> getLiveEventsByRoomId(Integer roomId, Integer pageNo, Integer pageSize) {
		Pagination pager = iLiveEventDao.getLiveEventsByRoomId(roomId, pageNo, pageSize);
		List<LiveEventsVo> events = new ArrayList<>();
		if (pager != null && pager.getList() != null && !pager.getList().isEmpty()) {
			List<ILiveEvent> eventList = (List<ILiveEvent>) pager.getList();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String baseUrl = ConfigUtils.get("statisticsAccessUrl");
			for (ILiveEvent event : eventList) {
				LiveEventsVo eventVo = this.convertEvent2Vo(event, sdf, baseUrl);
				long viewTotal = 0L;
				ILiveRoomStatics roomStatic = iLiveRoomStaticsMng.getRoomStatic(event.getLiveEventId());
				if (roomStatic != null) {
					Long showNum = roomStatic.getShowNum();
					if (showNum != null) {
						viewTotal = showNum;
					}
				}
				eventVo.setViewTotal(viewTotal);
				events.add(eventVo);
			}
		}
		return events;
	}

	private LiveEventsVo convertEvent2Vo(ILiveEvent event, SimpleDateFormat sdf, String baseUrl) {
		LiveEventsVo eventVo = new LiveEventsVo();
		eventVo.setAccessUrl(String.format("%s%s%s", baseUrl, "?id=", event.getLiveEventId()));
		eventVo.setCreateTime(sdf.format(event.getLiveStartTime()));
		eventVo.setLiveEventId(event.getLiveEventId());
		eventVo.setRoomDesc(event.getLiveDesc());
		eventVo.setRoomImg(event.getConverAddr());
		eventVo.setRoomName(event.getLiveTitle());
		eventVo.setRoomId(event.getRoomId());
		return eventVo;
	}

	@Override
	public List<ILiveEvent> getListEvent(Integer roomId) {
		return iLiveEventDao.getListEvent(roomId);
	}

}
