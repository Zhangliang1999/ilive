package com.jwzt.livems.ilive.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import com.jwzt.common.SomsConfigInfo;
import com.jwzt.livems.ilive.MediaFile;
import com.jwzt.livems.ilive.exception.FileScreenException;
import com.jwzt.livems.ilive.util.cmd.CmdExecuter;
import com.jwzt.livems.ilive.util.cmd.IStringGetter;

/**
 * FFMpegUntil
 * <p>
 * 
 * @version 1.0
 */
public class FFMpegUtil implements IStringGetter {

	private int runtime = 0;
	private String ffmpegUri;// ffmpeg
	private String originFileUri;
	private MediaFile mediaFile;

	private enum FFMpegUtilStatus {
		Empty, CheckingFile, GettingRuntime
	};

	private FFMpegUtilStatus status = FFMpegUtilStatus.GettingRuntime;

	/**
	 * @param ffmpegUri
	 * @param originFileUri
	 */
	public FFMpegUtil(String ffmpegUri, String originFileUri, MediaFile mediaFile) {
		this.ffmpegUri = ffmpegUri;
		this.originFileUri = originFileUri;
		this.mediaFile = mediaFile;
	}

	/**
	 * 
	 * @return
	 */
	public int getRuntime() {
		runtime = 0;
		status = FFMpegUtilStatus.GettingRuntime;
		cmd.clear();
		cmd.add(ffmpegUri);
		cmd.add("-i");
		cmd.add(originFileUri);
		CmdExecuter.exec(cmd, this);
		return runtime;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSupported() {
		isSupported = false;
		status = FFMpegUtilStatus.CheckingFile;
		cmd.clear();
		cmd.add(ffmpegUri);
		cmd.add("-i");
		cmd.add(originFileUri);
		CmdExecuter.exec(cmd, this);
		return isSupported;
	}

	private boolean isSupported;

	/**
	 * 
	 * @param imageSavePath
	 * @param screenSize
	 */
	public MediaFile makeScreenCut(String imageSavePath, String screenSize) throws FileScreenException {
		try {
			cmd.clear();
			cmd.add(ffmpegUri);
			cmd.add(" -i ");
			cmd.add(originFileUri);
			cmd.add(" -y ");
			cmd.add(" -f ");
			cmd.add("image2");
			cmd.add(" -ss ");
			cmd.add("2");
			cmd.add(" -t ");
			cmd.add("0.001");
			String basePath = SomsConfigInfo.get("BaseImgRealPath");
			cmd.add(basePath + imageSavePath);
			File file = new File(basePath + imageSavePath);
			if (!file.getParentFile().isDirectory()) {
				file.getParentFile().mkdirs();
			}
			CmdExecuter.exec(cmd, this);
			String httpPrefix = SomsConfigInfo.get("HttpImgPath");
			mediaFile.setFileConver(httpPrefix + imageSavePath);
			return mediaFile;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileScreenException();
		}

	}

	private List<String> cmd = new ArrayList<String>();

	private static int getTimelen(String timelen) {
		int min = 0;
		String strs[] = timelen.split(":");
		if (strs[0].compareTo("0") > 0) {
			min += Integer.valueOf(strs[0]) * 60 * 60;// 秒
		}
		if (strs[1].compareTo("0") > 0) {
			min += Integer.valueOf(strs[1]) * 60;
		}
		if (strs[2].compareTo("0") > 0) {
			min += Math.round(Float.valueOf(strs[2]));
		}
		return min;
	}

	@Override
	public void dealString(String str) {
		// System.out.println("Getter:"+str);
		switch (status) {
		case Empty:
			break;
		case CheckingFile: {
			if (-1 != str.indexOf("Metadata:"))
				this.isSupported = true;
			break;
		}
		case GettingRuntime: {
			Matcher m = Pattern.compile("Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s").matcher(str);
			while (m.find()) {
				int time = getTimelen(m.group(1));
				System.out.println(",视频时长：" + time + ", 开始时间：" + m.group(2) + ",比特率：" + m.group(3) + "kb/s");
				this.getMediaFile().setDuration(time);
				this.getMediaFile().setBiterate(Double.parseDouble(m.group(3)));
				File file = new File(this.originFileUri);
				if (file.exists()) {
					this.getMediaFile().setFileSize((int) (file.length() / 1024L));
					this.getMediaFile().setFileExt(FilenameUtils.getExtension(file.getAbsolutePath()));

				}
			}
			Matcher videoMatcher = Pattern.compile("Video: (.*?), (.*?), (.*?)[,\\s]").matcher(str);
			while (videoMatcher.find()) {
				// System.out.println("编码格式 ===" + videoMatcher.group(1));
				// System.out.println("视频格式 ===" + videoMatcher.group(2));
				System.out.println(" 分辨率 == =" + videoMatcher.group(3));
				this.getMediaFile().setFileWh(videoMatcher.group(3));

			}
			break;
		}
		}// switch
	}

	public MediaFile getMediaFile() {
		return mediaFile;
	}

	public void setMediaFile(MediaFile mediaFile) {
		this.mediaFile = mediaFile;
	}

}