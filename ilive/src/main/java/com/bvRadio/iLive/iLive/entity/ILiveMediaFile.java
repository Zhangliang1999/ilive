package com.bvRadio.iLive.iLive.entity;

import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveMediaFile;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

/**
 * 媒资文件
 * 
 * @author administrator
 */
public class ILiveMediaFile extends BaseILiveMediaFile {
	/**
	 * 文件存储状态  ： 0 暂存
	 */
	public static final Integer MEDIA_TYPE_Store=0;
	/**
	 * 文件存储状态：1 暂存
	 */
	public static final Integer MEDIA_TYPE_Temporary=1;

	/**
	 * 时：分：秒格式时间字符串
	 */
	private String durationTime;

	/**
	 * MB单位大小字符串
	 */
	private String fileSizeMb;
	/**
	 * 图片类型
	 */
	private String imageType;

	/**
	 * 评论数
	 */
	private Long commentsCount;

	/**
	 * 开启ffmpeg获取文件信息状态
	 */
	private Integer mediaInfoStatus;

	/**
	 * ffmpeg文件处理结果,为前台展示所用
	 */
	private Integer mediaInfoDealState;

	/**
	 * 允许评论标识 0为关闭 1为开启
	 */
	private Integer allowComment;

	/**
	 * 自动审核标识 0为关闭 1为开启
	 */
	private Integer autoCheckFlag;

	/**
	 * 分享地址
	 */
	private String shareUrl;

	/**
	 * 评论信息
	 */
	private Set<ILiveMediaFileComments> iLiveMediaFileCommentsSet;
	
	private Integer fileAuthentication;
	/**
	 * 
	 */
    private Integer openDataBeautifySwitch;
    
    /**
	 * 直播基础人数
	 */
	private Integer baseNum;

	/**
	 * 直播基础人数的倍数
	 */
	private Integer multiple;

	public Integer getBaseNum() {
		return baseNum;
	}

	public void setBaseNum(Integer baseNum) {
		this.baseNum = baseNum;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public Integer getOpenDataBeautifySwitch() {
		return openDataBeautifySwitch;
	}

	public void setOpenDataBeautifySwitch(Integer openDataBeautifySwitch) {
		this.openDataBeautifySwitch = openDataBeautifySwitch;
	}

	public String getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}

	public String getFileSizeMb() {
		return fileSizeMb;
	}

	public void setFileSizeMb(String fileSizeMb) {
		this.fileSizeMb = fileSizeMb;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public Set<ILiveMediaFileComments> getiLiveMediaFileCommentsSet() {
		return iLiveMediaFileCommentsSet;
	}

	public void setiLiveMediaFileCommentsSet(Set<ILiveMediaFileComments> iLiveMediaFileCommentsSet) {
		this.iLiveMediaFileCommentsSet = iLiveMediaFileCommentsSet;
	}

	public Long getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Long commentsCount) {
		this.commentsCount = commentsCount;
	}

	public JSONObject formatToJsonObject(ILiveMediaFile iLiveMediaFile) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mediaFileName", iLiveMediaFile.getMediaFileName());
		if (iLiveMediaFile.getMediaCreateTime() != null) {
			jsonObject.put("mediaCreateTime",
					new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(iLiveMediaFile.getMediaCreateTime()));
		}
		jsonObject.put("fileCover",
				StringUtils.isNotBlank(iLiveMediaFile.getFileCover()) ? iLiveMediaFile.getFileCover() : "");
		jsonObject.put("viewNum", iLiveMediaFile.getViewNum() != null ? iLiveMediaFile.getViewNum() : 0);
		jsonObject.put("commentsCount",
				iLiveMediaFile.getCommentsCount() != null ? iLiveMediaFile.getCommentsCount() : 0);
		jsonObject.put("durationTime",
				StringUtils.isNotBlank(iLiveMediaFile.getDurationTime()) ? iLiveMediaFile.getDurationTime() : "");
		return jsonObject;
	}

	public Integer getMediaInfoStatus() {
		return mediaInfoStatus;
	}

	public void setMediaInfoStatus(Integer mediaInfoStatus) {
		this.mediaInfoStatus = mediaInfoStatus;
	}

	public Integer getMediaInfoDealState() {
		return mediaInfoDealState;
	}

	public void setMediaInfoDealState(Integer mediaInfoDealState) {
		this.mediaInfoDealState = mediaInfoDealState;
	}

	public JSONObject toMediaFileJson() {
		JSONObject jObj = new JSONObject();
		jObj.put("mediaFileName", StringPatternUtil.convertEmpty(this.getMediaFileName()));
		jObj.put("mediaFileDesc", StringPatternUtil.convertEmpty(this.getMediaFileDesc()));
		jObj.put("filePath", StringPatternUtil.convertEmpty(this.getFilePath()));
		jObj.put("fileId", this.getFileId());
		if(openDataBeautifySwitch!=null&&openDataBeautifySwitch==1) {
			if(this.getMultiple()!=null&&this.getBaseNum()!=null) {
				jObj.put("viewNum", this.getViewNum()*this.getMultiple()+this.getBaseNum());
			}
			
		}else {
			jObj.put("viewNum", this.getViewNum());
		}
		
		jObj.put("fileCover", StringPatternUtil.convertEmpty(this.getFileCover()));
		jObj.put("mediaCreateTime", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(this.getMediaCreateTime()));
		jObj.put("allowComment", this.getAllowComment() == null ? 1 : this.getAllowComment());
		jObj.put("shareUrl", this.getShareUrl());
		jObj.put("isFileDoc", this.getIsFileDoc());
		jObj.put("fileCoverImg",this.getFileCover());
		jObj.put("fileDuration",convertTime(this.getDuration() == null ? 0 : this.getDuration()));
		if (null != getVrFile() && getVrFile()) {
			jObj.put("vrFile", 1);
		} else {
			jObj.put("vrFile", 0);
		}
		return jObj;
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
	private String formatTime(int time) {
		if (time >= 0 && time < 10) {
			return "0" + time;
		}
		return "" + time;
	}
	public Integer getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Integer allowComment) {
		this.allowComment = allowComment;
	}

	public Integer getAutoCheckFlag() {
		return autoCheckFlag;
	}

	public void setAutoCheckFlag(Integer autoCheckFlag) {
		this.autoCheckFlag = autoCheckFlag;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Integer getFileAuthentication() {
		return fileAuthentication;
	}

	public void setFileAuthentication(Integer fileAuthentication) {
		this.fileAuthentication = fileAuthentication;
	}


}
