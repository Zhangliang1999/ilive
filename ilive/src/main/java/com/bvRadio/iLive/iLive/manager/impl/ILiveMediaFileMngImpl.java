package com.bvRadio.iLive.iLive.manager.impl;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bvRadio.iLive.iLive.dao.ILiveMeetingFileDao;
import com.bvRadio.iLive.iLive.entity.*;
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.dao.ILiveFileViewStaticsDao;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileCommentsDao;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileDao;
import com.bvRadio.iLive.iLive.manager.ILiveContentSelectMng;
import com.bvRadio.iLive.iLive.manager.ILiveContentSelectMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileViewStaticsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubRoomMng;
import com.bvRadio.iLive.iLive.util.SafeTyChainPasswdUtil;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.jwzt.comm.StringUtils;

/**
 * 媒资管理类实现
 * 
 * @author administrator
 */

@Service
@Transactional
public class ILiveMediaFileMngImpl implements ILiveMediaFileMng {

	@Autowired
	private ILiveMediaFileDao iLiveMediaFileDao;

	@Autowired
	private ILiveMeetingFileDao iLiveMeetingFileDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldMng;

	@Autowired
	private ILiveEnterpriseMng iLiveEnterpriseMng;

	@Autowired
	private ILiveServerAccessMethodMng iLiveServerAccessMethodMng;

	@Autowired
	private ILiveFileViewStaticsMng iLiveFileViewStaticsMng;

	@Autowired
	private ILiveMediaFileCommentsDao iLiveMediaFileCommentsDao;

	@Autowired
	private ILiveFileViewStaticsDao iLiveFileViewStaticsDao;

	@Autowired
	private ILiveContentSelectMng iLiveContentSelectMng;

	@Autowired
	private ILiveFileAuthenticationMng iLiveFileAuthenticationMng;
	 @Autowired 
	 private ILiveSubRoomMng iLiveSubRoomMng;
	
	@Transactional
	@Override
	public Long saveIliveMediaFile(ILiveMediaFile iLiveMediaFile) {
		long fileId = iLiveFieldMng.getNextId("ilive_media_file", "file_id", 1);
		iLiveMediaFile.setFileId(fileId);
		Integer isMediaType = iLiveMediaFile.getIsMediaType()==null?ILiveMediaFile.MEDIA_TYPE_Store:iLiveMediaFile.getIsMediaType();
		iLiveMediaFile.setIsMediaType(isMediaType);
		iLiveMediaFileDao.saveILiveMediaFile(iLiveMediaFile);
		return fileId;
	}

	@Transactional
	@Override
	public ILiveMediaFile deleteVedio(Long id) {
		ILiveMediaFile bean = iLiveMediaFileDao.deleteLiveMediaFile(id);
		return bean;
	}

