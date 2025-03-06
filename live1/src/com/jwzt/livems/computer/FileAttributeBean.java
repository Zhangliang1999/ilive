package com.jwzt.livems.computer;

/**
 * 
 * 文件属性的封装
 * 
 * @author 赵飞洋
 * 
 */
public class FileAttributeBean {
	private int file_qiepian = 0;
	private int file_mp4 = 0;
	private String file_img;
	private String file_name;
	private String file_updateTime;
	private String file_type = "文件夹";
	private String file_size;
	private String file_path;

	public String getFile_path() {
		return file_path;
	}

	public int getFile_qiepian() {
		return file_qiepian;
	}

	public void setFile_qiepian(int fileQiepian) {
		file_qiepian = fileQiepian;
	}

	public int getFile_mp4() {
		return file_mp4;
	}

	public void setFile_mp4(int fileMp4) {
		file_mp4 = fileMp4;
	}

	public void setFile_path(String filePath) {
		file_path = filePath;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String fileName) {
		file_name = fileName;
	}

	public String getFile_updateTime() {
		return file_updateTime;
	}

	public void setFile_updateTime(String fileUpdateTime) {
		file_updateTime = fileUpdateTime;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String fileType) {
		file_type = fileType;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String fileSize) {
		file_size = fileSize;
	}

	public String getFile_img() {
		return file_img;
	}

	public void setFile_img(String fileImg) {
		file_img = fileImg;
	}

}
