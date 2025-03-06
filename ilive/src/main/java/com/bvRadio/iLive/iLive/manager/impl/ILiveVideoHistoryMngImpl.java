package com.bvRadio.iLive.iLive.manager.impl;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.dao.ILiveFileViewStaticsDao;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileCommentsDao;
import com.bvRadio.iLive.iLive.dao.ILiveVideoHistoryDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;
import com.bvRadio.iLive.iLive.entity.ILiveFileViewStatics;
import com.bvRadio.iLive.iLive.entity.ILiveHistoryVideo;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.vo.WebILiveFleVo;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveVideoHistoryMng;
import com.bvRadio.iLive.iLive.util.SafeTyChainPasswdUtil;
import com.jwzt.comm.StringUtils;

public class ILiveVideoHistoryMngImpl implements ILiveVideoHistoryMng {

	@Autowired
	private ILiveVideoHistoryDao iLiveVideoHistoryDao;

	@Autowired
	private ILiveMediaFileMng mediaFileMng;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	@Autowired
	private ILiveFileViewStaticsDao iLiveFileViewStaticsDao;

	@Autowired
	private ILiveFileAuthenticationMng iLiveFileAuthenticationMng;

	@Autowired
	private ILiveMediaFileCommentsDao iLiveFileCommentsMng;

	@Autowired
	private ILiveServerAccessMethodMng serverAccessMng;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination getHistoryList(Integer roomId, Integer pageNo, Integer pageSize) {
		Pagination pagination = iLiveVideoHistoryDao.getHistoryList(roomId, pageNo, pageSize);
		if (pagination != null) {
			List<ILiveHistoryVideo> list = (List<ILiveHistoryVideo>) pagination.getList();
			List<WebILiveFleVo> webList = new ArrayList<>();
			if (list != null && !list.isEmpty()) {
				for (ILiveHistoryVideo video : list) {
					Long fileId = video.getFileId();
					ILiveMediaFile selectILiveMediaFileByFileId = mediaFileMng.selectILiveMediaFileByFileId(fileId);
					WebILiveFleVo wi = new WebILiveFleVo();
					wi.setiLiveHistoryVideo(video);
					ILiveFileViewStatics viewNum = iLiveFileViewStaticsDao.getViewNum(fileId);
					Long numMount = 0L;
					if (viewNum != null) {
						numMount = viewNum.getNumMount();
					}
					ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
							.getFileAuthenticationByFileId(video.getFileId());
					if (fileAuth == null) {
						selectILiveMediaFileByFileId.setFileAuthentication(1);
					} else {
						selectILiveMediaFileByFileId
								.setFileAuthentication(fileAuth.getAuthType() == null ? 1 : fileAuth.getAuthType());
					}
					numMount = numMount == null ? 0 : numMount;
					selectILiveMediaFileByFileId.setViewNum(numMount);
					selectILiveMediaFileByFileId
							.setDurationTime(this.convertTime(selectILiveMediaFileByFileId.getDuration() == null ? 0
									: selectILiveMediaFileByFileId.getDuration()));
					wi.setiLiveMediaFile(selectILiveMediaFileByFileId);
					webList.add(wi);
				}
			}
			pagination.setList(webList);
		}
		return pagination;
	}

	/**
	 * 向上移动
	 */
	@Override
	@Transactional
	public void moveUp(Long historyId, Integer roomId) {
		ILiveHistoryVideo video = iLiveVideoHistoryDao.getHistory(historyId);
		Double orderSequence = iLiveVideoHistoryDao.getFrontHistory(video.getFileOrder(), roomId);
		video.setFileOrder(orderSequence);
		iLiveVideoHistoryDao.updateILiveHistoryVideo(video);
	}

