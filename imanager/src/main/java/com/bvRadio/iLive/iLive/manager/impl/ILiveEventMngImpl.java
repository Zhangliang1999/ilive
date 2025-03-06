package com.bvRadio.iLive.iLive.manager.impl;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILIvePageDecorateDao;
import com.bvRadio.iLive.iLive.dao.ILiveEventDao;
import com.bvRadio.iLive.iLive.dao.ILiveLiveRoomDao;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.web.ApplicationCache;

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
	private ILIvePageDecorateDao pageDecorateDao;

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
		if (iLiveEvent.getPageRecordList() != null && !iLiveEvent.getPageRecordList().isEmpty()) {
			Long nextPageId = filedIdMng.getNextId("ilive_page_decorate", "id", iLiveEvent.getPageRecordList().size());
			long firstPageId = nextPageId + 1 - iLiveEvent.getPageRecordList().size();
			for (PageDecorate page : iLiveEvent.getPageRecordList()) {
				PageDecorate newPage = new PageDecorate();
				BeanUtils.copyProperties(page, newPage);
				newPage.setId(firstPageId);
				newPage.setLiveEventId(iLiveEvent.getLiveEventId());
				pageDecorateDao.addPageDecorate(newPage);
				firstPageId++;
			}
		}
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
	public Pagination getPageByRoomId(Integer roomId, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		return iLiveEventDao.getPageByRoomId(roomId,pageSize,pageNo);
	}

	@Override
	public List<ILiveEvent> findAllEventByRoomId(Integer roomId) {
		return iLiveEventDao.findAllEventByRoomId(roomId);
	}

}
