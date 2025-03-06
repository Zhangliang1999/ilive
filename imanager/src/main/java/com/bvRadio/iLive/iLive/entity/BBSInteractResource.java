package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSInteractResource;

@SuppressWarnings("serial")
public class BBSInteractResource extends BaseBBSInteractResource implements java.io.Serializable {

	public String getFileRealPath() {
		ILiveUploadServer uploadServer = getiLiveUploadServer();
		if (null != uploadServer && !StringUtils.isBlank(getFilePath())) {
			String fileRootUrl = uploadServer.getImageRootUrl();
			return fileRootUrl + getFilePath();
		} else {
			return null;
		}
	}

	public String getThumbImgFileRealPath() {
		ILiveUploadServer uploadServer = getiLiveUploadServer();
		if (null != uploadServer && !StringUtils.isBlank(getThumbImgFilePath())) {
			String fileRootUrl = uploadServer.getImageRootUrl();
			return fileRootUrl + getThumbImgFilePath();
		} else {
			return null;
		}
	}

	public BBSInteractResource() {
		super();
	}

	public BBSInteractResource(Integer id) {
		super(id);
	}

	public BBSInteractResource(Integer id, Integer interactId, Integer interactType, String filePath, Timestamp createTime,
			ILiveUploadServer iLiveUploadServer) {
		super(id, interactId, interactType, filePath,  createTime, iLiveUploadServer);
	}

}
