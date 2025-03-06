package com.bvRadio.iLive.iLive.entity.vo;

import com.bvRadio.iLive.iLive.entity.ILiveHistoryVideo;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;

public class WebILiveFleVo {
	
	private ILiveMediaFile iLiveMediaFile;
	
	private ILiveHistoryVideo iLiveHistoryVideo;

	public ILiveMediaFile getiLiveMediaFile() {
		return iLiveMediaFile;
	}

	public void setiLiveMediaFile(ILiveMediaFile iLiveMediaFile) {
		this.iLiveMediaFile = iLiveMediaFile;
	}

	public ILiveHistoryVideo getiLiveHistoryVideo() {
		return iLiveHistoryVideo;
	}

	public void setiLiveHistoryVideo(ILiveHistoryVideo iLiveHistoryVideo) {
		this.iLiveHistoryVideo = iLiveHistoryVideo;
	}
	
	
	
}