	/**
	 * 向下移动
	 */
	@Override
	@Transactional
	public void moveDown(Long historyId, Integer roomId) {
		ILiveHistoryVideo video = iLiveVideoHistoryDao.getHistory(historyId);
		Double orderSequence = iLiveVideoHistoryDao.getDownHistory(video.getFileOrder(), roomId);
		video.setFileOrder(orderSequence);
		iLiveVideoHistoryDao.updateILiveHistoryVideo(video);
	}

	@Override
	@Transactional
	public void deleteHistory(Long historyId) {
		ILiveHistoryVideo video = iLiveVideoHistoryDao.getHistory(historyId);
		if (video != null) {
			video.setDelFlag(1);
			iLiveVideoHistoryDao.updateILiveHistoryVideo(video);
		}
	}

	private static AtomicLong count = new AtomicLong();
	
	@Override
	@Transactional
	public boolean batchAddHistory(Integer roomId, Long[] ids, Long userId) {
		if (ids != null) {
			for (Long fileId : ids) {
				ILiveHistoryVideo historyVideo = iLiveVideoHistoryDao.getHistoryByFileId(fileId, roomId);
				if (historyVideo == null) {
					Long historyId = iLiveFieldIdManagerMng.getNextId("ilive_history_video", "history_id", 1);
					historyVideo = new ILiveHistoryVideo();
					historyVideo.setHistoryId(historyId);
					historyVideo.setDelFlag(0);
					historyVideo.setRecordTime(new Timestamp(System.currentTimeMillis()));
					historyVideo.setUserId(userId);
					Double order = (double) (System.currentTimeMillis() + roomId + count.incrementAndGet()/ 1000L);
					historyVideo.setFileOrder(order);
					historyVideo.setRoomId(roomId);
					historyVideo.setFileId(fileId);
					iLiveVideoHistoryDao.saveVideoHistory(historyVideo);
				} else {
					historyVideo.setDelFlag(0);
					historyVideo.setRecordTime(new Timestamp(System.currentTimeMillis()));
					historyVideo.setUserId(userId);
					Double order = (double) (System.currentTimeMillis() + roomId + count.incrementAndGet() / 1000L);
					historyVideo.setFileOrder(order);
					iLiveVideoHistoryDao.updateILiveHistoryVideo(historyVideo);
				}
			}
		}
		return true;
	}

