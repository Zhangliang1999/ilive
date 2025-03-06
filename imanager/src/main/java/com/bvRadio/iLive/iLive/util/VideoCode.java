package com.bvRadio.iLive.iLive.util;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
public class VideoCode {
	private final static Logger log = LoggerFactory.getLogger(VideoCode.class);
	public static synchronized File code(ILiveUploadServer uploadServer, File uploadFile){
		boolean transcodeResult = false;
		try {
			System.out.println("开始转码");
			String uploadFilePath = uploadFile.getAbsolutePath();
			String fileExt = FileUtils.getFileExt(uploadFilePath);
			String transcodeTempFilePath = "";
			fileExt = "mp4";
			transcodeTempFilePath = uploadFile.getParent() + "/" + (System.currentTimeMillis()/1000) + "." + fileExt;
			System.out.println("转码前的路径===================================="+uploadFilePath);
			transcodeResult = FfmpegUtils.transcodeForVideo(uploadFilePath, transcodeTempFilePath);
			System.out.println("=================================="+transcodeResult);
			transcodeResult=true;
			System.out.println("转码结束");
			if (transcodeResult) {
				uploadFile.delete();
				System.out.println("====================2222================"+transcodeTempFilePath);
				uploadFile = new File(transcodeTempFilePath);
			}
	} catch (Exception e) {
		if (null != uploadFile) {
			String uploadFilePath = uploadFile.getAbsolutePath();
			log.error("转码上传出错。uploadFilePath = {} , fileType = {} , serverFilePath = {}", uploadFilePath, e);
		} else {
			log.error("转码上传出错。uploadFile = {} , fileType = {} , serverFilePath = {}", uploadFile,   e);
		}
	}
		return uploadFile;
	}
	
	

}
