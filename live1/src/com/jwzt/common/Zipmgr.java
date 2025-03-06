package com.jwzt.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Zipmgr {
	private String sLog = ""; // 记录日志

	/**
	 * 只适用于.zip
	 * 压缩制定文件/文件夹 ，压缩后的文件类型是zip；可以压缩中文内容的文本文件；
	 * 将文件或文件夹压缩成.zip文件的方法
	 * @param zipBeforeFilePath 压缩前的文件路径，文件或文件夹，如：D:\或D:\win或D:\a.txt
	 * @param zipAfterFileName 压缩后的文件路径及名称，如：D:\yasuo.zip,如果没有给定路径名
	 * 则用当前时间作为压缩文件名
	 * @return 布尔类型值
	 */
	public boolean toZip(String zipBeforeFilePath, String zipAfterFileName) {
		// String zipFileName=zipAfterFileName;//打包后文件名字
		sLog = "cms.common.Zipmgr->toZip";
		try{
			File inputFile = new File(zipBeforeFilePath);
			//检查给定的压缩后的文件路径中是否有文件名，如果没有则取当前系统日期做为文件名
			if(zipAfterFileName.indexOf(".zip")==-1)
			{
				Date da = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String zipName = sdf.format(da)+".zip";
				//截取路径中最后一个字符
				String lastChar=zipAfterFileName.substring(zipAfterFileName.length()-1, zipAfterFileName.length());
				if(lastChar.indexOf("/")>-1||lastChar.indexOf("\\")>-1)
				{
					zipAfterFileName=zipAfterFileName+zipName;
				}
				else
				{
					zipAfterFileName=zipAfterFileName+File.separator+zipName;
				}
			}
			
			org.apache.tools.zip.ZipOutputStream out = new org.apache.tools.zip.ZipOutputStream(
					new FileOutputStream(zipAfterFileName));
			//判断一下要压缩的是文件还是文件夹
			if(inputFile.isDirectory())
			{
				zip(out, inputFile, "");
			}
			else
			{
				zip(out, inputFile, inputFile.getName());
			}
			
			out.close();
			
		} catch (Exception e) {
			sLog += " 压缩文件时出错:";
			sLog += e.toString();
			Logger.log(sLog, 3);
			return false;
		}
		return true;
		
	}

	private void zip(org.apache.tools.zip.ZipOutputStream out, File f,
			String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			//System.out.println("base:"+base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}
	
	
	
/**
 * 检测zip包中文件在服务器上都已存在哪些
 * @param zipFileName
 * @param outputDirectory
 * @return
 */
public ArrayList<String> checkExistZip(String zipFileName, String outputDirectory){
		
		sLog = "cms.common.Zipmgr->unZip";
		ArrayList<String> existFile = new ArrayList<String>();
		try {
			org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(
					zipFileName);
			
			java.util.Enumeration e = zipFile.getEntries();
			org.apache.tools.zip.ZipEntry zipEntry = null;
			createDirectory(outputDirectory, "");
			while (e.hasMoreElements()) {
				
				zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					f.mkdir();
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');
					if (fileName.indexOf("/") != -1) {
						createDirectory(outputDirectory, fileName.substring(0,
								fileName.lastIndexOf("/")));
						fileName = fileName.substring(
								fileName.lastIndexOf("/") + 1, fileName
										.length());
					}
					File f = new File(outputDirectory + File.separator
							+ zipEntry.getName());
					if(f.exists()){
						existFile.add(zipEntry.getName());
					}
				}
			}
			zipFile.close();
			File file = new File(zipFileName);
			boolean bet = file.delete();
			System.out.println("删除压缩包=="+bet);
		} catch (Exception e) {
			sLog += "解压文件时出错:";
			sLog += e.toString();
			Logger.log(sLog, 3);
			return existFile;
		}
		return existFile;
	}
	
/**
 * 只适用于.zip
 * 解压.zip文件的方法，不能解压.rar文件
 * @param zipFileName 要解压的zip文件路径及文件
 * @param outputDirectory 解压后的文件存放路径，此处为文件夹地址,如果给定的路径不存在，则会按给定路径创建一个
 * @return 布尔类型值
 */
public boolean unZip(String zipFileName, String outputDirectory){
	
	sLog = "cms.common.Zipmgr->unZip";
	InputStream in = null;
	FileOutputStream out = null;
	try {
		org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(
				zipFileName);
		
		java.util.Enumeration e = zipFile.getEntries();
		org.apache.tools.zip.ZipEntry zipEntry = null;
		createDirectory(outputDirectory, "");			
		
		while (e.hasMoreElements()) {
			
			zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
			//System.out.println("unziping " + zipEntry.getName());
			if (zipEntry.isDirectory()) {
				String name = zipEntry.getName();
				name = name.substring(0, name.length() - 1);
				File f = new File(outputDirectory + File.separator + name);
				f.mkdir();
				//System.out.println("创建目录：" + outputDirectory+ File.separator + name);
			} else {
				String fileName = zipEntry.getName();
				System.out.println("unXZip==="+fileName);
				fileName = fileName.replace('\\', '/');
				// System.out.println("测试文件1：" +fileName);
				if (fileName.indexOf("/") != -1) {
					createDirectory(outputDirectory, fileName.substring(0,
							fileName.lastIndexOf("/")));
					fileName = fileName.substring(
							fileName.lastIndexOf("/") + 1, fileName
									.length());
				}
				File f = new File(outputDirectory + File.separator
						+ zipEntry.getName());
				if(fileName.indexOf("jquery")!=-1){
					 f = new File(outputDirectory + File.separator
							+ zipEntry.getName()+1);
				}

				f.createNewFile();
				in = zipFile.getInputStream(zipEntry);
				out = new FileOutputStream(f);

				byte[] by = new byte[1024];
				int c;
				while ((c = in.read(by)) != -1) {
					out.write(by, 0, c);
				}
				out.close();
				in.close();
			}
		}			
		zipFile.close();
		File file = new File(zipFileName);
		boolean bet = file.delete();
		System.out.println("删除压缩包=="+bet);
	} catch (Exception e) {
		sLog += "解压文件时出错:";
		sLog += e.toString();
		Logger.log(sLog, 3);
		return false;
	}
	try {
		out.close();
		in.close();
		//System.out.println("************"+in.read());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
	
	return true;
}

	/**
	 * 根据给定路径创建文件夹
	 * @param directory
	 * @param subDirectory
	 */
	private void createDirectory(String directory, String subDirectory) {
		sLog = "cms.common.Zipmgr->createDirectory";
		String dir[];
		File fl = new File(directory);
		try {
			if (subDirectory == "" && fl.exists() != true)
				fl.mkdir();
			else if (subDirectory != "") {
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (subFile.exists() == false)
						subFile.mkdir();
					directory += File.separator + dir[i];
				}
			}
		} catch (Exception e) {
			sLog += "创建createDirectory时出错:";
			sLog += e.toString();
			Logger.log(sLog, 3);
		}
	}
    /**
     * 解压zip包下所有文件及文件夹到指定目录中
	 * 只适用于.zip
	 * 解压.zip文件的方法，不能解压.rar文件
	 * @param zipFileName 要解压的zip文件路径及文件
	 * @param outputDirectory 解压后的文件存放路径，此处为文件夹地址,如果给定的路径不存在，则会按给定路径创建一个
	 * @return 布尔类型值
	 */
	public boolean unZipUnder(String zipFileName, String outputDirectory){
		
		sLog = "cms.common.Zipmgr->unZip";
		InputStream in = null;
		FileOutputStream out = null;
		try {
			org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(
					zipFileName);
			
			//获得 压缩包目录名 如 zipstyle.zip  可获取 zipstyle
			String zipDirectoryName = zipFileName.substring(zipFileName.lastIndexOf("\\") + 1, zipFileName.lastIndexOf("."));
			System.out.println("zipDirectoryName=="+zipDirectoryName);
			java.util.Enumeration e = zipFile.getEntries();
			org.apache.tools.zip.ZipEntry zipEntry = null;
			createDirectory(outputDirectory, "");			
			
			while (e.hasMoreElements()) {
				
				zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
				//System.out.println("unziping " + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					String dir1 = outputDirectory + File.separator + name;
					dir1 = dir1.replaceFirst(zipDirectoryName, "");
					System.out.println("创建目录：" + dir1);
					File f = new File(dir1);
					f.mkdir();
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');
					// System.out.println("测试文件1：" +fileName);
					if (fileName.indexOf("/") != -1) {
						String dir2 = fileName.substring(0,fileName.lastIndexOf("/"));
						dir2 = dir2.replaceFirst(zipDirectoryName, "");
						createDirectory(outputDirectory,dir2);
						fileName = fileName.substring(
								fileName.lastIndexOf("/") + 1, fileName
										.length());
					}
					String dir3 = outputDirectory + File.separator+ zipEntry.getName();
					dir3 = dir3.replaceFirst(zipDirectoryName, "");
					File f = new File(dir3);
					f.createNewFile();
					in = zipFile.getInputStream(zipEntry);
					out = new FileOutputStream(f);

					byte[] by = new byte[1024];
					int c;
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					out.close();
					in.close();
				}
			}
			zipFile.close();
			File file = new File(zipFileName);
			boolean bet = file.delete();
			System.out.println("删除压缩包=="+bet);
		} catch (Exception e) {
			sLog += "解压文件时出错:";
			sLog += e.toString();
			Logger.log(sLog, 3);
			return false;
		}
		return true;
	}
		public void deleteFile(String path) {
		File f = new File(path);
		if(f.isDirectory())
		{
		File[] file = f.listFiles();
		for (File file2 : file) {
		this.deleteFile(file2.toString());
		file2.delete();
		}
		}else
		{
		f.delete();
		}
		f.delete();
		}
		}
