package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.entity.ProgressStatusPoJo;

public interface QEMng {

	public String makeFailedXml(String type , String errordesc);
	public String makeLoginXml(String uploadUrl, String uploadPath, String catalogueUrl,String newsEditURL,String vodEditURL,String progressQueryUrl,String vodPlayUrl);
	public String makeUploadFileSuccXml(long fileSize);
	public ProgressStatusPoJo getUpdateTask(String taskUUID);
	public Long addUpdateTask(ProgressStatusPoJo progressStatusPoJo);
	public String makeFinishNotifySuccXml(long fileSize);
	public String makeFileSizeSuccXml(long fileSize);
	public String makeUploadNotifySuccXml();
	public String makeMergeSuccXml(long fileSize);
	public String makeDownloadXml(List<AppMediaFile> appMediaFileList);
	public String makeServerXml(String susername, String spassword, String sipaddr, String sport, String sserverurl);
}
