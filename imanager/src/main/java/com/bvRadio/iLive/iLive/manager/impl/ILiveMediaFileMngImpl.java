package com.bvRadio.iLive.iLive.manager.impl;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileDao;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;

/**
 * 媒资管理类实现
 * 
 * @author administrator
 */
@Service
public class ILiveMediaFileMngImpl implements ILiveMediaFileMng {

	@Autowired
	private ILiveMediaFileDao iLiveMediaFileDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldMng;

	@Transactional
	@Override
	public boolean saveIliveMediaFile(ILiveMediaFile iLiveMediaFile) {
		long fileId = iLiveFieldMng.getNextId("ilive_media_file", "file_id", 1);
		iLiveMediaFile.setFileId(fileId);
		iLiveMediaFileDao.saveILiveMediaFile(iLiveMediaFile);
		return true;
	}

	@Transactional
	@Override
	public void deleteVedio(Long id) {
		iLiveMediaFileDao.deleteLiveMediaFile(id);
	}

	@Transactional
	@Override
	public void deleteVediosByIds(Long[] ids) {
		iLiveMediaFileDao.deleteLiveMediaFilesByIds(ids);
	}

	@Override
	public Pagination selectILiveMediaFilePage(Long liveEventId, Integer pageNO, Integer pageSize, Integer delFlag,
			Integer fileType) {
		Pagination pagination = new Pagination(pageNO, pageSize, 0);
		try {
			pagination = iLiveMediaFileDao.selectILiveMediaFilePage(liveEventId, pageNO, pageSize, delFlag, fileType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	public List<ILiveMediaFile> selectILiveMediaFileListByEventId(Long liveEventId) {
		List<ILiveMediaFile> list = new ArrayList<ILiveMediaFile>();
		try {
			list = iLiveMediaFileDao.selectILiveMediaFileListByEventId(liveEventId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveMediaFile selectILiveMediaFileByFileId(Long fileId) {
		ILiveMediaFile iLiveMediaFile = new ILiveMediaFile();
		try {
			iLiveMediaFile = iLiveMediaFileDao.selectILiveMediaFileByFileId(fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveMediaFile;
	}

	@Transactional
	@Override
	public void updateVedioLineState(Long id, Integer state) {
		iLiveMediaFileDao.updateLiveMediaFileLineState(id, state);
	}

	@Transactional
	@Override
	public void updateVediosLineStateByIds(Long[] ids, Integer state) {
		iLiveMediaFileDao.updateLiveMediaFilesLineStateByIds(ids, state);
	}

	@Transactional(readOnly = true)
	@Override
	public Pagination getMediaFilePage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		Pagination pagination = iLiveMediaFileDao.getMedialFilePage(sqlParam, pageNo, pageSize);
		if (pagination != null && pagination.getList() != null && pagination.getList().size() > 0) {
			List<ILiveMediaFile> iLiveMediaFileList = (List<ILiveMediaFile>) pagination.getList();
			for (ILiveMediaFile iLiveMediaFile : iLiveMediaFileList) {
				// 换算时间
				Integer duration = iLiveMediaFile.getDuration();
				iLiveMediaFile.setDurationTime(convertTime(duration));
				// 换算大小单位，从kb-mb
				Long fileSize = iLiveMediaFile.getFileSize();
				iLiveMediaFile.setFileSizeMb(convertMemorySizeFromKb2Mb(fileSize));
				// 评论量
				if (iLiveMediaFile.getiLiveMediaFileCommentsSet() != null) {
					iLiveMediaFile.setCommentsCount(new Long(iLiveMediaFile.getiLiveMediaFileCommentsSet().size()));
				} else {
					iLiveMediaFile.setCommentsCount(new Long(0));
				}
			}
		}
		return pagination;
	}

	@Override
	@Transactional
	public void updateEventFileOnlineFlag(ILiveMediaFile file) {
		try {
			iLiveMediaFileDao.updateEventFileOnlineFlag(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Pagination selectILiveMediaFilePageByRoomId(Integer roomId, Integer pageNO, Integer pageSize,
			Integer delFlag, Integer fileType) {
		Pagination pagination = new Pagination(pageNO, pageSize, 0);
		try {
			pagination = iLiveMediaFileDao.selectILiveMediaFilePageByRoomId(roomId, pageNO, pageSize, delFlag,
					fileType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	public String selectLiveRecordTotalSize(Long userId, String mediaName) {
		return convertMemorySizeFromKb2Mb(iLiveMediaFileDao.selectLiveRecordTotalSize(userId, mediaName));
	}

	/**
	 * 个位数时间加上0前缀，十位数不用加
	 * 
	 * @param time
	 *            时间
	 * @return
	 */
	private String formatTime(int time) {
		if (time >= 0 && time < 10) {
			return "0" + time;
		}
		return "" + time;
	}

	/**
	 * 换算时间：秒-》 时:分:秒
	 * 
	 * @param duration
	 *            时间，单位为秒
	 * @return
	 */
	private String convertTime(Integer duration) {
		if (duration == null)
			return "00:00:00";
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (duration < 60) {
			return "00:00:" + duration;
		} else {
			minute = duration / 60;
			if (minute < 60) {
				second = duration % 60;
				return "00:" + formatTime(minute) + ":" + formatTime(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = duration - hour * 3600 - minute * 60;
				return formatTime(hour) + ":" + formatTime(minute) + ":" + formatTime(second);
			}
		}
	}

	/**
	 * 换算存储量大小：kb-》Mb
	 * 
	 * @param fileSize
	 *            存储量大小 kb
	 * @return
	 */
	private String convertMemorySizeFromKb2Mb(Long fileSize) {
		if (fileSize == null)
			return "0.0";
		double size = fileSize / 1024 + (fileSize % 1024) / 1024.0;
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setRoundingMode(RoundingMode.UP);
		return nf.format(size);
	}

	@Transactional
	@Override
	public void updateMediaFileById(ILiveMediaFile sqlParam) {
		iLiveMediaFileDao.updateMediaFileById(sqlParam);

	}

	@Override
	public Pagination getPager(Integer type, Integer pageNo, Integer pageSize) {
		return iLiveMediaFileDao.getPager(type, pageNo, pageSize);
	}
	@Override
	public Pagination getPager(Integer createType,Integer mediaState,Timestamp startTime,Timestamp endTime,String query,Integer type, Integer pageNo, Integer pageSize) {
		return iLiveMediaFileDao.getPager(createType,mediaState,startTime,endTime,query,type,pageNo,pageSize);
	}

	@Override
	public List<ILiveMediaFile> getListByType(Integer fileType,Integer enterpriseId) {
		return iLiveMediaFileDao.getListByType(fileType,enterpriseId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppMediaFile> getMediaFilePageByRoom(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		List<AppMediaFile> appList = new ArrayList<>();
		try {
			Pagination pager = iLiveMediaFileDao.getMediaFilePageByRoomForView(sqlParam, pageNo, pageSize);
			List<ILiveMediaFile> list = (List<ILiveMediaFile>) pager.getList();
			if (list != null && !list.isEmpty()) {
				AppMediaFile appMediaFile = null;
				for (ILiveMediaFile mediaFile : list) {
					appMediaFile = new AppMediaFile();
					appMediaFile.setFileName(mediaFile.getMediaFileName()==null?"":mediaFile.getMediaFileName());
					appMediaFile.setFileDuration(
							convertTime(mediaFile.getDuration() == null ? 0 : mediaFile.getDuration()));
					appMediaFile.setFileSize(
							convertMemorySizeFromKb2Mb(mediaFile.getFileSize() == null ? 0 : mediaFile.getFileSize())
									+ "M");
					appMediaFile.setFileId(mediaFile.getFileId());
					appMediaFile.setCreateTime(
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mediaFile.getMediaCreateTime()));
					appMediaFile.setFileImg(mediaFile.getFileCover() == null ? "" : mediaFile.getFileCover());
					appMediaFile.setCommentsNum(
							mediaFile.getCommentsCount() == null ? "0" : String.valueOf(mediaFile.getCommentsCount()));
					appMediaFile
							.setPlayNum(mediaFile.getViewNum() == null ? "0" : String.valueOf(mediaFile.getViewNum()));
					appList.add(appMediaFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appList;
	}

	@Override
	public int getNum(Integer type,Integer state) {
		// TODO Auto-generated method stub
		return iLiveMediaFileDao.getNum(type,state);
	}
}
