package com.jwzt.livems.ilive;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jwzt.common.SomsConfigInfo;
import com.jwzt.livems.ilive.exception.DbUpdaterException;
import com.jwzt.livems.ilive.exception.FileScreenException;
import com.jwzt.livems.ilive.util.FFMpegUtil;
import com.jwzt.livems.utils.LiveBillingUtils;

/**
 * 
 * @author administrator
 *
 */
public enum FFmpegUtils {

	INSTANCE;

	public boolean process(MediaFile mediaFile) throws FileNotFoundException, DbUpdaterException, FileScreenException {
		String realPath = SomsConfigInfo.get("BaseVodRealPath");
		String completePath = realPath + mediaFile.getFilePath();
		File file = new File(completePath);
		if (file.exists()) {
			FFMpegUtil ffmpeg = new FFMpegUtil("ffmpeg", file.getAbsolutePath(), mediaFile);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMdd/");
			String imgPath = sdf.format(new Date());
			imgPath = imgPath + System.currentTimeMillis() + ".jpg";
			MediaFile makeScreenCut = ffmpeg.makeScreenCut(imgPath, null);
			ILiveMediaFileMgr.INSTANCE.updateILiveMediaFile(makeScreenCut);
			try {
				Long fileId = makeScreenCut.getFileId();
				Integer enterpriseId = ILiveMediaFileMgr.INSTANCE.getEnterpriseId(fileId);
				Integer cer=ILiveMediaFileMgr.INSTANCE.getEnterpriseCer(enterpriseId);
				System.out.println("企业认证状态为："+cer);
				Long enterpriseRemainingStorage = LiveBillingUtils.getEnterpriseRemainingStorage(enterpriseId, cer);
				Integer fileSize = makeScreenCut.getFileSize();
				if (fileSize > enterpriseRemainingStorage) {
					ILiveMediaFileMgr.INSTANCE.updateIsMediaType(fileId, 1);
				} else {
					ILiveMediaFileMgr.INSTANCE.updateIsMediaType(fileId, 0);
					LiveBillingUtils.updateEnterpriseProductInfo(enterpriseId,
							LiveBillingUtils.ENTERPRISE_USE_TYPE_Store, fileSize, cer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return true;
		} else {
			System.out.println(file.getAbsolutePath() + "不存在");
			throw new FileNotFoundException(file.getAbsolutePath() + "不存在");
		}

	}

}
