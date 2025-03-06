package com.bvRadio.iLive.iLive.entity;

import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveMediaFile;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

public class ILiveMediaFile extends BaseILiveMediaFile {

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
	 * 评论信息
	 */
	private Set<ILiveMediaFileComments> iLiveMediaFileCommentsSet;

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
		jObj.put("viewNum", this.getViewNum());
		return jObj;
	}

}