	@Transactional
	@Override
	public ILiveMediaFile[] deleteVediosByIds(Long[] ids) {
		ILiveMediaFile[] mediaFiles = new ILiveMediaFile[ids.length];
		for (int i = 0; i < ids.length; i++) {
			mediaFiles[i] = iLiveMediaFileDao.deleteLiveMediaFile(ids[i]);
		}
		iLiveContentSelectMng.deleteContentPublish(ids);
		return mediaFiles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagination selectILiveMediaFilePage(Long liveEventId, Integer pageNO, Integer pageSize, Integer delFlag,
			Integer fileType) {
		Pagination pagination = new Pagination(pageNO, pageSize, 0);
		try {
			pagination = iLiveMediaFileDao.selectILiveMediaFilePage(liveEventId, pageNO, pageSize, delFlag, fileType);
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

	@Override
	public IliveMeetingFile selectIliveMeetingFileByFileId(Long fileId){
		IliveMeetingFile file = new IliveMeetingFile();
		try{
			file = iLiveMeetingFileDao.selectIliveMeetingFileByFileId(fileId);
		}catch (Exception e){
			e.printStackTrace();
		}
		return file;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveMediaFile selectILiveMediaForWeb(Long fileId) {
		ILiveMediaFile iLiveMediaFile = null;
		try {
			iLiveMediaFile = iLiveMediaFileDao.selectILiveMediaFileByFileId(fileId);
			if (iLiveMediaFile != null) {
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
		if (state != null && state.intValue() == 0) {
			iLiveContentSelectMng.deleteContentPublish(ids);
		}
	}

	@SuppressWarnings("unchecked")
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
				Long mount = iLiveMediaFileCommentsDao.getCommentsMount(iLiveMediaFile.getFileId());
				ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
						.getFileAuthenticationByFileId(iLiveMediaFile.getFileId());
				if (fileAuth == null) {
					iLiveMediaFile.setFileAuthentication(1);
				} else {
					iLiveMediaFile.setFileAuthentication(fileAuth.getAuthType() == null ? 1 : fileAuth.getAuthType());
				}
				iLiveMediaFile.setCommentsCount(mount);
				ILiveFileViewStatics viewNumStatics = iLiveFileViewStaticsDao.getViewNum(iLiveMediaFile.getFileId());
				if (viewNumStatics == null) {
					iLiveMediaFile.setViewNum(0L);
				} else {
					iLiveMediaFile.setViewNum(viewNumStatics.getNumMount());
				}
				//如果是暂存视频，计算暂存时间还有多久
				Integer type=iLiveMediaFile.getIsMediaType();
				if(type==null){
					type=0;
				}
				if(type==1){
					//获取视频创建时间
					Timestamp creatTime=iLiveMediaFile.getMediaCreateTime();
					if(creatTime==null){
						iLiveMediaFile.setTemporaryTime(new Timestamp(System.currentTimeMillis()));
					}else{
					Long validTime=(long) (1000*60*60*24*7);
					Timestamp temporaryTime=new Timestamp(creatTime.getTime()+validTime);
					iLiveMediaFile.setTemporaryTime(temporaryTime);
					}
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
			return "00:" + duration;
		} else {
			minute = duration / 60;
			if (minute < 60) {
				second = duration % 60;
				return  formatTime(minute) + ":" + formatTime(second);
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
	private String convertMemorySizeFromKb2Mb(Long size) {
		if (size == null)
			return "0.0";
		if (size < 1024) {
			return String.valueOf(size) + "KB";
		} else {
			size = size / 1024;
		}
		if (size < 1024) {
			//因为如果以MB为单位的话，要保留最后1位小数，
			//因此，把此数乘以100之后再取余
			size = size * 100;
			return String.valueOf((size / 100)) + "."
					+ String.valueOf((size % 100)) + "MB";
		} else {
			//否则如果要以GB为单位的，先除于1024再作同样的处理
			size = size * 100 / 1024;
			return String.valueOf((size / 100)) + "."
					+ String.valueOf((size % 100)) + "GB";
		}
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
	public List<ILiveMediaFile> getListByType(Integer fileType, Integer enterpriseId) {
		return iLiveMediaFileDao.getListByType(fileType, enterpriseId);
	}

	@Override
	public List<ILiveMediaFile> getListByTypeAndSize(Integer fileType, Integer enterpriseId, Integer num) {
		return iLiveMediaFileDao.getListByTypeAndSize(fileType, enterpriseId, num);
	}

	@SuppressWarnings("unchecked")
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
					appMediaFile.setFileName(mediaFile.getMediaFileName() == null ? "" : mediaFile.getMediaFileName());
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

					appMediaFile.setViewNum(mediaFile.getViewNum() == null ? 0L : 0L);
					appList.add(appMediaFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appList;
	}

	@Override
	public List<AppMediaFile> getTop4ForView(String keyword) {
		List<ILiveMediaFile> mediaFileList = iLiveMediaFileDao.getTop4ForView(keyword);
		List<AppMediaFile> appFileList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (mediaFileList != null && !mediaFileList.isEmpty()) {
			AppMediaFile appFile = null;
			for (ILiveMediaFile mediaFile : mediaFileList) {
				appFile = this.convertPo2Vo(mediaFile, sdf);
				Long enterpriseId = mediaFile.getEnterpriseId();
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId.intValue());
				ILiveAppEnterprise appEnterprise = this.convertPo2Vo(iLiveEnterPrise);
				appFile.setAppEnterprise(appEnterprise);
				appFileList.add(appFile);
			}
		}
		return appFileList;
	}

	private AppMediaFile convertPo2Vo(ILiveMediaFile mediaFile, SimpleDateFormat sdf) {
		AppMediaFile appFile = new AppMediaFile();
		appFile.setFileName(mediaFile.getMediaFileName());
		appFile.setCreateTime(sdf.format(mediaFile.getMediaCreateTime()));
		appFile.setFileId(mediaFile.getFileId());
		appFile.setFileImg(mediaFile.getFileCover() == null ? "" : mediaFile.getFileCover());
		appFile.setFileSize(
				convertMemorySizeFromKb2Mb(mediaFile.getFileSize() == null ? 0 : mediaFile.getFileSize()) + "M");
		appFile.setFileDuration(convertTime(mediaFile.getDuration() == null ? 0 : mediaFile.getDuration()));
		appFile.setCommentsNum(
				mediaFile.getCommentsCount() == null ? "0" : String.valueOf(mediaFile.getCommentsCount()));
		// appFile.setPlayNum(mediaFile.getViewNum() == null ? "0" :
		// String.valueOf(mediaFile.getViewNum()));
		appFile.setViewNum(0L);
		appFile.setRoomId(mediaFile.getLiveRoomId());
		appFile.setEnterpriseId(mediaFile.getEnterpriseId().intValue());
		appFile.setUserId(mediaFile.getUserId());
		Integer liveRoomId = mediaFile.getLiveRoomId();
		if (liveRoomId == null || liveRoomId == 0) {
			liveRoomId = 0;
		}
		appFile.setRoomId(liveRoomId);
		appFile.setLiveEventId(mediaFile.getLiveEventId());
		return appFile;
	}

	/**
	 * 构建对象
	 */
	@Override
	public List<AppMediaFile> getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType) {
		List<ILiveMediaFile> mediaFileList = iLiveMediaFileDao.getPagerForView(keyWord, pageNo, pageSize);
		List<AppMediaFile> appFileList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (mediaFileList != null && !mediaFileList.isEmpty()) {
			AppMediaFile appFile = null;
			for (ILiveMediaFile mediaFile : mediaFileList) {
				Long enterpriseId = mediaFile.getEnterpriseId();
				ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId.intValue());
				ILiveAppEnterprise appEnterprise = this.convertPo2Vo(iLiveEnterPrise);
				appFile = this.convertPo2Vo(mediaFile, sdf);
				appFile.setAppEnterprise(appEnterprise);
				appFileList.add(appFile);
			}
		}
		return appFileList;
	}

	private ILiveAppEnterprise convertPo2Vo(ILiveEnterprise enterprise) {
		ILiveAppEnterprise appEnterprise = new ILiveAppEnterprise();
		appEnterprise.setCertStatus(enterprise.getCertStatus());
		appEnterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
		appEnterprise.setConcernTotal(0L);
		appEnterprise.setEnterpriseId(enterprise.getEnterpriseId());
		appEnterprise.setEnterpriseName(StringPatternUtil.convertEmpty(enterprise.getEnterpriseName()));
		String enterpriseImg = enterprise.getEnterpriseImg();
		if (StringUtils.isEmpty(enterpriseImg)) {
			enterpriseImg = ConfigUtils.get("shareOtherConfig");
		}
		appEnterprise.setEnterpriseImg(enterpriseImg);
		String defaultEnterpriseServerId = ConfigUtils.get("defaultEnterpriseServerId");
		ILiveServerAccessMethod serverGroup = iLiveServerAccessMethodMng
				.getAccessMethodBySeverGroupId(Integer.parseInt(defaultEnterpriseServerId));
		String homePageUrl = serverGroup.getH5HttpUrl() + "/home/index.html?enterpriseId="
				+ enterprise.getEnterpriseId();
		appEnterprise.setHomePageUrl(homePageUrl);
		return appEnterprise;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AppMediaFile> getPersonalFileList(Long userId, Integer pageNo, Integer pageSize, HttpServletRequest request) {
		Pagination pager = iLiveMediaFileDao.getPersonalFileList(userId, pageNo, pageSize);
		List<AppMediaFile> appFileList = new ArrayList<>();
		if (pager != null && pager.getList() != null) {
			List<ILiveMediaFile> mediaFiles = (List<ILiveMediaFile>) pager.getList();
			if (!mediaFiles.isEmpty()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				for (ILiveMediaFile mediaFile : mediaFiles) {
					AppMediaFile appFile = this.convertPo2Vo(mediaFile, sdf);
					appFile.setOnlineState(mediaFile.getOnlineFlag() == null ? 0 : mediaFile.getOnlineFlag());
					Integer serverMountId = mediaFile.getServerMountId();
					ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
							.getAccessMethodByMountId(serverMountId);
					String downLoadPath = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
							+ accessMethod.getMountInfo().getBase_path() + mediaFile.getFilePath();
					String playPath = "http://" + accessMethod.getHttp_address() + ":"
							+ accessMethod.getUmsport() + accessMethod.getMountInfo().getBase_path() + mediaFile.getFilePath()+"/tzwj_video.m3u8";
					
					try{
						//判断一下是不是windows，windows不支持m3u8文件播放
						String agent = request.getHeader("User-Agent");
						if(agent.toLowerCase().indexOf("windows")>0){
							playPath = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
							+ accessMethod.getMountInfo().getBase_path() + mediaFile.getFilePath();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
					playPath = String.format("%s?ut=%s&us=%s&sign=%s", playPath, generatorEncoderPwd.get("timestamp"),
							generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
					// 因为客户端的地址用的是80端口，rtmp地址临时写死1935
				
					downLoadPath = String.format("%s?ut=%s&us=%s&sign=%s", downLoadPath, generatorEncoderPwd.get("timestamp"),
							generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
					appFile.setPlayAddr(playPath);
					appFile.setDownloadUrl(downLoadPath);
					ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng
							.getILiveEnterPrise(mediaFile.getEnterpriseId().intValue());
					ILiveAppEnterprise convertPo2Vo = this.convertPo2Vo(iLiveEnterPrise);
					appFile.setAppEnterprise(convertPo2Vo);
					appFile.setPraiseNum(0);
					appFile.setAllowComment(mediaFile.getAllowComment() == null ? 1 : mediaFile.getAllowComment());
					appFile.setFileDesc(StringPatternUtil.convertEmpty(mediaFile.getMediaFileDesc()));
					ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
							.getFileAuthenticationByFileId(mediaFile.getFileId());
					if (fileAuth == null) {
						fileAuth = new ILiveFileAuthentication();
						appFile.setAuthType(1);
						appFile.setAuthPassword("");
						appFile.setNeedLogin(0);
					} else {
						if (!StringUtils.isEmpty(fileAuth.getViewPassword())) {
							appFile.setAuthPassword(fileAuth.getViewPassword());
						}
						appFile.setNeedLogin(fileAuth.getNeedLogin()==null?0:fileAuth.getNeedLogin());
						appFile.setAuthType(fileAuth.getAuthType());
					}
					appFile.setStorageState(1);
					ILiveServerAccessMethod accessMethodBySever = iLiveServerAccessMethodMng
							.getAccessMethodByMountId(serverMountId);
					String shareUrl = accessMethodBySever.getH5HttpUrl() + "/phone" + "/review.html?roomId="
							+ (mediaFile.getLiveRoomId() == null ? 0 : mediaFile.getLiveRoomId()) + "&fileId="
							+ mediaFile.getFileId();
					appFile.setShareUrl(shareUrl);
					ILiveFileViewStatics viewNumStatics = iLiveFileViewStaticsMng.getViewNum(mediaFile.getFileId());
					if (viewNumStatics == null) {
						appFile.setViewNum(0L);
					} else {
						if(mediaFile.getOpenDataBeautifySwitch()!=null&&mediaFile.getOpenDataBeautifySwitch()==1) {
							appFile.setViewNum(viewNumStatics.getNumMount()*mediaFile.getMultiple()+mediaFile.getBaseNum());
						}else {
							appFile.setViewNum(viewNumStatics.getNumMount());
						}
					}
					appFileList.add(appFile);
				}
			}
		}
		return appFileList;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public AppMediaFile convertILiveMediaFile2AppMediaFile(ILiveMediaFile mediaFile){
		AppMediaFile ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		AppMediaFile appFile = this.convertPo2Vo(mediaFile, sdf);
		appFile.setOnlineState(mediaFile.getOnlineFlag() == null ? 0 : mediaFile.getOnlineFlag());
		Integer serverMountId = mediaFile.getServerMountId();
		ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
				.getAccessMethodByMountId(serverMountId);
		String allPath = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
				+ accessMethod.getMountInfo().getBase_path() + mediaFile.getFilePath()+"/tzwj_video.m3u8";
		Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
		allPath = String.format("%s?ut=%s&us=%s&sign=%s", allPath, generatorEncoderPwd.get("timestamp"),
				generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
		appFile.setPlayAddr(allPath);
		ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng
				.getILiveEnterPrise(mediaFile.getEnterpriseId().intValue());
		ILiveAppEnterprise convertPo2Vo = this.convertPo2Vo(iLiveEnterPrise);
		appFile.setAppEnterprise(convertPo2Vo);
		appFile.setPraiseNum(0);
		appFile.setAllowComment(mediaFile.getAllowComment() == null ? 1 : mediaFile.getAllowComment());
		appFile.setFileDesc(StringPatternUtil.convertEmpty(mediaFile.getMediaFileDesc()));
		ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
				.getFileAuthenticationByFileId(mediaFile.getFileId());
		if (fileAuth == null) {
			fileAuth = new ILiveFileAuthentication();
			appFile.setAuthType(1);
			appFile.setAuthPassword("");
		} else {
			if (!StringUtils.isEmpty(fileAuth.getViewPassword())) {
				appFile.setAuthPassword(fileAuth.getViewPassword());
			}
			appFile.setAuthType(fileAuth.getAuthType());
		}
		appFile.setStorageState(1);
		ILiveServerAccessMethod accessMethodBySever = iLiveServerAccessMethodMng
				.getAccessMethodByMountId(serverMountId);
		String shareUrl = accessMethodBySever.getH5HttpUrl() + "/phone" + "/review.html?roomId="
				+ (mediaFile.getLiveRoomId() == null ? 0 : mediaFile.getLiveRoomId()) + "&fileId="
				+ mediaFile.getFileId();
		appFile.setShareUrl(shareUrl);
		ILiveFileViewStatics viewNumStatics = iLiveFileViewStaticsMng.getViewNum(mediaFile.getFileId());
		if (viewNumStatics == null) {
			appFile.setViewNum(0L);
		} else {
			appFile.setViewNum(mediaFile.getViewNum());
		}
		Boolean vrFile = mediaFile.getVrFile();
		if (null != vrFile && vrFile) {
			appFile.setVrFile(1);
		} else {
			appFile.setVrFile(0);
		}
		ret = appFile;
		
		return ret;
	}


	@Override
	@Transactional
	public void updateMediaFile(ILiveMediaFile iLiveMediaFile) {
		iLiveMediaFileDao.updateMediaFile(iLiveMediaFile);
	}

	@Override
	@Transactional
	public void updateMeetingMediaFile(IliveMeetingFile iliveMeetingFile) {
		iLiveMeetingFileDao.updateMeetingMediaFile(iliveMeetingFile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagination getAppMediaFile(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		Pagination pagination = iLiveMediaFileDao.getMedialFilePage(sqlParam, pageNo, pageSize);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<AppMediaFile> appFileList = new ArrayList<>();
		if (pagination != null && pagination.getList() != null) {
			List<ILiveMediaFile> mediaList = (List<ILiveMediaFile>) pagination.getList();
			for (ILiveMediaFile file : mediaList) {
				AppMediaFile convertPo2Vo = this.convertPo2Vo(file, sdf);
				convertPo2Vo.setWidthHeight(file.getWidthHeight());
				appFileList.add(convertPo2Vo);
			}
			pagination.setList(appFileList);
		}
		return pagination;
	}

	@Override
	public List<AppMediaFile> getBatchVodListForStatics(Long startId, Integer size) {
		List<ILiveMediaFile> mediaFileList = iLiveMediaFileDao.getBatchVodListForStatics(startId, size);
		List<AppMediaFile> appFileList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (mediaFileList != null && !mediaFileList.isEmpty()) {
			for (ILiveMediaFile file : mediaFileList) {
				AppMediaFile convertPo2Vo = this.convertPo2VoForStatic(file, sdf);
				appFileList.add(convertPo2Vo);
			}
		}
		return appFileList;
	}

	private AppMediaFile convertPo2VoForStatic(ILiveMediaFile mediaFile, SimpleDateFormat sdf) {
		AppMediaFile appFile = new AppMediaFile();
		appFile.setFileName(mediaFile.getMediaFileName());
		appFile.setCreateTime(sdf.format(mediaFile.getMediaCreateTime()));
		appFile.setFileId(mediaFile.getFileId());
		appFile.setFileImg(mediaFile.getFileCover() == null ? "" : mediaFile.getFileCover());
		appFile.setFileSize(String.valueOf(mediaFile.getFileSize() == null ? 0 : mediaFile.getFileSize()));
		appFile.setFileDuration(String.valueOf(mediaFile.getDuration() == null ? 0 : mediaFile.getDuration()));
		appFile.setCommentsNum(
				mediaFile.getCommentsCount() == null ? "0" : String.valueOf(mediaFile.getCommentsCount()));
		appFile.setViewNum(0L);
		appFile.setRoomId(mediaFile.getLiveRoomId());
		appFile.setEnterpriseId(mediaFile.getEnterpriseId().intValue());
		appFile.setUserId(mediaFile.getUserId());
		appFile.setWidthHeight(mediaFile.getWidthHeight() == null ? "" : mediaFile.getWidthHeight());
		Integer liveRoomId = mediaFile.getLiveRoomId();
		if (liveRoomId == null || liveRoomId == 0) {
			liveRoomId = 0;
		}
		appFile.setRoomId(liveRoomId);
		appFile.setLiveEventId(mediaFile.getLiveEventId());
		return appFile;
	}

	@Override
	public List<AppMediaFile> getAppMediaFileForStatics(Long[] ids) {
		List<AppMediaFile> appFileList = new ArrayList<>();
		try {
			List<ILiveMediaFile> files = iLiveMediaFileDao.getListByIds(ids);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (files != null && !files.isEmpty()) {
				for (ILiveMediaFile file : files) {
					AppMediaFile convertPo2Vo = this.convertPo2VoForStatic(file, sdf);
					appFileList.add(convertPo2Vo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appFileList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagination getCollaborativeMediaFilePage(
			Map<String, Object> sqlParam, Integer pageNo, Integer pageSize,
			Long userId, Integer roomId) {
		Pagination pagination = iLiveMediaFileDao.getCollaborativeMediaFilePage(sqlParam, pageNo, pageSize,userId,roomId);
		if (pagination != null && pagination.getList() != null && pagination.getList().size() > 0) {
			List<ILiveMediaFile> iLiveMediaFileList = (List<ILiveMediaFile>) pagination.getList();
			for (ILiveMediaFile iLiveMediaFile : iLiveMediaFileList) {
				// 换算时间
				Integer duration = iLiveMediaFile.getDuration();
				iLiveMediaFile.setDurationTime(convertTime(duration));
				// 换算大小单位，从kb-mb
				Long fileSize = iLiveMediaFile.getFileSize();
				iLiveMediaFile.setFileSizeMb(convertMemorySizeFromKb2Mb(fileSize));
				Long mount = iLiveMediaFileCommentsDao.getCommentsMount(iLiveMediaFile.getFileId());
				ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
						.getFileAuthenticationByFileId(iLiveMediaFile.getFileId());
				if (fileAuth == null) {
					iLiveMediaFile.setFileAuthentication(1);
				} else {
					iLiveMediaFile.setFileAuthentication(fileAuth.getAuthType() == null ? 1 : fileAuth.getAuthType());
				}
				iLiveMediaFile.setCommentsCount(mount);
				ILiveFileViewStatics viewNumStatics = iLiveFileViewStaticsDao.getViewNum(iLiveMediaFile.getFileId());
				if (viewNumStatics == null) {
					iLiveMediaFile.setViewNum(0L);
				} else {
					iLiveMediaFile.setViewNum(viewNumStatics.getNumMount());
				}
			}
		}
		return pagination;
	}

	@Override
	public List<ILiveMediaFile> selectILiveMediaFileListByEnterprieId(Integer enterpriseId,
			Integer fileType) {
		List<ILiveMediaFile> list = iLiveMediaFileDao.selectILiveMediaFileListByEnterprieId(enterpriseId,fileType);
		return list;
	}

	@Override
	public List<ILiveMediaFile> selectILiveMediaFileListByIsMediaType(
			Integer mediaTypeTemporary, Integer delFlag) {
		List<ILiveMediaFile> list = iLiveMediaFileDao.selectILiveMediaFileListByIsMediaType(mediaTypeTemporary,delFlag);
		return list;
	}

	@Override
	public List<ILiveMediaFile> getListByTypeAndName(String name, Integer fileType, Integer enterpriseId) {
		return iLiveMediaFileDao.getListByTypeAndName(name,fileType, enterpriseId);
	}

	@Override
	public List<AppMediaFile> getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType,
			boolean per, Integer enterpriseId, Long userId, Integer level) {
		
		List<AppMediaFile> fileList = this.getPersonalFileList(userId, pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize,keyWord);
		
		return fileList;
	}

	@Override
	public Pagination getMediaFilePage1(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize) {
		Pagination pagination = iLiveMediaFileDao.getMedialFilePage1(sqlParam, pageNo, pageSize);
		if (pagination != null && pagination.getList() != null && pagination.getList().size() > 0) {
			List<ILiveMediaFile> iLiveMediaFileList = (List<ILiveMediaFile>) pagination.getList();
			for (ILiveMediaFile iLiveMediaFile : iLiveMediaFileList) {
				// 换算大小单位，从kb-mb
				Long fileSize = iLiveMediaFile.getFileSize();
				iLiveMediaFile.setFileSizeMb(convertMemorySizeFromKb2Mb(fileSize));
			}
		}
		return pagination;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveMediaFile> selectILiveMediaFileBycloudDiskFileIds(String[] cloudDiskFileId) {
		return iLiveMediaFileDao.selectILiveMediaFileBycloudDiskFileIds(cloudDiskFileId);
	}

	@Transactional(readOnly = true)
	private List<AppMediaFile> getPersonalFileList(Long userId, int pageNo, int pageSize, String keyWord) {
		Pagination pager = iLiveMediaFileDao.getPersonalFileList(userId, pageNo, pageSize,keyWord);
		List<AppMediaFile> appFileList = new ArrayList<>();
		if (pager != null && pager.getList() != null) {
			List<ILiveMediaFile> mediaFiles = (List<ILiveMediaFile>) pager.getList();
			if (!mediaFiles.isEmpty()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				for (ILiveMediaFile mediaFile : mediaFiles) {
					AppMediaFile appFile = this.convertPo2Vo(mediaFile, sdf);
					appFile.setOnlineState(mediaFile.getOnlineFlag() == null ? 0 : mediaFile.getOnlineFlag());
					Integer serverMountId = mediaFile.getServerMountId();
					ILiveServerAccessMethod accessMethod = iLiveServerAccessMethodMng
							.getAccessMethodByMountId(serverMountId);
					String allPath = "http://" + accessMethod.getHttp_address() + ":" + accessMethod.getUmsport()
							+ accessMethod.getMountInfo().getBase_path() + mediaFile.getFilePath();
					Map<String, String> generatorEncoderPwd = SafeTyChainPasswdUtil.INSTANCE.generatorEncoderPwd();
					allPath = String.format("%s?ut=%s&us=%s&sign=%s", allPath, generatorEncoderPwd.get("timestamp"),
							generatorEncoderPwd.get("sequence"), generatorEncoderPwd.get("encodePwd"));
					appFile.setPlayAddr(allPath);
					ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng
							.getILiveEnterPrise(mediaFile.getEnterpriseId().intValue());
					ILiveAppEnterprise convertPo2Vo = this.convertPo2Vo(iLiveEnterPrise);
					appFile.setAppEnterprise(convertPo2Vo);
					appFile.setPraiseNum(0);
					appFile.setAllowComment(mediaFile.getAllowComment() == null ? 1 : mediaFile.getAllowComment());
					appFile.setFileDesc(StringPatternUtil.convertEmpty(mediaFile.getMediaFileDesc()));
					ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
							.getFileAuthenticationByFileId(mediaFile.getFileId());
					if (fileAuth == null) {
						fileAuth = new ILiveFileAuthentication();
						appFile.setAuthType(1);
						appFile.setAuthPassword("");
					} else {
						if (!StringUtils.isEmpty(fileAuth.getViewPassword())) {
							appFile.setAuthPassword(fileAuth.getViewPassword());
						}
						appFile.setAuthType(fileAuth.getAuthType());
					}
					appFile.setStorageState(1);
					ILiveServerAccessMethod accessMethodBySever = iLiveServerAccessMethodMng
							.getAccessMethodByMountId(serverMountId);
					String shareUrl = accessMethodBySever.getH5HttpUrl() + "/phone" + "/review.html?roomId="
							+ (mediaFile.getLiveRoomId() == null ? 0 : mediaFile.getLiveRoomId()) + "&fileId="
							+ mediaFile.getFileId();
					appFile.setShareUrl(shareUrl);
					ILiveFileViewStatics viewNumStatics = iLiveFileViewStaticsMng.getViewNum(mediaFile.getFileId());
					if (viewNumStatics == null) {
						appFile.setViewNum(0L);
					} else {
						if(mediaFile.getOpenDataBeautifySwitch()!=null&&mediaFile.getOpenDataBeautifySwitch()==1) {
							appFile.setViewNum(viewNumStatics.getNumMount()*mediaFile.getMultiple()+mediaFile.getBaseNum());
						}else {
							appFile.setViewNum(viewNumStatics.getNumMount());
						}
					}
					appFileList.add(appFile);
				}
			}
		}
		return appFileList;
	}
}