	@Override
	@Transactional
	public void saveVideoHistory(Integer roomId, Long saveIliveMediaFile, long userId) {
		Long historyId = iLiveFieldIdManagerMng.getNextId("ilive_history_video", "history_id", 1);
		ILiveHistoryVideo historyVideo = new ILiveHistoryVideo();
		historyVideo.setHistoryId(historyId);
		historyVideo.setDelFlag(0);
		historyVideo.setRecordTime(new Timestamp(System.currentTimeMillis()));
		historyVideo.setUserId(userId);
		Double order = (double) (System.currentTimeMillis() / 1000L);
		historyVideo.setFileOrder(order);
		historyVideo.setRoomId(roomId);
		historyVideo.setFileId(saveIliveMediaFile);
		iLiveVideoHistoryDao.saveVideoHistory(historyVideo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AppMediaFile> getMediaFilePageByRoom(Map<String, Object> sqlParam, int pageNo, int pageSize,
			Integer roomId) {
		List<AppMediaFile> appList = new ArrayList<>();
		try {
			Pagination pagination = iLiveVideoHistoryDao.getMediaFilePageByRoom(sqlParam, pageNo, pageSize);
			if (pagination != null && pagination.getList() != null) {
				AppMediaFile appMediaFile = null;
				List<ILiveMediaFile> list = (List<ILiveMediaFile>) pagination.getList();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (ILiveMediaFile mediaFile : list) {
					appMediaFile = new AppMediaFile();
					appMediaFile.setOnlineState(mediaFile.getOnlineFlag() == null ? 0 : mediaFile.getOnlineFlag());
					appMediaFile.setFileName(mediaFile.getMediaFileName() == null ? "" : mediaFile.getMediaFileName());
					appMediaFile.setFileDuration(
							convertTime(mediaFile.getDuration() == null ? 0 : mediaFile.getDuration()));
					appMediaFile.setFileDesc(mediaFile.getMediaFileDesc()==null?"":mediaFile.getMediaFileDesc());
					appMediaFile.setFileSize(
							convertMemorySizeFromKb2Mb(mediaFile.getFileSize() == null ? 0 : mediaFile.getFileSize())
									+ "M");
					ILiveServerAccessMethod accessMethodBySever = serverAccessMng
							.getAccessMethodByMountId(mediaFile.getServerMountId());
					String allPath = "http://" + accessMethodBySever.getHttp_address() + ":"
							+ accessMethodBySever.getUmsport() + accessMethodBySever.getMountInfo().getBase_path()
							+ mediaFile.getFilePath();
					Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
					allPath = String.format("%s?ut=%s&us=%s&sign=%s", allPath, generatorEncoderPwd.get("timestamp"),
							generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
					appMediaFile.setPlayAddr(allPath);
					appMediaFile.setFileId(mediaFile.getFileId());
					appMediaFile.setCreateTime(simpleDateFormat.format(mediaFile.getMediaCreateTime()));
					appMediaFile.setFileImg(mediaFile.getFileCover() == null ? "" : mediaFile.getFileCover());
					appMediaFile.setCommentsNum(
							mediaFile.getCommentsCount() == null ? "0" : String.valueOf(mediaFile.getCommentsCount()));
					appMediaFile.setRoomId(roomId);
					ILiveFileViewStatics viewNumStatics = iLiveFileViewStaticsDao.getViewNum(mediaFile.getFileId());
					if (viewNumStatics == null) {
						appMediaFile.setViewNum(0L);
					} else {
						if(mediaFile.getOpenDataBeautifySwitch()!=null&&mediaFile.getOpenDataBeautifySwitch()==1) {
							appMediaFile.setViewNum(viewNumStatics.getNumMount()*mediaFile.getMultiple()+mediaFile.getBaseNum());
						}else {
							appMediaFile.setViewNum(viewNumStatics.getNumMount());
						}
						
					}
					Long commentsMount = iLiveFileCommentsMng.getCommentsMount(mediaFile.getFileId());
					appMediaFile.setCommentsNum(String.valueOf(commentsMount == null ? 0 : commentsMount));
					ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
							.getFileAuthenticationByFileId(mediaFile.getFileId());
					if (fileAuth == null) {
						fileAuth = new ILiveFileAuthentication();
						appMediaFile.setAuthType(1);
						appMediaFile.setAuthPassword("");
					} else {
						if (!StringUtils.isEmpty(fileAuth.getViewPassword())) {
							appMediaFile.setAuthPassword(fileAuth.getViewPassword());
						}
						appMediaFile.setAuthType(fileAuth.getAuthType());
					}
					// 文件暂存状态先不考虑 TODO 待设计
					appMediaFile.setStorageState(1);
					String shareUrl = accessMethodBySever.getH5HttpUrl() + "/phone" + "/review.html?roomId="
							+ (mediaFile.getLiveRoomId() == null ? 0 : mediaFile.getLiveRoomId()) + "&fileId="
							+ mediaFile.getFileId();
					appMediaFile.setShareUrl(shareUrl);
					appMediaFile.setAllowComment(mediaFile.getAllowComment() == null ? 1 : mediaFile.getAllowComment());
					appList.add(appMediaFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appList;
	}

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

	@Override
	public ILiveHistoryVideo getViewHistoryByFileId(Long fileId, Integer roomId) {
		return iLiveVideoHistoryDao.getHistoryByFileId(fileId, roomId);
	}


	@Override
	public void test() {
		System.out.print(count.getAndIncrement()+"   ");
		if (count.get()%10==0) {
			System.out.println();
		}
	}
	
	
}
