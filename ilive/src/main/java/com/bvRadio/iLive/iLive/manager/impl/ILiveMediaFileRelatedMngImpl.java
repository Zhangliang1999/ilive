package com.bvRadio.iLive.iLive.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileCommentsDao;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileRelatedDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthentication;
import com.bvRadio.iLive.iLive.entity.ILiveFileViewStatics;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileRelated;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.manager.ILiveFileAuthenticationMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileViewStaticsMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileRelatedMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.SafeTyChainPasswdUtil;
import com.bvRadio.iLive.iLive.util.TimeUtils;
import com.jwzt.comm.StringUtils;

/**
 * 媒资管理类实现
 * 
 * @author administrator
 */

@Service
public class ILiveMediaFileRelatedMngImpl implements ILiveMediaFileRelatedMng {

	@Autowired
	private ILiveMediaFileRelatedDao dao;
	@Autowired
	private ILiveMediaFileMng mediaFileMng;
	@Autowired
	private ILiveFileViewStaticsMng fileViewStaticsMng;
	@Autowired
	private ILiveFileAuthenticationMng iLiveFileAuthenticationMng;
	@Autowired
	private ILiveServerAccessMethodMng serverAccessMng;
	@Autowired
	private ILiveFileViewStaticsMng iLiveFileViewStaticsMng;
	@Autowired
	private ILiveMediaFileCommentsDao iLiveFileCommentsMng;

	@Transactional
	@Override
	public List<ILiveMediaFileRelated> save(Long mainFileId, Long[] relatedFileIds) {
		if (null != mainFileId && null != relatedFileIds) {
			try {
				List<ILiveMediaFileRelated> list = new ArrayList<ILiveMediaFileRelated>();
				for (Long relatedFileId : relatedFileIds) {
					ILiveMediaFileRelated bean = new ILiveMediaFileRelated();
					bean.setMainFileId(mainFileId);
					bean.setRelatedFileId(relatedFileId);
					bean = save(bean);
					initBean(bean, true);
					list.add(bean);
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	@Transactional
	@Override
	public ILiveMediaFileRelated save(ILiveMediaFileRelated bean) {
		if (null != bean) {
			UUID randomUUID = UUID.randomUUID();
			bean.setId(randomUUID.toString());
			bean.initFieldValue();
		}
		return dao.save(bean);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Pagination pageByParams(Long mainFileId, int pageNo, int pageSize) {
		Pagination page = dao.pageByParams(mainFileId, pageNo, pageSize);
		if (null != page) {
			List<ILiveMediaFileRelated> list = (List<ILiveMediaFileRelated>) page.getList();
			if (null != list) {
				for (ILiveMediaFileRelated bean : list) {
					initBean(bean, true);
				}
			}

		}
		return page;
	}

	@Transactional(readOnly = true)
	@Override
	public List<ILiveMediaFileRelated> listByParams(Long mainFileId) {
		List<ILiveMediaFileRelated> list = dao.listByParams(mainFileId);
		if (null != list) {
			for (ILiveMediaFileRelated bean : list) {
				initBean(bean, true);
			}
		}
		return list;

	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<AppMediaFile> listForApp(Long mainFileId, int pageNo, int pageSize) {
		List<AppMediaFile> appList = new ArrayList<>();
		try {
			Pagination pagination = dao.pageByParams(mainFileId, pageNo, pageSize);
			if (pagination != null && pagination.getList() != null) {
				AppMediaFile appMediaFile = null;
				List<ILiveMediaFileRelated> list = (List<ILiveMediaFileRelated>) pagination.getList();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (ILiveMediaFileRelated mediaFileRelated : list) {
					try {
						initBean(mediaFileRelated, true);
						ILiveMediaFile mediaFile = mediaFileRelated.getRelatedMediaFile();
						appMediaFile = new AppMediaFile();
						appMediaFile.setOnlineState(mediaFile.getOnlineFlag() == null ? 0 : mediaFile.getOnlineFlag());
						appMediaFile.setFileName(mediaFile.getMediaFileName() == null ? "" : mediaFile.getMediaFileName());

						appMediaFile.setFileDuration(
								TimeUtils.secToTime1(mediaFile.getDuration() == null ? 0 : mediaFile.getDuration()));
						appMediaFile.setFileSize(
								FileUtils.formatFileSize(mediaFile.getFileSize() == null ? 0 : mediaFile.getFileSize()));
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
						appMediaFile.setRoomId(mediaFile.getLiveRoomId());
						ILiveFileViewStatics viewNumStatics = iLiveFileViewStaticsMng.getViewNum(mediaFile.getFileId());
						if (viewNumStatics == null) {
							appMediaFile.setViewNum(0L);
						} else {
							appMediaFile.setViewNum(viewNumStatics.getNumMount());
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
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appList;
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
	
	
	@Transactional
	@Override
	public ILiveMediaFileRelated update(ILiveMediaFileRelated bean) {
		Updater<ILiveMediaFileRelated> updater = new Updater<ILiveMediaFileRelated>(bean);
		dao.updateByUpdater(updater);
		return null;
	}

	@Transactional
	@Override
	public ILiveMediaFileRelated deleteById(String id) {
		ILiveMediaFileRelated bean = null;
		if (null != id) {
			bean = dao.deleteById(id);
		}
		return bean;
	}

	@Transactional
	@Override
	public ILiveMediaFileRelated[] deleteByIds(String[] ids) {
		if (null != ids) {
			ILiveMediaFileRelated[] beans = new ILiveMediaFileRelated[ids.length];
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				beans[i] = deleteById(id);
			}
			return beans;
		}
		return null;
	}

	private void initBean(ILiveMediaFileRelated bean, boolean initRelatedMediaFile) {
		if (null != bean) {
			Long relatedFileId = bean.getRelatedFileId();
			if (null != relatedFileId) {
				ILiveMediaFile relatedMediaFile = mediaFileMng.selectILiveMediaFileByFileId(relatedFileId);
				ILiveFileViewStatics viewNum = fileViewStaticsMng.getViewNum(relatedFileId);
				Long numMount = 0L;
				if (viewNum != null) {
					numMount = viewNum.getNumMount();
				}
				ILiveFileAuthentication fileAuth = iLiveFileAuthenticationMng
						.getFileAuthenticationByFileId(relatedFileId);
				if (fileAuth == null) {
					relatedMediaFile.setFileAuthentication(1);
				} else {
					relatedMediaFile.setFileAuthentication(fileAuth.getAuthType() == null ? 1 : fileAuth.getAuthType());
				}
				numMount = numMount == null ? 0 : numMount;
				relatedMediaFile.setViewNum(numMount);
				relatedMediaFile.setDurationTime(TimeUtils
						.secToTime(relatedMediaFile.getDuration() == null ? 0 : relatedMediaFile.getDuration()));
				bean.setRelatedMediaFile(relatedMediaFile);
			}

		}
	}

}
