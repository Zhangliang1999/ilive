package com.bvRadio.iLive.iLive.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUtils {
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 新建目录
	 * 
	 * @param strFilePath
	 *            文件夹路径
	 */
	public static boolean mkdirFolder(String strFilePath) {
		boolean bFlag = false;
		try {
			File file = new File(strFilePath);
			if (!file.exists()) {
				bFlag = file.mkdirs();
			}
		} catch (Exception e) {
			log.error("新建目录出错，strFilePath = {}", strFilePath, e);
		}
		return bFlag;
	}

	public static void deleteFileList(File oldFile) {
		if (oldFile != null && oldFile.exists() && oldFile.isDirectory()) {
			File[] oldFileList = oldFile.listFiles();
			if (oldFileList != null) {
				for (int i = 0; i < oldFileList.length; i++) {
					if (oldFileList[i].isFile()) {
						oldFileList[i].delete();
					} else if (oldFileList[i].isDirectory()) {
						deleteFileList(oldFileList[i]);
					}
				}
			}
			oldFile.delete();
		}
	}

	/**
	 * 获取文件保存路径（按日期）。例：2016-03-30 返回 /2016_03/30/****.xxx
	 * 
	 * @param fileName
	 *            文件名
	 * 
	 * @return 文件保存路径
	 */
	public static String getTimeFilePath(String fileName) {
		String timeFIlePath = null;
		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("/yyyy_MM/dd/");
		try {
			timeFIlePath = sdf.format(nowDate);
			String ext = getFileExt(fileName);
			if (!StringUtils.isBlank(ext)) {
				timeFIlePath += System.currentTimeMillis() + "." + ext;
			} else {
				timeFIlePath += System.currentTimeMillis() + ".unk";
			}
		} catch (Exception e) {
			log.error("获取文件目录（按日期）出错", e);
		}
		return timeFIlePath;
	}

	public static String getFileExt(String fileName) {
		String ext = null;
		int lastIndex = fileName.lastIndexOf('.');
		try {
			ext = fileName.substring(lastIndex + 1);
		} catch (Exception e) {
			log.error("获取文件扩展名出错，fileName = {}", fileName, e);
		}
		return ext;
	}

	public static String formatFileSize(long size) {
		if (size > 1073741824) {
			return String.format("%.2f", size / 1073741824.0) + " GB";
		} else if (size > 1048576) {
			return String.format("%.2f", size / 1048576.0) + " MB";
		} else if (size > 1024) {
			return String.format("%.2f", size / 1024.0) + " KB";
		} else {
			return size + " B";
		}
	}

	public static File createTempFile(String tempFilePath, CommonsMultipartFile file) {
		log.info("开始创建临时上传文件：tempFilePath = {}", tempFilePath);
		long start = System.currentTimeMillis();
		File tempFile = new File(tempFilePath);
		try {
			file.transferTo(tempFile);
		} catch (Exception e) {
			log.error("创建临时上传文件出错：tempFilePath = {}", tempFilePath, e);
			return null;
		}
		long end = System.currentTimeMillis();
		log.info("创建临时上传文件耗时：{} ms", end - start);
		return tempFile;
	}

}
