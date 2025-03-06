package com.jwzt.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZipUtils {
	private static final Logger log = LogManager.getLogger();

	public static List<File> uncompress(File zipFile, String uncompressPath) {
		log.info("解压 ,zipFile = {}，uncompressPath = {}", zipFile.getAbsolutePath(), uncompressPath);
		List<File> bufferInList = null;
		try {
			if (null != zipFile) {
				log.info("解压开始,zipFile = {}，uncompressPath = {}", zipFile.getAbsolutePath(), uncompressPath);
				long startTime = System.currentTimeMillis();
				ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
				BufferedInputStream bufferIn = new BufferedInputStream(zipIn);
				File uncompressFile = null;
				ZipEntry entry;
				try {
					if (null != uncompressPath) {
						if (uncompressPath.endsWith("/")) {
							uncompressPath += "/";
						}
						bufferInList = new ArrayList<File>();
						while ((entry = zipIn.getNextEntry()) != null && !entry.isDirectory()) {
							uncompressFile = new File(uncompressPath, entry.getName());
							if (!uncompressFile.exists()) {
								(new File(uncompressFile.getParent())).mkdirs();
							}
							FileOutputStream out = new FileOutputStream(uncompressFile);
							BufferedOutputStream bufferOut = new BufferedOutputStream(out);
							int b;
							while ((b = bufferIn.read()) != -1) {
								bufferOut.write(b);
							}
							bufferOut.close();
							out.close();
							bufferInList.add(uncompressFile);
							log.info("解压成功,uncompressFile = {}", uncompressFile);
						}
						if (null != bufferIn) {
							bufferIn.close();
						}
						if (null != zipIn) {
							zipIn.close();
						}
					}
					long endTime = System.currentTimeMillis();
					log.info("解压耗费时间： {} ms", endTime - startTime);
				} catch (IOException e) {
					log.error("解压出错,zipFile = {}", zipFile, e);
				}
			}
		} catch (FileNotFoundException e) {
			log.error("解压出错,zipFile = {}", zipFile, e);
		}
		return bufferInList;
	}

	/**
	 * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
	 * 
	 * @param sourceFilePath
	 *            :待压缩的文件路径
	 * @param zipFilePath
	 *            :压缩后存放路径
	 * @param fileName
	 *            :压缩后文件的名称
	 * @return
	 */
	public static boolean compress(List<File> sourceFileList, String compressPath) {
		boolean flag = false;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {
			File zipFile = new File(compressPath);
			if (zipFile.exists()) {
				log.info("文件已存在： compressPath = {}", compressPath);
			} else {
				if (null == sourceFileList || sourceFileList.size() == 0) {
					log.info("待压缩的文件列表里面不存在文件，无需压缩.");
				} else {
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFileList.size(); i++) {
						File sourceFile = sourceFileList.get(i);
						// 创建ZIP实体，并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(sourceFile);
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
					}
					flag = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 关闭流
			try {
				if (null != bis) {
					bis.close();
				}
				if (null != zos) {
					zos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return flag;
	}
}
