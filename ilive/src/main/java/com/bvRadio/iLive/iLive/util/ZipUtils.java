package com.bvRadio.iLive.iLive.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtils {
	private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);

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
}
