package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppILiveRoom;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.front.vo.AppViewRecordVo;
import com.bvRadio.iLive.iLive.dao.ILiveViewRecordDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveViewRecord;
import com.bvRadio.iLive.iLive.manager.ILiveEventMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveViewRecordMng;

public class ILiveViewRecordMngImpl implements ILiveViewRecordMng {

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	@Autowired
	private ILiveViewRecordDao iLiveViewRecordDao;

	@Autowired
	private ILiveMediaFileMng iLiveMediaFileMng;

	@Autowired
	private ILiveEventMng iLiveEventMng;

	@Override
	@Transactional
	public boolean addILiveViewRecord(ILiveViewRecord viewRecord) {
		try {
			ILiveViewRecord view = iLiveViewRecordDao.getIliveViewByViewVo(viewRecord);
			if (view == null) {
				Long recordId = iLiveFieldIdManagerMng.getNextId("ilive_view_record", "record_id", 1);
				viewRecord.setRecordId(recordId);
				viewRecord.setDeleteStatus(0);
				viewRecord.setRecordTime(new Timestamp(System.currentTimeMillis()));
				// 新增
				boolean addILiveViewRecord = iLiveViewRecordDao.addILiveViewRecord(viewRecord);
				return addILiveViewRecord;
			} else {
				// 修改
				view = this.convertVo2Po(viewRecord, view);
				boolean addILiveViewRecord = iLiveViewRecordDao.updateILiveViewRecord(view);
				return addILiveViewRecord;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	private ILiveViewRecord convertVo2Po(ILiveViewRecord viewRecord, ILiveViewRecord view) {
		view.setRecordTime(new Timestamp(System.currentTimeMillis()));
		view.setViewOrigin(viewRecord.getViewOrigin());
		return view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppViewRecordVo> getILiveViewRecords(Map<String, Object> sqlMap) {
		Pagination pagination = iLiveViewRecordDao.getILiveViewRecords(sqlMap);
		List<AppViewRecordVo> recordList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (pagination != null) {
			AppViewRecordVo recordVo = null;
			List<ILiveViewRecord> list = (List<ILiveViewRecord>) pagination.getList();
			if (list != null && !list.isEmpty()) {
				for (ILiveViewRecord viewRc : list) {
					Integer viewType = viewRc.getViewType();
					Long outerId = viewRc.getOuterId();
					// 直播查询
					if (viewType.intValue() == 1) {
						ILiveEvent iLiveEvent = iLiveEventMng.selectILiveEventByID(outerId);
						recordVo = new AppViewRecordVo();
						recordVo = this.convertRoomPo2Vo(iLiveEvent, recordVo);
					}
					// 回看查询
					else {
						ILiveMediaFile mediaFile = iLiveMediaFileMng.selectILiveMediaFileByFileId(outerId);
						recordVo = new AppViewRecordVo();
						recordVo = this.convertFilePo2Vo(mediaFile, recordVo, sdf);
					}
					recordVo.setViewType(viewType);
					recordVo.setViewTime(sdf.format(viewRc.getRecordTime()));
					recordVo.setRecordId(viewRc.getRecordId());
					recordList.add(recordVo);
				}
			}

		}
		return recordList;
	}

	/**
	 * 
	 * @param mediaFile
	 * @param recordVo
	 * @return
	 */
	private AppViewRecordVo convertFilePo2Vo(ILiveMediaFile mediaFile, AppViewRecordVo recordVo, SimpleDateFormat sdf) {
		AppMediaFile appmediaFile = new AppMediaFile();
		appmediaFile.setCreateTime(sdf.format(mediaFile.getMediaCreateTime()));
		appmediaFile.setFileDuration(String.valueOf(mediaFile.getDuration() == null ? 0 : mediaFile.getDuration()));
		appmediaFile.setFileId(mediaFile.getFileId());
		appmediaFile.setFileImg(mediaFile.getFileCover() == null ? "" : mediaFile.getFileCover());
		appmediaFile.setFileName(mediaFile.getMediaFileName());
		appmediaFile.setViewNum(0L);
		appmediaFile.setFileSize(mediaFile.getFileSizeMb());
		appmediaFile.setRoomId(mediaFile.getLiveRoomId());
		appmediaFile.setEnterpriseId(mediaFile.getEnterpriseId().intValue());
		appmediaFile.setUserId(mediaFile.getUserId());
		appmediaFile.setLiveEventId(mediaFile.getLiveEventId());
		recordVo.setAppMediaFile(appmediaFile);
		return recordVo;
	}

	/**
	 * 
	 * @param liveRoom
	 * @param recordVo
	 * @return
	 */
	private AppViewRecordVo convertRoomPo2Vo(ILiveEvent liveEvent, AppViewRecordVo recordVo) {
		AppILiveRoom appILiveRoom = new AppILiveRoom();
		appILiveRoom.setRoomId(liveEvent.getRoomId());
		appILiveRoom.setRoomDesc(liveEvent.getLiveDesc());
		appILiveRoom.setRoomImg(liveEvent.getConverAddr());
		appILiveRoom.setRoomName(liveEvent.getLiveTitle());
		appILiveRoom.setLiveStatus(liveEvent.getLiveStatus());
		recordVo.setAppILiveRoom(appILiveRoom);
		return recordVo;
	}

	@Override
	public ILiveViewRecord getLiveViewRecordById(Long recordId) {
		return iLiveViewRecordDao.getLiveViewRecordById(recordId);
	}

	@Override
	@Transactional
	public void updateLiveRecord(ILiveViewRecord liveViewRecord) {
		// TODO Auto-generated method stub
		iLiveViewRecordDao.updateILiveViewRecord(liveViewRecord);
	}

}
